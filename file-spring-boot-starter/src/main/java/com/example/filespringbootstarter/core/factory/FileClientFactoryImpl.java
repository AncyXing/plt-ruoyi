package com.example.filespringbootstarter.core.factory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.util.Assert;

import com.example.filespringbootstarter.config.FileClientConfig;
import com.example.filespringbootstarter.core.client.AbstractFileClient;
import com.example.filespringbootstarter.core.client.FileClient;
import com.example.filespringbootstarter.enums.FileStorageEnum;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件客户端的工厂实现类
 *
 * @author 芋道源码
 */
@Slf4j
public class FileClientFactoryImpl implements FileClientFactory {

  /**
   * 文件客户端 Map key：配置编号
   */
  private final ConcurrentMap<FileStorageEnum, FileClient> clients = new ConcurrentHashMap<>();

  private final FileClientConfig localFileClientConfig;

  private final FileClientConfig s3FileClientConfig;


  public FileClientFactoryImpl(FileClientConfig localFileClientConfig,
      FileClientConfig s3FileClientConfig) {
    this.localFileClientConfig = localFileClientConfig;
    this.s3FileClientConfig = s3FileClientConfig;
  }

  @Override
  public FileClient getFileClient(FileStorageEnum fileStorageEnum) {
    fileStorageEnum = Optional.ofNullable(fileStorageEnum).orElse(FileStorageEnum.LOCAL);
    if (!clients.containsKey(fileStorageEnum)) {
      if (FileStorageEnum.LOCAL.equals(fileStorageEnum)) {
        Assert.notNull(localFileClientConfig, "请正确配置localFileClientConfig");
        log.info("初始化本地上传文件配置信息: {}", localFileClientConfig);
        clients.put(FileStorageEnum.LOCAL,
            createFileClient(FileStorageEnum.LOCAL, localFileClientConfig));
      }
      if (FileStorageEnum.S3.equals(fileStorageEnum)) {
        Assert.notNull(s3FileClientConfig, "请正确配置s3FileClientConfig");
        log.info("初始化s3上传文件配置信息: {}", s3FileClientConfig);
        clients.put(FileStorageEnum.S3, createFileClient(FileStorageEnum.S3, s3FileClientConfig));
      }
    }
    return clients.get(fileStorageEnum);
  }


  private <Config extends FileClientConfig> AbstractFileClient<Config> createFileClient(
      FileStorageEnum fileStorageEnum, FileClientConfig fileClientConfig) {
    // 创建客户端
    return (AbstractFileClient<Config>) ReflectUtil.newInstance(fileStorageEnum.getClientClass(),
        fileClientConfig);
  }

}
