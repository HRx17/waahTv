package com.example.firsttv.RetrofitFiles;

public class FailResponse {
    String email;
    String category;
    String url;
    String error;

    public FailResponse(String category, String url, String error) {
        this.category = category;
        this.url = url;
        this.error = error;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
