package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.OrderBean;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by liu on 2018/3/7.
 */

public class MyOrderAdapter extends ListBaseAdapter<OrderBean.DataBean.OrderListBean> {
    private String type;
    private OnClickButtonListener mListener;
    public void setListener(MyOrderAdapter.OnClickButtonListener listener) {
        mListener = listener;
    }
    public MyOrderAdapter(Context context, String type) {
        super(context);
        this.type = type;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_order;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final OrderBean.DataBean.OrderListBean order      = mDataList.get(position);
        ImageView                              myOrderImg = holder.getView(R.id.iv_my_order_img);
        TextView                               myBuyName  = holder.getView(R.id.tv_buy_name);
        TextView                               myBuyPrice = holder.getView(R.id.tv_buy_price);
        TextView                               myBuyTotal = holder.getView(R.id.tv_buy_total);
        TextView                               myBuyCount = holder.getView(R.id.tv_buy_count);
        TextView                         myBuyTime  = holder.getView(R.id.tv_buy_time);
        TextView                         myOrderNumber  = holder.getView(R.id.tv_order_number);
        TextView                         myOrderBtn  = holder.getView(R.id.tv_order_btn);
        if(null!=order.getListPicUrl() && !order.getListPicUrl().equals("")){
            Glide.with(mContext).load(order.getListPicUrl())
                 .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop()
                 .into(myOrderImg);
        }else{
            myOrderImg.setImageResource(R.drawable.default_image);
        }
        myBuyName.setText(order.getProductName());
        if(order.getSellType().equals("0")){
            myBuyPrice.setText(order.getSellingPrice()+"元");
            myBuyTotal.setText(order.getTotalCost() + "元");
        }else{
            myBuyPrice.setText(order.getPointsDeductions()/order.getNums() + "积分");
            myBuyTotal.setText(order.getPointsDeductions() + "积分");
        }
        myBuyCount.setText("数量"+order.getNums());
        myBuyTime.setText(Utils.getDataFormatString(order.getCreateTime(), "yyyy-MM-dd HH:mm"));
        myOrderNumber.setText(order.getOrderNum());
        if("0".equals(type)){
            myOrderBtn.setVisibility(View.GONE);
        }else if("1".equals(type)){
            myOrderBtn.setText("收货");
        }else if("2".equals(type)){
            myOrderBtn.setText("评价");
        }else if("3".equals(type)){
            myOrderBtn.setText("删除");
        }
        if(mListener != null){
            myOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.click(position, order.getId(), order);
                }
            });
        }
        /*myOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(type)){
                    handler.obtainMessage(fragment.SUBMIT_ORDER, position, 0).sendToTarget();
                }else if("2".equals(type)){
                    Intent i = new Intent();
                    i.setClass(mActivity,OrderEvaluateRelease.class);
                    i.putExtra("order",order);
                    mActivity.startActivity(i);
                }else if("3".equals(type)){
                    handler.obtainMessage(fragment.REMOVE_ORDER, position, 0).sendToTarget();
                }
            }
        });*/
    }

    public interface OnClickButtonListener{
        void click(int position, String orderId, OrderBean.DataBean.OrderListBean order);
    }
}
