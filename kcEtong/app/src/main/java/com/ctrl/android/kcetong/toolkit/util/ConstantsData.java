package com.ctrl.android.kcetong.toolkit.util;

import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/18.
 */
public class ConstantsData {

    public static int ViewFlowHeight;
    public static int ScreenWidth;
    public static int ScreenHeight;
    public static final String LOGIN_SUCCESS   = "cn.com.ctrl.login_success";
    public static final String LOGOUT_SUCCESS  = "cn.com.ctrl.logout_success";
    public static final String BALANCE_CHANGED = "cn.com.ctrl.balance_changed";
    public static final String TUIKUAN         = "cn.com.ctrl.tuikuan";
    public static final String PAY_URL         = "http://gyt2.viphk.ngrok.org/pm/";//支付地址

    public static final String success       = "000";
    public static final String PARENT_URL    = RetrofitUtil.baseUrl;
    /**
     * 用户名
     */
    public static final String USERNAME      = "username";
    /**
     * 密码
     */
    public static final String PASSWORD      = "password";
    /**
     * 用用程序的key
     */
    public static       String APPKEY        = "appKey";//用用程序的key
    /**
     * 用用程序的key
     */
    public static       String APPKEY_VALUE  = "002";//用用程序的key
    /**
     * 密匙
     */
    public static       String SECRET        = "secret";//密匙
    /**
     * 密匙
     */
    public static       String SECRET_VALUE  = "abc";//密匙
    /**
     * 版本
     */
    public static       String VERSION       = "v";//版本
    /**
     * 版本
     */
    public static       String VERSION_VALUE = "1.0";//版本
    /**
     * 输出格式 JSON
     */
    public static       String FORMAT        = "format";//输出格式 JSON
    /**
     * 输出格式 JSON
     */
    public static       String FORMAT_VALUE  = "JSON";//输出格式 JSON
    /**
     * 1:android  2:IOS  3:web
     */
    public static       String TYPE          = "type";//1:android  2:IOS  3:web
    /**
     * 1:android  2:IOS  3:web
     */
    public static       String TYPE_VALUE    = "1";//1:android  2:IOS  3:web
    /**
     * 方法名
     */
    public static       String METHOD        = "method";//方法名

    /**
     * 列表数据每页显示的条数 - Eric - 20150929
     */
    public static final int ROW_COUNT_PER_PAGE = 10;

    /**
     * 每页容量- 每页有多少条记录
     */
    public static final int PAGE_CAPACITY = 10;

    /**
     * 时间 类型 - Eric - 20151013
     */
    /*年-月-日*/
    public static final String YYYY_MM_DD       = "yyyy-MM-dd";
    /*年-月-日 时:分*/
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 支付宝相关 支付信息的配置 - Eric -
     */
    /*支付宝回调接口*/
    public static final String ALIPLY_URL          = PAY_URL + "alipayOrderNotify.do";
    public static final String ALIPLY_URL_PROPERTY = PAY_URL + "alipayPaymentNotify.do";
    public static final String ALIPLY_URL_SERVICE  = PAY_URL + "alipayServiceOrderNotify.do";
    public static final String ALIPLY_URL_ORDER  = PAY_URL + "newAlipayOrderNotify.do";
    /*合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。 例: 2088812260656255*/
    public static final String PARTNER             = "";
    /*商户收款的支付宝账号 例: wushidadao77@163.com*/


