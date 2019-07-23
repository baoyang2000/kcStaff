package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.UiUtil;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.third.common.library.utils.AnimUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ctrl.android.kcetong.R.id.search_btn;

public class EasyShopAroundActivity extends BaseActivity {


    @BindView(R.id.keyword_text)//关键词
    EditText       keywordText;
    @BindView(search_btn)//搜索按钮
    Button         searchBtn;
    @BindView(R.id.main_top_layout)
    LinearLayout   mainTopLayout;
    @BindView(R.id.btn_easy_hotel)//酒店
    TextView       btnEasyHotel;
    @BindView(R.id.btn_easy_life)//生活服务
    TextView       btnEasyLife;
    @BindView(R.id.btn_easy_landry)//洗衣
    TextView       btnEasyLandry;
    @BindView(R.id.btn_easy_entertain)//休闲娱乐
    TextView       btnEasyEntertain;
    @BindView(R.id.btn_easy_movie)//电影
    TextView       btnEasyMovie;
    @BindView(R.id.btn_easy_food)//美食
    TextView       btnEasyFood;
    @BindView(R.id.btn_easy_ktv)//ktv
    TextView       btnEasyKtv;
    @BindView(R.id.btn_easy_coffee)//咖啡
    TextView       btnEasyCoffee;
    @BindView(R.id.btn_easy_gym)//健身
    TextView       btnEasyGym;
    @BindView(R.id.btn_easy_parking)//停车
    TextView       btnEasyParking;
    @BindView(R.id.btn_easy_facial)//美容
    TextView       btnEasyFacial;
    @BindView(R.id.btn_easy_traval)//旅游
    TextView       btnEasyTraval;
    @BindView(R.id.activity_easy_shop_around)
    RelativeLayout activityEasyShopAround;
    private String           TITLE             = StrConstant.EASY_SHOP_ARROUND_TITLE;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_easy_shop_around);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(EasyShopAroundActivity.this);
                EasyShopAroundActivity.this.finish();
            }
        });
        initBtnLictener();
    }

    private void initBtnLictener(){

        UiUtil.clickToActivityWithArg(btnEasyHotel, ConstantsData.ARG_HOTEL, EasyShopAroundActivity.this, EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyLife, ConstantsData.ARG_LIFE, EasyShopAroundActivity.this, EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyLandry, ConstantsData.ARG_LANDRY,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyEntertain, ConstantsData.ARG_ENTERTAIN,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyMovie, ConstantsData.ARG_MOVIE,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyFood, ConstantsData.ARG_FOOD,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyKtv, ConstantsData.ARG_KTV,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyCoffee, ConstantsData.ARG_COFFEE,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyGym, ConstantsData.ARG_GYM,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyParking, ConstantsData.ARG_PARKING,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyFacial, ConstantsData.ARG_FACIAL,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);
        UiUtil.clickToActivityWithArg(btnEasyTraval, ConstantsData.ARG_TRAVAL,EasyShopAroundActivity.this,EasyShopAroundListActivity.class);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key_word = keywordText.getText().toString();

                if(!S.isNull(key_word)){
                    Intent intent = new Intent(EasyShopAroundActivity.this, EasyShopAroundListActivity.class);
                    intent.putExtra(ConstantsData.ARG_FLG,key_word);
                    startActivity(intent);
                    AnimUtil.intentSlidIn(EasyShopAroundActivity.this);
                } else {
                    MessageUtils.showShortToast(EasyShopAroundActivity.this,getString(R.string.pls_input_keyword));
                }

            }
        });

    }

}
