package com.example.pltool.mapper;

import java.util.List;
import java.util.Map;

import com.example.pltool.domain.vo.UserWordOfMonthVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.UserWord;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.domain.vo.UserWordPeriodVo;

/**
 * <p>
 * 用户单词表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Mapper
public interface UserWordMapper extends BaseMapper<UserWord> {
  Word getNeedReviewWord(@Param("timeStr") String timeStr, @Param("userId") Long userId);

  /**
   * 获取用户单词数量
   *
   * @return 单词数量
   */
  Long getWordCount(@Param("userId") Long userId);

  /**
   * 每月学习单词数统计
   *
   * @param beginStr 年开始时间
   * @param endStr 年结束时间
   * @return 每月学习单词数统计
   */
  List<UserWordOfMonthVo> getUserWordOfMonth(@Param("userId") Long userId, @Param("beginStr") String beginStr,
                                             @Param("endStr") String endStr);

  /**
   * 获取单词的阶段数量
   *
   * @return 单词阶段数量
   */
  List<UserWordPeriodVo> getUserWordPeriodCount(@Param("userId") Long userId);

  /**
   * 每天期望值
   *
   * @param beginStr 月开始时间
   * @param endStr 月结束时间
   * @return 每天期望值
   */
  @MapKey("day")
  Map<String, Map<String, Long>> getExceptValueOfDay(@Param("userId") Long userId,
      @Param("beginStr") String beginStr, @Param("endStr") String endStr);

  /**
   * 查询用户今日需复习单词总数
   *
   * @param userId 用户id
   * @param beginStr 日开始时间
   * @param endStr 日结束时间
   * @return 用户今日需复习单词总数
   */
  Long getNeedReviewNumOfDay(@Param("userId") Long userId, @Param("beginStr") String beginStr,
      @Param("endStr") String endStr);

  /**
   * 查询用户今日已复习单词数
   *
   * @param userId 用户id
   * @param beginStr 日开始时间
   * @param endStr 日结束时间
   * @return 用户今日已复习单词数
   */
  Long getHaveReviewNumOfDay(@Param("userId") Long userId, @Param("beginStr") String beginStr,
      @Param("endStr") String endStr);

  Long getTotalReviewNum(@Param("userId") Long userId);

  List<Word> getNeedReviewWords(@Param("timeStr") String timeStr, @Param("userId") Long userId);

  Word getReviewWordByIndex(@Param("userId") Long userId, @Param("offset") int index);

  /**
   * 获取复习单词的id
   *
   * @param timeStr 查询时间
   * @return 复习单词的id
   */
  Long getReviewWordId(@Param("timeStr") String timeStr, @Param("userId") Long userId);
}
