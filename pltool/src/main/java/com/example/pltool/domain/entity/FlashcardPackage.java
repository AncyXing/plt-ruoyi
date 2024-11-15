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
 * 卡包表
 * </p>
 *
 * @author author
 * @since 2024-05-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("flashcard_package")
@Entity
@Table(name = "flashcard_package")
public class FlashcardPackage implements Serializable {

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
   * 卡包名
   */
  @TableField("name")
  private String name;

  /**
   * 卡包类型
   */
  @TableField("type")
  private Integer type;

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
  @TableField("update_time")
  private LocalDateTime updateTime;
}
