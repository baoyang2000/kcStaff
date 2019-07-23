package com.ctrl.android.kcetong.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ComplaintDeatilBean;
import com.ctrl.android.kcetong.model.GoodPic;
import com.ctrl.android.kcetong.model.Img;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.widget.MediaManager;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyComplaintAftertreatmentActivity extends BaseActivity {

    @BindView(R.id.tv_my_complaint_aftertreament_progress)
    TextView  tv_my_complaint_aftertreament_progress;
    @BindView(R.id.tv_my_complaint_aftertreament_time)
    TextView  tv_my_complaint_aftertreament_time;
    @BindView(R.id.tv_my_complaint_aftertreament_type)
    TextView  tv_my_complaint_aftertreament_type;
    @BindView(R.id.tv_my_complaint_aftertreament_wuye)
    TextView  tv_my_complaint_aftertreament_wuye;
    @BindView(R.id.tv_my_complaint_aftertreament_result)
    TextView  tv_my_complaint_aftertreament_result;
    @BindView(R.id.tv_my_complaint_aftertreament_pingjia_content)
    TextView  tv_my_complaint_aftertreament_pingjia_content;
    @BindView(R.id.tv_my_complaint_aftertreament_content)
    TextView  tv_my_complaint_aftertreament_content;
    @BindView(R.id.tv_my_complaint_aftertreament_pingjia)
    TextView  tv_my_complaint_aftertreament_pingjia;
    @BindView(R.id.iv01_my_complaint_aftertreamen)
    ImageView iv01_my_complaint_aftertreamen;
    @BindView(R.id.iv02_my_complaint_aftertreamen)
    ImageView iv02_my_complaint_aftertreamen;
    @BindView(R.id.iv03_my_complaint_aftertreamen)
    ImageView iv03_my_complaint_aftertreamen;
    @BindView(R.id.iv04_my_complaint_aftertreamen)
    ImageView iv04_my_complaint_aftertreamen;
    @BindView(R.id.iv05_my_complaint_aftertreamen)
    ImageView iv05_my_complaint_aftertreamen;
    @BindView(R.id.iv06_my_complaint_aftertreamen)
    ImageView iv06_my_complaint_aftertreamen;
    @BindView(R.id.tv_tousu_image)
    TextView  tv_tousu_image;
    @BindView(R.id.tv_wuye_image)
    TextView  tv_wuye_image;
    @BindView(R.id.tv_my_complaint_aftertreament_room)
    TextView  tv_my_complaint_aftertreament_room;

    @BindView(R.id.voice_group_top)
    RelativeLayout voice_group_top;
    @BindView(R.id.id_anim)
    View           id_anim;
    @BindView(R.id.voice_group_2)
    RelativeLayout voice_group_2;
    @BindView(R.id.id_anim2)
    View           id_anim2;
    @BindView(R.id.layout_voice)
    LinearLayout   layout_voice;
    @BindView(R.id.tv_time)
    TextView       tv_time;
    @BindView(R.id.layout_voice2)
    LinearLayout   layout_voice2;
    @BindView(R.id.tv_time2)
    TextView       tv_time2;

    private String        id;
    private Bundle        bundle;
    private List<Img>     complaintResultPicList;
    private List<GoodPic> complaintPicList;

    private String voiceFilePath;
    private String voiceFilePath2;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_complaint_aftertreatment);
        ButterKnife.bind(this);
        toolbarBaseSetting("投诉详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyComplaintAftertreatmentActivity.this.finish();
            }
        });

        voice_group_top.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                id_anim.setBackgroundResource(R.drawable.play_3);
                MediaManager.pause();
                AnimationDrawable drawable;
                id_anim.setBackgroundResource(R.drawable.voice_play_receiver);
                drawable = (AnimationDrawable) id_anim.getBackground();
                drawable.start();
                if (!S.isNull(voiceFilePath)) {
                    MediaManager.playSound(voiceFilePath, new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            id_anim.setBackgroundResource(R.drawable.play_3);
                        }
                    });
                }
            }

        });

        voice_group_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                id_anim2.setBackgroundResource(R.drawable.play_3);
                MediaManager.pause();
                AnimationDrawable drawable;
                id_anim2.setBackgroundResource(R.drawable.voice_play_receiver);
                drawable = (AnimationDrawable) id_anim2.getBackground();
                drawable.start();
                if (!S.isNull(voiceFilePath2)) {
                    MediaManager.playSound(voiceFilePath2, new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            id_anim2.setBackgroundResource(R.drawable.play_3);
                        }
                    });
                }
            }

        });
    }

    @Override
    protected void initData() {
        complaintResultPicList = new ArrayList<>();
        complaintPicList = new ArrayList<>();
        bundle = getIntent().getExtras();
        id = bundle.getString("complaintId");
        complaintDeatil();
    }

    @OnClick({R.id.iv01_my_complaint_aftertreamen, R.id.iv02_my_complaint_aftertreamen, R.id.iv03_my_complaint_aftertreamen, R.id.iv04_my_complaint_aftertreamen, R.id.iv05_my_complaint_aftertreamen, R.id.iv06_my_complaint_aftertreamen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv01_my_complaint_aftertreamen:
                amplificationPhoto1(1, 0, view);
                break;
            case R.id.iv02_my_complaint_aftertreamen:
                amplificationPhoto1(2, 1, view);
                break;
            case R.id.iv03_my_complaint_aftertreamen:
                amplificationPhoto1(3, 2, view);
                break;
            case R.id.iv04_my_complaint_aftertreamen:
                amplificationPhoto2(1, 0, view);
                break;
            case R.id.iv05_my_complaint_aftertreamen:
                amplificationPhoto2(2, 1, view);
                break;
            case R.id.iv06_my_complaint_aftertreamen:
                amplificationPhoto2(3, 2, view);
                break;
        }
    }

    /**
     * 投诉详情
     */
    private void complaintDeatil() {

        showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.complaintDeatil);
        params.put("id", id);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().complaintDeatil(params).subscribeOn(Schedulers.io())//
                    .observeOn(AndroidSchedulers.mainThread())//
                    .subscribe(new BaseTSubscriber<ComplaintDeatilBean>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.w(response.toString());
                        }

                        @Override
                        public void onNext(ComplaintDeatilBean complaintDeatilBean) {
                            super.onNext(complaintDeatilBean);

                            LLog.w(complaintDeatilBean.getCode());
                            LLog.w(complaintDeatilBean.getData().getComplaintInfo().getCommunityName());
                            if (TextUtils.equals(complaintDeatilBean.getCode(), ConstantsData.success)) {
                                complaintPicList = complaintDeatilBean.getData().getComplaintPicList();
                                complaintResultPicList = complaintDeatilBean.getData().getComplaintResultPicList();
                                tv_my_complaint_aftertreament_progress.setText("投诉进度：已结束");
                                tv_my_complaint_aftertreament_wuye.setText("物业处理：已结束");
                                tv_my_complaint_aftertreament_time.setText("投诉时间：" + TimeUtil.date(Long.parseLong(complaintDeatilBean.getData().getComplaintInfo().getCreateTime())));
                                tv_my_complaint_aftertreament_room.setText("投诉房间：" + complaintDeatilBean.getData().getComplaintInfo().getCommunityName() + " " + complaintDeatilBean.getData().getComplaintInfo().getBuilding() + "-" + complaintDeatilBean.getData().getComplaintInfo().getUnit() + "-" + complaintDeatilBean.getData().getComplaintInfo().getRoom());
                                tv_my_complaint_aftertreament_type.setText("投诉类型：" + complaintDeatilBean.getData().getComplaintInfo().getComplaintKindName());
                                tv_my_complaint_aftertreament_content.setText(complaintDeatilBean.getData().getComplaintInfo().getContent());
                                tv_my_complaint_aftertreament_pingjia_content.setText(complaintDeatilBean.getData().getComplaintInfo().getEvaluateContent());

                                if (!S.isNull(complaintDeatilBean.getData().getComplaintInfo().getComplaintVoice())) {
                                    voiceFilePath = complaintDeatilBean.getData().getComplaintInfo().getComplaintVoice();
                                    layout_voice.setVisibility(View.VISIBLE);
                                    Log.d("--------",TimeUtil.getRingDuring(voiceFilePath));
                                    String        s   = TimeUtil.getRingDuring(voiceFilePath);
                                    double        dou = Double.parseDouble(s)/1000.0;
                                    DecimalFormat df  =new   java.text.DecimalFormat("#.0");
                                    tv_time.setText(df.format(dou) + "\"");
                                } else {
                                    layout_voice.setVisibility(View.GONE);
                                }

                                if (!S.isNull(complaintDeatilBean.getData().getComplaintInfo().getEvaluateVoice())) {
                                    layout_voice2.setVisibility(View.VISIBLE);
                                    voiceFilePath2 = complaintDeatilBean.getData().getComplaintInfo().getEvaluateVoice();
                                    Log.d("--------",TimeUtil.getRingDuring(voiceFilePath2));
                                    String        s   = TimeUtil.getRingDuring(voiceFilePath2);
                                    double        dou = Double.parseDouble(s)/1000.0;
                                    DecimalFormat df  =new   java.text.DecimalFormat("#.0");
                                    tv_time2.setText(df.format(dou) + "\"");
                                } else {
                                    layout_voice2.setVisibility(View.GONE);
                                }
                                if (complaintDeatilBean.getData().getComplaintInfo().getEvaluateLevel().equals("0")) {
                                    tv_my_complaint_aftertreament_pingjia.setText("非常满意");
                                }
                                if (complaintDeatilBean.getData().getComplaintInfo().getEvaluateLevel().equals("1")) {
                                    tv_my_complaint_aftertreament_pingjia.setText("基本满意");
                                }
                                if (complaintDeatilBean.getData().getComplaintInfo().getEvaluateLevel().equals("2")) {
                                    tv_my_complaint_aftertreament_pingjia.setText("不满意");
                                }
                                if (complaintPicList != null && complaintPicList.size() < 1 || complaintPicList == null) {
                                    tv_tousu_image.setVisibility(View.GONE);
                                    iv01_my_complaint_aftertreamen.setVisibility(View.GONE);
                                    iv02_my_complaint_aftertreamen.setVisibility(View.GONE);
                                    iv03_my_complaint_aftertreamen.setVisibility(View.GONE);
                                }
                                if (complaintResultPicList != null && complaintResultPicList.size() < 1 || complaintResultPicList == null) {
                                    tv_wuye_image.setVisibility(View.GONE);
                                    iv04_my_complaint_aftertreamen.setVisibility(View.GONE);
                                    iv05_my_complaint_aftertreamen.setVisibility(View.GONE);
                                    iv06_my_complaint_aftertreamen.setVisibility(View.GONE);
                                }
                                if (complaintPicList != null && complaintPicList.size() == 1) {
                                    iv05_my_complaint_aftertreamen.setVisibility(View.INVISIBLE);
                                    iv06_my_complaint_aftertreamen.setVisibility(View.INVISIBLE);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintPicList.get(0).getOriginalImg() == null || complaintPicList.get(0).getOriginalImg().equals("") ? "aa" : complaintPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_complaint_aftertreamen);
                                }
                                if (complaintPicList != null && complaintPicList.size() == 2) {
                                    iv06_my_complaint_aftertreamen.setVisibility(View.INVISIBLE);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintPicList.get(0).getOriginalImg() == null || complaintPicList.get(0).getOriginalImg().equals("") ? "aa" : complaintPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_complaint_aftertreamen);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintPicList.get(1).getOriginalImg() == null || complaintPicList.get(1).getOriginalImg().equals("") ? "aa" : complaintPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_complaint_aftertreamen);
                                }
                                if (complaintPicList != null && complaintPicList.size() == 3) {
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintPicList.get(0).getOriginalImg() == null || complaintPicList.get(0).getOriginalImg().equals("") ? "aa" : complaintPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_complaint_aftertreamen);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintPicList.get(1).getOriginalImg() == null || complaintPicList.get(1).getOriginalImg().equals("") ? "aa" : complaintPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_complaint_aftertreamen);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintPicList.get(2).getOriginalImg() == null || complaintPicList.get(2).getOriginalImg().equals("") ? "aa" : complaintPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv03_my_complaint_aftertreamen);
                                }
                                tv_my_complaint_aftertreament_result.setText(complaintDeatilBean.getData().getComplaintInfo().getResult());
                                if (complaintResultPicList != null && complaintResultPicList.size() == 1) {
                                    iv05_my_complaint_aftertreamen.setVisibility(View.GONE);
                                    iv06_my_complaint_aftertreamen.setVisibility(View.GONE);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintResultPicList.get(0).getOriginalImg() == null || complaintResultPicList.get(0).getOriginalImg().equals("") ? "aa" : complaintResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_complaint_aftertreamen);
                                }
                                if (complaintResultPicList != null && complaintResultPicList.size() == 2) {
                                    iv06_my_complaint_aftertreamen.setVisibility(View.GONE);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintResultPicList.get(0).getOriginalImg() == null || complaintResultPicList.get(0).getOriginalImg().equals("") ? "aa" : complaintResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_complaint_aftertreamen);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintResultPicList.get(1).getOriginalImg() == null || complaintResultPicList.get(1).getOriginalImg().equals("") ? "aa" : complaintResultPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv05_my_complaint_aftertreamen);
                                }
                                if (complaintResultPicList != null && complaintResultPicList.size() == 3) {
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintResultPicList.get(2).getOriginalImg() == null || complaintResultPicList.get(2).getOriginalImg().equals("") ? "aa" : complaintResultPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv06_my_complaint_aftertreamen);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintResultPicList.get(0).getOriginalImg() == null || complaintResultPicList.get(0).getOriginalImg().equals("") ? "aa" : complaintResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_complaint_aftertreamen);
                                    Glide.with(MyComplaintAftertreatmentActivity.this).load(complaintResultPicList.get(1).getOriginalImg() == null || complaintResultPicList.get(1).getOriginalImg().equals("") ? "aa" : complaintResultPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv05_my_complaint_aftertreamen);
                                }
                            }
                            showProgress(false);
                        }
                    });
    }

    /**
     * 放大图片(发布的图片)
     */
    private void amplificationPhoto1(int size, int position, View view) {
        List<String> imageUrlList = new ArrayList<>();
        for (GoodPic pic : complaintPicList) {
            imageUrlList.add(pic.getOriginalImg());
        }
        ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
        ImagePagerActivity.startImagePagerActivity(this, imageUrlList, position);
    }

    /**
     * 放大图片(返回的图片)
     */
    private void amplificationPhoto2(int size, int position, View view) {
        if (complaintResultPicList != null && complaintResultPicList.size() >= size) {
            List<String> imageUrlList = new ArrayList<>();
            for (Img img : complaintResultPicList) {
                imageUrlList.add(img.getOriginalImg());
            }
            ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(this, imageUrlList, position);
        }
    }
}
