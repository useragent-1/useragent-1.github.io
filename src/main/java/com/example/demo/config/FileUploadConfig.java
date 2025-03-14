package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@Primary
public class FileUploadConfig implements WebMvcConfigurer {
    
    @Value("${app.file.upload-dir}")
    private String configuredUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取上传目录的绝对路径
        String uploadDir = configuredUploadDir != null ? configuredUploadDir : 
                           System.getProperty("user.dir") + File.separator + "uploads";
        
        // 确保上传目录存在
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("创建上传目录: " + uploadDir);
        }
        
        // 添加资源处理器 - 确保 /uploads/** 映射到文件系统中的上传目录
        String location = "file:" + uploadDir.replace("\\", "/") + "/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
        
        System.out.println("**** 图片资源映射配置 ****");
        System.out.println("- 上传目录: " + uploadDir);
        System.out.println("- URL路径: /uploads/**");
        System.out.println("- 资源位置: " + location);
        System.out.println("*************************");
    }
} 