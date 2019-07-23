package com.jh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jh.R;
import com.jhsdk.bean.api.JHDevice;
import com.jhsdk.constant.JHConstant;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.Holder> implements View.OnClickListener{
    private LayoutInflater inflater;
    private List<JHDevice> deviceList;

    public interface OnItemClickLitener {
        void onItemClick(View view, String data);
    }

    private OnItemClickLitener mOnItemClickLitener = null;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickLitener  != null){
            mOnItemClickLitener .onItemClick(v,(String)v.getTag());
        }
    }

    public DevicesAdapter(Context context, List<JHDevice> deviceList) {
        inflater = LayoutInflater.from(context);
        this.deviceList = deviceList;

    }

    @Override
    public DevicesAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_adapter_devices, parent,false);
        Holder holder = new Holder(view);

        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        JHDevice device = deviceList.get(position);
        holder.tViewName.setText(device.getCommName() + (device.getUnitsInfo() == null ? "": "-" + device.getUnitsInfo()) +
                                 (device.getDisplayName() == null ? "": "-" + device.getDisplayName()));

//        holder.tViewAccount.setText(device.getAccounts());

        if(device.getDevicesType() == JHConstant.OUTDOOR_TYPE ){
            //门口机
            holder.iv_door.setBackgroundResource(R.mipmap.unit_door);
        }else if(device.getDevicesType() == JHConstant.WALL_TYPE){
            //围墙机
            holder.iv_door.setBackgroundResource(R.mipmap.door);
        }else if(device.getDevicesType() == JHConstant.INDOOR_TYPE){
            //室内机
            holder.iv_door.setBackgroundResource(R.mipmap.indoor);
        }else if(device.getDevicesType() == JHConstant.VILLA_TYPE){
            //别墅机
            holder.iv_door.setBackgroundResource(R.mipmap.villa);
        }else if(device.getDevicesType() == JHConstant.MANAGE_TYPE){
            //管理机
            holder.iv_door.setBackgroundResource(R.mipmap.manage);
        }

        holder.itemView.setTag(device.getAccounts());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        Holder holder;
//        if(view == null){
//            holder = new Holder();
//            view = inflater.inflate(R.layout.item_adapter_devices, null);
//            holder.tViewName = (TextView) view.findViewById(R.id.tViewName);
//            holder.tViewAccount = (TextView) view.findViewById(R.id.tViewAccount);
//            view.setTag(holder);
//        }else{
//            holder = (Holder) view.getTag();
//        }
//
//        JHDevice device = deviceList.get(i);
//        holder.tViewName.setText(device.getDisplayName());
//        holder.tViewAccount.setText(device.getAccounts());
//        return view;
//    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tViewName;
        TextView tViewAccount;
        ImageView iv_door;

        public Holder(View itemView) {
            super(itemView);
            tViewName = (TextView) itemView.findViewById(R.id.tViewName);
            iv_door = (ImageView) itemView.findViewById(R.id.iv_door);
        }
    }
}
