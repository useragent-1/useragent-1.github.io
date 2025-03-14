package com.example.demo.service;

import com.example.demo.model.Danmaku;
import com.example.demo.model.User;

import java.util.List;

public interface DanmakuService {
    Danmaku save(String content, User user, Integer colorType);
    List<Danmaku> findRecentDanmakus();
    void deleteById(Long id);
} 