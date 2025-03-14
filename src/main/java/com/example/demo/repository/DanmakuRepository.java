package com.example.demo.repository;

import com.example.demo.model.Danmaku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanmakuRepository extends JpaRepository<Danmaku, Long> {
    List<Danmaku> findTop100ByOrderByCreatedAtDesc();
} 