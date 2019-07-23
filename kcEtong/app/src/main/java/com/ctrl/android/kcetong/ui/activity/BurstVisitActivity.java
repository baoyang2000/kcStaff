package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Img;
import com.ctrl.android.kcetong.model.Visit;
import com.ctrl.android.kcetong.model.VisityinfoBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

import static com.ctrl.android.kcetong.R.id.img_01;
import static com.ctrl.android.kcetong.R.id.img_02;
import static com.ctrl.android.kcetong.R.id.img_03;
import static com.ctrl.android.kcetong.R.id.visit_agree;
import static com.ctrl.android.kcetong.R.id.visit_car;
import static com.ctrl.android.kcetong.R.id.visit_count;
import static com.ctrl.android.kcetong.R.id.visit_host;
import static com.ctrl.android.kcetong.R.id.visit_name;
import static com.ctrl.android.kcetong.R.id.visit_no_home;
import static com.ctrl.android.kcetong.R.id.visit_refuse;
import static com.ctrl.android.kcetong.R.id.visit_room;
import static com.ctrl.android.kcetong.R.id.visit_stop;
import static com.ctrl.android.kcetong.R.id.visit_tel;

/**
 * @项目名称: 诚信行<br>
 * @类描述: <br>
 * @创建人： whs <br>
 * @创建时间： 2017/1/16 15:59 <br>
 * @修改人： <br>
 * @修改时间: 2017/1/16 15:59 <br>
 */
public class BurstVisitActivity extends BaseActivity {


    @BindView(visit_name)
    TextView     visitName;
    @BindView(visit_host)
    TextView     visitHost;
    @BindView(visit_room)
    TextView     visitRoom;
    @BindView(visit_count)
    TextView     visitCount;
    @BindView(visit_car)
    TextView     visitCar;
    @BindView(visit_tel)
    TextView     visitTel;
    @BindView(visit_stop)
    TextView     visitStop;
    @BindView(img_01)
    ImageView    img01;
    @BindView(img_02)
    ImageView    img02;
    @BindView(img_03)
    ImageView    img03;
    @BindView(visit_refuse)
    TextView     visitRefuse;
    @BindView(visit_agree)
    TextView     visitAgree;
    @BindView(visit_no_home)
    TextView     visitNoHome;
    @BindView(R.id.activity_burst_visit)
    LinearLayout activityBurstVisit;

