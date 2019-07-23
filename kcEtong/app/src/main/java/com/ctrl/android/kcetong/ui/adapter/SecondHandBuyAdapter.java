package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.UsedGoods;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.ui.view.RoundImageView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu on 2017/1/21.
 */

public class SecondHandBuyAdapter extends LRecyclerView.Adapter<SecondHandBuyAdapter.SecondHandBuyViewHolder> implements View.OnClickListener{
    private Context mContext;

    private List<UsedGoods> usedGoodsList;

    private SecondHandBuyAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(Map<String,String>)v.getTag());
        }
    }
    public void setOnItemClickListener(SecondHandBuyAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Map<String,String> data);
    }

    public SecondHandBuyAdapter(Context mContext) {
        this.mContext = mContext;
//        this.surveyType = surveyType;
        usedGoodsList = new ArrayList<>();
    }

    /**
     * 添加
     *
     * @param usedGoodsList
     */
    public void addGoodsList(List<UsedGoods> usedGoodsList) {
        this.usedGoodsList = usedGoodsList;
        notifyDataSetChanged();
    }

    /**
     * 清除
     */
    public void clear() {
        this.usedGoodsList.clear();
    }

    @Override
    public SecondHandBuyAdapter.SecondHandBuyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_second_hand_buy_item, parent, false);
        rootView.setOnClickListener(this);
        return new SecondHandBuyAdapter.SecondHandBuyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SecondHandBuyAdapter.SecondHandBuyViewHolder holder, int position) {
        SecondHandBuyAdapter.SecondHandBuyViewHolder buyHolder = holder;

        UsedGoods usedGoods=usedGoodsList.get(position);
        buyHolder.tv_second_hand_item_name.setText(usedGoods.getProprietorName());
        buyHolder.tv_second_hand_item_location.setText(usedGoods.getCommunityName() + " " + usedGoods.getBuilding()+"-"+usedGoods.getUnit()+"-"+usedGoods.getRoom());
        DecimalFormat df = new DecimalFormat("#.00");
        buyHolder.tv_second_hand_item_price.setText("￥" +df.format(usedGoods.getSellingPrice()));
//        long currentTime = System.currentTimeMillis();
//        Log.i("demo", "当前时间" + currentTime);
//        Log.i("demo", "传人时间" + usedGoods.getCreateTime());
        // long time = currentTime - Long.parseLong(usedGoods.getCreateTime());
        buyHolder.tv_second_hand_item_time.setText(TimeUtil.dateTime(usedGoods.getCreateTime()));
        buyHolder.tv_second_hand_item_title.setText(usedGoods.getTitle());
//        Glide.with(mContext).load(usedGoods.getImgUrl()==null || usedGoods.getImgUrl().equals("")
//                ?"aa":usedGoods.getImgUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.touxiang2x).centerCrop().into(buyHolder.iv_photo_buy);

        CustomApplication.setImageWithDiffDisplayImageOptions(usedGoods.getImgUrl()==null || usedGoods.getImgUrl().equals("")
                ?"aa":usedGoods.getImgUrl(),buyHolder.iv_photo_buy,null,mBannerOptions);
        Map<String,String> map = new HashMap<>();
        map.put("usedGoodsId",usedGoods.getId());
        map.put("position",position+"");
//        intent.addFlags(StrConstant.MY_BABY_DETAIL);
        map.put("releaseTime", usedGoods.getCreateTime());
        holder.itemView.setTag(map);
    }
    public static DisplayImageOptions mBannerOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.touxiang2x)
                                                                                        // 图片加载的时候显示的图片
                                                                                        .showImageForEmptyUri(R.drawable.touxiang2x)
                                                                                        // 图片加载地址为空的时候显示的图片
                                                                                        .showImageOnFail(R.drawable.touxiang2x)
                                                                                        // 图片加载失败的时候显示的图片
                                                                                        .cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    @Override
    public int getItemCount() {
        return usedGoodsList == null?0:usedGoodsList.size();
    }

    class SecondHandBuyViewHolder extends LRecyclerViewAdapter.ViewHolder{

        @BindView(R.id.tv_second_hand_item_name)
        TextView       tv_second_hand_item_name;
        @BindView(R.id.tv_second_hand_item_time)
        TextView       tv_second_hand_item_time;
        @BindView(R.id.tv_second_hand_item_location)
        TextView       tv_second_hand_item_location;
        @BindView(R.id.tv_second_hand_item_price)
        TextView       tv_second_hand_item_price;
        @BindView(R.id.tv_second_hand_item_title)
        TextView       tv_second_hand_item_title;
        @BindView(R.id.iv_photo_buy)
        RoundImageView iv_photo_buy;
        public SecondHandBuyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
