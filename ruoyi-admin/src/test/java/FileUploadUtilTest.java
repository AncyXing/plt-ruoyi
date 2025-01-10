import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.filespringbootstarter.FileClientAutoConfiguration;
import com.example.filespringbootstarter.config.local.LocalFileClientConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.Seq;
import com.ruoyi.web.core.config.EnvConfig;

import cn.hutool.core.util.IdUtil;


@SpringBootTest(classes = FileUploadUtilTest.Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FileUploadUtilTest {
  @Autowired
  private LocalFileClientConfig localFileClientConfig;

  @Disabled
  @Test
  void testGetPathFileName() {
    String path = StringUtils.format("{}/{}_{}.{}", DateUtils.datePath(), IdUtil.fastSimpleUUID(),
        Seq.getId(Seq.uploadSeqType), "jpg");
    int dirLastIndex = localFileClientConfig.getBasePath().length() + 1;
    String currentDir = StringUtils.substring(localFileClientConfig.getBasePath(), dirLastIndex);
    String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + path;;
    System.out.println(pathFileName);
  }

  @Import({FileClientAutoConfiguration.class, EnvConfig.class})
  public static class Application {
  }
}
