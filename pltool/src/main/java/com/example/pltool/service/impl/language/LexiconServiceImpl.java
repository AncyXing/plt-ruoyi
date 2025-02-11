package com.example.pltool.service.impl.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.example.pltool.domain.dto.language.lexicon.*;
import com.example.pltool.service.IWordSentenceService;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.entity.*;
import com.example.pltool.mapper.LexiconMapper;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.LabelService;
import com.example.pltool.service.language.LexiconService;
import com.example.pltool.service.language.LexiconWordService;
import com.example.pltool.service.language.WordService;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 词库表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Slf4j
@Service
public class LexiconServiceImpl extends ServiceImpl<LexiconMapper, Lexicon>
        implements LexiconService {

    @Autowired
    private WordService wordService;

    @Autowired
    private LexiconService lexiconService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private WordService wordNewService;


    @Autowired
    private LexiconWordService lexiconWordService;

    @Autowired
    private IWordSentenceService wordSentenceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createLexicon(LexiconData lexiconData) {
        List<ComposeData> composeData = readFileAndParse(lexiconData.getFile());
        writeDataToDb(composeData);
        return "创建成功";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String removeLexicon(Long[] ids) {
        Assert.notEmpty(ids, "请求参数错误");
        List<Lexicon> lexicons = lexiconService.listByIds(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(lexicons)) {
            return "删除失败";
        }
        List<String> lexiconUUIdList =
                lexicons.stream().map(Lexicon::getUuid).collect(Collectors.toList());
        // 删除词库下的单词
        QueryWrapper<LexiconWord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("lexion_uuid", lexiconUUIdList);
        lexiconWordService.remove(queryWrapper);
        // 删除词库
        lexiconService.removeBatchByIds(Arrays.asList(ids));
        // 删除词库与标签的关联关系
        QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
        labelRefQueryWrapper.in("ref_uuid", lexiconUUIdList);
        labelRefService.remove(labelRefQueryWrapper);
        return "删除成功";
    }

    @Override
    public LexiconShowData getInfo(Long id) {
        Lexicon lexicon = getById(id);
        Assert.notNull(lexicon, "未获取到词库信息");
        LexiconShowData lexiconData = new LexiconShowData();
        BeanUtils.copyProperties(lexicon, lexiconData);
        Map<String, List<Label>> labelNameMap = getLabelNameList(Collections.singletonList(lexicon));
        List<Label> labelNameList = labelNameMap.get(lexicon.getUuid());
        lexiconData.setLabelList(labelNameList);
        return lexiconData;
    }

    @Override
    public List<LexiconShowData> convertLexiconList(List<Lexicon> lexicons) {
        if (CollectionUtils.isEmpty(lexicons)) {
            return Collections.emptyList();
        }
        List<LexiconShowData> result = new ArrayList<>();
        Optional.ofNullable(lexicons).ifPresent(e -> {
            List<LexiconShowData> lexiconDataList = e.stream().map(lex -> {
                LexiconShowData lexiconData = new LexiconShowData();
                BeanUtils.copyProperties(lex, lexiconData);
                return lexiconData;
            }).collect(Collectors.toList());

            Map<String, List<Label>> labelNameMap = getLabelNameList(lexicons);
            lexiconDataList.forEach(l -> {
                l.setLabelList(labelNameMap.get(l.getUuid()));
            });
            result.addAll(lexiconDataList);
        });
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateLexicon(LexiconShowData lexiconShowData) {
        // 更新词库
        Lexicon updateLexicon = new Lexicon();
        BeanUtils.copyProperties(lexiconShowData, updateLexicon);
        lexiconService.updateById(updateLexicon);
        // 更新词库对应的label标签，更新名称
        List<Label> labelList = lexiconShowData.getLabelList();
        // 需要新增的标签
        List<Label> needAddLabelList = new ArrayList<>();
        List<Label> needUpadateLabelList = new ArrayList<>();
        labelList.forEach(e -> {
            if (Objects.nonNull(e.getId())) {
                needUpadateLabelList.add(e);
            } else {
                e.setUuid(UUID.randomUUID().toString().replace("-", ""));
                e.setCreateUserId(lexiconShowData.getCreateUserId());
                needAddLabelList.add(e);
            }
        });
        // 需要更新的标签
        if (!CollectionUtils.isEmpty(needUpadateLabelList)) {
            labelService.updateBatchById(needUpadateLabelList);
        }
        if (!CollectionUtils.isEmpty(needAddLabelList)) {
            labelService.saveBatch(needAddLabelList);
            List<LabelRef> labelRefList = needAddLabelList.stream().map(e -> {
                LabelRef labelRef = new LabelRef();
                labelRef.setUuid(UUID.randomUUID().toString());
                // 0表示词库，1表示单词
                labelRef.setRefType(0);
                labelRef.setRefUuid(lexiconShowData.getUuid());
                labelRef.setLabelUuid(e.getUuid());
                labelRef.setCreateUserId(lexiconShowData.getCreateUserId());
                return labelRef;
            }).collect(Collectors.toList());
            labelRefService.saveBatch(labelRefList);
        }
        if (!CollectionUtils.isEmpty(lexiconShowData.getDeleteTags())) {
            List<String> lableUUIDList =
                    lexiconShowData.getDeleteTags().stream().map(Label::getUuid).collect(Collectors.toList());
            // 删除标签与词库的关联关系，标签可以不删除
            // 删除词库与标签的关联关系
            QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
            labelRefQueryWrapper.in("label_uuid", lableUUIDList)
                    .and(e -> e.eq("ref_uuid", lexiconShowData.getUuid()));
            labelRefService.remove(labelRefQueryWrapper);
        }
        return "修改成功";
    }

    @Override
    public List<Label> getLabelOfLexicon(String lexiconUUID) {
        // 词库的标签
        QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
        labelRefQueryWrapper.eq("ref_uuid", lexiconUUID);
        List<LabelRef> labelRefList = labelRefService.list(labelRefQueryWrapper);
        Map<String, List<LabelRef>> labelRefGroupByLabelUUID =
                labelRefList.stream().collect(Collectors.groupingBy(LabelRef::getLabelUuid));
        if (CollectionUtils.isEmpty(labelRefGroupByLabelUUID.keySet())) {
            return Collections.emptyList();
        }
        QueryWrapper<Label> labelQueryWrapper = new QueryWrapper<>();
        labelQueryWrapper.in("uuid", labelRefGroupByLabelUUID.keySet());
        return labelService.list(labelQueryWrapper);
    }


    private Map<String, List<Label>> getLabelNameList(List<Lexicon> lexicons) {
        List<String> lexiconUUIdList = Optional.ofNullable(lexicons).orElse(Collections.emptyList())
                .stream().map(Lexicon::getUuid).collect(Collectors.toList());
        Assert.notEmpty(lexiconUUIdList, "请求参数错误");
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ref_uuid", lexiconUUIdList);

        Map<String, List<LabelRef>> labelRefMap =
                Optional.ofNullable(labelRefService.list(queryWrapper)).orElse(Collections.emptyList())
                        .stream().collect(Collectors.groupingBy(LabelRef::getRefUuid));

        Map<String, List<String>> labelUUIDMap = new HashMap<>();
        Optional.of(labelRefMap).ifPresent(e -> {
            e.forEach((k, v) -> {
                List<String> labelUUIDs = new ArrayList<>();
                v.forEach(labelRef -> {
                    labelUUIDs.add(labelRef.getLabelUuid());
                });
                labelUUIDMap.put(k, labelUUIDs);
            });
        });
        Map<String, List<Label>> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(labelUUIDMap)) {
            QueryWrapper<Label> queryLabel = new QueryWrapper<>();
            List<String> labelUUIDList = new ArrayList<>();
            labelUUIDMap.values().forEach(labelUUIDList::addAll);
            queryLabel.in("uuid", labelUUIDList);
            List<Label> list = labelService.list(queryLabel);
            Optional.ofNullable(list).ifPresent(e -> {
                Map<String, Label> labelMap =
                        e.stream().collect(Collectors.toMap(Label::getUuid, Function.identity()));
                labelUUIDMap.forEach((k, v) -> {
                    List<Label> labelNames = new ArrayList<>();
                    v.forEach(u -> {
                        if (labelMap.containsKey(u)) {
                            labelNames.add(labelMap.get(u));
                        }
                    });
                    result.put(k, labelNames);
                });
            });
        }
        return result;
    }


    private List<ComposeData> readFileAndParse(MultipartFile file) {
        List<ComposeData> composeDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                com.alibaba.fastjson2.JSONObject rowJson = JSON.parseObject(line);
                String word = rowJson.getString("headWord");
                String lexicon = rowJson.getString("bookId");
                com.alibaba.fastjson2.JSONObject contentStr = rowJson.getJSONObject("content")
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
            log.error(e.getMessage());
        }
        return composeDataList;
    }

    private ComposeData buildComposeData(ParseData parseData) {
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

    private Word buildWord(ParseData parseData) {
        // 拼接翻译和词性
        StringBuffer pos = new StringBuffer();
        StringBuffer translate = new StringBuffer();
        parseData.getTrans().forEach(e -> {
            translate.append(e.getPos()).append(". ").append(e.getTranCn()).append(",");
        });
        String translation = translate.toString().substring(0, translate.lastIndexOf(","));
        return new Word().setWord(parseData.getWordHead())
                .setUuid(UUID.randomUUID().toString())
                .setCreateUserId(1L)
                .setTranslation(translation);
    }

    private Lexicon buildLexicon(ParseData parseData) {
        return new Lexicon().setName(parseData.getLexicon())
                .setUuid(UUID.randomUUID().toString())
                .setCreateUserId(1L)
                .setLanguage("英语");
    }

    private List<WordSentence> buildWordSentence(ParseData parseData, String wordUUID) {
        List<WordSentence> wordSentenceList = new ArrayList<>();
        parseData.getSentences().forEach(e -> {
            WordSentence wordSentence = new WordSentence();
            wordSentence.setUuid(UUID.randomUUID().toString())
                    .setWordUuid(wordUUID)
                    .setSentenceContent(e.getSContent())
                    .setTranslateContent(e.getSCn());
            wordSentenceList.add(wordSentence);
        });
        return wordSentenceList;
    }

    private LexiconWord buildLexiconWord(ParseData parseData, String wordUUID, String lexiconUUID) {
        return new LexiconWord().setWordUuid(wordUUID)
                .setLexiconUuid(lexiconUUID)
                .setUuid(UUID.randomUUID().toString());
    }

   private void writeDataToDb(List<ComposeData> composeDataList) {
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
        List<String> wordUUIds = wordService.batchInsertInChunks(wordList, 1000, 1L)
                .stream()
                .map(Word::getUuid)
                .collect(Collectors.toList());
        List<WordSentence> filteredWordSentences = wordSentences.stream()
                .filter(e -> wordUUIds.contains(e.getWordUuid()))
                .collect(Collectors.toList());
        wordSentenceService.saveBatch(filteredWordSentences);
        lexiconService.saveBatch(lexiconList);
        List<LexiconWord> filteredLexiconWords = lexiconWordList.stream()
                .filter(e -> wordUUIds.contains(e.getWordUuid()))
                .collect(Collectors.toList());
        lexiconWordService.saveBatch(filteredLexiconWords);
    }
}
