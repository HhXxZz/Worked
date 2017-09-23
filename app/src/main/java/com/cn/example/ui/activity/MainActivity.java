package com.cn.example.ui.activity;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.cn.example.R;
import com.cn.example.base.BaseActivity;
import com.cn.example.bean.Subject;
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
        verifyStoragePermissions(this);
        presenter.getData();
    }

    @Override
    protected MainPresenterImpl getPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    public void getDataSuccess(Subject data) {
        textView.setText(data.getResults().get(0).getSource());
    }

    @Override
    public void getDataFail(String failMsg) {
        textView.setText(failMsg);
    }

    public static void verifyStoragePermissions(Activity activity) {

        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
