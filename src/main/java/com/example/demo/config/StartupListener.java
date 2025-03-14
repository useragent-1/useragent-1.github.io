package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class StartupListener implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${app.file.upload-dir}")
    private String uploadDir;
    
    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("\n=================================================");
        System.out.println("应用启动完成，服务器地址: http://localhost:" + serverPort);
        System.out.println("=================================================");
        
        // 检查上传目录
        File uploadDirFile = new File(uploadDir);
        System.out.println("上传目录路径: " + uploadDirFile.getAbsolutePath());
        System.out.println("上传目录是否存在: " + uploadDirFile.exists());
        
        // 打印静态资源映射信息
        System.out.println("\n静态资源映射信息:");
        System.out.println("- /uploads/** -> " + uploadDir);
        
        // 打印测试页面URL
        System.out.println("\n图片测试页面: http://localhost:" + serverPort + "/test-image");
        System.out.println("=================================================\n");
    }
} 