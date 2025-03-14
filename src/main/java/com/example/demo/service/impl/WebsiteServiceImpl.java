package com.example.demo.service.impl;

import com.example.demo.model.Website;
import com.example.demo.model.User;
import com.example.demo.repository.WebsiteRepository;
import com.example.demo.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteRepository websiteRepository;

    @Autowired
    public WebsiteServiceImpl(WebsiteRepository websiteRepository) {
        this.websiteRepository = websiteRepository;
    }

    @Override
    public List<Website> getAllWebsitesByUser(User user) {
        return websiteRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Website getWebsiteById(Long id) {
        return websiteRepository.findById(id).orElse(null);
    }

    @Override
    public Website saveWebsite(Website website) {
        return websiteRepository.save(website);
    }

    @Override
    public void deleteWebsite(Long id) {
        websiteRepository.deleteById(id);
    }

    @Override
    public boolean isOwner(Long websiteId, User user) {
        Website website = getWebsiteById(websiteId);
        return website != null && website.getUser().getId().equals(user.getId());
    }
} 