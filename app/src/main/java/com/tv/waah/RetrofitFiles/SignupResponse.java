package com.tv.waah.RetrofitFiles;

public class SignupResponse {
    String email;
    String password;
    String message;
    String errorCode;
    String ipAddress;
    public String deviceType;
    public String modelNumner;

    public SignupResponse(String email, String password, String deviceType, String modelNumner, String ipAddress) {
        this.email = email;
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
        this.modelNumner = modelNumner;
        this.password = password;
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


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return errorCode;
    }

    public void setError(String error) {
        this.errorCode = error;
    }
}
