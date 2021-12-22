package com.tv.waah.RetrofitFiles;

import com.google.gson.JsonObject;
import com.tv.waah.model.Post;
import com.tv.waah.model.SubPost;

import org.json.JSONStringer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("api/getCategoryByUser")
    Call<List<Post>> getPosts(@Header("access") String access,
            @Query("email") String id
    );

    @GET("api/serverversion")
    Call<Update> autoUpdate(@Query("email") String id
    );

    @GET("api/getChannelByCatUser")
    Call<SubPost> getSubPosts(@Header("access") String access,
            @Query("email") String id,
            @Query("category") String cat);

    @POST("api/reportfailedchannel")
    Call<Void> response(@Header("access") String access,@Body FailResponse failResponse);

    @POST("api/userproperties")
    Call<String> setLang(@Header("access") String access,@Body JSONStringer data);

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginResponse loginResponse);
    @POST("api/signup")
    Call<SignupResponse> Signup(@Body SignupResponse signupResponse);

    @GET("api/validatecoupon")
    Call<Validate> validate(@Query("coupon") String coupon);

    @POST("api/logout")
    Call<LoginResponse> logout(@Body LoginResponse loginResponse);
}