    public static final String SELLER      = "sdgangyitong@163.com";
    /*商户私钥 例: MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ+Q9tJIfK5PvZtC6eGgucND3cOEBbc6wUJVrsgEPKiYJmMFJFe0fya1hvPragz4scRw+ozi45e4eeWnpMUJmXg4pYyZcpozom1nsGlp293DE29nqJvXTv0qEzNqZcQgSCkoZzVBTnaREw6o2XBXKyVt11B4DrYWTBqFDWyhzlkHAgMBAAECgYEAiHreklgLxLBRtdYS47isitamfM+Ub/diS5Gr8Eqnc3DIDJPeVOH+i6Ziaoll6PhiXGph81UxY5kXMhYk+Z9PUsnOq6piLR6jajs7/PQbeUOrK/27lzKx97f2/zVacFadkx33P/ReXNe6sCY7xvVr8AiDL2Qyh0TiNhmfzx39CDECQQDThTngCjCVEO1AjCX9keA3E3isAaZeDGWXfkdO4JIsOnCyRh/V1QiUvCwbpgdvdxf+N9RsQxC2faR/p9/7UDffAkEAwR7kOzuq/dGTTb65xTSY314O3qe3oaK+G9euWzQPavj+DybxhClqc1HNcYoHteM47Mry1vOmeRY+iug4UESj2QJATGyDd7ZWzVU7U6oPg+m0CFJJtGQ4Nxzli/H9U7uCNOa8lz0M/ZamLg87JJY9c4GlMp37a05j+Hu29sSyAbx/IwJAfGKMN8aHrLGmgcWdW3I0IHIxe6FkufvbHI2/ZEjUwV6cLGA14JzYTmxauY1gx/sQ+BsDbAVErOrx34AQfUqoiQJBANNb9XhPjFNYsjMlqlV2ccYURzQNQkqSZZF2WuudoglxtgiK0w+RbdZcB8cRi/EkuNT6CODb3chlJCNpTpn/CXQ=*/
    public static final String RSA_PRIVATE = "///sOSAmmPD1V/yZvkoy/UxyRuawJDktUpzh6Ol9l4AbDwbJDS/8KWOf89pUzk6w0TnxKLmh2tqjE/ZRKGMx5Sqp+oJvwmD6/HbaP6sxu20VQiZ/PmFn+STcdC21K8JHgINUtJ12OyrP4geneDFyg6ZZLGxCiA2e3sPSIWQqkxJQJBAPoW7NgIsik0p3RBiWT1a8ow22A42py+3zM4ixJ/MRHQfke6Bjt/CXyzDKbli4hPmuw5Fz0yxzSeGxchSEKS/GcCQQDigPk3U6rBJJOG3Yz2AkCbYFjDbPjQr3GZni1k3bJXempL/6KsvjrmXeXJyXpqAxpEOzvaFHqXptkeDeKSByMXAkEAkdkEzwhjPnyim74MroAiGIxdo8QEWh1Gdcyn7mWW7Fi3DIWEVPZFRh/1ZO/BbAFgzBqPXvVaD2mVSuvyBEFMSQJAeDCoDcu/2C1oezAlFIWtcsV1VH4GqfXu+e0NeXCKqhCzRxCnabKn3oYE4UagRpDO51XnNJmZU8+ddm03xPU0kQJAGcKuqHsgjuHrgkXSCUDf0tbzvNY4x8jhmfoXVWUuEddw8Iy7kDhPFXEsPpVvnWpTvvC5WOG1fiS/aPEcivwO4g==";

    /**
     * 微信相关 支付信息的配置 - Eric -
     */
    /*微信分配的公众账号ID 例: wx6848dc314d5a2b80*/
    public static final String APP_ID              = "";
    /*微信支付分配的商户号 例: 1232506602*/
    public static final String MCH_ID              = "1493747202";
    /*API密钥，在商户平台设置 例: q1w2e3r4t5y6u7i8o9p0asdfghjklzxc*/
    public static final String API_KEY             = "a477fa1ab60ae5592e6d81a056bb5046";
    //异步通知地址
    public static final String NOTICE_URL          = PAY_URL + "wxOrderNotify.do";
    public static final String NOTICE_URL_PROPERTY = PAY_URL + "wxPaymentNotify.do";
    public static final String NOTICE_URL_SERVICE  = PAY_URL + "wxServiceOrderNotify.do";
    public static final String NOTICE_URL_ORDER  = PAY_URL + "newWxOrderNotify.do";
    /*微信支付返回Code  固定值*/
    public static final int    PAY_STATUS_SUCCESS  = 0;//微信支付成功
    public static final int    PAY_STATUS_FAILED   = -1;//微信支付失败
    public static final int    PAY_STATUS_CANCLE   = -2;//微信支付 被取消

