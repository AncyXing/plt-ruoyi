package com.example.filespringbootstarter.core.client.local;


import java.io.File;

import com.example.filespringbootstarter.config.FileClientConfig;
import com.example.filespringbootstarter.config.local.LocalFileClientConfig;
import com.example.filespringbootstarter.constant.Constants;
import com.example.filespringbootstarter.core.client.AbstractFileClient;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 本地文件客户端
 *
 * @author 芋道源码
 */
public class LocalFileClient extends AbstractFileClient<LocalFileClientConfig> {

  private LocalFileClient(FileClientConfig config) {
    super((LocalFileClientConfig) config);
  }

  @Override
  protected void doInit() {
    // 补全风格。例如说 Linux 是 /，Windows 是 \
    if (!config.getBasePath().endsWith(File.separator)) {
      config.setBasePath(config.getBasePath() + File.separator);
    }
  }

  @Override
  public String upload(byte[] content, String path, String type) {
    // 执行写入
    String filePath = getFilePath(path);
    FileUtil.writeBytes(content, filePath);
    // 拼接返回路径
    return StrUtil.format("{}{}/{}", config.getDomain(), Constants.RESOURCE_PREFIX, path);
  }

  @Override
  public void delete(String path) {
    String filePath = getFilePath(path);
    FileUtil.del(filePath);
  }

  @Override
  public byte[] getContent(String path) {
    String filePath = getFilePath(path);
    return FileUtil.readBytes(filePath);
  }

  private String getFilePath(String path) {
    return config.getBasePath() + File.separator + path;
  }

}
