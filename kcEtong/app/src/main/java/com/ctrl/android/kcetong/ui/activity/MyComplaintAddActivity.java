package com.ctrl.android.kcetong.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ComplaintTypeBean;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.CompressHelper;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.FileSaveUtil;
import com.ctrl.android.kcetong.toolkit.util.FileUtil;
import com.ctrl.android.kcetong.toolkit.util.FileUtil2;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.PermissionsChecker;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.UILImageLoader;
import com.ctrl.android.kcetong.toolkit.util.UILPauseOnScrollListener;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.widget.AudioRecordButton;
import com.ctrl.android.kcetong.ui.widget.MediaManager;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxl on 2017/1/16.
 */

public class MyComplaintAddActivity extends BaseActivity {

    @BindView(R.id.tv_change)
    TextView          tv_change;
    @BindView(R.id.spinner_complaint_add)
    Spinner           spinner_complaint_add;
    @BindView(R.id.et_my_complaint_add)
    EditText          et_my_complaint_add;
    @BindView(R.id.tv_my_complaint_add_room)
    TextView          tv_my_complaint_add_room;
    @BindView(R.id.toolbar_leftbtn)
    ImageView         toolbarLeftbtn;
    @BindView(R.id.upload_layout_1)
    RelativeLayout    upload_layout_1;
    @BindView(R.id.del_up_1)
    ImageView         del_up_1;
    @BindView(R.id.upload_layout_2)
    RelativeLayout    upload_layout_2;
    @BindView(R.id.del_up_2)
    ImageView         del_up_2;
    @BindView(R.id.upload_layout_3)
    RelativeLayout    upload_layout_3;
    @BindView(R.id.del_up_3)
    ImageView         del_up_3;
    @BindView(R.id.uploadImage1)
    ImageView         uploadImage1;
    @BindView(R.id.uploadImage2)
    ImageView         uploadImage2;
    @BindView(R.id.uploadImage3)
    ImageView         uploadImage3;
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
    private List<String>      list;
    private String            kingId;
    private ComplaintTypeBean complaintTypeBean;
    private String TITLE = StrConstant.COMPLAINDEATAIL;
    private ThemeConfig           themeConfig;
    private ImageLoader           imageLoader;
    private PauseOnScrollListener pauseOnScrollListener;
    private FunctionConfig functionConfigBuilder = null;
    private List<PhotoInfo>                      mPhotoList;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private static final int REQUEST_CODE_GALLERY = 112;
    private List<String> qnList;
    private static final int CHOOSE_ROOM_CODE = 3;
    private House house;

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器


    @Override
    protected void initView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_my_complaint_add);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        mPermissionsChecker = new PermissionsChecker(this);
//        requestRecorderPermissions();

        qnList = new ArrayList<>();
        list = new ArrayList<>();
        tv_change.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        if (AppHolder.getInstance().getHouse().getCommunityId() != null) {
            //my_community_name.setText(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
            tv_my_complaint_add_room.setText(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()) + " " + AppHolder.getInstance().getHouse().getBuilding() + getString(R.string.floor) + AppHolder.getInstance().getHouse().getUnit() + "单元" + AppHolder.getInstance().getHouse().getRoom() + "室");
        }
        spinner_complaint_add.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kingId = complaintTypeBean.getData().getKindList().get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initComplaintType();

        //        图片线则

        themeConfig = new ThemeConfig.Builder().setTitleBarBgColor(Color.rgb(40, 149, 118)).setTitleBarTextColor(Color.WHITE).setTitleBarIconColor(Color.WHITE).setFabNornalColor(Color.rgb(0xd3, 0x29, 0x24)).setFabPressedColor(Color.RED).setIconCamera(R.mipmap.ic_action_camera).setCheckNornalColor(Color.rgb(0xd2, 0xd2, 0xd2)).setCheckSelectedColor(Color.rgb(0xd3, 0x29, 0x24)).build();
        imageLoader = new UILImageLoader();
        pauseOnScrollListener = new UILPauseOnScrollListener(false, true);


        mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null) {
                    if (mPhotoList == null) {
                        mPhotoList = new ArrayList<>();
                    } else {
                        mPhotoList.clear();
                    }
                    mPhotoList.addAll(resultList);
                    initUploadImageVisible(mPhotoList.size());
                    for (int i = 0; i < mPhotoList.size(); i++) {
                        LLog.w(mPhotoList.get(i).getPhotoPath());
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Toast.makeText(MyComplaintAddActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };


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
                        File file = new File(voiceFilePath == null ? "" : voiceFilePath);
//                        if (!(!voiceFilePath.equals("") && FileSaveUtil.isFileExists(file))) {
//                            voicePath = tbub.getUserVoiceUrl() == null ? "" : tbub.getUserVoiceUrl();
//                        }
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

                            Utils.showShortToast(MyComplaintAddActivity.this, "删除成功");
                        }
                        voiceGroup.setVisibility(View.GONE);
                        voiceBtn.setVisibility(View.VISIBLE);
                    }
                });
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
                            File file = new File(voiceFilePath == null ? "" : voiceFilePath);
