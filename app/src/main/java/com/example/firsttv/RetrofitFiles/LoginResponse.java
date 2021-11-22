package com.example.firsttv.RetrofitFiles;

public class LoginResponse {
    String emailId;
    String expiryDate;
    String isActive;
    String Languages;

    public LoginResponse(String emailId, String expiryDate, String isActive, String languages) {
        this.emailId = emailId;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
        Languages = languages;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getLanguages() {
        return Languages;
    }

    public void setLanguages(String languages) {
        Languages = languages;
    }
}
