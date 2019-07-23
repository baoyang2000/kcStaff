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
import com.ctrl.android.kcetong.model.RegionList;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2017/2/3.
 */

public class RegionAdapter extends LRecyclerView.Adapter<RegionAdapter.RegionAdapterViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<RegionList> list = new ArrayList<>();

    private RegionAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(Map<String,String>)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Map<String,String> data);
    }

    public RegionAdapter(Context context){
        mContext = context;
    }

    public void setList(List<RegionList> list){
        this.list = list;
    }

    public void clear(){
        this.list.clear();
    }
    @Override
    public RegionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_engaged_service, parent, false);

        rootView.setOnClickListener(this);
        return new RegionAdapter.RegionAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RegionAdapterViewHolder holder, int position) {
        RegionAdapterViewHolder regionAdapterViewHolder = holder;
        RegionList regionList = list.get(position);
        regionAdapterViewHolder.tv_engaged_service.setText(regionList.getRegionalName());

        Glide.with(mContext).load(regionList.getImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image)
             .centerCrop().into(regionAdapterViewHolder.iv_engaged_service);

        Map<String,String> map = new HashMap<>();
        map.put("id",regionList.getId());
        map.put("regionName",regionList.getRegionalName());
        holder.itemView.setTag(map);
    }

    public void setOnItemClickListener(RegionAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class RegionAdapterViewHolder extends LRecyclerViewAdapter.ViewHolder {
        //图片
        private ImageView iv_engaged_service;
        //快递时间
        private TextView  tv_engaged_service;

        public RegionAdapterViewHolder(View itemView) {
            super(itemView);
            iv_engaged_service = (ImageView) itemView.findViewById(R.id.iv_engaged_service);
            tv_engaged_service = (TextView) itemView.findViewById(R.id.tv_engaged_service);
        }
    }
}
