package com.bombon.voxr.service;

import android.util.Log;

import com.bombon.voxr.common.dagger.remote.UserRemote;
import com.bombon.voxr.dao.UserDao;
import com.bombon.voxr.model.User;
import com.bombon.voxr.util.ErrorCode;
import com.bombon.voxr.util.ServiceCallback;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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

    public static boolean isLoggedIn() {
        if (dao.profile() == null)
            return false;
        return true;
    }

    public static void login(final User user, final ServiceCallback callback) {
        remote.login(user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<User> userResponse) {
                        if (userResponse.code() == 200) {
                            dao.save(userResponse.body());
                            callback.onSuccess(userResponse.code(), null);
                            return;
                        }
                        callback.onError(ErrorCode.UNAUTHORIZED, userResponse.message());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onError(ErrorCode.SERVICE_UNAVAILABLE, e.getMessage());
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
                        Log.e(TAG, response.code()+"");
                        if (response.code() == 201) {
                            callback.onSuccess(response.code(), null);
                            return;
                        }
                        if (response.code() == 409) {
                            callback.onError(ErrorCode.CONFLICT, response.message());
                            return;
                        }
                        if (response.code() == 400) {
                            callback.onError(ErrorCode.BAD_REQUEST, response.message());
                            return;
                        }
                        if (response.code() == 500) {
                            callback.onError(ErrorCode.SERVICE_UNAVAILABLE, response.message());
                            return;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onError(ErrorCode.SERVICE_UNAVAILABLE, e.getMessage());
                    }
                });
    }

    public static void logout() {
        dao.clear();
    }

}
