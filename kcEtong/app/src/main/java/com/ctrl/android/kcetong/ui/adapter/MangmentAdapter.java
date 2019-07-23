package com.ctrl.android.kcetong.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Managment;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.ui.activity.StewardDetailActivity;
import com.ctrl.android.kcetong.ui.view.ListViewForScrollView;
import com.ctrl.android.kcetong.ui.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * e管家 adapter
 * Created by Eric on 2015/10/13.
 */
public class MangmentAdapter extends BaseAdapter {

    private Activity        mActivity;
    private List<Managment> list;

    // 有两种布局
    final int VIEW_TYPE = 2;
    final int TYPE_1    = 0;
    final int TYPE_2    = 1;

    public MangmentAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        LLog.w("11111111111111111");
    }

    public void setList(List<Managment> list) {
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

    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        // 得到type
        int type = list.get(position).getType() == 0 ? 0 : 1;
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        int         type    = getItemViewType(position);

        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_managment_me, parent, false);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_managment_other, parent, false);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                    break;
            }

        } else {

            switch (type) {
                case TYPE_1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }

        }
        final Managment managment = list.get(position);
        switch (type) {
            case TYPE_1:
                LLog.w(AppHolder.getInstance().getMemberInfo().getImgUrl() +"");
                if (AppHolder.getInstance().getMemberInfo().getImgUrl() != null && !AppHolder.getInstance().getMemberInfo().getImgUrl().equals(""))
//                    Glide.with(mActivity).load(AppHolder.getInstance().getMemberInfo().getImgUrl()).placeholder(R.drawable.touxiang2x).into(holder1.iv_my);
                    CustomApplication.setImageWithDiffDisplayImageOptions(AppHolder.getInstance().getMemberInfo().getImgUrl(),holder1.iv_my,null,mBannerOptions);

                holder1.tv_my.setText(managment.getQuestion());
                break;
            case TYPE_2:
                holder2.tv_answer.setText(StrConstant.ESTEWARD_WELCOME);
                if (managment.getListProblem().get(0).getNoneInfo().equals("0")) {
                    ArrayList<String> listAnswer = new ArrayList<>();
                    holder2.tv_answer.setText(StrConstant.ESTEWARD_ANSWER);
                    for (int i = 0; i < managment.getListProblem().size(); i++) {
                        listAnswer.add(managment.getListProblem().get(i).getProblemName());
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, listAnswer);
                    if (holder2.lv_answer.getVisibility() == View.GONE) {
                        holder2.lv_answer.setVisibility(View.VISIBLE);
                    }
                    holder2.lv_answer.setAdapter(arrayAdapter);
                    holder2.lv_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(mActivity, StewardDetailActivity.class);
                            i.putExtra("id", managment.getListProblem().get(position).getId());
                            mActivity.startActivity(i);
                        }
                    });
                } else if (managment.getListProblem().get(0).getNoneInfo().equals("1")) {
                    holder2.tv_answer.setText(StrConstant.ESTEWARD_NO_ANSWER);
                    holder2.lv_answer.setVisibility(View.GONE);
                }
                break;
        }
        return convertView;
    }

    private static DisplayImageOptions mBannerOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.touxiang2x)
                                                                                        // 图片加载的时候显示的图片
                                                                                        .showImageForEmptyUri(R.drawable.touxiang2x)
                                                                                        // 图片加载地址为空的时候显示的图片
                                                                                        .showImageOnFail(R.drawable.touxiang2x)
                                                                                        // 图片加载失败的时候显示的图片
                                                                                        .cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();


    static class ViewHolder1 {
        @BindView(R.id.iv_my)//图片
                RoundImageView iv_my;
        @BindView(R.id.tv_my)//
                TextView       tv_my;

        ViewHolder1(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder2 {
        @BindView(R.id.iv_answer)//图片
                ImageView             iv_answer;
        @BindView(R.id.tv_answer)//
                TextView              tv_answer;
        @BindView(R.id.lv_answer)
                ListViewForScrollView lv_answer;

        ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
