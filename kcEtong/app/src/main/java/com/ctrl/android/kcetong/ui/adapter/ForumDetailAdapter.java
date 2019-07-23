package com.ctrl.android.kcetong.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ForumNoteComment;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 活动列表 adapter
 * Created by Eric on 2015/10/13.
 */
public class ForumDetailAdapter extends BaseAdapter {

    private Activity               mActivity;
    private List<ForumNoteComment> list;

    public ForumDetailAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setList(List<ForumNoteComment> list) {
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.forum_detail_comment_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final ForumNoteComment comment = list.get(position);

        holder.forum_comment_writer.setText(S.getStr(comment.getMemberName()));
        holder.forum_comment_floor.setText((position + 1) + mActivity.getResources().getString(R.string.floor));
        holder.forum_comment_time.setText(D.getDateStrFromStamp(ConstantsData.YYYY_MM_DD_HH_MM,comment.getCreateTime()));
        holder.forum_comment_content.setText(S.getStr(comment.getReplyContent()));

        holder.forum_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    MessageUtils.showShortToast(mActivity,mActivity.getString(R.string.comment));
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(mActivity, mActivity.getString(R.string.manager_cannot));
                }

            }
        });

        return convertView;
    }


    static class ViewHolder {

        @BindView(R.id.forum_comment_writer)//回帖人
                TextView  forum_comment_writer;
        @BindView(R.id.forum_comment_floor)//楼层
                TextView  forum_comment_floor;
        @BindView(R.id.forum_comment_time)//回帖时间
                TextView  forum_comment_time;
        @BindView(R.id.forum_comment_content)//回帖内容
                TextView  forum_comment_content;
        @BindView(R.id.forum_comment_btn)//评论按钮
                ImageView forum_comment_btn;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }




}
