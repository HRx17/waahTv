package com.tv.waah.RetrofitFiles;

import com.tv.waah.model.Post;
import com.tv.waah.model.SubPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("api/getCategoryByUser")
    Call<List<Post>> getPosts(
            @Query("email") String id
    );

    @GET("api/getChannelByCatUser")
    Call<SubPost> getSubPosts(
            @Query("email") String id,
            @Query("category") String cat);

    @POST("api/reportfailedchannel")
    Call<Void> response(@Body FailResponse failResponse);

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginResponse loginResponse);
    @POST("api/signup")
    Call<SignupResponse> Signup(@Body SignupResponse signupResponse);

    @GET("api/validatecoupon")
    Call<Validate> validate(@Query("coupon") String coupon);
}
