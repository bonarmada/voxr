package com.bombon.voxr.common.dagger.remote;

import com.bombon.voxr.model.User;

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
    Single<Response> register(@Body User user);
}
