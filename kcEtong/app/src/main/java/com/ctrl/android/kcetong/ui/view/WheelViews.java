package com.ctrl.android.kcetong.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 类似日期选择空间，滚轮
 */
public class WheelViews extends ScrollView {
	public static final String TAG = "WheelView";
	/**
	 * 存放数据的集合
	 */
	private  List<String> items= null;
	/**
	 * Context
	 */
	private Context context;
//	public static final int OFF_SET_DEFAULT = 1;
	int offset = 1;//OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）

	int displayItemCount; // 每页显示的数量

	int selectedIndex = 1;
	/**
	 * 此线性布局是放置在scollview里面
	 */
	private LinearLayout views;
	int initialY;

	Runnable scrollerTask;
	int newCheck = 50;

	/**选中监听
	 * @author 小米粥
	 * @date 2015-1-15
	 */
	public static class OnWheelViewListener {
		public void onSelected(int selectedIndex, String str) {
		};
	}
	public WheelViews(Context context) {
		super(context);
		init(context);
	}

	public WheelViews(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WheelViews(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}


	public void setItems(List<String> list) {
		if (null == items) {
			items = new ArrayList<String>();
		}
		items.clear();
		views.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			items.add(list.get(i));
		}

		// 前面和后面补全
		for (int i = 0; i < offset; i++) {
			items.add(0, "");
			items.add("");
		}
		initData();

	}
//	public void setItems(List<List<String>> list){
//		if (null == items) {
//			items = new ArrayList<String>();
//		}
//		items.clear();
//		views.removeAllViews();
//		for (int i = 0; i < list.size(); i++) {
//			items.add(list.get(i));
//		}
//
//		// 前面和后面补全
//		for (int i = 0; i < offset; i++) {
//			items.add(0, "");
//			items.add("");
//		}
//		initData();
//	}


	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void scrollTop(){
		scrollTo(0, 0);
	}

