package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ChoiceGroup;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by liu on 2018/3/5.
 */

public class ChoiceAdapter extends ListBaseAdapter<ChoiceGroup> {
    public ChoiceAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_choice;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView iv_img   = holder.getView(R.id.iv_img);
        TextView  tv_title = holder.getView(R.id.tv_title);
        TextView tv_price   = holder.getView(R.id.tv_price);
        TextView tv_time   = holder.getView(R.id.tv_time);
        TextView tv_sell_num   = holder.getView(R.id.tv_sell_num);

        ChoiceGroup data = mDataList.get(position);
        Glide.with(mContext).load(S.getStr(data.getListPicUrl()))
             .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
             .into(iv_img);
        tv_title.setText(data.getProductName() == null ? "" : data.getProductName());
        tv_price.setText("￥" + data.getSellingPrice());
        tv_time.setText(data.getOverTime());
        tv_sell_num.setText("已售：" + data.getSalesVolume());
    }
}
