package com.cn.example.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cn.example.app.AppManager;
import com.cn.example.mvp.IView;
import com.cn.example.mvp.base.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/20.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initPresenter();
        initView();
    }

    private void initPresenter(){
        presenter = getPresenter();
        if(presenter != null){
            presenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter != null){
            presenter.detachView();
        }
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract P getPresenter();

}
