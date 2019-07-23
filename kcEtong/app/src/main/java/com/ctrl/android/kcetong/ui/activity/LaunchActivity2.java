package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Img2;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.CompressHelper;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.FileUtil2;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.UILImageLoader;
import com.ctrl.android.kcetong.toolkit.util.UILPauseOnScrollListener;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;
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
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 论坛 发布活动 页面2
 * Created by liu on 2017/1/11.
 */
public class LaunchActivity2 extends BaseActivity {
    @BindView(R.id.toolbar_right_btn)
    TextView       toolbar_right_btn;
    @BindView(R.id.act_content)//活动内容
    EditText       act_content;
    @BindView(R.id.upload_layout_1)
    RelativeLayout upload_layout_1;
    @BindView(R.id.del_up_1)
    ImageView      del_up_1;
    @BindView(R.id.upload_layout_2)
    RelativeLayout upload_layout_2;
    @BindView(R.id.del_up_2)
    ImageView      del_up_2;
    @BindView(R.id.upload_layout_3)
    RelativeLayout upload_layout_3;
    @BindView(R.id.del_up_3)
    ImageView      del_up_3;
    @BindView(R.id.uploadImage1)
    ImageView      uploadImage1;
    @BindView(R.id.uploadImage2)
    ImageView      uploadImage2;
    @BindView(R.id.uploadImage3)
    ImageView      uploadImage3;

