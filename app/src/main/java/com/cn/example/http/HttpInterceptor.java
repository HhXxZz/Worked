package com.cn.example.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/15.
 */

public class HttpInterceptor implements Interceptor {

    public HttpInterceptor(){

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        request.newBuilder();
        return null;
    }

}
