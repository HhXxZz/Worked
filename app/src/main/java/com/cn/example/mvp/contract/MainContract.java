package com.cn.example.mvp.contract;

/**
 * Created by Administrator on 2017/9/20.
 */

public class MainContract {

    public interface MainView{
        void getDataSuccess(String data);

        void getDataFail(String failMsg);
    }

    public interface IMainPresenter{
        void getData();
    }

}
