package com.example.filespringbootstarter;

import com.example.filespringbootstarter.config.FileClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.filespringbootstarter.config.local.LocalFileClientConfig;
import com.example.filespringbootstarter.config.s3.S3FileClientConfig;
import com.example.filespringbootstarter.core.factory.FileClientFactoryImpl;
import com.example.filespringbootstarter.core.service.FileService;
import com.example.filespringbootstarter.core.service.FileServiceImpl;

@ConditionalOnProperty(value = "file.storage.enable", matchIfMissing = false)
@Configuration
public class FileClientAutoConfiguration {
  @Autowired
  private LocalFileClientConfig localFileClientConfig;

  @Autowired
  private S3FileClientConfig s3FileClientConfig;

  @ConditionalOnProperty(value = {"file.storage.local.use","file.storage.s3.use"})
  @Bean
  public FileService complexFileService() {
    return new FileServiceImpl(
        new FileClientFactoryImpl(localFileClientConfig, s3FileClientConfig));
  }

  @ConditionalOnProperty(value = "file.storage.local.use", havingValue = "true")
  @Bean
  public FileService localFileService() {
    return new FileServiceImpl(new FileClientFactoryImpl(localFileClientConfig, null));
  }

  @ConditionalOnProperty(value = "file.storage.s3.use", havingValue = "true")
  @Bean
  public FileService s3FileService() {
    return new FileServiceImpl(new FileClientFactoryImpl(null, s3FileClientConfig));
  }
}
