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
 * 闪卡表
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("flashcard_attribute")
@Entity
@Table(name = "flashcard_attribute")
public class FlashcardAttribute implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 记录uuid
   */
  @TableField("uuid")
  private String uuid;

  /**
   * 闪卡uuid
   */
  @TableField("card_uuid")
  private String cardUuid;

  /**
   * 熟悉类型（0不会 1会 2待定）
   */
  @TableField("familiarity")
  private Integer familiarity;

  /**
   * 下次学习时间
   */
  @TableField("next_study_time")
  private LocalDateTime nextStudyTime;

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
