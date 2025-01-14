package com.example.filespringbootstarter.config.local;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.filespringbootstarter.config.FileClientConfig;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * 本地文件客户端的配置类
 *
 * @author author
 */
@ConfigurationProperties(prefix = "file.storage.local")
@Configuration
@Data
public class LocalFileClientConfig implements FileClientConfig {

  /**
   * 基础路径
   */
  @NotEmpty(message = "基础路径不能为空")
  private String basePath;

  /**
   * 自定义域名
   */
  @NotEmpty(message = "domain 不能为空")
  @URL(message = "domain 必须是 URL 格式")
  private String domain;

}
