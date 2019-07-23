package com.ctrl.android.kcetong.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.CommentBean;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.ListBaseAdapter;
import com.ctrl.android.kcetong.ui.base.SuperViewHolder;

/**
 * Created by liu on 2018/3/7.
 */

public class ChoiceCommentAdapter extends ListBaseAdapter<CommentBean.DataBean.EvaluationListBean> {

    public ChoiceCommentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_choice_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView     mBuyerName      = holder.getView(R.id.tv_buyer_name);
        TextView     mCommentTime    = holder.getView(R.id.tv_comment_time);
        TextView     mSatisfyYes     = holder.getView(R.id.tv_buyer_satisfy_yes);
        TextView     mCommentContent = holder.getView(R.id.tv_comment_content);
        LinearLayout llImg           = holder.getView(R.id.ll_img);
        ImageView    commentOne = holder.getView(R.id.iv_comment_one);
        ImageView     commentTwo = holder.getView(R.id.iv_comment_two);
        ImageView     commentThree = holder.getView(R.id.iv_comment_three);
        final CommentBean.DataBean.EvaluationListBean evaluation = mDataList.get(position);
        mBuyerName.setText(evaluation.getNickName());
        mCommentTime.setText(Utils.getDataFormatString(evaluation.getCreateTime(), "yyyy-MM-dd HH:mm"));
        if(evaluation.getLevel().equals("0")){
            mSatisfyYes.setText("不满意");
        }else{
            mSatisfyYes.setText("满意");
        }
        mCommentContent.setText(evaluation.getContent());
        /*if(evaluation.getEvaluationPicSubList()!=null){
            final Intent i = new Intent();
            i.setClass(mActivity,SpaceImageDetailActivity.class);
            if(evaluation.getEvaluationPicSubList().size()>0){
                llImg.setVisibility(View.VISIBLE);
                commentOne.setVisibility(View.VISIBLE);
                Arad.imageLoader.load(evaluation.getEvaluationPicSubList().get(0).getZipImg()).placeholder(R.drawable.img_post_mr).into(holder.commentOne);
                commentOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] location = new int[2];
                        commentOne.getLocationOnScreen(location);
                        i.putExtra("locationX", location[0]);
                        i.putExtra("locationY", location[1]);
                        i.putExtra("width",  holder.commentOne.getWidth());
                        i.putExtra("height",  holder.commentOne.getHeight());
                        i.putExtra("imgUrl", evaluation.getEvaluationPicSubList().get(0).getOriginalImg());
                        mActivity.startActivity(i);
                        mActivity.overridePendingTransition(0, 0);
                    }
                });
            }
            if(evaluation.getEvaluationPicSubList().size()>1){
                commentTwo.setVisibility(View.VISIBLE);
                Arad.imageLoader.load(evaluation.getEvaluationPicSubList().get(1).getZipImg()).placeholder(R.drawable.img_post_mr).into(holder.commentTwo);
                commentTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] location = new int[2];
                        commentTwo.getLocationOnScreen(location);
                        i.putExtra("locationX", location[0]);
                        i.putExtra("locationY", location[1]);
                        i.putExtra("width",  commentTwo.getWidth());
                        i.putExtra("height",  commentTwo.getHeight());
                        i.putExtra("imgUrl", evaluation.getEvaluationPicSubList().get(1).getOriginalImg());
                        mActivity.startActivity(i);
                        mActivity.overridePendingTransition(0, 0);
                    }
                });
            }else {
                commentTwo.setVisibility(View.GONE);
            }
            if(evaluation.getEvaluationPicSubList().size()>2){
                commentThree.setVisibility(View.VISIBLE);
                Arad.imageLoader.load(evaluation.getEvaluationPicSubList().get(2).getZipImg()).placeholder(R.drawable.img_post_mr).into(holder.commentThree);
                commentThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] location = new int[2];
                        holder.commentThree.getLocationOnScreen(location);
                        i.putExtra("locationX", location[0]);
                        i.putExtra("locationY", location[1]);
                        i.putExtra("width",  commentThree.getWidth());
                        i.putExtra("height",  commentThree.getHeight());
                        i.putExtra("imgUrl", evaluation.getEvaluationPicSubList().get(2).getOriginalImg());
                        mActivity.startActivity(i);
                        mActivity.overridePendingTransition(0, 0);
                    }
                });
            }else {
                commentThree.setVisibility(View.GONE);
            }
        }else{
            llImg.setVisibility(View.GONE);
            commentOne.setVisibility(View.GONE);
            commentTwo.setVisibility(View.GONE);
            commentThree.setVisibility(View.GONE);
        }*/
    }
}
