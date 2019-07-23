package com.jh.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.R;
import com.jh.constant.APPConstant;
import com.jh.view.MaterialDialog;
import com.jhsdk.bean.api.JHRole;
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHCallback;
import com.jhsdk.utils.AccountUtils;
import com.jhsdk.utils.SharedPreUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tViewAccount, tViewCommName, tViewBuildingInfo, tViewTipsInfo, tViewAccountRole;
    private MaterialDialog alert;
    private RelativeLayout rlayoutTips, rLoyoutAuthorize;
    private LinearLayout lLayoutInfo;
    private final static int REQUEST_CODE = 1;
    private ImageView iv_user_info_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        lLayoutInfo = (LinearLayout) findViewById(R.id.lLayoutInfo);
        rlayoutTips = (RelativeLayout) findViewById(R.id.rlayoutTips);

        RelativeLayout rLayoutSynCommInfo = (RelativeLayout) findViewById(R.id.rLayoutSynCommInfo);
        rLayoutSynCommInfo.setOnClickListener(this);
        RelativeLayout rlayoutAccountRole = (RelativeLayout) findViewById(R.id.rlayoutAccountRole);
        rlayoutAccountRole.setOnClickListener(this);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
        rLoyoutAuthorize = (RelativeLayout) findViewById(R.id.rLoyoutAuthorize);
        rLoyoutAuthorize.setOnClickListener(this);
        TextView tViewApply = (TextView) findViewById(R.id.tViewApply);
        tViewApply.setOnClickListener(this);

        tViewAccount = (TextView) findViewById(R.id.tViewAccount);
        tViewCommName = (TextView) findViewById(R.id.tViewCommName);
        tViewBuildingInfo = (TextView) findViewById(R.id.tViewBuildingInfo);
        tViewTipsInfo = (TextView) findViewById(R.id.tViewTipsInfo);
        tViewAccountRole = (TextView) findViewById(R.id.tViewAccountRole);
        iv_user_info_back = (ImageView) findViewById(R.id.iv_user_info_back);
        iv_user_info_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String account = SharedPreUtils.getStringValue(UserInfoActivity.this
                , JHConstant.KEY_USER_ACCOUNT, "");
        tViewAccount.setText(account);
        initBindingInfo();
    }

    private void initBindingInfo() {
        JHRole role = null;
        try {
            role = JHSDKCore.getUserService().getCurrentRole();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (role == null) {
            SpannableStringBuilder builder = new SpannableStringBuilder("   你还未绑定小区，你将不能使用小区相关功能。如：监视、开锁、小区通知等，如果你需要启用相关功能，你可以通过 ");
            SpannableStringBuilder buildingInfoBuilder = new SpannableStringBuilder("业主授权 ");
            buildingInfoBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
                    buildingInfoBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(buildingInfoBuilder);
            builder.append(" 或者自己 ");
            buildingInfoBuilder.clear();
            buildingInfoBuilder.append("申请成为业主");
            buildingInfoBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
                    buildingInfoBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(buildingInfoBuilder);
            tViewTipsInfo.setText(builder);
            if (lLayoutInfo.getVisibility() == View.VISIBLE) {
                lLayoutInfo.setVisibility(View.GONE);
            }
            if (rlayoutTips.getVisibility() == View.GONE) {
                rlayoutTips.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (lLayoutInfo.getVisibility() == View.GONE) {
            lLayoutInfo.setVisibility(View.VISIBLE);
        }
        if (rlayoutTips.getVisibility() == View.VISIBLE) {
            rlayoutTips.setVisibility(View.GONE);
        }

        tViewCommName.setText(role.getCommName());
        if (JHConstant.ROLE_OWNER_TYPE.equals(role.getUserType())) {
            //业主类型 业主=“0”，家属=“2”
            if (role.getApplyType() == 0) {
                if (rLoyoutAuthorize.getVisibility() == View.GONE) {
                    rLoyoutAuthorize.setVisibility(View.VISIBLE);
                }
                tViewAccountRole.setText("业主");
            } else if (role.getApplyType() == 2) {
                if (rLoyoutAuthorize.getVisibility() == View.VISIBLE) {
                    rLoyoutAuthorize.setVisibility(View.GONE);
                }
                tViewAccountRole.setText("家属");
            }
            tViewBuildingInfo.setText(AccountUtils.getRoomInfo(role.getBindingRoom()));
        } else if (JHConstant.ROLE_FAMILY_TYPE.equals(role.getUserType())) {
            if (rLoyoutAuthorize.getVisibility() == View.VISIBLE) {
                rLoyoutAuthorize.setVisibility(View.GONE);
            }
            tViewAccountRole.setText("家属");
            tViewBuildingInfo.setText(AccountUtils.getRoomInfo(role.getBindingRoom()));
        } else if (JHConstant.ROLE_MANAGER_TYPE.equals(role.getUserType())) {
            tViewBuildingInfo.setText("无");
            tViewAccountRole.setText("管理处");
            if (rLoyoutAuthorize.getVisibility() == View.VISIBLE) {
                rLoyoutAuthorize.setVisibility(View.GONE);
            }
        } else if (JHConstant.ROLE_VILLA_TYPE.equals(role.getUserType())) {
            tViewBuildingInfo.setText("无");
            tViewAccountRole.setText("别墅用户");
            if (rLoyoutAuthorize.getVisibility() == View.VISIBLE) {
                rLoyoutAuthorize.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rLoyoutAuthorize) {
            Intent intent = new Intent(UserInfoActivity.this, AuthorizedFamilyActivity.class);
            startActivity(intent);
        } else if (id == R.id.tViewApply) {
            Intent intent = new Intent(UserInfoActivity.this, RegisterOwnerActivity.class);
            startActivity(intent);
        } else if (id == R.id.rlayoutAccountRole) {
            final List<JHRole> roles = JHSDKCore.getUserService().getAllRole();
            int roleSize = roles.size();
            if (roleSize > 1) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
                String[] operateRoles = new String[roleSize];
                for (int i = 0; i < roleSize; i++) {
                    JHRole role = roles.get(i);
                    if (JHConstant.ROLE_OWNER_TYPE.equals(role.getUserType())) {
                        //业主类型 业主=“0”，家属=“2”
                        if (role.getApplyType() == 0) {
                            operateRoles[i] = "业主 -- [" + role.getCommName() + "]" + AccountUtils.getRoomInfo(role.getBindingRoom());
                        } else if (role.getApplyType() == 2) {
                            operateRoles[i] = "家属 -- [" + role.getCommName() + "]" + AccountUtils.getRoomInfo(role.getBindingRoom());
                        }
                    } else if (JHConstant.ROLE_MANAGER_TYPE.equals(role.getUserType())) {
                        operateRoles[i] = "管理处";
                    } else if (JHConstant.ROLE_VILLA_TYPE.equals(role.getUserType())) {
                        operateRoles[i] = "别墅用户";
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    arrayAdapter.addAll(operateRoles);
                } else {
                    for (String operateType : operateRoles) {
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
                        JHSDKCore.getUserService().setCurrentRole(roles.get(i));
                        initBindingInfo();
                        alert.dismiss();
                    }
                });
            }
        } else if (id == R.id.rLayoutSynCommInfo) {
            showDialog("正在同步小区信息...");
            JHSDKCore.getUserService().syncAccountInfo(new JHCallback<String>() {
                @Override
                public void onSuccess(String s) {

                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    Toast.makeText(UserInfoActivity.this, "同步失败！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    initBindingInfo();
                    clossDialog();
                }
            });
        } else if (id == R.id.buttonLogout) {
            new MaterialDialog(UserInfoActivity.this)
                    .setTitle("帐号退出提示")
                    .setMessage("确定要退出帐号吗？")
                    .setPositiveButton("确定", new MaterialDialog.DialogOnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreUtils.put(UserInfoActivity.this, APPConstant.IS_LOGIN, false);
                            SharedPreUtils.put(UserInfoActivity.this, JHConstant.KEY_USER_PASSWORD, "");
                            Log.d("------password------", SharedPreUtils.get(UserInfoActivity.this, JHConstant.KEY_USER_PASSWORD,"")+"");
                            JHSDKCore.getUserService().unRegister();
//                            Intent intent = new Intent();
//                            intent.setClass(UserInfoActivity.this, LoginActivity.class);
//                            startActivityForResult(intent, REQUEST_CODE);
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new MaterialDialog.DialogOnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }else if(id == R.id.iv_user_info_back){
            UserInfoActivity.this.finish();
        }
    }

    private void showDialog(String text) {
        clossDialog();
        // 获取Dialog布局
        View view = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.dialog_loading_guard, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_loading);
        textView.setText(text);
        alert = new MaterialDialog(UserInfoActivity.this)
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

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
