package com.bombon.voxr.util;

import com.bombon.voxr.model.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by bonbonme on 10/20/2017.
 */
public class ErrorUtils {

    private static Retrofit retrofit;

    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter =
                retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }
        return error;
    }
}