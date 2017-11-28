package com.cn.example.ui.activity;


import android.Manifest;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cn.example.R;
import com.cn.example.base.BaseActivity;
import com.cn.example.bean.Subject;
import com.cn.example.mvp.contract.MainContract;
import com.cn.example.mvp.presenter.MainPresenterImpl;
import com.cn.example.ui.adapter.HomeAdapter;
import com.cn.example.utils.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseActivity<MainPresenterImpl> implements MainContract.MainView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private HomeAdapter mHomeAdapter;
    private List<Subject.ResultsBean>mData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //verifyStoragePermissions(this);
        initRecyclerView();
        requestPermission();


    }

    private void initRecyclerView() {
        mData = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(R.layout.item_img,mData);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mRecyclerView.setAdapter(mHomeAdapter);
        mHomeAdapter.setEnableLoadMore(true);
    }

    private void requestPermission() {

        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        if(permission.granted){
                            //同意了该权限
                            LogUtils.i("rxPermission",permission.name+"同意了该权限"+Thread.currentThread().getName());
                            presenter.getData();

                        }else if(permission.shouldShowRequestPermissionRationale){
                            //拒绝了该权限，没有选择 【不再询问】
                            LogUtils.i("rxPermission",permission.name+"拒绝了该权限，没有选择 【不再询问】");
                        }else{
                            //拒绝了该权限，勾选了 【不再询问】
                            LogUtils.i("rxPermission",permission.name+"拒绝了该权限，勾选了 【不再询问】");
                        }
                    }
                });
    }

    @Override
    protected MainPresenterImpl getPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    public void getDataSuccess(Subject data) {
        mHomeAdapter.setEnableLoadMore(false);
        mData.addAll(data.getResults());
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataFail(String failMsg) {

    }

}
