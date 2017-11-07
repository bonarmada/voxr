package com.bombon.voxr.service;

import android.util.Log;

import com.bombon.voxr.common.dagger.remote.UserRemote;
import com.bombon.voxr.dao.UserDao;
import com.bombon.voxr.model.User;
import com.bombon.voxr.model.pojo.Password;
import com.bombon.voxr.util.ErrorCode;
import com.bombon.voxr.util.ServiceCallback;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by Vaughn on 10/16/17.
 */


public class UserService {
    private static final String TAG = UserService.class.getSimpleName();

    private static UserRemote remote;
    private static UserDao dao;

    @Inject
    public UserService(UserRemote remote, UserDao dao) {
        this.remote = remote;
        this.dao = dao;
    }

    public static void login(final User user, final ServiceCallback callback) {
        remote.login(user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<User> response) {
                        Log.e(TAG, response.code() + " " + response.body());
                        if (response.code() == 200) {
                            dao.save(response.body());
                            callback.onSuccess(response.code(), null);
                            return;
                        }

                        try {
                            callback.onError(response.code(), response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            callback.onError(((HttpException) e).code(), e.getMessage());
                            return;
                        }
                        callback.onError(0, e.getMessage());
                    }
                });
    }

    public static void register(final User user, final ServiceCallback callback) {
        remote.register(user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<User> response) {
                        Log.e(TAG, response.code() + " " + response.body());
                        if (response.code() == 201) {
                            callback.onSuccess(response.code(), null);
                            return;
                        }

                        try {
                            callback.onError(response.code(), response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            callback.onError(((HttpException) e).code(), e.getMessage());
                            return;
                        }
                        callback.onError(0, e.getMessage());
                    }
                });
    }

    // Password
    public static void forgot(final Password pass, final ServiceCallback callback) {
        remote.forgot(pass).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<String> response) {
                        Log.e(TAG, response.code() + " " + response.body());
                        if (response.code() == 202) {
                            callback.onSuccess(response.code(), null);
                            return;
                        }

                        try {
                            callback.onError(response.code(), response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            callback.onError(((HttpException) e).code(), e.getMessage());
                            return;
                        }
                        callback.onError(0, e.getMessage());
                    }
                });
    }

    public static void verify(final Password pass, final ServiceCallback callback) {
        Log.e(TAG, pass.toString());
        remote.verify(pass).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<String> response) {
                        Log.e(TAG, response.code() + " " + response.body());
                        if (response.code() == 200) {
                            callback.onSuccess(response.code(), null);
                            return;
                        }

                        try {
                            callback.onError(response.code(), response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            callback.onError(((HttpException) e).code(), e.getMessage());
                            return;
                        }
                        callback.onError(0, e.getMessage());
                    }
                });
    }

    public static void reset(final Password pass, final ServiceCallback callback) {
        remote.reset(pass).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<String> response) {
                        Log.e(TAG, response.code() + " " + response.body());
                        if (response.code() == 202) {
                            callback.onSuccess(response.code(), null);
                            return;
                        }

                        try {
                            callback.onError(response.code(), response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onError(((HttpException) e).code(), e.getMessage());
                    }
                });
    }


    public static boolean isLoggedIn() {
        if (dao.profile() == null)
            return false;
        return true;
    }

    public static User getLoggedIn() {
        return dao.profile();
    }

    public static void logout() {
        dao.clear();
    }

}
