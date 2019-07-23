package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ForumNote;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by Administrator on 2017/1/20.
 */

public class ForumListtAdapter extends ListBaseAdapter<ForumNote> {
    public ForumListtAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.forum_list_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ForumNote note              = mDataList.get(position);
        TextView  forum_title       = holder.getView(R.id.forum_title);//帖子名称
        TextView  forum_content     = holder.getView(R.id.forum_content);//帖子内容
        TextView  forum_member      = holder.getView(R.id.forum_member);//帖子发布人
        TextView  forum_time        = holder.getView(R.id.forum_time);//帖子发布时间
        TextView  forum_favor_num   = holder.getView(R.id.forum_favor_num);//帖子赞数量
        TextView  forum_look_num    = holder.getView(R.id.forum_look_num);//帖子浏览数量
        TextView  forum_comment_num = holder.getView(R.id.forum_comment_num);//帖子评论数量

        forum_title.setText(S.getStr(note.getTitle()));
        forum_content.setText(S.getStr(note.getContent()));
        forum_member.setText(S.getStr(note.getMemberName()));
        forum_time.setText(D.getDateStrFromStamp(ConstantsData.YYYY_MM_DD, S.getStr(note.getCreateTime())));
        forum_favor_num.setText(S.getStr(String.valueOf(note.getPraiseNum())));
        forum_look_num.setText(S.getStr(String.valueOf(note.getReadNum())));
        forum_comment_num.setText(S.getStr(String.valueOf(note.getReplyNum())));


    }
}
