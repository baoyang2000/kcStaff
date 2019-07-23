package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Kind;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WantBuyActivity extends BaseActivity {
    @BindView(R.id.rl_want_buy)
    RelativeLayout rl_want_buy;
    @BindView(R.id.et_baobei_title)
    EditText       et_baobei_title;
    @BindView(R.id.et_baobei_detail)
    EditText       et_baobei_detail;
    @BindView(R.id.et_baobei_originalprice)
    EditText       et_baobei_originalprice;
    @BindView(R.id.et_contact)
    EditText       et_contact;
    @BindView(R.id.et_contact_telephone)
    EditText       et_contact_telephone;
    @BindView(R.id.tv_secong_hand_buy_type)
    TextView       tv_secong_hand_buy_type;
    @BindView(R.id.toolbar_right_btn)
    TextView       toolbar_right_btn;

    public static final int WANT_BUY_TYPE_CODE=0;
    private Kind kind;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_want_buy);
        ButterKnife.bind(this);
        toolbarBaseSetting("我要购", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WantBuyActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("完成");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case WANT_BUY_TYPE_CODE:
                if(resultCode == RESULT_OK){
                    kind = (Kind) data.getSerializableExtra("kind");
                    tv_secong_hand_buy_type.setText(kind.getKindName());
                    Log.d("demo", "kindId: " + kind.getId());
                }
                break;
        }
    }

    @OnClick({R.id.toolbar_right_btn, R.id.rl_want_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_btn://完成
                if (TextUtils.isEmpty(et_baobei_title.getText().toString())) {
                    MessageUtils.showShortToast(WantBuyActivity.this, "标题为空");
                    return;
                }
                if (TextUtils.isEmpty(et_baobei_detail.getText().toString())) {
                    MessageUtils.showShortToast(WantBuyActivity.this, "描述为空");
                    return;
                }
                if (TextUtils.isEmpty(et_baobei_originalprice.getText().toString())) {
                    MessageUtils.showShortToast(WantBuyActivity.this, "价格为空");
                    return;
                }
                if(kind == null){
                    MessageUtils.showShortToast(WantBuyActivity.this, "请选择分类");
                    return;
                }else {
                    if (kind.getId() == null || kind.getId().equals("")) {
                        MessageUtils.showShortToast(WantBuyActivity.this, "请选择分类");
                        return;
                    }
                }
                if (TextUtils.isEmpty(et_contact.getText().toString())) {
                    MessageUtils.showShortToast(WantBuyActivity.this, "联系人为空");
                    return;
                }
                if (TextUtils.isEmpty(et_contact_telephone.getText().toString())) {
                    MessageUtils.showShortToast(WantBuyActivity.this, "联系电话为空");
                    return;
                }
                if (!Utils.isMobileNO(et_contact_telephone.getText().toString().trim())){
                    MessageUtils.showShortToast(WantBuyActivity.this, "联系电话不正确");
                    return;
                }
                showProgress(true);
                requestGoodsAdd(AppHolder.getInstance().getHouse().getCommunityId(),
                        AppHolder.getInstance().getMemberInfo().getMemberId(),
                        AppHolder.getInstance().getProprietor().getProprietorId(),
                        et_baobei_title.getText().toString(),
                        et_baobei_detail.getText().toString(),
                        et_contact.getText().toString(),
                        et_contact_telephone.getText().toString(),
                        et_baobei_originalprice.getText().toString(),
                        kind.getId(),
                        "",
                        StrConstant.WANT_BUY_TRANSACTION_TYPE,
                        StrConstant.VISIBLE_TYPE
                );
                break;
            case R.id.rl_want_buy:
                Intent intent=new Intent(WantBuyActivity.this,ClassifyActivity.class);
                intent.addFlags(StrConstant.WANT_BUY_TYPE);
                startActivityForResult(intent,WANT_BUY_TYPE_CODE);
                break;
        }
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "pm.ppt.usedGoods.add");
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
        params.put("transactionType", transactionType);
        params.put("visibleType", visibleType);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);

        LLog.d(params+"");
        params.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestGoodsAdd(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseTSubscriber<ResponseBody>(this) {
                        @Override
                        public void onResponseCallback(JSONObject response, String resultCode) {
                            LLog.d(response+"");
                            showProgress(false);
                            try {
                                if(TextUtils.equals("000",response.getString("code"))){
                                    MessageUtils.showShortToast(WantBuyActivity.this, "信息发布成功");
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
}
