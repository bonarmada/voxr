package com.bombon.voxr.service;

import com.bombon.voxr.common.dagger.remote.RecordRemote;
import com.bombon.voxr.dao.RecordDao;
import com.bombon.voxr.model.Record;
import com.bombon.voxr.util.ServiceCallback;

import java.util.List;

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


public class RecordService {
    private static final String TAG = RecordService.class.getSimpleName();

    private static RecordRemote remote;
    private static RecordDao dao;

    @Inject
    public RecordService(RecordRemote remote, RecordDao dao) {
        this.remote = remote;
        this.dao = dao;
    }

    public static void get(int userId, final ServiceCallback callback) {
        remote.get(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<Record>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<List<Record>> listResponse) {
                        if (listResponse.code() == 200) {
                            dao.save(listResponse.body());
                            callback.onSuccess(listResponse.code(), dao.get());
                            return;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
