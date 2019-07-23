package com.ctrl.android.kcetong.toolkit.webutils;


import com.ctrl.android.kcetong.model.ActDetailResult;
import com.ctrl.android.kcetong.model.ActResult;
import com.ctrl.android.kcetong.model.Ad;
import com.ctrl.android.kcetong.model.AddressList;
import com.ctrl.android.kcetong.model.ApkVersionsBean;
import com.ctrl.android.kcetong.model.AuthCodeResult;
import com.ctrl.android.kcetong.model.BannerList;
import com.ctrl.android.kcetong.model.BeforePropertyPayResult;
import com.ctrl.android.kcetong.model.BuildingListBean;
import com.ctrl.android.kcetong.model.BuildingResult;
import com.ctrl.android.kcetong.model.CityResult;
import com.ctrl.android.kcetong.model.CommentBean;
import com.ctrl.android.kcetong.model.CommunituList;
import com.ctrl.android.kcetong.model.Complaint;
import com.ctrl.android.kcetong.model.ComplaintDeatilBean;
import com.ctrl.android.kcetong.model.ComplaintTypeBean;
import com.ctrl.android.kcetong.model.DefaultAddress;
import com.ctrl.android.kcetong.model.EmessagingDetailBean;
import com.ctrl.android.kcetong.model.ExpressDetailResult;
import com.ctrl.android.kcetong.model.ExpressResult;
import com.ctrl.android.kcetong.model.ForumCategoryBean;
import com.ctrl.android.kcetong.model.ForumNoteBean;
import com.ctrl.android.kcetong.model.ForumNoteDetailBean;
import com.ctrl.android.kcetong.model.GenerateOrder;
import com.ctrl.android.kcetong.model.HouseBean;
import com.ctrl.android.kcetong.model.HouseEstateResult;
import com.ctrl.android.kcetong.model.HouseResult;
import com.ctrl.android.kcetong.model.InitResult;
import com.ctrl.android.kcetong.model.KindResult;
import com.ctrl.android.kcetong.model.LoginBean;
import com.ctrl.android.kcetong.model.ManagmentBean;
import com.ctrl.android.kcetong.model.Notice;
import com.ctrl.android.kcetong.model.Noticedetail;
import com.ctrl.android.kcetong.model.OrderBean;
import com.ctrl.android.kcetong.model.PaymentListResult;
import com.ctrl.android.kcetong.model.PaymentResult;
import com.ctrl.android.kcetong.model.ProductDetail;
import com.ctrl.android.kcetong.model.ProductListBean;
import com.ctrl.android.kcetong.model.PropertyHisPayResult;
import com.ctrl.android.kcetong.model.Proprietorinfo;
import com.ctrl.android.kcetong.model.RegionDetailListResult;
import com.ctrl.android.kcetong.model.RegionFollowListResult;
import com.ctrl.android.kcetong.model.RegionListResult;
import com.ctrl.android.kcetong.model.RepairDeatilBean;
import com.ctrl.android.kcetong.model.RepairListBean;
import com.ctrl.android.kcetong.model.RoomListBean;
import com.ctrl.android.kcetong.model.ServiceList;
import com.ctrl.android.kcetong.model.ServiceListBottom;
import com.ctrl.android.kcetong.model.ServiceListResult;
import com.ctrl.android.kcetong.model.ServiceOrderListResult;
import com.ctrl.android.kcetong.model.ServiceOrderResult;
import com.ctrl.android.kcetong.model.ServiceProductResult;
import com.ctrl.android.kcetong.model.ShopCategoryBean;
import com.ctrl.android.kcetong.model.SubmitVoteResult;
import com.ctrl.android.kcetong.model.SuperCommunity;
import com.ctrl.android.kcetong.model.Surround;
import com.ctrl.android.kcetong.model.SurveyDetailResult;
import com.ctrl.android.kcetong.model.SurveyListResult;
import com.ctrl.android.kcetong.model.UnitListBean;
import com.ctrl.android.kcetong.model.UpLoadResult;
import com.ctrl.android.kcetong.model.UserGoodInfoResult;
import com.ctrl.android.kcetong.model.UserGoodsResult;
import com.ctrl.android.kcetong.model.VisitBean;
import com.ctrl.android.kcetong.model.VisityinfoBean;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by qiu on 2016/07/25.
 */
public interface APIService {

    //广告列表
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.advertisement.list")
    Observable<Ad> Banner(@FieldMap Map<String, String> map);

