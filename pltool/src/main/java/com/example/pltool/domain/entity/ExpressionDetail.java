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
 * 表达记录详情表
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("expression_detail")
@Entity
@Table(name = "expression_detail")
public class ExpressionDetail implements Serializable {

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
   * 内容
   */
  @TableField("content_detail")
  private String contentDetail;

  @TableField("user_id")
  private Long userId;

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
