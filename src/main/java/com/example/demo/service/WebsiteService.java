package com.example.demo.service;

import com.example.demo.model.Website;
import com.example.demo.model.User;

import java.util.List;

public interface WebsiteService {
    List<Website> getAllWebsitesByUser(User user);
    Website getWebsiteById(Long id);
    Website saveWebsite(Website website);
    void deleteWebsite(Long id);
    boolean isOwner(Long websiteId, User user);
} 