    /**
     * 系统级参数
     */
    public static Map<String, String> getSystemParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ConstantsData.APPKEY, ConstantsData.APPKEY_VALUE);
        params.put(ConstantsData.SECRET, ConstantsData.SECRET_VALUE);
        params.put(ConstantsData.VERSION, ConstantsData.VERSION_VALUE);
        params.put(ConstantsData.FORMAT, ConstantsData.FORMAT_VALUE);
        params.put(ConstantsData.TYPE, ConstantsData.TYPE_VALUE);

        return params;
    }

    /**
     * 获取七牛token
     *
     * @return
     */
    public static String getToken() {

        Mac       mac       = new Mac(StrConstant.QINIU_AK, StrConstant.QINIU_SK);
        PutPolicy putPolicy = new PutPolicy(StrConstant.QINIU_BUCKNAME);
        putPolicy.returnBody = "{\"name\": $(fname),\"size\": \"$(fsize)\",\"w\": \"$(imageInfo.width)\",\"h\": \"$(imageInfo.height)\",\"key\":$(etag)}";
        try {
            String uptoken = putPolicy.token(mac);
            LLog.w("debug:uptoken = " + uptoken);
            return uptoken;
        } catch (AuthException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 活动列表的类型 - Eric - 20151026
     */
    /*全部活动*/
    public static final String ACT_ALL        = "act_all";
    /*我参与的*/
    public static final String ACT_I_TAKE_IN  = "act_i_take_in";
    /*我发起的*/
    public static final String ACT_I_START_UP = "act_i_start_up";

    /**
     * 投诉
     */
    public static final String MY_COMPLAINT_PENDING     = "0";
    public static final String MY_COMPLAINT_PROGRESSING = "1";
    public static final String MY_COMPLAINT_PROGRESSED  = "2";
    public static final String MY_COMPLAINT_END         = "3";
    public static final String MY_COMPLAINT_ALL         = "4";
    public static final String COMPLAINT_IMAGE_APPKEY   = "CPT";//投诉类型appkey
    public static final String MAC_NAME                 = "HmacShA1"; // hmac-sha1 秘钥算法名称

    /**
     * 当activity之间传递参数, 只有一个参数时 该变量为固定的key值
     */
    public static final String ARG_FLG = "arg";

    /**
     * 跳转至周围商家时 固定参数
     */
    public static final String ARG_HOTEL     = "酒店";
    public static final String ARG_LIFE      = "生活服务";
    public static final String ARG_LANDRY    = "洗衣";
    public static final String ARG_ENTERTAIN = "休闲娱乐";
    public static final String ARG_MOVIE     = "电影";
    public static final String ARG_FOOD      = "美食";
    public static final String ARG_KTV       = "KTV";
    public static final String ARG_COFFEE    = "咖啡";
    public static final String ARG_GYM       = "健身";
    public static final String ARG_PARKING   = "停车";
    public static final String ARG_FACIAL    = "美容";
    public static final String ARG_TRAVAL    = "旅游";

    public static final String ARG_KEY     = "开锁配钥";
    public static final String ARG_WATER      = "水电维修";
    public static final String ARG_MOVING    = "搬家服务";
    public static final String ARG_RUSH_PIPE = "管道疏通";
    public static final String ARG_FANGSHUI     = "防水堵漏";
    public static final String ARG_APT      = "跑腿服务";
    public static final String ARG_MOTHER       = "母婴服务";
    public static final String ARG_HOUSEKEEPING    = "家政公司";
    public static final String ARG_decoration       = "装饰装修";
//    public static final String ARG_PARKING   = "装饰价格";
    public static final String ARG_formaldehyde_treatment = "甲醛治理";
    public static final String ARG_BANJIA = "搬家公司";
    public static final String ARG_DATONGSHUI = "送大桶水";
    public static final String ARG_UCMC = "洗衣连锁";
    public static final String ARG_ZULIAO = "足疗保健";

    /**
     * 默认检索关键字
     */
    public static final String SEARCH_KEY_WORD = "公交";

    /**
     * 分页编号
     */
    public static final int DEFAULT_PAGE_NUM = 0;

    /**
     * 默认检索范围 单位: 米
     */
    public static final int SEARCH_RADIUS = 2000;
}
