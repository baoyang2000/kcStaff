package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Kind;
import com.ctrl.android.kcetong.model.KindResult;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.util.jUtils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.kcetong.ui.adapter.SecondHandGridViewAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.fragment.SecondHandFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SecondHandActivity extends BaseActivity {

    @BindView(R.id.viewpager_secondhand)
    ViewPager    viewpager_secondhand;
    @BindView(R.id.tv_secong_hand_transfer)
    TextView     tv_secong_hand_transfer;//转让
    @BindView(R.id.tv_secong_hand_buy)
    TextView     tv_secong_hand_buy;//求购
    @BindView(R.id.toolbar_right_left)
    TextView     toolbar_right_left;
    @BindView(R.id.toolbar_right_right)
    TextView     toolbar_right_right;
    @BindView(R.id.toolbar_layout_right)
    LinearLayout toolbar_layout_right;

    private RelativeLayout rl_second_hand;
    private RelativeLayout rl_main;

    private List<Fragment> fragments = new ArrayList<>();
    private SecondHandFragment        fragment01;
    private SecondHandFragment        fragment02;
    private JasonViewPagerAdapter     adapter;
    private String transactionType = "0";

    private String kindId = null;
    public static final int UPDATE_LIST=2000;
    private PopupWindow popupWindow;
    public static final int UPDATE_LIST_BUY=2001;
    private List<String>mKindListName = new ArrayList<>();
    private List<String>mKindListId = new ArrayList<>();
    private View mMenuView;
    private SecondHandGridViewAdapter gvadapter;
    private GridView gv_second_hand;
    private Button btn_second_hand_gridview;
    private int mposition;
    private PopupWindow popupWindowButtom;

    private List<Kind> kindList ;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_second_hand);
        ButterKnife.bind(this);
        toolbarBaseSetting("闲鱼市场", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondHandActivity.this.finish();
            }
        });
        toolbar_layout_right.setVisibility(View.VISIBLE);
        toolbar_right_left.setText("类别");
        toolbar_right_right.setText("添加");
    }

    @Override
    protected void initData() {
        kindList = new ArrayList<>();
        gvadapter = new SecondHandGridViewAdapter(this);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);

        fragment01 = SecondHandFragment.newInstance(StrConstant.SECOND_HAND_TRANSFER, kindId);
        fragment02 = SecondHandFragment.newInstance(StrConstant.SECOND_HAND_BUY, kindId);
        fragments.add(fragment01);
        fragments.add(fragment02);

        adapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        adapter.setOnReloadListener(new JasonViewPagerAdapter.OnReloadListener() {
            @Override
            public void onReload() {
                fragments = null;
                List<Fragment> list = new ArrayList<Fragment>();

                if(fragment01 != null){
                    list.add(SecondHandFragment.newInstance(StrConstant.SECOND_HAND_TRANSFER, kindId));
                }
                if (fragment02 != null){
                    list.add(SecondHandFragment.newInstance(StrConstant.SECOND_HAND_BUY, kindId));
                }
                adapter.setPagerItems(list);
                viewpager_secondhand.setAdapter(adapter);
            }
        });
        viewpager_secondhand.setAdapter(adapter);
        viewpager_secondhand.setCurrentItem(0);
        viewpager_secondhand.setOffscreenPageLimit(2);
        viewpager_secondhand.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        transactionType = "0";
                        tv_secong_hand_transfer.setBackgroundResource(R.drawable.button_left_shap_checked);
                        tv_secong_hand_transfer.setTextColor(Color.WHITE);
                        tv_secong_hand_buy.setBackgroundResource(R.drawable.button_right_shap);
                        tv_secong_hand_buy.setTextColor(Color.GRAY);
                        break;
                    case 1:
                        transactionType = "1";
                        tv_secong_hand_buy.setBackgroundResource(R.drawable.button_right_shap_checked);
                        tv_secong_hand_buy.setTextColor(Color.WHITE);
                        tv_secong_hand_transfer.setBackgroundResource(R.drawable.button_left_shap);
                        tv_secong_hand_transfer.setTextColor(Color.GRAY);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case UPDATE_LIST://转让列表更新
                if(RESULT_OK==resultCode){
                    adapter.reLoad();

                }
                break;
            case UPDATE_LIST_BUY:
                if(RESULT_OK == resultCode){
                    adapter.reLoad();
                    viewpager_secondhand.setCurrentItem(1);
                }
                break;
        }
    }

    @OnClick({R.id.tv_secong_hand_transfer, R.id.tv_secong_hand_buy, R.id.toolbar_right_left, R.id.toolbar_right_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_secong_hand_transfer:
                tv_secong_hand_transfer.setBackgroundResource(R.drawable.button_left_shap_checked);
                tv_secong_hand_transfer.setTextColor(Color.WHITE);
                tv_secong_hand_buy.setBackgroundResource(R.drawable.button_right_shap);
                tv_secong_hand_buy.setTextColor(Color.GRAY);
                viewpager_secondhand.setCurrentItem(0);
                transactionType = "0";
                break;
            case R.id.tv_secong_hand_buy:
                tv_secong_hand_buy.setBackgroundResource(R.drawable.button_right_shap_checked);
                tv_secong_hand_buy.setTextColor(Color.WHITE);
                tv_secong_hand_transfer.setBackgroundResource(R.drawable.button_left_shap);
                tv_secong_hand_transfer.setTextColor(Color.GRAY);
                viewpager_secondhand.setCurrentItem(1);
                transactionType = "1";
                break;
            case R.id.toolbar_right_left://类别
                kindList.clear();
                requestData(StrConstant.SECOND_MARKET_TYPE_APPKEY);
                break;
            case R.id.toolbar_right_right://添加
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    showPopupWindow(view);//弹出下拉菜单
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(SecondHandActivity.this, getString(R.string.manager_cannot));
                }

                break;
        }
    }
    /**
     *获取分类列表接口
     * @param kindKey 类别KEY
     */
    public void requestData(String kindKey){
        Map<String,String> params = new HashMap<String,String>();
        params.put("method","pm.common.kind.list");
        params.putAll(ConstantsData.getSystemParams());

        params.put("kindKey",kindKey);
        String sign = AopUtils.sign(params, ConstantsData.SECRET_VALUE);
        params.put("sign",sign);
        LLog.d(params + "");
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
                                kindList = list_result;
                                mKindListName.clear();
                                mKindListId.clear();
                                for(int i=0;i < kindList.size();i++){
                                    mKindListName.add(kindList.get(i).getKindName());
                                    mKindListId.add(kindList.get(i).getId());
                                }
                                mKindListName.add(0,"全部");
                                mKindListId.add(0, null);
                                showPopupBottom();//底部弹出菜单
                            }
                        }
                    }
                });
    }

    private void showPopupBottom() {
        mMenuView = LayoutInflater.from(this).inflate(R.layout.second_hand_gridview,null);
        gv_second_hand = (GridView)mMenuView.findViewById(R.id.gv_second_hand);
        btn_second_hand_gridview = (Button)mMenuView.findViewById(R.id.btn_second_hand_gridview);
        gvadapter.setList(mKindListName);
        gv_second_hand.setAdapter(gvadapter);
        jUtils.setListViewHeightBasedOnChildren(gv_second_hand);
        gvadapter.notifyDataSetChanged();
        gv_second_hand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mposition = position;
                gvadapter.setSeclection(position);
                gvadapter.notifyDataSetChanged();
                kindId=mKindListId.get(position);

            }
        });
        popupWindowButtom = new PopupWindow(mMenuView,
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 设置SelectPicPopupWindow的View
        popupWindowButtom.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindowButtom.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindowButtom.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindowButtom.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindowButtom.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        popupWindowButtom.setBackgroundDrawable(dw);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        /*
        * 设置popupwindow 点击自身消失
        * */
        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindowButtom.isShowing()) {
                    popupWindowButtom.dismiss();
                }
            }
        });

        btn_second_hand_gridview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.reLoad();

                /*switch (viewpager_secondhand.getCurrentItem()) {
                    case 0:
                        fragment01.mLRecyclerView.setRefreshing(true);
                        break;
                    case 1:
                        fragment02.mLRecyclerView.setRefreshing(true);
                        break;
                }*/
                popupWindowButtom.dismiss();
            }
        });
        popupWindowButtom.setOutsideTouchable(true);
        popupWindowButtom.showAtLocation(rl_main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
   /*     popupWindowButtom.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindowButtom.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.edit_shap_green));*/

    }
    private void showPopupWindow(View view) {
        View contentView= LayoutInflater.from(this).inflate(R.layout.pop_window_secondhand,null);
        TextView tv01=(TextView)contentView.findViewById(R.id.tv_popwindow_transfer);
        TextView tv02=(TextView)contentView.findViewById(R.id.tv_popwindow_buy);
        tv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(SecondHandActivity.this,"我要转",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(SecondHandActivity.this, TransferActivity.class);
                startActivityForResult(intent,UPDATE_LIST);
                popupWindow.dismiss();
            }
        });
        tv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(SecondHandActivity.this,"我要购",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SecondHandActivity.this, WantBuyActivity.class);
                startActivityForResult(intent, UPDATE_LIST_BUY);
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.edit_shap_green));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view,-140,60);

    }

}
