package com.ctrl.android.kcetong.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.listener.OngetTimesLinstener;
import com.ctrl.android.kcetong.model.CityModel;
import com.ctrl.android.kcetong.model.DistrictModel;
import com.ctrl.android.kcetong.model.ProvinceModel;
import com.ctrl.android.kcetong.toolkit.util.AddressData;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.view.WheelViews;

import java.util.ArrayList;
import java.util.List;

public class AddressDialog extends Dialog {
	/**
	 * 所有省
	 */
	protected List<String> mProvinceDatas;
	protected List<String> mCitisDatas;
	protected List<String> mDistrictDatas;
	protected String mCurrentProviceName;// 当前省的名称
	protected String mCurrentCityName;// 当前市的名称
	protected String mCurrentDistrictName = ""; // 当前区的名称
	private WheelViews mViewProvince;
	private WheelViews mViewCity;
	private WheelViews mViewDistrict;
	private Activity            mContext     = null;
	private View                parentView   = null;
	private String              proName      = "山东省";
	private String              cityName     = "济南市";
	private String              disName      = "历下区";
	private String              proId        = "370000";
	private String              cityId       = "370100";
	private String              disId        = "370102";
	private OngetTimesLinstener linstener    = null;
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
	private List<CityModel>     cityList     = new ArrayList<CityModel>();
	private List<DistrictModel> districtList = new ArrayList<DistrictModel>();
	private AddressData         addressdata  = new AddressData();

	public AddressDialog(Activity context) {
		super(context, R.style.transparentFrameWindowStyle);
		mContext = context;

	}

	public void setLinstener(OngetTimesLinstener linstener) {
		this.linstener = linstener;
	}

	public static int dip2px(float dip, Context context) {
		DisplayMetrics me = context.getResources().getDisplayMetrics();
		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dip, me);
		return margin;
	}

	/**
	 * 设置监听器并show 转为 设置城市使用
	 *
	 * @date 2015-1-14
	 */
	public void showDialogForTiems() {
		if (provinceList.size() < 2) {
			provinceList = addressdata.addressdata(mContext);
		}
		loaddate();
	}

	private void initView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				Utils.getDisplayWidth(mContext) / 3, LayoutParams.WRAP_CONTENT);
		if (parentView == null) {
			parentView = getLayoutInflater().inflate(
					R.layout.dialog_business_address, null);
		}
		proName = "山东省";
		cityName = "济南市";
		disName = "历下区";
		proId = "370000";
		cityId = "370100";
		disId = "370102";
		if (mViewDistrict == null) {
			mViewProvince = (WheelViews) parentView
					.findViewById(R.id.id_province);
			mViewCity = (WheelViews) parentView.findViewById(R.id.id_city);
			mViewDistrict = (WheelViews) parentView
					.findViewById(R.id.id_district);
			mViewProvince.setLayoutParams(params);
			mViewCity.setLayoutParams(params);
			mViewDistrict.setLayoutParams(params);
		}
		Button sub = (Button) parentView.findViewById(R.id.btn_sub);
		Button cancel = (Button) parentView.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		sub.setOnClickListener(new MyOnclick());
		mViewProvince.setOffset(1);
		mViewProvince.setItems(mProvinceDatas);
		mViewProvince.scrollTop();
		mViewProvince
				.setOnWheelViewListener(new WheelViews.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						proName = item;
						proId = provinceList.get(selectedIndex - 1).getId();
						changeProvince(selectedIndex - 1);
					}
				});
		mViewCity.setOffset(1);
		mViewCity.setItems(mCitisDatas);
		mViewCity.scrollTop();
		mViewCity.setOnWheelViewListener(new WheelViews.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				cityName = item;
				cityId = cityList.get(selectedIndex - 1).getId();
				changeCity(selectedIndex - 1);
			}
		});
		mViewDistrict.setOffset(1);
		mViewDistrict.setItems(mDistrictDatas);
		mViewDistrict.scrollTop();
		mViewDistrict
				.setOnWheelViewListener(new WheelViews.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						disName = item;
						disId = districtList.get(selectedIndex - 1).getId();
					}
				});
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
		window.setWindowAnimations(R.style.main_menu_animstyle);
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
	 *
	 * @author 小米粥
	 * @date 2015-1-15
	 */
	public class MyOnclick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (linstener != null) {
				linstener.getString("{'address':'" + proName + cityName
						+ disName +"','proName':'"+proName+"','cityName':'"+cityName+ "','disName':'"+disName+"','proId':'" + proId + "','cityId':'"
						+ cityId + "','disId':'" + disId + "'}");
			}
		}

	}

	private void loaddate() {
		mProvinceDatas = new ArrayList<String>();
		if (provinceList == null || provinceList.size() < 1) {
			return;
		}
		for (int i = 0; i < provinceList.size(); i++) {
			// 遍历所有省的数据
			mProvinceDatas.add(provinceList.get(i).getName());

		}
		mCitisDatas = new ArrayList<String>();
		cityList = provinceList.get(0).getCityList();

		for (int j = 0; j < cityList.size(); j++) {
			// 遍历所有市的数据
			mCitisDatas.add(cityList.get(j).getName());

		}
		mDistrictDatas = new ArrayList<String>();
		districtList = cityList.get(0).getDistrictList();

		for (int k = 0; k < districtList.size(); k++) {
			// 遍历所有省的数据
			mDistrictDatas.add(districtList.get(k).getName());

		}
		initView();
	}

	/**

	 *Created by  赵昌星
	 *2015-4-10
	 *下午4:17:12
	 *@Description: TODO方法的作用：改变省级列表数据
	 *  @param i
	 */
	private void changeProvince(int i) {
		mCitisDatas = new ArrayList<String>();
		cityList = provinceList.get(i).getCityList();

		cityId = cityList.get(0).getId();
		cityName =cityList.get(0).getName();

		for (int j = 0; j < cityList.size(); j++) {
			// 遍历所有市的数据
			mCitisDatas.add(cityList.get(j).getName());
		}
		mViewCity.setItems(mCitisDatas);
		mViewCity.setOffset(1);
		mViewCity.scrollTop();
		mDistrictDatas = new ArrayList<String>();

		districtList = cityList.get(0).getDistrictList();

		for (int k = 0; k < districtList.size(); k++) {
			// 遍历所有省的数据
			mDistrictDatas.add(districtList.get(k).getName());
		}
		mViewDistrict.setItems(mDistrictDatas);
		mViewDistrict.setOffset(1);
		mViewDistrict.scrollTop();
		changeCity(0);

	}

	/**
	 改变市级列表
	 *Created by  赵昌星
	 *2015-4-10
	 *下午4:17:39
	 *@Description: TODO方法的作用：
	 *  @param i
	 */
	private void changeCity(int i) {
		districtList = cityList.get(i).getDistrictList();

		disId = districtList.get(0).getId();
		disName = districtList.get(0).getName();

		mDistrictDatas = new ArrayList<String>();
		for (int k = 0; k < districtList.size(); k++) {
			// 遍历所有省的数据
			mDistrictDatas.add(districtList.get(k).getName());
		}
		mViewDistrict.setItems(mDistrictDatas);
		mViewDistrict.setOffset(1);
		mViewDistrict.scrollTop();
	}
}
