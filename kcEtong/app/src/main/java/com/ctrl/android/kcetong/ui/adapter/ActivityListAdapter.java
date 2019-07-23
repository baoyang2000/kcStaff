package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Act;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.activity.ActDetailActivity;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2017/1/10.
 */

public class ActivityListAdapter extends LRecyclerView.Adapter<ActivityListAdapter.ActivityListViewHolder> implements View.OnClickListener{

    private Context mContext;

    private List<Act> allList;
    private List<Act> takeInList;
    private List<Act> launchList;
    private String    type;

    private OnRecyclerViewItemClickListener mOnItemClickListener  = null;
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,String data);
    }
    public ActivityListAdapter(String type, Context mContext) {
        this.mContext = mContext;
        allList = new ArrayList<>();
        takeInList = new ArrayList<>();
        launchList = new ArrayList<>();
        this.type = type;
        Log.d("-------adapter", "ActivityListAdapter: "+type);
    }
    /**
     * 添加全部活动
     *
     * @param allList
     */
    public void addActAllList(List<Act> allList) {
        this.allList = allList;
    }
    /**
     * 添加参与的活动
     *
     * @param takeInList
     */
    public void addTakeInList(List<Act> takeInList) {
        this.takeInList = takeInList;
    }
    /**
     * 添加我发起的
     *
     * @param launchList
     */
    public void addLaunchList(List<Act> launchList) {
        this.launchList = launchList;
    }

    /**
     * 清除
     */
    public void clearActAll() {
        this.allList.clear();
    }

    public void clearActTakeIn() {
        this.takeInList.clear();
    }

    public void clearActLaunch() {
        this.launchList.clear();
    }

    @Override
    public ActivityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.activity_list_item, parent, false);
        rootView.setOnClickListener(this);
        return new ActivityListViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ActivityListViewHolder holder, int position) {
        ActivityListViewHolder activityListViewHolder = holder;
        Act act;
        Log.d("type----------",type);
        if(ConstantsData.ACT_ALL.equals(type)){//全部活动
            act    = allList.get(position);
            initData(act,activityListViewHolder);
        }else if(ConstantsData.ACT_I_TAKE_IN.equals(type)) {//我参与的
            act = takeInList.get(position);
            initData(act,activityListViewHolder);
        }else if(ConstantsData.ACT_I_START_UP.equals(type)){//我发起的
            act = launchList.get(position);
            initData(act,activityListViewHolder);
        }

    }
    private void initData(final Act act,ActivityListViewHolder activityListViewHolder){
        String zipImg = act.getZipImg();
        Glide.with(mContext).load(Utils.getUrl(zipImg)).diskCacheStrategy(DiskCacheStrategy.ALL).
                error(R.drawable.default_image).centerCrop().into(activityListViewHolder.activity_img);
        //参与状态(0:未参与,1:参与)
        if(!Utils.isNull(act.getParticipateStatus())){
            if(act.getParticipateStatus().equals("1")){
                activityListViewHolder.activity_status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.activity_take_part_in_already_icon));
            } else if((act.getParticipateStatus().equals("0"))){
                activityListViewHolder.activity_status.setImageDrawable(mContext.getResources().getDrawable(R.drawable.activitytake_part_in_not_icon));
            }
        } else { }
        //活动名称
        activityListViewHolder.activity_name.setText(Utils.getStr(act.getTitle()));
        activityListViewHolder.activity_time.setText(Utils.getDateStrFromStamp("yyyy.MM.dd",act.getStartTime()) + " - " + Utils.getDateStrFromStamp("MM.dd",act.getEndTime()));

        activityListViewHolder.activity_detail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActDetailActivity.class);
                intent.putExtra("actionId",act.getId());
                mContext.startActivity(intent);
//                    AnimUtil.intentSlidIn(mContext);
            }
        });
        activityListViewHolder.itemView.setTag(act.getId());
    }
    @Override
    public void onClick(View v){
        if(mOnItemClickListener  != null){
            mOnItemClickListener .onItemClick(v,(String)v.getTag());
        }
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    @Override
    public int getItemCount() {
        if (ConstantsData.ACT_ALL.equals(type)) {//全部活动
            return this.allList.size();
        } else if (ConstantsData.ACT_I_TAKE_IN.equals(type)) {//我参与的
            return this.takeInList.size();
        } else if (ConstantsData.ACT_I_START_UP.equals(type)) {//我发起的
            return this.launchList.size();
        }
        return 0;
    }

    class ActivityListViewHolder extends LRecyclerViewAdapter.ViewHolder {
        //活动图片
        private ImageView activity_img;
        //活动状态
        private ImageView activity_status;
        //活动名称
         private TextView  activity_name;
        //活动时间
        private TextView activity_time;
        //活动详细按钮
        private TextView activity_detail_btn;
        public ActivityListViewHolder(View itemView) {
            super(itemView);
            activity_img = (ImageView) itemView.findViewById(R.id.activity_img);
            activity_status = (ImageView) itemView.findViewById(R.id.activity_status);
            activity_name = (TextView) itemView.findViewById(R.id.activity_name);
            activity_time = (TextView) itemView.findViewById(R.id.activity_time);
            activity_detail_btn = (TextView) itemView.findViewById(R.id.activity_detail_btn);
        }
    }

}
