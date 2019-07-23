package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.util.ViewUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 论坛 发布活动 页面1
 * Created by liu on 2017/1/11.
 */

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.toolbar_right_btn)
    TextView       toolbar_right_btn;
    @BindView(R.id.act_title_text)//标题
    EditText       act_title_text;
    @BindView(R.id.act_start_time_text)//起始时间
    TextView       act_start_time_text;
    @BindView(R.id.act_end_time_text)//结束时间
    TextView       act_end_time_text;
    @BindView(R.id.act_address_text)//活动地点
    EditText       act_address_text;
    @BindView(R.id.activity_layout)
    RelativeLayout activity_layout;

    private View       mMenuView;
    private TimePicker timePicker;
    private Button     superman_ok_btn;
    private Button     superman_cancel_btn;

    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String time_str;
    private static final int REQUEST_NEXT = 111;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.START_ACT_TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchActivity.this.finish();
            }
        });
        toolbar_right_btn.setVisibility(View.VISIBLE);
        toolbar_right_btn.setText("下一步");

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_NEXT:
                if(resultCode == RESULT_OK){//发布成功的话
                    if("1".equals(data.getStringExtra("releaseSuccess"))){
                        LaunchActivity.this.finish();
                    }
                }
                break;
        }
    }

    @OnClick({R.id.toolbar_right_btn, R.id.act_start_time_text, R.id.act_end_time_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_btn://下一步
                if (checkInput()) {
                    Intent intent = new Intent(LaunchActivity.this, LaunchActivity2.class);
                    intent.putExtra("title", act_title_text.getText().toString());
                    intent.putExtra("startTime", act_start_time_text.getText().toString() + ":00");
                    intent.putExtra("endTime", act_end_time_text.getText().toString() + ":00");
                    intent.putExtra("location", act_address_text.getText().toString());
                    startActivityForResult(intent, REQUEST_NEXT);
                }
                break;
            case R.id.act_start_time_text://开始时间
                ViewUtil.hideInput(this, act_start_time_text);
                showTimePickerPop(act_start_time_text);
                break;
            case R.id.act_end_time_text://结束时间
                ViewUtil.hideInput(this, act_start_time_text);
                showTimePickerPop(act_end_time_text);
                break;
        }
    }

    private void showTimePickerPop(final TextView editText) {
        //setRoomData();
        mMenuView = LayoutInflater.from(LaunchActivity.this).inflate(R.layout.choose_time_bottom_pop, null);
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
                editText.setText(time_str);
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
        Pop.showAtLocation(activity_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private boolean checkInput() {

        if (act_title_text.getText().toString() == null || act_title_text.getText().toString().equals("")) {
            Utils.toastError(this, "标题不可为空");
            return false;
        }
        if (act_start_time_text.getText().toString() == null || act_start_time_text.getText().toString().equals("")) {
            Utils.toastError(this, "开始时间不可为空");
            return false;
        }

        if (act_end_time_text.getText().toString() == null || act_end_time_text.getText().toString().equals("")) {
            Utils.toastError(this, "结束时间不可为空");
            return false;
        }

        if (act_address_text.getText().toString() == null || act_address_text.getText().toString().equals("")) {
            Utils.toastError(this, "活动地点不可为空");
            return false;
        }

        return true;
    }

}
