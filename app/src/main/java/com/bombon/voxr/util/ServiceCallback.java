package com.bombon.voxr.util;

/**
 * Created by Vaughn on 7/18/17.
 */

public interface ServiceCallback<T> {
    void onSuccess(int statusCode, T result);
    void onError(ErrorCode code, String message);
}
