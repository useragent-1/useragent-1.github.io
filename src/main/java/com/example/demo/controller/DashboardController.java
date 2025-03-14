package com.example.demo.controller;

import com.example.demo.dto.PasswordChangeDto;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.TextDocumentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final TextDocumentService textDocumentService;
    private final CategoryService categoryService;

    @Autowired
    public DashboardController(UserService userService, 
                              FileStorageService fileStorageService,
                              TextDocumentService textDocumentService,
                              CategoryService categoryService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.textDocumentService = textDocumentService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String dashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        
        // 添加统计数据
        long documentCount = textDocumentService.countAllDocuments();
        long categoryCount = categoryService.findAllCategories().size();
        long todayDocumentCount = textDocumentService.countTodayDocuments();
        
        model.addAttribute("documentCount", documentCount);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("todayDocumentCount", todayDocumentCount);
        
        return "dashboard/index";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "dashboard/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, 
                               @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            // 处理文件上传
            if (profileImageFile != null && !profileImageFile.isEmpty()) {
                String profileImagePath = fileStorageService.storeFile(profileImageFile);
                user.setProfileImage(profileImagePath);
            }
            
            userService.updateUserProfile(user);
            redirectAttributes.addFlashAttribute("successMessage", "个人信息更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute PasswordChangeDto passwordChangeDto, 
                                RedirectAttributes redirectAttributes) {
        if (userService.changePassword(passwordChangeDto)) {
            redirectAttributes.addFlashAttribute("successMessage", "密码修改成功");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "密码修改失败，请检查当前密码是否正确或新密码是否一致");
        }
        return "redirect:/dashboard/change-password";
    }
}