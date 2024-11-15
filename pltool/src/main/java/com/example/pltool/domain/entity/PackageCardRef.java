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
 * 卡包闪卡关系表
 * </p>
 *
 * @author author
 * @since 2024-07-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("package_card_ref")
@Entity
@Table(name = "package_card_ref")
public class PackageCardRef implements Serializable {

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
   * 卡包uuid
   */
  @TableField("package_uuid")
  private String packageUuid;

  /**
   * 闪卡uuid
   */
  @TableField("card_uuid")
  private String cardUuid;

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

  /**
   * 单词集uuid
   */

  @TableField("collection_uuid")
  private String collectionUuid;

  /**
   * 创建人
   */

  @TableField("create_user_id")
  private Long createUserId;
}
