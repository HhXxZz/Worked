package com.cn.example.mvp.model;

import com.cn.example.mvp.IModel;


/**
 * Created by Administrator on 2017/9/20.
 */

public class LoginModel implements IModel {
    public boolean login(String username, String pwd) {
        boolean isLogin = false;
        if ("admin".equals(username) && "123".equals(pwd)) {
            isLogin = true;
        } else {
            isLogin = false;
        }
        return isLogin;
    }

    public String getList(){
        return "登录正式";
    }

    public String getCode(){
        return "2333";
    }
}
