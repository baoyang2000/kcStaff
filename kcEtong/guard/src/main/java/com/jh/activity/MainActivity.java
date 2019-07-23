package com.jh.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jh.R;
import com.jh.adapter.MainPageAdapter;
import com.jh.bean.MainMenu;
import com.jh.fragment.DevicesFragment;
import com.jh.fragment.DialPlateFragment;
import com.jh.fragment.PersonalCenterFragment;
import com.jh.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jh.R.id.lLoyoutContainer;

/**
 * Created by Administrator on 2016/12/20.
 */

public class MainActivity extends AppCompatActivity{
    private RelativeLayout lloyoutContainer;
    private MainPageAdapter mainPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lloyoutContainer = (RelativeLayout) findViewById(lLoyoutContainer);
        TabLayout tLayoutMainMenu = (TabLayout) findViewById(R.id.tLayoutMainMenu);

        FragmentManager fm = getSupportFragmentManager();
        List<MainMenu> mainMenus = new ArrayList<>();
        mainMenus.add(new MainMenu(new DevicesFragment(), R.drawable.green_mobile_icon, "设备列表"));
        mainMenus.add(new MainMenu(new DialPlateFragment(), R.drawable.easy_shop_around_service_phone_icon, "拨号"));
        mainMenus.add(new MainMenu(new PersonalCenterFragment(), R.drawable.menu_my_icon_press, "个人中心"));
        mainPageAdapter = new MainPageAdapter(fm, mainMenus);
        for (int i = 0; i < mainMenus.size(); i++) {
            TextView textView = (TextView) ViewUtils.layoutToView(MainActivity.this, R.layout.button_main_menu);
            textView.setText(mainMenus.get(i).getText());
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getResources().getDrawable(mainMenus.get(i).getDrawableTop(), null);
            } else {
                drawable = getResources().getDrawable(mainMenus.get(i).getDrawableTop());
            }
            if (drawable != null) {
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, drawable, null, null);
            }
            TabLayout.Tab tab = tLayoutMainMenu.newTab();
            tab.setCustomView(textView);
            if (i == 0) {
                tLayoutMainMenu.addTab(tab, true);
                Fragment fragment = (Fragment) mainPageAdapter.instantiateItem(lloyoutContainer, i);
                mainPageAdapter.setPrimaryItem(lloyoutContainer, i, fragment);
                mainPageAdapter.finishUpdate(lloyoutContainer);
            } else {
                tLayoutMainMenu.addTab(tab, false);
            }
        }
        tLayoutMainMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment fragment = (Fragment) mainPageAdapter.instantiateItem(lloyoutContainer, position);
                mainPageAdapter.setPrimaryItem(lloyoutContainer, position, fragment);
                mainPageAdapter.finishUpdate(lloyoutContainer);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
