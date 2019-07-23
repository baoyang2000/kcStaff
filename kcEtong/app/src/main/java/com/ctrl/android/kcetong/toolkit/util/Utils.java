package com.ctrl.android.kcetong.toolkit.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.PermissionChecker;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @data 2015年3月4日
 */
public class Utils {

    /**
     * startActivtityForResult 登录界面时的requestcode
     */
    public static final int RequstCodeForLogin              = 0x11;
    /**
     * 重新定位或者选择定位地址
     */
    public static final int RequestCodeGotoPositionPage     = 0x19;
    /**
     * 申请权限拨打电话
     */
    public static final int RequstCodeForCALL_PHONE         = 0x11;
    /**
     * 申请定位权限
     */
    public static final int RequstCodeForPermissionLocation = 0x12;
    /**
     * 申请读写文件权限
     */
    public static final int RequstCodeForPermissionStorage  = 0x13;
    /**
     * 申请读写文件权限
     */
    public static final int requestCameraPermissions  = 0x28;
    /**
     * 申请录音权限
     */
    public static final int requestRecorderPermissions  = 0x29;
    /**
     * 订单->去支付页面requestCode
     */
    public static final int RequestCodeGotoPayPage          = 0x13;
    /**
     * 默认收货地址
     */
    public static final int RequstCodeForDefaultAddress     = 0x14;

    /**
     * 新增收货地址
     */
    public static final int RequstCodeForAddAddress = 0x15;

    /**
     * 购物车到编辑购物车
     */
    public static final int RequstCodeForShopCar             = 0x16;
    /**
     * 购物车到核对订单
     */
    public static final int RequstCodeForShopCarToCheckOrder = 0x17;
    /**
     * 我的钱包
     */
    public static final int RequstCodeForMineCenterToWallet  = 0x18;

    /**
     * 提现
     */
    public static final int RequstCodeForWallettoWithDraw = 0x19;

    /**
     * 充值
     */
    public static final int RequstCodeForWallettoDeposit = 0x20;
    /**
     * 订单详情
     */
    public static final int RequstCodeForOrderDetail     = 0x21;
    /**
     * 订单评价
     */
    public static final int RequstCodeForOrderComment    = 0x22;

    /**
     * 订单退款
     */
    public static final int RequstCodeForOrderRefound = 0x23;

    /**
     * 核对订单地址
     */
    public static final int RequstCodeForShopcarAddress   = 0x24;
    /**
     * 核对订单地址
     */
    public static final int RequstCodeForNewAccount       = 0x25;
    /**
     * //充值requestCode
     */
    public static final int requestCodeForPayment         = 0x15;
    /**
     * 银行Web支付
     */
    public static final int RequestCodeGotoBankPayWebPage = 0x18;

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][345678]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * @bref 匹配短信中间的6个数字（验证码等）
     */
    public static String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        String  patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
        Pattern p            = Pattern.compile(patternCoder);
        Matcher matcher      = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * @param activity
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 获得userID
     */
