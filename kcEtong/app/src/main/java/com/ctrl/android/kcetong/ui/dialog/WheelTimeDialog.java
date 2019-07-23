package com.ctrl.android.kcetong.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ctrl.android.kcetong.listener.OngetTimesLinstener;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.view.WheelViews;

import java.util.ArrayList;
import java.util.List;

public class WheelTimeDialog extends Dialog {
    /**
     * 所有省
     */
    protected String mCurrentProviceName;// 当前省的名称
    protected String mCurrentCityName;// 当前市的名称
    protected String              mCurrentDistrictName = ""; // 当前区的名称
    private   Activity            mContext             = null;
    private   View                parentView           = null;
    private   String              proName              = "山东省";
    private   String              cityName             = "济南市";
    private   String              disName              = "历下区";
    private   String              proId                = "370000";
    private   String              cityId               = "370100";
    private   String              disId                = "370102";
    private   OngetTimesLinstener linstener            = null;
    private   List<String>        timeList             = new ArrayList<>();

    private ArrayList<String> leftData = new ArrayList<>();

    private ArrayList<String> strData = new ArrayList<>();//2016-11-11
    private ArrayList<ArrayList<String>> rightData = new ArrayList<>();
    private WheelViews mViewDay;
    private WheelViews mViewTime;
    private String dayName;
    private String dayId;
    private String timeName;
    private String strName;
    private String strDateId;
    private String timeId;
    public WheelTimeDialog(Activity context) {
        super(context, com.ctrl.android.kcetong.R.style.transparentFrameWindowStyle);
        mContext = context;

    }

    public void setLinstener(OngetTimesLinstener linstener) {
        this.linstener = linstener;
    }

    public static int dip2px(float dip, Context context) {
        DisplayMetrics me = context.getResources().getDisplayMetrics();
        int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, me);
        return margin;
    }

    /**
     * 设置监听器并show 转为 设置城市使用
     *
     * @date 2015-1-14
     */
    public void showDialogForTiems(ArrayList<String> strData,ArrayList<String> leftData,ArrayList<ArrayList<String>> rightData) {
        this.rightData = rightData;
//        if (provinceList.size() < 2) {
//            provinceList = addressdata.addressdata(mContext);
//        }
//        loaddate();
        this.leftData = leftData;
        this.strData = strData;
        /*for(int i = 0;i < rightData.size();i++){
            timeList = rightData.get(i);

        }*/
        timeList = rightData.get(0);
        strName = strData.get(0);
        timeName = timeList.get(0);

        initView();
    }

    private void initView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Utils.getDisplayWidth(mContext) / 2, LayoutParams.WRAP_CONTENT);
        if (parentView == null) {
            parentView = getLayoutInflater().inflate(
                    com.ctrl.android.kcetong.R.layout.dialog_two_wheels_time, null);
        }
        if (mViewTime == null) {
            mViewDay = (WheelViews) parentView
                    .findViewById(com.ctrl.android.kcetong.R.id.wheelview_day);
            mViewTime = (WheelViews) parentView
                    .findViewById(com.ctrl.android.kcetong.R.id.wheelview_time);
            mViewDay.setLayoutParams(params);
            mViewTime.setLayoutParams(params);
        }
        Button sub = (Button) parentView.findViewById(com.ctrl.android.kcetong.R.id.btn_to_tip_ok);
        Button cancel = (Button) parentView.findViewById(com.ctrl.android.kcetong.R.id.btn_to_tip_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        sub.setOnClickListener(new MyOnclick());
        mViewDay.setOffset(1);
        mViewDay.setItems(leftData);
        mViewDay.scrollTop();
        mViewDay.setOnWheelViewListener(new WheelViews.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        changeProvince(selectedIndex - 1);
                        dayName = item;
                        dayId = leftData.get(selectedIndex-1);
                        strName = strData.get(selectedIndex-1);
                        timeName = timeList.get(0);
                    }
                });
        mViewTime.setOffset(1);
        mViewTime.setItems(timeList);
        mViewTime.scrollTop();
        mViewTime.setOnWheelViewListener(new WheelViews.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                timeName = item;
                timeId = timeList.get(selectedIndex-1);
            }
        });
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        parentView.setLayoutParams(mLayoutParams);

        setContentView(parentView);
        setShowPosition();
        show();
    }

    /**
     * 设置对话框显示位置
     *
     * @date 2015-1-14
     */
    @SuppressWarnings("deprecation")
    private void setShowPosition() {
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(com.ctrl.android.kcetong.R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mContext.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = LayoutParams.MATCH_PARENT;
        wl.height = LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
    }

    /**
     * 用户点击城市确定按钮后返回的回调此回调将用户选择的城市返回
     *
     * @author 小米粥
     * @date 2015-1-15
     */
    public class MyOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (linstener != null) {
                linstener.getString(strName+" "+timeName);
            }
        }

    }

    private void loaddate() {
//        mDayDatas = new ArrayList<String>();
//        if (dayList == null || dayList.size() < 1) {
//            return;
//        }
//        for (int i = 0; i < dayList.size(); i++) {
//            // 遍历所有省的数据
//            mDayDatas.add(dayList.get(i));
//
//        }
//        mTimeDatas = new ArrayList<String>();
//        timeList = dayList.get(0).getTimeList();
//
//        for (int j = 0; j < timeList.size(); j++) {
//            // 遍历所有市的数据
//            mTimeDatas.add(timeList.get(j).getName());
//
//        }
//        mDistrictDatas = new ArrayList<String>();
//        districtList = cityList.get(0).getDistrictList();
//
//        for (int k = 0; k < districtList.size(); k++) {
//            // 遍历所有区的数据
//            mDistrictDatas.add(districtList.get(k).getName());
//
//        }
        initView();
    }

    /**

     *Created by  赵昌星
     *2015-4-10
     *下午4:17:12
     *@Description: TODO方法的作用：改变省级列表数据
     *  @param i
     */
    private void changeProvince(int i) {
        //timeList = new ArrayList<>();
        timeList = rightData.get(i);

        /*cityId = timeList.get(0);
        cityName =timeList.get(0);*/

        /*for (int j = 0; j < timeList.size(); j++) {
            // 遍历所有市的数据
            timeList.add(timeList.get(j));
        }*/
        mViewTime.setItems(timeList);
        mViewTime.setOffset(1);
        mViewTime.scrollTop();
        Log.d("rightList------",rightData.toString());
        Log.d("rightList------",timeList.toString());
    }

    /**
     改变市级列表
     *Created by  赵昌星
     *2015-4-10
     *下午4:17:39
     *@Description: TODO方法的作用：
     *  @param i
     */
//    private void changeCity(int i) {
//        districtList = cityList.get(i).getDistrictList();
//
//        disId = districtList.get(0).getId();
//        disName = districtList.get(0).getName();
//
//        mDistrictDatas = new ArrayList<String>();
//        for (int k = 0; k < districtList.size(); k++) {
//            // 遍历所有省的数据
//            mDistrictDatas.add(districtList.get(k).getName());
//        }
//        mViewDistrict.setItems(mDistrictDatas);
//        mViewDistrict.setOffset(1);
//        mViewDistrict.scrollTop();
//    }
}
