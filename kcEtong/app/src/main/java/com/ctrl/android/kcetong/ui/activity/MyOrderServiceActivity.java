package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
import com.ctrl.android.kcetong.ui.fragment.MyOrderServiceFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrderServiceActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener{

    @BindView(R.id.btn_my_repairs_pending)
    TextView  btn_my_repairs_pending;
    @BindView(R.id.btn_my_repairs_progressing)
    TextView  btn_my_repairs_progressing;
    @BindView(R.id.btn_my_repairs_progressed)
    TextView  btn_my_repairs_progressed;
    @BindView(R.id.btn_my_repairs_end)
    TextView  btn_my_repairs_end;
    @BindView(R.id.viewpager_repairs)
    ViewPager viewpager_repairs;
    @BindView(R.id.btn_my_repairs_all)
    TextView btn_my_repairs_all;

    List<Fragment> fragments =new ArrayList<>();
    private JasonViewPagerAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order_service);
        ButterKnife.bind(this);
        toolbarBaseSetting(getString(R.string.my_order_service), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderServiceActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        MyOrderServiceFragment fragment00 = MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_ALL);
        MyOrderServiceFragment fragment01 = MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_PENDING);
        MyOrderServiceFragment fragment02 = MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_PROGRESSING);
        MyOrderServiceFragment fragment03 = MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_PROGRESSED);
        MyOrderServiceFragment fragment04 = MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_END);
        fragments.add(fragment00);
        fragments.add(fragment01);
        fragments.add(fragment02);
        fragments.add(fragment03);
        fragments.add(fragment04);

        adapter=new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        adapter.setOnReloadListener(new JasonViewPagerAdapter.OnReloadListener() {
            @Override
            public void onReload() {
                fragments = null;
                List<Fragment> list = new ArrayList<>();
                list.add( MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_ALL));

                list.add(MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_PENDING));
                list.add(MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_PROGRESSING));
                list.add(MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_PROGRESSED));
                list.add( MyOrderServiceFragment.newInstance(StrConstant.MY_REPAIRS_END));
                adapter.setPagerItems(list);
            }
        });
        viewpager_repairs.setAdapter(adapter);
        viewpager_repairs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                hide();
                switch (position) {
                    case 0:
                        btn_my_repairs_all.setBackgroundResource(R.drawable.button_left_shap_checked);
                        btn_my_repairs_all.setTextColor(Color.WHITE);
                        break;
                    case 1:
                        btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap_checked);
                        btn_my_repairs_pending.setTextColor(Color.WHITE);
                        break;
                    case 2:
                        btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap_checked);
                        btn_my_repairs_progressing.setTextColor(Color.WHITE);
                        break;
                    case 3:
                        btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap_checked);
                        btn_my_repairs_progressed.setTextColor(Color.WHITE);
                        break;
                    case 4:
                        btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap_checked);
                        btn_my_repairs_end.setTextColor(Color.WHITE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.btn_my_repairs_all,R.id.btn_my_repairs_pending, R.id.btn_my_repairs_progressing, R.id.btn_my_repairs_progressed, R.id.btn_my_repairs_end})
    public void onClick(View view) {
        hide();
        switch (view.getId()) {

            case R.id.btn_my_repairs_all:
                btn_my_repairs_all.setBackgroundResource(R.drawable.button_right_shap_checked);
                btn_my_repairs_all.setTextColor(Color.WHITE);
                viewpager_repairs.setCurrentItem(0);
                break;
            case R.id.btn_my_repairs_pending://待受理

                btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap_checked);
                btn_my_repairs_pending.setTextColor(Color.WHITE);
                viewpager_repairs.setCurrentItem(1);
                break;
            case R.id.btn_my_repairs_progressing://已受理
                btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap_checked);
                btn_my_repairs_progressing.setTextColor(Color.WHITE);
                viewpager_repairs.setCurrentItem(2);
                break;
            case R.id.btn_my_repairs_progressed://服务中
                btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap_checked);
                btn_my_repairs_progressed.setTextColor(Color.WHITE);
                viewpager_repairs.setCurrentItem(3);
                break;
            case R.id.btn_my_repairs_end://已结束
                btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap_checked);
                btn_my_repairs_end.setTextColor(Color.WHITE);
                viewpager_repairs.setCurrentItem(4);
                break;
        }
    }
    //重置按钮颜色
    private void hide(){
        btn_my_repairs_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_repairs_all.setTextColor(Color.GRAY);
        btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_pending.setTextColor(Color.GRAY);
        btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressing.setTextColor(Color.GRAY);
        btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressed.setTextColor(Color.GRAY);
        btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_repairs_end.setTextColor(Color.GRAY);
    }
    public JasonViewPagerAdapter getAdapter(){
        return adapter;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(3000==requestCode && RESULT_OK==resultCode){
            adapter.reLoad();
        }
    }

    @Override
    public void onSwitchPagerFragment(int page) {

    }
}
