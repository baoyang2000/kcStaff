package com.jh.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.R;
import com.jh.bean.City;
import com.jh.utils.StringUtils;
import com.jh.view.DropDownListEditTextView;
import com.jh.view.MaterialDialog;
import com.jhsdk.bean.api.JHCommInfo;
import com.jhsdk.bean.api.JHOwnerInfo;
import com.jhsdk.bean.api.JHUnitsInfo;
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHCallback;
import com.jhsdk.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.jh.R.id.rLayoutCity;

/**
 * Created by Administrator on 2016/12/23.
 */

public class RegisterOwnerActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tViewCommCode, tViewCommName, tViewCity,
            tViewRoomId, tViewName;
    private LinearLayout Llayout_authorize, Lloyout_owner;
    private MaterialDialog     alert;
    private FrameLayout        commit_btn;
    private InputMethodManager imm;
    public static final String PHONE_KEY = "PHONE_KEY";
    private String phone;
    private DropDownListEditTextView dropDownListBuilding, dropDownListUnit;
    private List<JHUnitsInfo> unitsInfoList;
    private ImageView iv_register_owner_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        phone = getIntent().getStringExtra(PHONE_KEY);
        if (StringUtils.isEmpty(phone)) {
            phone = SharedPreUtils.getStringValue(RegisterOwnerActivity.this, JHConstant.KEY_USER_ACCOUNT, "");
        }
        unitsInfoList = new ArrayList<>();
        initWidget();
    }

    public void initWidget() {
        commit_btn = (FrameLayout) findViewById(R.id.commit_btn);
        commit_btn.setOnClickListener(this);

        Llayout_authorize = (LinearLayout) findViewById(R.id.Llayout_authorize);
        Lloyout_owner = (LinearLayout) findViewById(R.id.Lloyout_owner);

        iv_register_owner_back = (ImageView) findViewById(R.id.iv_register_owner_back);
        iv_register_owner_back.setOnClickListener(this);
        RelativeLayout rLayoutCity = (RelativeLayout) findViewById(R.id.rLayoutCity);
        rLayoutCity.setOnClickListener(this);
        RelativeLayout rlayoutCommCode = (RelativeLayout) findViewById(R.id.rlayoutCommCode);
        rlayoutCommCode.setOnClickListener(this);
        RelativeLayout rLayoutRoomId = (RelativeLayout) findViewById(R.id.rLayoutRoomId);
        rLayoutRoomId.setOnClickListener(this);
        RelativeLayout rLayoutName = (RelativeLayout) findViewById(R.id.rLayoutName);
        rLayoutName.setOnClickListener(this);

        tViewName = (TextView) findViewById(R.id.tViewName);
        tViewCity = (TextView) findViewById(R.id.tViewCity);
        tViewCommCode = (TextView) findViewById(R.id.tViewCommCode);
        tViewCommName = (TextView) findViewById(R.id.tViewCommName);
        tViewRoomId = (TextView) findViewById(R.id.tViewRoomId);

        dropDownListBuilding = (DropDownListEditTextView) findViewById(R.id.DropDownListBuilding);
        dropDownListBuilding.setInputType(InputType.TYPE_CLASS_NUMBER);
        dropDownListBuilding.setOnItemClickListener(new DropDownListEditTextView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String building = dropDownListBuilding.getContent();
                List<String> unitList = new ArrayList<>();
                for (JHUnitsInfo unitsInfo : unitsInfoList) {
                    if (unitsInfo.getAreaName().equals(building)) {
                        unitList.add(unitsInfo.getBuildingName());
                    }
                }
                Collections.sort(unitList, new Comparator<String>() {
                    @Override
                    public int compare(String b1, String b2) {
                        if (Integer.valueOf(b1) > Integer.valueOf(b2)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }

                });
                dropDownListUnit.setItemsData(unitList);
            }
        });

        dropDownListUnit = (DropDownListEditTextView) findViewById(R.id.DropDownListUnit);
        dropDownListUnit.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
