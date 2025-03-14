package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("category", new Category());
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard/categories/list";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            // 检查名称是否已存在
            if (categoryService.findByName(category.getName()).isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "分类名称已存在");
                return "redirect:/dashboard/categories";
            }
            
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("successMessage", "分类创建成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id).orElseThrow(() -> new RuntimeException("分类不存在")));
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("user", userService.getCurrentUser());
        return "dashboard/categories/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            Category existingCategory = categoryService.findById(id).orElseThrow(() -> new RuntimeException("分类不存在"));
            
            // 检查更新的名称是否已被其他分类使用
            categoryService.findByName(category.getName()).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new RuntimeException("分类名称已存在");
                }
            });
            
            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());
            categoryService.saveCategory(existingCategory);
            redirectAttributes.addFlashAttribute("successMessage", "分类更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "分类已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除分类失败：" + e.getMessage());
        }
        return "redirect:/dashboard/categories";
    }
} 