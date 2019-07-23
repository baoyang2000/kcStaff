package com.ctrl.android.kcetong.ui.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Participant;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.ui.activity.ActDetailActivity;
import com.ctrl.android.kcetong.ui.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 参加活动成员 列表
 * Created by Administrator on 2015/7/20.
 */
public class ActGridListAdapter extends BaseAdapter {
    private ActDetailActivity mActivity;
    private List<Participant> list;

    public void setList(List<Participant> list) {
        this.list = list;
    }

    public ActGridListAdapter(ActDetailActivity activity){
        this.mActivity = activity;
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

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.act_list_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Participant p = list.get(position);

        if(p == null){
            //
        } else {
//            Arad.imageLoader.load(S.isNull(p.getImgUrl()) ? "aa" : p.getImgUrl())
//                    .placeholder(R.drawable.touxiang2x)
//                    .into(holder.act_icon);
            CustomApplication.setImageWithDiffDisplayImageOptions(S.isNull(p.getImgUrl()) ? "aa" : p.getImgUrl(),holder.act_icon,null,mBannerOptions);
            holder.act_name.setText(S.getStr(p.getNickName()));
        }

        return convertView;
    }
    public static DisplayImageOptions mBannerOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.touxiang2x)
            // 图片加载的时候显示的图片
            .showImageForEmptyUri(R.drawable.touxiang2x)
            // 图片加载地址为空的时候显示的图片
            .showImageOnFail(R.drawable.touxiang2x)
            // 图片加载失败的时候显示的图片
            .cacheInMemory(true).cacheOnDisk(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    static class ViewHolder {
        @BindView(R.id.act_icon)//用户头像
                RoundImageView act_icon;
        @BindView(R.id.act_name)//用户名
        TextView               act_name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
