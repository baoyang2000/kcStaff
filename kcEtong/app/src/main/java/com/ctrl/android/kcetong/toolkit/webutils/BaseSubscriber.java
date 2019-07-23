package com.ctrl.android.kcetong.toolkit.webutils;


import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import java.lang.ref.WeakReference;

import rx.Subscriber;

/**
 * Created by Qiu on 2016/7/25.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private final WeakReference<BaseActivity> mRef;
    private String                            apiUrl;

    public BaseSubscriber(BaseActivity context, String apiUrl) {
        super();
        mRef = new WeakReference<BaseActivity>(context);
        this.apiUrl = apiUrl;
    }

    public void onCompleted(String apiUrl) {

    }

    public void onError(Throwable e, String apiUrl) {
        e.printStackTrace();
        if (mRef != null && mRef.get() != null) {
            if (e.getMessage() == null) {

            } else if (e.getMessage().contains("Failed to connect") || e.getMessage().contains("failed to connect")) {
                Utils.toastError(mRef.get(), "服务器连接超时,请检查网络");
            } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {

            } else {
                Utils.toastError(mRef.get(), (e.getMessage() != null && !"".equals(e.getMessage())) ? e.getMessage() : "网络异常,请检查网络");
            }
        }
    }

    public abstract void onNetBack(T t, String apiUrl);

    @Override
    public void onCompleted() {
        if (mRef != null && mRef.get() != null) {
            mRef.get().dismissProgressDialog();
        }
        onCompleted(apiUrl);
    }

    @Override
    public void onError(Throwable e) {
        if (mRef != null && mRef.get() != null) {
            mRef.get().dismissProgressDialog();
        }
        onError(e, apiUrl);
    }

    @Override
    public void onNext(T t) {
        onNetBack(t, apiUrl);
    }
}
