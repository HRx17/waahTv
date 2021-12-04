package com.Beta1.waahTv.RetrofitFiles;

import android.content.SharedPreferences;

import com.Beta1.waahTv.model.Post;
import com.Beta1.waahTv.model.SubPost;
import com.Beta1.waahTv.ui.PlaybackActivity;

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
    Call<FailResponse> response(@Body FailResponse failResponse);

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginResponse loginResponse);
    @POST("api/signup")
    Call<SignupResponse> Signup(@Body SignupResponse signupResponse);

    @GET("api/validatecoupon")
    Call<Validate> validate(@Query("coupon") String coupon);
}
