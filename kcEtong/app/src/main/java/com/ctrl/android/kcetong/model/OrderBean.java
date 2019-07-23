package com.ctrl.android.kcetong.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liu on 2018/3/7.
 */

public class OrderBean implements Serializable{
    /**
     * method : pm.ppt.order.list
     * level : Info
     * code : 000
     * description : 查询成功！
     * data : {"orderList":[{"id":"4a2971133f4d4c76a350b7e1fd44788a","productName":"无底洞","orderNum":"1520234091925563","createTime":1520234092000,"pointsDeductions":0,"sellType":"0","payType":"0","receiverName":"经常会遇到","receiverMobile":"18853176543","originalPrice":1,"sellingPrice":11,"nums":1,"totalCost":11,"address":"山东省淄博市博山区崇文区崇文区cghcvjnvccnvgnc！你给成功 vv 巨好yfihifgfuudg统筹兼顾价格从也不会低的情况和你联系吧、这些是你给你发个","orderStatus":"0","index":0,"rowCountPerPage":0},{"id":"dd393c07d4a745ca9693e7f48df15dc2","productName":"无底洞","orderNum":"15202335343473430","createTime":1520233534000,"pointsDeductions":0,"sellType":"0","payType":"0","receiverName":"经常会遇到","receiverMobile":"18853176543","originalPrice":1,"sellingPrice":11,"nums":2,"totalCost":22,"address":"山东省淄博市博山区崇文区崇文区cghcvjnvccnvgnc！你给成功 vv 巨好yfihifgfuudg统筹兼顾价格从也不会低的情况和你联系吧、这些是你给你发个","orderStatus":"0","index":0,"rowCountPerPage":0},{"id":"b8d843b90fa64137918a8236e234e19d","productName":"无底洞","orderNum":"15202318652853218","createTime":1520231865000,"pointsDeductions":0,"sellType":"0","payType":"0","receiverName":"经常会遇到","receiverMobile":"18853176543","originalPrice":1,"sellingPrice":11,"nums":2,"totalCost":22,"address":"山东省淄博市博山区崇文区崇文区cghcvjnvccnvgnc！你给成功 vv 巨好yfihifgfuudg统筹兼顾价格从也不会低的情况和你联系吧、这些是你给你发个","orderStatus":"0","index":0,"rowCountPerPage":0}]}
     */

    private String method;
    private String   level;
    private String   code;
    private String   description;
    private DataBean data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private List<OrderListBean> orderList;

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean implements Serializable{
            /**
             * id : 4a2971133f4d4c76a350b7e1fd44788a
             * productName : 无底洞
             * orderNum : 1520234091925563
             * createTime : 1520234092000
             * pointsDeductions : 0
             * sellType : 0
             * payType : 0
             * receiverName : 经常会遇到
             * receiverMobile : 18853176543
             * originalPrice : 1.0
             * sellingPrice : 11.0
             * nums : 1
             * totalCost : 11.0
             * address : 山东省淄博市博山区崇文区崇文区cghcvjnvccnvgnc！你给成功 vv 巨好yfihifgfuudg统筹兼顾价格从也不会低的情况和你联系吧、这些是你给你发个
             * orderStatus : 0
             * index : 0
             * rowCountPerPage : 0
             */

            private String id;
            private String productName;
            private String orderNum;
            private long   createTime;
            private int    pointsDeductions;
            private String sellType;
            private String payType;
            private String receiverName;
            private String receiverMobile;
            private double originalPrice;
            private double sellingPrice;
            private int    nums;
            private double totalCost;
            private String address;
            private String orderStatus;
            private int    index;
            private String    level;
            private String    content;
            private int    rowCountPerPage;
            private String listPicUrl;//商品列表展示图片Url

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getListPicUrl() {
                return listPicUrl;
            }

            public void setListPicUrl(String listPicUrl) {
                this.listPicUrl = listPicUrl;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getPointsDeductions() {
                return pointsDeductions;
            }

            public void setPointsDeductions(int pointsDeductions) {
                this.pointsDeductions = pointsDeductions;
            }

            public String getSellType() {
                return sellType;
            }

            public void setSellType(String sellType) {
                this.sellType = sellType;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getReceiverName() {
                return receiverName;
            }

            public void setReceiverName(String receiverName) {
                this.receiverName = receiverName;
            }

            public String getReceiverMobile() {
                return receiverMobile;
            }

            public void setReceiverMobile(String receiverMobile) {
                this.receiverMobile = receiverMobile;
            }

            public double getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(double originalPrice) {
                this.originalPrice = originalPrice;
            }

            public double getSellingPrice() {
                return sellingPrice;
            }

            public void setSellingPrice(double sellingPrice) {
                this.sellingPrice = sellingPrice;
            }

            public int getNums() {
                return nums;
            }

            public void setNums(int nums) {
                this.nums = nums;
            }

            public double getTotalCost() {
                return totalCost;
            }

            public void setTotalCost(double totalCost) {
                this.totalCost = totalCost;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getRowCountPerPage() {
                return rowCountPerPage;
            }

            public void setRowCountPerPage(int rowCountPerPage) {
                this.rowCountPerPage = rowCountPerPage;
            }
        }
    }
}
