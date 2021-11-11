package com.example.firsttv.RetrofitFiles;

public class FailResponse {
    String message;

    public FailResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
