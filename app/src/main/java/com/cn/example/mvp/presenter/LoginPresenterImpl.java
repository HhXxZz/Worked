package com.cn.example.mvp.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.cn.example.ui.activity.LoginActivity;
import com.cn.example.mvp.contract.LoginContract;
import com.cn.example.mvp.base.BasePresenter;
import com.cn.example.mvp.model.LoginModel;

/**
 * Created by Administrator on 2017/9/20.
 */

public class LoginPresenterImpl extends BasePresenter<LoginModel,LoginActivity> implements LoginContract.ILoginPresenter {
    @Override
    public void login(String name, String pwd) {

        if(getIView() == null){
            Log.i("getIView","null");
        }else {

            if (!getIView().checkNull()) {
                if (getModel().login(name, pwd)) {
                    getIView().loginSuccess();
                } else {
                    getIView().loginFail("账号密码不正确");
                }
            }
        }
    }

    @Override
    public String getList(){
        return getModel().getList();
    }

    @Override
    public void getCode(){
        if(!TextUtils.isEmpty(getModel().getCode())){
            getIView().getCodeSuccess(getModel().getCode());
        }else{
            getIView().getCodeFail();
        }

    }

    @Override
    public LoginModel loadModel() {
        return new LoginModel();
    }
}
