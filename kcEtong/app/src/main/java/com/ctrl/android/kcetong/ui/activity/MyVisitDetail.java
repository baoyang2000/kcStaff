package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Visit;
import com.ctrl.android.kcetong.model.VisityinfoBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.mob.tools.utils.UIHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.action;
import static com.ctrl.android.kcetong.R.id.visit_car_num;
import static com.ctrl.android.kcetong.R.id.visit_count;
import static com.ctrl.android.kcetong.R.id.visit_name;
import static com.ctrl.android.kcetong.R.id.visit_num;
import static com.ctrl.android.kcetong.R.id.visit_qrc;
import static com.ctrl.android.kcetong.R.id.visit_room;
import static com.ctrl.android.kcetong.R.id.visit_stop_time;
import static com.ctrl.android.kcetong.R.id.visit_time;

public class MyVisitDetail extends BaseActivity implements PlatformActionListener, Handler.Callback{

    @BindView(visit_num)//到访编码
            TextView     visitNum;
    @BindView(visit_room)//到访房间
            TextView     visitRoom;
    @BindView(visit_name)//到访人
            TextView     visitName;
    @BindView(visit_time)//到访时间
            TextView     visitTime;
    @BindView(visit_count)//到访人数
            TextView     visitCount;
    @BindView(visit_car_num)//车牌号
            TextView     visitCarNum;
    @BindView(visit_stop_time)//预计停留时间
            TextView     visitStopTime;
    @BindView(visit_qrc)//二维码地址
            ImageView    visitQrc;
    @BindView(R.id.visit_qq)//qq
            ImageView    visitQq;
    @BindView(R.id.visit_wx)//微信
            ImageView    visitWx;
    @BindView(R.id.activity_my_visit_detail)
            LinearLayout activityMyVisitDetail;

    private String communityVisitId;
    private Visit  visitDetail;
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    @Override
    protected void initView(Bundle savedInstanceState) {
        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_my_visit_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        toolbarBaseSetting("我的到访", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(MyVisitDetail.this);
                Intent intent = new Intent();
                setResult(7001, intent);
                MyVisitDetail.this.finish();
            }
        });
        communityVisitId = getIntent().getStringExtra("communityVisitId");
        getMyvisitdetail(communityVisitId);
    }

    //到访详情
    private void getMyvisitdetail(String communityVisitId) {
        showProgress(true);
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.visitiinfoUrl);
        map.put("communityVisitId", communityVisitId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().visitedetailinfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<VisityinfoBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(VisityinfoBean visityinfoBean) {
                super.onNext(visityinfoBean);
                if (TextUtils.equals("000", visityinfoBean.getCode())) {
                    visitDetail = visityinfoBean.getData().getVisitInfo();
                    visitQrc.setLayoutParams(new LinearLayout.LayoutParams(Utils.getDisplayWidth(MyVisitDetail.this) * 2 / 3, Utils.getDisplayWidth(MyVisitDetail.this) * 2 / 3));
                    Glide.with(MyVisitDetail.this).load(visitDetail.getQrImgUrl() == null || visitDetail.getQrImgUrl().equals("") ? "aa" : visitDetail.getQrImgUrl()).placeholder(R.drawable.default_image).into(visitQrc);
                    visitNum.setText(S.getStr(visitDetail.getVisitNum()));
                    visitRoom.setText(S.getStr(visitDetail.getCommunityName()) + " " + S.getStr(visitDetail.getBuilding()) + "-" + S.getStr(visitDetail.getUnit()) + "-" + S.getStr(visitDetail.getRoom()));
                    visitName.setText(S.getStr(visitDetail.getVisitorName()));
                    visitTime.setText(D.getDateStrFromStamp(ConstantsData.YYYY_MM_DD, visitDetail.getArriveTime()));
                    visitCount.setText(S.getStr(visitDetail.getPeopleNum()));
                    visitCarNum.setText(S.getStr(visitDetail.getNumberPlates()));
                    visitStopTime.setText(S.getStr(visitDetail.getResidenceTime()) + "小时");


                }
                showProgress(false);
            }
        });


    }

    @OnClick({R.id.visit_qq, R.id.visit_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.visit_qq:
                MessageUtils.showLongToast(this, "正在启动QQ");
                //showProgress(true);
                Platform.ShareParams qqsp = new Platform.ShareParams();
                qqsp.setTitle("二维码");
                qqsp.setTitleUrl(visitDetail.getQrImgUrl());//分享后点击跳转链接
                qqsp.setText("请扫描二维码");
                qqsp.setImageUrl(visitDetail.getQrImgUrl());
                Platform qqPlatform = ShareSDK.getPlatform(this, QQ.NAME);
                qqPlatform.setPlatformActionListener(this);
                qqPlatform.share(qqsp);
                break;
            case R.id.visit_wx:
                /*MessageUtils.showLongToast(this, "正在启动微信");
                Platform.ShareParams wxsp = new Platform.ShareParams();
                //wxsp.setShareType(Platform.SHARE_IMAGE);
                wxsp.setTitle("二维码");
                wxsp.setTitleUrl(visitDetail.getQrImgUrl());//分享后点击跳转链接
                //wxsp.setTitleUrl("到访二维码");//分享后点击跳转链接
                wxsp.setText("请扫描二维码");
                wxsp.setImageUrl(visitDetail.getQrImgUrl());
                Platform wxPlatform = ShareSDK.getPlatform(this, Wechat.NAME);
                wxPlatform.setPlatformActionListener(this);
                wxPlatform.share(wxsp);*/
                Platform.ShareParams wxsp = new Platform.ShareParams();
                Platform wechat = ShareSDK.getPlatform(this, Wechat.NAME);
                boolean clientValid = wechat.isClientValid();
                if(!clientValid){
                    MessageUtils.showLongToast(this,"请安装微信客户端");
                    return;
                }
                MessageUtils.showLongToast(this, "正在启动微信");
                wxsp.setShareType(Platform.SHARE_WEBPAGE);
                wxsp.setTitle("二维码");
                wxsp.setUrl(visitDetail.getQrImgUrl());//分享后点击跳转链接
                wxsp.setTitleUrl("到访二维码");//分享后点击跳转链接
                wxsp.setText("请扫描二维码");
                wxsp.setImageUrl(visitDetail.getQrImgUrl());
                Platform wxPlatform = ShareSDK.getPlatform(this,Wechat.NAME);
                wxPlatform.setPlatformActionListener(this);
                wxPlatform.share(wxsp);

                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case 1: {
                // 成功
                //Toast.makeText(this,"分享成功", 10000).show();
                //System.out.println("分享回调成功------------");
                MessageUtils.showShortToast(this,"分享成功");
            }
            break;
            case 2: {
                // 失败
                //Toast.makeText(MainActivity.this, "分享失败", 10000).show();
                MessageUtils.showShortToast(this,"分享失败");
            }
            break;
            case 3: {
                // 取消
                //Toast.makeText(MainActivity.this,"分享取消", 10000).show();
                MessageUtils.showShortToast(this,"分享取消");
            }
            break;
        }

        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        //showProgress(false);
        // 成功
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg,this);
    }

    @Override
    public void onError(Platform platform, int i, Throwable t) {
        // 失敗
        //打印错误信息,print the error msg
        t.printStackTrace();
        //错误监听,handle the error msg
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        //showProgress(false);
        // 取消
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);

    }
}
