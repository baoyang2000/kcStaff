package com.ctrl.android.kcetong.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ctrl.android.kcetong.listener.OngetTimesLinstener;
import com.ctrl.android.kcetong.listener.ZBindingShopTimesListener;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.view.WheelViews;

import java.util.List;

public class SelectorDialog extends Dialog {
	private Activity mContext = null;
	private WheelViews mWheelView = null;
	private View parentView = null;

	private String                    strName   = null;
	private OngetTimesLinstener       linstener = null;
	private ZBindingShopTimesListener zListener = null;
	/**兼容中高档，此参数为中高档餐厅ID*/
	private String                    shopId    = null;
	public SelectorDialog(Activity context) {
		super(context, com.ctrl.android.kcetong.R.style.transparentFrameWindowStyle);
		mContext = context;

	}
	/**
	 * 外卖选择时间
	 * @param linstener
	 */
	public void setLinstener(OngetTimesLinstener linstener) {
		this.linstener = linstener;
		this.zListener = null;
	}
	/**
	 * 中高档选择时间
	 * @param zListener
	 * @param shopId
	 */
	public void setZListener(ZBindingShopTimesListener zListener, String shopId)
	{
		this.zListener = zListener;
		this.shopId = shopId;
		this.linstener = null;
	}
	
	/**
	 * 设置监听器并show 转为 设置城市使用
	 * @date 2015-1-14
	 */
	public void showDialogForTiems(List<String> mplace) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (parentView == null) {
			parentView = getLayoutInflater().inflate(
					com.ctrl.android.kcetong.R.layout.dialog_times_layout, null);
		}
		if (mWheelView == null) {
			mWheelView = (WheelViews) parentView
					.findViewById(com.ctrl.android.kcetong.R.id.view_whellview_times);
			mWheelView.setLayoutParams(params);
		}

		Button sub = (Button) parentView.findViewById(com.ctrl.android.kcetong.R.id.btn_sub);
		Button cancel = (Button) parentView.findViewById(com.ctrl.android.kcetong.R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		if (mplace!=null && mplace.size()>0)//默认打开选中第一个
		{
			strName = mplace.get(0);
		}
		sub.setOnClickListener(new MyOnclick());
		mWheelView.setOffset(1);
		mWheelView.setItems(mplace);
		mWheelView.scrollTop();
		mWheelView.setOnWheelViewListener(new WheelViews.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				strName = item;
			}
		});

		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, Utils.dip2px(300, mContext));
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
	 */
	public class MyOnclick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (linstener != null) {
				linstener.getString(strName);
			}
			if (zListener!=null)
			{
				zListener.onSelectedTime(shopId==null?"":shopId, strName);
			}
		}

	}

}
