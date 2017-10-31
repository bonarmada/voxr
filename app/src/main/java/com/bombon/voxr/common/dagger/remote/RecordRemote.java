package com.bombon.voxr.common.dagger.remote;

import com.bombon.voxr.model.Record;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Vaughn on 10/16/17.
 */

public interface RecordRemote {
    @GET("users/{user_id}/records")
    Single<Response<List<Record>>> get(@Path("user_id") int userId);

    @GET("users/{user_id}/records/{record_id}")
    Single<Response<Record>> getById(@Path("user_id") int userId , @Path("record_id") int recordId);

    @POST("users/{user_id}/records")
    Single<Response> createRecord(@Path("user_id") int userId);
}

