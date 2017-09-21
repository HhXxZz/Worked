package com.cn.example.ui.activity;


import android.widget.TextView;

import com.cn.example.R;
import com.cn.example.base.BaseActivity;
import com.cn.example.mvp.contract.MainContract;
import com.cn.example.mvp.presenter.MainPresenterImpl;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenterImpl> implements MainContract.MainView {

    @BindView(R.id.tv_data)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        presenter.getData();
    }

    @Override
    protected MainPresenterImpl getPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    public void getDataSuccess(String data) {
        textView.setText(data);
    }

    @Override
    public void getDataFail(String failMsg) {
        textView.setText(failMsg);
    }
}
