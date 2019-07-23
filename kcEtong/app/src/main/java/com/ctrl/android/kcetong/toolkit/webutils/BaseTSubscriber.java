package com.ctrl.android.kcetong.toolkit.webutils;

import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Qiu on 2016/7/25.
 */
public abstract class BaseTSubscriber<T> extends Subscriber<T> {
    private final WeakReference<BaseActivity> mRef;

    public BaseTSubscriber(BaseActivity context) {
        super();
        mRef = new WeakReference<>(context);
    }

    public void onNetError(Throwable e) {
        e.printStackTrace();
        if (mRef != null && mRef.get() != null) {
            if (e.getMessage() == null) {

            } else if (e.getMessage().contains("Failed to connect") || e.getMessage().contains("failed to connect") || e.getMessage().contains("SocketTimeoutException")) {
                Utils.toastError(mRef.get(), "服务器连接超时,请检查网络");
            } else if ("Invalid index 0, size is 0".equals(e.getMessage())) {//连接超时

            } else if (e.getMessage().contains("Unable to resolve host")) {//网络访问错误
                Utils.toastError(mRef.get(), "网络请求错误");
            } else {
                Utils.toastError(mRef.get(), "网络请求错误");
            }
        }
    }

    public void onNetCallback(T t) {

    }

    public abstract void onResponseCallback(JSONObject response, String resultCode);

    @Override
    public void onCompleted() {
        if (mRef != null && mRef.get() != null) {
            mRef.get().dismissProgressDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mRef != null && mRef.get() != null) {
            mRef.get().dismissProgressDialog();
        }
        onNetError(e);
    }

    @Override
    public void onNext(T t) {
        if (t instanceof ResponseBody) {
            ResponseBody mBody = (ResponseBody) t;
            try {
                String json = mBody.string();
                JSONObject jsonObject = new JSONObject(json);
                String resultCode = jsonObject.optString("code");
                onResponseCallback(jsonObject, resultCode);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            onNetCallback(t);
        }
    }
}