    private String communityVisitId;
    private Visit  visitDetail;
    private List<Img> listImg = new ArrayList<>();
    private List<String> imagelist;
    private int flg = 0;
    String handleStatus = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_burst_visit);
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {
        toolbarBaseSetting("我的到访", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(BurstVisitActivity.this);
                Intent intent = new Intent();
                setResult(8001, intent);
                BurstVisitActivity.this.finish();
            }
        });
        communityVisitId = getIntent().getStringExtra("communityVisitId");
        getBurstinfo(communityVisitId);
    }

    private void getBurstinfo(String communityVisitId) {
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
                showProgress(false);
            }

            @Override
            public void onNext(VisityinfoBean visityinfoBean) {
                super.onNext(visityinfoBean);
                if (TextUtils.equals("000", visityinfoBean.getCode())) {
                    visitDetail = visityinfoBean.getData().getVisitInfo();
                    listImg = visityinfoBean.getData().getVisitInfo().getPicList();
                    visitName.setText(S.getStr(visitDetail.getVisitorName()));
                    visitHost.setText(S.getStr(visitDetail.getProprietorName()));
                    visitRoom.setText(S.getStr(visitDetail.getBuilding()) + "-" + S.getStr(visitDetail.getUnit()) + "-" + S.getStr(visitDetail.getRoom()));
                    visitCount.setText(S.getStr(visitDetail.getPeopleNum()));
                    visitCar.setText(S.getStr(visitDetail.getNumberPlates()));
                    visitTel.setText(S.getStr(visitDetail.getVisitorMobile()));
                    visitStop.setText(S.getStr(visitDetail.getResidenceTime()) + "小时");
                    if (listImg != null && listImg.size() > 0) {
                        if (listImg.size() == 1) {
                            Glide.with(BurstVisitActivity.this).load(listImg.get(0).getOriginalImg() == null || (listImg.get(0).getOriginalImg()).equals("") ? "aa" : listImg.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(img01);
                            img01.setVisibility(View.VISIBLE);
                            img02.setVisibility(View.GONE);
                            img03.setVisibility(View.GONE);
                        }
                        if (listImg.size() == 2) {
                            Glide.with(BurstVisitActivity.this).load(listImg.get(0).getOriginalImg() == null || (listImg.get(0).getOriginalImg()).equals("") ? "aa" : listImg.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(img01);
                            Glide.with(BurstVisitActivity.this).load(listImg.get(1).getOriginalImg() == null || (listImg.get(1).getOriginalImg()).equals("") ? "aa" : listImg.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(img02);
                            img01.setVisibility(View.VISIBLE);
                            img02.setVisibility(View.VISIBLE);
                            img03.setVisibility(View.GONE);
                        }

                        if (listImg.size() >= 3) {
                            Glide.with(BurstVisitActivity.this).load(listImg.get(0).getOriginalImg() == null || (listImg.get(0).getOriginalImg()).equals("") ? "aa" : listImg.get(0).getOriginalImg()).placeholder(R.drawable.default_image).into(img01);
                            Glide.with(BurstVisitActivity.this).load(listImg.get(1).getOriginalImg() == null || (listImg.get(1).getOriginalImg()).equals("") ? "aa" : listImg.get(1).getOriginalImg()).placeholder(R.drawable.default_image).into(img02);
                            Glide.with(BurstVisitActivity.this).load(listImg.get(2).getOriginalImg() == null || (listImg.get(2).getOriginalImg()).equals("") ? "aa" : listImg.get(2).getOriginalImg()).placeholder(R.drawable.default_image).into(img03);
                            img01.setVisibility(View.VISIBLE);
                            img02.setVisibility(View.VISIBLE);
                            img03.setVisibility(View.VISIBLE);
                        }
                    } else {
                        img01.setVisibility(View.GONE);
                        img02.setVisibility(View.GONE);
                        img03.setVisibility(View.GONE);
                    }

                    //返回状态（0：已预约 1：已结束 2：同意到访 3：其他 4：拒绝到访 5：业主不在家 6：显示拒绝到访、同意进入、我不在家）
                    if (visitDetail.getReturnStatus() == 0) {
                        visitAgree.setVisibility(View.VISIBLE);
                        visitAgree.setClickable(false);
                        visitAgree.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitAgree.setText("已预约");
                    } else if (visitDetail.getReturnStatus() == 1) {
                        visitAgree.setVisibility(View.VISIBLE);
                        visitAgree.setClickable(false);
                        visitAgree.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitAgree.setText("已结束");
                    } else if (visitDetail.getReturnStatus() == 2) {
                        visitAgree.setVisibility(View.VISIBLE);
                        visitAgree.setClickable(false);
                        visitAgree.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitAgree.setText("同意到访");
                    } else if (visitDetail.getReturnStatus() == 3) {
                        visitAgree.setVisibility(View.VISIBLE);
                        visitAgree.setClickable(false);
                        visitAgree.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitAgree.setText("其他");
                    } else if (visitDetail.getReturnStatus() == 4) {
                        visitAgree.setVisibility(View.VISIBLE);
                        visitAgree.setClickable(false);
                        visitAgree.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitAgree.setText("拒绝到访");
                    } else if (visitDetail.getReturnStatus() == 5) {
                        visitAgree.setVisibility(View.VISIBLE);
                        visitAgree.setClickable(false);
                        visitAgree.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitAgree.setText("业主不在家");
                    } else if (visitDetail.getReturnStatus() == 6) {
                        visitRefuse.setVisibility(View.VISIBLE);
                        visitAgree.setVisibility(View.VISIBLE);
                        visitNoHome.setVisibility(View.VISIBLE);
                    }

                }
                showProgress(false);
            }
        });

    }

    @OnClick({img_01, img_02, img_03, visit_refuse, visit_agree, visit_no_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case img_01:
                imagelist = new ArrayList<String>();
                for (int i = 0; i < listImg.size(); i++) {
                    imagelist.add(listImg.get(i).getOriginalImg());
                }

                break;
            case img_02:
                imagelist = new ArrayList<String>();
                for (int i = 0; i < listImg.size(); i++) {
                    imagelist.add(listImg.get(i).getOriginalImg());
                }
                break;
            case img_03:
                imagelist = new ArrayList<String>();
                for (int i = 0; i < listImg.size(); i++) {
                    imagelist.add(listImg.get(i).getOriginalImg());
                }
                break;
            case visit_refuse:
                flg = 2;
                //1：同意到访、2：拒绝到访、3：我不在家
                 handleStatus = "2";
                HandleVisit(communityVisitId,handleStatus);
                break;
            case visit_agree:
                flg = 1;
                //1：同意到访、2：拒绝到访、3：我不在家
                handleStatus = "1";
                HandleVisit(communityVisitId,handleStatus);
                break;
            case visit_no_home:
                flg = 3;
                //1：同意到访、2：拒绝到访、3：我不在家
                 handleStatus = "3";
                HandleVisit(communityVisitId,handleStatus);
                break;
        }
    }

    //处理到访信息
    private void HandleVisit(String communityVisitId, String handleStatus) {
        showProgress(true);
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.HandleVisitUrl);
        map.put("communityVisitId", communityVisitId);
        map.put("handleStatus", handleStatus);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().handlevisitinfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String reultCode) {
                LLog.d(response + "");
                showProgress(false);
                try {
                    if (TextUtils.equals("000", (CharSequence) response.get("code"))) {
                        LLog.d("成功");
                        if(flg == 1){
                            MessageUtils.showShortToast(BurstVisitActivity.this, "同意到访");

                        }

                        if(flg == 2){
                            MessageUtils.showShortToast(BurstVisitActivity.this,"拒绝到访");
                        }

                        if(flg == 3){
                            MessageUtils.showShortToast(BurstVisitActivity.this,"我不在家");
                        }

                        visitRefuse.setClickable(false);
                        visitRefuse.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitAgree.setClickable(false);
                        visitAgree.setBackgroundResource(R.drawable.gray_bg_shap);
                        visitNoHome.setClickable(false);
                        visitNoHome.setBackgroundResource(R.drawable.gray_bg_shap);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
