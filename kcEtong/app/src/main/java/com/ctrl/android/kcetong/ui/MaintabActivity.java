package com.ctrl.android.kcetong.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.ApkInfo;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.base.BaseFragment;
import com.ctrl.android.kcetong.ui.fragment.BlankFragment;
import com.ctrl.android.kcetong.ui.fragment.HomeFragment;
import com.ctrl.android.kcetong.ui.fragment.MineFragment;
import com.ctrl.android.kcetong.ui.jpush.ExampleUtil;
import com.ctrl.android.kcetong.ui.news.PropertyServiceActivity;
import com.ctrl.android.kcetong.ui.view.CustomViewPager;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MaintabActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, BaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.tab_top_line)
    View tabTopLine;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    private HomeFragment homeFragment;
    private MineFragment mMineFragment;
    private int currentTabPosition;
    private int elogo = -1;
    private int bTabPosition;
    public static boolean isForeground = false;
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.ctrl.android.etong.MESSAGE_RECEIVED_ACTION";

    /*
     * 退出程序时两次点击的间隔时间
     */
    private long waitTime = 1000;
    private long touchTime = 0;
    private MainTabFragmentPagerAdapter pagerAdapter;
    private ApkInfo info;

    @Override
    protected void initView(Bundle savedInstanceState) {
        locationSetting();
        setContentView(R.layout.activity_main_tab);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setNoScroll(true);//设置viewpage不可滑动
        pagerAdapter = new MainTabFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(this);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
        //设置默认首页
        isTabSelected(tabLayout.getTabAt(0), true);
        viewPager.setCurrentItem(0);
        initJpush();
    }

    @Override
    protected void initData() {
        info = AppHolder.getInstance().getVersionInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (elogo == 2) {
            isTabSelected(tabLayout.getTabAt(bTabPosition), true);
            isTabSelected(tabLayout.getTabAt(2), false);
            elogo = -1;
        } else if (elogo == 1) {
            isTabSelected(tabLayout.getTabAt(bTabPosition), true);
            isTabSelected(tabLayout.getTabAt(1), false);
            elogo = -1;
        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJpush() {
        try {
            JPushInterface.init(getApplicationContext());

            if (AppHolder.getInstance().getMemberInfo() == null || S.isNull(AppHolder.getInstance().getMemberInfo().getMemberId())) {
                //
            } else {
                JPushInterface.setAlias(this, AppHolder.getInstance().getMemberInfo().getMemberId(), new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        //MessageUtils.showShortToast(MainActivity.this,"" + i);
                        Log.d("demo", "Alias: " + AppHolder.getInstance().getMemberInfo().getMemberId());
                    }
                });
            }
            registerMessageReceiver();
        } catch (Exception e) {
            LLog.e(e.toString());
        }
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }


    private void setCostomMsg(String msg) {
        MessageUtils.showShortToast(this, msg);
    }

    /**
     * 定位
     */
    private void locationSetting() {
        CustomApplication application = (CustomApplication) getApplication();
        if (application.getLocation() != null) {
        } else {
            if (Utils.isNetWorkConnected(this)) {
                locationIfGranted();
            }
        }
    }

    //tablayout的三个方法
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //选中的状态
        tabSelctedOrReselectd(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //此方法主要是没选中的状态
        isTabSelected(tab, false);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 1) {
            Intent intent = new Intent(this, PropertyServiceActivity.class);
            startActivity(intent);
        }

    }

    /**
     * @param page 0-3切换到指定fragment
     *             -1:切换到当前currentTabPosition指示fragment
     */
    public void onSwitchPagerFragment(int page) {
        viewPager.setCurrentItem(page);
        switch (page) {
            case 0:

            case 1:
                onTabSelected(tabLayout.getTabAt(page));
            case 2:
                onTabSelected(tabLayout.getTabAt(page));
                break;
            case -1:
                onTabSelected(tabLayout.getTabAt(currentTabPosition));
                break;
            default:
                break;
        }
    }

    /**
     * @param intent 第二次点击底部图标时走的方法
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LLog.w("111111111111111");
        int selectFragmentIndex;
        if (intent == null || intent.getExtras() == null) {
            selectFragmentIndex = 0;
        } else {
            selectFragmentIndex = intent.getExtras().getInt("selectFragmentIndex", -1);
        }

        switch (selectFragmentIndex) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                tabSelctedOrReselectd(tabLayout.getTabAt(selectFragmentIndex));
                break;
            case 1:
                intent = new Intent(this, PropertyServiceActivity.class);
                startActivity(intent);
                break;
            case 2:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                }
                tabSelctedOrReselectd(tabLayout.getTabAt(selectFragmentIndex));
                break;
            case 3:

                break;
            default:
                break;
        }
    }

    /**
     * tab切换事件响应
     *
     * @param tab
     * @param selected
     */
    private void isTabSelected(TabLayout.Tab tab, boolean selected) {
        int tabPosition = tab.getPosition();
        View view = tab.getCustomView();
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setTextColor(ContextCompat.getColor(MaintabActivity.this, selected ? R.color.text_green : R.color.color_4E4C5A));
        switch (tabPosition) {
            case 0:
                imageView.setImageResource(selected ? R.drawable.menu_main_page_icon_press : R.drawable.menu_main_page_icon_press);
                break;
            case 1:
                imageView.setImageResource(selected ? R.drawable.zhidong_press : R.drawable.zhidong_index);
                break;
            case 2:
//                imageView.setImageResource(selected ? R.drawable.magagent_press : R.drawable.magagend_index);
                imageView.setImageResource(selected ? R.drawable.my_icon_press : R.drawable.my_icon_press);
                break;
            default:
                break;
        }
    }

    //选中之后的设置，根据点击的不同切换不同的fragment
    private void tabSelctedOrReselectd(TabLayout.Tab tab) {
        elogo = tab.getPosition();
        int tabPosition = tab.getPosition();
        LLog.w("tabPosition =" + tabPosition);
        bTabPosition = currentTabPosition;
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (tabPosition == i) {
                isTabSelected(tabLayout.getTabAt(i), true);
            } else {
                isTabSelected(tabLayout.getTabAt(i), false);
            }
        }
        if (tabPosition == 1) {
            isTabSelected(tabLayout.getTabAt(1), true);
            Intent intent = new Intent(this, PropertyServiceActivity.class);
            startActivity(intent);
        } else {
            currentTabPosition = tabPosition;
            viewPager.setCurrentItem(currentTabPosition);
        }
    }

    public class MainTabFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[]{"首页", "物业服务", "我的"};
        private int tabImages[] = new int[]{R.drawable.menu_main_page_icon_press, R.drawable.zhidong_index, R.drawable.my_icon_press,};
        private Context context;
        private FragmentManager fm;

        public MainTabFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.fm = fm;
            this.context = context;
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.main_custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) v.findViewById(R.id.imageView);

            img.setImageResource(tabImages[position]);
            return v;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                return homeFragment;
            } else if (position == 2) {
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                }
                return mMineFragment;

            }
            return BlankFragment.newInstance("", "");
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
        /*if (conn != null) {
            this.getApplicationContext().unbindService(conn);
        }*/
    }

}
