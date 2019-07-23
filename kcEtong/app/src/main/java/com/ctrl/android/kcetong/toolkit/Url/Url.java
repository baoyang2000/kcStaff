package com.ctrl.android.kcetong.toolkit.Url;

/**
 * Created by Administrator on 2017/1/4.
 */

public class Url {

    // 登录
    public static String loginUrl = "pm.ppt.members.login";

    // 注册
    public static String registeUrl = "pm.ppt.members.registe";

    // 手机验证码
    public static String codeUrl = "pm.ppt.messageRecord.getAuthCode";

    // 找回密码
    public static String findPasswordUrl = "pm.ppt.members.modifyPassword";

    // 个人社区信息
    public static String communityUrl = "pm.ppt.init";

    //获取社区活动列表
    public static String queryActionList       = "pm.ppt.action.queryActionList";
    //获得业主认证信息
    public static String ProprietorUrl         = "pm.ppt.proprietor.get";
    //获取大分类接口
    public static String serviceListUrl         = "pm.ppt.service.kindList";
    //获取温馨提示列表
    public static String noticeUrl             = "pm.ppt.propertyNotice.queryPropertyNoticeList";
    //获取小区列表
    public static String CommunitylistUrl      = "pm.ppt.community.list";
    //获取物业公告详情
    public static String noticedetailsUrl      = "pm.ppt.propertyNotice.queryPropertyNoticeInfo";
    //获取访问列表
    public static String VisitlistUrl          = "pm.ppt.communityVisit.queryCommunityVisitList";
    //修改公告状态为已读
    public static String NoticeModifyUrl       = "pm.ppt.memberNoticeRelation.updatePropertyNoticeStatus";
    //到访详情
    public static String visitiinfoUrl         = "pm.ppt.communityVisit.queryCommunityVisitInfo";
    //处理到访信息
    public static String HandleVisitUrl        = "pm.ppt.communityVisit.handleCommunityVisit";
    //添加到访信息
    public static String addvisitUrl           = "pm.ppt.communityVisit.addCommunityVisit";
    //周边商户列表
    public static String surroundListUrl           = "pm.ppt.service.list";
    //获取会员基本信息
    public static String getmemberinfoUrl      = "pm.ppt.members.get";
    //修改会员头像
    public static String memberHeadImgUrl      = "pm.ppt.members.modifyHeadImg";
    //e管家列表
    public static String EStewardListUrl       = "pm.ppt.eSteward.selectInstantMessaging";
    //e管家详情列表
    public static String EStewardListDetailUrl = "pm.ppt.eSteward.selectMessagingById";
    //获取投诉列表
    public static String complaintListUrl      = "pm.ppt.complaint.list";

    //获取投诉类型
    public static String complaintTypeUrl = "pm.common.kind.list";

    //获取房屋列表
    public static String houseListUrl = "pm.ppt.memberBind.list";

    //管理员获取房屋列表
    public static String superCommunityListUrl = "pm.stf.community.communityList";

    //解除房屋绑定
    public static String removeHouseBind = "pm.ppt.memberBind.removeBind";

    //设置默认房屋
    public static String setDefaultHouse = "pm.ppt.memberBind.setDefault";

    //获取论坛的所有板块
    public static String getForumDateUrl      = "pm.ppt.forumCategory.queryForumCategoryList";
    //获取所有帖子列表
    public static String getForumnoticlistUrl = "pm.ppt.forumPost.queryForumPostList";
    //获取帖子详情
    public static String getDetailinfoUrl     = "pm.ppt.forumPost.queryForumPostDetl";
    //点赞帖子
    public static String setLikeNoteUrl       = "pm.ppt.PostPraise.addPostPraise";
    //取消点赞帖子
    public static String CancleNoteUrl        = "pm.ppt.PostPraise.delPostPraise";
    //回复帖子
    public static String CommentNoteUrl       = "pm.ppt.postReply.addPostReply";
    //发布投诉
    public static String releaseComplaint     = "pm.ppt.complaint.complaint";
    //投诉详情
    public static String complaintDeatil      = "pm.ppt.complaint.get";
    //评价投诉
    public static String evaluationComplaint  = "pm.ppt.complaint.evaluate";
    //发布帖子
    public static String PostMessageUrl       = "pm.ppt.forumPost.addForumPost";
    //获取周围商家
    public static String requestShopListUrl   = "pm.ppt.companysAround.get";
    //认证小区
    public static String communityList        = "pm.ppt.community.list";
    //楼栋列表/单元列表/房间列表
    public static String buildingList         = "pm.ppt.communityAddress.list";
    //认证
    public static String proprietyVerify      = "pm.ppt.proprietor.verify";
    //报修列表
    public static String repairList           = "pm.ppt.repairDemand.list";
    //报修详情
    public static String repairDeatil         = "pm.ppt.repairDemand.get";
    //报修评价
    public static String repairEvaluate       = "pm.ppt.repairDemand.evaluate";
    //发布报修
    public static String releaseRepair        = "pm.ppt.repairDemand.requestRepair";
    //发布活动
    public static String PostActionUrl        = "pm.ppt.action.addAction";
    //版本信息
    public static String updateApkVersions    = "pm.ppt.apkVersions.updateApkVersions";
    //指动登录
    public static String zhidongLoginUrl    = "zhidongH5/html";
    //获取团购商品信息列表
    public static String productListUrl    = "pm.ppt.newProduct.list";
    //获取团购商品详情
    public static String productDetUrl    = "pm.ppt.newProduct.get";
    //获取默认地址
    public static String defaultAddressUrl    = "pm.ppt.newReceiveAddress.getdefault";
    //获取会员收货地址列表
    public static String addressListUrl    = "pm.ppt.receiveAddress.list";
    //删除会员收货地址
    public static String deleteAddressUrl    = "pm.ppt.receiveAddress.delete";
    //设置会员默认收货地址
    public static String setDefaultAddressUrl    = "pm.ppt.receiveAddress.setdefault";
    //修改收货地址
    public static String modifyAddressUrl    = "pm.ppt.receiveAddress.modify";
    //添加收货地址
    public static String addAddressUrl    = "pm.ppt.receiveAddress.add";
    //生成团购商品订单
    public static String generateOrderUrl    = "pm.ppt.newOrder.generateOrder";
    //评论列表
    public static String commentListUrl    = "pm.ppt.newOrderEvaluation.list";
    //订单列表
    public static String orderListUrl    = "pm.ppt.newOrder.list";
    //会员评价订单
    public static String orderEvaluateUrl    = "pm.ppt.newOrderEvaluation.evaluate";
    //会员删除订单
    public static String orderDeleteUrl    = "pm.ppt.newOrder.delete";
    //会员确认收货
    public static String orderReceiveUrl    = "pm.ppt.newOrder.receive";

}
