package com.tv.waah.RetrofitFiles;

public class FailResponse {
    String email;
    String category;
    String url;
    String error;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FailResponse(String email, String category, String url, String error) {
        this.email = email;
        this.category = category;
        this.url = url;
        this.error = error;
    }

}