//        switch (id) {
//            case R.id.rLayoutCity:
//                Intent i = new Intent(RegisterOwnerActivity.this, SelectCtiyActivity.class);
//                startActivityForResult(i, 0);
//                break;
//            case R.id.rlayoutCommCode:
//                getAreaData();
//                break;
//            case R.id.rLayoutRoomId:
//                dialogEditShow(R.id.rLayoutRoomId, "房号");
//                break;
//            case R.id.commit_btn:
//                applySubmit();
//                break;
//            case R.id.rLayoutName:
//                dialogEditShow(R.id.rLayoutName, "真实姓名");
//                break;
//            case R.id.setting_authorize_name:
//                dialogEditShow(R.id.setting_authorize_name, "真实姓名");
//                break;
//            case R.id.setting_authorize_phone:
//                dialogEditShow(R.id.setting_authorize_phone, "被授权人电话号码");
//                break;
//        }
        if(id == rLayoutCity){
            Intent i = new Intent(RegisterOwnerActivity.this, SelectCtiyActivity.class);
            startActivityForResult(i, 0);
        }else if(id == R.id.rlayoutCommCode){
            getAreaData();
        }else if(id == R.id.rLayoutRoomId){
            dialogEditShow(R.id.rLayoutRoomId, "房号");
        }else if(id == R.id.commit_btn){
            applySubmit();
        }else if(id == R.id.rLayoutName){
            dialogEditShow(R.id.rLayoutName, "真实姓名");
        }else if(id == R.id.setting_authorize_name){
            dialogEditShow(R.id.setting_authorize_name, "真实姓名");
        }else if(id == R.id.setting_authorize_phone){
            dialogEditShow(R.id.setting_authorize_phone, "被授权人电话号码");
        }else if(id == R.id.iv_register_owner_back){
            RegisterOwnerActivity.this.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            City city = (City) data.getSerializableExtra("city");
            tViewCity.setText(city.getCity());
        }
    }

    private void applySubmit() {
        String commCode, cityName, buildingId, unitId, roomId, name;
        commCode = tViewCommCode.getText().toString();
        cityName = tViewCity.getText().toString();
        buildingId = dropDownListBuilding.getContent();
        unitId = dropDownListUnit.getContent();
        roomId = tViewRoomId.getText().toString();
        name = tViewName.getText().toString();

        if (StringUtils.isEmpty(name)) {
            Toast.makeText(RegisterOwnerActivity.this, "必须填写真实姓名！", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.isEmpty(commCode)) {
            Toast.makeText(RegisterOwnerActivity.this, "小区编号输入错误！", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.isEmpty(cityName)) {
            Toast.makeText(RegisterOwnerActivity.this, "城市选择未选择！", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.isEmpty(buildingId)) {
            Toast.makeText(RegisterOwnerActivity.this, "楼栋输入错误！", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.isEmpty(unitId)) {
            Toast.makeText(RegisterOwnerActivity.this, "单元输入错误！", Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.isEmpty(roomId)) {
            Toast.makeText(RegisterOwnerActivity.this, "绑定房号错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (buildingId.length() == 1) {
            buildingId = "0" + buildingId;
        } else if (buildingId.length() != 2) {
            Toast.makeText(RegisterOwnerActivity.this, "楼栋输入错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (unitId.length() == 1) {
            unitId = "0" + unitId;
        } else if (unitId.length() != 2) {
            Toast.makeText(RegisterOwnerActivity.this, "单元输入错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        showDialog("正在提交申请...");
        JHOwnerInfo jhOwnerInfo = new JHOwnerInfo();
        jhOwnerInfo.setOwnerName(name);
        jhOwnerInfo.setCommCode(commCode);
        jhOwnerInfo.setOwnerMob(phone);
        jhOwnerInfo.setAreaCode(buildingId);
        jhOwnerInfo.setBuildingCode(unitId);
        jhOwnerInfo.setRoomCode(roomId);
        JHSDKCore.getUserService().registeredOwner(jhOwnerInfo, new JHCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(RegisterOwnerActivity.this, "提交申请成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                tips("注册提示", "申请失败: " + strMsg, false);
            }

            @Override
            public void onFinish() {
                clossDialog();
            }
        });
    }

    private void dialogEditShow(final int id, String title) {
        final EditText editText = new EditText(RegisterOwnerActivity.this);
        if (id == R.id.rlayoutCommCode || id == R.id.setting_building_id || id == R.id.setting_unit_id
                || id == R.id.rLayoutRoomId || id == R.id.setting_authorize_phone) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        new MaterialDialog(RegisterOwnerActivity.this)
                .setTitle(title)
                .setContentView(editText)
                .setPositiveButton("确定", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s;
                        s = editText.getText().toString();
                        int sLength = s.length();
                        if (id == R.id.rLayoutRoomId) {
                            if (sLength == 3) {
                                s = "0" + s;
                                tViewRoomId.setText(s);
                            } else if (sLength == 4) {
                                tViewRoomId.setText(s);
                            } else {
                                Toast.makeText(RegisterOwnerActivity.this, "房号输入错误！"
                                        , Toast.LENGTH_SHORT).show();
                            }
                        } else if (id == R.id.rLayoutName) {
                            tViewName.setText(s);
                        }

                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                })
                .setNegativeButton("取消", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    /**
     * 获取小区数据
     */
    private void getAreaData() {
        String city = tViewCity.getText().toString();
        showDialog("正在获取小区列表...");
        JHSDKCore.getUserService().getCommByCity(city, new JHCallback<List<JHCommInfo>>() {
            @Override
            public void onSuccess(List<JHCommInfo> jhCommInfos) {
                showFunctionMenu(jhCommInfos);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                Toast toast = Toast.makeText(RegisterOwnerActivity.this, strMsg
                        , Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void onFinish() {
                clossDialog();
            }
        });
    }

    /**
     * 小区选择弹出框
     */
    private void showFunctionMenu(final List<JHCommInfo> commInfos) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterOwnerActivity.this, android.R.layout.simple_list_item_1);
        String[] operateTypes = new String[commInfos.size()];
        for (int i = 0; i < commInfos.size(); i++){
            String commInfo = "编号："+commInfos.get(i).getCommCode()+"  ["+commInfos.get(i).getCommName()+"]";
            operateTypes[i] = commInfo;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            arrayAdapter.addAll(operateTypes);
        } else {
            for (String operateType : operateTypes) {
                arrayAdapter.add(operateType);
            }
        }
        ListView listView = new ListView(RegisterOwnerActivity.this);
        listView.setDividerHeight(1);
        listView.setAdapter(arrayAdapter);
        final MaterialDialog alert = new MaterialDialog(RegisterOwnerActivity.this);
        alert.setContentView(listView);
        alert.setCanceledOnTouchOutside(true);
        alert.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String areaNum = commInfos.get(i).getCommCode();
                String areaName = commInfos.get(i).getCommName();
                tViewCommCode.setText(areaNum);
                tViewCommName.setText(areaName);

                showDialog("正在获取小区数据...");
                JHSDKCore.getUserService().getUnitsInfo(areaNum, new JHCallback<List<JHUnitsInfo>>() {
                    @Override
                    public void onSuccess(List<JHUnitsInfo> jhUnitsInfos) {
                        unitsInfoList.addAll(jhUnitsInfos);
                        List<String> buildingList = new ArrayList<>();
                        outterLoop:
                        for (JHUnitsInfo unitsInfo : unitsInfoList) {
                            String building = unitsInfo.getAreaName();
                            for (String s : buildingList) {
                                if (s.equals(building)) {
                                    continue outterLoop;
                                }
                            }
                            buildingList.add(building);
                        }
                        Collections.sort(buildingList, new Comparator<String>() {
                            @Override
                            public int compare(String b1, String b2) {
                                if (Integer.valueOf(b1) > Integer.valueOf(b2)) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }

                        });
                        dropDownListBuilding.setItemsData(buildingList);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {

                    }

                    @Override
                    public void onFinish() {
                        clossDialog();
                    }
                });
                alert.dismiss();
            }
        });
    }

    private void showDialog(String text) {
        clossDialog();
        // 获取Dialog布局
        View view = LayoutInflater.from(RegisterOwnerActivity.this).inflate(R.layout.dialog_loading_guard, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_loading);
        textView.setText(text);
        alert = new MaterialDialog(RegisterOwnerActivity.this)
                .setContentView(view);
        alert.setNegativeButton("取消", new MaterialDialog.DialogOnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        alert.show();
    }

    private void clossDialog() {
        if (alert != null && alert.isShow()) {
            alert.dismiss();
        }
    }

    private void tips(String title, String text, final boolean isOk) {
        new MaterialDialog(RegisterOwnerActivity.this)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("确定", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOk) {
                            finish();
                        }
                    }
                })
                .setNegativeButton("取消", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
}
