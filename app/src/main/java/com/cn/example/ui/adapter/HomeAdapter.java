package com.cn.example.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.example.R;
import com.cn.example.app.GlideApp;
import com.cn.example.bean.Subject;

import java.util.List;

/**
 * Created by Administrator on 2017/9/23.
 */

public class HomeAdapter extends BaseQuickAdapter<Subject.ResultsBean,BaseViewHolder> {

    public HomeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Subject.ResultsBean item) {

//        Glide.with(mContext)
//                .load(item.getUrl())
//                .into((ImageView) holder.getView(R.id.iv_img));

        GlideApp.with(mContext)
                .load(item.getUrl())
                .centerCrop()
                //.placeholder(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) holder.getView(R.id.iv_img));
    }
}
