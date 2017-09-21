package com.cn.example.http.listener;

/**
 * Created by Administrator on 2017/3/8.
 */

public interface RequestCallBack<T> {

    void onSuccess(T data);

    void onError(int errcode, String errorMsg);

}
