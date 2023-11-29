package com.cqupt.reggie.controller;

import com.cqupt.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param
     * @return {@link R}<{@link String}>
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("file:{}", file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename:{}", originalFilename);
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info("suffix:{}", suffix);
        //使用UUID随机生成文件名
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //    目录不存在
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //    输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();

            //设置响应文件
            response.setContentType("image/jpeg");
            //    输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            int len  =  0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
