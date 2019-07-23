package com.jh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.jh.R;
import com.jh.utils.StringUtils;
import com.jh.view.T9TelephoneDialpadView;
import com.jhsdk.core.JHSDKCore;

/**
 * Created by Administrator on 2016/12/20.
 */

public class DialPlateFragment extends Fragment implements  T9TelephoneDialpadView.OnT9TelephoneDialpadView{
    private EditText eTextDialPlate;

    private ImageView iv_call_back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dial_plate, container, false);
        eTextDialPlate = (EditText) view.findViewById(R.id.eTextDialPlate);
        iv_call_back = (ImageView) view.findViewById(R.id.iv_call_back);
        iv_call_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialPlateFragment.this.getActivity().finish();
            }
        });
        T9TelephoneDialpadView t9TelephoneDialpadLayout = (T9TelephoneDialpadView) view.findViewById(R.id.t9TelephoneDialpadLayout);
        t9TelephoneDialpadLayout.setOnT9TelephoneDialpadView(this, eTextDialPlate);
        return view;
    }

    @Override
    public void onCall() {
        String account = eTextDialPlate.getText().toString();
        if (!StringUtils.isEmail(account)) {
            JHSDKCore.getCallService().makeCall(account);
        }
    }

    @Override
    public void addContacts() {
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        View view = this.getView();
        if (view != null) {
            view.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }
}
