package com.example.firsttv;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("api/getCategoryByUser?email=a@a.com")
    Call<List<Post>> getPosts();
}
