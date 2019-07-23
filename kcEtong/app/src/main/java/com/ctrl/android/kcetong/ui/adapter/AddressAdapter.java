package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ReceiveAddress;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;


/**
 * Created by liu on 2018/1/15.
 */

public class AddressAdapter extends ListBaseAdapter<ReceiveAddress> {
    private OnClickButtonListener mListener;

    public void setListener(OnClickButtonListener listener) {
        mListener = listener;
    }

    public AddressAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_address;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView  tv_name    = holder.getView(R.id.tv_name);
        TextView  tv_phone   = holder.getView(R.id.tv_phone);
        TextView  tv_address = holder.getView(R.id.tv_address);
        ImageView iv_default = holder.getView(R.id.iv_default);
        TextView  tv_delete  = holder.getView(R.id.tv_delete);
        TextView  tv_edit    = holder.getView(R.id.tv_edit);

        final ReceiveAddress bean = mDataList.get(position);
        tv_name.setText(bean.getReceiveName());
        tv_phone.setText(bean.getMobile());
        tv_address.setText(bean.getAddress());

        if("1".equals(bean.getIsDefault())){
            iv_default.setImageResource(R.drawable.check_box_selected);
        }else {
            iv_default.setImageResource(R.drawable.check_box_normal);
        }
        if (mListener != null) {
            tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.edit(position, bean);
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.delete(position, bean.getReceiveAddressId());
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.select(position, bean);
                }
            });
            iv_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.check(bean.getIsDefault(), bean.getReceiveAddressId());
                }
            });
        }
    }

    public interface OnClickButtonListener {
        void edit(int position, ReceiveAddress data);

        void delete(int position, String id);

        void select(int position, ReceiveAddress data);

        void check(String isDefault, String id);
    }
}
