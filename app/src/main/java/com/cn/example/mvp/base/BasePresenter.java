package com.cn.example.mvp.base;

import com.cn.example.mvp.IModel;
import com.cn.example.mvp.IPresenter;
import com.cn.example.mvp.IView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/9/20.
 */

public abstract   class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    private WeakReference weakReference;
    protected M iModel;
    protected V iView;

    public M getModel(){
        iModel = loadModel();
        return iModel;
    }

    @Override
    public void attachView(IView view) {
        weakReference = new WeakReference(view);
    }

    @Override
    public void detachView() {
        if(weakReference != null){
            weakReference.clear();
            weakReference = null;
        }
    }

    @Override
    public V getIView() {
        return (V) weakReference.get();
    }


    public abstract M loadModel();


}
