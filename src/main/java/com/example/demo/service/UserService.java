package com.example.demo.service;

import com.example.demo.dto.PasswordChangeDto;
import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerNewUser(UserRegistrationDto registrationDto);
    User getCurrentUser();
    User updateUserProfile(User user);
    boolean changePassword(PasswordChangeDto passwordChangeDto);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
} 