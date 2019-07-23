package com.ctrl.android.kcetong;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.ctrl.android.kcetong.listener.HttpResponseHandler;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.iflytek.cloud.SpeechUtility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.bugly.Bugly;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by Qiu on 2016/7/14.
 */
public class CustomApplication extends Application {
    public static ImageLoader mImageLoader = ImageLoader.getInstance();

    public static BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }

    private static BDLocation location;

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(CustomApplication.this, "appid=" + getString(R.string.app_id));

        super.onCreate();
        SDKInitializer.initialize(this);
        ConstantsData.ScreenWidth = Utils.getDisplayWidth(this);
        ConstantsData.ScreenHeight = Utils.getDisplayHeight(this);
        ConstantsData.ViewFlowHeight = Utils.dip2px(getResources().getDimension(R.dimen.ViewFlowHeight), this);
        initImageLoader(this);
        Bugly.init(getApplicationContext(), "02b30aee61", true);//建议在测试阶段建议设置成true，发布时设置为false。
    }

    /**
     * 设置banner图片
     *
     * @param url      图片的地址
     * @param view     图片要显示的控件
     * @param listener 图片加载监听回调
     */
    public static void loadImageForBanner(String url, ImageView view, ImageLoadingListener listener) {
        if (url == null || url.equals("")) {
            url += "http://www.none";
        }
        setImageWithDiffDisplayImageOptions(url, view, listener, mBannerOptions);
    }

    public static DisplayImageOptions mBannerOptions = new DisplayImageOptions.Builder()
            // .showImageOnLoading(R.drawable.default_image)
            // 图片加载的时候显示的图片
            .showImageForEmptyUri(R.drawable.default_image)
            // 图片加载地址为空的时候显示的图片
            .showImageOnFail(R.drawable.default_image)
            // 图片加载失败的时候显示的图片
            .cacheInMemory(true).cacheOnDisk(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    /**
     * 设置图片
     *
     * @param url      图片的地址
     * @param view     图片要显示的控件
     * @param listener 图片加载监听回调
     */
    public static void setImageWithDiffDisplayImageOptions(String url,
                                                           ImageView view, ImageLoadingListener listener,
                                                           DisplayImageOptions options) {
        if (url == null || url.equals("")) {
            return;
        }
        if (!url.contains("http")) {
            if (url.startsWith("/")) {
                url = url.substring(1, url.length());
            }
            url = RetrofitUtil.baseUrl + url;
        }
        boolean hasListener = false;
        if (listener == null) {
            hasListener = false;
        } else {
            hasListener = true;
        }
        if (hasListener) {
            mImageLoader.displayImage(url, view, options, listener);
        } else {
            mImageLoader.displayImage(url, view, options);
        }
    }

    /**
     * 初始化设置ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheSize(3 * 1024 * 1024)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
        com.nostra13.universalimageloader.utils.L.disableLogging();
    }

    /* http请求客户端 */
    private static AsyncHttpClient mClient = null;

    /**
     * 请求
     *
     * @param context  上下文对象
     * @param response 返回数据对象
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static AsyncHttpClient useHttp(final Activity context,
                                          final RequestParams params, final String URL,
                                          final HttpResponseHandler response) throws JSONException,
            UnsupportedEncodingException {

        if (mClient == null) {
            mClient = new AsyncHttpClient();
        }
        if (!Utils.isNetWorkConnected(context)) {

            Utils.toastError(context, R.string.network_error);
            if (response.response != null) {
                response.response.dataError(0, null);
            }
            return mClient;
        }
        mClient.setMaxRetriesAndTimeout(0, 10000);
        mClient.getHttpClient().getParams()
                .setParameter("http.socket.timeout", Integer.valueOf(30000));
        mClient.setTimeout(10000);
        if (params != null) {
            mClient.post(URL, params, response);
        } else {
            mClient.get(context, URL, response);
        }

        return mClient;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile) {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws
     */
    public boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void delFileData(String file) {
        File data = getFileStreamPath(file);
        data.delete();
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     */
    public Serializable readObject(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}


