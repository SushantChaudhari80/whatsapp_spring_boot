package com.example.whatsapp.utils;


public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();

    private String username;

   
    private SessionManager() {}

    
    public static SessionManager getInstance() {
        return INSTANCE;
    }

    // Method to set the username
    public void setUsername(String username) {
    	
        this.username = username;
    }

    // Method to get the username
    public String getUsername() {
    	return username; 
    }
}
