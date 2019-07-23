package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FamillyHotLineListActivity extends BaseActivity {

    @BindView(R.id.btn_repairs)
    Button   btnRepairs;
    @BindView(R.id.btn_complaint)
    Button   btnComplaint;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_familly_hot_line_list);
        ButterKnife.bind(this);
        toolbarBaseSetting("报修投诉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamillyHotLineListActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.btn_repairs, R.id.btn_complaint})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_repairs:
                Utils.startActivity(null, FamillyHotLineListActivity.this, MyRepairsActivity.class);
                break;
            case R.id.btn_complaint:
                Utils.startActivity(null, FamillyHotLineListActivity.this, MyComplaintActivity.class);
                break;
        }
    }
}
