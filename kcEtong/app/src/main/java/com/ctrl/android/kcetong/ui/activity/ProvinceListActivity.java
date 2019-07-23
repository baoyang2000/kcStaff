package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.city.Province;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProvinceListActivity extends BaseActivity {
    @BindView(R.id.lv_province_select)
    ListView lv_province_select;
    private static final String TITLE= StrConstant.SELECTE_PROVINCE;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_province_list);
        ButterKnife.bind(this);
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProvinceListActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        final List<String> province_list_code = new ArrayList<>();
        province_list_code.addAll(new Province().getProvince());
        LLog.d(province_list_code+"");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,province_list_code);
        lv_province_select.setAdapter(adapter);
        lv_province_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i =new Intent();
                i.putExtra("province",province_list_code.get(position));
                i.putExtra("pos",position);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }
}
