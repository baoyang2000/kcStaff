package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Surround;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by liu on 2018/2/9.
 */

public class ServiceDetAdapter extends ListBaseAdapter<Surround.DataBean.ServiceListBean.BusinessListBean> {
    public ServiceDetAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_service;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final Surround.DataBean.ServiceListBean.BusinessListBean bean = mDataList.get(position);

        LinearLayout ll_item = holder.getView(R.id.ll_item);
        ImageView    iv_img  = holder.getView(R.id.iv_img);
        TextView     tv_name = holder.getView(R.id.tv_name);
        Glide.with(mContext).load(bean.getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).
                error(R.drawable.default_image).centerCrop().into(iv_img);
        tv_name.setText(bean.getTitle() == null ? "" : bean.getTitle());
        /*ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServiceDetailActivity.class);
                intent.putExtra("data", bean);
                mContext.startActivity(intent);
            }
        });*/
    }
}