    //获取大分类接口
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.service.kindList")
    Observable<ServiceList> serviceKindList(@FieldMap Map<String, String> map);

    //获取大分类下面的
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.service.list")
    Observable<ServiceListBottom> serviceList(@FieldMap Map<String, String> map);

    //
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.banner.list")
    Observable<BannerList> serviceBannerList(@FieldMap Map<String, String> map);

    //e管家
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.eSteward.selectInstantMessaging")
    Observable<ManagmentBean> Emessaging(@FieldMap Map<String, String> map);

    //e管家
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.eSteward.selectMessagingById")
    Observable<EmessagingDetailBean> EmessagingDetail(@FieldMap Map<String, String> map);

    /**
     * 余额支付
     *
     * @param data
     * @return
     */
    @POST("mainExpressPay/1302")
    Observable<ResponseBody> payBalance(@Query("data") String data);

    /**
     * 修改用户头像
     *
     * @param picMap
     * @return
     */
    @Multipart
    @POST("mainUserInfo/1203")
    Observable<ResponseBody> setUserHeadAvatar(@PartMap Map<String, RequestBody> picMap);


    /**
     * 用户登录
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.members.login")
    Observable<LoginBean> UserLogin(@FieldMap Map<String, String> map);

    /**
     * 用户注册
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.members.registe")
    Observable<ResponseBody> register(@FieldMap Map<String, String> map);

    /**
     * 周边商户列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.service.list")
    Observable<Surround> surroundList(@FieldMap Map<String, String> map);

    /**
     * 获取短信验证码
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.messageRecord.getAuthCode")
    Observable<AuthCodeResult> getAuthCode(@FieldMap Map<String, String> map);

    /**
     * 修改/找回登录密码
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.members.modifyPassword")
    Observable<ResponseBody> changePassword(@FieldMap Map<String, String> map);

    /**
     * 会员初始化
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.init")
    Observable<InitResult> userCommunity(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api?&method=pm.ppt.proprietor.get")
    Observable<Proprietorinfo> userProprietor(@FieldMap Map<String, String> map);

    /**
     * 获取社区活动列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.action.queryActionList")
    Observable<ActResult> getActList(@FieldMap Map<String, String> map);

    /**
     * 获取社区活动详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.action.queryActionDetail")
    Observable<ActDetailResult> getActDetail(@FieldMap Map<String, String> map);

    /**
     * 参与社区活动
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.action.joinAction")
    Observable<ResponseBody> requestTakePartInAct(@FieldMap Map<String, String> map);

    /**
     * 获取温馨提示列表数据
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.propertyNotice.queryPropertyNoticeList")
    Observable<Notice> NoticrLiseDate(@FieldMap Map<String, String> map);

    /**
     * 获取小区列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.community.list")
    Observable<CommunituList> CommunityLiseDate(@FieldMap Map<String, String> map);

    /**
     * 管理员获取小区列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.stf.community.communityList")
    Observable<SuperCommunity> CommunityList(@FieldMap Map<String, String> map);

    /**
     * 上传图片 接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.propertyPicture.upload")
    Observable<UpLoadResult> uploadImg(@FieldMap Map<String, String> map);

    /**
     * 获取物业公告详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.propertyNotice.queryPropertyNoticeInfo")
    Observable<Noticedetail> PropertyNoticeInfo(@FieldMap Map<String, String> map);


    /**
     * 发布活动 接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.action.addAction")
    Observable<ResponseBody> releaseActivity(@FieldMap Map<String, String> map);

    /**
     * 获取房屋列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.memberBind.list")
    Observable<HouseResult> requestHouseList(@FieldMap Map<String, String> map);

    /**
     * 获取小区列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.community.list")
    Observable<CommunituList> requestCommunityList(@FieldMap Map<String, String> map);

    /**
     * 获取楼号、单元。房间列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityAddress.list")
    Observable<BuildingResult> requestBuildingList(@FieldMap Map<String, String> map);

    /**
     * 获取业主快递信息列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.express.list")
    Observable<ExpressResult> requestExpressList(@FieldMap Map<String, String> map);

    /**
     * 获取快递信息详情
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.express.get")
    Observable<ExpressDetailResult> requestExpressDetail(@FieldMap Map<String, String> map);

    /**
     * 会员进行业主认证
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.proprietor.verify")
    Observable<ResponseBody> requestProprietyVerify(@FieldMap Map<String, String> map);

    /**
     * 物业缴费列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.propertyPayment.queryPropertyPaymentList")
    Observable<PaymentListResult> requestPropertyPayList(@FieldMap Map<String, String> map);

    /**
     * 计算物业账单总额
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.propertyPayment.calcPropertyPaymentPrice")
    Observable<PaymentResult> requestPropertyPayAmount(@FieldMap Map<String, String> map);

    /**
     * 查询物业账单记录列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.propertyPaymentRecord.queryPropertyPaymentRecordList")
    Observable<PropertyHisPayResult> requestPayHistory(@FieldMap Map<String, String> map);

    /**
     * 物业账单  支付前校验
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.propertyPayment.checkBeforePay")
    Observable<BeforePropertyPayResult> requestBeforePropertyPay(@FieldMap Map<String, String> map);

    /**
     * 获得访问列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityVisit.queryCommunityVisitList")
    Observable<VisitBean> VisitListdate(@FieldMap Map<String, String> map);

    /**
     * 更改公告状态为已读
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.memberNoticeRelation.updatePropertyNoticeStatus")
    Observable<ResponseBody> chengenoticmodify(@FieldMap Map<String, String> map);

    /**
     * 获取预约访客详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityVisit.queryCommunityVisitInfo")
    Observable<VisityinfoBean> visitedetailinfo(@FieldMap Map<String, String> map);

    /**
     * 处理到访信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityVisit.handleCommunityVisit")
    Observable<ResponseBody> handlevisitinfo(@FieldMap Map<String, String> map);

    /**
     * 获取特约服务类别列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceKind.list")
    Observable<ServiceListResult> requestServiceKindList(@FieldMap Map<String, String> map);

    /**
     * 获取我的预约服务列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceOrder.list")
    Observable<ServiceOrderListResult> requestServiceOrderList(@FieldMap Map<String, String> map);

    /**
     * 获取我的预约服务详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceOrder.get")
    Observable<ServiceOrderResult> requestServiceOrderInfo(@FieldMap Map<String, String> map);

    /**
     * 会员取消订单
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceOrder.cancel")
    Observable<ResponseBody> requestCancelServiceOrder(@FieldMap Map<String, String> map);

    /**
     * 服务订单支付前校验
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceOrder.checkServicePay")
    Observable<ResponseBody> requestCheckService(@FieldMap Map<String, String> map);

    /**
     * 订单支付成功后
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceOrder.afterServiceOrderPay")
    Observable<ResponseBody> requestAfterServiceOrderPay(@FieldMap Map<String, String> map);

    /**
     * 获取特约服务列表及详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceProduct.list")
    Observable<ServiceProductResult> requestServiceList(@FieldMap Map<String, String> map);

    /**
     * 服务预约
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.serviceOrder.add")
    Observable<ResponseBody> requestServiceAdd(@FieldMap Map<String, String> map);

    /**
     * 调查列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.questionnaire.list")
    Observable<SurveyListResult> requestSurveyList(@FieldMap Map<String, String> map);

    /**
     * 调查详细列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.questionnaire.get")
    Observable<SurveyDetailResult> requestSurveyDetail(@FieldMap Map<String, String> map);

    /**
     * 提交调查
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.questionnaireMessageProprietor.reply")
    Observable<ResponseBody> requestSubmitSurvey(@FieldMap Map<String, String> map);

    /**
     * 提交投票
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.questionnaireMessageProprietor.reply")
    Observable<SubmitVoteResult> requestSubmitVote(@FieldMap Map<String, String> map);

    /**
     * 获取二手交易列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.usedGoods.list")
    Observable<UserGoodsResult> requestGoodsList(@FieldMap Map<String, String> map);

    /**
     * 获取二手交易详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.usedGoods.get")
    Observable<UserGoodInfoResult> requestGoodsGet(@FieldMap Map<String, String> map);

    /**
     * 删除二手交易信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.usedGoods.delete")
    Observable<ResponseBody> requestGoodsDelete(@FieldMap Map<String, String> map);

    /**
     * 获取分类列表接口
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.common.kind.list")
    Observable<KindResult> requestData(@FieldMap Map<String, String> map);

    /**
     * 发布二手交易信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.usedGoods.add")
    Observable<ResponseBody> requestGoodsAdd(@FieldMap Map<String, String> map);

    /**
     * 房产信息列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.homeEstate.list")
    Observable<HouseEstateResult> requestHouseEstate(@FieldMap Map<String, String> map);

    /**
     * 房产信息中省市区信息接口
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.getCitiesList.list")
    Observable<CityResult> requestCitiesList(@FieldMap Map<String, String> map);

    /**
     * 添加到访信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityVisit.addCommunityVisit")
    Observable<ResponseBody> addvisitinfo(@FieldMap Map<String, String> map);

    /**
     * 获取会员基本信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.members.get")
    Observable<LoginBean> getmemberinfo(@FieldMap Map<String, String> map);

    /**
     * 更改会员头像
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.members.modifyHeadImg")
    Observable<ResponseBody> changeMemberIcon(@FieldMap Map<String, String> map);

    /**
     * 更改会员头像
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.members.modify")
    Observable<ResponseBody> requestModifyMemberInfo(@FieldMap Map<String, String> map);

    /**
     * 投诉列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.complaint.list")
    Observable<Complaint> ComplaintList(@FieldMap Map<String, String> map);

    /**
     * 投诉类型
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.common.kind.list")
    Observable<ComplaintTypeBean> ComplaintType(@FieldMap Map<String, String> map);

    /**
     * 解除房屋绑定
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.memberBind.removeBind")
    Observable<ResponseBody> removeHouseBind(@FieldMap Map<String, String> map);


    /**
     * 设置认证房屋
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.memberBind.setDefault")
    Observable<ResponseBody> setHouseBind(@FieldMap Map<String, String> map);

    /**
     * 获得论坛所有板块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.forumCategory.queryForumCategoryList")
    Observable<ForumCategoryBean> getForumlist(@FieldMap Map<String, String> map);

    /**
     * 获得所有帖子列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.forumPost.queryForumPostList")
    Observable<ForumNoteBean> getAllForumnitic(@FieldMap Map<String, String> map);

    /**
     * 获得帖子信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.forumPost.queryForumPostDetl")
    Observable<ForumNoteDetailBean> getNoteDetaildata(@FieldMap Map<String, String> map);

    /**
     * 给帖子点赞
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.PostPraise.addPostPraise")
    Observable<ResponseBody> setLikeNote(@FieldMap Map<String, String> map);

    /**
     * 帖子取消点赞
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.PostPraise.delPostPraise")
    Observable<ResponseBody> CanclesetLikeNote(@FieldMap Map<String, String> map);

    /**
     * 回复帖子
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.postReply.addPostReply")
    Observable<ResponseBody> CommenNoteDate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api?&method=pm.ppt.complaint.complaint")
    Observable<ResponseBody> releaseComplaint(@FieldMap Map<String, String> map);

    /**
     * 投诉详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.complaint.get")
    Observable<ComplaintDeatilBean> complaintDeatil(@FieldMap Map<String, String> map);

    /**
     * 评价投诉内容
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.complaint.evaluate")
    Observable<ResponseBody> evaluationComplaint(@FieldMap Map<String, String> map);

    /**
     * 发布帖子
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.forumPost.addForumPost")
    Observable<ResponseBody> postMessage(@FieldMap Map<String, String> map);

    /**
     * 获取周边商家
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.companysAround.get")
    Observable<ShopCategoryBean> requestShopList(@FieldMap Map<String, String> map);

    /**
     * 发布活动
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.action.addAction")
    Observable<ResponseBody> postAction(@FieldMap Map<String, String> map);


    @POST("/webservice/GetHomeInfo.ashx")
    Observable<ResponseBody> testApi(@Body String memberID);

    @FormUrlEncoded
    @POST("/webservice/member/MemberLogin.ashx")
    Observable<ResponseBody> getMemberLoginMap(@FieldMap Map<String, String> map);

    @POST("/webservice/member/MemberRegister.ashx")
    Observable<ResponseBody> getMemberRegisterMap(@QueryMap Map<String, String> map);

    //设为默认地址
    @POST("/webservice/member/SetDefaultAddress.ashx")
    Observable<VisityinfoBean> SetDefaultAddress(@Query("memberid") String memberid, @Query("id") String id);

    /**
     * 获取地域列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.regionalManagement.list")
    Observable<RegionListResult> requestRegionList(@FieldMap Map<String, String> map);

    /**
     * 获取地域列表详情
     * 置业列表查询、置业详情查询、以及是否关注
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.regionalProperties.list")
    Observable<RegionDetailListResult> requestRegionDetailList(@FieldMap Map<String, String> map);

    /**
     * 取消、添加关注
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.regionalFollow.operate")
    Observable<ResponseBody> addAndCancelConcern(@FieldMap Map<String, String> map);

    /**
     * 获取我的关注列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.regionalFollow.list")
    Observable<RegionFollowListResult> requestMyFollowList(@FieldMap Map<String, String> map);

    /**
     * 房屋列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.memberBind.list")
    Observable<HouseBean> houseList(@FieldMap Map<String, String> map);

    /**
     * 认证小区
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.community.list")
    Observable<HouseBean> communityList(@FieldMap Map<String, String> map);

    /**
     * 楼栋列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityAddress.list")
    Observable<BuildingListBean> buildingList(@FieldMap Map<String, String> map);

    /**
     * 楼栋列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityAddress.list")
    Observable<UnitListBean> unitList(@FieldMap Map<String, String> map);

    /**
     * 房间列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.communityAddress.list")
    Observable<RoomListBean> roomList(@FieldMap Map<String, String> map);

    /**
     * 认证
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.proprietor.verify")
    Observable<ResponseBody> proprietyVerify(@FieldMap Map<String, String> map);

    /**
     * 报修列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.repairDemand.list")
    Observable<RepairListBean> repairList(@FieldMap Map<String, String> map);

    /**
     * 报修详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.repairDemand.get")
    Observable<RepairDeatilBean> repairDeatil(@FieldMap Map<String, String> map);

    /**
     * 报修评价
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.repairDemand.evaluate")
    Observable<ResponseBody> repairEvaluate(@FieldMap Map<String, String> map);

    /**
     * 报修
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.repairDemand.requestRepair")
    Observable<ResponseBody> releaseRepair(@FieldMap Map<String, String> map);

    /**
     * 版本信息
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.apkVersions.updateApkVersions")
    Observable<ApkVersionsBean> updateApkVersions(@FieldMap Map<String, String> map);

    /**
     * 版本信息
     */
    @FormUrlEncoded
    @POST("api?&method=zhidongH5/html")
    Observable<ResponseBody> zhiDongLogin(@FieldMap Map<String, String> map);

