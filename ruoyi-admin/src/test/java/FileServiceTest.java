import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.filespringbootstarter.config.FileClientAutoConfiguration;
import com.example.filespringbootstarter.core.service.FileService;
import com.example.filespringbootstarter.enums.FileStorageEnum;
import com.ruoyi.web.core.config.EnvConfig;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;

@SpringBootTest(classes = FileServiceTest.Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileServiceTest {
    byte[] content = ResourceUtil.readBytes("file/erweima.jpg");
    String path = IdUtil.fastSimpleUUID() + ".jpg";
    String s3Path = "/2024/10/14/test.txt";
    byte[] s3Content = ResourceUtil.readBytes("file/test.txt");
    @Autowired
    private FileService fileService;

    @Test
    @Disabled
    @Order(1)
    void testCreateFile() throws Exception {
        String returnPath =
                fileService.createFile(FileStorageEnum.LOCAL, "test", "/home/" + path, content);
        System.out.println(returnPath);
    }

    @Test
    @Order(1)
    void testCreateWebFile() throws Exception {
        // 创建MockMultipartFile
        MultipartFile multipartFile = new MockMultipartFile("file", // 表单字段名
                "test.txt", // 文件名
                "text/plain", // 文件类型
                "Hello, World!".getBytes() // 文件内容
        );
        String returnPath = fileService.createWebFile(FileStorageEnum.S3, multipartFile);
        System.out.println(returnPath);
        s3Path = returnPath;
    }

    @Test
    @Order(2)
        // @Disabled
    void testGetFile() throws Exception {
        // byte[] result = fileService.getFileContent(FileStorageEnum.Local, "/home/" + path);
        // assertEquals(result.length, content.length);
        byte[] result = fileService.getFileContent(FileStorageEnum.S3, s3Path);
        assertEquals(result.length, s3Content.length);
    }

    @Test
    @Order(3)
    @Disabled
    void testDeleteFile() throws Exception {
        // fileService.deleteFile(FileStorageEnum.LOCAL, "/home/" + path);
        fileService.deleteFile(FileStorageEnum.S3, s3Path);
    }

    @Import({FileClientAutoConfiguration.class, EnvConfig.class})
    public static class Application {
    }
}
