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
import com.ctrl.android.kcetong.model.RegionDetailList;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu on 2017/2/3.
 */

public class RegionDetailListAdapter extends LRecyclerView.Adapter<RegionDetailListAdapter.RegionDetailListAdapterViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<RegionDetailList> list = new ArrayList<>();

    private RegionDetailListAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(Map<String,String>)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Map<String,String> data);
    }

    public RegionDetailListAdapter(Context context){
        mContext = context;
    }

    public void setList(List<RegionDetailList> list){
        this.list = list;
    }

    public void clear(){
        this.list.clear();
    }
    @Override
    public RegionDetailListAdapter.RegionDetailListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_region_detail_list, parent, false);

        rootView.setOnClickListener(this);
        return new RegionDetailListAdapter.RegionDetailListAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RegionDetailListAdapter.RegionDetailListAdapterViewHolder holder, int position) {
        RegionDetailListAdapter.RegionDetailListAdapterViewHolder regionDetailViewHolder = holder;

        RegionDetailList item = list.get(position);
        regionDetailViewHolder.tv_region_detail_name.setText(item.getPropertiesName());
        Glide.with(mContext).load(item.getPropertiesImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image)
             .centerCrop().into(regionDetailViewHolder.iv_region_detail);
        Map<String,String> map = new HashMap<>();
        map.put("id",item.getId());//置业id
        map.put("regionalManagementId",item.getRegionalManagementId());//地域id
        map.put("PropertiesName",item.getPropertiesName());
        map.put("img_url",item.getPropertiesImg());
        map.put("info",item.getInformation());
        map.put("phone",item.getPhone());

        regionDetailViewHolder.itemView.setTag(map);
    }
    public void setOnItemClickListener(RegionDetailListAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class RegionDetailListAdapterViewHolder extends LRecyclerViewAdapter.ViewHolder{
        @BindView(R.id.iv_region_detail)//图片
                ImageView iv_region_detail;
        @BindView(R.id.tv_region_detail)//
                TextView  tv_region_detail_name;

        public RegionDetailListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

