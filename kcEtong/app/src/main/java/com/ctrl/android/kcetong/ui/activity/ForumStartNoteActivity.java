package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.CompressHelper;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.FileUtil2;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.UILImageLoader;
import com.ctrl.android.kcetong.toolkit.util.UILPauseOnScrollListener;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
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

import static com.ctrl.android.kcetong.R.id.note_content;
import static com.ctrl.android.kcetong.R.id.note_title;

public class ForumStartNoteActivity extends BaseActivity {
    @BindView(note_title)//帖子标题
            EditText       noteTitle;
    @BindView(note_content)//帖子内容
            EditText       noteContent;
    @BindView(R.id.img_01)
            RelativeLayout Relv_img01;
    @BindView(R.id.img_02)
            RelativeLayout Relv_img02;
    @BindView(R.id.img_03)
            RelativeLayout Relv_img03;
    @BindView(R.id.del_up_1)
            ImageView      del_up_1;
    @BindView(R.id.del_up_2)
            ImageView      del_up_2;
    @BindView(R.id.del_up_3)
            ImageView      del_up_3;
    @BindView(R.id.uploadImage1)
            ImageView      uploadImage1;
    @BindView(R.id.uploadImage2)
            ImageView      uploadImage2;
    @BindView(R.id.uploadImage3)
            ImageView      uploadImage3;
    @BindView(R.id.activity_forum_start_note)
            RelativeLayout activityForumStartNote;
    @BindView(R.id.toolbar_right_btn)
            TextView       rightbtn;
    private String TITLE = StrConstant.START_NOTE_TITLE;
    private String categoryId;
    private FunctionConfig functionConfigBuilder = null;
    private ThemeConfig                           themeConfig;
    private cn.finalteam.galleryfinal.ImageLoader imageLoader;
    private PauseOnScrollListener                 pauseOnScrollListener;
    private static final int REQUEST_CODE_GALLERY = 113;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private List<PhotoInfo>                      mPhotoList;
    private List<String>                         qnList;
    private String                               memberId;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum_start_note);
        ButterKnife.bind(this);
        categoryId = getIntent().getStringExtra("categoryId");
    }

    @Override
    protected void initData() {
        memberId= AppHolder.getInstance().getMemberInfo().getMemberId();
        qnList = new ArrayList<>();
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(ForumStartNoteActivity.this);
                ForumStartNoteActivity.this.finish();
            }
        });
        rightbtn.setVisibility(View.VISIBLE);
        rightbtn.setText("发布");
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    postPhoto();
                }
            }
        });

        //        图片线则
        themeConfig = new ThemeConfig.Builder().setTitleBarBgColor(Color.rgb(40, 149, 118))
                                               .setTitleBarTextColor(Color.WHITE).setTitleBarIconColor(Color.WHITE)
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
                Toast.makeText(ForumStartNoteActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void configGalleryFinal() {
        functionConfigBuilder = new FunctionConfig.Builder().setEnableCamera(true).setEnableEdit(false).setEnableCrop(false).setEnableRotate(false).setCropSquare(false).setEnablePreview(false).setMutiSelectMaxSize(3).setForceCrop(false).setForceCropEdit(false).setSelected(mPhotoList).build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig).setFunctionConfig(functionConfigBuilder).setPauseOnScrollListener(pauseOnScrollListener).build();
        GalleryFinal.init(coreConfig);
    }

    private void initUploadImageVisible(int size) {
        switch (size) {
            case 0:
                Relv_img01.setVisibility(View.VISIBLE);
                Relv_img02.setVisibility(View.GONE);
                Relv_img03.setVisibility(View.GONE);
                del_up_1.setVisibility(View.GONE);
                uploadImage1.setVisibility(View.GONE);
                break;
            case 1:
                Relv_img01.setVisibility(View.VISIBLE);
                Relv_img02.setVisibility(View.VISIBLE);
                Relv_img03.setVisibility(View.GONE);
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
                Relv_img01.setVisibility(View.VISIBLE);
                Relv_img02.setVisibility(View.VISIBLE);
                Relv_img03.setVisibility(View.VISIBLE);
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
                Relv_img01.setVisibility(View.VISIBLE);
                Relv_img02.setVisibility(View.VISIBLE);
                Relv_img03.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.img_01, R.id.img_02, R.id.img_03, R.id.del_up_1, R.id.del_up_2, R.id.del_up_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_01:
                requestStoragePermissions();
                break;
            case R.id.img_02:
                requestStoragePermissions();
                break;
            case R.id.img_03:
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

    @Override
    public void noticeChildStorageComplete() {
        configGalleryFinal();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfigBuilder, mOnHanlderResultCallback);
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
                    oldFile = FileUtil2.getTempFile(ForumStartNoteActivity.this, Uri.fromFile(file));
                } catch (IOException e) {
                    Utils.showShortToast(ForumStartNoteActivity.this, "Failed to read picture data!");
                    e.printStackTrace();
                }
                newFile = CompressHelper.getDefault(ForumStartNoteActivity.this).compressToFile(oldFile);

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
                                PostMessage();//发布帖子
                            }
                        } else {
                            Utils.toastError(ForumStartNoteActivity.this, StrConstant.COMPLAINTFAIL);
                        }
                    }
                }, null);
            }
        } else {
            PostMessage();//发布帖子
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

    //发布帖子
    private void PostMessage() {
        showProgress(true);
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.PostMessageUrl);
        map.put("categoryId",categoryId);
        map.put("title",title);
        map.put("content",content);
        map.put("memberId",memberId);
        if (qnList != null && qnList.size() > 0) {
            for (int i = 0; i < qnList.size(); i++) {
                map.put("picUrlStr" + (i + 1), qnList.get(i) + "," + qnList.get(i));
                LLog.w("picUrlStr" + (i + 1) + "=" + qnList.get(i));
            }
        }
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().postMessage(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                if(TextUtils.equals(resultCode, ConstantsData.success)){
                    MessageUtils.showShortToast(ForumStartNoteActivity.this, "发布成功");
                    Intent intent = new Intent();
                    setResult(5001, intent);
                    finish();

                }
            }
        });
    }

    private boolean checkInput() {

        if (S.isNull(noteTitle.getText().toString())) {
            MessageUtils.showShortToast(this, "请输入标题");
            return false;
        }

        if (S.isNull(noteContent.getText().toString())) {
            MessageUtils.showShortToast(this, "请输入内容");
            return false;
        }

        return true;
    }
}
