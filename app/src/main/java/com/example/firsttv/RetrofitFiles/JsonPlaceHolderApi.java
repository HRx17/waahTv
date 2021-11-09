package com.example.firsttv.RetrofitFiles;

import com.example.firsttv.model.Post;
import com.example.firsttv.model.SubPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {


    @GET("api/getCategoryByUser?email=a@a.com")
    Call<List<Post>> getPosts();

    @GET("api/getChannelByCatUser")
    Call<SubPost> getSubPosts(
            @Query("email") String id,
            @Query("category") String cat);

}
