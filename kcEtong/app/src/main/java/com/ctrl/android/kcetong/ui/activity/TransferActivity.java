package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.ctrl.android.kcetong.model.GoodPic;
import com.ctrl.android.kcetong.model.Kind;
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
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransferActivity extends BaseActivity {

    @BindView(R.id.et_baobei_title)
    EditText       et_baobei_title;
    @BindView(R.id.et_baobei_detail)
    EditText       et_baobei_detail;
    @BindView(R.id.et_baobei_transferprice)//转让价
    EditText       et_baobei_transferprice;
    @BindView(R.id.et_baobei_originalprice)
    EditText       et_baobei_originalprice;
    @BindView(R.id.et_contact)
    EditText       et_contact;
    @BindView(R.id.et_contact_telephone)
    EditText       et_contact_telephone;
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
    @BindView(R.id.rl_wang_transfer)
    RelativeLayout rl_wang_transfer;
    @BindView(R.id.tv_secong_hand_transfer_type)
    TextView       tv_secong_hand_transfer_type;
    @BindView(R.id.toolbar_right_btn)
    TextView toolbar_right_btn;

    private List<ImageView> listImg;
    private List<String>    listImgStr;
    private List<Bitmap>    listBitmap;
    private List<String>  listImageUrl =new ArrayList<>();
    private List<String>  listImageId  =new ArrayList<>();
    private List<GoodPic> listImage    =new ArrayList<>();

    private ThemeConfig                           themeConfig;
    private cn.finalteam.galleryfinal.ImageLoader imageLoader;
    private PauseOnScrollListener                 pauseOnScrollListener;
    private FunctionConfig functionConfigBuilder = null;
    private List<PhotoInfo>                      mPhotoList;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private static final int REQUEST_CODE_GALLERY = 112;
    private static final int WANT_TRANSFER_TYPE_CODE = 3;
    private Kind kind;

    private List<String> qnList;
    private int imageFlag=-1;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        toolbarBaseSetting("我要转", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("完成");
    }

    @Override
    protected void initData() {

//        listBitmap=new ArrayList<>();
//        listImgStr=new ArrayList<>();
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
                Toast.makeText(TransferActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
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
                    oldFile = FileUtil2.getTempFile(TransferActivity.this, Uri.fromFile(file));
                } catch (IOException e) {
                    Utils.showShortToast(TransferActivity.this, "Failed to read picture data!");
                    e.printStackTrace();
                }
                newFile = CompressHelper.getDefault(TransferActivity.this).compressToFile(oldFile);

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
                                requestGoodsAdd(AppHolder.getInstance().getHouse().getCommunityId(),
                                        AppHolder.getInstance().getMemberInfo().getMemberId(),
                                        AppHolder.getInstance().getProprietor().getProprietorId(),
                                        et_baobei_title.getText().toString(),
                                        et_baobei_detail.getText().toString(),
                                        et_contact.getText().toString(),
                                        et_contact_telephone.getText().toString(),
                                        et_baobei_transferprice.getText().toString(),
                                        kind.getId(),
                                        et_baobei_originalprice.getText().toString(),
                                        StrConstant.WANT_TRANSFER_TRANSACTION_TYPE,
                                        StrConstant.VISIBLE_TYPE
                                );
                            }
                        } else {
                            Utils.toastError(TransferActivity.this, StrConstant.COMPLAINTFAIL);
                        }
                    }
                }, null);
            }
        } else {
            requestGoodsAdd(AppHolder.getInstance().getHouse().getCommunityId(),
                    AppHolder.getInstance().getMemberInfo().getMemberId(),
                    AppHolder.getInstance().getProprietor().getProprietorId(),
                    et_baobei_title.getText().toString(),
                    et_baobei_detail.getText().toString(),
                    et_contact.getText().toString(),
                    et_contact_telephone.getText().toString(),
                    et_baobei_transferprice.getText().toString(),
                    kind.getId(),
                    et_baobei_originalprice.getText().toString(),
                    StrConstant.WANT_TRANSFER_TRANSACTION_TYPE,
                    StrConstant.VISIBLE_TYPE
            );
        }
    }
    @Override
    public void noticeChildStorageComplete() {
        configGalleryFinal();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfigBuilder, mOnHanlderResultCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case WANT_TRANSFER_TYPE_CODE:
                if (resultCode == RESULT_OK) {
                    kind = (Kind) data.getSerializableExtra("kind");
                    tv_secong_hand_transfer_type.setText(kind.getKindName());
                }
                break;
        }
    }

    @OnClick({R.id.upload_layout_1, R.id.upload_layout_2, R.id.upload_layout_3, R.id.del_up_1, R.id.del_up_2, R.id.del_up_3, R.id.rl_wang_transfer, R.id.toolbar_right_btn})
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
            case R.id.rl_wang_transfer://选择分类
                Intent intent=new Intent(TransferActivity.this,ClassifyActivity.class);
                intent.addFlags(StrConstant.WANT_BUY_TYPE);
                startActivityForResult(intent,WANT_TRANSFER_TYPE_CODE);
                break;
            case R.id.toolbar_right_btn:
                if (TextUtils.isEmpty(et_baobei_title.getText().toString())) {
                    MessageUtils.showShortToast(TransferActivity.this, "标题为空");
                    return;
                }
                if (TextUtils.isEmpty(et_baobei_detail.getText().toString())) {
                    MessageUtils.showShortToast(TransferActivity.this, "描述为空");
                    return;
                }
                if (TextUtils.isEmpty(et_baobei_transferprice.getText().toString())) {
                    MessageUtils.showShortToast(TransferActivity.this, "价格为空");
                    return;
                }
                if(kind == null){
                    MessageUtils.showShortToast(TransferActivity.this, "请选择分类");
                    return;
                }else {
                    if (kind.getId() == null || kind.getId().equals("")) {
                        MessageUtils.showShortToast(TransferActivity.this, "请选择分类");
                        return;
                    }
                }
                if (TextUtils.isEmpty(et_contact.getText().toString())) {
                    MessageUtils.showShortToast(TransferActivity.this, "联系人为空");
                    return;
                }
                if (TextUtils.isEmpty(et_contact_telephone.getText().toString())) {
                    MessageUtils.showShortToast(TransferActivity.this, "联系电话为空");
                    return;
                }
                if (!Utils.isMobileNO(et_contact_telephone.getText().toString().trim())){
                    MessageUtils.showShortToast(TransferActivity.this, "联系电话不正确");
                    return;
                }
                postPhoto();
                break;
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
     * 发布二手交易信息
     * @param communityId 社区id
     * @param memberId 会员id
     * @param proprietorId 业主id
     * @param title 标题
     * @param content 描述
     * @param contactName 联系人姓名
     * @param contactMobile 联系人电话
     * @param sellingPrice 转让价/期望价格
     * @param goodKindId 物品分类id
     * @param originalPrice 原价
     * @param transactionType 交易类型（0：出卖、1：求购）
     * @param visibleType 可见类型（0：对外公开、1：仅社区内可见）
     */
    public void requestGoodsAdd(String communityId, String memberId,String proprietorId,String title,
            String content,String contactName,String contactMobile,String sellingPrice,
            String goodKindId,String originalPrice,String transactionType,String visibleType) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("method","pm.ppt.usedGoods.add");
        params.putAll(ConstantsData.getSystemParams());

        params.put("communityId", communityId);
        params.put("memberId", memberId);
        params.put("proprietorId", proprietorId);
        params.put("title", title);
        params.put("content", content);
        params.put("contactName", contactName);
        params.put("contactMobile", contactMobile);
        params.put("sellingPrice", sellingPrice);
        params.put("goodKindId", goodKindId);
        params.put("originalPrice", originalPrice);
