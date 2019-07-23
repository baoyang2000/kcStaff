package com.ctrl.android.kcetong.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.listener.SetheadListener;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.LoginBean;
import com.ctrl.android.kcetong.model.MemberInfo;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.UILImageLoader;
import com.ctrl.android.kcetong.toolkit.util.UILPauseOnScrollListener;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.activity.CommunityHomeActivity;
import com.ctrl.android.kcetong.ui.activity.ExpressActivity;
import com.ctrl.android.kcetong.ui.activity.FamillyHotLineListActivity;
import com.ctrl.android.kcetong.ui.activity.HouseListActivity;
import com.ctrl.android.kcetong.ui.activity.HouseListActivity2;
import com.ctrl.android.kcetong.ui.activity.MyComplaintActivity;
import com.ctrl.android.kcetong.ui.activity.MySettingActivity;
import com.ctrl.android.kcetong.ui.activity.MyVisitActivity;
import com.ctrl.android.kcetong.ui.activity.PropertyPaymentActivity;
import com.ctrl.android.kcetong.ui.activity.RegisterActivity;
import com.ctrl.android.kcetong.ui.activity.SecondHandActivity;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
import com.ctrl.android.kcetong.ui.dialog.HintMessageDialog;
import com.ctrl.android.kcetong.ui.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import pub.devrel.easypermissions.EasyPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ctrl.android.kcetong.R.id.my_community_house;
import static com.ctrl.android.kcetong.R.id.my_community_name;
import static com.ctrl.android.kcetong.R.id.my_express_btn_wirh_num;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.welcome_text)//欢迎语
            TextView welcomeText;
    @BindView(R.id.top_bg_layout)
    LinearLayout topBgLayout;
    @BindView(R.id.my_orders_num)//我的订单数量
            TextView myOrdersNum;
    @BindView(R.id.my_order_btn_with_num)//我的订单  带个数的
            LinearLayout myOrderBtnWithNum;
    @BindView(R.id.my_express_num)//我的快递数量
            TextView myExpressNum;
    @BindView(R.id.my_express_btn_wirh_num)//我的快递按钮  带个数的
            LinearLayout myExpressBtnWirhNum;
    @BindView(R.id.my_score_num)//我的积分数量
            TextView myScoreNum;
    @BindView(R.id.my_score_btn_with_num)//我的积分  带个数的
            LinearLayout myScoreBtnWithNum;
    @BindView(R.id.userIcon)//用户头像
            RoundImageView userIcon;
    @BindView(R.id.my_setting_btn)//设置按钮
            ImageView mySettingBtn;
    @BindView(R.id.infoLayout)
    RelativeLayout infoLayout;
    @BindView(R.id.my_community_name)//小区名称
            TextView myCommunityName;
    @BindView(R.id.my_community_house)//房子地址
            TextView myCommunityHouse;
    @BindView(R.id.my_locate_btn)//小区定位
            RelativeLayout myLocateBtn;
    @BindView(R.id.shop_order_btn)//商城订单
            RelativeLayout shopOrderBtn;
    @BindView(R.id.my_pay_history_btn)//我的缴费记录
            RelativeLayout myPayHistoryBtn;
    @BindView(R.id.my_activity_btn)//我的活动
            RelativeLayout myActivityBtn;
    @BindView(R.id.my_complain_btn)//我的投诉
            RelativeLayout myComplainBtn;
    @BindView(R.id.my_repair_btn)//我的报修
            RelativeLayout myRepairBtn;
    @BindView(R.id.my_appointment_btn)//预约及到访
            RelativeLayout myAppointmentBtn;
    @BindView(R.id.my_community_info_btn)//我的分类信息
            RelativeLayout myCommunityInfoBtn;
    @BindView(R.id.my_express_btn)//我的快递
            RelativeLayout myExpressBtn;
    @BindView(R.id.my_coupon_btn)//我的优惠券
            RelativeLayout myCouponBtn;

    private Context context;
    private MemberInfo memberInfo;
    private boolean isRefreshUserInfo = false;
    private FunctionConfig functionConfigBuilder = null;
    private ThemeConfig themeConfig;
    private PauseOnScrollListener pauseOnScrollListener;
    private List<PhotoInfo> mPhotoList;
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private final int REQUEST_CODE_GALLERY = 860;
    private final int REQUEST_CODE_CAMERA = 861;
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            ButterKnife.bind(this, rootView);
            context = this.getActivity();

            int isVisiter = AppHolder.getInstance().getVisiterFlg();
            if (isVisiter == 0) {
                myExpressBtnWirhNum.setClickable(true);
                if (AppHolder.getInstance().getHouse().getCommunityId() != null) {
                    getMemberInfo(AppHolder.getInstance().getMemberInfo().getMemberId(), AppHolder.getInstance().getHouse().getCommunityId());
                } else {
                    CustomApplication.setImageWithDiffDisplayImageOptions(S.isNull(AppHolder.getInstance().getMemberInfo().getImgUrl()) ? "aa" : AppHolder.getInstance().getMemberInfo().getImgUrl(), userIcon, null, mBannerOptions);

                    welcomeText.setText("欢迎你  " + S.getStr(AppHolder.getInstance().getMemberInfo().getNickName()));
                    myOrdersNum.setText(S.getStr(AppHolder.getInstance().getMemberInfo().getOrderCount()));
                    myExpressNum.setText(S.getStr(AppHolder.getInstance().getMemberInfo().getExpressCount()));
                    myScoreNum.setText("0");
                    /*if (AppHolder.getInstance().getHouse() != null) {
                        LLog.d(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()) + "133");
                        myCommunityName.setText(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
                        myCommunityHouse.setText(S.getStr(AppHolder.getInstance().getHouse().getBuilding()) + getString(R.string.floor) + S.getStr(AppHolder.getInstance().getHouse().getUnit()) + "单元" + S.getStr(AppHolder.getInstance().getHouse().getRoom()) + "室");
                    }*/
                }
            } else {
                if ("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                    Log.e("communityName---", S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
                    myCommunityName.setText(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
                    CustomApplication.setImageWithDiffDisplayImageOptions(S.isNull(AppHolder.getInstance().getMemberInfo().getImgUrl()) ? "aa" : AppHolder.getInstance().getMemberInfo().getImgUrl(), userIcon, null, mBannerOptions);
                    welcomeText.setText("欢迎您  ");
                    myExpressBtnWirhNum.setClickable(false);
                }
            }

            themeConfig = new ThemeConfig.Builder().setTitleBarBgColor(Color.rgb(40, 149, 118)).setTitleBarTextColor(Color.WHITE).setTitleBarIconColor(Color.WHITE).setFabNornalColor(Color.rgb(0xd3, 0x29, 0x24)).setFabPressedColor(Color.RED).setIconCamera(R.mipmap.ic_action_camera).setCheckNornalColor(Color.rgb(0xd2, 0xd2, 0xd2)).setCheckSelectedColor(Color.rgb(0xd3, 0x29, 0x24)).build();
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
                        for (int i = 0; i < mPhotoList.size(); i++) {
                            LLog.w(mPhotoList.get(i).getPhotoPath());
                        }
                        postPhoto();
                    }
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            };
        }
        return rootView;
    }

    public void initData() {
        if (AppHolder.getInstance().getHouse() != null) {
            LLog.d(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()) + "133");
            if ("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                myCommunityName.setText(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
                myCommunityHouse.setText(S.getStr(AppHolder.getInstance().getHouse().getBuilding()) + getString(R.string.floor) + S.getStr(AppHolder.getInstance().getHouse().getUnit()) + "单元" + S.getStr(AppHolder.getInstance().getHouse().getRoom()) + "室");

            }
        }

        CustomApplication.setImageWithDiffDisplayImageOptions(S.isNull(AppHolder.getInstance().getMemberInfo().getImgUrl()) ? "aa" : AppHolder.getInstance().getMemberInfo().getImgUrl(), userIcon, null, mBannerOptions);
        if (AppHolder.getInstance().getVisiterFlg() == 1) {
            welcomeText.setText("欢迎您  ");
        } else {
            welcomeText.setText("欢迎您  " + AppHolder.getInstance().getMemberInfo().getNickName());
        }
        myOrdersNum.setText("" + AppHolder.getInstance().getMemberInfo().getOrderCount());
        myExpressNum.setText("" + AppHolder.getInstance().getMemberInfo().getExpressCount());
        myScoreNum.setText("0");
    }

    //获取会员基本信息
    private void getMemberInfo(String memberId, String communityId) {
        showProgress(true);
        if (isRefreshUserInfo) {
            return;
        }
        isRefreshUserInfo = true;
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.getmemberinfoUrl);

        map.put("memberId", memberId);
        map.put("communityId", communityId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().getmemberinfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<LoginBean>((BaseActivity) context) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(LoginBean loginBean) {
                super.onNext(loginBean);
                showProgress(false);
                if (TextUtils.equals("000", loginBean.getCode())) {
                    memberInfo = loginBean.getData().getMemberInfo();
//                    AppHolder.getInstance().setMemberInfo(memberInfo);

                    CustomApplication.setImageWithDiffDisplayImageOptions(S.isNull(AppHolder.getInstance().getMemberInfo().getImgUrl()) ? "aa" : AppHolder.getInstance().getMemberInfo().getImgUrl(), userIcon, null, mBannerOptions);

                    welcomeText.setText("欢迎你  " + AppHolder.getInstance().getMemberInfo().getNickName());
                    myOrdersNum.setText("" + AppHolder.getInstance().getMemberInfo().getOrderCount());
                    myExpressNum.setText("" + AppHolder.getInstance().getMemberInfo().getExpressCount());
                    myScoreNum.setText("0");
                    if (AppHolder.getInstance().getHouse() != null) {
                        LLog.d(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()) + "133");
                        if ("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                            myCommunityName.setText(S.getStr(AppHolder.getInstance().getHouse().getCommunityName()));
                            myCommunityHouse.setText(S.getStr(AppHolder.getInstance().getHouse().getBuilding()) + getString(R.string.floor) + S.getStr(AppHolder.getInstance().getHouse().getUnit()) + "单元" + S.getStr(AppHolder.getInstance().getHouse().getRoom()) + "室");
                        }
                    }

                }
                isRefreshUserInfo = false;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                isRefreshUserInfo = false;
                showProgress(false);
            }
        });
    }

    public static DisplayImageOptions mBannerOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.touxiang2x)
            // 图片加载的时候显示的图片
            .showImageForEmptyUri(R.drawable.touxiang2x)
            // 图片加载地址为空的时候显示的图片
            .showImageOnFail(R.drawable.touxiang2x)
            // 图片加载失败的时候显示的图片
            .cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    @OnClick({R.id.welcome_text, R.id.top_bg_layout, R.id.my_orders_num, R.id.my_order_btn_with_num, R.id.my_express_num, my_express_btn_wirh_num, R.id.my_score_num, R.id.my_score_btn_with_num, R.id.userIcon, R.id.my_setting_btn, R.id.infoLayout, my_community_name, my_community_house, R.id.my_locate_btn, R.id.shop_order_btn, R.id.my_pay_history_btn, R.id.my_activity_btn, R.id.my_complain_btn, R.id.my_repair_btn, R.id.my_appointment_btn, R.id.my_community_info_btn, R.id.my_express_btn, R.id.my_coupon_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.welcome_text:
                break;
            case R.id.top_bg_layout:
                break;
            case R.id.my_orders_num:
                break;
            case R.id.my_order_btn_with_num:
                break;
            case R.id.my_express_num:
                break;
            case R.id.my_express_btn_wirh_num://我的快递
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            Intent intent = new Intent(this.getActivity(), ExpressActivity.class);
                            startActivity(intent);
                        }
                    }

                }
                break;
            case R.id.my_score_num:
                break;
            case R.id.my_score_btn_with_num:
                Utils.toastError(this.getActivity(), "尚未开通，敬请期待");
                break;
            case R.id.userIcon:
                final HintMessageDialog setHead = new HintMessageDialog(getActivity());
                setHead.showHintDialogForSetHead(new SetheadListener() {
                    @Override
                    public void btnCameraListener() {//相机
                        setHead.dismiss();
                        type = 861;
                        if (!Utils.isExitsSdcard()) {// 判读SD卡是否存在
                            Utils.toastError(getActivity(), "SD卡不存在");
                            return;
                        }
                        requestStoragePermissions(type);
                    }

                    @Override
                    public void submitListener() {//相册
                        setHead.dismiss();
                        type = 860;
                        requestStoragePermissions(type);
                    }

                    @Override
                    public void cancelListener() {
                        setHead.dismiss();
                    }
                });
                break;
            case R.id.my_setting_btn://设置
                startActivity(new Intent(this.getActivity(), MySettingActivity.class));
                break;
            case R.id.infoLayout:
                break;
            case my_community_name:
                break;
            case my_community_house:
                break;
            case R.id.my_locate_btn://社区
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            Intent intent = new Intent(this.getActivity(), HouseListActivity2.class);
                            startActivity(intent);
                        }
                    }

                }
                break;
            case R.id.shop_order_btn:
                break;
            case R.id.my_pay_history_btn:
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            Intent intent = new Intent(this.getActivity(), PropertyPaymentActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case R.id.my_activity_btn://我的活动
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            startActivity(new Intent(this.getActivity(), CommunityHomeActivity.class));
                        }
                    }
                }
                break;
            case R.id.my_complain_btn://投诉
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            Utils.startActivity(null, getActivity(), MyComplaintActivity.class);
                        }
                    }

                }
                break;
            case R.id.my_repair_btn://保修
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            Utils.startActivity(null, getActivity(), FamillyHotLineListActivity.class);
                        }
                    }

                }
                break;
            case R.id.my_appointment_btn://预约访客
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            startActivity(new Intent(this.getActivity(), MyVisitActivity.class));
                        }
                    }

                }
                break;
            case R.id.my_community_info_btn://我的分类信息
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            Intent intent = new Intent(this.getActivity(), SecondHandActivity.class);
                            intent.addFlags(StrConstant.GET_OWNER_LIST);
                            startActivity(intent);
                        }
                    }

                }
                break;
            case R.id.my_express_btn://快递代收
                //0:正常用户; 1:游客
                if (AppHolder.getInstance().getVisiterFlg() == 1) {
                    showHintDialog(getString(R.string.visitor_want_to_register));
                } else if (AppHolder.getInstance().getVisiterFlg() == 0) {
                    if ("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                            || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())) {
                        Utils.showShortToast(this.getActivity(), getString(R.string.manager_cannot));
                    } else {
                        if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
                            showProperDialog(getResources().getString(R.string.not_found_owner_information));
                        } else {
                            Intent intent = new Intent(this.getActivity(), ExpressActivity.class);
                            startActivity(intent);
                        }
                    }

                }
                break;
            case R.id.my_coupon_btn:
                Utils.toastError(getActivity(), "暂无优惠券信息");
                break;

        }
    }

    private void showHintDialog(String info) {
        final HintMessageDialog hintDialog = new HintMessageDialog(this.getContext());
        hintDialog.showHintDialog("提示", info, new HintDialogListener() {
            @Override
            public void submitListener() {
                hintDialog.dismiss();
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void cancelListener() {
                hintDialog.dismiss();
            }
        });
    }

    private void showProperDialog(String info) {
        final HintMessageDialog hintDialog = new HintMessageDialog(this.getContext());
        hintDialog.showHintDialog("提示", info, new HintDialogListener() {
            @Override
            public void submitListener() {
                hintDialog.dismiss();
                Intent intent = new Intent(getActivity(), HouseListActivity.class);
                startActivity(intent);
            }

            @Override
            public void cancelListener() {
                hintDialog.dismiss();
            }
        });
    }

    public void noticeChildStorageComplete(int type) {
        if (type == REQUEST_CODE_CAMERA) {
            configGalleryFinal();
            GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfigBuilder, mOnHanlderResultCallback);
        } else if (type == REQUEST_CODE_GALLERY) {
            configGalleryFinal();
            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfigBuilder, mOnHanlderResultCallback);
        }
    }

    //选择相册
    public void configGalleryFinal() {
        functionConfigBuilder = new FunctionConfig.Builder().setEnableCamera(true).setEnableEdit(true).setEnableCrop(true).setEnableRotate(false).setCropSquare(true).setEnablePreview(true).setMutiSelectMaxSize(1).setForceCrop(true).setForceCropEdit(false).build();
        CoreConfig coreConfig = new CoreConfig.Builder(getActivity(), new UILImageLoader(), themeConfig).setFunctionConfig(functionConfigBuilder).setPauseOnScrollListener(pauseOnScrollListener).build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * 上传图片到七牛
     */
    private void postPhoto() {

        if (mPhotoList != null && mPhotoList.size() > 0) {
            showProgress(true);
            UploadManager uploadManager = new UploadManager();
            for (PhotoInfo info : mPhotoList) {
//                LLog.w(info.getPhotoPath());
                String filePath = info.getPhotoPath();
                File file = new File(filePath);
                uploadManager.put(file, file.getName(), getToken(), new UpCompletionHandler() {
                    @Override
                    public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {

                        LLog.w(responseInfo.toString());
                        LLog.w("responseInfo.isOK() =" + responseInfo.isOK());
                        if (responseInfo.isOK()) {
                            String headUrl = StrConstant.BASE_URL + "/" + s;
                            LLog.w("11111111111111111");
                            AppHolder.getInstance().getMemberInfo().setImgUrl(headUrl);
                            LLog.w("AppHolder.getInstance().getMemberInfo().getImgUrl() =" + AppHolder.getInstance().getMemberInfo().getImgUrl());
                            CustomApplication.setImageWithDiffDisplayImageOptions(S.isNull(headUrl) ? "aa" : headUrl, userIcon, null, mBannerOptions);
                            showProgress(false);
                            changeMemberIcon(headUrl);

                        } else {
                            Utils.toastError(getActivity(), "头像上传失败");
                            showProgress(false);
                        }
                    }
                }, null);
            }
        }
    }

    private void changeMemberIcon(String headUrl) {
        showProgress(true);
        Map<String, String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.memberHeadImgUrl);

        map.put("memberId", AppHolder.getInstance().getMemberInfo().getMemberId());
        map.put("headImg", headUrl);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().changeMemberIcon(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>((BaseActivity) context) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                showProgress(false);
                LLog.d("onResponseCallback: " + response);
                if (TextUtils.equals("000", resultCode)) {
                    Utils.showShortToast(getActivity(), "修改头像成功");
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showProgress(false);
            }
        });
    }

    /**
     * 获取七牛token
     *
     * @return
     */
    private String getToken() {

        Mac mac = new Mac(StrConstant.QINIU_AK, StrConstant.QINIU_SK);
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
     * 申请文件读写权限
     */
    public void requestStoragePermissions(int type) {
        LLog.w("??????????????????");
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context, perms)) {
            noticeChildStorageComplete(type);
        } else {
            EasyPermissions.requestPermissions(this, "App正常运行需要存储权限、媒体权限", 1, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
