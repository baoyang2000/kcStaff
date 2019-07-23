package com.ctrl.android.kcetong.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Surround;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.SurroundAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SurroundingMerchantsActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SurroundAdapter adapter;
    private String serviceKindId;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_surrounding_merchants);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.getStringExtra("serviceKindId") != null){
            serviceKindId = intent.getStringExtra("serviceKindId");
        }
        String title = "";
        if(intent.getStringExtra("title") != null){
            title = intent.getStringExtra("title");
        }
        toolbarBaseSetting(title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurroundingMerchantsActivity.this.finish();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SurroundAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        getData();
    }

    private void getData() {
        if(S.isNull(serviceKindId)){
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ConstantsData.METHOD, Url.surroundListUrl);
//        params.put("communityId", holder.getHouse().getCommunityId());
        params.put("serviceKindId", serviceKindId);
        params.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign", sign);
        params.remove(ConstantsData.METHOD);
        LLog.d(params + "");
        RetrofitUtil.Api().surroundList(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<Surround>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(Surround surround) {
                if (surround.getCode().equals("000")) {
                    if(surround.getData() != null && surround.getData().getServiceList() != null && surround.getData().getServiceList().size() > 0){
                        adapter.setDataList(surround.getData().getServiceList());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
