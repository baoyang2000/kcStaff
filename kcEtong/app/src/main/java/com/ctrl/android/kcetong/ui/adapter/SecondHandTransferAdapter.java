package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.GoodPic;
import com.ctrl.android.kcetong.model.UsedGoods;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.view.RoundImageView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 二手交易详细页
 * Created by liu on 2017/1/21.
 */

public class SecondHandTransferAdapter extends LRecyclerView.Adapter<SecondHandTransferAdapter.SecondHandTransferViewHolder> implements View.OnClickListener{

    private Context mContext;

    private List<UsedGoods> usedGoodsList;
    private List<GoodPic> mGoodPicList = new ArrayList<>();
    private String handType;
    private UsedGoods usedGoods;

    private SecondHandTransferAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(Map<String,String>)v.getTag());
        }
    }
    public void setOnItemClickListener(SecondHandTransferAdapter.OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,Map<String,String> data);
    }

    public SecondHandTransferAdapter(String handType,Context mContext) {
        this.mContext = mContext;
        this.handType = handType;
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
    public SecondHandTransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_second_hand_item, parent, false);
        rootView.setOnClickListener(this);
        return new SecondHandTransferAdapter.SecondHandTransferViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SecondHandTransferViewHolder holder, int position) {
        SecondHandTransferViewHolder secondHolder = holder;
        usedGoods = usedGoodsList.get(position);
        mGoodPicList = usedGoods.getUsedGoodPicSubList();
        secondHolder.tv_second_hand_item_name.setText(usedGoods.getProprietorName());
        secondHolder.tv_second_hand_item_location.setText(usedGoods.getCommunityName() + " " + usedGoods.getBuilding()+"-"+usedGoods.getUnit()+"-"+usedGoods.getRoom());
        holder.tv_second_hand_item_price.setText("￥" + Utils.get2Double(usedGoods.getSellingPrice()));
        holder.tv_second_hand_item_time.setText(TimeUtil.dateTime(usedGoods.getCreateTime()));
        holder.tv_second_hand_item_title.setText(usedGoods.getTitle());

        if (mGoodPicList == null || mGoodPicList.size()< 1){
            holder.iv01_second_hand.setVisibility(View.GONE);
            holder.iv02_second_hand.setVisibility(View.GONE);
            holder.iv03_second_hand.setVisibility(View.GONE);
        }else if (mGoodPicList.size() == 1) {
            holder.iv01_second_hand.setVisibility(View.VISIBLE);
//            Glide.with(mContext).load(mGoodPicList.get(0).getOriginalImg() == null || mGoodPicList.get(0).getOriginalImg().equals("") ? "aa"
//                    : mGoodPicList.get(0).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop().into(secondHolder.iv01_second_hand);
            CustomApplication.setImageWithDiffDisplayImageOptions(mGoodPicList.get(0).getOriginalImg() == null || mGoodPicList.get(0).getOriginalImg().equals("") ? "aa"
                    : mGoodPicList.get(0).getOriginalImg(),secondHolder.iv01_second_hand,null,mBannerOptions1);
            holder.iv02_second_hand.setVisibility(View.INVISIBLE);
            holder.iv03_second_hand.setVisibility(View.INVISIBLE);
        } else if (mGoodPicList.size() == 2) {
            holder.iv01_second_hand.setVisibility(View.VISIBLE);
            holder.iv02_second_hand.setVisibility(View.VISIBLE);

//            Glide.with(mContext).load(mGoodPicList.get(0).getOriginalImg() == null || mGoodPicList.get(0).getOriginalImg().equals("") ?
//                    "aa" : mGoodPicList.get(0).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop().into(secondHolder.iv01_second_hand);
//            Glide.with(mContext).load(mGoodPicList.get(1).getOriginalImg() == null || mGoodPicList.get(1).getOriginalImg().equals("") ? "aa"
//                    : mGoodPicList.get(1).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop().into(secondHolder.iv02_second_hand);

            CustomApplication.setImageWithDiffDisplayImageOptions(mGoodPicList.get(0).getOriginalImg() == null || mGoodPicList.get(0).getOriginalImg().equals("") ?
                    "aa" : mGoodPicList.get(0).getOriginalImg(),secondHolder.iv01_second_hand,null,mBannerOptions1);
            CustomApplication.setImageWithDiffDisplayImageOptions(mGoodPicList.get(1).getOriginalImg() == null || mGoodPicList.get(1).getOriginalImg().equals("") ? "aa"
                    : mGoodPicList.get(1).getOriginalImg(),secondHolder.iv02_second_hand,null,mBannerOptions1);
        } else if (mGoodPicList.size() == 3) {
            holder.iv01_second_hand.setVisibility(View.VISIBLE);
            holder.iv02_second_hand.setVisibility(View.VISIBLE);
            holder.iv03_second_hand.setVisibility(View.VISIBLE);

//            Glide.with(mContext).load(mGoodPicList.get(0).getOriginalImg() == null || mGoodPicList.get(0).getOriginalImg().equals("") ?
//                    "aa" : mGoodPicList.get(0).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop().into(secondHolder.iv01_second_hand);
//            Glide.with(mContext).load(mGoodPicList.get(1).getOriginalImg() == null || mGoodPicList.get(1).getOriginalImg().equals("") ?
//                    "aa" : mGoodPicList.get(1).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop().into(secondHolder.iv02_second_hand);
//            Glide.with(mContext).load(mGoodPicList.get(2).getOriginalImg() == null || mGoodPicList.get(2).getOriginalImg().equals("") ?
//                    "aa" : mGoodPicList.get(2).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).centerCrop().into(secondHolder.iv03_second_hand);

            CustomApplication.setImageWithDiffDisplayImageOptions(mGoodPicList.get(0).getOriginalImg() == null || mGoodPicList.get(0).getOriginalImg().equals("") ?
                    "aa" : mGoodPicList.get(0).getOriginalImg(),secondHolder.iv01_second_hand,null,mBannerOptions1);
            CustomApplication.setImageWithDiffDisplayImageOptions(mGoodPicList.get(1).getOriginalImg() == null || mGoodPicList.get(1).getOriginalImg().equals("") ?
                    "aa" : mGoodPicList.get(0).getOriginalImg(),secondHolder.iv02_second_hand,null,mBannerOptions1);
            CustomApplication.setImageWithDiffDisplayImageOptions(mGoodPicList.get(2).getOriginalImg() == null || mGoodPicList.get(2).getOriginalImg().equals("") ?
                    "aa" : mGoodPicList.get(0).getOriginalImg(),secondHolder.iv03_second_hand,null,mBannerOptions1);
        }
//        Glide.with(mContext).load(usedGoods.getImgUrl()==null||usedGoods.getImgUrl().equals("")
//                ?"aa":usedGoods.getImgUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.touxiang2x).centerCrop().into(secondHolder.iv_photo);

        CustomApplication.setImageWithDiffDisplayImageOptions(usedGoods.getImgUrl()==null||usedGoods.getImgUrl().equals("")
                ?"aa":usedGoods.getImgUrl(),secondHolder.iv_photo,null,mBannerOptions);
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
    public static DisplayImageOptions mBannerOptions1 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_image)
                                                                                        // 图片加载的时候显示的图片
                                                                                        .showImageForEmptyUri(R.drawable.default_image)
                                                                                        // 图片加载地址为空的时候显示的图片
                                                                                        .showImageOnFail(R.drawable.default_image)
                                                                                        // 图片加载失败的时候显示的图片
                                                                                        .cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    @Override
    public int getItemCount() {
        if(StrConstant.SECOND_HAND_TRANSFER.equals(handType)){
            return usedGoodsList.size();
        }
        return 0;
    }

    class SecondHandTransferViewHolder extends LRecyclerViewAdapter.ViewHolder{
        @BindView(R.id.tv_second_hand_item_name)
        TextView               tv_second_hand_item_name;
        @BindView(R.id.tv_second_hand_item_time)
        TextView       tv_second_hand_item_time;
        @BindView(R.id.tv_second_hand_item_location)
        TextView       tv_second_hand_item_location;
        @BindView(R.id.tv_second_hand_item_price)
        TextView       tv_second_hand_item_price;
        @BindView(R.id.iv01_second_hand)
        ImageView      iv01_second_hand;
        @BindView(R.id.iv02_second_hand)
        ImageView      iv02_second_hand;
        @BindView(R.id.iv03_second_hand)
        ImageView      iv03_second_hand;
        @BindView(R.id.tv_second_hand_item_title)
        TextView       tv_second_hand_item_title;
        @BindView(R.id.iv_photo)
        RoundImageView iv_photo;

        public SecondHandTransferViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
