package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ShopArround_Easy;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public class EasyShopArroundListAdapter extends LRecyclerView.Adapter<EasyShopArroundListAdapter.EasyShopArroundViewHolder> {
    private   Context              context;
    private List<ShopArround_Easy> data;

    public EasyShopArroundListAdapter(Context context){
        this.context=context;
        data= new ArrayList<>();

    }
    public void setListData(List<ShopArround_Easy> list){
        data = list;
        this.notifyDataSetChanged();
    }

    @Override
    public EasyShopArroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.easy_shop_arround_list_item, null);
        return new EasyShopArroundListAdapter.EasyShopArroundViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EasyShopArroundViewHolder holder, int position) {
        final ShopArround_Easy shop = data.get(position);

        holder.easy_shop_arround_name.setText((position + 1) + "." + S.getStr(shop.getShopName()));
        holder.easy_shop_arround_address.setText(S.getStr(shop.getShopAddress()));
        holder.easy_shop_arround_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MessageUtils.showShortToast(mActivity,shop.getShopName() + shop.getShopPhoneNum());
                if(!S.isNull(shop.getShopPhoneNum())){
                    Utils.dial(context, shop.getShopPhoneNum());
                } else {
                    MessageUtils.showShortToast(context, StrConstant.HAVE_NO_TEL);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
    class EasyShopArroundViewHolder extends LRecyclerViewAdapter.ViewHolder{
        TextView  easy_shop_arround_name;//商家名称
        TextView  easy_shop_arround_address;//商家地址
        ImageView easy_shop_arround_tel; //商家电话

        public EasyShopArroundViewHolder(View itemView) {
            super(itemView);
            easy_shop_arround_name= (TextView) itemView.findViewById(R.id.easy_shop_arround_name);
            easy_shop_arround_address= (TextView) itemView.findViewById(R.id.easy_shop_arround_address);
            easy_shop_arround_tel= (ImageView) itemView.findViewById(R.id.easy_shop_arround_tel);

        }
    }
}
