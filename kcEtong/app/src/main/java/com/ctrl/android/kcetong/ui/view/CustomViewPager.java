package com.ctrl.android.kcetong.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * @项目名称: 诚信行<br>
 * @类描述: 重写viewpager实现不滑动<br>
 * @创建人： whs<br>
 * @创建时间： 2016/12/29 14:40 <br>
 * @修改人： <br>
 * @修改时间: 2016/12/29 14:40 <br>
 */

public class CustomViewPager extends ViewPager {

    private boolean noScroll = false;//默认正常滑动
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    public CustomViewPager(Context context) {
        super(context);
    }
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
