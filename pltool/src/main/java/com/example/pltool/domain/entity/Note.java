package com.example.pltool.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 笔记表
 * </p>
 *
 * @author author
 * @since 2024-09-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("note")
@Entity
@Table(name = "note")
public class Note implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("uuid")
  private String uuid;

  /**
   * 笔记标题
   */
  @TableField("title")
  private String title;

  /**
   * 总结
   */
  @TableField("summary")
  private String summary;

  @TableField("user_id")
  private Long userId;

  /**
   * 笔记类型
   * 
   * @see com.example.pltool.controller.business.constant.enums.NoteTypeEnum
   */
  @TableField("type")
  private Integer type;

  /**
   * 关联uuid
   */
  @TableField("ref_uuid")
  private String refUuid;

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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("update_time")
  private LocalDateTime updateTime;

  /**
   * 笔记内容
   */
  @TableField("content")
  private String content;
}
