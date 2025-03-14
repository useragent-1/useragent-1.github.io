package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.TextDocument;
import com.example.demo.service.CategoryService;
import com.example.demo.service.TextDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final TextDocumentService textDocumentService;
    private final CategoryService categoryService;

    @Autowired
    public HomeController(TextDocumentService textDocumentService, CategoryService categoryService) {
        this.textDocumentService = textDocumentService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) Long categoryId) {
        // 获取所有分类
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        
        // 如果指定了分类ID，则获取该分类下的文档
        List<TextDocument> documents;
        Category selectedCategory = null;
        
        if (categoryId != null) {
            Optional<Category> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isPresent()) {
                selectedCategory = categoryOpt.get();
                documents = textDocumentService.getDocumentsByCategory(categoryId);
            } else {
                documents = textDocumentService.getFeaturedDocuments();
            }
        } else {
            documents = textDocumentService.getFeaturedDocuments();
        }
        
        model.addAttribute("documents", documents);
        model.addAttribute("selectedCategory", selectedCategory);
        return "home";
    }
    
    @GetMapping("/documents/{id}")
    public String viewDocument(@PathVariable Long id, Model model) {
        TextDocument document = textDocumentService.getDocumentById(id);
        model.addAttribute("document", document);
        return "document-view";
    }
} 