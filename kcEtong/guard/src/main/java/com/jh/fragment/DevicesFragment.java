package com.jh.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;
import com.jhsdk.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class DevicesFragment extends Fragment implements JHSDKListener.RegistrStateChangedListener ,JHSDKListener.MessageReceivedListener,JHSDKListener.MessageStateListener{
    private DevicesAdapter devicesAdapter;
    private List<JHDevice> deviceList;
    private TextView tv_device_door;
    private RelativeLayout layout_unit_door;

    private ImageView iv_device_back;
    private RecyclerView recyclerView;
    private List<JHDevice> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View         view     = inflater.inflate(R.layout.fragment_devices, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.lViewDevices);
        iv_device_back = (ImageView) view.findViewById(R.id.iv_device_back);
        iv_device_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicesFragment.this.getActivity().finish();
            }
        });
        tv_device_door = (TextView) view.findViewById(R.id.tv_device_door);
        layout_unit_door = (RelativeLayout) view.findViewById(R.id.layout_unit_door);

        recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
        deviceList = new ArrayList<>();
        JHSDKCore.addListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JHSDKCore.addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        JHSDKCore.addListener(this);
        deviceList.clear();
        if(!"".equals(SharedPreUtils.getStringValue(this.getActivity(), JHConstant.KEY_USER_PASSWORD, ""))){
//            Log.d("-----password----", SharedPreUtils.getStringValue(this.getActivity(), JHConstant.KEY_USER_PASSWORD, "")+"");
            list.addAll(JHSDKCore.getUserService().getDevices());
            if(list.size() > 0){
                /*layout_unit_door.setVisibility(View.VISIBLE);
                tv_device_door.setText(list.get(0).getDisplayName());
                Log.d("-----单元门----",list.get(0).getAccounts()+"");
                final String account = list.get(0).getAccounts();
                layout_unit_door.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFunctionMenu(account);
                        Log.d("------list----",JHSDKCore.getUserService().getDevices()+"");
//                        Log.d("-----单元门----",list.get(0).getAccounts()+"");
                        Log.d("-----account----",account);
                    }
                });
                if(list.size()>0){
                    deviceList = list;
                    deviceList.remove(0);
                }*/

                deviceList = list;
                devicesAdapter = new DevicesAdapter(getActivity(), deviceList);
                recyclerView.setAdapter(devicesAdapter);
                devicesAdapter.setOnItemClickLitener(new DevicesAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, String data) {
                        showFunctionMenu(data);
                        Log.d("------围墙机----",data);
                    }
                });
                devicesAdapter.notifyDataSetChanged();
            }else {
                layout_unit_door.setVisibility(View.GONE);
            }
        }

//        deviceList.addAll(JHSDKCore.getUserService().getDevices());

    }

    private void showFunctionMenu(final String account) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        String[] operateTypes = getResources().getStringArray(R.array.array_devices_operation_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            arrayAdapter.addAll(operateTypes);
        } else {
            for (String operateType : operateTypes) {
                arrayAdapter.add(operateType);
            }
        }
        ListView listView = new ListView(getActivity());
        listView.setDividerHeight(1);
        listView.setAdapter(arrayAdapter);
        final MaterialDialog alert = new MaterialDialog(getActivity());
        alert.setContentView(listView);
        alert.setCanceledOnTouchOutside(true);
        alert.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    JHSDKCore.getMessageService().unlock(account);
                } else if (i == 1) {
                    JHSDKCore.getCallService().makeCall(account);
                }
                alert.dismiss();
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        View view = this.getView();
        if (view != null) {
            view.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onRegistrStateChanged(int i, String s) {
        JHSDKCore.addListener(this);
        if(devicesAdapter != null){
            devicesAdapter.notifyDataSetChanged();
        }
    }
    private final int TIPS_KEY = 1;

    @Override
    public void onMessageReceived(final JHMessage jhMessage) {
        JHSDKCore.addListener(this);
        Log.d("我收到消息了----", jhMessage.getContent());
        Toast.makeText(getActivity(), "",Toast.LENGTH_SHORT).show();

//        handler.obtainMessage(TIPS_KEY, "xiaoxi1111").sendToTarget();
        /*try {
            BaseJson baseJson = new BaseJson(jhMessage.getContent());
            if ("call_sync".equals(baseJson.getType())) {
                if (callInfo.getAccount().equals(jhMessage.getAccount())) {
                    JHSDKCore.getCallService().hangup();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JHSDKCore.getCallService().makeCall(jhMessage.getAccount());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                } else {
                    JHSDKCore.getMessageService().sendMessage(jhMessage.getAccount(),
                            JSON.toJSONString(new BaseJson("error", -1, "call_sync")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    @Override
    public void onMessageState(JHMessageState jhMessageState) {
        Log.e("aaaaaaaa", "ssss---->   "+jhMessageState.getCode()+"     "+jhMessageState.getReason());
    }
}
