package com.ctrl.android.kcetong.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.GoodPic;
import com.ctrl.android.kcetong.model.RepairDeatilBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.FileSaveUtil;
import com.ctrl.android.kcetong.toolkit.util.FileUtil;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.PermissionsChecker;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.TimeUtil;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.widget.AudioRecordButton;
import com.ctrl.android.kcetong.ui.widget.MediaManager;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
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

import static com.ctrl.android.kcetong.toolkit.util.ConstantsData.getToken;

public class MyRepairsPretreatmentActivity extends BaseActivity {

    @BindView(R.id.tv_my_repairs_pretreament_progress)
    TextView     tv_my_repairs_pretreament_progress;
    @BindView(R.id.tv_my_repairs_pretreament_time)
    TextView     tv_my_repairs_pretreament_time;
    @BindView(R.id.tv_my_repairs_pretreament_type)
    TextView     tv_my_repairs_pretreament_type;
    @BindView(R.id.tv_my_repairs_pretreament_content)
    TextView     tv_my_repairs_pretreament_content;
    @BindView(R.id.tv_my_repairs_pretreament_wuye)
    TextView     tv_my_repairs_pretreament_wuye;
    @BindView(R.id.tv_my_repairs_pretreament_result)
    TextView     tv_my_repairs_pretreament_result;
    @BindView(R.id.iv01_my_repairs_pretreament)
    ImageView    iv01_my_repairs_pretreament;
    @BindView(R.id.iv02_my_repairs_pretreament)
    ImageView    iv02_my_repairs_pretreament;
    @BindView(R.id.iv03_my_repairs_pretreament)
    ImageView    iv03_my_repairs_pretreament;
    @BindView(R.id.iv04_my_repairs_pretreament)
    ImageView    iv04_my_repairs_pretreament;
    @BindView(R.id.iv05_my_repairs_pretreament)
    ImageView    iv05_my_repairs_pretreament;
    @BindView(R.id.iv06_my_repairs_pretreament)
    ImageView    iv06_my_repairs_pretreament;
    @BindView(R.id.ll_my_repairs_wuye)
    LinearLayout ll_my_repairs_wuye;
    @BindView(R.id.ll_my_repairs_pingjia)
    LinearLayout ll_my_repairs_pingjia;
    @BindView(R.id.tv_baoxiu_image)
    TextView     tv_baoxiu_image;
    @BindView(R.id.tv_wuye_image)
    TextView     tv_wuye_image;
    @BindView(R.id.tv_my_repairs_pretreament_room)//报修房间
    TextView     tv_my_repairs_pretreament_room;

