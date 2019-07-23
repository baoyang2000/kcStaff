package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.third.common.library.utils.AnimUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopAroundFirstActivity extends BaseActivity {

    @BindView(R.id.easy_bus_btn)//公交出行
            TextView easy_bus_btn;
    @BindView(R.id.easy_shop_btn)//便民商家
            TextView easy_shop_btn;
    private String TITLE = StrConstant.SHOP_AROUND_FIRST_TITLE;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_around_first);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.easy_bus_btn, R.id.easy_shop_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.easy_bus_btn:
                Utils.startActivity(null, ShopAroundFirstActivity.this, EasyBusActivity.class);
                AnimUtil.intentSlidIn(ShopAroundFirstActivity.this);
                break;
            case R.id.easy_shop_btn:
                Utils.startActivity(null,ShopAroundFirstActivity.this,EasyShopAroundActivity.class);
                AnimUtil.intentSlidIn(ShopAroundFirstActivity.this);
                break;
        }
    }

    /**
     * header 左侧按钮
     */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MessageUtils.showShortToast(MallShopActivity.this, "AA");
                onBackPressed();
            }
        });
        return true;
    }

    /**
     * 页面标题
     */
    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }
}