    private List<ImageView> listImgView = new ArrayList<>();
    private List<Img2>      listImg     = new ArrayList<>();
    private String[] items = new String[]{"本地图片", "拍照"};
    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private String                                title;
    private String                                startTime;
    private String                                endTime;
    private String                                location;
    private List<String>                          qnList;
    private ThemeConfig                           themeConfig;
    private cn.finalteam.galleryfinal.ImageLoader imageLoader;
    private PauseOnScrollListener                 pauseOnScrollListener;
    private FunctionConfig functionConfigBuilder = null;
    private List<PhotoInfo>                      mPhotoList;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private static final int REQUEST_CODE_GALLERY = 1000;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_launch2);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.START_ACT_TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchActivity2.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("发布");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        location = intent.getStringExtra("location");

        qnList = new ArrayList<>();
        listImg = new ArrayList<>();
        //        图片线则

        themeConfig = new ThemeConfig.Builder().setTitleBarBgColor(Color.rgb(40, 149, 118)).
                setTitleBarTextColor(Color.WHITE).setTitleBarIconColor(Color.WHITE)
                                               .setFabNornalColor(Color.rgb(0xd3, 0x29, 0x24)).setFabPressedColor(Color.RED)
                                               .setIconCamera(R.mipmap.ic_action_camera).setCheckNornalColor(Color.rgb(0xd2, 0xd2, 0xd2))
                                               .setCheckSelectedColor(Color.rgb(0xd3, 0x29, 0x24)).build();
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
                Toast.makeText(LaunchActivity2.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void noticeChildStorageComplete() {
        configGalleryFinal();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfigBuilder, mOnHanlderResultCallback);
    }

    @OnClick({R.id.upload_layout_1, R.id.upload_layout_2, R.id.upload_layout_3, R.id.del_up_1, R.id.del_up_2, R.id.del_up_3,R.id.toolbar_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_layout_1:
            case R.id.upload_layout_2:
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
            case R.id.toolbar_right_btn://发布
                if(checkInput()){

                    postPhoto();
                }
                break;

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
                    oldFile = FileUtil2.getTempFile(LaunchActivity2.this, Uri.fromFile(file));
                } catch (IOException e) {
                    Utils.showShortToast(LaunchActivity2.this, "Failed to read picture data!");
                    e.printStackTrace();
                }
                newFile = CompressHelper.getDefault(LaunchActivity2.this).compressToFile(oldFile);

                uploadManager.put(newFile, newFile.getName(), getToken(), new UpCompletionHandler() {
                    @Override
                    public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {

                        LLog.w(responseInfo.toString());
                        LLog.w("responseInfo.isOK() =" + responseInfo.isOK());
                        if (responseInfo.isOK()) {
                            qnList.add(StrConstant.BASE_URL + "/" + s);
                            if (mPhotoList.size() == qnList.size()) {
                                LLog.w("11111111111111111");
                                String communityId = AppHolder.getInstance().getHouse().getCommunityId();
                                String memberId = AppHolder.getInstance().getMemberInfo().getMemberId();

                                String content = act_content.getText().toString();
                                showProgress(true);
                                postRelease(communityId,memberId, title, startTime, endTime, location, content);
                            }
                        } else {
                            Utils.toastError(LaunchActivity2.this, StrConstant.COMPLAINTFAIL);
                        }
                    }
                }, null);
            }
        } else {
            String communityId = AppHolder.getInstance().getHouse().getCommunityId();
            String memberId = AppHolder.getInstance().getMemberInfo().getMemberId();

            String content = act_content.getText().toString();
            showProgress(true);
            postRelease(communityId,memberId, title, startTime, endTime, location, content);
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
                            error(R.mipmap.ic_launcher).centerCrop().into(uploadImage1);
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
                            error(R.mipmap.ic_launcher).centerCrop().into(uploadImage1);
                    Glide.with(this).load(mPhotoList.get(1).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.mipmap.ic_launcher).centerCrop().into(uploadImage2);
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
                            error(R.mipmap.ic_launcher).centerCrop().into(uploadImage1);
                    Glide.with(this).load(mPhotoList.get(1).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.mipmap.ic_launcher).centerCrop().into(uploadImage2);
                    Glide.with(this).load(mPhotoList.get(2).getPhotoPath()).diskCacheStrategy(DiskCacheStrategy.ALL).
                            error(R.mipmap.ic_launcher).centerCrop().into(uploadImage3);
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

//    /**
//     * 上传图片
//     * @param activityId
//     * @param imgData
//     * @param typeKey
//     * @param optMode
//     */
//    public void postUploadImg(String activityId,String imgData,String typeKey,String optMode){
//        Map<String,String> map = new HashMap<String,String>();
//        map.put(ConstantsData.METHOD,"pm.ppt.propertyPicture.upload");//方法名称
//
//        map.put("activityId",activityId);
//        map.put("imgData",imgData);
//        map.put("typeKey",typeKey);
//        map.put("optMode",optMode);
//        map.putAll(ConstantsData.getSystemParams());
//        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
//        map.put("sign",sign);
//        LLog.d(map+"");
//        map.remove(ConstantsData.METHOD);
//
//        RetrofitUtil.Api().uploadImg(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseTSubscriber<UpLoadResult>(this) {
//                    @Override
//                    public void onResponseCallback(JSONObject response, String resultCode) {
//                        LLog.d(response+"");
//                        showProgress(false);
//                    }
//                    @Override
//                    public void onNext(UpLoadResult upLoadResult) {
//                        super.onNext(upLoadResult);
//                        if(TextUtils.equals("000", upLoadResult.getCode())){
//                            listImg.add(upLoadResult.getData().getImg2());
//                            setImageDisplay();
//                        }
//                        showProgress(false);
//                    }
//                });
//    }

    /**
     * 发布活动
     * @param communityId 社区id
     * @param memberId 会员id
     * @param title 活动标题
     * @param startTime 开始时间(yyyy-MM-dd HH:mm:ss)
     * @param endTime 结束时间(yyyy-MM-dd HH:mm:ss)
     * @param location 活动地点
     * @param content 活动内容
     */
    private void postRelease(String communityId,String memberId,String title
            ,String startTime,String endTime,String location,String content){
        Map<String,String> map = new HashMap<String,String>();
        map.put(ConstantsData.METHOD,"pm.ppt.action.addAction");//方法名称
        map.putAll(ConstantsData.getSystemParams());

        map.put("communityId",communityId);
        map.put("memberId",memberId);
        map.put("title",title);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("location",location);
        map.put("content",content);
//        map.put("actionPicUrlStr1",actionPicUrlStr1);
//        map.put("actionPicUrlStr2",actionPicUrlStr2);
//        map.put("actionPicUrlStr3",actionPicUrlStr3);
        if (qnList != null && qnList.size() > 0) {
            for (int i = 0; i < qnList.size(); i++) {
                map.put("actionPicUrlStr" + (i + 1), qnList.get(i) + "," + qnList.get(i));
                LLog.w("actionPicUrlStr" + (i + 1) + "=" + qnList.get(i));
            }
        }
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map+"");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().releaseActivity(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        try {
                            if("000".equals(response.get("code"))){
                                showDialog2("活动发布后需要审核, 请耐心等待");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showProgress(false);
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        showProgress(false);
                    }
                });
    }

    /**
     * 显示提示框
     */
    private void showDialog2(String info) {
        final HintMessageDialog hintDialog = new HintMessageDialog(this);
        hintDialog.showDefaultDialogOneButton("提示", info, new HintDialogListener() {
                    @Override
                    public void submitListener() {
                        hintDialog.dismiss();
                    }
                    @Override
                    public void cancelListener() {
                        hintDialog.dismiss();
                        Intent intent=new Intent();
                        intent.putExtra("releaseSuccess","1");
                        setResult(RESULT_OK,intent);
                        LaunchActivity2.this.finish();
                    }
                },"确定",false);
    }

    private boolean checkInput(){

        if(act_content.getText().toString() == null || act_content.getText().toString().equals("")){
            Utils.toastError(this,"内容不可为空");
            return false;
        }
        return true;
    }
}
