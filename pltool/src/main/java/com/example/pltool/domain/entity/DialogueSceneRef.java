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
 * 场景对话关系表
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dialogue_scene_ref")
@Entity
@Table(name = "dialogue_scene_ref")
public class DialogueSceneRef implements Serializable {

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
   * 对话uuid
   */
  @TableField("dialogue_uuid")
  private String dialogueUuid;

  /**
   * 场景uuid
   */
  @TableField("scene_uuid")
  private String sceneUuid;

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
