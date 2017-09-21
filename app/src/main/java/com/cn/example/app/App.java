package com.cn.example.app;

import android.app.Application;



/**
 * Created by Administrator on 2017/6/7.
 */

public class App extends Application {

    //application 实例
    private static App instance;

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







}
