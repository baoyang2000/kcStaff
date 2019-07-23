package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ServiceOrder;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2017/1/19.
 */

public class MyOrderServiceAdapter extends LRecyclerView.Adapter<MyOrderServiceAdapter.MyOrderServiceAdapterViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<ServiceOrder> list = new ArrayList<>();

    private String state;//状态

    private List<ServiceOrder> pendingList = new ArrayList<>();
    private List<ServiceOrder> progressList = new ArrayList<>();
    private List<ServiceOrder> progressedList = new ArrayList<>();
    private List<ServiceOrder> endList = new ArrayList<>();

    private MyOrderServiceAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(Map<String,String>)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Map<String,String> data);
    }

    public MyOrderServiceAdapter(String state,Context context){
        this.state = state;
        mContext = context;
    }

    public void setList(List<ServiceOrder> list){
        this.list = list;
    }

    public void clear(){
        this.list.clear();
    }
    /**
     * 添加待受理
     *
     * @param pendingList
     */
    public void addPendingList(List<ServiceOrder> pendingList) {
        this.pendingList = pendingList;
    }
    /**
     * 添加已受理
     *
     * @param progressList
     */
    public void addProgressingList(List<ServiceOrder> progressList) {
        this.progressList = progressList;
    }
    /**
     * 添加服务中
     *
     * @param progressedList
     */
    public void addProgressedList(List<ServiceOrder> progressedList) {
        this.progressedList = progressedList;
    }

    /**
     * 添加结束
     *
     * @param endList
     */
    public void addEndList(List<ServiceOrder> endList) {
        this.endList = endList;
    }
    /**
     * 清除
     */
    public void clearPending() {
        this.pendingList.clear();
    }

    public void clearProgressing() {
        this.progressList.clear();
    }

    public void clearProgressed() {
        this.progressedList.clear();
    }

    public void clearEnd() {
        this.endList.clear();
    }

    @Override
    public MyOrderServiceAdapter.MyOrderServiceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_my_repairs_item, parent, false);

        rootView.setOnClickListener(this);
        return new MyOrderServiceAdapter.MyOrderServiceAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyOrderServiceAdapter.MyOrderServiceAdapterViewHolder holder, int position) {
        MyOrderServiceAdapter.MyOrderServiceAdapterViewHolder myOrderServiceAdapterViewHolder = holder;

        ServiceOrder serviceOrder ;
        if(StrConstant.MY_REPAIRS_ALL.equals(state)){
            serviceOrder = list.get(position);
            initData(serviceOrder,myOrderServiceAdapterViewHolder);
        }else if(StrConstant.MY_REPAIRS_PENDING.equals(state)){//待受理
            serviceOrder    = pendingList.get(position);
            initData(serviceOrder,myOrderServiceAdapterViewHolder);

        }else if(StrConstant.MY_REPAIRS_PROGRESSING.equals(state)) {//已受理
            serviceOrder    = progressList.get(position);
            initData(serviceOrder,myOrderServiceAdapterViewHolder);

        }else if(StrConstant.MY_REPAIRS_PROGRESSED.equals(state)){//服务中
            serviceOrder    = progressedList.get(position);
            initData(serviceOrder,myOrderServiceAdapterViewHolder);

        }else if(StrConstant.MY_REPAIRS_END.equals(state)){
            serviceOrder = endList.get(position);
            initData(serviceOrder,myOrderServiceAdapterViewHolder);
        }
//
    }
    private void initData(ServiceOrder serviceOrder,MyOrderServiceAdapterViewHolder holder){
        holder.tv_repairs_number.setText("服务编号："+serviceOrder.getRepairNum());
        holder.tv_repairs_type.setText("服务名称:  "+serviceOrder.getProductName());
        holder.tv_repairs_room.setText("预约房间："+serviceOrder.getBuilding()+"-"+serviceOrder.getUnit()+"-"+serviceOrder.getRoom());
        if(null != serviceOrder.getAppointmentTime()) {
            holder.tv_repairs_time.setText("预约时间：" + serviceOrder.getAppointmentTime());
        }
        if("0".equals(state)){
            holder.tv_my_repairs_status.setText("待受理");
            holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_red);
        }
        if("1".equals(state)){
            holder.tv_my_repairs_status.setText("已受理");
            holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_green);
        }
        if("2".equals(state)){
            holder.tv_my_repairs_status.setText("服务中");
            holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_yellow);
        }
        if("3".equals(state)){
//            if(serviceOrder.getEvaluateLevel().equals("3")){
//                holder.tv_my_repairs_status.setText("未评价");
//                holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_green);
//            }else {
//                holder.tv_my_repairs_status.setText("已评价");
//                holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_gray);
//            }
            holder.tv_my_repairs_status.setText("已结束");
            holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_gray);
        }
        if("4".equals(state)){//全部
            String handleStatus = serviceOrder.getHandleStatus();
            if("0".equals(handleStatus)){
                holder.tv_my_repairs_status.setText("待受理");
                holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_red);
            }
            if("1".equals(handleStatus)){
                holder.tv_my_repairs_status.setText("已受理");
                holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_green);
            }
            if("2".equals(handleStatus)){
                holder.tv_my_repairs_status.setText("服务中");
                holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_yellow);
            }
            if("3".equals(handleStatus)){
                holder.tv_my_repairs_status.setText("已结束");
                holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_gray);
            }
        }
        Map<String,String> map = new HashMap<>();
        map.put("id",serviceOrder.getId());
        map.put("handleStatus",serviceOrder.getHandleStatus());

        holder.itemView.setTag(map);
    }
    public void setOnItemClickListener(MyOrderServiceAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if("4".equals(state)){
            return this.list.size();//全部

        }else if ("0".equals(state)) {//待受理
            return this.pendingList.size();
        } else if ("1".equals(state)) {//已受理
            return this.progressList.size();
        } else if ("2".equals(state)) {//服务中
            return this.progressedList.size();
        }else if("3".equals(state)){//已结束
            return this.endList.size();
        }
        return 0;
    }

    class MyOrderServiceAdapterViewHolder extends LRecyclerViewAdapter.ViewHolder {

        //服务编号
        private TextView  tv_repairs_number;
        //服务名称
        private TextView  tv_repairs_type;
        //预约时间
        private TextView  tv_repairs_time;
        //状态
        private TextView  tv_my_repairs_status;
        //预约房间
        private TextView  tv_repairs_room;

        public MyOrderServiceAdapterViewHolder(View itemView) {
            super(itemView);
            tv_repairs_number = (TextView) itemView.findViewById(R.id.tv_repairs_number);
            tv_repairs_type = (TextView) itemView.findViewById(R.id.tv_repairs_type);
            tv_repairs_time = (TextView) itemView.findViewById(R.id.tv_repairs_time);
            tv_my_repairs_status = (TextView) itemView.findViewById(R.id.tv_my_repairs_status);
            tv_repairs_room = (TextView) itemView.findViewById(R.id.tv_repairs_room);
        }
    }
}
