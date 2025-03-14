package com.example.demo.service.impl;

import com.example.demo.model.Category;
import com.example.demo.model.TextDocument;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TextDocumentRepository;
import com.example.demo.service.TextDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TextDocumentServiceImpl implements TextDocumentService {

    private final TextDocumentRepository textDocumentRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TextDocumentServiceImpl(TextDocumentRepository textDocumentRepository, CategoryRepository categoryRepository) {
        this.textDocumentRepository = textDocumentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<TextDocument> getAllDocuments() {
        return textDocumentRepository.findAll();
    }

    @Override
    public List<TextDocument> getFeaturedDocuments() {
        return textDocumentRepository.findByFeaturedTrue();
    }

    @Override
    public List<TextDocument> getDocumentsByCategory(Long categoryId) {
        // 这里需要实现按分类获取文档的逻辑
        return textDocumentRepository.findAll(); // 临时返回所有文档
    }

    @Override
    public TextDocument getDocumentById(Long id) {
        return textDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public TextDocument saveDocument(TextDocument document) {
        return textDocumentRepository.save(document);
    }

    @Override
    public void deleteDocument(Long id) {
        textDocumentRepository.deleteById(id);
    }

    @Override
    public void toggleFeatured(Long id) {
        Optional<TextDocument> documentOpt = textDocumentRepository.findById(id);
        if (documentOpt.isPresent()) {
            TextDocument document = documentOpt.get();
            document.setFeatured(!document.isFeatured());
            textDocumentRepository.save(document);
        }
    }

    @Override
    public TextDocument updateDocumentCategory(Long documentId, Long categoryId) {
        Optional<TextDocument> documentOpt = textDocumentRepository.findById(documentId);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        
        if (documentOpt.isPresent() && categoryOpt.isPresent()) {
            TextDocument document = documentOpt.get();
            document.setCategory(categoryOpt.get());
            return textDocumentRepository.save(document);
        }
        return null;
    }
    
    @Override
    public long countAllDocuments() {
        return textDocumentRepository.count();
    }

    @Override
    public long countTodayDocuments() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return textDocumentRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    }
} 