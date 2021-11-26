package com.example.firsttv.RetrofitFiles;

public class SignupResponse {
    String email;
    String password;
    String message;
    String errorCode;
    String macAddress;
    public String deviceType;
    public String modelNumner;

    public SignupResponse(String email, String macAddress, String deviceType, String modelNumner, String osVersion, String password) {
        this.email = email;
        this.macAddress = macAddress;
        this.deviceType = deviceType;
        this.modelNumner = modelNumner;
        this.osVersion = osVersion;
        this.password = password;
    }

    public String osVersion;

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
