package com.ctrl.android.kcetong.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ServiceKind;

import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/3/29
 */
public class HomeListBottomAdapter extends BaseQuickAdapter<ServiceKind, BaseViewHolder> {

    public HomeListBottomAdapter(int layoutResId, @Nullable List<ServiceKind> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceKind item) {
        helper.setText(R.id.tv_gridview, item.getSortsName());

        if (item.getSortsUrl().equals("mall_logo")) {
            Glide.with(mContext).load(R.drawable.easy_service).centerCrop().into((ImageView) helper.getView(R.id.iv_gridview));
        } else {
            Glide.with(mContext).load(item.getSortsUrl()).centerCrop().into((ImageView) helper.getView(R.id.iv_gridview));
        }
    }
}
