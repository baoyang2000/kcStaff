package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.adapter.ViewPagerAdapter2;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity {

//    @BindView(R.id.guide_viewpager)
//    ViewPager viewPager;
//    @BindView(R.id.page0)
//    ImageView pointImage0;
//    @BindView(R.id.page1)
//    ImageView pointImage1;
//    @BindView(R.id.page2)
//    ImageView pointImage2;
//    @BindView(R.id.page3)
//    ImageView pointImage3;
//    @BindView(R.id.page4)
//    ImageView pointImage4;
    // 定义底部小点图片
    private ImageView pointImage0, pointImage1, pointImage2, pointImage3, pointImage4;
    private boolean isFirstLog = true;

    // 定义ViewPager适配器
    private ViewPagerAdapter2 vpAdapter;

    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    //定义各个界面View对象
    private View view1, view2, view3, view4;

    // 当前的位置索引值
    private int currIndex = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        isFirstLog = Utils.isFirst(this);
        if (!isFirstLog) {//不是第一次登录
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        isFirstLog = false;
        Utils.setFirst(isFirstLog, this);


        setContentView(R.layout.activity_guide);

        //实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.guide_view01, null);
        view2 = mLi.inflate(R.layout.guide_view02, null);
        view3 = mLi.inflate(R.layout.guide_view03, null);
        view4 = mLi.inflate(R.layout.guide_view04, null);

        // 实例化ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewpager);

        // 实例化底部小点图片对象
        pointImage0 = (ImageView) findViewById(R.id.page0);
        pointImage1 = (ImageView) findViewById(R.id.page1);
        pointImage2 = (ImageView) findViewById(R.id.page2);
        pointImage3 = (ImageView) findViewById(R.id.page3);
        // 实例化ArrayList对象
        views = new ArrayList<>();
        //将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter2(views);

        Button startBt = (Button) view4.findViewById(R.id.startBtn);

        // 设置监听
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        // 设置适配器数据
        viewPager.setAdapter(vpAdapter);

        // 给开始按钮设置监听
        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startbutton();
            }
        });
    }

    @Override
    protected void initData() {

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    pointImage0.setImageDrawable(getResources().getDrawable(R.drawable.select_guide));
                    pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.normal_guide));
                    break;
                case 1:
                    pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.select_guide));
                    pointImage0.setImageDrawable(getResources().getDrawable(R.drawable.normal_guide));
                    pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.normal_guide));
                    break;
                case 2:
                    pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.select_guide));
                    pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.normal_guide));
                    pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.normal_guide));
                    break;
                case 3:
                    pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.select_guide));
//				pointImage4.setImageDrawable(getResources().getDrawable(R.drawable.pointnormal));
                    pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.normal_guide));
                    break;
//			case 4:
//				pointImage4.setImageDrawable(getResources().getDrawable(R.drawable.pointselect));
//				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.pointnormal));
//				//				pointImage5.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
//				break;
                //			case 5:
                //				pointImage5.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
                //				pointImage4.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
                //				break;
            }
            currIndex = position;
            // animation.setFillAfter(true);// True:图片停在动画结束位置
            // animation.setDuration(300);
            // mPageImg.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /**
     * 相应按钮点击事件
     */
    private void startbutton() {
        Intent intent = new Intent();
        intent.setClass(GuideActivity.this, StartActivity.class);
        startActivity(intent);
        this.finish();
    }
}
