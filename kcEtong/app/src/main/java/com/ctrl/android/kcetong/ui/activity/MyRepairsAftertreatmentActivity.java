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
import com.ctrl.android.kcetong.model.GoodPic;
import com.ctrl.android.kcetong.model.RepairDeatilBean;
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

public class MyRepairsAftertreatmentActivity extends BaseActivity {

    @BindView(R.id.tv_my_repairs_aftertreament_progress)//
            TextView       tv_my_repairs_aftertreament_progress;
    @BindView(R.id.tv_my_repairs_aftertreament_time)
            TextView       tv_my_repairs_aftertreament_time;
    @BindView(R.id.tv_my_repairs_aftertreament_type)
            TextView       tv_my_repairs_aftertreament_type;
    @BindView(R.id.tv_my_repairs_aftertreament_content)
            TextView       tv_my_repairs_aftertreament_content;
    @BindView(R.id.tv_my_repairs_aftertreament_wuye)
            TextView  tv_my_repairs_aftertreament_wuye;
    @BindView(R.id.tv_my_repairs_aftertreament_result)
            TextView  tv_my_repairs_aftertreament_result;
    @BindView(R.id.tv_my_repairs_aftertreament_pingjia)
            TextView  tv_my_repairs_aftertreament_pingjia;
    @BindView(R.id.tv_my_repairs_aftertreament_pingjia_content)
            TextView  tv_my_repairs_aftertreament_pingjia_content;
    @BindView(R.id.iv01_my_repairs_aftertreament)
            ImageView iv01_my_repairs_aftertreament;
    @BindView(R.id.iv02_my_repairs_aftertreament)
            ImageView iv02_my_repairs_aftertreament;
    @BindView(R.id.iv03_my_repairs_aftertreament)
            ImageView iv03_my_repairs_aftertreament;
    @BindView(R.id.iv04_my_repairs_aftertreament)
            ImageView      iv04_my_repairs_aftertreament;
    @BindView(R.id.iv05_my_repairs_aftertreament)
            ImageView      iv05_my_repairs_aftertreament;
    @BindView(R.id.iv06_my_repairs_aftertreament)
            ImageView      iv06_my_repairs_aftertreament;
    @BindView(R.id.tv_my_repairs_aftertreament_room)
            TextView       tv_my_repairs_aftertreament_room;
    @BindView(R.id.tv_baoxiu_image)
            TextView       tv_baoxiu_image;
    @BindView(R.id.tv_wuye_image)
            TextView       tv_wuye_image;
    @BindView(R.id.voice_group_top)
            RelativeLayout voice_group_top;
    @BindView(R.id.id_anim)
    View                   id_anim;
    @BindView(R.id.voice_group_2)
    RelativeLayout    voice_group_2;
    @BindView(R.id.id_anim2)
    View              id_anim2;

    @BindView(R.id.layout_voice)
    LinearLayout layout_voice;
    @BindView(R.id.tv_time)
    TextView     tv_time;
    @BindView(R.id.layout_voice2)
    LinearLayout layout_voice2;
    @BindView(R.id.tv_time2)
    TextView     tv_time2;

    private String           id;
    private Bundle           bundle;
    private List<GoodPic>    repairDemandPicList;
    private List<GoodPic>    repairDemandResultPicList;
    private RepairDeatilBean repairDeatilBean;

