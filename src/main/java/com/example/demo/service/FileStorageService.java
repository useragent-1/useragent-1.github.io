package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /**
     * 存储上传的文件并返回文件路径
     * 
     * @param file 上传的文件
     * @return 存储后的文件路径
     */
    String storeFile(MultipartFile file);
    
    /**
     * 获取默认头像路径
     * 
     * @return 默认头像路径
     */
    String getDefaultAvatarPath();
} 