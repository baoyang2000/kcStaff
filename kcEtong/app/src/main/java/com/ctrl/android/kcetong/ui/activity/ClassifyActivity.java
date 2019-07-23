package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.Kind;
import com.ctrl.android.kcetong.model.KindResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassifyActivity extends BaseActivity {

    @BindView(R.id.lv_classify)
    ListView lv_classify;

    private List<Kind> mKindList;
    private List<String> list;
    private ArrayAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);
        toolbarBaseSetting("分类", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassifyActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mKindList = new ArrayList<>();
        list = new ArrayList<>();
        requestData(StrConstant.SECOND_MARKET_TYPE_APPKEY);
    }
    /**
     *获取分类列表接口
     * @param kindKey 类别KEY
     */
    public void requestData(String kindKey){
        Map<String,String> params = new HashMap<>();
        params.put("method","pm.common.kind.list");
        params.putAll(ConstantsData.getSystemParams());

        params.put("kindKey",kindKey);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        LLog.d(params+"");
        params.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().requestData(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseTSubscriber<KindResult>(this) {
                    @Override
                    public void onResponseCallback(JSONObject response, String resultCode) {
                        LLog.d(response+"");
                    }

                    @Override
                    public void onNext(KindResult kindResult) {
                        super.onNext(kindResult);
                        if(TextUtils.equals("000",kindResult.getCode())){
                            List<Kind> list_result = kindResult.getData().getKindList();
                            if(list_result != null && list_result.size()>0){
                                mKindList = list_result;
                                for(int i=0;i<mKindList.size();i++){
                                    list.add(mKindList.get(i).getKindName());
                                }
                                adapter = new ArrayAdapter<>(ClassifyActivity.this,android.R.layout.simple_list_item_1,list);
                                lv_classify.setAdapter(adapter);
                                lv_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(getIntent().getFlags()==StrConstant.WANT_BUY_TYPE){
                                            getIntent().putExtra("kind", mKindList.get(position));
                                            setResult(RESULT_OK, getIntent());
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }
}
