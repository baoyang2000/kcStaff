package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Visit;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class MyVisitAdapter extends LRecyclerView.Adapter<MyVisitAdapter.VisitViewHolder> {

    private Context     context;
    private List<Visit> data;

    public MyVisitAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();

    }

    public void setListData(List<Visit> list) {
        data = list;
        this.notifyDataSetChanged();
    }

    @Override
    public VisitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_visit_item, null);
        return new VisitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VisitViewHolder holder, int position) {
        final Visit visit = data.get(position);

        //0：预约到访、1：突发到访
        if(visit.getType() == 0){
            holder.visitFlg.setImageResource(R.drawable.appointment_flg_icon);
        } else {
            holder.visitFlg.setImageResource(R.drawable.burst_appointment_flg_icon);
            //holder.visit_flg.setVisibility(View.GONE);
        }

        holder.visitNum.setText("到访编号: " + S.getStr(visit.getVisitNum()));
        holder.visitTime.setText(Utils.getDateStrFromStamp(ConstantsData.YYYY_MM_DD, visit.getArriveTime() == null ? "0000000" : visit.getArriveTime()));
        holder.visitName.setText(S.getStr(visit.getVisitorName()));

        //0：已预约 1：已结束 2：待处理 3：其他
        if(visit.getReturnStatus() == 0){
            holder.visitStatus.setText("已预约");
            holder.visitStatus.setBackgroundResource(R.drawable.orange_bg_shap);
        } else if(visit.getReturnStatus() == 1){
            holder.visitStatus.setText("已结束");
            holder.visitStatus.setBackgroundResource(R.drawable.gray_bg_shap);
        } else if(visit.getReturnStatus() == 2){
            holder.visitStatus.setText("待处理");
            holder.visitStatus.setBackgroundResource(R.drawable.green_bg_shap);
        } else if(visit.getReturnStatus() == 3){
            holder.visitStatus.setText("其他");
            holder.visitStatus.setBackgroundResource(R.color.text_black);
            holder.visitStatus.setBackgroundResource(R.drawable.gray_bg_shap);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    class VisitViewHolder extends LRecyclerViewAdapter.ViewHolder {

        TextView     visitNum;
        TextView     visitTime;
        TextView     visitName;
        TextView     visitStatus;
        ImageView    visitFlg;
        public VisitViewHolder(View itemView) {
            super(itemView);
            visitNum= (TextView) itemView.findViewById(R.id.visit_num);
            visitTime= (TextView) itemView.findViewById(R.id.visit_time);
            visitName=(TextView) itemView.findViewById(R.id.visit_name);
            visitStatus=(TextView) itemView.findViewById(R.id.visit_status);
            visitFlg= (ImageView) itemView.findViewById(R.id.visit_flg);
        }
    }
    }

