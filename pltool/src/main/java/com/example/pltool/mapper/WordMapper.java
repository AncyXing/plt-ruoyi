package com.example.pltool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.Word;

/**
 * <p>
 * 单词表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Mapper
public interface WordMapper extends BaseMapper<Word> {
  /**
   * 使用中文操作单词
   *
   * @param searchCn 中文搜索内容
   * @return 匹配的单词
   */
  List<Word> searchWordByCN(@Param("searchCn") String searchCn);

  /**
   * 随机获取用户词库的一个单词
   *
   * @param userId 用户id
   * @return
   */
  Word getRandomWordOfUser(@Param("userId") Long userId, @Param("lexiconUUID") String lexiconUUID,
      @Param("offset") Long offset);


  /**
   * 随机获取用户单词集中的一个单词
   *
   * @param userId 用户id
   * @return
   */
  Word getRandomWordInCollectionOfUser(@Param("userId") Long userId,
      @Param("collectionUUId") String collectionUUId, @Param("offset") Long offset);

  /**
   * 查询用户未学习的新单词总数
   *
   * @param userId 用户id
   * @return 未学习的新单词总数
   */
  Long getNewWordCountOfUser(@Param("userId") Long userId);

  /**
   * 查询单词总数量
   *
   * @param userId 用户id
   * @return 单词总数量
   */
  Long getWordCount(@Param("userId") Long userId);

  /**
   * 查询单词集下单词总数量
   *
   * @param userId 用户id
   * @return 单词总数量
   */
  Long getCollectionWordCount(@Param("collectionUUId") String collectionUUId,
      @Param("userId") Long userId);

  /**
   * 查询用户单词集中未学习的新单词总数
   *
   * @param userId 用户id
   * @return 未学习的新单词总数
   */
  Long getCollectionNewWordCountOfUser(@Param("collectionUUId") String collectionUUId,
      @Param("userId") Long userId);

  boolean batchInsertWord(@Param("list") List<Word> list);
}
