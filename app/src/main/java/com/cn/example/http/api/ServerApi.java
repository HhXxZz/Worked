package com.cn.example.http.api;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/6/15.
 */

public interface ServerApi {

    @GET("api/data/福利/10/1")
    Observable<ResponseBody>getGankList();

}