package com.jh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jh.R;
import com.jh.activity.ConfigActivity;
import com.jh.activity.UserInfoActivity;
import com.jh.constant.APPConstant;
import com.jhsdk.bean.api.JHRole;
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;
import com.jhsdk.utils.JHReturnCode;
import com.jhsdk.utils.SharedPreUtils;

/**
 * Created by Administrator on 2016/12/20.
 */

public class PersonalCenterFragment extends Fragment implements View.OnClickListener
        , JHSDKListener.RegistrStateChangedListener {
    private TextView tViewAccount, tViewCommName, tViewRegistrStatus;

    private ImageView iv_personal_back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        JHSDKCore.addListener(this);

        LinearLayout lLayoutUserInfo = (LinearLayout) view.findViewById(R.id.lLayoutUserInfo);
        lLayoutUserInfo.setOnClickListener(this);
        RelativeLayout rlayoutConfig = (RelativeLayout) view.findViewById(R.id.rlayoutConfig);
        rlayoutConfig.setOnClickListener(this);
        iv_personal_back = (ImageView) view.findViewById(R.id.iv_personal_back);
        iv_personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalCenterFragment.this.getActivity().finish();
            }
        });
        tViewAccount = (TextView) view.findViewById(R.id.tViewAccount);
        tViewCommName = (TextView) view.findViewById(R.id.tViewCommName);
        tViewRegistrStatus = (TextView) view.findViewById(R.id.tViewRegistrStatus);
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
        String account = SharedPreUtils.getStringValue(getActivity()
                , JHConstant.KEY_USER_ACCOUNT, "");
        tViewAccount.setText(account);
        JHRole role = JHSDKCore.getUserService().getCurrentRole();
        if (role != null) {
            tViewCommName.setText(role.getCommName());
        }

        tViewRegistrStatus.setText(
                JHReturnCode.showReqStatus(JHSDKCore.getUserService().registerStateCode(), getActivity()));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        if(id == R.id.lLayoutUserInfo){
            boolean isLogin = SharedPreUtils.getBooleanValue(getActivity(), APPConstant.IS_LOGIN, false);
            if (!isLogin) {
                intent = new Intent(getActivity(), com.jh.activity.LoginActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        }else if(id == R.id.rlayoutConfig){
            intent = new Intent(getActivity(), ConfigActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        View view = this.getView();
        if (view != null) {
            view.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

//    @Override
//    public void onRegistrStateChanged(int code) {
//        JHSDKCore.addListener(this);
//        Log.w("-----getActivity  -----",getActivity()+"");
////        Log.w("-----mommmmmmmmm--",JHReturnCode.showReqStatus(code, getActivity()));
//        if(tViewRegistrStatus != null && getActivity() != null){
//            tViewRegistrStatus.setText(JHReturnCode.showReqStatus(code, getActivity()));
//        }
//    }

    @Override
    public void onRegistrStateChanged(int i, String s) {
        JHSDKCore.addListener(this);
        Log.w("-----getActivity  -----",getActivity()+"");
//        Log.w("-----mommmmmmmmm--",JHReturnCode.showReqStatus(code, getActivity()));
        if(tViewRegistrStatus != null && getActivity() != null){
            tViewRegistrStatus.setText(JHReturnCode.showReqStatus(i, getActivity()));
        }
    }
}
