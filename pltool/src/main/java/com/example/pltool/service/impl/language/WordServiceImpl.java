package com.example.pltool.service.impl.language;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.language.word.WordShowData;
import com.example.pltool.domain.entity.*;
import com.example.pltool.mapper.WordMapper;
import com.example.pltool.service.IWordSentenceService;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.LabelService;
import com.example.pltool.service.language.LexiconService;
import com.example.pltool.service.language.LexiconWordService;
import com.example.pltool.service.language.WordService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.file.ParseUtils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 单词表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Slf4j
@Service("wordService")
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private LexiconWordService lexiconWordService;

    @Autowired
    private LexiconService lexiconService;

    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private IWordSentenceService wordSentenceService;

    @Override
    public List<WordShowData> searchWordByCN(String searchCn) {
        List<Word> wordList = wordMapper.searchWordByCN(searchCn);
        if (CollectionUtils.isEmpty(wordList)) {
            return Collections.emptyList();
        }
        // 查询单词对应的词库
        List<String> wordUUIDList = wordList.stream().map(Word::getUuid).collect(Collectors.toList());

        QueryWrapper<LexiconWord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("word_uuid", wordUUIDList);
        List<LexiconWord> lexiconWords = lexiconWordService.list(queryWrapper);

        Map<String, List<LexiconWord>> lexiconWordGroupByLexiconUUID =
                lexiconWords.stream().collect(Collectors.groupingBy(LexiconWord::getLexiconUuid));

        Set<String> lexiconUUIDSet = lexiconWordGroupByLexiconUUID.keySet();

        Map<String, List<Lexicon>> lexiconMapGroupByUUID = new HashMap<>();
        Map<String, List<LabelRef>> labelRefGroupByLexiconUUID = new HashMap<>();
        Map<String, List<LexiconWord>> lexiconWordGroupByWordUUID = new HashMap<>();
        Map<String, List<Label>> labelGroupByUUID = new HashMap<>();

        if (!CollectionUtils.isEmpty(lexiconUUIDSet)) {
            QueryWrapper<Lexicon> lexiconQueryWrapper = new QueryWrapper<>();
            lexiconQueryWrapper.in("uuid", lexiconUUIDSet);
            List<Lexicon> lexicons = lexiconService.list(lexiconQueryWrapper);
            lexiconMapGroupByUUID
                    .putAll(lexicons.stream().collect(Collectors.groupingBy(Lexicon::getUuid)));

            // 词库的标签
            QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
            labelRefQueryWrapper.in("ref_uuid", lexiconUUIDSet);
            List<LabelRef> labelRefList = labelRefService.list(labelRefQueryWrapper);
            Map<String, List<LabelRef>> labelRefGroupByLabelUUID =
                    labelRefList.stream().collect(Collectors.groupingBy(LabelRef::getLabelUuid));
            labelRefGroupByLexiconUUID
                    .putAll(labelRefList.stream().collect(Collectors.groupingBy(LabelRef::getRefUuid)));
            if (!CollectionUtils.isEmpty(labelRefGroupByLabelUUID)) {
                QueryWrapper<Label> labelQueryWrapper = new QueryWrapper<>();
                labelQueryWrapper.in("uuid", labelRefGroupByLabelUUID.keySet());
                List<Label> labelList = labelService.list(labelQueryWrapper);
                labelGroupByUUID.putAll(labelList.stream().collect(Collectors.groupingBy(Label::getUuid)));
            }
            lexiconWordGroupByWordUUID
                    .putAll(lexiconWords.stream().collect(Collectors.groupingBy(LexiconWord::getWordUuid)));
        }

        return wordList.stream().map(e -> {
            List<String> lexiconNameList = new ArrayList<>();
            List<String> labelNameList = new ArrayList<>();
            List<LexiconWord> lexiconWordList = lexiconWordGroupByWordUUID.get(e.getUuid());
            if (lexiconWordGroupByWordUUID.containsKey(e.getUuid())) {
                List<String> lexiconUUIDList =
                        lexiconWordList.stream().map(LexiconWord::getLexiconUuid).collect(Collectors.toList());
                lexiconUUIDList.forEach(l -> {
                    if (lexiconMapGroupByUUID.containsKey(l)) {
                        lexiconNameList.add(lexiconMapGroupByUUID.get(l).get(0).getName());
                    }
                    if (labelRefGroupByLexiconUUID.containsKey(l)) {
                        String labelUuid = labelRefGroupByLexiconUUID.get(l).get(0).getLabelUuid();
                        labelNameList.add(labelGroupByUUID.get(labelUuid).get(0).getName());
                    }
                });
            }
            WordShowData wordShowData = new WordShowData();
            BeanUtils.copyProperties(e, wordShowData);
            return wordShowData;
        }).collect(Collectors.toList());
    }

    @Override
    public WordShowData getOneWord(Long userId, String lexiconUUID, int index) {
        // 查询词库标签
        Word word = wordMapper.getRandomWordOfUser(userId, lexiconUUID, (long) index);
        Assert.notNull(word, "未查询到单词数据");
        WordShowData wordShowData = new WordShowData();
        BeanUtils.copyProperties(word, wordShowData);
        return wordShowData;
    }

    @Override
    public WordShowData getOneWordInCollection(Long userId, String collectionUUId, int index) {
        // 查询词库标签
        Word word = wordMapper.getRandomWordInCollectionOfUser(userId, collectionUUId, (long) index);
        Assert.notNull(word, "未查询到单词数据");
        WordShowData wordShowData = new WordShowData();
        BeanUtils.copyProperties(word, wordShowData);
        List<WordSentence> wordSentenceList =
                wordSentenceService.getSentencesByWordUUId(word.getUuid());
        if (!CollectionUtils.isEmpty(wordSentenceList)) {
            wordShowData.setSentenceList(wordSentenceList);
        }
        return wordShowData;
    }

    @Override
    public WordShowData getWordInfo(String wordUuid) {
        // 查询词库标签
        Word word = getWordByUUID(wordUuid);
        Assert.notNull(word, "未查询到单词数据");
        WordShowData wordShowData = new WordShowData();
        BeanUtils.copyProperties(word, wordShowData);
        return wordShowData;
    }

    @Override
    public Word getWordByUUID(String uuid) {
        QueryWrapper<Word> wordQuery = new QueryWrapper<>();
        wordQuery.eq("uuid", uuid);
        return getOne(wordQuery);
    }

    @Override
    public List<Word> getWordListByUUID(List<String> uuidList) {
        QueryWrapper<Word> wordQuery = new QueryWrapper<>();
        wordQuery.in("uuid", uuidList);
        return list(wordQuery);
    }

    @Override
    public List<WordShowData> searchWord(String word) {
        QueryWrapper<Word> wordQuery = new QueryWrapper<>();
        wordQuery.like("word", word);
        List<Word> wordList = list(wordQuery);
        if (CollectionUtils.isEmpty(wordList)) {
            return Collections.emptyList();
        }
        List<WordShowData> wordShowDataList = new ArrayList<>();
        wordList.forEach(e -> {
            WordShowData wordShowData = new WordShowData();
            BeanUtils.copyProperties(e, wordShowData);
            wordShowDataList.add(wordShowData);
        });
        return wordShowDataList;
    }

    @Override
    public JSONArray parseUploadFile(MultipartFile file) {
        try {
            String jsonStr = ParseUtils.parseUploadFile(file);
            return JSONUtil.parseArray(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String importWords(List<Word> wordList, Boolean isUpdateSupport, Long userId) {
        if (CollectionUtils.isEmpty(wordList)) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Word word : wordList) {
            try {
                word.setCreateUserId(userId);
                QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
                wordQueryWrapper.eq("word", word.getWord());
                wordQueryWrapper.eq("create_user_id", userId);
                // 验证是否存在这个单词
                Word wordFromDb = getOne(wordQueryWrapper);
                if (Objects.isNull(wordFromDb)) {
                    save(word);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + word.getWord() + " 导入成功");
                } else if (isUpdateSupport) {
                    word.setId(wordFromDb.getId());
                    this.updateById(word);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + word.getWord() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + word.getWord() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + word.getWord() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<Word> batchInsertInChunks(List<Word> wordList, int chunkSize, Long userId) {
        log.info("共{}条数据", wordList.size());
        List<List<Word>> chunks = partitionList(wordList, chunkSize);
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        StringBuilder msg = new StringBuilder();
        List<Word> resultList = new ArrayList<>();
        for (List<Word> chunk : chunks) {
            QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
            List<String> checktWordList = new ArrayList<>();
            chunk.forEach(e -> {
                checktWordList.add(e.getWord());
            });
            // 对这批进行查询过滤去重，再批量保存
            wordQueryWrapper.in("word", checktWordList);
            wordQueryWrapper.eq("create_user_id", userId);
            // 验证是否存在这个单词
            List<Word> wordListFromDb = wordMapper.selectList(wordQueryWrapper);
            List<String> wordContent = wordListFromDb.stream().map(Word::getWord).collect(Collectors.toList());
            List<Word> filteredData = chunk.stream()
                    .filter(e -> !wordContent.contains(e.getWord()))
                    .collect(Collectors.toList());
            failureNum += chunk.size() - filteredData.size();
            if (!CollectionUtils.isEmpty(filteredData)) {
                successNum += filteredData.size();
                resultList.addAll(filteredData);
                wordMapper.batchInsertWord(filteredData);
            }
        }
        if (failureNum > 0) {
            failureMsg.append("<br/>").append(failureNum).append("条数据 导入失败");
        }
        if (successNum > 0) {
            successMsg.append("<br/>").append(successNum).append("条数据 导入成功");
        }
        msg.append(successMsg).append(failureMsg);
        log.info(msg.toString());
        return resultList;
    }

    // 将列表按指定大小分块
    static <T> List<List<T>> partitionList(List<T> list, int chunkSize) {
        System.out.println((list.size() + chunkSize - 1) / chunkSize);
        return IntStream.range(0, (list.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min((i + 1) * chunkSize, list.size())))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        int size = 500;
        int chunkSize = 50;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        System.out.println(partitionList(list, chunkSize));
    }
}
