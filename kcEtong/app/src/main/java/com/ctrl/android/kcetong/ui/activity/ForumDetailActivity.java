package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ForumNoteComment;
import com.ctrl.android.kcetong.model.ForumNoteDetail;
import com.ctrl.android.kcetong.model.ForumNoteDetailBean;
import com.ctrl.android.kcetong.model.Img;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.ForumDetailAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @项目名称: 诚信行<br>
 * @类描述: <br>
 * @创建人： whs <br>
 * @创建时间： 2017/1/21 13:40 <br>
 * @修改人： <br>
 * @修改时间: 2017/1/21 13:40 <br>
 */
public class ForumDetailActivity extends BaseActivity {

    @BindView(R.id.forum_title)//帖子标题
            TextView              forumTitle;
    @BindView(R.id.forum_favor_num)//赞的数量
            TextView              forumFavorNum;
    @BindView(R.id.forum_look_num)//看的数量
            TextView              forumLookNum;
    @BindView(R.id.forum_comment_num)//回帖数量
            TextView              forumCommentNum;
    @BindView(R.id.forum_writer)//帖子作者
            TextView              forumWriter;
    @BindView(R.id.forum_time)//帖子发布时间
            TextView              forumTime;
    @BindView(R.id.forum_content)//帖子发布内容
            TextView              forumContent;
    @BindView(R.id.forum_img)//图片
            ImageView             forumImg;
    @BindView(R.id.forum_img2)//图片
            ImageView             forumImg2;
    @BindView(R.id.forum_img3)//图片
            ImageView             forumImg3;
    @BindView(R.id.forum_like_btn)//赞
            ImageView             forumLikeBtn;
    @BindView(R.id.listView)
            ListViewForScrollView listView;
    @BindView(R.id.forum_comment_text)//评论内容
            EditText              forumCommentText;
    @BindView(R.id.forum_comment_submit)//帖子评论按钮
            TextView              forumCommentSubmit;
    @BindView(R.id.activity_forum_detail)
            RelativeLayout        activityForumDetail;
    @BindView(R.id.layout_image)
    LinearLayout layout_image;//图片行

