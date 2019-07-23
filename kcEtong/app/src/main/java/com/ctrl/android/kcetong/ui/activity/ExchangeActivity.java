package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends BaseActivity {
    @BindView(R.id.btn_second_market)
    Button btn_second_market;
    @BindView(R.id.btn_house_info)
    Button btn_house_info;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        toolbarBaseSetting("互通有无", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_second_market, R.id.btn_house_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_second_market://咸鱼市场
                Intent intent = new Intent(ExchangeActivity.this,SecondHandActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_house_info://房产信息
                startActivity(new Intent(ExchangeActivity.this,HouseInfoActivity.class));
                break;
        }
    }
}
