package com.cn.example.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cn.example.app.App;


/**
 * Created by Eric on 2017/1/17.
 */

public class NetUtil {

    /**
     * 检查当前网络是否可用
     * @return 是否连接到网络
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

//    public static boolean isNetworkErrThenShowMsg() {
//        if (!isNetworkAvailable()) {
//
//            Toast.makeText(App.getAppContext(), App.getAppContext().getString(R.string.internet_error),
//                    Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return false;
//    }
}
