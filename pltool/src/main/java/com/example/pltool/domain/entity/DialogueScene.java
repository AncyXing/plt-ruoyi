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
 * 对话场景表
 * </p>
 *
 * @author author
 * @since 2024-09-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dialogue_scene")
@Entity
@Table(name = "dialogue_scene")
public class DialogueScene implements Serializable {

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
   * 名称
   */
  @TableField("name")
  private String name;

  /**
   * 场景介绍
   */
  @TableField("introduce")
  private String introduce;

  /**
   * 总结
   */
  @TableField("summary")
  private String summary;

  /**
   * 需要学习的信息
   */
  @TableField("study_info")
  private String studyInfo;

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
