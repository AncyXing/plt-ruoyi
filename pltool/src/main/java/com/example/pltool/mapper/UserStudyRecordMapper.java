package com.example.pltool.mapper;

import java.util.List;
import java.util.Map;

import com.example.pltool.domain.vo.UserStudyRecordOfMonthVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.UserStudyRecord;

/**
 * <p>
 * 用户学习记录表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Mapper
public interface UserStudyRecordMapper extends BaseMapper<UserStudyRecord> {
  /**
   * 当天的学习过的单词
   *
   * @param userId 用户id
   * @param beginStr 开始时间
   * @param endStr 结束时间
   * @return 学习记录
   */
  List<UserStudyRecord> listWordOfDay(@Param("userId") Long userId,
      @Param("beginStr") String beginStr, @Param("endStr") String endStr);

  /**
   * 获取学习记录数量
   *
   * @return 学习记录数量
   */
  Long getStudyRecordCount(@Param("userId") Long userId);

  /**
   * 每月学习单词数统计
   *
   * @param userId 用户id
   * @param beginStr 年开始时间
   * @param endStr 年结束时间
   * @return 每月学习单词数统计
   */
  List<UserStudyRecordOfMonthVo> getStudyRecordOfMonth(@Param("userId") Long userId, @Param("beginStr") String beginStr,
                                                       @Param("endStr") String endStr);

  /**
   * 每天实际值
   *
   * @param beginStr 月开始时间
   * @param endStr 月结束时间
   * @return 每天期望值
   */
  @MapKey("day")
  Map<String, Map<String, Long>> getActualValueOfDay(@Param("userId") Long userId,
      @Param("beginStr") String beginStr, @Param("endStr") String endStr);

}
