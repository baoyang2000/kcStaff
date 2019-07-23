package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.House;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.InputMethodUtils;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ctrl.android.kcetong.R.id.visit_room;

public class VisitAddActivity extends BaseActivity {


    @BindView(visit_room)//拜访房间
            TextView     visitRoom;
    @BindView(R.id.room_change)//修改
            TextView     roomChange;
    @BindView(R.id.visit_name)//到访人
            EditText     visitName;
    @BindView(R.id.visit_time)//到访时间
            TextView     visitTime;
    @BindView(R.id.visit_count)//到访人数
            EditText     visitCount;
    @BindView(R.id.visit_car)//车牌号
            EditText     visitCar;
    @BindView(R.id.visit_stop)//预计停留时间
            EditText     visitStop;
    @BindView(R.id.activity_visit_add)
            LinearLayout activityVisitAdd;
    @BindView(R.id.toolbar_right_btn)//完成按钮
            TextView     rightbtn;

    private View       mMenuView;
    private TimePicker timePicker;
    private Button     superman_ok_btn;
    private Button     superman_cancel_btn;
    private static final int CHOOSE_visit_CODE = 5;//表示从预约访客跳到房屋列表的参数
    private House house;

    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String time_str;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_visit_add);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        toolbarBaseSetting("添加到访", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(VisitAddActivity.this);
                VisitAddActivity.this.finish();
            }
        });
        rightbtn.setVisibility(View.VISIBLE);
        rightbtn.setText("完成");

        if(AppHolder.getInstance().getHouse().getId() != null){
            visitRoom.setText(AppHolder.getInstance().getHouse().getCommunityName()
                               + "  " + AppHolder.getInstance().getHouse().getBuilding()
                               + "-" + AppHolder.getInstance().getHouse().getUnit()
                               + "-" + AppHolder.getInstance().getHouse().getRoom());
        }

        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    String communityId   = AppHolder.getInstance().getHouse().getCommunityId();//具体问题具体分析
                    String proprietorId  = AppHolder.getInstance().getProprietor().getProprietorId();//具体问题具体分析
                    String addressId     = AppHolder.getInstance().getHouse().getAddressId();//具体问题具体分析
                    String visitorName   = S.getStr(visitName.getText().toString());
                    String arriveTime    = S.getStr(visitTime.getText().toString()) + ":00";
                    String peopleNum     = S.getStr(visitCount.getText().toString());
                    String numberPlates  = S.getStr(visitCar.getText().toString());
                    String residenceTime = S.getStr(visitStop.getText().toString());
                    addvisitinfo(communityId, proprietorId, addressId, visitorName, arriveTime, peopleNum, numberPlates, residenceTime);
                }
            }
        });


    }
    //添加到访信息
    private void addvisitinfo(String communityId,String proprietorId,
            String addressId,String visitorName,
            String arriveTime,String peopleNum,
            String numberPlates,String residenceTime) {
       showProgress(true);
        Map<String,String> map= new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.addvisitUrl);
        map.put("communityId",communityId);
        map.put("proprietorId",proprietorId);
        map.put("addressId",addressId);
        map.put("visitorName",visitorName);
        map.put("arriveTime",arriveTime);
        map.put("peopleNum",peopleNum);
        map.put("numberPlates",numberPlates);
        map.put("residenceTime",residenceTime);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        LLog.d(map + "");
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().addvisitinfo(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ResponseBody>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
              showProgress(false);
                LLog.d("onResponseCallback: " + response);
                    if(TextUtils.equals("000", resultCode)){
                        MessageUtils.showShortToast(VisitAddActivity.this,"添加成功");
                        Intent intent = new Intent();
                        setResult(7001, intent);
                        finish();
                    }else{
                        try {
                            MessageUtils.showShortToast(VisitAddActivity.this, (String) response.get("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
    }

    private boolean checkInput() {

        if (S.isNull(visitRoom.getText().toString())) {
            MessageUtils.showShortToast(this, "拜访房间不可为空");
            return false;
        }

        if (S.isNull(visitName.getText().toString())) {
            MessageUtils.showShortToast(this, "到访人不可为空");
            return false;
        }

        if ((visitCount.getText().toString() == null || visitCount.getText().toString().equals("") ? 0 : Integer.parseInt(visitCount.getText().toString())) <= 0) {
            MessageUtils.showShortToast(this, "到访人数不可小于0");
            return false;
        }

        return true;
    }

    @OnClick({R.id.room_change, R.id.visit_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.room_change:
               Intent intent = new Intent(VisitAddActivity.this, HouseListActivity.class);
                intent.addFlags(StrConstant.COMPLAINT_ROOM_CHANGE);
                startActivityForResult(intent, CHOOSE_visit_CODE);
                break;
            case R.id.visit_time:
                InputMethodUtils.hide(VisitAddActivity.this);
                showTimePickerPop(visitTime);
                break;
        }
    }

    /**
     * 显示时间拾取器的 弹出
     */
    private void showTimePickerPop(final TextView textView) {
        //setRoomData();
        mMenuView = LayoutInflater.from(VisitAddActivity.this).inflate(R.layout.choose_time_bottom_pop, null);
        timePicker = (TimePicker) mMenuView.findViewById(R.id.timePicker);
        superman_ok_btn = (Button) mMenuView.findViewById(R.id.superman_ok_btn);
        superman_cancel_btn = (Button) mMenuView.findViewById(R.id.superman_cancel_btn);

        final PopupWindow Pop = new PopupWindow();


        // 设置SelectPicPopupWindow的View
        Pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        Pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        Pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        Pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        Pop.setBackgroundDrawable(dw);


        superman_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = timePicker.getYear();
                month = timePicker.getMonth();
                day = timePicker.getDay();
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
                time_str = timePicker.getTime_string();
                Log.d("demo", "time_str: " + time_str);
                textView.setText(time_str);
                Pop.dismiss();
            }
        });

        superman_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pop.dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y      = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        Pop.dismiss();
                    }
                }
                return true;
            }
        });
        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        Pop.setFocusable(true);
        Pop.showAtLocation(activityVisitAdd, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_visit_CODE&&resultCode==2001){
            Bundle bundle = data.getExtras();
            house = (House) bundle.getSerializable("house");

            if (TextUtils.isEmpty(house.getId())) {
                visitRoom.setText(house.getCommunityName() + house.getBuilding() + getString(R.string.floor) + house.getUnit() + "单元" + house.getRoom() + "号房");
            }

        }
    }
}
