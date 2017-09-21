package com.cn.example.mvp.presenter;

import com.cn.example.http.listener.RequestCallBack;
import com.cn.example.mvp.base.BasePresenter;
import com.cn.example.mvp.contract.MainContract;
import com.cn.example.mvp.model.MainModel;
import com.cn.example.ui.activity.MainActivity;

/**
 * Created by Administrator on 2017/9/20.
 */

public class MainPresenterImpl extends BasePresenter<MainModel,MainActivity> implements MainContract.IMainPresenter {

    @Override
    public void getData() {
        getModel().getData(new RequestCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                getIView().getDataSuccess(data);
            }

            @Override
            public void onError(int errcode, String errorMsg) {
                getIView().getDataFail(errorMsg);
            }
        });
    }

    @Override
    public MainModel loadModel() {
        return new MainModel();
    }
}
