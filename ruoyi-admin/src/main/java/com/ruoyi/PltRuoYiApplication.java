package com.ruoyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
    scanBasePackages = {"com.ruoyi", "com.example.pltool", "com.example.filespringbootstarter"})
@MapperScan("com.example.pltool.mapper")
@EntityScan("com.example.pltool.domain.entity")
public class PltRuoYiApplication {
  public static void main(String[] args) {
    // System.setProperty("spring.devtools.restart.enabled", "false");
    SpringApplication.run(PltRuoYiApplication.class, args);
  }
}
