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
 * 对话表
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dialogue")
@Entity
@Table(name = "dialogue")
public class Dialogue implements Serializable {

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
   * 发送内容
   */
  @TableField("sender_content")
  private String senderContent;

  /**
   * 回复
   */
  @TableField("reply")
  private String reply;

  /**
   * 在对话中的排序号
   */
  @TableField("sort_num")
  private Integer sortNum;

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
