package com.example.pltool.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 问题表
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("question")
@Entity
@Table(name = "question")
public class Question implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * uuid
   */
  @TableField("uuid")
  private String uuid;

  @TableField("note_id")
  private Long noteId;

  /**
   * 问题
   */
  @TableField("question")
  private String question;

  /**
   * 答案
   */
  @TableField("answer")
  private String answer;

  /**
   * 标签
   */
  @TableField("tag")
  private String tag;

  @TableField("user_id")
  private Long userId;

  /**
   * 创建时间
   */
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("create_time")
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("update_time")
  private LocalDateTime updateTime;
}
