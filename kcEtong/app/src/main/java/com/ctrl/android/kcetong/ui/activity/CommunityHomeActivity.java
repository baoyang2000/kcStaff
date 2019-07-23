package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.adapter.ViewPagerAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
import com.ctrl.android.kcetong.ui.fragment.ActListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityHomeActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener{

    @BindView(R.id.toolbar_right_btn)//右侧toolbar标题
    TextView     toolbar_right_btn;
    @BindView(R.id.radio_all_act)//全部活动
    RadioButton  radio_all_act;
    @BindView(R.id.radio_I_take_in)//我参与的
    RadioButton  radio_I_take_in;
    @BindView(R.id.radio_I_start_up)//我发起的
    RadioButton  radio_I_start_up;
    @BindView(R.id.radio_group)//表头
    RadioGroup   radio_group;

    @BindView(R.id.viewpager)
    ViewPager    viewpager;

    SparseArray<Fragment> fragments = new SparseArray<Fragment>();
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_community_home);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.COMMUNITY_ACTIVITY_TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityHomeActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("发起活动");

    }

    @Override
    protected void initData() {
        fragments.put(0, ActListFragment.newInstance(ConstantsData.ACT_ALL));
        fragments.put(1, ActListFragment.newInstance(ConstantsData.ACT_I_TAKE_IN));
        fragments.put(2, ActListFragment.newInstance(ConstantsData.ACT_I_START_UP));

        /**设置默认选择为全部*/
        radio_all_act.setChecked(true);

        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewpager.setOnPageChangeListener(new FragmentOnPageChangeListener());
        //指定加载页数(可避免页面的重复加载)
        viewpager.setOffscreenPageLimit(6);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_all_act:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.radio_I_take_in:
                        if("0".equals(holder.getMemberInfo().getSupers())){
                            viewpager.setCurrentItem(1);
                        }else if("1".equals(holder.getMemberInfo().getSupers())
                                 || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                            Utils.showShortToast(CommunityHomeActivity.this, getString(R.string.manager_cannot));
                        }

                        break;
                    case R.id.radio_I_start_up:
                        if("0".equals(holder.getMemberInfo().getSupers())){
                            viewpager.setCurrentItem(2);
                        }else if("1".equals(holder.getMemberInfo().getSupers())
                                 || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                            Utils.showShortToast(CommunityHomeActivity.this, getString(R.string.manager_cannot));
                        }

                        break;
                }
            }
        });
    }

    @Override
    public void onSwitchPagerFragment(int page) {
    }
    public class FragmentOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch(position){
                case 0:
                    radio_group.check(R.id.radio_all_act);
                    break;
                case 1:
                    if("0".equals(holder.getMemberInfo().getSupers())){
                        radio_group.check(R.id.radio_I_take_in);
                    }else if("1".equals(holder.getMemberInfo().getSupers())
                             || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                        Utils.showShortToast(CommunityHomeActivity.this, getString(R.string.manager_cannot));
                    }

                    break;
                case 2:
                    if("0".equals(holder.getMemberInfo().getSupers())){
                        radio_group.check(R.id.radio_I_start_up);
                    }else if("1".equals(holder.getMemberInfo().getSupers())
                             || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                        Utils.showShortToast(CommunityHomeActivity.this, getString(R.string.manager_cannot));
                    }
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @OnClick({R.id.toolbar_right_btn, R.id.radio_all_act, R.id.radio_I_take_in, R.id.radio_I_start_up, R.id.radio_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_btn:
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Intent intent = new Intent(CommunityHomeActivity.this, LaunchActivity.class);
                    startActivity(intent);
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(CommunityHomeActivity.this, getString(R.string.manager_cannot));
                }
                break;
            case R.id.radio_all_act:
                break;
            case R.id.radio_I_take_in:
                break;
            case R.id.radio_I_start_up:
                break;
            case R.id.radio_group:
                break;
        }
    }
}
