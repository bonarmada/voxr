package com.bombon.voxr.dagger.remote;


import com.bombon.voxr.model.Post;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Vaughn on 6/5/17.
 */


public interface PostRemote {
    @GET("/posts")
    Observable<Response<List<Post>>> getPosts();

    @GET("/posts/{id}")
    Observable<Response<Post>> getPost(@Path("id") int id);
}
