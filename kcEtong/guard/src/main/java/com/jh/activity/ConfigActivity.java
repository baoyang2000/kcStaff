package com.jh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jh.R;
import com.jh.view.SlideSwitch;
import com.jhsdk.bean.api.JHUserSettingInfo;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHCallback;

/**
 * Created by Administrator on 2016/12/23.
 */

public class ConfigActivity extends AppCompatActivity{
    private SlideSwitch sSwitchDonotDisturb, sSwitchOnlyWifiVideo;
    private ImageView iv_config_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        sSwitchDonotDisturb = (SlideSwitch) findViewById(R.id.sSwitchDonotDisturb);
        sSwitchDonotDisturb.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                /*JHSDKCore.getSettingService().setDonotDisturb(true, new JHCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        sSwitchDonotDisturb.setDisplayState(false);
                        Toast.makeText(ConfigActivity.this,
                                "开启失败: " + strMsg,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                });*/
            }

            @Override
            public void close() {
                /*JHSDKCore.getSettingService().setDonotDisturb(false, new JHCallback<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        sSwitchDonotDisturb.setDisplayState(true);
                        Toast.makeText(ConfigActivity.this,
                                "关闭失败: " + strMsg,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                });*/
            }
        });

        sSwitchOnlyWifiVideo = (SlideSwitch) findViewById(R.id.sSwitchOnlyWifiVideo);
        sSwitchOnlyWifiVideo.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                JHSDKCore.getSettingService().setOnlyWifiVideo(true);
            }

            @Override
            public void close() {
                JHSDKCore.getSettingService().setOnlyWifiVideo(false);
            }
        });
        iv_config_back = (ImageView) findViewById(R.id.iv_config_back);
        iv_config_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigActivity.this.finish();
            }
        });
        initData();
    }

    private void initData(){
        JHSDKCore.getSettingService().getSettingInfo(new JHCallback<JHUserSettingInfo>() {
            @Override
            public void onSuccess(JHUserSettingInfo jhUserSettingInfo) {
                sSwitchDonotDisturb.setDisplayState(jhUserSettingInfo.isDonotDisturb());
                sSwitchOnlyWifiVideo.setDisplayState(jhUserSettingInfo.isOnlyWifiVideo());
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                sSwitchDonotDisturb.setDisplayState(false);
                Toast.makeText(ConfigActivity.this,
                        "获取设置信息失败，请检查网络！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
