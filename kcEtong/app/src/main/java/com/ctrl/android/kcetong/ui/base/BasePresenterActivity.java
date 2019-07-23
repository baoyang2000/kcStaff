package com.ctrl.android.kcetong.ui.base;

import android.os.Bundle;

import com.ctrl.android.kcetong.presenter.IPresenter;

/**
 * Created by Qiu on 2016/4/1.
 */
public abstract class BasePresenterActivity<P extends IPresenter> extends BaseActivity {

    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getPresenterClass() != null) {
                presenter = getPresenterClass().newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract Class<? extends P> getPresenterClass();

}
