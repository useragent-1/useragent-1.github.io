package com.example.demo.service.impl;

import com.example.demo.model.Danmaku;
import com.example.demo.model.User;
import com.example.demo.repository.DanmakuRepository;
import com.example.demo.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DanmakuServiceImpl implements DanmakuService {

    private final DanmakuRepository danmakuRepository;

    @Autowired
    public DanmakuServiceImpl(DanmakuRepository danmakuRepository) {
        this.danmakuRepository = danmakuRepository;
    }

    @Override
    @Transactional
    public Danmaku save(String content, User user, Integer colorType) {
        Danmaku danmaku = new Danmaku(content, user, colorType);
        return danmakuRepository.save(danmaku);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Danmaku> findRecentDanmakus() {
        return danmakuRepository.findTop100ByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        danmakuRepository.deleteById(id);
    }
} 