package com.ruoyi.common.utils.file;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.springframework.web.multipart.MultipartFile;

public class ParseUtils {
    /**
     * 把一个文件中的内容读取成一个String字符串
     *
     * @param jsonFile 需要解析的json文件
     * @return 解析后得到的字符串
     */
    public static String parseJsonFile(File jsonFile) throws IOException {
        FileReader fileReader = new FileReader(jsonFile);
        // Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = fileReader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        return sb.toString();
    }

    public static String parseUploadFile(MultipartFile file) throws IOException {
        Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        reader.close();
        return sb.toString();
    }


}