	/**
	 * 控件初始化
	 * @param context
	 * @date 2015-1-15
	 */
	private void init(Context context) {
		this.context = context;
		this.setVerticalScrollBarEnabled(false);
		views = new LinearLayout(context);
		views.setOrientation(LinearLayout.VERTICAL);
		this.addView(views);
		scrollerTask = new Runnable() {
			public void run() {
				int newY = getScrollY();
				if (initialY - newY == 0) { // stopped
					final int remainder = initialY % itemHeight;
					final int divided = initialY / itemHeight;
					if (remainder == 0) {
						selectedIndex = divided + offset;
						onSeletedCallBack();
					} else {
						if (remainder > itemHeight / 2) {
							WheelViews.this.post(new Runnable() {
								@Override
								public void run() {
									WheelViews.this.smoothScrollTo(0, initialY
											- remainder + itemHeight);
									selectedIndex = divided + offset + 1;
									onSeletedCallBack();
								}
							});
						} else {
							WheelViews.this.post(new Runnable() {
								@Override
								public void run() {
									WheelViews.this.smoothScrollTo(0, initialY
											- remainder);
									selectedIndex = divided + offset;
									onSeletedCallBack();
								}
							});
						}

					}

				} else {
					initialY = getScrollY();
					WheelViews.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};

	}



	public void startScrollerTask() {

		initialY = getScrollY();
		this.postDelayed(scrollerTask, newCheck);
	}

	/**
	 * 添加数据
	 * @date 2015-1-15
	 */
	private void initData() {
		displayItemCount = offset * 2 + 1;

		for (int i = 0; i < items.size(); i++) {
			views.addView(createView(items.get(i)));

		}

		refreshItemView(0);
	}

	int itemHeight = 0;

	private TextView createView(String item) {
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		tv.setSingleLine(true);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tv.setText(item);
		tv.setGravity(Gravity.CENTER);
		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
						.getDisplayMetrics());
		tv.setPadding(padding, padding, padding, padding);
		if (0 == itemHeight) {
			int height = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
			int width = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
			tv.measure(width, height);
			itemHeight = tv.getMeasuredHeight();
			views.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, itemHeight
							* displayItemCount));
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this
					.getLayoutParams();
			this.setLayoutParams(new LinearLayout.LayoutParams(lp.width,
					itemHeight * displayItemCount));
		}
		return tv;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		refreshItemView(t);

	}

	private void refreshItemView(int y) {
		int position = y / itemHeight + offset;
		int remainder = y % itemHeight;
		int divided = y / itemHeight;

		if (remainder == 0) {
			position = divided + offset;
		} else {
			if (remainder > itemHeight / 2) {
				position = divided + offset + 1;
			}

		}

		int childSize = views.getChildCount();
		for (int i = 0; i < childSize; i++) {
			TextView itemView = (TextView) views.getChildAt(i);
			
			if (null == itemView) {
				return;
			}
			if (position == i) {
				itemView.setTextColor(Color.parseColor("#0288ce"));
			} else {
				itemView.setTextColor(Color.parseColor("#bbbbbb"));
			}
		}
	}

	/**
	 * 获取选中区域的边界
	 */
	int[] selectedAreaBorder;

	private int[] obtainSelectedAreaBorder() {
		if (null == selectedAreaBorder) {
			selectedAreaBorder = new int[2];
			selectedAreaBorder[0] = itemHeight * offset;
			selectedAreaBorder[1] = itemHeight * (offset + 1);
		}
		return selectedAreaBorder;
	}

	Paint paint;
	int viewWidth;

	@SuppressWarnings("deprecation")
	@Override
	public void setBackgroundDrawable(Drawable background) {

		if (viewWidth == 0) {
			viewWidth = ((Activity) context).getWindowManager()
					.getDefaultDisplay().getWidth();
		}

		if (null == paint) {
			paint = new Paint();
			paint.setColor(Color.parseColor("#83cde6"));
			paint.setStrokeWidth((int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_SP, 1, getResources()
					.getDisplayMetrics()));
		}

		background = new Drawable() {
			@Override
			public void draw(Canvas canvas) {
				canvas.drawLine(viewWidth * 1 / 6,	
						obtainSelectedAreaBorder()[0], viewWidth * 5 / 6,
						obtainSelectedAreaBorder()[0], paint);
				canvas.drawLine(viewWidth * 1 / 6,
						obtainSelectedAreaBorder()[1], viewWidth * 5 / 6,
						obtainSelectedAreaBorder()[1], paint);
			}

			@Override
			public void setAlpha(int alpha) {

			}

			@Override
			public void setColorFilter(ColorFilter cf) {

			}

			@Override
			public int getOpacity() {

				return PixelFormat.UNKNOWN;//0
			}
		};

		super.setBackgroundDrawable(background);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		setBackgroundDrawable(null);
	}

	/**
	 * 选中回调
	 */
	private void onSeletedCallBack() {
		if (null != onWheelViewListener) {
			onWheelViewListener.onSelected(selectedIndex,
					items.get(selectedIndex));
		}

	}

	public void setSeletion(int position) {
		final int p = position;
		selectedIndex = p + offset;
		this.post(new Runnable() {
			@Override
			public void run() {
				WheelViews.this.smoothScrollTo(0, p * itemHeight);
			}
		});

	}

	public String getSeletedItem() {
		return items.get(selectedIndex);
	}

	public int getSeletedIndex() {
		return selectedIndex - offset;
	}

	@Override
	public void fling(int velocityY) {
		super.fling(velocityY / 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {

			startScrollerTask();
		}
		return super.onTouchEvent(ev);
	}

	private OnWheelViewListener onWheelViewListener;

	public OnWheelViewListener getOnWheelViewListener() {
		return onWheelViewListener;
	}

	public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
		this.onWheelViewListener = onWheelViewListener;
	}

}
