package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.fragment.SurveyListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyListActivity extends BaseActivity {
    @BindView(R.id.radio_group)//表头
            RadioGroup  radio_group;
    @BindView(R.id.radio_survey)//社区调查
            RadioButton radio_survey;
    @BindView(R.id.radio_vote)//投票
            RadioButton     radio_vote;

    @BindView(R.id.viewpager)//列表内容
            ViewPager viewpager;

    List<Fragment> fragments = new ArrayList<>();
    private SurveyListFragment fragment1;
    private SurveyListFragment fragment2;

    private String TITLE = StrConstant.COMMUNITY_SURVEY_TITLE;
    private JasonViewPagerAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_survey_list);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyListActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

        Log.d("demo", "Proprietor : " + AppHolder.getInstance().getProprietor().getProprietorId());

        fragment1 = SurveyListFragment.newInstance(StrConstant.COMMINITY_SURVEY, AppHolder.getInstance().getProprietor().getProprietorId());
        fragment2 = SurveyListFragment.newInstance(StrConstant.COMMINITY_VOTE,AppHolder.getInstance().getProprietor().getProprietorId());

        fragments.add(fragment1);
        fragments.add(fragment2);

        /**设置默认选择为全部*/
        radio_survey.setChecked(true);

        adapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        adapter.setOnReloadListener(new JasonViewPagerAdapter.OnReloadListener() {
            @Override
            public void onReload() {
                fragments = null;
                List<Fragment> list = new ArrayList<>();
                list.add(fragment1);

                list.add(fragment2);
                adapter.setPagerItems(list);
            }
        });

        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new FragmentOnPageChangeListener());
        //指定加载页数(可避免页面的重复加载)
//        viewpager.setOffscreenPageLimit(6);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_survey:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.radio_vote:
                        viewpager.setCurrentItem(1);
                        break;
                }
            }
        });
    }
    public class FragmentOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch(position){
                case 0:
                    radio_group.check(R.id.radio_survey);
                    break;
                case 1:
                    radio_group.check(R.id.radio_vote);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    public JasonViewPagerAdapter getAdapter(){
        return adapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LLog.w("requestCode =" + requestCode);
        LLog.w("resultCode =" + resultCode);
        if (RESULT_OK == resultCode && requestCode == 200) {

            switch (viewpager.getCurrentItem()) {
                case 0:
                    fragment1.mLRecyclerView.setRefreshing(true);
                    break;
                case 1:
                    fragment2.mLRecyclerView.setRefreshing(true);
                    break;
            }
            /*adapter.reLoad();*/

        }else {
            fragment2.mLRecyclerView.setRefreshing(true);
        }
    }
}
