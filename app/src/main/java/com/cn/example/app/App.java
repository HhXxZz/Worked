package com.cn.example.app;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;

/**
 * Created by Administrator on 2017/6/7.
 */

public class App extends Application {

    //application 实例
    private static App instance;

    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }


    /**
     * 得到APP实例
     * @return
     */
    public static App getInstance(){
        return instance;
    }


    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory( new File(this.getExternalCacheDir(), "video-cache"))
                .build();
    }

}