    private String TITLE = "";
    private String          forumPostId;//帖子id
    private String          memberId;
    private ForumNoteDetail forumNoteDetail;
    private List<ForumNoteComment> listComment = new ArrayList<>();
    private List<Img>              listImg     = new ArrayList<>();
    private ForumDetailAdapter forumDetailAdapter;
    private ArrayList<String>  imagelist;//传入到图片放大类 用
    private int LikeNote  = -1;//判断是否点赞过
    private int PraiseNum = 0;//点赞的个数
    private int position = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum_detail);
        ButterKnife.bind(this);
        /**首次进入该页不让弹出软键盘*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TITLE = getIntent().getStringExtra("title");
        forumPostId = getIntent().getStringExtra("forumPostId");
        memberId = AppHolder.getInstance().getMemberInfo().getMemberId();//用户id
    }

    @Override
    protected void initData() {
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(ForumDetailActivity.this);
                ForumDetailActivity.this.finish();
            }
        });
        NoteDetailinfo(forumPostId, memberId);

    }

    //获取帖子详情
    private void NoteDetailinfo(String forumPostId, String memberId) {
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.getDetailinfoUrl);
        map.put("forumPostId", forumPostId);
        map.put("memberId", memberId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().getNoteDetaildata(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ForumNoteDetailBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(ForumNoteDetailBean forumNoteDetailBean) {
                super.onNext(forumNoteDetailBean);
                if (TextUtils.equals("000", forumNoteDetailBean.getCode())) {
                    forumNoteDetail = forumNoteDetailBean.getData().getQueryPostInfo();
                    listComment = forumNoteDetailBean.getData().getQueryPostInfo().getReplyList();
                    listImg = forumNoteDetailBean.getData().getQueryPostInfo().getListImg();
                    forumTitle.setText(S.getStr(forumNoteDetail.getTitle()));
                    forumFavorNum.setText(S.getStr(forumNoteDetail.getPraiseNum()));
                    forumLookNum.setText(S.getStr(forumNoteDetail.getReadNum()));
                    forumCommentNum.setText(S.getStr(forumNoteDetail.getReplyNum()));
                    forumWriter.setText(S.getStr(forumNoteDetail.getMemberName()));
                    forumTime.setText(D.getDateStrFromStamp(ConstantsData.YYYY_MM_DD_HH_MM, forumNoteDetail.getCreateTime()));
                    forumContent.setText(S.getStr(forumNoteDetail.getContent()));
                    LikeNote = forumNoteDetail.getIsPraise();
                    PraiseNum = forumNoteDetail.getPraiseNum();
                    if (listImg == null || listImg.size() < 1) {
                        layout_image.setVisibility(View.GONE);
                        forumImg.setVisibility(View.GONE);
                        forumImg2.setVisibility(View.GONE);
                        forumImg3.setVisibility(View.GONE);

                    } else {

                        imagelist = new ArrayList<>();
                        if(listImg.size() == 1){
                            layout_image.setVisibility(View.VISIBLE);
                            forumImg.setVisibility(View.VISIBLE);
                            forumImg2.setVisibility(View.INVISIBLE);
                            forumImg3.setVisibility(View.INVISIBLE);

                            Glide.with(ForumDetailActivity.this).load(listImg.get(0).getZipImg() == null || (listImg.get(0).getZipImg()).equals("") ? "aa" : listImg.get(0).getZipImg()).placeholder(R.drawable.default_image).into(forumImg);
                            imagelist.add(listImg.get(0).getOriginalImg());
                        }else if(listImg.size() == 2){
                            layout_image.setVisibility(View.VISIBLE);
                            forumImg.setVisibility(View.VISIBLE);
                            forumImg2.setVisibility(View.VISIBLE);
                            forumImg3.setVisibility(View.INVISIBLE);
                            Glide.with(ForumDetailActivity.this).load(listImg.get(0).getZipImg() == null || (listImg.get(0).getZipImg()).equals("") ? "aa" : listImg.get(0).getZipImg()).placeholder(R.drawable.default_image).into(forumImg);

                            Glide.with(ForumDetailActivity.this).load(listImg.get(1).getZipImg() == null || (listImg.get(1).getZipImg()).equals("") ? "aa" : listImg.get(1).getZipImg()).placeholder(R.drawable.default_image).into(forumImg2);

                            imagelist.add(listImg.get(0).getOriginalImg());
                            imagelist.add(listImg.get(1).getOriginalImg());
                        }else if(listImg.size() == 3){

                            layout_image.setVisibility(View.VISIBLE);
                            forumImg.setVisibility(View.VISIBLE);
                            forumImg2.setVisibility(View.VISIBLE);
                            forumImg3.setVisibility(View.VISIBLE);
                            Glide.with(ForumDetailActivity.this).load(listImg.get(0).getZipImg() == null || (listImg.get(0).getZipImg()).equals("") ? "aa" : listImg.get(0).getZipImg()).placeholder(R.drawable.default_image).into(forumImg);

                            Glide.with(ForumDetailActivity.this).load(listImg.get(1).getZipImg() == null || (listImg.get(1).getZipImg()).equals("") ? "aa" : listImg.get(1).getZipImg()).placeholder(R.drawable.default_image).into(forumImg2);

                            Glide.with(ForumDetailActivity.this).load(listImg.get(2).getZipImg() == null || (listImg.get(2).getZipImg()).equals("") ? "aa" : listImg.get(2).getZipImg()).placeholder(R.drawable.default_image).into(forumImg3);

                            imagelist.add(listImg.get(0).getOriginalImg());
                            imagelist.add(listImg.get(1).getOriginalImg());
                            imagelist.add(listImg.get(2).getOriginalImg());
                        }

                        //点击放大图片
                        forumImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                position = 0;
                                ImagePagerActivity.imageSize = new ImageSize(v.getMeasuredWidth(), v.getMeasuredHeight());
                                ImagePagerActivity.startImagePagerActivity(ForumDetailActivity.this, imagelist, 0);
                            }
                        });
                        //点击放大图片
                        forumImg2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                position = 1;
                                ImagePagerActivity.imageSize = new ImageSize(v.getMeasuredWidth(), v.getMeasuredHeight());
                                ImagePagerActivity.startImagePagerActivity(ForumDetailActivity.this, imagelist, 1);
                            }
                        });

                        //点击放大图片
                        forumImg3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                position = 2;
                                ImagePagerActivity.imageSize = new ImageSize(v.getMeasuredWidth(), v.getMeasuredHeight());
                                ImagePagerActivity.startImagePagerActivity(ForumDetailActivity.this, imagelist, 2);
                            }
                        });


                    }
                    forumDetailAdapter = new ForumDetailAdapter(ForumDetailActivity.this);
                    forumDetailAdapter.setList(listComment);
                    listView.setAdapter(forumDetailAdapter);
                    showProgress(false);
                }
            }
        });
    }

    //点赞帖子
    private void requestLikeNote(String postId, String memberId) {
        showProgress(true);
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.setLikeNoteUrl);
        map.put("postId", postId);
        map.put("memberId", memberId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().setLikeNote(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
                if (TextUtils.equals("000", resultCode)) {
                    PraiseNum = PraiseNum + 1;
                    forumFavorNum.setText(S.getStr(PraiseNum));
                    LikeNote = 1;
                    MessageUtils.showShortToast(ForumDetailActivity.this, "点赞成功");

                }
            }
        });
    }

    //取消点赞
    private void CancelLikeNote(String postId, String memberId) {
        showProgress(true);
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.CancleNoteUrl);
        map.put("postId", postId);
        map.put("memberId", memberId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().CanclesetLikeNote(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
                if (TextUtils.equals("000", resultCode)) {
                    if (PraiseNum == 0) {
                        forumFavorNum.setText(S.getStr(PraiseNum));
                    } else {
                        PraiseNum = PraiseNum - 1;
                        forumFavorNum.setText(S.getStr(PraiseNum));
                    }
                    LikeNote = 0;
                    MessageUtils.showShortToast(ForumDetailActivity.this, "取消点赞");

                }
            }
        });
    }

    //回复帖子
    private void requestCommentNote(String postId, String pid, String replyContent, String receiverId) {
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.CommentNoteUrl);
        map.put("postId",postId);
        map.put("pid",pid);
        map.put("memberId",memberId);
        map.put("replyContent",replyContent);
        map.put("receiverId",receiverId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().CommenNoteDate(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
                showProgress(false);
                forumCommentText.setText("");
                MessageUtils.showShortToast(ForumDetailActivity.this,"回复成功");
               NoteDetailinfo(forumPostId, memberId);
            }
        });

    }

    @OnClick({R.id.forum_like_btn, R.id.forum_comment_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forum_like_btn:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    //是否赞过（0：没有 1：赞过）
                    if (LikeNote == 0) {
                        requestLikeNote(forumNoteDetail.getForumPostId(), AppHolder.getInstance().getMemberInfo().getMemberId());
                    } else {
                        CancelLikeNote(forumNoteDetail.getForumPostId(), AppHolder.getInstance().getMemberInfo().getMemberId());

                    }
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(ForumDetailActivity.this, getString(R.string.manager_cannot));
                }

                break;
            case R.id.forum_comment_submit:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    String postId = forumPostId;
                    String pid = "";
                    //String memberId = "";
                    String replyContent = forumCommentText.getText().toString();
                    String receiverId = "";
                    if (replyContent == null || replyContent.equals("")) {
                        MessageUtils.showShortToast(this, "请输入评论内容");
                    } else {
                        requestCommentNote(postId, pid, replyContent, receiverId);
                    }
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(ForumDetailActivity.this, getString(R.string.manager_cannot));
                }

                break;
        }
    }

    private boolean checkInput() {
        if (S.isNull(forumCommentText.getText().toString())) {
            MessageUtils.showShortToast(this, "请输入评论内容");
            return false;
        }
        return true;
    }
}