//        params.put("usedGoodPicStr1", usedGoodPicStr1);
//        params.put("usedGoodPicStr2", usedGoodPicStr2);
//        params.put("usedGoodPicStr3", usedGoodPicStr3);
        if (qnList != null && qnList.size() > 0) {
            for (int i = 0; i < qnList.size(); i++) {
                params.put("usedGoodPicStr" + (i + 1), qnList.get(i) + "," + qnList.get(i));
                LLog.w("usedGoodPicStr" + (i + 1) + "=" + qnList.get(i));
            }
        }
        params.put("transactionType", transactionType);
        params.put("visibleType", visibleType);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign",sign);

        params.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestGoodsAdd(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                        showProgress(false);
                        try {
                            if(TextUtils.equals("000",response.getString("code"))){
                                MessageUtils.showShortToast(TransferActivity.this, "信息发布成功");
                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNetError(Throwable e) {
                        super.onNetError(e);
                        showProgress(false);
                    }
                });
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
    }public void configGalleryFinal() {
        functionConfigBuilder = new FunctionConfig.Builder().setEnableCamera(true).setEnableEdit(false).setEnableCrop(false).setEnableRotate(false).setCropSquare(false).setEnablePreview(false).setMutiSelectMaxSize(3).setForceCrop(false).setForceCropEdit(false).setSelected(mPhotoList).build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig).setFunctionConfig(functionConfigBuilder).setPauseOnScrollListener(pauseOnScrollListener).build();
        GalleryFinal.init(coreConfig);
    }
}
