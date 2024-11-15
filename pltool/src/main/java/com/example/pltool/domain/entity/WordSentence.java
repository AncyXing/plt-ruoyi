package com.example.pltool.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 单词例句表
 * </p>
 *
 * @author author
 * @since 2024-08-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("word_sentence")
@Entity
@Table(name = "word_sentence")
public class WordSentence implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * uuid
   */
  @TableField("uuid")
  private String uuid;

  /**
   * 单词uuid
   */
  @TableField("word_uuid")
  private String wordUuid;

  /**
   * 例句内容
   */
  @TableField("sentence_content")
  private String sentenceContent;

  /**
   * 翻译内容
   */
  @TableField("translate_content")
  private String translateContent;

  /**
   * 创建时间
   */
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @TableField("create_time")
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @TableField("update_time")
  private LocalDateTime updateTime;
}
