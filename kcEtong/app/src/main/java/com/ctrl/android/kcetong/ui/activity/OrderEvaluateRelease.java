package com.ctrl.android.kcetong.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.OrderBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.CompressHelper;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.FileUtil2;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
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
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderEvaluateRelease extends BaseActivity {
    @BindView(R.id.tv_evaluate_buy_name)
    TextView       evaluateBuyName;
    @BindView(R.id.tv_order_total)
    TextView       orderTotal;
    @BindView(R.id.tv_order_time)
    TextView       orderTime;
    @BindView(R.id.tv_evaluate_yes)
    TextView       evaluateYes;
    @BindView(R.id.tv_evaluate_no)
    TextView       evaluateNo;
    @BindView(R.id.et_evaluate_content)
    EditText       evaluateContent;
    @BindView(R.id.tv_evaluate_submit)
    TextView       evaluateSubmit;
    @BindView(R.id.rl_img_come)
    RelativeLayout imgCome;
    @BindView(R.id.v_hyg)
    View           viewhyg;
    @BindView(R.id.ll_photograph)
    LinearLayout   mPhotograph;
    @BindView(R.id.ll_photo)
    LinearLayout   mPhoto;
    @BindView(R.id.ll_cancel)
    LinearLayout   mCancel;
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

    private OrderBean.DataBean.OrderListBean order;
    private String level = "1";
    private ThemeConfig           themeConfig;
    private ImageLoader           imageLoader;
    private PauseOnScrollListener pauseOnScrollListener;
    private FunctionConfig functionConfigBuilder = null;
    private List<PhotoInfo>                      mPhotoList;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private static final int REQUEST_CODE_GALLERY = 112;
    private List<String> qnList = new ArrayList<>();
    private static final int CHOOSE_ROOM_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_evaluate_release);
        ButterKnife.bind(this);
        toolbarBaseSetting("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderEvaluateRelease.this.finish();
            }
        });
        order = (OrderBean.DataBean.OrderListBean) getIntent().getSerializableExtra("order");
        evaluateBuyName.setText(order.getProductName() + " X" + order.getNums());
        orderTotal.setText(order.getTotalCost() + "");
        orderTime.setText(Utils.getDataFormatString(order.getCreateTime(), "yyyy-MM-dd HH:mm"));
    }

    @Override
    protected void initData() {
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
                Toast.makeText(OrderEvaluateRelease.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void noticeChildStorageComplete() {
        super.noticeChildStorageComplete();
        configGalleryFinal();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfigBuilder, mOnHanlderResultCallback);
    }

    @Override
    public void noticeChildCameraComplete() {
        super.noticeChildCameraComplete();
    }

    @OnClick({R.id.tv_evaluate_no, R.id.tv_evaluate_yes, R.id.del_up_1, R.id.upload_layout_1, R.id.del_up_2, R.id.upload_layout_2, R.id.del_up_3, R.id.upload_layout_3, R.id.tv_evaluate_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_evaluate_no:
                evaluateNo.setTextColor(getResources().getColor(R.color.main_red));
                evaluateNo.setBackgroundResource(R.drawable.jf_shap);
                evaluateYes.setTextColor(getResources().getColor(R.color.text_gray));
                evaluateYes.setBackgroundResource(R.drawable.edit_shap);
                level = "0";
                break;
            case R.id.tv_evaluate_yes:
                evaluateYes.setTextColor(getResources().getColor(R.color.main_red));
                evaluateYes.setBackgroundResource(R.drawable.jf_shap);
                evaluateNo.setTextColor(getResources().getColor(R.color.text_gray));
                evaluateNo.setBackgroundResource(R.drawable.edit_shap);
                level = "1";
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
            case R.id.tv_evaluate_submit:
                if(TextUtils.isEmpty(evaluateContent.getText().toString().trim())){
                    MessageUtils.showShortToast(this,"请填写评价内容");
                    return;
                }
                postPhoto();
                break;
        }
    }

    private void orderEvaluation() {
        showProgress(true);
        Map<String, String> map = new HashMap();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.orderEvaluateUrl);
        map.put("orderId", order.getId());
        map.put("memberId", AppHolder.getInstance().getMemberInfo().getMemberId());
        map.put("level", level);
        map.put("content", evaluateContent.getText().toString());
        if (qnList != null && qnList.size() > 0) {
            for (int i = 0; i < qnList.size(); i++) {
                map.put("evaluationPicStr" + (i + 1), qnList.get(i));
                LLog.w("evaluationPicStr" + (i + 1) + "=" + qnList.get(i));
            }
        }
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().orderEvaluate(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                if (TextUtils.equals(resultCode, ConstantsData.success)) {
                    Utils.toastError(OrderEvaluateRelease.this, "删除地址成功");
                }
            }

            @Override
            public void onNetError(Throwable e) {
            }

        });
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
                    oldFile = FileUtil2.getTempFile(OrderEvaluateRelease.this, Uri.fromFile(file));
                } catch (IOException e) {
                    Utils.showShortToast(OrderEvaluateRelease.this, "Failed to read picture data!");
                    e.printStackTrace();
                }
                newFile = CompressHelper.getDefault(OrderEvaluateRelease.this).compressToFile(oldFile);

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
                                orderEvaluation();
                            }
                        } else {
                            Utils.toastError(OrderEvaluateRelease.this, StrConstant.COMPLAINTFAIL);
                            showProgress(false);
                        }
                    }
                }, null);
            }
        } else {
            orderEvaluation();
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
}
