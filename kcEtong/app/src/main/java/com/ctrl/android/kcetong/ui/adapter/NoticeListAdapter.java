package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Notice;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class NoticeListAdapter extends LRecyclerView.Adapter<NoticeListAdapter.NoticeListViewHolder>{
    private Context                                      context;
    private List<Notice.DataBean.PropertyNoticeListBean> data;

    public NoticeListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();

    }

    public void setListData(List<Notice.DataBean.PropertyNoticeListBean> list){
        data = list;
        this.notifyDataSetChanged();
    }
    @Override
    public NoticeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_list_item, null);
        return new NoticeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoticeListViewHolder holder, int position) {
        Notice.DataBean.PropertyNoticeListBean bean =data.get(position);
        holder.notice_title.setText(bean.getNoticeTitle());
        holder.tv_red_round.setVisibility(View.GONE);

        if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){//非管理员
            //公告级别（0：重要、1：一般）
            if(bean.getLevel().equals("0")){
                if(bean.getStatus().equals("0")) {
                    holder.notice_title.setTextColor(context.getResources().getColor(R.color.text_red));
                    holder.tv_red_round.setBackgroundResource(R.drawable.round_red_shap);
                    holder.tv_red_round.setVisibility(View.VISIBLE);
                }
                if(bean.getStatus().equals("1")) {
                    holder.notice_title.setTextColor(context.getResources().getColor(R.color.text_black));
                    holder.tv_red_round.setBackgroundResource(R.drawable.round_black_shap);
                    holder.tv_red_round.setVisibility(View.VISIBLE);
                }
            }

            if(bean.getStatus().equals("1")){
                holder.notice_status_flg.setText(R.string.have_sign);
                //holder.notice_status_flg.setBackgroundResource(R.drawable.notice_status_sign_y);
                holder.notice_status_flg.setBackgroundResource(R.drawable.gray_bg_shap);
            } else if(bean.getStatus().equals("0")){
                holder.notice_status_flg.setText(R.string.havnt_sign);
                //holder.notice_status_flg.setBackgroundResource(R.drawable.notice_status_sign_n);
                holder.notice_status_flg.setBackgroundResource(R.drawable.green_bg_shap);
            }
        }else {
            holder.notice_status_flg.setVisibility(View.INVISIBLE);
        }

        holder.notice_time.setText(TimeUtil.date(Long.parseLong(String.valueOf(bean.getCreateTime()))));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }



    class NoticeListViewHolder extends LRecyclerViewAdapter.ViewHolder{
                TextView notice_title;//公告标题
                TextView notice_time;//公告发布时间
                TextView tv_red_round;//红点
                TextView notice_status_flg;//公告签收情况

        public NoticeListViewHolder(View itemView) {
            super(itemView);
            notice_title= (TextView) itemView.findViewById(R.id.notice_title);
            notice_time= (TextView) itemView.findViewById(R.id.notice_time);
            tv_red_round= (TextView) itemView.findViewById(R.id.tv_red_round);
            notice_status_flg=(TextView) itemView.findViewById(R.id.notice_status_flg);
        }
    }
}
