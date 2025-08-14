package com.example.whatsapp.service;

import com.example.whatsapp.entity.User;
import com.example.whatsapp.repository.UserRepository;
import com.example.whatsapp.utils.SessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public String login(String username, String password) {
        try {
            User user = userRepo.getUser(username, password);
            if (user != null) {
            	SessionManager.getInstance().setUsername(username);
                return "Success";
            } else {
                return "Invalid username or password.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Login failed: " + e.getLocalizedMessage();
        }
    }
    
    
    public User getUserDetails() {
    	return userRepo.getUserDetails(SessionManager.getInstance().getUsername());
    }
    
    
}
