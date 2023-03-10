package com.tv.waah.RetrofitFiles;


public class LoginResponse {
    public String ipAddress;
    public String deviceType;
    public String modelNumner;
    String errorCode;
    String message;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String email;
    String password;

    public LoginResponse(String email, String password,String ipAddress, String deviceType, String modelNumner) {
        this.email = email;
        this.password = password;
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
        this.modelNumner = modelNumner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getModelNumner() {
        return modelNumner;
    }

    public void setModelNumner(String modelNumner) {
        this.modelNumner = modelNumner;
    }

    String emailId;
    String expiryDate;
    String isActive;
    String Languages;


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
