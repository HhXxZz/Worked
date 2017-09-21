package com.cn.example.mvp.contract;

/**
 * Created by Administrator on 2017/9/19.
 */

public class LoginContract {

    public interface LoginView {
        String getUserName();

        String getPwd();

        void loginSuccess();

        void loginFail(String failMsg);

        void getCodeSuccess(String code);

        void getCodeFail();
    }


    public interface ILoginPresenter {
        void login(String name, String pwd);

        void getCode();

        String getList();
    }
}