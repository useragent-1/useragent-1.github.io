package com.example.demo.repository;

import com.example.demo.model.TextDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TextDocumentRepository extends JpaRepository<TextDocument, Long> {
    List<TextDocument> findByFeaturedTrue();
    
    // 查询今日创建的文档数量
    long countByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
} 