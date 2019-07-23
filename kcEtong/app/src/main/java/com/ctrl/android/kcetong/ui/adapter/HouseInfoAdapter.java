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
import com.ctrl.android.kcetong.model.HouseEstateInfo;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liu on 2017/2/7.
 */

public class HouseInfoAdapter extends LRecyclerView.Adapter<HouseInfoAdapter.HouseInfoAdapterViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<HouseEstateInfo> list = new ArrayList<>();

    private HouseInfoAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(String)v.getTag());
        }
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,String data);
    }

    public HouseInfoAdapter(Context context){
        mContext = context;
    }

    public void setList(List<HouseEstateInfo> list){
        this.list = list;
    }

    public void clear(){
        this.list.clear();
    }
    @Override
    public HouseInfoAdapter.HouseInfoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_house_info, parent, false);

        rootView.setOnClickListener(this);
        return new HouseInfoAdapter.HouseInfoAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(HouseInfoAdapter.HouseInfoAdapterViewHolder holder, int position) {
        HouseInfoAdapter.HouseInfoAdapterViewHolder houseInfoAdapterViewHolder = holder;

        HouseEstateInfo item = list.get(position);
        houseInfoAdapterViewHolder.tv_title.setText(item.getTitle());
        if(item.getOriginalImg()!=null&&!item.getOriginalImg().equals("")) {
            String str2=item.getOriginalImg().replace(" ", "");//去掉所用空格
            List<String> listUrls = Arrays.asList(str2.split(","));
//            Arad.imageLoader.load(listUrls.get(0)).placeholder(R.drawable.default_image).into(holder.iv_house_info);
            Glide.with(mContext).load(listUrls.get(0)).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                    .error(R.drawable.default_image).override(200,200).into(houseInfoAdapterViewHolder.iv_house_info);
        }else {
            houseInfoAdapterViewHolder.iv_house_info.setImageResource(R.drawable.default_image);
        }

        houseInfoAdapterViewHolder.tv_room.setText(item.getCommunityName()+item.getSittingRoom()+"室"+item.getLivingRoom()+"厅");
        if(item.getTotalPrice()!=null&&!item.getTotalPrice().equals(""))
            houseInfoAdapterViewHolder.tv_price.setText((int)Double.parseDouble(item.getTotalPrice())+"万");

//        Map<String,String> map = new HashMap<>();
//        map.put("title",item.getTitle());
//        map.put("time",item.getCreateTime());
//        map.put("price",item.getTotalPrice());
//        map.put("sittingRoom",item.getSittingRoom());
//        map.put("livingRoom",item.getLivingRoom());
//        map.put("bathRoom",item.getBathRoom());
//        map.put("area",item.getRoomAcreage());
//        map.put("title",item.getTitle());

        houseInfoAdapterViewHolder.itemView.setTag(item.getId());
    }

    public void setOnItemClickListener(HouseInfoAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    class HouseInfoAdapterViewHolder extends LRecyclerViewAdapter.ViewHolder {
        //图片
        private ImageView iv_house_info;
        private TextView    tv_title;
        private TextView          tv_room;
        private TextView          tv_price;
        public HouseInfoAdapterViewHolder(View itemView) {
            super(itemView);
            iv_house_info = (ImageView) itemView.findViewById(R.id.iv_house_info);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_room = (TextView) itemView.findViewById(R.id.tv_room);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}