    /**
     * 获取团购商品信息列表
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newProduct.list")
    Observable<ProductListBean> productList(@FieldMap Map<String, String> map);

    /**
     * 获取团购商品详情
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newProduct.get")
    Observable<ProductDetail> productDetList(@FieldMap Map<String, String> map);

    /**
     * 获取默认地址
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newReceiveAddress.getdefault")
    Observable<DefaultAddress> getdefaultAddress(@FieldMap Map<String, String> map);

    /**
     * 获取会员收货地址列表
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.receiveAddress.list")
    Observable<AddressList> getAddressList(@FieldMap Map<String, String> map);

    /**
     * 删除会员收货地址
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.receiveAddress.delete")
    Observable<ResponseBody> deleteAddress(@FieldMap Map<String, String> map);

    /**
     * 设置会员默认收货地址
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.receiveAddress.setdefault")
    Observable<ResponseBody> setDefaultAddress(@FieldMap Map<String, String> map);

    /**
     * 修改收货地址
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.receiveAddress.modify")
    Observable<ResponseBody> modifyAddress(@FieldMap Map<String, String> map);

    /**
     * 添加收货地址
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.receiveAddress.add")
    Observable<ResponseBody> addAddress(@FieldMap Map<String, String> map);

    /**
     * 生成团购商品订单
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newOrder.generateOrder")
    Observable<GenerateOrder> generateOrder(@FieldMap Map<String, String> map);

    /**
     * 评论列表
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newOrderEvaluation.list")
    Observable<CommentBean> commentList(@FieldMap Map<String, String> map);

    /**
     * 商品列表
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newOrder.list")
    Observable<OrderBean> orderList(@FieldMap Map<String, String> map);

    /**
     * 会员评价订单
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newOrderEvaluation.evaluate")
    Observable<ResponseBody> orderEvaluate(@FieldMap Map<String, String> map);

    /**
     * 会员删除订单
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newOrder.delete")
    Observable<ResponseBody> orderDelete(@FieldMap Map<String, String> map);

    /**
     * 会员确认收货
     */
    @FormUrlEncoded
    @POST("api?&method=pm.ppt.newOrder.receive")
    Observable<ResponseBody> orderReceive(@FieldMap Map<String, String> map);

}