//    public static String getUserId(Context context) {
//        UserInfo info = Utils.getUserLogin(context);
//        if (info == null) {
//            return "";
//        } else {
//            String uid = info.getUserId();
//            if (uid == null || uid.equals("")) {
//                return "";
//            }
//            return uid;
//        }
//    }

    /**
     * @bref 获取屏幕宽度
     */
    public static int getDisplayWidthItem(Context context) {
        DisplayMetrics me     = context.getResources().getDisplayMetrics();
        int            width  = me.widthPixels;
        int            margin = dip2px(32, context);
        int            item   = (width - margin) / 3;
        return item;
    }

    /**
     * @bref 获取屏幕宽度
     */
    public static int getDisplayWidth(Context context) {
        DisplayMetrics me    = context.getResources().getDisplayMetrics();
        int            width = me.widthPixels;
        return width;
    }

    /**
     * @bref 获取屏幕高度
     */
    public static int getDisplayHeight(Context context) {
        DisplayMetrics me     = context.getResources().getDisplayMetrics();
        int            height = me.heightPixels;
        return height;
    }

    /**
     * @bref 获取屏幕密度
     */
    public static float getDisplayDensity(Context context) {
        DisplayMetrics me     = context.getResources().getDisplayMetrics();
        float          height = me.density;
        return height;
    }

    /**
     * @bref dip转化px
     */
    public static int dip2px(float dip, Context context) {
        DisplayMetrics me = context.getResources().getDisplayMetrics();
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, me);
        return margin;
    }

    /**
     * 获取控件上的字符串
     */
    public static String getText(View view) {
        String content = "";
        if (view instanceof EditText) {
            content = ((EditText) view).getText().toString();
        } else if (view instanceof TextView) {
            content = ((TextView) view).getText().toString();
        }
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        return content.trim();
    }

    public static Toast mStaticToast;

    /**
     * 提示错误信息
     *
     * @param context
     * @param str     提示内容字符串 Created by 赵昌星
     * @data 2015年3月4日
     */
    public static void toastError(Context context, String str) {
        if (mStaticToast == null) {
            mStaticToast = Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_SHORT);
        } else {
            mStaticToast.setText(str);
        }
        mStaticToast.show();
    }

    /**
     * 提示错误信息
     *
     * @param context
     * @param strId   文字资源id Created by 赵昌星
     * @data 2015年3月4日
     */
    public static void toastError(Context context, int strId) {
        Toast.makeText(context, strId, Toast.LENGTH_SHORT).show();

    }

    /**
     * 字符检测
     *
     * @param str 用户输入的字符
     * @return
     * @throws PatternSyntaxException Created by 赵昌星
     * @data 2015年3月4日
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String  regEx = "[-`_~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p     = Pattern.compile(regEx);
        Matcher m     = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 去除各种空白
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 返回距离发布的时间
     */
    public static String getDateChage(String inputTime, int type) {
        try {
            String           ret      = "";
            SimpleDateFormat sdf      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date             date     = sdf.parse(inputTime);
            Calendar         calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            System.out.println(month);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            System.out.println(day);
            int    temphour    = calendar.get(Calendar.HOUR_OF_DAY);
            int    tempminutes = calendar.get(Calendar.MINUTE);
            String hour        = "";
            String minutes     = "";
            if (temphour < 10) {
                hour = "0" + temphour;
            } else {
                hour = String.valueOf(temphour);
            }
            if (tempminutes < 10) {
                minutes = "0" + tempminutes;
            } else {
                minutes = String.valueOf(tempminutes);
            }
            long     create = sdf.parse(inputTime).getTime();
            Calendar now    = Calendar.getInstance();
            long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
            long ms_now = now.getTimeInMillis();
            if (ms_now - create < ms) {

                if ((ms_now - create) / (1000 * 60) < 60) {
                    ret = (ms_now - create) / (1000 * 60) + "分钟前";
                    if (ret.equals("0分钟前")) {
                        return "刚刚";
                    } else {
                        return ret = (ms_now - create) / (1000 * 60) + "分钟前";
                    }
                } else {
                    return ret = (ms_now - create) / (1000 * 60 * 60) + "小时前";
                }
                // ret = hour + ":" + minutes;
            } else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
                ret = "昨天 " + hour + ":" + minutes;
            } else if (ms_now - create < (ms + 24 * 3600 * 1000 * 2)) {
                ret = "前天 " + hour + ":" + minutes;
            } else {
                if (type == 1) {
                    ret = month + "月" + day + "日 " + hour + ":" + minutes;
                } else {
                    ret = month + "月" + day + "日 ";
                }
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取最后一个字符,并且过滤是否为符号
     */
    public static String getLastStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Matcher m = Pattern.compile(".*?([\u4E00-\u9FA5])(?:[^\u4E00-\u9FA5])*$").matcher(str);
        if (m.find()) return m.group(1);
        m = Pattern.compile(".*?(\\w)\\W*$").matcher(str);
        return m.find() ? m.group(1) : str.substring(str.length() - 1);
    }

    /**
     * 设置已退款不同样式text
     */
    public static SpannableStringBuilder getRefundedTextHighlight(String money) {

        String                 part1       = "已退款";
        String                 part2       = "(" + money + "已退至余额，";
        String                 part3       = "查看余额";
        String                 part4       = ")";
        String                 text        = (part1 + part2 + part3 + part4);
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(text);
        spanBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, part1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, part1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanBuilder.setSpan(new ForegroundColorSpan(Color.RED), text.length() - part3.length() - 1, text.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanBuilder;
    }

    /**
     * 计算文件夹大小
     */
    public static double getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long   dirSize = 0;
        File[] files   = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 如果遇到目录则通过递归调用继续统计
            }
        }
        double size = (dirSize + 0.0) / (1024 * 1024);

        return size;
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return true;
        else return false;
    }

    /**
     * 获取当前应用所在的SD卡的文件夹
     *
     * @return
     * @date 2015-1-10
     */
    public static File getAppFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/fingerlife");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;

    }

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey   = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 解析百度推送的tag
     */
    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags         = new ArrayList<String>();
        int          indexOfComma = originalText.indexOf(',');
        String       tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    /**
     * 检查本机是否包含包名
     */
    public static boolean isInstallApkWithPackageName(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) return true;
        }
        return false;
    }

    /**
     * 获取应用的版本号
     *
     * @return
     * @date 2015-1-13
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断网络连接环境 true wifi flase gprs
     *
     * @param context
     * @return
     * @data 2015年3月4日
     */
    @SuppressWarnings("unused")
    public static int isWifiConnection(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();

        // 一、判断网络是否是wifi，在判断之前一定要进行的非空判断，如果没有任何网络
        // 连接
        // ConnectivityManager.TYPE_WIF//WiFi连接
        // ConnectivityManager.Type.MODIFY//GPRS连接
        // ConnectivityManager.TYPE_BLUETOOTH//蓝牙连接
        // ConnectivityManager.TYPE_DUMMY//
        if (info != null) {
            return info.getType();
        } else {
            return -1;
        }
    }

    /**
     * 清理用户信息
     *
     * @param context
     * @date 2015-1-15
     */
    @SuppressWarnings("static-access")
    public static void deteleUserLogin(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("user", context.MODE_PRIVATE);
        Editor ditor = mPreferencesInfo.edit();
        ditor.clear();
        ditor.apply();
    }


    /**
     * 清除shared用户信息
     *
     * @param context
     */
    public static void clearUserLoginInfo(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Editor ditor = mPreferencesInfo.edit();
        ditor.clear().commit();
    }

    /**
     * 清除shared用户上次登录信息
     *
     * @param context
     */
    public static void clearUserLastUsedAddress(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        mPreferencesInfo.edit().putString("address", "").commit();
    }

    /**
     * /**
     * 清除shared用户搜索结果
     */
    public static void clearHomeSearchResult(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        mPreferencesInfo.edit().putString("homeSearchList", "").commit();
    }

    /**
     * 持久化保存的区域信息
     */
    public static void saveSharedSelectedCityArea(Context context, String area) {
        if (area == null) {
            return;
        }
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        Editor ditor = mPreferencesInfo.edit();
        ditor.putString("cityArea", area);
        ditor.commit();
    }

    /**
     * 获取本地保存的区域信息
     */
    public static String getSelectedCityArea(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        String searchString = mPreferencesInfo.getString("cityArea", "");
        return searchString;
    }

    /**
     * 持久化首页搜索记录
     */
    public static void saveSharedSearchList(Context context, ArrayList<String> resultList) {
        if (resultList == null || resultList.size() == 0) {
            return;
        }
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        Editor ditor = mPreferencesInfo.edit();

        JSONArray  jsonArray  = new JSONArray(resultList);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("homeSearchListKey", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ditor.putString("homeSearchList", jsonObject.toString());
        ditor.commit();
    }

    /**
     * 获取本地首页搜索结果
     */
    public static ArrayList<String> getHomeSearchArrayList(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        String searchString = mPreferencesInfo.getString("homeSearchList", "");
        if (searchString == null || searchString.equals("")) {
            return null;
        }
        try {
            JSONObject object  = new JSONObject(searchString);
            JSONArray  jsonArr = object.optJSONArray("homeSearchListKey");
            if (jsonArr == null || jsonArr.length() == 0) return null;
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                list.add(jsonArr.optString(i));
            }
            return list;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 持久化是否接收推送消息
     */
    public static void savePushAble(boolean isPush, Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", context.MODE_PRIVATE);
        Editor ditor = mPreferencesInfo.edit();
        ditor.putBoolean("isPush", isPush);
        ditor.commit();
    }

    /**
     * 持久化是否第一次登录
     */
    public static void setFirst(boolean isFirst, Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", context.MODE_PRIVATE);
        Editor ditor = mPreferencesInfo.edit();
        ditor.putBoolean("isFirst", isFirst);
        ditor.commit();
    }

    /**
     * 获取应用的版本号
     */
    private static int getAppVersionCode(Context context, String packageName) {
        try {
            int versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
            return versionCode;
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 获取持久化是否推送通知
     */
    public static boolean getPushAble(Context context) {

        String shPkName = "com.zdlife";//商户端15推送百度版本较旧，百度推送内部crash无法补货
        if (isInstallApkWithPackageName(context, shPkName)) {
            //if (getAppVersionCode(context, shPkName) < 15)
            return false;
        }

        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        return mPreferencesInfo.getBoolean("isPush", true);
    }

    /**
     * 获取持久化是否第一次登录
     */
    public static boolean isFirst(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        return mPreferencesInfo.getBoolean("isFirst", true);
    }

    /**
     * 持久化保存校园ID和校园名称
     */
    public static void setSchoolyard(String schoolId, String schoolname, Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", context.MODE_PRIVATE);
        Editor ditor = mPreferencesInfo.edit();
        ditor.putString("schoolId", schoolId);
        ditor.putString("schoolname", schoolname);
        ditor.commit();
    }

    /**
     * 获取持久化保存的校园ID和名称
     */
    public static HashMap<String, String> getSchoolyard(Context context) {
        SharedPreferences mPreferencesInfo = context.getSharedPreferences("zdlifeshared", Context.MODE_PRIVATE);
        String schoolId   = mPreferencesInfo.getString("schoolId", "");
        String schoolname = mPreferencesInfo.getString("schoolname", "");
        if (schoolId != null && !schoolId.equals("") && schoolname != null && !schoolname.equals("")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("schoolId", schoolId);
            map.put("schoolname", schoolname);
            return map;
        } else {
            return null;
        }
    }


    /**
     * @param timestamp 时间戳
     * @param patten    格式化格式，例如"yyyy-MM-dd HH:mm:ss"
     * @return 格式化时间字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDataFormatString(long timestamp, String patten) {
        SimpleDateFormat format = new SimpleDateFormat(patten);
        return format.format(timestamp);
    }

    /**
     * 将时间戳类型的时间 转换为规定格式的时间
     *
     * @param strFormate 例: yyyy-MM-dd HH:mm:ss
     */
    public static String getDateStrFromStamp(String strFormate, String timeStamp) {

        if (timeStamp == null || timeStamp.equals("")) {
            return "";
        } else {
            SimpleDateFormat sdf     = new SimpleDateFormat(strFormate);
            String           dateStr = sdf.format(new Date(Long.parseLong(timeStamp)));
            return dateStr;
        }

    }

    /**
     * @param date 格式为yyyy-MM-dd HH:mm:ss的时间字符串
     * @return long型时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static long getMilliseconds(String date) {
        if (date == null || date.trim().equals("")) return -1;
        Date             mDate  = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate.getTime();
    }

    /**
     * 从yyyy-MM-dd HH:mm:ss时间格式化为yyyy-MM-dd
     */
    public static String formatDateWithFormatter(String date) {
        long             timestamp = getMilliseconds(date);
        SimpleDateFormat format    = new SimpleDateFormat("yyyy-MM-dd");
        Long             time      = new Long(timestamp);
        String           d         = format.format(time);
        return d;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static double getLength(String s) {
        double valueLength = 0;
        String chinese     = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        // 进位取整
        return Math.ceil(valueLength);
    }

    /**
     * 检查是否有网络连接
     *
     * @param context
     * @return true:连接；false:未连接；
     */
    public static boolean isHavingNetWork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                return true;
            }
        }
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isAvailable();
        } else {
            return false;
        }
    }

    /**
     * 检查是否有网络连接
     *
     * @param context
     * @return true:连接；false:未连接；
     */
    public static boolean isConn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().isAvailable();
        } else {
            return false;
        }
    }

    /**
     * 判断当前网络是否是wifi网络
     * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { //判断3G网
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    /**
     * 是否登陆
     *
     * @return
     */
    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sp.getString("userId", "");
        if (TextUtils.isEmpty(userId)) {//userId为空没有登录
            return false;
        } else {
            return true;
        }
    }


    /**
     * 非wifi下获得ip地址
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

    /**
     * 获取DeviceId 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID.
     *
     * @param context
     * @return 当获取到的TelephonyManager为null时，将返回"null"
     */
    public static String getDeviceId(Context context) {
        String serial = null;
        String m_szDevIDShort = "35" +
                                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                                Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码(不需要任何设备权限，替换原有需要设备权限的方法)
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();


//        TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        if (tm == null) {
//            return "";
//        } else {
//            String id = tm.getDeviceId();
//            return id == null ? "" : id;
//        }
    }


    /**
     * dismiss wait dialog
     */
    public static void dismissWaitDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 判断是否整型
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideKeyBoard(Activity activity) {
        if ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE) != null && activity.getCurrentFocus() != null) {
            ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断字符串是否为 NULL 或者 "",
     *
     * @return 为 NULL 或者 "" 时返回 "" , 不为 NULL 或者 "" 时返回字符串
     */
    public static String getStr(String str) {
        if (str == null || str.equals("") || str.length() <= 0) {
            return "";
        }
        return str;
    }

    /**
     * 判断字符串是否为 NULL 或者 "",
     *
     * @return 为 NULL 或者 "" 时返回true , 不为 NULL 或者 "" 时返回false
     */
    public static boolean isNull(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 打电话拨号
     */
    public static void dial(Context ctx, String telephone) {
        try {
            if (telephone != null && !telephone.equals("")) {
                Uri    uri    = Uri.parse("tel:" + telephone);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                ctx.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 文本分享
    public static void shareText(Context context, String title, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, title));
    }

    // 图文
    public static void share(Context context, String title, String text, String imgurl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.encode(imgurl));
        context.startActivity(Intent.createChooser(intent, title));
    }

    // 图文
    public static void shareTI(Context context, String title, String text, String imgurl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("image/*");
        File f = new File(imgurl);
        if (f != null && f.exists() && f.isFile()) {
            intent.setType("image/png");
            Uri u = Uri.fromFile(f);
            intent.putExtra(Intent.EXTRA_STREAM, u);
        }
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 检查列表是否有数据
     *
     * @param data
     * @param nodata_img
     */
    public static void checkData(List<?> data, ImageView nodata_img) {
        if (data == null || data.size() == 0) {
            nodata_img.setVisibility(View.VISIBLE);
        } else {
            nodata_img.setVisibility(View.GONE);
        }
    }

    public static void showAlertDialog(Context context, String title, String message, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

        new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(positiveButton, positiveListener).setNegativeButton(negativeButton, negativeListener).show();
    }

    public static void showAlertDialogWithOneButton(Context context, String title, String message, String positiveButton, DialogInterface.OnClickListener positiveListener) {

        // AlertDialog dialog = new AlertDialog.Builder(context).create();
        // dialog.show();
        // dialog.setContentView(R.layout.actionbar);

        new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(positiveButton, positiveListener).show();
    }

    /**
     * 判断是否是金额
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return str.matches("[\\d]+[.]?[\\d]+");
    }

    /**
     * 计算付款剩余时间
     *
     * @return 需要倒计时文本
     */
    public static String getTimeFromInt(long time) {
        time /= 1000;
        long   minute = time / (1 * 60) % 60;
        long   second = time / (1) % 60;
        String mm     = "" + minute, ss = "" + second;
        if (minute < 10) {
            mm = "0" + minute;
        }
        if (second < 10) {
            ss = "0" + second;
        }
        return "剩余支付时间：" + mm + "分" + ss + "秒";
    }

    /**
     * 计算付款剩余时间
     *
     * @return 需要倒计时文本
     */
    public static String getSimpleTimeFromInt(long time) {
        time /= 1000;
        long   minute = time / (1 * 60) % 60;
        long   second = time / (1) % 60;
        String mm     = "" + minute, ss = "" + second;
        if (minute < 10) {
            mm = "0" + minute;
        }
        if (second < 10) {
            ss = "0" + second;
        }
        return mm + ":" + ss;
    }


    public static boolean ifCurrentActivityTopStack(Activity activity) {
        if (activity == null || activity.isFinishing()) return false;
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(activity.getClass().getName());
    }

    // String str = "^[1-9][0-9]{5}$";

    /**
     * 判断邮编
     *
     * @return
     */
    public static boolean isZipNO(String zipString) {
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    /**
     * 金额格式化（整形的数值不加小数点）
     *
     * @param money double 类型的钱数
     */
    public static String getFormatMoney(double money) {
        if (money == (int) money) {
            return (int) money + "";
        } else {
            return new DecimalFormat("0.00").format(money);
        }
    }

    /**
     * 一位小数格式化（整形的数值不加小数点）
     *
     * @param speed double 类型的钱数
     */
    public static String getFormatSpeed(double speed) {
        if (speed == (int) speed) {
            return (int) speed + "";
        } else {
            return new DecimalFormat("0.0").format(speed);
        }
    }

    /**
     * 计算两点之间距离
     *
     * @return 米
     */
    public static double getLatLngDistance(double lat1, double lng1, double lat2, double lng2) {
        if (lat1 <= 0 || lat2 <= 0 || lng1 <= 0 || lng2 <= 0) {
            return -1;
        }
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a       = radLat1 - radLat2;
        double b       = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 格式化星期几 为整形数据
     */
    public static int getFormatWeekDay(String weekDay) {
        if (weekDay.contains("一")) return 1;
        else if (weekDay.contains("二")) return 2;
        else if (weekDay.contains("三")) return 3;
        else if (weekDay.contains("四")) return 4;
        else if (weekDay.contains("五")) return 5;
        return 0;
    }

    /**
     * 格式化整形数据为星期几
     */
    public static String getDeFormatWeekDay(String weekNum) {
        if (weekNum.equals("1")) return "星期一";
        else if (weekNum.equals("2")) return "星期二";
        else if (weekNum.equals("3")) return "星期三";
        else if (weekNum.equals("4")) return "星期四";
        else if (weekNum.equals("5")) return "星期五";
        return "";
    }

    /*
     * apk MD5校验相关
     */
    public static char[] MD5HexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getFile(String fileName) {
        String md5String = "";
        try {
            md5String = getFileHash(fileName, "MD5");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return md5String;
    }

    public static String getFileHash(String fileName, String hashType) throws Exception {
        InputStream   fis    = null;
        byte[]        buffer = new byte[1024];
        MessageDigest md5    = MessageDigest.getInstance(hashType);
        try {
            fis = new FileInputStream(fileName);

            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
        } catch (Exception e) {
        }

        return toHexString(md5.digest());
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(MD5HexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(MD5HexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }


    public static String getMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 发送红包广播
     *
     * @param activity context
     * @param orderId  订单ID
     */
    public static void sendPurseSharedReceiver(Activity activity, String orderId) {
        Intent intent = new Intent();
        intent.setAction("com.zdlife.fingerlife.service.PurseSharedReceiver");
        intent.putExtra("orderId", orderId);
        activity.sendBroadcast(intent);
    }

    /**
     * 发送红包广播(可延时)
     *
     * @param activity context
     * @param orderId  订单ID
     */
    public static void sendPurseSharedReceiverDelay(final Activity activity, String orderId, long delay) {
        final Intent intent = new Intent();
        intent.setAction("com.zdlife.fingerlife.service.PurseSharedReceiver");
        intent.putExtra("orderId", orderId);
        Handler handler = new Handler();
        // 延迟执行发送广播的逻辑
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.sendBroadcast(intent);
            }
        }, delay);
    }


    /**
     * 系统播放器打开视频
     */
    public static void openVideoPlayerInSysType(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type   = "video/mp4 ";
        Uri    uri    = Uri.parse(url);
        intent.setDataAndType(uri, type);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * 判断是否满足schema启动数据格式
     *
     * @param bundle
     * @return
     */
    public static boolean isVerifySchemaPattern(Bundle bundle) {
        if (bundle != null && bundle.getString("value1") != null && !bundle.getString("value1").equals("")) {
            return true;
        }
        return false;
    }


    /**
     * 设置textView部分字体高亮显示
     *
     * @param textView
     * @param partText
     * @param color
     */
    public static void setPartTextColorHighlight(TextView textView, String partText, int color) {
        if (textView == null || textView.getText().toString().length() <= 0) return;
        String text = textView.getText().toString().trim();
        if (!text.contains(partText)) return;
        int                    start = text.lastIndexOf(partText);
        int                    end   = start + partText.length();
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(style);
    }

    public static boolean selfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result           = true;
        int     targetSdkVersion = 11;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    /**
     * 设置textView部分字体高亮显示
     * 两个参数
     *
     * @param textView
     * @param partText
     * @param color
     */
    public static void setPartTwoTextColorHighlight(TextView textView, String partText, String pareText, int color) {
        if (textView == null || textView.getText().toString().length() <= 0) return;
        String text = textView.getText().toString().trim();
        if (!text.contains(partText)) return;
        if (!text.contains(pareText)) return;

        int                    start  = text.lastIndexOf(partText);
        int                    end    = start + partText.length();
        int                    start1 = text.lastIndexOf(pareText);
        int                    end1   = start1 + pareText.length();
        SpannableStringBuilder style  = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(Color.RED), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(style);
    }

    /**
     * 金额保留2位小数点
     *
     * @param a
     * @return
     */
    public static double get2Double(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return new Double(df.format(a).toString());
    }

    /**
     * 手机号隐藏中间4位
     */
    public static String encodePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7)
            return phoneNumber == null ? "" : phoneNumber;
        StringBuffer newPhoneNumber = new StringBuffer();
        newPhoneNumber.append(phoneNumber.substring(0, 3)).append("****").append(phoneNumber.substring(7));
        return newPhoneNumber.toString();
    }

//    public static UserInfo getUserLogin(Context context) {
//        SharedPreferences mPreferencesInfo = context.getSharedPreferences(
//                "user", Context.MODE_PRIVATE);
//        UserInfo info = new UserInfo();
//
//        // 解密
//        String userPhone = "";
//        try {
//            userPhone = AESUtil.Decrypt(
//                    mPreferencesInfo.getString("phone", null), ConstantsData.CKEY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        info.setMobile(userPhone);
//        // 解密
//        String userPass = "";
//        try {
//            userPass = AESUtil.Decrypt(
//                    mPreferencesInfo.getString("pass", null), ConstantsData.CKEY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        info.setPassWord(userPass);
//        info.setUserName(mPreferencesInfo.getString("name", null));
//        info.setUserId(mPreferencesInfo.getString("userId", null));
//        info.setId(mPreferencesInfo.getString("userId", null));
//        info.setNickName(mPreferencesInfo.getString("nickname", null));
//        String money = mPreferencesInfo.getString("money", "0.0");
//        info.setMoney(Double.parseDouble(money));
//        info.setAvatar(mPreferencesInfo.getString("avatar", ""));
//        info.setPaypwd(mPreferencesInfo.getString("paypwd", null));
//        String defaultAddrString = mPreferencesInfo.getString("defaultAddress","");
//        if (defaultAddrString != null && !defaultAddrString.equals("")) {
//            try {
//                List<Address> list = new ArrayList<>();
//                list.add(Utils.deSerializeTakeAddress(defaultAddrString));
//                info.setDefaultAddress(list);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        info.setEmail(mPreferencesInfo.getString("email", null));
//        return info;
//    }

//    public static void saveUserLogin(Context ctx, UserInfo info) {
//        SharedPreferences mPreferencesInfo = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
//        Editor ditor = mPreferencesInfo.edit();
//        // 加密
//        String newPhone = "";
//        try {
//            newPhone = AESUtil.Encrypt(info.getMobile(), ConstantsData.CKEY);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//
//        ditor.putString("phone", newPhone);
//        ditor.putString("userId", info.getId());
//        // 加密
//        String newPass = "";
//        try {
//            newPass = AESUtil.Encrypt(info.getPassWord(), ConstantsData.CKEY);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        ditor.putString("pass", newPass);
//        ditor.putString("name", info.getUserName());
//        ditor.putString("nickname", info.getNickName());
//        ditor.putString("money", info.getMoney()+"");
//        ditor.putString("avatar", info.getAvatar());
//        ditor.putString("paypwd", info.getPaypwd());
//        try {
//            ditor.remove("defaultAddress");
//            if (info.getDefaultAddress() != null&& info.getDefaultAddress().size() > 0&&info.getDefaultAddress().get(0)!=null) {
//                ditor.putString("defaultAddress",
//                        Utils.serializeTakeAddress(info.getDefaultAddress().get(0)));
//            } else {
//                ditor.putString("defaultAddress", "");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ditor.putString("email", info.getEmail());
//        ditor.commit();
//    }

    /**
     * 序列化收货地址对象
     *
     * @param person
     * @return
     * @throws IOException
     */
//    public static String serializeTakeAddress(Address person)
//            throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
//                byteArrayOutputStream);
//        objectOutputStream.writeObject(person);
//        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
//        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
//        objectOutputStream.close();
//        byteArrayOutputStream.close();
//        return serStr;
//    }

    /**
     * 反序列化TakeAddress对象
     *
     * @param
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
//    public static Address deSerializeTakeAddress(String str)
//            throws IOException, ClassNotFoundException {
//        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
//                redStr.getBytes("ISO-8859-1"));
//        ObjectInputStream objectInputStream = new ObjectInputStream(
//                byteArrayInputStream);
//        Address address = (Address) objectInputStream.readObject();
//        objectInputStream.close();
//        byteArrayInputStream.close();
//        return address;
//    }
    public static String getFileMd5(String fileName) {
        String md5String = "";
        try {
            md5String = getFileHash(fileName, "MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return md5String;
    }


    //保存订单列表是否需要刷新状态
    public static void saveOrderListRefreshState(Context ctx, boolean refreshState) {
        SharedPreferences mPreferencesInfo = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
        Editor            editor           = mPreferencesInfo.edit();
        editor.putBoolean("refreshState", refreshState);
        editor.commit();
    }

    public static Boolean getOrderListRefreshState(Context ctx) {
        SharedPreferences mPreferencesInfo = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
        return mPreferencesInfo.getBoolean("refreshState", false);
    }

    //保存悬浮窗状态
    public static void saveFloatState(Context ctx, boolean refreshState) {
        SharedPreferences mPreferencesInfo = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
        Editor            editor           = mPreferencesInfo.edit();
        editor.putBoolean("floatState", refreshState);
        editor.commit();
    }

    public static Boolean getFloatState(Context ctx) {
        SharedPreferences mPreferencesInfo = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
        return mPreferencesInfo.getBoolean("floatState", false);
    }

    public static void startActivity(Bundle bundle, Activity fromActivity, Class toActivity) {
        Intent intent = new Intent(fromActivity, toActivity);
        if (bundle == null) {
            fromActivity.startActivity(intent);
        } else {
            intent.putExtras(bundle);
            fromActivity.startActivity(intent);
        }
    }


    //判断是否是URL
    public static String getUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return url;
            } else {
//                return RetrofitUtil.imageServerHost + url;
                return url;
            }
        } else {
            return "";
        }
    }

    public static void showShortToast(Context context, String text) {
        try {
            if (!text.equals("")) Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
    /**
     * 检查手机上是否安装了指定的软件
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(String packageName, Context context){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
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
