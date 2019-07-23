package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ActDetail;
import com.ctrl.android.kcetong.model.ActDetailResult;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Img;
import com.ctrl.android.kcetong.model.Participant;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.ActGridListAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.GridViewForScrollView;
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

public class ActDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.act_title)
    TextView     act_title;
    @BindView(R.id.act_writer)
    TextView     act_writer;
    @BindView(R.id.act_name)
    TextView     act_name;
    @BindView(R.id.title_layout)
    LinearLayout title_layout;

    @BindView(R.id.layout_img)
    LinearLayout          layout_img;
    @BindView(R.id.act_img1)
    ImageView             act_img1;
    @BindView(R.id.act_img2)
    ImageView             act_img2;
    @BindView(R.id.act_img3)
    ImageView             act_img3;
    @BindView(R.id.act_content)
    TextView              act_content;
    @BindView(R.id.take_part_in_btn)
    TextView              take_part_in_btn;
    @BindView(R.id.scroll_view)
    ScrollView            scroll_view;
    @BindView(R.id.act_gridview)
    GridViewForScrollView act_gridview;

    /**
     * 活动详细
     */
    private ActDetail actDetail = new ActDetail();

    /**
     * 参与者列表
     */
    private List<Participant> listParticipant = new ArrayList<>();

    /**
     * 图片列表
     */
    private List<Img> listImg = new ArrayList<>();

    private String actionId;
    private String memberId = AppHolder.getInstance().getMemberInfo().getMemberId();

    private ActGridListAdapter actGridListAdapter;
    private ArrayList<String>  imagelist;//传入到图片放大类 用
    private int position = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_act_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.COMMUNITY_ACTIVITY_DETAIL_DETAIL, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActDetailActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        actionId = getIntent().getStringExtra("actionId");

        take_part_in_btn.setOnClickListener(this);
        showProgress(true);
        requestActDetail(actionId, memberId);
    }

    private void requestActDetail(String actionId, String memberId) {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantsData.METHOD, "pm.ppt.action.queryActionDetail");//方法名称

        map.put("actionId", actionId);
        map.put("memberId", memberId);
        map.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().getActDetail(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ActDetailResult>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d(response + "");
                showProgress(false);
            }

            @Override
            public void onNext(ActDetailResult actDetailResult) {
                super.onNext(actDetailResult);
                if (TextUtils.equals("000", actDetailResult.getCode())) {
                    listImg = actDetailResult.getData().getActionPictureList();
                    listParticipant = actDetailResult.getData().getParticipantList();
                    actDetail = actDetailResult.getData().getActionInfo();
                    if (actDetail.getActionPeopleName() != null && !actDetail.getActionPeopleName().equals("")) {
                        act_name.setText(getString(R.string.initiator) + actDetail.getActionPeopleName());
                    } else {
                        act_name.setVisibility(View.GONE);
                    }
                    act_title.setText(S.getStr(actDetail.getTitle()));
                    act_writer.setText("时间: " + Utils.getDateStrFromStamp("yyyy.MM.dd", actDetail.getStartTime()) + " - " + Utils.getDateStrFromStamp("MM.dd", actDetail.getEndTime()));
                    if (listImg == null || listImg.size() < 1) {
                        layout_img.setVisibility(View.GONE);

                    } else {
                        layout_img.setVisibility(View.VISIBLE);

                        if (listImg.size() == 1) {
                            act_img1.setVisibility(View.VISIBLE);
                            act_img2.setVisibility(View.GONE);
                            act_img3.setVisibility(View.GONE);
                            Glide.with(ActDetailActivity.this).load(listImg.get(0).getOriginalImg() == null || "".equals(listImg.get(0).getOriginalImg()) ? "aa" : listImg.get(0).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).
                                    error(R.drawable.default_image).centerCrop().into(act_img1);
                        } else if (listImg.size() == 2) {
                            act_img1.setVisibility(View.VISIBLE);
                            act_img2.setVisibility(View.VISIBLE);
                            act_img3.setVisibility(View.GONE);
                            LLog.d("----list_img1111111111" + listImg.get(0).getOriginalImg());
                            LLog.d("----list_img122222222222" + listImg.get(1).getOriginalImg());
                            Glide.with(ActDetailActivity.this).load(listImg.get(0).getOriginalImg() == null || "".equals(listImg.get(0).getOriginalImg()) ? "aa" : listImg.get(0).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).
                                    error(R.drawable.default_image).centerCrop().into(act_img1);
                            Glide.with(ActDetailActivity.this).load(listImg.get(1).getOriginalImg() == null || "".equals(listImg.get(1).getOriginalImg()) ? "aa" : listImg.get(1).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).
                                    error(R.drawable.default_image).centerCrop().into(act_img2);
                        } else if (listImg.size() == 3) {
                            act_img1.setVisibility(View.VISIBLE);
                            act_img2.setVisibility(View.VISIBLE);
                            act_img3.setVisibility(View.VISIBLE);
                            Glide.with(ActDetailActivity.this).load(listImg.get(0).getOriginalImg() == null || "".equals(listImg.get(0).getOriginalImg()) ? "aa" : listImg.get(0).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).
                                    error(R.drawable.default_image).centerCrop().into(act_img1);
                            Glide.with(ActDetailActivity.this).load(listImg.get(1).getOriginalImg() == null || "".equals(listImg.get(1).getOriginalImg()) ? "aa" : listImg.get(1).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).
                                    error(R.drawable.default_image).centerCrop().into(act_img2);
                            Glide.with(ActDetailActivity.this).load(listImg.get(2).getOriginalImg() == null || "".equals(listImg.get(2).getOriginalImg()) ? "aa" : listImg.get(2).getOriginalImg()).diskCacheStrategy(DiskCacheStrategy.ALL).
                                    error(R.drawable.default_image).centerCrop().into(act_img3);
                        }
                    }

                    act_content.setText(S.getStr(actDetail.getContent()));

                    actGridListAdapter = new ActGridListAdapter(ActDetailActivity.this);
                    if (listParticipant != null) {
                        actGridListAdapter.setList(listParticipant);
                    } else {
                        actGridListAdapter.setList(new ArrayList<Participant>());
                    }
                    act_gridview.setAdapter(actGridListAdapter);

                    //参与状态(0:参与,1:未参与)
                    if (actDetail.getStatus() == 1) {
                        take_part_in_btn.setEnabled(false);
                        take_part_in_btn.setClickable(false);
                        take_part_in_btn.setBackgroundResource(R.drawable.gray_bg_shap);
                        take_part_in_btn.setText("已参与");
                    } else if (actDetail.getStatus() == 0) {
                        take_part_in_btn.setEnabled(true);
                        take_part_in_btn.setClickable(true);
                        take_part_in_btn.setBackgroundResource(R.drawable.green_bg_shap);
                        take_part_in_btn.setText("参加活动");
                    }
                    scroll_view.smoothScrollTo(0, 20);//设置scrollview的起始位置在顶部

                }
                showProgress(false);
            }
        });
    }

    @OnClick({R.id.act_img1, R.id.act_img2, R.id.act_img3, R.id.take_part_in_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_img1:
                amplificationPhoto2(1, 0, view);
                break;
            case R.id.act_img2:
                amplificationPhoto2(2, 1, view);
                break;
            case R.id.act_img3:
                amplificationPhoto2(3, 2, view);
                break;
            case R.id.take_part_in_btn:
                showProgress(true);
                Map<String, String> map = new HashMap<>();
                map.put(ConstantsData.METHOD, "pm.ppt.action.joinAction");//方法名称
                map.put("actionId", actionId);
                map.put("memberId", memberId);
                map.putAll(ConstantsData.getSystemParams());
                String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
                map.put("sign", sign);
                LLog.d(map + "");
                map.remove(ConstantsData.METHOD);

                RetrofitUtil.Api().requestTakePartInAct(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response + "");
                        showProgress(false);
                        Utils.toastError(ActDetailActivity.this, "参加成功");
                        showProgress(true);
                        requestActDetail(actionId, memberId);
                    }
                });
                break;
        }
    }
    /**
     * 放大图片(返回的图片)
     */
    private void amplificationPhoto2(int size, int position, View view) {
        if (listImg != null && listImg.size() >= size) {
            List<String> imageUrlList = new ArrayList<>();
            for (Img img : listImg) {
                imageUrlList.add(img.getOriginalImg());
            }
            ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(this, imageUrlList, position);
        }
    }
}
