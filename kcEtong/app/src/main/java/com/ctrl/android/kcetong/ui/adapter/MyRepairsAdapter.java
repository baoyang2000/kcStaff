package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Repair;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by cxl on 2017/1/22.
 */

public class MyRepairsAdapter extends ListBaseAdapter<Repair> {

    public MyRepairsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_repairs_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        Repair   repair               = mDataList.get(position);
        TextView tv_repairs_number    = holder.getView(R.id.tv_repairs_number);
        TextView tv_repairs_type      = holder.getView(R.id.tv_repairs_type);
        TextView tv_repairs_time      = holder.getView(R.id.tv_repairs_time);
        TextView tv_my_repairs_status = holder.getView(R.id.tv_my_repairs_status);
        TextView tv_repairs_room      = holder.getView(R.id.tv_repairs_room);
        tv_repairs_number.setText("报修编号：" + repair.getRepairNum());
        tv_repairs_type.setText("报修类型：" + repair.getRepairKindName());
        tv_repairs_room.setText("报修房间：" + repair.getCommunityName() + " " + repair.getBuilding() + "-" + repair.getUnit() + "-" + repair.getRoom());
        if (repair.getCreateTime() != null) {
            tv_repairs_time.setText("报修时间：" + TimeUtil.date(Long.parseLong(repair.getCreateTime())));
        }
        if (repair.getHandleStatus().equals("0")) {
            tv_my_repairs_status.setText("待处理");
            tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_red);
        } else if (repair.getHandleStatus().equals("1")) {
            tv_my_repairs_status.setText("处理中");
            tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_green);
        } else if (repair.getHandleStatus().equals("2")) {
            tv_my_repairs_status.setText("已处理");
            tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_yellow);
        } else if (repair.getHandleStatus().equals("3")) {
            tv_my_repairs_status.setText("已结束");
            tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_gray);
        }
    }
}
