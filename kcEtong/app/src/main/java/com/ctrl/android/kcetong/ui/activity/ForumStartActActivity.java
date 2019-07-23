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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.util.ViewUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.TimePicker;
import com.ctrl.third.common.library.utils.AnimUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ctrl.android.kcetong.R.id.act_end_time_text;
import static com.ctrl.android.kcetong.R.id.act_start_time_text;

public class ForumStartActActivity extends BaseActivity {

    @BindView(R.id.act_title_text)//标题
            EditText       actTitleText;
    @BindView(R.id.act_start_time_text)//起始时间
            TextView       actStartTimeText;
    @BindView(R.id.act_end_time_text)//结束时间
            TextView       actEndTimeText;
    @BindView(R.id.act_address_text)//活动地点
            EditText       actAddressText;
    @BindView(R.id.activity_forum_start_act)
            RelativeLayout activityForumStartAct;
    @BindView(R.id.toolbar_right_btn)
            TextView       rightbtn;
    private String TITLE = StrConstant.START_ACT_TITLE;
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
    private String categoryId;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum_start_act);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        categoryId = getIntent().getStringExtra("categoryId");
    }

    @Override
    protected void initData() {
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(ForumStartActActivity.this);
                ForumStartActActivity.this.finish();
            }
        });
        rightbtn.setVisibility(View.VISIBLE);
        rightbtn.setText("下一步");
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                   // MessageUtils.showShortToast(ForumStartActActivity.this,"下一步");
                    Intent intent= new Intent(ForumStartActActivity.this,ForumStartActActivity2.class);
                    intent.putExtra("title",actTitleText.getText().toString());
                    intent.putExtra("startTime",actStartTimeText.getText().toString() + ":00");
                    intent.putExtra("endTime",actEndTimeText.getText().toString() + ":00");
                    intent.putExtra("location",actAddressText.getText().toString());
                    startActivityForResult(intent,101);
                    AnimUtil.intentSlidIn(ForumStartActActivity.this);

                }
            }
        });
    }

    private void showTimePickerPop(final TextView textView) {
        //setRoomData();
        mMenuView = LayoutInflater.from(ForumStartActActivity.this).inflate(R.layout.choose_time_bottom_pop, null);
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
        Pop.showAtLocation(activityForumStartAct, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    @OnClick({act_start_time_text, act_end_time_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case act_start_time_text:
                ViewUtil.hideInput(this,actStartTimeText);
                showTimePickerPop(actStartTimeText);
                break;
            case act_end_time_text:
                ViewUtil.hideInput(this,actEndTimeText);
                showTimePickerPop(actEndTimeText);
                break;
        }
    }
    private boolean checkInput(){

        if(actTitleText.getText().toString() == null || actTitleText.getText().toString().equals("")){
            MessageUtils.showShortToast(this,"标题不可为空");
            return false;
        }

        if(actStartTimeText.getText().toString() == null || actStartTimeText.getText().toString().equals("")){
            MessageUtils.showShortToast(this,"开始时间不可为空");
            return false;
        }

        if(actEndTimeText.getText().toString() == null || actEndTimeText.getText().toString().equals("")){
            MessageUtils.showShortToast(this,"结束时间不可为空");
            return false;
        }

        if(actAddressText.getText().toString() == null || actAddressText.getText().toString().equals("")){
            MessageUtils.showShortToast(this,"活动地点不可为空");
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (100 == resultCode && requestCode == 101){
            Intent intent = new Intent();
            setResult(5001,intent);
            finish();

        }
    }
}