    private String voiceFilePath;
    private String voiceFilePath2;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_repairs_aftertreatment);
        ButterKnife.bind(this);

        toolbarBaseSetting("报修详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyRepairsAftertreatmentActivity.this.finish();
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
        bundle = getIntent().getExtras();
        id = bundle.getString("complaintId");
        RepairDeatil();
    }

    @OnClick({R.id.iv01_my_repairs_aftertreament, R.id.iv02_my_repairs_aftertreament, R.id.iv03_my_repairs_aftertreament, R.id.iv04_my_repairs_aftertreament, R.id.iv05_my_repairs_aftertreament, R.id.iv06_my_repairs_aftertreament})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv01_my_repairs_aftertreament:
                amplificationPhoto(0, view, repairDemandPicList);
                break;
            case R.id.iv02_my_repairs_aftertreament:
                amplificationPhoto(1, view, repairDemandPicList);
                break;
            case R.id.iv03_my_repairs_aftertreament:
                amplificationPhoto(2, view, repairDemandPicList);
                break;
            case R.id.iv04_my_repairs_aftertreament:
                amplificationPhoto(0, view, repairDemandResultPicList);
                break;
            case R.id.iv05_my_repairs_aftertreament:
                amplificationPhoto(1, view, repairDemandResultPicList);
                break;
            case R.id.iv06_my_repairs_aftertreament:
                amplificationPhoto(2, view, repairDemandResultPicList);
                break;
        }
    }

    /**
     * 报修详情
     */
    private void RepairDeatil() {

        showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.repairDeatil);
        params.put("repairDemandId", id);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().repairDeatil(params).subscribeOn(Schedulers.io())//
                    .observeOn(AndroidSchedulers.mainThread())//
                    .subscribe(new BaseTSubscriber<RepairDeatilBean>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.w(response.toString());
                        }

                        @Override
                        public void onNext(RepairDeatilBean repairDeatilBean) {
                            super.onNext(repairDeatilBean);

                            LLog.w(repairDeatilBean.getCode());
                            LLog.w(repairDeatilBean.getData().getRepairDemandInfo().getCommunityName());
                            if (TextUtils.equals(repairDeatilBean.getCode(), ConstantsData.success)) {
                                MyRepairsAftertreatmentActivity.this.repairDeatilBean = repairDeatilBean;
                                repairDemandPicList = repairDeatilBean.getData().getRepairDemandPicList();
                                repairDemandResultPicList = repairDeatilBean.getData().getRepairDemandResultPicList();
                                tv_my_repairs_aftertreament_progress.setText("报修进度：已结束");
                                tv_my_repairs_aftertreament_time.setText("报修时间：" + TimeUtil.date(Long.parseLong(repairDeatilBean.getData().getRepairDemandInfo().getCreateTime())));
                                tv_my_repairs_aftertreament_room.setText("报修房间：" + repairDeatilBean.getData().getRepairDemandInfo().getCommunityName() + " " + repairDeatilBean.getData().getRepairDemandInfo().getBuilding() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getUnit() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getRoom());
                                tv_my_repairs_aftertreament_type.setText("报修类型：" + repairDeatilBean.getData().getRepairDemandInfo().getRepairKindName());
                                tv_my_repairs_aftertreament_content.setText(repairDeatilBean.getData().getRepairDemandInfo().getContent());
                                if (!S.isNull(repairDeatilBean.getData().getRepairDemandInfo().getRepairVoice())) {
                                    layout_voice.setVisibility(View.VISIBLE);
                                    voiceFilePath = repairDeatilBean.getData().getRepairDemandInfo().getRepairVoice();
                                    Log.d("--------",TimeUtil.getRingDuring(voiceFilePath));
                                    String        s   = TimeUtil.getRingDuring(voiceFilePath);
                                    double        dou = Double.parseDouble(s)/1000.0;
                                    DecimalFormat df  =new   java.text.DecimalFormat("#.0");
                                    tv_time.setText(df.format(dou) + "\"");
                                } else {
                                    layout_voice.setVisibility(View.GONE);
                                }
                                if (!S.isNull(repairDeatilBean.getData().getRepairDemandInfo().getEvaluateVoice())) {
                                    layout_voice2.setVisibility(View.VISIBLE);
                                    voiceFilePath2 = repairDeatilBean.getData().getRepairDemandInfo().getEvaluateVoice();
                                    Log.d("--------",TimeUtil.getRingDuring(voiceFilePath2));
                                    String        s   = TimeUtil.getRingDuring(voiceFilePath2);
                                    double        dou = Double.parseDouble(s)/1000.0;
                                    DecimalFormat df  =new   java.text.DecimalFormat("#.0");
                                    tv_time2.setText(df.format(dou) + "\"");
                                } else {
                                    layout_voice2.setVisibility(View.GONE);
                                }
                                if (repairDeatilBean.getData().getRepairDemandInfo().getEvaluateLevel().equals("0")) {
                                    tv_my_repairs_aftertreament_pingjia.setText("非常满意");
                                }
                                if (repairDeatilBean.getData().getRepairDemandInfo().getEvaluateLevel().equals("1")) {
                                    tv_my_repairs_aftertreament_pingjia.setText("基本满意");
                                }
                                if (repairDeatilBean.getData().getRepairDemandInfo().getEvaluateLevel().equals("2")) {
                                    tv_my_repairs_aftertreament_pingjia.setText("不满意");
                                }
                                tv_my_repairs_aftertreament_pingjia_content.setText(repairDeatilBean.getData().getRepairDemandInfo().getEvaluateContent());
                                if ((repairDemandPicList != null && repairDemandPicList.size() < 1) || repairDemandPicList == null) {
                                    tv_baoxiu_image.setVisibility(View.GONE);
                                    iv01_my_repairs_aftertreament.setVisibility(View.GONE);
                                    iv02_my_repairs_aftertreament.setVisibility(View.GONE);
                                    iv03_my_repairs_aftertreament.setVisibility(View.GONE);
                                }
                                if ((repairDemandResultPicList != null && repairDemandResultPicList.size() < 1) || repairDemandResultPicList == null) {
                                    tv_wuye_image.setVisibility(View.GONE);
                                    iv04_my_repairs_aftertreament.setVisibility(View.GONE);
                                    iv05_my_repairs_aftertreament.setVisibility(View.GONE);
                                    iv06_my_repairs_aftertreament.setVisibility(View.GONE);
                                }
                                if (repairDemandPicList != null && repairDemandPicList.size() == 1) {
                                    iv02_my_repairs_aftertreament.setVisibility(View.INVISIBLE);
                                    iv03_my_repairs_aftertreament.setVisibility(View.INVISIBLE);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_aftertreament);

                                }
                                if (repairDemandPicList != null && repairDemandPicList.size() == 2) {
                                    iv03_my_repairs_aftertreament.setVisibility(View.INVISIBLE);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_aftertreament);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_aftertreament);
                                }
                                if (repairDemandPicList != null && repairDemandPicList.size() == 3) {
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_aftertreament);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandPicList.get(2).getOriginalImg() == null || repairDemandPicList.get(2).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_aftertreament);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv03_my_repairs_aftertreament);
                                }
                                tv_my_repairs_aftertreament_wuye.setText("物业处理：已处理");
                                tv_my_repairs_aftertreament_result.setText(repairDeatilBean.getData().getRepairDemandInfo().getResult());
                                if (repairDemandResultPicList != null && repairDemandResultPicList.size() == 1) {
                                    iv05_my_repairs_aftertreament.setVisibility(View.INVISIBLE);
                                    iv06_my_repairs_aftertreament.setVisibility(View.INVISIBLE);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandResultPicList.get(0).getOriginalImg() == null || repairDemandResultPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_repairs_aftertreament);
                                }
                                if (repairDemandResultPicList != null && repairDemandResultPicList.size() == 2) {
                                    iv06_my_repairs_aftertreament.setVisibility(View.INVISIBLE);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandResultPicList.get(1).getOriginalImg() == null || repairDemandResultPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_repairs_aftertreament);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandResultPicList.get(0).getOriginalImg() == null || repairDemandResultPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv05_my_repairs_aftertreament);
                                }
                                if (repairDemandResultPicList != null && repairDemandResultPicList.size() == 3) {
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandResultPicList.get(1).getOriginalImg() == null || repairDemandResultPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_repairs_aftertreament);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandResultPicList.get(0).getOriginalImg() == null || repairDemandResultPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv05_my_repairs_aftertreament);
                                    Glide.with(MyRepairsAftertreatmentActivity.this).load(repairDemandResultPicList.get(2).getOriginalImg() == null || repairDemandResultPicList.get(2).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv06_my_repairs_aftertreament);
                                }
                            }
                            showProgress(false);
                        }
                    });
    }

    /**
     * 放大图片(发布的图片)
     */
    private void amplificationPhoto(int position, View view, List<GoodPic> list) {
        List<String> imageUrlList = new ArrayList<>();
        if (list != null) {
            for (GoodPic pic : list) {
                imageUrlList.add(pic.getOriginalImg());
            }
            ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(this, imageUrlList, position);
        }

    }
}
