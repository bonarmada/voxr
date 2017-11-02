package com.bombon.voxr.common.dagger.remote;

import com.bombon.voxr.model.User;
import com.bombon.voxr.model.pojo.Password;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Vaughn on 10/16/17.
 */

public interface UserRemote {
    @POST("login")
    Single<Response<User>> login(@Body User user);

    @POST("users")
    Single<Response<User>> register(@Body User user);


    // Password remote
    @POST("password/forgot")
    Single<Response> forgot(@Body Password password);

    @POST("password/verify")
    Single<Response> reset(@Body Password password);

    @POST("password/reset")
    Single<Response> verify(@Body Password password);
}
