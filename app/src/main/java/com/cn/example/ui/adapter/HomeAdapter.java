package com.cn.example.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.example.R;
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
        Glide.with(mContext)
                .load(item.getUrl())
                .into((ImageView) holder.getView(R.id.iv_img));
    }
}
