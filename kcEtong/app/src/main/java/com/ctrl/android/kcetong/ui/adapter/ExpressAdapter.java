package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Express;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/1/17.
 */

public class ExpressAdapter extends LRecyclerView.Adapter<ExpressAdapter.ExpressAdapterViewHolder> implements View.OnClickListener{

    private Context       mContext;
    private List<Express> list = new ArrayList<>();

    private OnRecyclerViewItemClickListener mOnItemClickListener  = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(String)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,String data);
    }

    public ExpressAdapter(Context context){
        mContext = context;
    }

    public void setList(List<Express> list){
        this.list = list;
    }

    public void clear(){
        this.list.clear();
    }
    @Override
    public ExpressAdapter.ExpressAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.express_item, parent, false);

        rootView.setOnClickListener(this);
        return new ExpressAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ExpressAdapter.ExpressAdapterViewHolder holder, int position) {
        ExpressAdapterViewHolder expressAdapterViewHolder = holder;

        Express express = list.get(position);
        expressAdapterViewHolder.express_type.setText(express.getLogisticsCompanyName());
        expressAdapterViewHolder.express_time.setText(D.getDateStrFromStamp("yyyy-MM-dd HH:mm",express.getCreateTime()));

        //领取状态（0：待领取、1：已领取）
        if(express.getStatus() == 0){
            expressAdapterViewHolder.express_state.setText("未领取");
            expressAdapterViewHolder.express_state.setBackgroundResource(R.drawable.orange_bg_shap);
        } else {
            expressAdapterViewHolder.express_state.setText("已领取");
            expressAdapterViewHolder.express_state.setBackgroundResource(R.drawable.gray_bg_shap);
        }
        expressAdapterViewHolder.itemView.setTag(express.getId());
    }

    public void setOnItemClickListener(ExpressAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    class ExpressAdapterViewHolder extends LRecyclerViewAdapter.ViewHolder {
        //快递类型
         private TextView express_type;
        //快递时间
        private TextView express_time;
        //快递状态
        private TextView express_state;
        public ExpressAdapterViewHolder(View itemView) {
            super(itemView);
            express_type = (TextView) itemView.findViewById(R.id.express_type);
            express_time = (TextView) itemView.findViewById(R.id.express_time);
            express_state = (TextView) itemView.findViewById(R.id.express_state);
        }
    }
}
