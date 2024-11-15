package com.example.pltool.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 用户单词表
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_word")
@Entity
@Table(name = "user_word")
public class UserWord implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 编号
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 记录uuid
   */
  @TableField("uuid")
  private String uuid;

  /**
   * 单词uuid
   */
  @TableField("word_uuid")
  private String wordUuid;

  /**
   * 单词
   */
  @TableField("word")
  private String word;

  /**
   * 用户id
   */
  @JsonIgnore
  @TableField("user_id")
  private Long userId;

  /**
   * 用户名称
   */
  @TableField("user_name")
  private String userName;

  /**
   * 阶段，拥有标识单词的单词的熟悉程度
   */
  @TableField("period")
  private Integer period;

  /**
   * 是否收藏
   */
  @TableField("collect_flag")
  private Integer collectFlag;

  /**
   * 创建时间
   */
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("create_time")
  private LocalDateTime createTime;

  /**
   * 更新时间时间
   */
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @TableField("update_time")
  private LocalDateTime updateTime;

  /**
   * 下次学习时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("next_study_time")
  private LocalDateTime nextStudyTime;
}
