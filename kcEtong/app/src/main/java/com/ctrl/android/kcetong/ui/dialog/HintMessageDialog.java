package com.ctrl.android.kcetong.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.HintDialogListener;
import com.ctrl.android.kcetong.listener.SetheadListener;

/**
 * Created by Administrator on 2016/10/31.
 */

public class HintMessageDialog extends Dialog {
    private static final String TAG = "HintMessageDialog";

    private Button btn_cancel;
    private Button btn_submit;
    private Button btn_camera;
    private TextView tv_content;
    private TextView tv_hint_title;
    private TextView tv_phone;
    private Context context;
    private EditText tv_hintEidt;// 支付密码
    private EditText tv_hintEdit2;// 支付密码输入确认

    /**
     * 系统回调方法，初始化dialog
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        // lp.alpha = 0.80f;
        lp.alpha = 1.0f;
        this.getWindow().setAttributes(lp);

    }

    /**
     * 构造方法，可以传入theme的id
     *
     * @param context
     * @param theme
     */
    public HintMessageDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    /**
     * 构造方法
     *
     * @param context
     */
    public HintMessageDialog(Context context) {
        super(context, R.style.CommonDialog);
        this.context = context;
    }

    /**
     * 对话框显示
     *
     * @param content 要显示的提示内容
     * @param phone   打电话时候的电话号码 没有传null
     * @Description: TODO方法的作用：
     */
    public void showHintDialog(String content, String phone,
                               final HintDialogListener mHintDialogListener) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_hint, null);

        if (tv_content == null) {
            tv_content = (TextView) parentView
                    .findViewById(R.id.tv_hintContent);
        }
        if (tv_phone == null) {
            tv_phone = (TextView) parentView.findViewById(R.id.tv_callphone);
        }
        if (phone == null) {
            tv_phone.setVisibility(View.GONE);
        } else {
            tv_phone.setText(phone);
        }
        if (content != null) {
            tv_content.setText(content);
        }
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mHintDialogListener != null) {
                    mHintDialogListener.cancelListener();
                }

            }
        });
        // 确定按钮
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mHintDialogListener != null) {
                    mHintDialogListener.submitListener();
                }

            }
        });
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        parentView.setLayoutParams(mLayoutParams);
        setContentView(parentView);
        // setShowPosition();

        setCanceledOnTouchOutside(true);
        show();
    }

    /**
     * 对话框显示 Created by 赵昌星 2015-3-13 上午10:11:00
     *
     * @Description: TODO方法的作用：
     */
    public void showHintDialogForSetHead(final SetheadListener mSetheadListener) {

        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_sethead, null);

        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.cancelListener();
                }

            }
        });
        // 相册按钮
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_photo);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.submitListener();
                }

            }
        });
        // 相机按钮
        if (btn_camera == null) {
            btn_camera = (Button) parentView.findViewById(R.id.btn_cameraTou);
        }

        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.btnCameraListener();
                }

            }
        });
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        parentView.setLayoutParams(mLayoutParams);
        setContentView(parentView);
        setShowPosition();
        setCanceledOnTouchOutside(true);
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
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay()
                .getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
    }

    /**
     * update Qiu
     *
     * @param content 要显示的提示内容
     * @param btnText 按钮字
     * @Description: 显示内容只有一个TextView
     */
    public void showHintDialogForTips(String content,
                                      final HintDialogListener mSetheadListener, String btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_hint_tips, null);
        if (tv_content == null) {
            tv_content = (TextView) parentView
                    .findViewById(R.id.tv_hintContent);
            if (content != null)
                tv_content.setText(content);
        }
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.submitListener();
                }
            }
        });
        setContentView(parentView);
        setCanceledOnTouchOutside(true);
        show();
    }

    /**
     * update Qiu
     *
     * @param content 要显示的提示内容
     * @Description: 显示内容只有一个TextView
     */
    public void showHintDialogForSetHead(String content,
                                         final HintDialogListener mSetheadListener, String... btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_hint_stillpay, null);
        if (tv_content == null) {
            tv_content = (TextView) parentView
                    .findViewById(R.id.tv_hintContent);
            if (content != null)
                tv_content.setText(content);
        }
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.cancelListener();
                }
            }
        });
        // 相册按钮
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.submitListener();
                }
            }
        });

        if (btnText.length > 1) {
            btn_cancel.setText(btnText[0]);
            btn_submit.setText(btnText[1]);
        }

        setContentView(parentView);
        // setShowPosition();

        setCanceledOnTouchOutside(true);
        show();
    }

    /**
     * 存在支付密码，输入支付密码验证 update Qiu
     *
     * @Description: 显示输入支付密码的布局
     */
    /*public void showHintDialogForInputPassword(
            final HintInputDialogListener mSetheadListener, String... btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_hint_input_password, null);
        Button forgetPassword = (Button) parentView.findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(context, FindPassWordActivity.class);
                intent.putExtra("isPayPassBack", true);
                context.startActivity(intent);
            }
        });
        if (tv_hintEidt == null) {
            tv_hintEidt = (EditText) parentView.findViewById(R.id.tv_hintEidt);
        }
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.cancelListener();
                }
            }
        });
        // 相册按钮
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String password = tv_hintEidt.getText().toString();
                if (password == null || password.trim().equals("")) {
                    Utils.toastError(context, "请输入您的支付密码后提交");
                    return;
                }
                if (mSetheadListener != null) {
                    mSetheadListener.submitListener(password);
                }
            }
        });

        if (btnText.length > 1) {
            btn_cancel.setText(btnText[0]);
            btn_submit.setText(btnText[1]);
        }

        setContentView(parentView);
        // setShowPosition();

        setCanceledOnTouchOutside(true);
        show();
    }*/

    /**
     * 倒计时
     */
    /*class HintCountDownTimer extends CountDownTimer {
        private Button ed_verification_code;

        public void setObjectButton(Button button) {
            this.ed_verification_code = button;
        }

        public HintCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (ed_verification_code != null && HintMessageDialog.this != null
                    && HintMessageDialog.this.isShowing()) {
                ed_verification_code.setText((millisUntilFinished / 1000)
                        + "秒重新获取");
                ed_verification_code.setEnabled(false);
                ed_verification_code
                        .setBackgroundResource(R.drawable.register_againacquires);
            }
        }

        @Override
        public void onFinish() {
            if (ed_verification_code != null && HintMessageDialog.this != null
                    && HintMessageDialog.this.isShowing()) {
                ed_verification_code.setEnabled(true);
                ed_verification_code
                        .setBackgroundResource(R.drawable.register_acquires);
                ed_verification_code.setText("获取验证码");
            }
        }

    }*/

    /**
     * @Description: 显示输入支付密码的布局
     * @author Qiu
     * @deprecated 用户存在绑定手机号码不存在支付密码
     */
    /*public void showHintDialogForSettingPayPassword(
            final HintInputDialogListener mSetheadListener, String... btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_setting_pay_password, null);
        if (tv_hintEidt == null) {
            tv_hintEidt = (EditText) parentView.findViewById(R.id.tv_hintEidt);
        }
        if (tv_hintEdit2 == null) {
            tv_hintEdit2 = (EditText) parentView
                    .findViewById(R.id.tv_hintEidt2);
        }
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.cancelListener();
                }
            }
        });
        // 相册按钮
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String password = tv_hintEidt.getText().toString();
                String password2 = tv_hintEdit2.getText().toString();
                if (password == null || password.trim().equals("")
                        || password.length() < 6 || password.length() > 20
                        || !password.matches(Constant.REGEX)
                        || password.matches(Constant.REGEX2)) {
                    Utils.toastError(context, "请输入6-16位数字和字母密码");
                    return;
                } else if (!password.equals(password2)) {
                    Utils.toastError(context, "两次输入的密码不一致");
                    return;
                }
                if (mSetheadListener != null) {
                    mSetheadListener.submitListener(password);
                }
            }
        });

        if (btnText.length > 1) {
            btn_cancel.setText(btnText[0]);
            btn_submit.setText(btnText[1]);
        }

        setContentView(parentView);
        // setShowPosition();

        setCanceledOnTouchOutside(true);
        show();
    }*/

    /**
     * 普通用户存在绑定手机号码不存在支付密码
     *
     * @Description: 显示输入支付密码的布局
     * @author Qiu
     */
    /*public void showHintDialogForSettingPhonePassword(
            final Hint3rdPhonePaypwdDialogListener mSetheadListener, final String phoneNumber,
            String... btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_setting_3rdphonepay_password, null);
        if (tv_hintEidt == null) {
            tv_hintEidt = (EditText) parentView.findViewById(R.id.tv_hintEidt);
        }
        if (tv_hintEdit2 == null) {
            tv_hintEdit2 = (EditText) parentView
                    .findViewById(R.id.tv_hintEidt2);
        }
        final EditText ed_inputPhone = (EditText) parentView
                .findViewById(R.id.ed_inputPhone);
        ed_inputPhone.setText(phoneNumber);
        ed_inputPhone.setEnabled(false);
        final EditText ed_verification_code = (EditText) parentView
                .findViewById(R.id.ed_verification_code);
        Button btn_verification = (Button) parentView
                .findViewById(R.id.btn_verification);
        final HintCountDownTimer timer = new HintCountDownTimer(60000, 1000);
        timer.setObjectButton(btn_verification);
        btn_verification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSetheadListener != null) {
                    String mobile = ed_inputPhone.getText().toString().trim();
                    if (mobile == null || mobile.length() <= 0
                            || !Utils.isMobileNO(mobile)) {
                        Utils.toastError(context, "请输入正确的手机号码");
                        return;
                    }
                    mSetheadListener.sendRancode(mobile);
                    timer.start();
                }
            }
        });
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    mSetheadListener.cancel();
                }
            }
        });
        // 相册按钮
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String mobile = ed_inputPhone.getText().toString().trim();
                String rancode = ed_verification_code.getText().toString()
                        .trim();
                String password = tv_hintEidt.getText().toString();
                String password2 = tv_hintEdit2.getText().toString();
                if (mobile == null || mobile.length() <= 0
                        || !Utils.isMobileNO(mobile)) {
                    Utils.toastError(context, "请输入正确的手机号码");
                    return;
                } else if (rancode == null || rancode.length() <= 0) {
                    Utils.toastError(context, "请输入验证码");
                    return;
                } else if (password == null || password.trim().equals("")
                        || password.length() < 6 || password.length() > 20
                        || !password.matches(Constant.REGEX)
                        || password.matches(Constant.REGEX2)) {
                    Utils.toastError(context, "请输入6-16位数字和字母密码");
                    return;
                } else if (!password.equals(password2)) {
                    Utils.toastError(context, "两次输入的支付密码不一致");
                    return;
                }
                if (mSetheadListener != null) {
                    mSetheadListener.submit(mobile, rancode, password);
                }
            }
        });

        if (btnText.length > 1) {
            btn_cancel.setText(btnText[0]);
            btn_submit.setText(btnText[1]);
        }

        setContentView(parentView);
        // setShowPosition();

        setCanceledOnTouchOutside(true);
        show();
    }*/

    /**
     * 第三方用户不存在绑定手机号码不存在支付密码 update Qiu
     *
     * @Description: 显示输入支付密码的布局
     */
    /*public void showHintDialogForSetting3rdPhonePassword(
            final Hint3rdPhonePaypwdDialogListener mSetheadListener,
            String... btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_setting_3rdphonepay_password, null);
        if (tv_hintEidt == null) {
            tv_hintEidt = (EditText) parentView.findViewById(R.id.tv_hintEidt);
        }
        if (tv_hintEdit2 == null) {
            tv_hintEdit2 = (EditText) parentView
                    .findViewById(R.id.tv_hintEidt2);
        }
        final EditText ed_inputPhone = (EditText) parentView
                .findViewById(R.id.ed_inputPhone);
        final EditText ed_verification_code = (EditText) parentView
                .findViewById(R.id.ed_verification_code);
        Button btn_verification = (Button) parentView
                .findViewById(R.id.btn_verification);
        final HintCountDownTimer timer = new HintCountDownTimer(60000, 1000);
        timer.setObjectButton(btn_verification);
        btn_verification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSetheadListener != null) {
                    String mobile = ed_inputPhone.getText().toString().trim();
                    if (mobile == null || mobile.length() <= 0
                            || !Utils.isMobileNO(mobile)) {
                        Utils.toastError(context, "请输入正确的手机号码");
                        return;
                    }
                    mSetheadListener.sendRancode(mobile);
                    timer.start();
                }
            }
        });
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    mSetheadListener.cancel();
                }
            }
        });
        // 相册按钮
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String mobile = ed_inputPhone.getText().toString().trim();
                String rancode = ed_verification_code.getText().toString()
                        .trim();
                String password = tv_hintEidt.getText().toString();
                String password2 = tv_hintEdit2.getText().toString();
                if (mobile == null || mobile.length() <= 0
                        || !Utils.isMobileNO(mobile)) {
                    Utils.toastError(context, "请输入正确的手机号码");
                    return;
                } else if (rancode == null || rancode.length() <= 0) {
                    Utils.toastError(context, "请输入验证码");
                    return;
                } else if (password == null || password.trim().equals("")
                        || password.length() < 6 || password.length() > 20
                        || !password.matches(Constant.REGEX)
                        || password.matches(Constant.REGEX2)) {
                    Utils.toastError(context, "请输入6-16位数字和字母密码");
                    return;
                } else if (!password.equals(password2)) {
                    Utils.toastError(context, "两次输入的支付密码不一致");
                    return;
                }
                if (mSetheadListener != null) {
                    mSetheadListener.submit(mobile, rancode, password);
                }
            }
        });

        if (btnText.length > 1) {
            btn_cancel.setText(btnText[0]);
            btn_submit.setText(btnText[1]);
        }

        setContentView(parentView);
        // setShowPosition();

        setCanceledOnTouchOutside(true);
        show();
    }*/

    /**
     * 校园ceo/楼长对话框
     *
     * @param content 要显示的提示内容
     * @Description: 显示内容只有一个TextView
     */
    public void showDialogForSchoolCEO(String content,
                                       final HintDialogListener mSetheadListener, String... btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_school_ceo, null);
        if (tv_content == null) {
            tv_content = (TextView) parentView
                    .findViewById(R.id.tv_hintContent);
            if (content != null)
                tv_content.setText(content);
        }
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.cancelListener();
                }
            }
        });
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.submitListener();
                }
            }
        });
        if (btnText.length > 1) {
            btn_cancel.setText(btnText[0]);
            btn_submit.setText(btnText[1]);
        }
        setContentView(parentView);
        setCanceledOnTouchOutside(true);
        show();
    }

    /**
     * 两个按钮的提示框
     *
     * @param title   (提示)
     * @param content 要显示的提示内容
     * @Description: 显示内容只有一个TextView
     */
    public void showDefaultDialogVariableButtons(String title, String content,
                                                 final HintDialogListener mSetheadListener, String... btnText) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_integral_confirm_exchange, null);

        if (tv_hint_title == null) {
            tv_hint_title = (TextView) parentView
                    .findViewById(R.id.tv_hint_title);
            if (title != null && !title.equals("")) {
                tv_hint_title.setText(title);
            }
        }

        if (tv_content == null) {
            tv_content = (TextView) parentView
                    .findViewById(R.id.tv_hintContent);
            if (content != null)
                tv_content.setText(content);
        }
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.cancelListener();
                }
            }
        });
        if (btn_submit == null) {
            btn_submit = (Button) parentView.findViewById(R.id.btn_submit);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.submitListener();
                }
            }
        });
        if (btnText.length > 1) {
            btn_cancel.setText(btnText[0]);
            btn_submit.setText(btnText[1]);
        } else {
            btn_cancel.setVisibility(View.GONE);
        }
        setContentView(parentView);
        setCanceledOnTouchOutside(true);
        show();
    }

    /**
     * 一个按钮的提示框
     *
     * @param title   (提示)
     * @param content 要显示的提示内容
     * @Description: 显示内容只有一个TextView
     */
    public void showDefaultDialogOneButton(String title, String content,
                                           final HintDialogListener mSetheadListener, String btnText, boolean... ifContentLeft) {
        View parentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_integral_exchange_tip, null);
        if (tv_hint_title == null) {
            tv_hint_title = (TextView) parentView
                    .findViewById(R.id.tv_hint_title);
            if (title != null && !title.equals("")) {
                tv_hint_title.setText(title);
            }
        }
        if (tv_content == null) {
            tv_content = (TextView) parentView
                    .findViewById(R.id.tv_hintContent);
            if (content != null)
                tv_content.setText(content);
        }
        if (ifContentLeft.length > 0) {
            if (ifContentLeft[0]) {
                tv_content.setGravity(Gravity.LEFT);
            }
        }
        // 取消按钮
        if (btn_cancel == null) {
            btn_cancel = (Button) parentView.findViewById(R.id.btn_cancel);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mSetheadListener != null) {
                    mSetheadListener.cancelListener();
                }
            }
        });
        btn_cancel.setText(btnText);
        setContentView(parentView);
        setCanceledOnTouchOutside(true);
        show();
    }
}
