package com.example.demo.repository;

import com.example.demo.model.Website;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {
    List<Website> findByUser(User user);
    List<Website> findByUserOrderByCreatedAtDesc(User user);
} 