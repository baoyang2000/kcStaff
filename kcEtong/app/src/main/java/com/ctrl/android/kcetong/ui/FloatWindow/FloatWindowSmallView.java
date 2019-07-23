package com.ctrl.android.kcetong.ui.FloatWindow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.toolkit.util.JsonParser;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.S;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.ui.MaintabActivity;
import com.ctrl.android.kcetong.ui.activity.CommunityFinanceActivity;
import com.ctrl.android.kcetong.ui.activity.CommunityHomeActivity;
import com.ctrl.android.kcetong.ui.activity.EStewardActivity;
import com.ctrl.android.kcetong.ui.activity.EasyShopAroundActivity;
import com.ctrl.android.kcetong.ui.activity.EngagedServiceActivity;
import com.ctrl.android.kcetong.ui.activity.ExpressActivity;
import com.ctrl.android.kcetong.ui.activity.ForumActivity;
import com.ctrl.android.kcetong.ui.activity.HouseInfoActivity;
import com.ctrl.android.kcetong.ui.activity.MyComplaintActivity;
import com.ctrl.android.kcetong.ui.activity.MyRepairsActivity;
import com.ctrl.android.kcetong.ui.activity.MyVisitActivity;
import com.ctrl.android.kcetong.ui.activity.NoticeListActivity;
import com.ctrl.android.kcetong.ui.activity.PropertyPaymentActivity;
import com.ctrl.android.kcetong.ui.activity.SecondHandActivity;
import com.ctrl.android.kcetong.ui.activity.StationWebActivity;
import com.ctrl.android.kcetong.ui.activity.SurveyListActivity;
import com.ctrl.android.kcetong.ui.activity.ZhidongActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FloatWindowSmallView extends LinearLayout {

	/**
	 * 经度
	 */
	private double longitude ;
	/**
	 * 纬度
	 */
	private double latitude ;
	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int viewHeight;

	/**
	 * 记录系统状态栏的高度
	 */
	 private static int statusBarHeight;

	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;

	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * 记录当前手指位置在屏幕上的横坐标值
	 */
	private float xInScreen;

	/**
	 * 记录当前手指位置在屏幕上的纵坐标值
	 */
	private float yInScreen;

	/**
	 * 记录手指按下时在屏幕上的横坐标的值
	 */
	private float xDownInScreen;

	/**
	 * 记录手指按下时在屏幕上的纵坐标的值
	 */
	private float yDownInScreen;

	/**
	 * 记录手指按下时在小悬浮窗的View上的横坐标的值
	 */
	private float xInView;

	/**
	 * 记录手指按下时在小悬浮窗的View上的纵坐标的值
	 */
	private float yInView;

	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<>();

	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;

	private Toast mToast;
	int ret = 0; // 函数调用返回值

	private Context mContext;

	public FloatWindowSmallView(Context context) {
		super(context);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		View view = findViewById(R.id.small_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
//		TextView percentView = (TextView) findViewById(R.id.percent);
//		percentView.setText(MyWindowManager.getUsedPercentValue(context));

		mContext = context;
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面
		SpeechUtility.createUtility(context, "appid=" + context.getString(R.string.app_id));
		mIat = SpeechRecognizer.createRecognizer(context, mInitListener);

		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		mIatDialog = new RecognizerDialog(context, mInitListener);
		if(AppHolder.getInstance().getBdLocation() != null){
			longitude  = AppHolder.getInstance().getBdLocation().getLongitude();
			latitude = AppHolder.getInstance().getBdLocation().getLatitude();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			// 手指移动的时候更新小悬浮窗的位置
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
			// 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
//				openBigWindow();

				// 移动数据分析，收集开始听写事件
				FlowerCollector.onEvent(mContext, "iat_recognize");

				mIatResults.clear();
				// 设置参数
				setParam();

				// 显示听写对话框
				mIatDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				mIatDialog.setListener(mRecognizerDialogListener);

				mIatDialog.show();
				showTip(mContext.getString(R.string.text_begin));
			}
			break;
		default:
			break;
		}
		return true;
	}

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		String str = resultBuffer.toString();

		if(str.contains("报修") || str.contains("保修") || str.contains("维修") || str.contains("我要报修") || str.contains("投诉报修") ){
			managerStart(MyRepairsActivity.class);
		}else if(str.contains("投诉") || str.contains("投宿") || str.contains("我要投诉")){
			managerStart(MyComplaintActivity.class);
		}else if(str.contains("公告") || str.contains("温馨提示") || str.contains("提示") || str.contains("通知") ){

			startActivity(NoticeListActivity.class);
		}else if(str.contains("活动") || str.contains("家园活动") || str.contains("社区活动") ) {

			startActivity(CommunityHomeActivity.class);
		}else if(str.contains("门禁") || str.contains("开门") || str.contains("智能门禁")){

			managerStart(com.jh.activity.LauncherActivity.class);
		}else if(str.contains("物业缴费") || str.contains("交费") || str.contains("缴费") || str.contains("物业费") ){

			managerStart(PropertyPaymentActivity.class);
		}else if(str.contains("社区调查") || str.contains("调查") || str.contains("问卷") || str.contains("投票")){

			startActivity(SurveyListActivity.class);
		}else if (str.contains("特约服务") || str.contains("服务") ||str.contains("预约服务") ||str.contains("有偿服务")){

			startActivity(EngagedServiceActivity.class);
		}else if(str.contains("社区论坛") || str.contains("论坛") || str.contains("朋友圈") ){
			startActivity(ForumActivity.class);
		}
		else if(str.contains("闲鱼市场") || str.contains("二手市场")|| str.contains("互通有无")){
			startActivity(SecondHandActivity.class);
		}else if(str.contains("房产信息") || str.contains("二手房") || str.contains("出售") || str.contains("出租")){
			startActivity(HouseInfoActivity.class);
		}else if(str.contains("快递") ||str.contains("收快递") ||str.contains("快递代收")){
			managerStart(ExpressActivity.class);
		}else if(str.contains("公交") || str.contains("公交出行") || str.contains("公交车站")){
			bus();
		}else if(str.contains("周边信息") || str.contains("搜索")|| str.contains("周边")|| str.contains("地图")|| str.contains("周围商家")){
			startActivity(EasyShopAroundActivity.class);
		}else if(str.contains("预约访客") || str.contains("访客")){
			managerStart(MyVisitActivity.class);
		}else if(str.contains("指动") || str.contains("外卖") || str.contains("指动生活") ){
			if(!S.isNull(AppHolder.getInstance().getMemberInfo().getUserName())){
				Intent intent = new Intent(mContext, ZhidongActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		}else if(str.contains("管家") || str.contains("e管家") || str.contains("小e管家") || str.contains("诚信行") || str.contains("物业")){
			Intent intent = new Intent(mContext, EStewardActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}else if(str.contains("我的") || str.contains("设置")){
			Intent intent = new Intent(mContext, MaintabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("selectFragmentIndex",3);
			mContext.startActivity(intent);
		}else if(str.contains("金融") || str.contains("理财")|| str.contains("社区金融")){

			if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers()) || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
				if(!S.isNull(AppHolder.getInstance().getHouse().getCommunityId())){
					if(!S.isNull(AppHolder.getInstance().getFinanceUrl())){
						Intent intent = new Intent(mContext, CommunityFinanceActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("url", AppHolder.getInstance().getFinanceUrl());
						mContext.startActivity(intent);
					}else {
						Utils.toastError(mContext, "未获取到社区金融地址");
					}

				}else {
					showTip("请选择社区");
				}
			}else {
				if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
					showTip(mContext.getString(R.string.not_found_owner_information));
				} else {
					if(AppHolder.getInstance().getHouse().getProprietorId() == null){
						Utils.toastError(mContext, mContext.getString(R.string.please_));
					}else {
						if(!S.isNull(AppHolder.getInstance().getFinanceUrl())){
							Intent intent = new Intent(mContext, CommunityFinanceActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("url", AppHolder.getInstance().getFinanceUrl());
							mContext.startActivity(intent);
						}else {
							Utils.toastError(mContext, "未获取到社区金融地址");
						}
					}
				}
			}
		}else if(str.contains("首页") || str.contains("主页") || str.contains("菜单")){
			Intent intent = new Intent(mContext, MaintabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("selectFragmentIndex",0);
			mContext.startActivity(intent);
		}else if(str.contains("退出") || str.contains("关闭")){
			MyWindowManager.removeSmallWindow(mContext);
			Intent intent = new Intent(mContext, FloatWindowService.class);
			mContext.stopService(intent);
		}
		else {
			showTip("未识别语音");
		}

	}

	private void startActivity(Class activity){
		if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers()) || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
			if(!S.isNull(AppHolder.getInstance().getHouse().getCommunityId())){
				Intent intent = new Intent(mContext, activity);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}else {
				showTip("请选择社区");
			}
		}else {
			if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
				showTip(mContext.getString(R.string.not_found_owner_information));
			} else {
				if(AppHolder.getInstance().getHouse().getProprietorId() == null){
					Utils.toastError(mContext, mContext.getString(R.string.please_));
				}else {
					Intent intent = new Intent(mContext, activity);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		}
	}

	private void managerStart(Class activity){
		if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
		   || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
			Utils.showShortToast(mContext, mContext.getString(R.string.manager_cannot));
		}else {
			if (AppHolder.getInstance().getProprietor() == null || S.isNull(AppHolder.getInstance().getProprietor().getProprietorId())) {
				showTip(mContext.getString(R.string.not_found_owner_information));
			} else {
				if(AppHolder.getInstance().getHouse().getProprietorId() == null){
					Utils.toastError(mContext, mContext.getString(R.string.please_));
				}else {
					Intent intent = new Intent(mContext, activity);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		}
	}
	private void bus(){
		if(isAvilible("com.baidu.BaiduMap")){
			Intent i1       = new Intent();
			i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i1.setData(Uri.parse("baidumap://map/place/nearby?query=公交站"));
			mContext.startActivity(i1);
		}
		//未安装，跳转至market下载该程序
		else {
			LLog.d("---------22222222222");
			String location2 =longitude + "," +latitude;
			LLog.d(location2);
			String city = AppHolder.getInstance().getBdLocation().getCity();
			Intent i1   = new Intent(mContext, StationWebActivity.class);
			i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i1.putExtra("url", "http://api.map.baidu.com/place/search?query=公交&location=" + location2+"&radius=1000&region="+ city+"&output=html&src=幸福爱家");
			mContext.startActivity(i1);
		}
	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * 更新小悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**
	 * 打开大悬浮窗，同时关闭小悬浮窗。
	 */
	private void openBigWindow() {
		MyWindowManager.createBigWindow(getContext());
		MyWindowManager.removeSmallWindow(getContext());
	}

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c     = Class.forName("com.android.internal.R$dimen");
				Object   o     = c.newInstance();
				Field    field = c.getField("status_bar_height");
				int      x     = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}
	/**
	 * 参数设置
	 *
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		String mEngineType = SpeechConstant.TYPE_CLOUD;
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, "1");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
	}

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {

			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {

			showTip(error.getPlainDescription(true));
		}

	};
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d("---------", "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 检查手机上是否安装了指定的软件
	 * @param packageName：应用包名
	 * @return b
	 */
	private boolean isAvilible(String packageName){
		//获取packagemanager
		final PackageManager packageManager = mContext.getPackageManager();
		//获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		//用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<>();
		//从pinfo中将包名字逐一取出，压入pName list中
		if(packageInfos != null){
			for(int i = 0; i < packageInfos.size(); i++){
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		//判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}
}
