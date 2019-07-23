package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.ui.adapter.ViewPagerAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.fragment.MyOrderFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.radio_delivery)
    RadioButton mRadioDelivery;
    @BindView(R.id.radio_receipt)
    RadioButton mRadioReceipt;
    @BindView(R.id.radio_evaluate)
    RadioButton mRadioEvaluate;
    @BindView(R.id.radio_finish)
    RadioButton mRadioFinish;

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.viewpager)
    ViewPager  mViewpager;

    SparseArray<Fragment> fragments = new SparseArray<Fragment>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        toolbarBaseSetting("团购订单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderActivity.this.finish();
            }
        });
        MyOrderFragment fragment1 = MyOrderFragment.newInstance("0");
        MyOrderFragment fragment2 = MyOrderFragment.newInstance("1");
        MyOrderFragment fragment3 = MyOrderFragment.newInstance("2");
        MyOrderFragment fragment4 = MyOrderFragment.newInstance("3");
        fragments.put(0, fragment1);
        fragments.put(1, fragment2);
        fragments.put(2, fragment3);
        fragments.put(3, fragment4);
        mViewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mViewpager.setOnPageChangeListener(new FragmentOnPageChangeListener());
        mViewpager.setOffscreenPageLimit(5);

        mRadioDelivery.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_delivery:
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.radio_receipt:
                        mViewpager.setCurrentItem(1);
                        break;
                    case R.id.radio_evaluate:
                        mViewpager.setCurrentItem(2);
                        break;
                    case R.id.radio_finish:
                        mViewpager.setCurrentItem(3);
                        break;
                }
            }
        });
        mRadioDelivery.setText("待发货");
        mRadioReceipt.setText("待收货");
        mRadioEvaluate.setText("待评价");
        mRadioFinish.setText("已完成");
    }

    @Override
    protected void initData() {

    }
    public class FragmentOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int index) {
            switch (index){
                case 0:
                    mRadioGroup.check(R.id.radio_delivery);
                    break;
                case 1:
                    mRadioGroup.check(R.id.radio_receipt);
                    break;
                case 2:
                    mRadioGroup.check(R.id.radio_evaluate);
                    break;
                case 3:
                    mRadioGroup.check(R.id.radio_finish);
                    break;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
