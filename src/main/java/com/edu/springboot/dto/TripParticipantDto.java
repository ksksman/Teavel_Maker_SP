package com.edu.springboot.dto;

public class TripParticipantDto {
    private int userId;
    private String nickname;
    private String email;
    
    // Getters & Setters
    public int getUserId() { 
        return userId; 
    }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }
    public String getNickname() { 
        return nickname; 
    }
    public void setNickname(String nickname) { 
        this.nickname = nickname; 
    }
    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
}
