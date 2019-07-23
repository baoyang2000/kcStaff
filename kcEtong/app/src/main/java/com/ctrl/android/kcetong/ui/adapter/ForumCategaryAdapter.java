package com.ctrl.android.kcetong.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ForumCategory;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 论坛板块 adapter
 * Created by Eric on 2015/10/13.
 */
public class ForumCategaryAdapter extends BaseAdapter {

    private Activity            mActivity;
    private List<ForumCategory> list;

    public ForumCategaryAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setList(List<ForumCategory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.forum_area_list_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final ForumCategory category = list.get(position);
        LLog.d(category.getImgUrl()+"适配器68");
       /* Glide.with(mActivity).load(S.getStr(category.getImgUrl()))
             .placeholder(R.drawable.default_image)
             .into(holder.area_img);*/
        CustomApplication.setImageWithDiffDisplayImageOptions(S.isNull(category.getImgUrl()) ? "aa" : category.getImgUrl(),holder.area_img,null,mBannerOptions);
        holder.area_name.setText(S.getStr(category.getName()));
        holder.area_num.setText("" + category.getCount());

        return convertView;
    }
    public static DisplayImageOptions mBannerOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_image)
            // 图片加载的时候显示的图片
            .showImageForEmptyUri(R.drawable.default_image)
            // 图片加载地址为空的时候显示的图片
            .showImageOnFail(R.drawable.default_image)
            // 图片加载失败的时候显示的图片
            .cacheInMemory(true).cacheOnDisk(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    static class ViewHolder {
        @BindView(R.id.area_img)//板块图片
                ImageView area_img;
        @BindView(R.id.area_name)//板块名称
                TextView  area_name;
        @BindView(R.id.area_num)//贴子数
                TextView  area_num;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }




}
