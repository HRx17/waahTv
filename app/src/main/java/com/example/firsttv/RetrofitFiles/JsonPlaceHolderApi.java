package com.example.firsttv.RetrofitFiles;

import com.example.firsttv.model.Post;
import com.example.firsttv.model.SubPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {


    @GET("api/getCategoryByUser?email=a@a.com")
    Call<List<Post>> getPosts();

    @GET("api/getChannelByCatUser")
    Call<SubPost> getSubPosts(
            @Query("email") String id,
            @Query("category") String cat);

    @FormUrlEncoded
    @POST("api/reportfailedchannel")
    Call<FailResponse> response(
            @Field("category") String category,
            @Field("url") String url,
            @Field("error") String error
    );
}
