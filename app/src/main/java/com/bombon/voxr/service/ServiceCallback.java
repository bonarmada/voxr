package com.bombon.voxr.service;

/**
 * Created by Vaughn on 7/18/17.
 */

public interface ServiceCallback<T> {
    void getResult(int statusCode, T result);
}
