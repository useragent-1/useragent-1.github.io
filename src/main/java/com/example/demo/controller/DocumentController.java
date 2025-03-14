package com.example.demo.controller;

import com.example.demo.model.TextDocument;
import com.example.demo.model.User;
import com.example.demo.model.Category;
import com.example.demo.service.TextDocumentService;
import com.example.demo.service.UserService;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard/documents")
public class DocumentController {

    private final TextDocumentService textDocumentService;
    private final UserService userService;
    private final CategoryService categoryService;
    
    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public DocumentController(TextDocumentService textDocumentService, UserService userService, CategoryService categoryService) {
        this.textDocumentService = textDocumentService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listDocuments(Model model, @RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            model.addAttribute("documents", textDocumentService.getDocumentsByCategory(categoryId));
            model.addAttribute("selectedCategory", categoryService.findById(categoryId).orElse(null));
        } else {
            model.addAttribute("documents", textDocumentService.getAllDocuments());
        }
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard/documents/list";
    }

    @GetMapping("/create")
    public String createDocumentForm(Model model) {
        model.addAttribute("document", new TextDocument());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard/documents/form";
    }

    @PostMapping("/create")
    public String createDocument(@ModelAttribute TextDocument document, 
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            // 验证文档标题和内容
            if (document.getTitle() == null || document.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("文档标题不能为空");
            }
            if (document.getContent() == null || document.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("文档内容不能为空");
            }

            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("用户未登录");
            }
            
            document.setAuthor(currentUser.getUsername());
            
            // 设置分类
            if (categoryId != null) {
                Category category = categoryService.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("分类不存在，ID: " + categoryId));
                document.setCategory(category);
            }
            
            // 处理图片上传
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                document.setImagePath(imagePath);
            }
            
            TextDocument savedDocument = textDocumentService.saveDocument(document);
            if (savedDocument == null) {
                throw new RuntimeException("文档保存失败");
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "文档创建成功");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "创建文档时发生错误：" + e.getMessage());
        }
        return "redirect:/dashboard/documents";
    }

    @GetMapping("/edit/{id}")
    public String editDocumentForm(@PathVariable Long id, Model model) {
        TextDocument document = textDocumentService.getDocumentById(id);
        model.addAttribute("document", document);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard/documents/form";
    }

    @PostMapping("/edit/{id}")
    public String updateDocument(@PathVariable Long id, @ModelAttribute TextDocument document, 
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            TextDocument existingDocument = textDocumentService.getDocumentById(id);
            existingDocument.setTitle(document.getTitle());
            existingDocument.setContent(document.getContent());
            existingDocument.setFeatured(document.isFeatured());
            
            // 更新分类
            if (categoryId != null) {
                Category category = categoryService.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("分类不存在，ID: " + categoryId));
                existingDocument.setCategory(category);
            } else {
                existingDocument.setCategory(null);
            }
            
            // 处理图片上传
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                existingDocument.setImagePath(imagePath);
            } else if (document.getImagePath() != null) {
                // 保留原有图片
                existingDocument.setImagePath(document.getImagePath());
            }
            
            textDocumentService.saveDocument(existingDocument);
            redirectAttributes.addFlashAttribute("successMessage", "文档更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard/documents";
    }

    @GetMapping("/delete/{id}")
    public String deleteDocument(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            textDocumentService.deleteDocument(id);
            redirectAttributes.addFlashAttribute("successMessage", "文档已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard/documents";
    }

    @GetMapping("/toggle-featured/{id}")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            textDocumentService.toggleFeatured(id);
            redirectAttributes.addFlashAttribute("successMessage", "文档首页显示状态已更新");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard/documents";
    }

    @GetMapping("/change-category/{id}")
    public String changeCategoryForm(@PathVariable Long id, Model model) {
        TextDocument document = textDocumentService.getDocumentById(id);
        model.addAttribute("document", document);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard/documents/change-category";
    }

    @PostMapping("/change-category/{id}")
    public String changeCategory(@PathVariable Long id, @RequestParam(required = false) Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            textDocumentService.updateDocumentCategory(id, categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "文档分类已更新");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard/documents";
    }
    
    // 保存上传的图片
    private String saveImage(MultipartFile file) throws IOException {
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
        
        // 返回Web访问路径
        return "/uploads/" + filename;
    }
    
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
} 