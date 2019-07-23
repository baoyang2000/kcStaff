package com.ctrl.android.kcetong.toolkit.webutils;


import android.util.Log;

import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Qiu on 2016/7/25.
 */
public class RetrofitUtil {

    /**
     * 服务地址 根路径 的配置 - Eric -
     */
    public static final String URL = "http://192.144.144.237:8999/PM";//地址
//    public static final String URL = "http://192.168.200.140:8888/pm";//测试地址

    public static final String baseUrl = URL + "/";

    public static final String zhiDongUrl = "http://h5.zhidong.cn/zhidongH5/html/index.html?tel=";
    //    public static final String imageServerHost = "http://www.zdlife.net/";
    public static Retrofit retrofit;


    public static APIService Api() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(httpClient).build();
        }
        APIService service = retrofit.create(APIService.class);
        return service;
    }

    static OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LLog.d(request.url() + "\nrequest:" + request.toString());
            Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LLog.d("\nresponse body:" + content);
            return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
        }
    }).build();

    public static HashMap<String, String> produceParams(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
            params.put("overType", "1");//Type 1: android
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        try {
            json = mapper.writeValueAsString(params);
            Log.d("[request json data=] \n", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("data", json);
        LLog.d("------data={" + json + "}");
        return map;
    }
}