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
 * 用户学习记录表
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_study_record")
@Entity
@Table(name = "user_study_record")
public class UserStudyRecord implements Serializable {

  private static final long serialVersionUID = 1L;

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
   * 用户id
   */
  @JsonIgnore
  @TableField("user_id")
  private Long userId;

  /**
   * 用户名
   */
  @TableField("user_name")
  private String userName;

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
   * 学习时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("study_time")
  private LocalDateTime studyTime;

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
}
