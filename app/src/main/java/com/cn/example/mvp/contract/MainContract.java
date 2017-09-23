package com.cn.example.mvp.contract;

import com.cn.example.bean.Subject;

/**
 * Created by Administrator on 2017/9/20.
 */

public class MainContract {

    public interface MainView{
        void getDataSuccess(Subject data);

        void getDataFail(String failMsg);
    }

    public interface IMainPresenter{
        void getData();
    }

}
