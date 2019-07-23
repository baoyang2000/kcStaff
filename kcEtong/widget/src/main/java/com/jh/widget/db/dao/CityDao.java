package com.jh.widget.db.dao;

import android.content.Context;
import android.os.Environment;

import com.jh.widget.bean.City;
import com.jh.widget.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDao {
    private static CityDao instance;
    public static ArrayList<EventHandler> mListeners = new ArrayList<>();
    private static final String FORMAT = "^[a-z,A-Z].*$";
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<City>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private CityDB mCityDB;
    private List<City> mCityList;
    private boolean isCityListComplite;

    public static CityDao instance() {
        if (instance == null) {
            synchronized (CityDao.class) {
                if (instance == null) {
                    instance = new CityDao();
                }
            }
        }
        return instance;
    }

    private CityDB openCityDB(Context context) {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.jh" + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        if (!db.exists()) {
            // L.i("db is not exists");
            try {
                InputStream is = context.getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(context, path);
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public List<String> getSections() {
        return mSections;
    }

    public Map<String, List<City>> getMap() {
        return mMap;
    }

    public List<Integer> getPositions() {
        return mPositions;
    }

    public Map<String, Integer> getIndexer() {
        return mIndexer;
    }

    public boolean isCityListComplite() {
        return isCityListComplite;
    }


    public void initCityList(Context context) {
        mCityList = new ArrayList<>();
        mSections = new ArrayList<>();
        mMap = new HashMap<>();
        mPositions = new ArrayList<>();
        mIndexer = new HashMap<>();
        mCityDB = openCityDB(context);// 这个必须最先复制完,所以我放在单线程中处理
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                isCityListComplite = false;
                prepareCityList();

                isCityListComplite = true;
                if (mListeners.size() > 0) {// 通知接口完成加载
                    for (EventHandler handler : mListeners) {
                        handler.onCityComplite();
                    }
                }
            }
        }).start();
    }

    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
        for (City city : mCityList) {
            String firstName = city.getFirstPY();// 第一个字拼音的第一个字母
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(city);
                } else {
                    mSections.add(firstName);
                    List<City> list = new ArrayList<>();
                    list.add(city);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(city);
                } else {
                    mSections.add("#");
                    List<City> list = new ArrayList<>();
                    list.add(city);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
        return true;
    }

    public interface EventHandler {
        void onCityComplite();
    }
}
