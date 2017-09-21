package com.cn.example.mvp.model;

import com.cn.example.http.HttpUtils;
import com.cn.example.http.api.ServerApi;
import com.cn.example.http.listener.RequestCallBack;
import com.cn.example.mvp.IModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/9/20.
 */

public class MainModel implements IModel {

    public void getData(final RequestCallBack<String> callBack){
        HttpUtils.getInstance().getHttpServer(ServerApi.class).getGankList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callBack.onSuccess(responseBody.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onSuccess(throwable.getMessage());
                    }
                });
    }

}
