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
import org.hibernate.annotations.GenerationTime;

import javax.annotation.Generated;
import javax.persistence.*;

/**
 * <p>
 * 单词表
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("word")
@Entity
@Table(name = "word")
public class Word implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("uuid")
  private String uuid;

  /**
   * 单词
   */
  @TableField("word")
  private String word;

  /**
   * 音标
   */
  @TableField("phonetic")
  private String phonetic;

  /**
   * 翻译
   */
  @TableField("translation")
  private String translation;

  /**
   * 词性
   */
  @TableField("pos")
  private String pos;

  /**
   * 标签
   */
  @TableField("tag")
  private String tag;

  /**
   * 例句
   */
  @TableField("sentence")
  private String sentence;

  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @TableField("create_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @TableField("update_time")
  private LocalDateTime updateTime;

  @TableField("create_user_id")
  private Long createUserId;
}
