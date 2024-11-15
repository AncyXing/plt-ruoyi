package dataprocessor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.pltool.domain.entity.Lexicon;
import com.example.pltool.domain.entity.LexiconWord;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.domain.entity.WordSentence;
import com.example.pltool.service.IWordSentenceService;
import com.example.pltool.service.language.LexiconService;
import com.example.pltool.service.language.LexiconWordService;
import com.example.pltool.service.language.WordService;
import com.ruoyi.PltRuoYiApplication;
import com.ruoyi.common.utils.StringUtils;
import domain.ComposeData;
import domain.ParseData;
import domain.SentenceData;
import domain.TransData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author yangxing
 * @date 2024/11/14
 */
//@SpringBootTest(classes = DataProcessorText.Application.class,
//        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringBootTest(classes = PltRuoYiApplication.class)
public class DataProcessorText {
    @Autowired
    private WordService wordService;

    @Autowired
    private IWordSentenceService wordSentenceService;

    @Autowired
    private LexiconService lexiconService;

    @Autowired
    private LexiconWordService lexiconWordService;


    @Test
    void testDataInsert() throws Exception {
        String fileName = "CET4_2.json";
        // 读取文件
        List<ComposeData> composeData = readFileAndParse(fileName);
        // 写入数据库
        writeDataToDb(composeData);
    }

    void writeDataToDb(List<ComposeData> composeDataList) {
        List<Word> wordList = new ArrayList<>();
        List<WordSentence> wordSentences = new ArrayList<>();
        List<Lexicon> lexiconList = new ArrayList<>();
        Set<String> lexiconSet = new HashSet<>();
        List<LexiconWord> lexiconWordList = new ArrayList<>();
        composeDataList.forEach(e -> {
            wordList.add(e.getWord());
            if (!CollectionUtils.isEmpty(e.getWordSentence())) {
                wordSentences.addAll(e.getWordSentence());
            }
            if (!lexiconSet.contains(e.getLexicon().getName())) {
                lexiconSet.add(e.getLexicon().getName());
                lexiconList.add(e.getLexicon());
            }
            lexiconWordList.add(e.getLexiconWord());
        });
        wordService.saveBatch(wordList);
        wordSentenceService.saveBatch(wordSentences);
        lexiconService.saveBatch(lexiconList);
        lexiconWordService.saveBatch(lexiconWordList);
    }


    private List<ComposeData> readFileAndParse(String fileName) {
        List<ComposeData> composeDataList = new ArrayList<>();
        String filePath = this.getClass().getClassLoader().getResource(fileName).getPath();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                JSONObject rowJson = JSON.parseObject(line);
                String word = rowJson.getString("headWord");
                String lexicon = rowJson.getString("bookId");
                JSONObject contentStr = rowJson.getJSONObject("content")
                        .getJSONObject("word").getJSONObject("content");
                if (StringUtils.isEmpty(contentStr)) {
                    continue;
                }
                String sentenceStr = StringUtils.isEmpty(contentStr
                        .getJSONObject("sentence")) ? "" : contentStr.getJSONObject("sentence")
                        .getString("sentences");
                String transStr = contentStr.getString("trans");
                if (StringUtils.isAnyEmpty(sentenceStr, transStr)) {
                    continue;
                }
                // 字符串解析为json
                List<SentenceData> sentenceList = JSON.parseArray(sentenceStr, SentenceData.class);
                List<TransData> transDataList = JSON.parseArray(transStr, TransData.class);
                ParseData parseData = new ParseData();
                parseData.setSentences(sentenceList);
                parseData.setTrans(transDataList);
                parseData.setWordHead(word);
                parseData.setLexicon(lexicon);
                ComposeData composeData = buildComposeData(parseData);
                composeDataList.add(composeData);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(composeDataList.size());
        return composeDataList;
    }

    ComposeData buildComposeData(ParseData parseData) {
        ComposeData composeData = new ComposeData();
        Lexicon lexicon = buildLexicon(parseData);
        Word word = buildWord(parseData);
        List<WordSentence> wordSentences = buildWordSentence(parseData, word.getUuid());
        LexiconWord lexiconWord = buildLexiconWord(parseData, word.getUuid(), lexicon.getUuid());
        composeData.setLexicon(lexicon);
        composeData.setWord(word);
        composeData.setWordSentence(wordSentences);
        composeData.setLexiconWord(lexiconWord);
        return composeData;
    }

    String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    Word buildWord(ParseData parseData) {
        // 拼接翻译和词性
        StringBuffer pos = new StringBuffer();
        StringBuffer translate = new StringBuffer();
        parseData.getTrans().forEach(e -> {
            translate.append(e.getPos()).append(". ").append(e.getTranCn()).append(",");
        });
        String translation = translate.toString().substring(0, translate.lastIndexOf(","));
        return new Word().setWord(parseData.getWordHead())
                .setUuid(generateUUID())
                .setCreateUserId(1L)
                .setTranslation(translation);
    }

    Lexicon buildLexicon(ParseData parseData) {
        return new Lexicon().setName(parseData.getLexicon())
                .setUuid(generateUUID())
                .setCreateUserId(1L)
                .setLanguage("英语");
    }

    List<WordSentence> buildWordSentence(ParseData parseData, String wordUUID) {
        List<WordSentence> wordSentenceList = new ArrayList<>();
        parseData.getSentences().forEach(e -> {
            WordSentence wordSentence = new WordSentence();
            wordSentence.setUuid(generateUUID())
                    .setWordUuid(wordUUID)
                    .setSentenceContent(e.getSContent())
                    .setTranslateContent(e.getSCn());
            wordSentenceList.add(wordSentence);
        });
        return wordSentenceList;
    }

    LexiconWord buildLexiconWord(ParseData parseData, String wordUUID, String lexiconUUID) {
        return new LexiconWord().setWordUuid(wordUUID)
                .setLexiconUuid(lexiconUUID)
                .setUuid(generateUUID());
    }


    public static class Application {
    }
}
