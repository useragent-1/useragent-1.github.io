package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/test-image")
public class ImageTestController {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public String showTestPage(Model model) {
        // 显示上传目录中的所有图片
        List<String> images = new ArrayList<>();
        try {
            File dir = new File(uploadDir);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && isImageFile(file.getName())) {
                            images.add("/uploads/" + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        model.addAttribute("images", images);
        model.addAttribute("uploadDir", uploadDir);
        return "image-test";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择一个文件上传");
            return "redirect:/test-image";
        }

        try {
            // 创建上传目录
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String filename = UUID.randomUUID() + getFileExtension(file.getOriginalFilename());
            
            // 保存文件
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);
            
            redirectAttributes.addFlashAttribute("message", "上传成功！");
            redirectAttributes.addFlashAttribute("imagePath", "/uploads/" + filename);
            
            // 打印调试信息
            System.out.println("上传文件到：" + filePath.toAbsolutePath());
            System.out.println("Web访问路径：/uploads/" + filename);
            
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "上传失败：" + e.getMessage());
        }

        return "redirect:/test-image";
    }
    
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
    
    private boolean isImageFile(String filename) {
        String[] extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        String lowerCaseFilename = filename.toLowerCase();
        for (String ext : extensions) {
            if (lowerCaseFilename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
} 