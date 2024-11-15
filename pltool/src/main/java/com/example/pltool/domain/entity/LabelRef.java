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
 * 标签关联表
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("label_ref")
@Entity
@Table(name = "label_ref")
public class LabelRef implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * uuid
   */
  @TableField("uuid")
  private String uuid;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * label_uuid
   */
  @TableField("label_uuid")
  private String labelUuid;

  /**
   * 关联uuid 单词uuid或者词库uuid
   */
  @TableField("ref_uuid")
  private String refUuid;

  /**
   * @see com.example.pltool.controller.business.constant.enums.RefTypeEnum 关联类型 0：代表词库 1：代表单词 2：卡包
   */
  @TableField("ref_type")
  private Integer refType;

  /**
   * 创建人
   */
  @TableField("create_user_id")
  private Long createUserId;

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
