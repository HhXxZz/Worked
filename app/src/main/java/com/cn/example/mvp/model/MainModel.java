package com.cn.example.mvp.model;

import com.cn.example.bean.Subject;
import com.cn.example.http.HttpUtils;
import com.cn.example.http.api.Repository;
import com.cn.example.http.api.ServerApi;
import com.cn.example.http.listener.RequestCallBack;
import com.cn.example.mvp.IModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/20.
 */

public class MainModel implements IModel {

    public void getData(final RequestCallBack<Subject> callBack){
        Repository.getRepository().getGankio()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Subject>() {
                    @Override
                    public void accept(Subject subject) throws Exception {
                        callBack.onSuccess(subject);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError(0,throwable.getMessage());
                    }
                });
    }

}
