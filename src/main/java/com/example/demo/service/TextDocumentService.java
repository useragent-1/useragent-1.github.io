package com.example.demo.service;

import com.example.demo.model.TextDocument;
import com.example.demo.model.Category;

import java.util.List;

public interface TextDocumentService {
    List<TextDocument> getAllDocuments();
    List<TextDocument> getFeaturedDocuments();
    List<TextDocument> getDocumentsByCategory(Long categoryId);
    TextDocument getDocumentById(Long id);
    TextDocument saveDocument(TextDocument document);
    void deleteDocument(Long id);
    void toggleFeatured(Long id);
    TextDocument updateDocumentCategory(Long documentId, Long categoryId);
    
    // 统计方法
    long countAllDocuments();
    long countTodayDocuments();
} 