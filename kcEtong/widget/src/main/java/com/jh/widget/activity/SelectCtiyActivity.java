package com.jh.widget.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jh.widget.R;
import com.jh.widget.adapter.CityAdapter;
import com.jh.widget.adapter.SearchCityAdapter;
import com.jh.widget.bean.City;
import com.jh.widget.db.dao.CityDao;
import com.jh.widget.view.BladeView;
import com.jh.widget.view.PinnedHeaderListView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23.
 */

public class SelectCtiyActivity extends AppCompatActivity implements TextWatcher,
        View.OnClickListener, CityDao.EventHandler {
    private static final String TAG = "SelectCtiyActivity";
    private EditText mSearchEditText;
    // private Button mCancelSearchBtn;
    private ImageButton mClearSearchBtn;
    private View mCityContainer;
    private View mSearchContainer;
    private PinnedHeaderListView mCityListView;
    private BladeView mLetter;
    private ListView mSearchListView;
    private List<City> mCities;
    private SearchCityAdapter mSearchCityAdapter;
    private CityAdapter mCityAdapter;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<City>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;
    private InputMethodManager mInputMethodManager;

    private Handler handler = new ServiceHandler(this);

    @SuppressLint("HandlerLeak")
    private class ServiceHandler extends Handler {
        WeakReference<SelectCtiyActivity> s;

        public ServiceHandler(SelectCtiyActivity selectCtiyActivity) {
            s = new WeakReference<>(selectCtiyActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SelectCtiyActivity selectCtiyActivity = s.get();
            if (selectCtiyActivity == null) {
                return;
            }

            mCities = CityDao.instance().getCityList();
            mSections = CityDao.instance().getSections();
            mMap = CityDao.instance().getMap();
            mPositions = CityDao.instance().getPositions();
            mIndexer = CityDao.instance().getIndexer();

            mCityAdapter = new CityAdapter(SelectCtiyActivity.this, mCities, mMap,
                    mSections, mPositions);
            mLetter.setVisibility(View.VISIBLE);
            mCityListView.setAdapter(mCityAdapter);
            mCityListView.setOnScrollListener(mCityAdapter);
            mCityListView.setPinnedHeaderView(LayoutInflater.from(
                    SelectCtiyActivity.this).inflate(
                    R.layout.biz_plugin_weather_list_group_item, mCityListView,
                    false));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biz_plugin_weather_select_city);
        CityDao.mListeners.add(this);
        initView();
        initData();
    }

    private void initView() {

        mSearchEditText = (EditText) findViewById(R.id.search_edit);
        mSearchEditText.addTextChangedListener(this);
        mClearSearchBtn = (ImageButton) findViewById(R.id.ib_clear_text);
        mClearSearchBtn.setOnClickListener(this);

        mCityContainer = findViewById(R.id.city_content_container);
        mSearchContainer = findViewById(R.id.search_content_container);
        mCityListView = (PinnedHeaderListView) findViewById(R.id.citys_list);
        mCityListView.setEmptyView(findViewById(R.id.citys_list_empty));
        mLetter = (BladeView) findViewById(R.id.citys_bladeview);
        mLetter.setOnItemClickListener(new BladeView.OnItemClickListener() {
            @Override
            public void onItemClick(String s) {
                if (mIndexer.get(s) != null) {
                    mCityListView.setSelection(mIndexer.get(s));
                }
            }
        });
        mLetter.setVisibility(View.GONE);
        mSearchListView = (ListView) findViewById(R.id.search_list);
        mSearchListView.setEmptyView(findViewById(R.id.search_empty));
        mSearchContainer.setVisibility(View.GONE);
        mSearchListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                mInputMethodManager.hideSoftInputFromWindow(
                        mSearchEditText.getWindowToken(), 0);
                return false;
            }
        });
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.i(TAG, mCityAdapter.getItem(position).toString());
                startActivity(mCityAdapter.getItem(position));
            }
        });

        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.i(TAG, mSearchCityAdapter.getItem(position).toString());
                startActivity(mSearchCityAdapter.getItem(position));
            }
        });
    }

    private void startActivity(City city) {
        Intent i = new Intent();
        i.putExtra("city", city);
        setResult(1, i);
        finish();
    }

    public void initData() {
        CityDao.instance().initCityList(SelectCtiyActivity.this);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (CityDao.instance().isCityListComplite()) {
            mCities = CityDao.instance().getCityList();
            mSections = CityDao.instance().getSections();
            mMap = CityDao.instance().getMap();
            mPositions = CityDao.instance().getPositions();
            mIndexer = CityDao.instance().getIndexer();

            mCityAdapter = new CityAdapter(SelectCtiyActivity.this, mCities,
                    mMap, mSections, mPositions);
            mCityListView.setAdapter(mCityAdapter);
            mCityListView.setOnScrollListener(mCityAdapter);
            mCityListView.setPinnedHeaderView(LayoutInflater.from(
                    SelectCtiyActivity.this).inflate(
                    R.layout.biz_plugin_weather_list_group_item, mCityListView,
                    false));
            mLetter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mSearchCityAdapter = new SearchCityAdapter(SelectCtiyActivity.this,
                mCities);
        mSearchListView.setAdapter(mSearchCityAdapter);
        mSearchListView.setTextFilterEnabled(true);
        if (mCities.size() < 1 || TextUtils.isEmpty(s)) {
            mCityContainer.setVisibility(View.VISIBLE);
            mSearchContainer.setVisibility(View.INVISIBLE);
            mClearSearchBtn.setVisibility(View.GONE);
        } else {
            mClearSearchBtn.setVisibility(View.VISIBLE);
            mCityContainer.setVisibility(View.INVISIBLE);
            mSearchContainer.setVisibility(View.VISIBLE);
            mSearchCityAdapter.getFilter().filter(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // 如何搜索字符串长度为0，是否隐藏输入法
        // if(TextUtils.isEmpty(s)){
        // mInputMethodManager.hideSoftInputFromWindow(
        // mSearchEditText.getWindowToken(), 0);
        // }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int i = v.getId();
        if (i == R.id.ib_clear_text) {
            if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                mSearchEditText.setText("");
                mInputMethodManager.hideSoftInputFromWindow(
                        mSearchEditText.getWindowToken(), 0);
            }

        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        CityDao.mListeners.remove(this);
    }

    @Override
    public void onCityComplite() {
        // 城市列表加载完的回调函数
        handler.sendEmptyMessage(1);
    }
}

