package com.ctrl.android.kcetong.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.BannerList;
import com.ctrl.android.kcetong.toolkit.util.LLog;

import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/3/29
 */
public class HomeBannerAdapter extends BaseQuickAdapter<BannerList.DataBean.BannerListBean, BaseViewHolder> {

    public HomeBannerAdapter(int layoutResId, @Nullable List<BannerList.DataBean.BannerListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerList.DataBean.BannerListBean item) {

        helper.setText(R.id.title, item.getTitle())
                .setText(R.id.subtitle, item.getSubTitle());

        try {
            helper.setTextColor(R.id.title, Color.parseColor(item.getTitleColor()))
                    .setTextColor(R.id.subtitle, Color.parseColor(item.getSubTitleColor()));
        } catch (Exception e) {
            LLog.e(e.toString());
        }

        Glide.with(mContext).load(item.getPicUrl()).into((ImageView) helper.getView(R.id.ava));
    }
}
