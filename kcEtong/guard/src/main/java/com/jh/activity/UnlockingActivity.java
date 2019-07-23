package com.jh.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.R;
import com.jh.adapter.DevicesAdapter;
import com.jh.view.MaterialDialog;
import com.jhsdk.bean.api.JHDevice;
import com.jhsdk.bean.sip.JHMessage;
import com.jhsdk.bean.sip.JHMessageState;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;

import java.util.ArrayList;
import java.util.List;

public class UnlockingActivity extends AppCompatActivity implements JHSDKListener.RegistrStateChangedListener,
        JHSDKListener.MessageReceivedListener, JHSDKListener.MessageStateListener {
    private DevicesAdapter devicesAdapter;
    private List<JHDevice> deviceList;
    private TextView tv_device_door;
    private RelativeLayout layout_unit_door;

    private ImageView iv_device_back;
    private RecyclerView recyclerView;
    private List<JHDevice> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlocking);
        JHSDKCore.addListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.lViewDevices);
        iv_device_back = (ImageView) findViewById(R.id.iv_device_back);
        iv_device_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnlockingActivity.this.finish();
            }
        });
        tv_device_door = (TextView) findViewById(R.id.tv_device_door);
        layout_unit_door = (RelativeLayout) findViewById(R.id.layout_unit_door);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        deviceList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        JHSDKCore.addListener(this);
        deviceList.clear();
        final String i = getIntent().getStringExtra("do");
        // TODO：去掉了一个判断，之前有这个判断
        // if (!"".equals(SharedPreUtils.getStringValue(this, JHConstant.KEY_USER_PASSWORD, ""))) {
        list.addAll(JHSDKCore.getUserService().getDevices());
        Log.e("demo", JHSDKCore.getUserService().getDevices().toString());
        if (list.size() > 0) {

            deviceList = list;
            devicesAdapter = new DevicesAdapter(this, deviceList);
            recyclerView.setAdapter(devicesAdapter);
            devicesAdapter.setOnItemClickLitener(new DevicesAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, String data) {
                    if ("unlock".equals(i)) {
                        JHSDKCore.getMessageService().unlock(data);
//                            JHSDKCore.getMessageService().sendMessage(data, "");
                    } else if ("monitor".equals(i)) {
                        JHSDKCore.getCallService().makeCall(data);
                    }

                    Log.d("demo", data);
                }
            });
            devicesAdapter.notifyDataSetChanged();
        } else {
            layout_unit_door.setVisibility(View.GONE);
        }
        // }

//        deviceList.addAll(JHSDKCore.getUserService().getDevices());
    }

    private void showFunctionMenu(final String account) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        String[] operateTypes = getResources().getStringArray(R.array.array_devices_operation_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            arrayAdapter.addAll(operateTypes);
        } else {
            for (String operateType : operateTypes) {
                arrayAdapter.add(operateType);
            }
        }
        ListView listView = new ListView(this);
        listView.setDividerHeight(1);
        listView.setAdapter(arrayAdapter);
        final MaterialDialog alert = new MaterialDialog(this);
        alert.setContentView(listView);
        alert.setCanceledOnTouchOutside(true);
        alert.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alert.dismiss();
            }
        });
    }

    @Override
    public void onRegistrStateChanged(int i, String s) {
        JHSDKCore.addListener(this);
        if (devicesAdapter != null) {
            devicesAdapter.notifyDataSetChanged();
        }
    }

    private final int TIPS_KEY = 1;

    @Override
    public void onMessageReceived(final JHMessage jhMessage) {
        JHSDKCore.addListener(this);
        Log.d("demo", jhMessage.getContent());
    }

    @Override
    public void onMessageState(JHMessageState jhMessageState) {
        Log.e("demo", "ssss---->   " + jhMessageState.getCode() + "     " + jhMessageState.getReason());
        if (jhMessageState.getCode() >= 0) {
            Toast.makeText(this, "开锁成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "开锁失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
