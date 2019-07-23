package com.ctrl.android.kcetong.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.TitleViewListener;

/**
 * 顶部导航栏自定义view
 * @author Qiu  2016/11/14
 */
public class TitleView extends LinearLayout {
    private Context mContext;
    private LayoutInflater mInflater;
    private Button mLeftButton;//左边控件Button
    private TextView mTitleText;//中间控件TextView
    private Button mRightOneButton;//右边第一个控件Button
    private Button mRightTwoButton;//右边第二个控件Button
    private TitleViewListener listener;
    private RelativeLayout rootView;

    static final String TAG = "TitleBar";
    private String leftVisibility = "visible";
    private String rightVisibility = "visible";
    public TitleView(Context context) {
        super(context);
        mContext = context;

        mInflater = LayoutInflater.from(mContext);
        View mTitleBar = mInflater.inflate(R.layout.title_view, this, true);

        mLeftButton = (Button) mTitleBar.findViewById(R.id.left_button);
        mTitleText = (TextView) mTitleBar.findViewById(R.id.title_text);
        mRightOneButton = (Button) mTitleBar.findViewById(R.id.right_one_button);
        rootView = (RelativeLayout)mTitleBar.findViewById(R.id.rootView);

        mLeftButton.setOnClickListener(new ButtonClickListener());
        mRightOneButton.setOnClickListener(new ButtonClickListener());
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs) {
        mContext = ctx;

        mInflater = LayoutInflater.from(mContext);
        View mTitleBar = mInflater.inflate(R.layout.title_view, this, true);

        mLeftButton = (Button) mTitleBar.findViewById(R.id.left_button);
        mTitleText = (TextView) mTitleBar.findViewById(R.id.title_text);
        mRightOneButton = (Button) mTitleBar.findViewById(R.id.right_one_button);
        mRightTwoButton = (Button) mTitleBar.findViewById(R.id.right_two_button);

        rootView = (RelativeLayout)mTitleBar.findViewById(R.id.rootView);
        mLeftButton.setOnClickListener(new ButtonClickListener());
        mRightOneButton.setOnClickListener(new ButtonClickListener());
        mRightTwoButton.setOnClickListener(new ButtonClickListener());

        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TitleView);
        int aCount = a.getIndexCount();
        for (int i = 0; i < aCount; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TitleView_bg_color:
                    rootView.setBackgroundColor(a.getColor(R.styleable.TitleView_bg_color, getResources().getColor(R.color.tv_title_color)));
                    break;
                case R.styleable.TitleView_center_text:
                    mTitleText.setText(a.getString(R.styleable.TitleView_center_text));
                    break;
                case R.styleable.TitleView_center_text_color:
                    mTitleText.setTextColor(a.getColor(R.styleable.TitleView_center_text_color, getResources().getColor(R.color.tab_bar_unselect)));
                    break;
                case R.styleable.TitleView_left_visibility:
                    leftVisibility = a.getString(R.styleable.TitleView_left_visibility);
                    if (leftVisibility.equals("visible"))
                    {
                        mLeftButton.setVisibility(View.VISIBLE);
                    }else if (leftVisibility.equals("invisible")){
                        mLeftButton.setVisibility(View.INVISIBLE);
                    }else if (leftVisibility.equals("gone"))
                    {
                        mLeftButton.setVisibility(View.GONE);
                    }
                    break;
                case R.styleable.TitleView_right_visibility:
                    rightVisibility = a.getString(R.styleable.TitleView_right_visibility);
                    if (rightVisibility.equals("visible"))
                    {
                        mRightOneButton.setVisibility(View.VISIBLE);
                        mRightTwoButton.setVisibility(View.VISIBLE);
                    }else if (rightVisibility.equals("invisible")){
                        mRightOneButton.setVisibility(View.INVISIBLE);
                        mRightTwoButton.setVisibility(View.INVISIBLE);
                    }else if (rightVisibility.equals("gone"))
                    {
                        mRightOneButton.setVisibility(View.GONE);
                        mRightTwoButton.setVisibility(View.GONE);
                    }
                    break;
                case R.styleable.TitleView_left_bg:
                    mLeftButton.setBackgroundResource(a.getResourceId(R.styleable.TitleView_left_bg, R.drawable.back_select));
                    break;
                case R.styleable.TitleView_right_bg:
                    mRightOneButton.setBackgroundResource(a.getResourceId(R.styleable.TitleView_right_bg, R.drawable.back_select));
                    mRightTwoButton.setBackgroundResource(a.getResourceId(R.styleable.TitleView_right_bg, R.drawable.back_select));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    /**
     * 获得左右按钮控件以设置属性
     * @param op 0:左按钮 1:右按钮 2:右二按钮
     */
    public Button getTitleButton(int op)
    {
        if (op==0)
        {
            return mLeftButton;
        }else if (op == 1)
        {
            return mRightOneButton;
        }else if(op == 2){
            return mRightTwoButton;
        }
        else {
            return null;
        }
    }

    /**
     * 必须设置TitleViewListener，否则点击事件不生效
     * @param listener
     */
    public void setOnClickListener(TitleViewListener listener)
    {
        this.listener = listener;
    }

    class ButtonClickListener implements OnClickListener
    {

        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if(listener!=null)
            {
                listener.onTitleClick(v);
            }
        }
    }
    /**
     * 设置中间TextView标题
     * @param titleText
     */
    public void setTitleText(String titleText)
    {
        mTitleText.setText(titleText);
    }

    /**
     * 设置按钮显示字
     * @param right true:设置右侧按钮字体
     * 	   		  false:设置左侧按钮字体
     * @param text 将要设置的字
     */
    public void setButtonText(boolean right, String text)
    {
        if (right)
        {
            mRightOneButton.setText(text);
        }else
        {
            mLeftButton.setText(text);
        }
    }
    /**
     * @param isLeftShow 是否隐藏左边button
     * @param isRightShow 是否隐藏右边button
     * @param leftRid 左边button背景图片设置，-1表示不发生变化
     * @param rightRid 右边button背景图片设置，-1表示不发生变化
     * @param titleText 标题字体设置
     */
    public void setTitleProperty(boolean isLeftShow, boolean isRightShow, int leftRid, int rightRid, String titleText)
    {
        if (isLeftShow)
        {
            mLeftButton.setVisibility(View.VISIBLE);
        }else {
            mLeftButton.setVisibility(View.INVISIBLE);
        }
        if (isRightShow)
        {
            mRightOneButton.setVisibility(View.VISIBLE);
        }else
        {
            mRightOneButton.setVisibility(View.INVISIBLE);
        }
        if (leftRid > 0)
        {
            mLeftButton.setBackgroundResource(leftRid);
        }
        if (rightRid > 0)
        {
            mRightOneButton.setBackgroundResource(rightRid);
        }
        if (titleText!=null)
        {
            mTitleText.setText(titleText);
        }
    }
}
