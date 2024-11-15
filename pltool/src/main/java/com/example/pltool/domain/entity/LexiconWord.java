package com.example.pltool.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 词库与单词关系表
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("lexicon_word")
@Entity
@Table(name = "lexicon_word")
public class LexiconWord implements Serializable {

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

  /**
   * 词库id
   */
  @TableField("lexicon_uuid")
  private String lexiconUuid;

  /**
   * 单词id
   */
  @TableField("word_uuid")
  private String wordUuid;

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