    @BindView(R.id.rb_repairs_01)
    RadioButton       rb_repairs_01;
    @BindView(R.id.rb_repairs_02)
    RadioButton       rb_repairs_02;
    @BindView(R.id.rb_repairs_03)
    RadioButton       rb_repairs_03;
    @BindView(R.id.rg_repairs)
    RadioGroup        rg_repairs;
    @BindView(R.id.et_my_repairs_pretreament_pingjia)
    EditText          et_my_repairs_pretreament_pingjia;
    @BindView(R.id.voice_group_top)
    RelativeLayout    voice_group_top;
    @BindView(R.id.id_anim)
    View              id_anim;
    @BindView(R.id.voice_btn)
    AudioRecordButton voiceBtn;
    @BindView(R.id.id_receiver_recorder_anim)
    View              idReceiverRecorderAnim;
    @BindView(R.id.voice_time)
    TextView          voiceTime;
    @BindView(R.id.del_voice)
    ImageView         delVoice;
    @BindView(R.id.voice_group)
    RelativeLayout    voiceGroup;
    @BindView(R.id.layout_voice)
    LinearLayout layout_voice;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.toolbar_right_btn)
    TextView toolbar_right_btn;

    private String           id;
    private String           progressState;
    private Bundle           bundle;
    private List<GoodPic>    repairDemandPicList;
    private List<GoodPic>    repairDemandResultPicList;
    private RepairDeatilBean repairDeatilBean;
    private int              position;

    private String voiceFilePath2;
    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_repairs_pretreatment);
        ButterKnife.bind(this);
        toolbarBaseSetting("报修详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyRepairsPretreatmentActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("完成");
    }

    @Override
    protected void initData() {

        mPermissionsChecker = new PermissionsChecker(this);
//        requestRecorderPermissions();

        bundle = getIntent().getExtras();
        id = bundle.getString("complaintId");
        progressState = bundle.getString("progressState");
        position = bundle.getInt("position", -1);
        if (TextUtils.equals(progressState, "0")) {
            ll_my_repairs_pingjia.setVisibility(View.GONE);
            ll_my_repairs_wuye.setVisibility(View.GONE);
            tv_my_repairs_pretreament_progress.setText("报修进度：待处理");
        }
        if (TextUtils.equals(progressState, "1")) {
            ll_my_repairs_pingjia.setVisibility(View.GONE);
            ll_my_repairs_wuye.setVisibility(View.GONE);
            tv_my_repairs_pretreament_progress.setText("报修进度：处理中");
        }
        if (TextUtils.equals(progressState, "2")) {
            tv_my_repairs_pretreament_progress.setText("报修进度：已处理");
        }
        rg_repairs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_repairs_01.getId()) {
                    rb_repairs_01.setTextColor(Color.WHITE);
                    rb_repairs_01.setText("非常满意");
                } else {
                    rb_repairs_01.setTextColor(Color.GRAY);
                    rb_repairs_01.setText("非常满意");

                }
                if (checkedId == rb_repairs_02.getId()) {
                    rb_repairs_02.setTextColor(Color.WHITE);
                    rb_repairs_02.setText("基本满意");
                } else {
                    rb_repairs_02.setTextColor(Color.GRAY);
                    rb_repairs_02.setText("基本满意");

                }
                if (checkedId == rb_repairs_03.getId()) {
                    rb_repairs_03.setTextColor(Color.WHITE);
                    rb_repairs_03.setText("不满意");
                } else {
                    rb_repairs_03.setTextColor(Color.GRAY);
                    rb_repairs_03.setText("不满意");

                }
            }
        });
        RepairDeatil();

        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onStart() {
//                tbAdapter.stopPlayVoice();
            }

            @Override
            public void onFinished(float seconds, final String filePath) {
                sendVoice(seconds, filePath);
                voiceBtn.setVisibility(View.GONE);
                voiceGroup.setVisibility(View.VISIBLE);
                voiceTime.setText(seconds + "\"");

                postVoice(filePath);

                voiceGroup.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        idReceiverRecorderAnim.setBackgroundResource(R.drawable.play_3);
                        MediaManager.pause();
                        AnimationDrawable drawable;
                        idReceiverRecorderAnim.setBackgroundResource(R.drawable.voice_play_receiver);
                        drawable = (AnimationDrawable) idReceiverRecorderAnim.getBackground();
                        drawable.start();
                        MediaManager.playSound(voiceFilePath2, new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                idReceiverRecorderAnim.setBackgroundResource(R.drawable.play_3);
                            }
                        });
                    }

                });
                delVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FileUtil.isFileExists(filePath)) {
                            FileSaveUtil.deleteFile(filePath);

                            Utils.showShortToast(MyRepairsPretreatmentActivity.this, "删除成功");
                        }
                        voiceGroup.setVisibility(View.GONE);
                        voiceBtn.setVisibility(View.VISIBLE);
                    }
                });
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
    }

    @Override protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        /*if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }else {
            voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
                @Override
                public void onStart() {
//                tbAdapter.stopPlayVoice();
                }

                @Override
                public void onFinished(float seconds, final String filePath) {
                    sendVoice(seconds, filePath);
                    voiceBtn.setVisibility(View.GONE);
                    voiceGroup.setVisibility(View.VISIBLE);
                    voiceTime.setText(seconds + "\"");

                    postVoice(filePath);

                    voiceGroup.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            idReceiverRecorderAnim.setBackgroundResource(R.drawable.play_3);
                            MediaManager.pause();
                            AnimationDrawable drawable;
                            idReceiverRecorderAnim.setBackgroundResource(R.drawable.voice_play_receiver);
                            drawable = (AnimationDrawable) idReceiverRecorderAnim.getBackground();
                            drawable.start();
                            MediaManager.playSound(voiceFilePath, new MediaPlayer.OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    idReceiverRecorderAnim.setBackgroundResource(R.drawable.play_3);
                                }
                            });
                        }

                    });
                    delVoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (FileUtil.isFileExists(filePath)) {
                                FileSaveUtil.deleteFile(filePath);

                                Utils.showShortToast(MyRepairsPretreatmentActivity.this, "删除成功");
                            }
                            voiceGroup.setVisibility(View.GONE);
                            voiceBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        }*/
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    public void noticeChildRecorderComplete() {
        super.noticeChildRecorderComplete();
        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onStart() {
//                tbAdapter.stopPlayVoice();
            }

            @Override
            public void onFinished(float seconds, final String filePath) {
                sendVoice(seconds, filePath);
                voiceBtn.setVisibility(View.GONE);
                voiceGroup.setVisibility(View.VISIBLE);
                voiceTime.setText(seconds + "\"");

                postVoice(filePath);

                voiceGroup.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        idReceiverRecorderAnim.setBackgroundResource(R.drawable.play_3);
                        MediaManager.pause();
                        AnimationDrawable drawable;
                        idReceiverRecorderAnim.setBackgroundResource(R.drawable.voice_play_receiver);
                        drawable = (AnimationDrawable) idReceiverRecorderAnim.getBackground();
                        drawable.start();
                        MediaManager.playSound(voiceFilePath, new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                idReceiverRecorderAnim.setBackgroundResource(R.drawable.play_3);
                            }
                        });
                    }

                });
                delVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FileUtil.isFileExists(filePath)) {
                            FileSaveUtil.deleteFile(filePath);

                            Utils.showShortToast(MyRepairsPretreatmentActivity.this, "删除成功");
                        }
                        voiceGroup.setVisibility(View.GONE);
                        voiceBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
    private String voiceFilePath = "";
    private float  seconds       = 0.0f;

    /**
     * 发送语音
     */
    protected void sendVoice(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyRepairsPretreatmentActivity.this.seconds = seconds;
                voiceFilePath2 = filePath;
                Log.d("----------", voiceFilePath2 + "---" + seconds);

//                receriveHandler.sendEmptyMessageDelayed(2, 3000);
            }
        }).start();
    }

    @OnClick({R.id.iv01_my_repairs_pretreament, R.id.iv02_my_repairs_pretreament, R.id.iv03_my_repairs_pretreament, R.id.iv04_my_repairs_pretreament, R.id.iv05_my_repairs_pretreament, R.id.iv06_my_repairs_pretreament, R.id.toolbar_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv01_my_repairs_pretreament:
                amplificationPhoto(0, view, repairDemandPicList);
                break;
            case R.id.iv02_my_repairs_pretreament:
                amplificationPhoto(1, view, repairDemandPicList);
                break;
            case R.id.iv03_my_repairs_pretreament:
                amplificationPhoto(2, view, repairDemandPicList);
                break;
            case R.id.iv04_my_repairs_pretreament:
                amplificationPhoto(0, view, repairDemandResultPicList);
                break;
            case R.id.iv05_my_repairs_pretreament:
                amplificationPhoto(1, view, repairDemandResultPicList);
                break;
            case R.id.iv06_my_repairs_pretreament:
                amplificationPhoto(2, view, repairDemandResultPicList);
                break;
            case R.id.toolbar_right_btn:
                if (progressState.equals("0")) {
                    finish();
                }
                if (progressState.equals("1")) {
                    finish();
                }
                if (progressState.equals("2")) {
                    if (rb_repairs_01.isChecked()) {
                        rb_repairs_01.setText("非常满意");
                        repairDeatilBean.getData().getRepairDemandInfo().setEvaluateLevel("0");
                    }
                    if (rb_repairs_02.isChecked()) {
                        repairDeatilBean.getData().getRepairDemandInfo().setEvaluateLevel("1");

                    }
                    if (rb_repairs_03.isChecked()) {
                        repairDeatilBean.getData().getRepairDemandInfo().setEvaluateLevel("2");
                    }

                    if (TextUtils.isEmpty(et_my_repairs_pretreament_pingjia.getText().toString()) && S.isNull(voiceUrl)) {
                        Utils.showShortToast(MyRepairsPretreatmentActivity.this, "评价内容为空");
                    } else {
                        evaluationComplaint();
                    }
                }
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
                                MyRepairsPretreatmentActivity.this.repairDeatilBean = repairDeatilBean;
                                repairDemandPicList = repairDeatilBean.getData().getRepairDemandPicList();
                                repairDemandResultPicList = repairDeatilBean.getData().getRepairDemandResultPicList();
                                if (progressState.equals("0")) {

                                    tv_my_repairs_pretreament_time.setText("报修时间：" + TimeUtil.date(Long.parseLong(repairDeatilBean.getData().getRepairDemandInfo().getCreateTime())));
                                    tv_my_repairs_pretreament_type.setText("报修类型：" + repairDeatilBean.getData().getRepairDemandInfo().getRepairKindName());
                                    tv_my_repairs_pretreament_room.setText("报修房间：" + repairDeatilBean.getData().getRepairDemandInfo().getCommunityName() + " " + repairDeatilBean.getData().getRepairDemandInfo().getBuilding() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getUnit() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getRoom());
                                    tv_my_repairs_pretreament_content.setText(repairDeatilBean.getData().getRepairDemandInfo().getContent());
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
                                    if ((repairDemandPicList != null && repairDemandPicList.size() < 1) || repairDemandPicList == null) {
                                        tv_baoxiu_image.setVisibility(View.GONE);
                                        iv01_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv02_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv03_my_repairs_pretreament.setVisibility(View.GONE);

                                    }

                                    if ((repairDemandResultPicList != null && repairDemandResultPicList.size() < 1) || repairDemandResultPicList == null) {
                                        tv_wuye_image.setVisibility(View.GONE);
                                        iv04_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv05_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv06_my_repairs_pretreament.setVisibility(View.GONE);

                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 1) {
                                        iv02_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        iv03_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 2) {
                                        iv03_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 3) {
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(2).getOriginalImg() == null || repairDemandPicList.get(2).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv03_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                    }
                                }

                                if (progressState.equals("1")) {

                                    tv_my_repairs_pretreament_time.setText("报修时间：" + TimeUtil.date(Long.parseLong(repairDeatilBean.getData().getRepairDemandInfo().getCreateTime())));
                                    tv_my_repairs_pretreament_type.setText("报修类型：" + repairDeatilBean.getData().getRepairDemandInfo().getRepairKindName());
                                    tv_my_repairs_pretreament_room.setText("报修房间：" + repairDeatilBean.getData().getRepairDemandInfo().getCommunityName() + " " + repairDeatilBean.getData().getRepairDemandInfo().getBuilding() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getUnit() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getRoom());
                                    tv_my_repairs_pretreament_content.setText(repairDeatilBean.getData().getRepairDemandInfo().getContent());
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
                                    if ((repairDemandPicList != null && repairDemandPicList.size() < 1) || repairDemandPicList == null) {
                                        tv_baoxiu_image.setVisibility(View.GONE);
                                        iv01_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv02_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv03_my_repairs_pretreament.setVisibility(View.GONE);

                                    }

                                    if ((repairDemandResultPicList != null && repairDemandResultPicList.size() < 1) || repairDemandResultPicList == null) {
                                        tv_wuye_image.setVisibility(View.GONE);
                                        iv04_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv05_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv06_my_repairs_pretreament.setVisibility(View.GONE);

                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 1) {
                                        iv02_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        iv03_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 2) {
                                        iv03_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_pretreament);
                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 3) {
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(2).getOriginalImg() == null || repairDemandPicList.get(2).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv03_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_pretreament);
                                    }
                                }
                                if (progressState.equals("2")) {

                                    tv_my_repairs_pretreament_time.setText("报修时间：" + TimeUtil.date(Long.parseLong(repairDeatilBean.getData().getRepairDemandInfo().getCreateTime())));
                                    tv_my_repairs_pretreament_room.setText("报修房间：" + repairDeatilBean.getData().getRepairDemandInfo().getCommunityName() + " " + repairDeatilBean.getData().getRepairDemandInfo().getBuilding() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getUnit() + "-" + repairDeatilBean.getData().getRepairDemandInfo().getRoom());
                                    tv_my_repairs_pretreament_type.setText("报修类型：" + repairDeatilBean.getData().getRepairDemandInfo().getRepairKindName());
                                    tv_my_repairs_pretreament_content.setText(repairDeatilBean.getData().getRepairDemandInfo().getContent());
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
                                    if ((repairDemandPicList != null && repairDemandPicList.size() < 1) || repairDemandPicList == null) {
                                        tv_baoxiu_image.setVisibility(View.GONE);
                                        iv01_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv02_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv03_my_repairs_pretreament.setVisibility(View.GONE);
                                    }
                                    if ((repairDemandResultPicList != null && repairDemandResultPicList.size() < 1) || repairDemandResultPicList == null) {
                                        tv_wuye_image.setVisibility(View.GONE);
                                        iv04_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv05_my_repairs_pretreament.setVisibility(View.GONE);
                                        iv06_my_repairs_pretreament.setVisibility(View.GONE);
                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 1) {
                                        iv02_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        iv03_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);

                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 2) {
                                        iv03_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_pretreament);
                                    }
                                    if (repairDemandPicList != null && repairDemandPicList.size() == 3) {
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(0).getOriginalImg() == null || repairDemandPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv01_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(2).getOriginalImg() == null || repairDemandPicList.get(2).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv03_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandPicList.get(1).getOriginalImg() == null || repairDemandPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv02_my_repairs_pretreament);
                                    }
                                    tv_my_repairs_pretreament_wuye.setText("物业处理：已处理");
                                    tv_my_repairs_pretreament_result.setText(repairDeatilBean.getData().getRepairDemandInfo().getResult());
                                    if (repairDemandResultPicList != null && repairDemandResultPicList.size() == 1) {
                                        iv05_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        iv06_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandResultPicList.get(0).getOriginalImg() == null || repairDemandResultPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_repairs_pretreament);
                                    }
                                    if (repairDemandResultPicList != null && repairDemandResultPicList.size() == 2) {
                                        iv06_my_repairs_pretreament.setVisibility(View.INVISIBLE);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandResultPicList.get(1).getOriginalImg() == null || repairDemandResultPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv05_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandResultPicList.get(0).getOriginalImg() == null || repairDemandResultPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_repairs_pretreament);
                                    }
                                    if (repairDemandResultPicList != null && repairDemandResultPicList.size() == 3) {
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandResultPicList.get(1).getOriginalImg() == null || repairDemandResultPicList.get(1).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(iv05_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandResultPicList.get(0).getOriginalImg() == null || repairDemandResultPicList.get(0).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(iv04_my_repairs_pretreament);
                                        Glide.with(MyRepairsPretreatmentActivity.this).load(repairDemandResultPicList.get(2).getOriginalImg() == null || repairDemandResultPicList.get(2).getOriginalImg().equals("") ? "aa" : repairDemandResultPicList.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(iv06_my_repairs_pretreament);
                                    }
                                }
                            }
                            showProgress(false);
                        }
                    });
    }

    private String voiceUrl = "";

    private void postVoice(String filePath) {
        if (!S.isNull(filePath) && FileSaveUtil.isFileExists(new File(filePath))) {
            showProgress(true);
            UploadManager uploadManager = new UploadManager();
            File          file          = new File(filePath);

            String fileName = System.currentTimeMillis() + file.getName();
            uploadManager.put(file, fileName, getToken(), new UpCompletionHandler() {
                @Override
                public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                    LLog.w(responseInfo.toString());
                    LLog.w("responseInfo.isOK() =" + responseInfo.isOK());
                    if (responseInfo.isOK()) {
                        voiceUrl = StrConstant.BASE_URL + "/" + s;

                        LLog.w("11111111111111111"+ voiceUrl);
                        showProgress(false);

                        Utils.toastError(MyRepairsPretreatmentActivity.this, "语音上传成功");
                    } else {
                        showProgress(false);
                        Utils.toastError(MyRepairsPretreatmentActivity.this, StrConstant.COMPLAINTFAIL);
                    }
                }
            }, null);
        }
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

    /**
     * 评价投诉内容
     */
    private void evaluationComplaint() {

        showProgress(true);
        String              evaluateLevel   = repairDeatilBean.getData().getRepairDemandInfo().getEvaluateLevel();
        String              evaluateContent = et_my_repairs_pretreament_pingjia.getText().toString();
        Map<String, String> params          = new HashMap<>();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.repairEvaluate);
        params.put("repairDemandId", id);
        params.put("evaluateLevel", evaluateLevel);
        params.put("evaluateContent", evaluateContent);
        params.put("evaluateVoice", voiceUrl);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().repairEvaluate(params).subscribeOn(Schedulers.io())//
                    .observeOn(AndroidSchedulers.mainThread())//
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            if (TextUtils.equals(resultCode, ConstantsData.success)) {

                                if (FileUtil.isFileExists(voiceFilePath2)) {
                                    FileSaveUtil.deleteFile(voiceFilePath2);

                                    LLog.i("-----删除语音了------");
                                }

                                Utils.showShortToast(MyRepairsPretreatmentActivity.this, StrConstant.EVALUATION_SUCCESS);
                                Intent intent = new Intent();
                                intent.putExtra("position", position);
                                setResult(2501, intent);
                                finish();
                            } else {
                                Utils.toastError(MyRepairsPretreatmentActivity.this, StrConstant.EVALUATION_FAIL);
                            }
                            showProgress(false);
                        }
                    });
    }

}
