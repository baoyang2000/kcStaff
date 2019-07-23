package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Complaint;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by cxl on 2017/1/12.
 */

public class MyComplaintAdapter extends ListBaseAdapter<Complaint.DataBean.ComplaintListBean> {


    public MyComplaintAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_complaint_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        LLog.w("????????????????????");
        Complaint.DataBean.ComplaintListBean mComplaint          = mDataList.get(position);
        TextView                             tv_complaint_number = holder.getView(R.id.tv_complaint_number);
        TextView                             tv_complaint_type   = holder.getView(R.id.tv_complaint_type);
        TextView                             tv_complaint_room   = holder.getView(R.id.tv_complaint_room);
        TextView                             tv_complaint_time   = holder.getView(R.id.tv_complaint_time);
        TextView                             tv_complaint_status = holder.getView(R.id.tv_complaint_status);
        tv_complaint_number.setText("投诉编号：" + mComplaint.getComplaintNum());
        tv_complaint_type.setText("投诉类型：" + mComplaint.getComplaintKindName());
        tv_complaint_room.setText("投诉房间：" + mComplaint.getCommunityName() + " " + mComplaint.getBuilding() + "-" + mComplaint.getUnit() + "-" + mComplaint.getRoom());
        if (mComplaint.getCreateTime() != null) {
            tv_complaint_time.setText("投诉时间：" + TimeUtil.date(Long.parseLong(mComplaint.getCreateTime())));
        }
        if (mComplaint.getHandleStatus().equals("0")) {
            tv_complaint_status.setText("待处理");
            tv_complaint_status.setBackgroundResource(R.drawable.tv_shape_red);
        } else if (mComplaint.getHandleStatus().equals("1")) {
            tv_complaint_status.setText("处理中");
            tv_complaint_status.setBackgroundResource(R.drawable.tv_shape_green);
        } else if (mComplaint.getHandleStatus().equals("2")) {
            tv_complaint_status.setText("已处理");
            tv_complaint_status.setBackgroundResource(R.drawable.tv_shape_yellow);
        } else if (mComplaint.getHandleStatus().equals("3")) {
            tv_complaint_status.setText("已结束");
            tv_complaint_status.setBackgroundResource(R.drawable.tv_shape_gray);
        }
    }
}