//                        if (!(!voiceFilePath.equals("") && FileSaveUtil.isFileExists(file))) {
//                            voicePath = tbub.getUserVoiceUrl() == null ? "" : tbub.getUserVoiceUrl();
//                        }
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

                                Utils.showShortToast(MyComplaintAddActivity.this, "删除成功");
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
                        File file = new File(voiceFilePath == null ? "" : voiceFilePath);
//                        if (!(!voiceFilePath.equals("") && FileSaveUtil.isFileExists(file))) {
//                            voicePath = tbub.getUserVoiceUrl() == null ? "" : tbub.getUserVoiceUrl();
//                        }
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

                            Utils.showShortToast(MyComplaintAddActivity.this, "删除成功");
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
                MyComplaintAddActivity.this.seconds = seconds;
                voiceFilePath = filePath;
                Log.d("----------", voiceFilePath + "---" + seconds);

//                receriveHandler.sendEmptyMessageDelayed(2, 3000);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        MediaManager.pause();
        MediaManager.release();
        super.onDestroy();
    }

    @Override
    public void noticeChildStorageComplete() {
        requestCameraPermissions();

    }

    @Override
    public void noticeChildCameraComplete() {
        super.noticeChildCameraComplete();
        configGalleryFinal();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfigBuilder, mOnHanlderResultCallback);
    }

    @OnClick({R.id.tv_change, R.id.upload_layout_1, R.id.upload_layout_2, R.id.upload_layout_3, R.id.del_up_1, R.id.del_up_2, R.id.del_up_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change:
                Intent intent = new Intent(MyComplaintAddActivity.this, HouseListActivity.class);
                intent.addFlags(StrConstant.COMPLAINT_ROOM_CHANGE);
                startActivityForResult(intent, CHOOSE_ROOM_CODE);
                break;
            case R.id.upload_layout_1:
                requestStoragePermissions();
                break;
            case R.id.upload_layout_2:
                requestStoragePermissions();
                break;
            case R.id.upload_layout_3:
                requestStoragePermissions();
                break;
            case R.id.del_up_1:
                if (mPhotoList.size() >= 1) {
                    mPhotoList.remove(0);
                    initUploadImageVisible(mPhotoList.size());
                }
                break;
            case R.id.del_up_2:
                if (mPhotoList.size() >= 2) {
                    mPhotoList.remove(1);
                    initUploadImageVisible(mPhotoList.size());
                }
                break;
            case R.id.del_up_3:
                if (mPhotoList.size() >= 3) {
                    mPhotoList.remove(2);
                    initUploadImageVisible(mPhotoList.size());
                }
                break;
        }
    }

    /**
     * 初始化报修类型
     */
    private void initComplaintType() {

        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.complaintTypeUrl);
        map.put("kindKey", ConstantsData.COMPLAINT_IMAGE_APPKEY);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().ComplaintType(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ComplaintTypeBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {

            }

            @Override
            public void onNext(ComplaintTypeBean complaintTypeBean) {
                super.onNext(complaintTypeBean);

                MyComplaintAddActivity.this.complaintTypeBean = complaintTypeBean;
                if (TextUtils.equals(complaintTypeBean.getCode(), "000")) {
                    for (ComplaintTypeBean.DataBean.KindListBean kindListBean : complaintTypeBean.getData().getKindList()) {
                        list.add(kindListBean.getKindName());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyComplaintAddActivity.this, android.R.layout.simple_spinner_item, list);
                spinner_complaint_add.setAdapter(adapter);
                kingId = complaintTypeBean.getData().getKindList().get(0).getId();
                showProgress(false);
            }
        });
    }

    /**
     * header 左侧按钮
     */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MessageUtils.showShortToast(MallShopActivity.this, "AA");
                if (FileUtil.isFileExists(voiceFilePath)) {
                    FileSaveUtil.deleteFile(voiceFilePath);
                    LLog.i("-----删除语音了------");
                }
                onBackPressed();
            }
        });
        return true;
    }

    /**
     * 右侧文本
     */
    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("完成");
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tv_my_complaint_add_room.getText().toString())) {
                    Utils.showShortToast(MyComplaintAddActivity.this, "请选择房间");
                    return;
                }
                if (TextUtils.isEmpty(et_my_complaint_add.getText().toString()) && S.isNull(voiceUrl)) {
                    Utils.showShortToast(MyComplaintAddActivity.this, "内容为空");
                    return;
                }
                if (TextUtils.isEmpty(kingId)) {
                    Utils.showShortToast(MyComplaintAddActivity.this, "请选择类型");
                    return;
                }
                postPhoto();
            }
        });
        return true;
    }


    /**
     * 页面标题
     */
    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }

    private void initUploadImageVisible(int size) {
        switch (size) {
            case 0:
                upload_layout_1.setVisibility(View.VISIBLE);
                upload_layout_2.setVisibility(View.GONE);
                upload_layout_3.setVisibility(View.GONE);
                del_up_1.setVisibility(View.GONE);
                uploadImage1.setVisibility(View.GONE);
                break;
            case 1:
                upload_layout_1.setVisibility(View.VISIBLE);
                upload_layout_2.setVisibility(View.VISIBLE);
                upload_layout_3.setVisibility(View.GONE);
                del_up_1.setVisibility(View.VISIBLE);
                del_up_2.setVisibility(View.GONE);
                uploadImage1.setVisibility(View.VISIBLE);
                uploadImage2.setVisibility(View.GONE);
                if (mPhotoList.size() >= 1) {
                    Glide.with(this).load(mPhotoList.get(0).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.drawable.default_image).centerCrop().into(uploadImage1);
                }
                break;
            case 2:
                upload_layout_1.setVisibility(View.VISIBLE);
                upload_layout_2.setVisibility(View.VISIBLE);
                upload_layout_3.setVisibility(View.VISIBLE);
                del_up_1.setVisibility(View.VISIBLE);
                del_up_2.setVisibility(View.VISIBLE);
                del_up_3.setVisibility(View.GONE);
                uploadImage1.setVisibility(View.VISIBLE);
                uploadImage2.setVisibility(View.VISIBLE);
                uploadImage3.setVisibility(View.GONE);
                if (mPhotoList.size() >= 2) {
                    Glide.with(this).load(mPhotoList.get(0).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.drawable.default_image).centerCrop().into(uploadImage1);
                    Glide.with(this).load(mPhotoList.get(1).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.drawable.default_image).centerCrop().into(uploadImage2);
                }
                break;
            case 3:
                upload_layout_1.setVisibility(View.VISIBLE);
                upload_layout_2.setVisibility(View.VISIBLE);
                upload_layout_3.setVisibility(View.VISIBLE);
                del_up_1.setVisibility(View.VISIBLE);
                del_up_2.setVisibility(View.VISIBLE);
                del_up_3.setVisibility(View.VISIBLE);
                uploadImage1.setVisibility(View.VISIBLE);
                uploadImage2.setVisibility(View.VISIBLE);
                uploadImage3.setVisibility(View.VISIBLE);
                if (mPhotoList.size() >= 3) {
                    Glide.with(this).load(mPhotoList.get(0).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.drawable.default_image).centerCrop().into(uploadImage1);
                    Glide.with(this).load(mPhotoList.get(1).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.drawable.default_image).centerCrop().into(uploadImage2);
                    Glide.with(this).load(mPhotoList.get(2).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.drawable.default_image).centerCrop().into(uploadImage3);
                }
                break;
            default:
                break;
        }
    }

    public void configGalleryFinal() {
        functionConfigBuilder = new FunctionConfig.Builder().setEnableCamera(true).setEnableEdit(false).setEnableCrop(false).setEnableRotate(false).setCropSquare(false).setEnablePreview(false).setMutiSelectMaxSize(3).setForceCrop(false).setForceCropEdit(false).setSelected(mPhotoList).build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig).setFunctionConfig(functionConfigBuilder).setPauseOnScrollListener(pauseOnScrollListener).build();
        GalleryFinal.init(coreConfig);
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

                        Utils.toastError(MyComplaintAddActivity.this, "语音上传成功");
                    } else {
                        showProgress(false);
                        Utils.toastError(MyComplaintAddActivity.this, StrConstant.COMPLAINTFAIL);
                    }
                }
            }, null);
        }
    }

    private File oldFile;
    private File newFile;

    /**
     * 上传图片到七牛
     */
    private void postPhoto() {

        if (mPhotoList != null && mPhotoList.size() > 0) {
            showProgress(true);
            UploadManager uploadManager = new UploadManager();
            for (PhotoInfo info : mPhotoList) {
                String filePath = info.getPhotoPath();
                File   file     = new File(filePath);

                //图片压缩
                try {
                    oldFile = FileUtil2.getTempFile(MyComplaintAddActivity.this, Uri.fromFile(file));
                } catch (IOException e) {
                    Utils.showShortToast(MyComplaintAddActivity.this, "Failed to read picture data!");
                    e.printStackTrace();
                }
                newFile = CompressHelper.getDefault(MyComplaintAddActivity.this).compressToFile(oldFile);

                uploadManager.put(newFile, newFile.getName(), getToken(), new UpCompletionHandler() {
                    @Override
                    public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {

                        LLog.w(responseInfo.toString());
                        LLog.w("responseInfo.isOK() =" + responseInfo.isOK());
                        if (responseInfo.isOK()) {
                            qnList.add(StrConstant.BASE_URL + "/" + s);
                            if (mPhotoList.size() == qnList.size()) {
                                LLog.w("11111111111111111");
                                showProgress(false);
                                releaseComplaint();
                            }
                        } else {
                            Utils.toastError(MyComplaintAddActivity.this, StrConstant.COMPLAINTFAIL);
                            showProgress(false);
                        }
                    }
                }, null);
            }
        } else {
            releaseComplaint();
        }
    }

    /**
     * 获取七牛token
     *
     * @return
     */
    private String getToken() {

        Mac       mac       = new Mac(StrConstant.QINIU_AK, StrConstant.QINIU_SK);
        PutPolicy putPolicy = new PutPolicy(StrConstant.QINIU_BUCKNAME);
        putPolicy.returnBody = "{\"name\": $(fname),\"size\": \"$(fsize)\",\"w\": \"$(imageInfo.width)\",\"h\": \"$(imageInfo.height)\",\"key\":$(etag)}";
        try {
            String uptoken = putPolicy.token(mac);
            LLog.w("debug:uptoken = " + uptoken);
            return uptoken;
        } catch (AuthException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发布投诉
     */
    private void releaseComplaint() {

        LLog.w("3333333333333333333");
        String contentValue = et_my_complaint_add.getText().toString();
        String proprietorId = AppHolder.getInstance().getProprietor().getProprietorId();
        String communityId  = AppHolder.getInstance().getHouse().getCommunityId();
        String addressId    = AppHolder.getInstance().getHouse().getAddressId();
        if (TextUtils.isEmpty(addressId)) {
            addressId = AppHolder.getInstance().getHouse().getAddressId();
        }
        showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.putAll(ConstantsData.getSystemParams());
        params.put(ConstantsData.METHOD, Url.releaseComplaint);
        params.put("proprietorId", proprietorId);
        params.put("communityId", communityId);
        params.put("addressId", addressId);
        params.put("complaintKindId", kingId);
        if(!S.isNull(contentValue)){
            params.put("content", contentValue);
        }

        if(!S.isNull(voiceUrl)){
            params.put("complaintVoice", voiceUrl);
        }
        if (qnList != null && qnList.size() > 0) {
            for (int i = 0; i < qnList.size(); i++) {
                params.put("complaintPicUrl" + (i + 1), qnList.get(i) + "," + qnList.get(i));
                LLog.w("complaintPicUrl" + (i + 1) + "=" + qnList.get(i));
            }
        }
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().releaseComplaint(params).subscribeOn(Schedulers.io())//
                    .observeOn(AndroidSchedulers.mainThread())//
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {

                            if (TextUtils.equals(resultCode, ConstantsData.success)) {

                                if (FileUtil.isFileExists(voiceFilePath)) {
                                    FileSaveUtil.deleteFile(voiceFilePath);

                                    LLog.i("-----删除语音了------");
                                }

                                Utils.showShortToast(MyComplaintAddActivity.this, StrConstant.COMPLAINT_SUCCESS);
                                Intent intent = new Intent();
                                setResult(4001, intent);
                                finish();
                            }
                            showProgress(false);
                        }
                    });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case CHOOSE_ROOM_CODE:
                    if (resultCode == 2001) {
                        Bundle bundle = data.getExtras();
                        house = (House) bundle.getSerializable("house");

                        if (TextUtils.isEmpty(house.getId())) {
                            tv_my_complaint_add_room.setText(house.getCommunityName() + house.getBuilding() + getString(R.string.floor) + house.getUnit() + "单元" + house.getRoom() + "号房");
                        }
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
