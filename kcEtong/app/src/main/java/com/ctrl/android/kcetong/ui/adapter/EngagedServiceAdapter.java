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
import com.ctrl.android.kcetong.model.ServiceKind;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/1/18.
 */

public class EngagedServiceAdapter extends LRecyclerView.Adapter<EngagedServiceAdapter.EngagedServiceAdapterViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<ServiceKind> list = new ArrayList<>();

    private EngagedServiceAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(String)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,String data);
    }

    public EngagedServiceAdapter(Context context){
        mContext = context;
    }

    public void setList(List<ServiceKind> list){
        this.list = list;
    }

    public void clear(){
        this.list.clear();
    }
    @Override
    public EngagedServiceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_engaged_service, parent, false);

        rootView.setOnClickListener(this);
        return new EngagedServiceAdapter.EngagedServiceAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(EngagedServiceAdapterViewHolder holder, int position) {
        EngagedServiceAdapterViewHolder engagedServiceAdapterViewHolder = holder;
        ServiceKind serviceKind = list.get(position);
        engagedServiceAdapterViewHolder.tv_engaged_service.setText(serviceKind.getSortsName());

        Glide.with(mContext).load(serviceKind.getSortsUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image)
             .centerCrop().override(200,200).into(engagedServiceAdapterViewHolder.iv_engaged_service);

        engagedServiceAdapterViewHolder.itemView.setTag(serviceKind.getId());
    }
    public void setOnItemClickListener(EngagedServiceAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class EngagedServiceAdapterViewHolder extends LRecyclerViewAdapter.ViewHolder {
        //图片
        private ImageView iv_engaged_service;
        //快递时间
        private TextView tv_engaged_service;

        public EngagedServiceAdapterViewHolder(View itemView) {
            super(itemView);
            iv_engaged_service = (ImageView) itemView.findViewById(R.id.iv_engaged_service);
            tv_engaged_service = (TextView) itemView.findViewById(R.id.tv_engaged_service);
        }
    }
}
