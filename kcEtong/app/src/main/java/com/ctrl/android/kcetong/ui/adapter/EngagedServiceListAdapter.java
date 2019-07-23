package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ServiceProduct;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 特约服务 adapter
 * Created by Eric on 2015/10/13.
 */
public class EngagedServiceListAdapter extends LRecyclerView.Adapter<EngagedServiceListAdapter.EngagedServiceListAdapterViewHolder> implements View.OnClickListener {

    private Context             mContext;
    private List<ServiceProduct> list = new ArrayList<>();

    private EngagedServiceListAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(Map<String,String>)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Map<String,String> data);
    }

    public EngagedServiceListAdapter(Context context){
        mContext = context;
    }

    public void setList(List<ServiceProduct> list){
        this.list = list;
    }

    public void clear(){
        this.list.clear();
    }
    @Override
    public EngagedServiceListAdapter.EngagedServiceListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_engaged_service_list, parent, false);

        rootView.setOnClickListener(this);
        return new EngagedServiceListAdapter.EngagedServiceListAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(EngagedServiceListAdapter.EngagedServiceListAdapterViewHolder holder, int position) {
        EngagedServiceListAdapter.EngagedServiceListAdapterViewHolder engagedServiceListAdapterViewHolder = holder;

        ServiceProduct item = list.get(position);
        engagedServiceListAdapterViewHolder.tv_engaged_service_name.setText(item.getServiceName());
        engagedServiceListAdapterViewHolder.tv_engaged_service_sale.setText("已售"+item.getSalesVolume());
        engagedServiceListAdapterViewHolder.tv_engaged_service_price.setText("￥"+item.getSellingPrice()+"元");
//        Arad.imageLoader.load(item.getServiceUrl()).placeholder(R.drawable.default_image).into(holder.iv_engaged_service_list);
        Glide.with(mContext).load(item.getServiceUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image)
                .centerCrop().override(200,200).into(engagedServiceListAdapterViewHolder.iv_engaged_service_list);

        Map<String,String> map = new HashMap<>();
        map.put("serviceName",item.getServiceName());
        map.put("sellingPrice",item.getSellingPrice());
        map.put("infomation",item.getInfomation());
        map.put("img_url",item.getServiceUrl());
        map.put("serviceKindId",item.getServiceKindId());
        map.put("id",item.getId());

        engagedServiceListAdapterViewHolder.itemView.setTag(map);
    }
    public void setOnItemClickListener(EngagedServiceListAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class EngagedServiceListAdapterViewHolder extends LRecyclerViewAdapter.ViewHolder{
        @BindView(R.id.iv_engaged_service_list)//图片
                ImageView iv_engaged_service_list;
        @BindView(R.id.tv_engaged_service_name)//
                TextView  tv_engaged_service_name;
        @BindView(R.id.tv_engaged_service_sale)//
                TextView  tv_engaged_service_sale;

        @BindView(R.id.tv_engaged_service_price)//
                TextView tv_engaged_service_price;

        public EngagedServiceListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
