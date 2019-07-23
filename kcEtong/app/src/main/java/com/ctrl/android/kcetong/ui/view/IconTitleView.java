package com.ctrl.android.kcetong.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.android.kcetong.listener.TitleViewListener;

/**
 * 顶部导航栏自定义view
 * 
 * @author Qiu
 */
public class IconTitleView extends LinearLayout {
	private Context           mContext;
	private LayoutInflater    mInflater;
	private ImageButton       mLeftButton;// 左边控件Button
	private TextView          mTitleText;// 中间控件TextView
	private ImageButton       mRightButton;// 右边控件Button
	private ImageButton       mNearRightButton;// 次右边控件
	private TitleViewListener listener;
	private RelativeLayout    rootView;
	private LinearLayout      title_layout;
	static final String TAG = "IconTitleView";
	private String leftVisibility = "visible";
	private String rightVisibility = "visible";

	public IconTitleView(Context context) {
		super(context);
		mContext = context;

		mInflater = LayoutInflater.from(mContext);
		View mTitleBar = mInflater
				.inflate(com.ctrl.android.kcetong.R.layout.icon_title_view, this, true);

		mLeftButton = (ImageButton) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.left_button);
		mTitleText = (TextView) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.title_text);
		mRightButton = (ImageButton) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.right_button);
		mNearRightButton = (ImageButton) mTitleBar
				.findViewById(com.ctrl.android.kcetong.R.id.near_right_button);
		rootView = (RelativeLayout) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.rootView);
		title_layout = (LinearLayout) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.title_layout);
		mLeftButton.setOnClickListener(new ButtonClickListener());
		mRightButton.setOnClickListener(new ButtonClickListener());
		mNearRightButton.setOnClickListener(new ButtonClickListener());
	}

	public IconTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context, attrs);
	}

	private void init(Context ctx, AttributeSet attrs) {
		mContext = ctx;

		mInflater = LayoutInflater.from(mContext);
		View mTitleBar = mInflater
				.inflate(com.ctrl.android.kcetong.R.layout.icon_title_view, this, true);

		mLeftButton = (ImageButton) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.left_button);
		mTitleText = (TextView) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.title_text);
		mRightButton = (ImageButton) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.right_button);
		mNearRightButton = (ImageButton) mTitleBar
				.findViewById(com.ctrl.android.kcetong.R.id.near_right_button);

		rootView = (RelativeLayout) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.rootView);
		title_layout = (LinearLayout) mTitleBar.findViewById(com.ctrl.android.kcetong.R.id.title_layout);
		mLeftButton.setOnClickListener(new ButtonClickListener());
		mRightButton.setOnClickListener(new ButtonClickListener());
		mNearRightButton.setOnClickListener(new ButtonClickListener());

		TypedArray a = ctx.obtainStyledAttributes(attrs,
				com.ctrl.android.kcetong.R.styleable.IconTitleView);
		int aCount = a.getIndexCount();
		for (int i = 0; i < aCount; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_ibg_color:
				rootView.setBackgroundColor(a.getColor(
						com.ctrl.android.kcetong.R.styleable.IconTitleView_ibg_color, getResources()
								.getColor(com.ctrl.android.kcetong.R.color.tv_title_color)));
				break;
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_icenter_text:
				mTitleText.setText(a
						.getString(com.ctrl.android.kcetong.R.styleable.IconTitleView_icenter_text));
				break;
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_icenter_text_color:
				mTitleText.setTextColor(a.getColor(
						com.ctrl.android.kcetong.R.styleable.IconTitleView_icenter_text_color,
						getResources().getColor(com.ctrl.android.kcetong.R.color.tab_bar_unselect)));
				break;
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_ileft_visibility:
				leftVisibility = a
						.getString(com.ctrl.android.kcetong.R.styleable.IconTitleView_ileft_visibility);
				if (leftVisibility.equals("visible")) {
					mLeftButton.setVisibility(View.VISIBLE);
				} else if (leftVisibility.equals("invisible")) {
					mLeftButton.setVisibility(View.INVISIBLE);
				} else if (leftVisibility.equals("gone")) {
					mLeftButton.setVisibility(View.GONE);
				}
				break;
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_iright_visibility:
				rightVisibility = a
						.getString(com.ctrl.android.kcetong.R.styleable.IconTitleView_iright_visibility);
				if (rightVisibility.equals("visible")) {
					mRightButton.setVisibility(View.VISIBLE);
				} else if (rightVisibility.equals("invisible")) {
					mRightButton.setVisibility(View.INVISIBLE);
				} else if (rightVisibility.equals("gone")) {
					mRightButton.setVisibility(View.GONE);
				}
				break;
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_ileft_src:
				mLeftButton.setImageResource(a.getResourceId(
						com.ctrl.android.kcetong.R.styleable.IconTitleView_ileft_src,
						com.ctrl.android.kcetong.R.drawable.back_select));
				break;
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_iright_src:
				mRightButton.setImageResource(a.getResourceId(
						com.ctrl.android.kcetong.R.styleable.IconTitleView_iright_src,
						com.ctrl.android.kcetong.R.drawable.back_select));
				break;
			case com.ctrl.android.kcetong.R.styleable.IconTitleView_inear_right_src:
				mNearRightButton.setVisibility(View.VISIBLE);
				mNearRightButton.setImageResource(a.getResourceId(
						com.ctrl.android.kcetong.R.styleable.IconTitleView_inear_right_src,
						com.ctrl.android.kcetong.R.drawable.back_select));
				break;
			default:
				break;
			}
		}
		a.recycle();
	}

	/**
	 * 获得左右按钮控件以设置属性
	 * 
	 * @param op
	 *            0:左按钮 1:右按钮 2:次右边按钮
	 */
	public ImageButton getTitleButton(int op) {
		if (op == 0) {
			return mLeftButton;
		} else if (op == 1) {
			return mRightButton;
		} else if (op == 2) {
			return mNearRightButton;
		} else {
			return null;
		}
	}

	/**
	 * 必须设置TitleViewListener，否则点击事件不生效
	 */
	public void setOnClickListener(TitleViewListener listener) {
		this.listener = listener;
	}

	class ButtonClickListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (listener != null) {
				listener.onTitleClick(v);
			}
		}
	}

	/**
	 * 设置中间TextView标题
	 */
	public void setTitleText(String titleText) {
		mTitleText.setText(titleText);
	}

	/**
	 * 设置Title居中
	 */
	public void setTitleCenterInParent() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);;
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		title_layout.setLayoutParams(params);
	}

	/**
	 * @param isLeftShow
	 *            是否隐藏左边button
	 * @param isRightShow
	 *            是否隐藏右边button
	 * @param leftRid
	 *            左边button背景图片设置，-1表示不发生变化
	 * @param rightRid
	 *            右边button背景图片设置，-1表示不发生变化
	 * @param titleText
	 *            标题字体设置
	 */
	public void setTitleProperty(boolean isLeftShow, boolean isRightShow,
			int leftRid, int rightRid, String titleText) {
		if (isLeftShow) {
			mLeftButton.setVisibility(View.VISIBLE);
		} else {
			mLeftButton.setVisibility(View.INVISIBLE);
		}
		if (isRightShow) {
			mRightButton.setVisibility(View.VISIBLE);
		} else {
			mRightButton.setVisibility(View.INVISIBLE);
		}
		if (leftRid > 0) {
			mLeftButton.setBackgroundResource(leftRid);
		}
		if (rightRid > 0) {
			mRightButton.setBackgroundResource(rightRid);
		}
		if (titleText != null) {
			mTitleText.setText(titleText);
		}
	}
}
