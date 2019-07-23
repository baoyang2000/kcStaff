package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.fragment.MyComplaintFragment;
import com.ctrl.third.common.library.utils.AnimUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyComplaintActivity extends BaseActivity {

    @BindView(R.id.toolbar_right_btn)//右侧toolbar标题
    TextView     toolbar_right_btn;
    @BindView(R.id.viewpager_complaint)
    ViewPager viewpager_complaint;
    @BindView(R.id.btn_my_complaint_all)
    TextView  btn_my_complaint_all;
    @BindView(R.id.btn_my_complaint_progressing)
    TextView  btn_my_complaint_progressing;
    @BindView(R.id.btn_my_complaint_progressed)
    TextView  btn_my_complaint_progressed;
    @BindView(R.id.btn_my_complaint_end)
    TextView  btn_my_complaint_end;
    @BindView(R.id.btn_my_complaint_pending)
    TextView  btn_my_complaint_pending;
    private List<Fragment> fragments = new ArrayList<>();
    private JasonViewPagerAdapter adapter;
    private MyComplaintFragment   fragment0;
    private MyComplaintFragment   fragment1;
    private MyComplaintFragment   fragment2;
    private MyComplaintFragment   fragment3;
    private MyComplaintFragment   fragment4;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_complaint);
        ButterKnife.bind(this);
        toolbarBaseSetting("我的投诉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyComplaintActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("添加");
    }

    @Override
    protected void initData() {

        fragments.addAll(load());
        adapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager_complaint.setAdapter(adapter);
        viewpager_complaint.setCurrentItem(0);
        viewpager_complaint.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        all();
                        break;
                    case 1:
                        pendding();
                        break;
                    case 2:
                        progressing();
                        break;
                    case 3:
                        progressed();
                        break;
                    case 4:
                        end();
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.btn_my_complaint_all, R.id.btn_my_complaint_progressing, R.id.btn_my_complaint_progressed, R.id.btn_my_complaint_end, R.id.btn_my_complaint_pending, R.id.toolbar_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_my_complaint_all:
                all();
                viewpager_complaint.setCurrentItem(0);
                break;
            case R.id.btn_my_complaint_progressing:
                progressing();
                viewpager_complaint.setCurrentItem(2);
                break;
            case R.id.btn_my_complaint_progressed:
                progressed();
                viewpager_complaint.setCurrentItem(3);
                break;
            case R.id.btn_my_complaint_end:
                end();
                viewpager_complaint.setCurrentItem(4);
                break;
            case R.id.btn_my_complaint_pending:
                pendding();
                viewpager_complaint.setCurrentItem(1);
                break;
            case R.id.toolbar_right_btn:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Intent intent = new Intent(MyComplaintActivity.this, MyComplaintAddActivity.class);
                    startActivityForResult(intent, 4000);
                    AnimUtil.intentSlidIn(MyComplaintActivity.this);
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyComplaintActivity.this, getString(R.string.manager_cannot));
                }
                break;
        }
    }

    /**
     * 全部
     */
    private void all() {
        btn_my_complaint_all.setBackgroundResource(R.drawable.button_left_shap_checked);
        btn_my_complaint_all.setTextColor(Color.WHITE);
        btn_my_complaint_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_pending.setTextColor(Color.GRAY);
        btn_my_complaint_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressing.setTextColor(Color.GRAY);
        btn_my_complaint_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressed.setTextColor(Color.GRAY);
        btn_my_complaint_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_complaint_end.setTextColor(Color.GRAY);
    }

    /**
     * 待处理
     */
    private void pendding() {
        btn_my_complaint_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_complaint_all.setTextColor(Color.GRAY);
        btn_my_complaint_pending.setBackgroundResource(R.drawable.button_center_shap_checked);
        btn_my_complaint_pending.setTextColor(Color.WHITE);
        btn_my_complaint_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressing.setTextColor(Color.GRAY);
        btn_my_complaint_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressed.setTextColor(Color.GRAY);
        btn_my_complaint_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_complaint_end.setTextColor(Color.GRAY);
    }

    /**
     * 处理中
     */
    private void progressing() {
        btn_my_complaint_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_complaint_all.setTextColor(Color.GRAY);
        btn_my_complaint_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_pending.setTextColor(Color.GRAY);
        btn_my_complaint_progressing.setBackgroundResource(R.drawable.button_center_shap_checked);
        btn_my_complaint_progressing.setTextColor(Color.WHITE);
        btn_my_complaint_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressed.setTextColor(Color.GRAY);
        btn_my_complaint_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_complaint_end.setTextColor(Color.GRAY);
    }

    /**
     * 已处理
     */
    private void progressed() {
        btn_my_complaint_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_complaint_all.setTextColor(Color.GRAY);
        btn_my_complaint_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_pending.setTextColor(Color.GRAY);
        btn_my_complaint_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressing.setTextColor(Color.GRAY);
        btn_my_complaint_progressed.setBackgroundResource(R.drawable.button_center_shap_checked);
        btn_my_complaint_progressed.setTextColor(Color.WHITE);
        btn_my_complaint_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_complaint_end.setTextColor(Color.GRAY);
    }

    /**
     * 已结束
     */
    private void end() {
        btn_my_complaint_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_complaint_all.setTextColor(Color.GRAY);
        btn_my_complaint_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_pending.setTextColor(Color.GRAY);
        btn_my_complaint_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressing.setTextColor(Color.GRAY);
        btn_my_complaint_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_complaint_progressed.setTextColor(Color.GRAY);
        btn_my_complaint_end.setBackgroundResource(R.drawable.button_right_shap_checked);
        btn_my_complaint_end.setTextColor(Color.WHITE);
    }

    /**
     * 加载fragment
     *
     * @return
     */
    private List<Fragment> load() {
        List<Fragment> list = new ArrayList();
        fragment0 = MyComplaintFragment.newInstance(ConstantsData.MY_COMPLAINT_ALL);
        fragment1 = MyComplaintFragment.newInstance(ConstantsData.MY_COMPLAINT_PENDING);
        fragment2 = MyComplaintFragment.newInstance(ConstantsData.MY_COMPLAINT_PROGRESSING);
        fragment3 = MyComplaintFragment.newInstance(ConstantsData.MY_COMPLAINT_PROGRESSED);
        fragment4 = MyComplaintFragment.newInstance(ConstantsData.MY_COMPLAINT_END);
        list.add(fragment0);
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        list.add(fragment4);
        return list;
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public JasonViewPagerAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (4001 == resultCode && requestCode == 4000) {

            switch (viewpager_complaint.getCurrentItem()) {
                case 0:
                    fragment0.pullToRefreshListView001.setRefreshing(true);
                    break;
                case 1:
                    fragment1.pullToRefreshListView001.setRefreshing(true);
                    break;
                case 2:
                    fragment2.pullToRefreshListView001.setRefreshing(true);
                    break;
                case 3:
                    fragment3.pullToRefreshListView001.setRefreshing(true);
                    break;
                case 4:
                    fragment4.pullToRefreshListView001.setRefreshing(true);
                    break;
            }
        }
    }
}
