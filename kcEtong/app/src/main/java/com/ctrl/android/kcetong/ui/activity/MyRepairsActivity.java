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
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.fragment.MyRepairsFragment;
import com.ctrl.third.common.library.utils.AnimUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyRepairsActivity extends BaseActivity {

    @BindView(R.id.toolbar_right_btn)//右侧toolbar标题
    TextView     toolbar_right_btn;
    @BindView(R.id.viewpager_repairs)
    ViewPager viewpager_repairs;
    @BindView(R.id.btn_my_repairs_all)
    TextView  btn_my_repairs_all;
    @BindView(R.id.btn_my_repairs_progressing)
    TextView  btn_my_repairs_progressing;
    @BindView(R.id.btn_my_repairs_progressed)
    TextView  btn_my_repairs_progressed;
    @BindView(R.id.btn_my_repairs_end)
    TextView  btn_my_repairs_end;
    @BindView(R.id.btn_my_repairs_pending)
    TextView  btn_my_repairs_pending;
    private List<Fragment> fragments = new ArrayList<>();
    private JasonViewPagerAdapter adapter;
    private MyRepairsFragment     fragment0;
    private MyRepairsFragment     fragment1;
    private MyRepairsFragment     fragment2;
    private MyRepairsFragment     fragment3;
    private MyRepairsFragment     fragment4;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_repairs);
        ButterKnife.bind(this);
        toolbarBaseSetting("我的报修", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyRepairsActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("添加");
    }

    @Override
    protected void initData() {
        fragments.addAll(load());
        adapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager_repairs.setAdapter(adapter);
        viewpager_repairs.setCurrentItem(0);
        viewpager_repairs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @OnClick({R.id.btn_my_repairs_all, R.id.btn_my_repairs_progressing, R.id.btn_my_repairs_progressed, R.id.btn_my_repairs_end, R.id.btn_my_repairs_pending,R.id.toolbar_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_my_repairs_all:
                progressing();
                viewpager_repairs.setCurrentItem(0);
                break;
            case R.id.btn_my_repairs_progressing:
                progressing();
                viewpager_repairs.setCurrentItem(2);
                break;
            case R.id.btn_my_repairs_progressed:
                progressed();
                viewpager_repairs.setCurrentItem(3);
                break;
            case R.id.btn_my_repairs_end:
                end();
                viewpager_repairs.setCurrentItem(4);
                break;
            case R.id.btn_my_repairs_pending:
                pendding();
                viewpager_repairs.setCurrentItem(1);
                break;
            case R.id.toolbar_right_btn:
                LLog.i(AppHolder.getInstance().getMemberInfo().getSupers() +"");
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Intent intent = new Intent(MyRepairsActivity.this, MyRepairsAddActivity.class);
                    startActivityForResult(intent, 4000);
                    AnimUtil.intentSlidIn(MyRepairsActivity.this);
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(MyRepairsActivity.this, getString(R.string.manager_cannot));
                }
                break;
        }
    }

    /**
     * 加载fragment
     *
     * @return
     */
    private List<Fragment> load() {
        List<Fragment> list = new ArrayList();
        fragment0 = MyRepairsFragment.newInstance(ConstantsData.MY_COMPLAINT_ALL);
        fragment1 = MyRepairsFragment.newInstance(ConstantsData.MY_COMPLAINT_PENDING);
        fragment2 = MyRepairsFragment.newInstance(ConstantsData.MY_COMPLAINT_PROGRESSING);
        fragment3 = MyRepairsFragment.newInstance(ConstantsData.MY_COMPLAINT_PROGRESSED);
        fragment4 = MyRepairsFragment.newInstance(ConstantsData.MY_COMPLAINT_END);
        list.add(fragment0);
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        list.add(fragment4);
        return list;
    }

    /**
     * 全部
     */
    private void all() {
        btn_my_repairs_all.setBackgroundResource(R.drawable.button_left_shap_checked);
        btn_my_repairs_all.setTextColor(Color.WHITE);
        btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_pending.setTextColor(Color.GRAY);
        btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressing.setTextColor(Color.GRAY);
        btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressed.setTextColor(Color.GRAY);
        btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_repairs_end.setTextColor(Color.GRAY);
    }

    /**
     * 待处理
     */
    private void pendding() {
        btn_my_repairs_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_repairs_all.setTextColor(Color.GRAY);
        btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap_checked);
        btn_my_repairs_pending.setTextColor(Color.WHITE);
        btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressing.setTextColor(Color.GRAY);
        btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressed.setTextColor(Color.GRAY);
        btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_repairs_end.setTextColor(Color.GRAY);
    }

    /**
     * 处理中
     */
    private void progressing() {
        btn_my_repairs_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_repairs_all.setTextColor(Color.GRAY);
        btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_pending.setTextColor(Color.GRAY);
        btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap_checked);
        btn_my_repairs_progressing.setTextColor(Color.WHITE);
        btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressed.setTextColor(Color.GRAY);
        btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_repairs_end.setTextColor(Color.GRAY);
    }

    /**
     * 已处理
     */
    private void progressed() {
        btn_my_repairs_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_repairs_all.setTextColor(Color.GRAY);
        btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_pending.setTextColor(Color.GRAY);
        btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressing.setTextColor(Color.GRAY);
        btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap_checked);
        btn_my_repairs_progressed.setTextColor(Color.WHITE);
        btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap);
        btn_my_repairs_end.setTextColor(Color.GRAY);
    }

    /**
     * 已结束
     */
    private void end() {
        btn_my_repairs_all.setBackgroundResource(R.drawable.button_left_shap);
        btn_my_repairs_all.setTextColor(Color.GRAY);
        btn_my_repairs_pending.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_pending.setTextColor(Color.GRAY);
        btn_my_repairs_progressing.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressing.setTextColor(Color.GRAY);
        btn_my_repairs_progressed.setBackgroundResource(R.drawable.button_center_shap);
        btn_my_repairs_progressed.setTextColor(Color.GRAY);
        btn_my_repairs_end.setBackgroundResource(R.drawable.button_right_shap_checked);
        btn_my_repairs_end.setTextColor(Color.WHITE);
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
        LLog.w("requestCode =" + requestCode);
        LLog.w("resultCode =" + resultCode);
        if (4001 == resultCode && requestCode == 4000) {

            switch (viewpager_repairs.getCurrentItem()) {
                case 0:
                    fragment0.pull_to_refresh_listView_repairs.setRefreshing(true);
                    break;
                case 1:
                    fragment1.pull_to_refresh_listView_repairs.setRefreshing(true);
                    break;
                case 2:
                    fragment2.pull_to_refresh_listView_repairs.setRefreshing(true);
                    break;
                case 3:
                    fragment3.pull_to_refresh_listView_repairs.setRefreshing(true);
                    break;
                case 4:
                    fragment4.pull_to_refresh_listView_repairs.setRefreshing(true);
                    break;
            }
        }
    }
}
