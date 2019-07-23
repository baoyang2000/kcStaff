package com.ctrl.android.kcetong.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Managment;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @项目名称: 诚信行业主端 <br>
 * @类描述: e管家<br>
 * @创建人： whs <br>
 * @创建时间： 2017/1/5 17:21 <br>
 * @修改人： <br>
 * @修改时间: 2017/1/5 17:21 <br>
 */
public class EStewardFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.listView)//商品列表
    ListView     listView;
    @BindView(R.id.et_e_house_keeper)//输入框
    EditText     etEHouseKeeper;
    @BindView(R.id.tv_e_house_keeper_send)//发送
    TextView     tvEHouseKeeperSend;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    private Context context;
    private List<Managment> mManagmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_estewar, container, false);

            ButterKnife.bind(this, rootView);
            context = this.getActivity();
            toolbar_title.setText("e管家");
        }
        tvEHouseKeeperSend.setOnClickListener(this);
        return rootView;
    }

    private void getMessageList(String ownerProblem, String communityId) {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantsData.METHOD, "pm.ppt.eSteward.selectInstantMessaging");
        map.put("ownerProblem", ownerProblem);
        map.put("communityId", communityId);
        map.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
    }

    @Override
    public void onClick(View v) {
        LLog.d("----click---");
    }
}
