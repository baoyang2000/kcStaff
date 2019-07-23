package com.ctrl.android.kcetong.model;

import java.io.Serializable;

/**
 * 分享内容
 * Created by Qiu on 2016/3/31.
 */
public class ShareModel implements Serializable {
    private static final long serialVersionUID = 411977060559401774L;
    private String shareTitle;
    private String shareUrl;
    private String shareContent;
    private String shareLogo;
    private static final String defualtUrl = "http://www.baidu.com/";
    private static final String defualtLogoUrl = "http://www.baidu.com/";
    public ShareModel() {
    }

    public ShareModel(String title, String content, String url, String logo) {
        this.shareTitle = ((title != null && !title.trim().equals("")) ? title : "延安教育");
        this.shareContent = ((content == null || content.trim().equals("")) ?  "延安教育":content);
        boolean d =content.trim().equals("");
        boolean e = content == null;
        this.shareUrl = ((url != null && !url.trim().equals("")) ? url : defualtUrl);
        this.shareLogo = ((logo != null && !logo.trim().equals("")) ? logo : defualtLogoUrl);
    }

    public String getShareTitle() {
        return shareTitle == null ? "" : shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareUrl() {
        return shareUrl == null ? "" : shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareContent() {
        return shareContent == null ? "" : shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareLogo() {
        return shareLogo == null ? "" : shareLogo;
    }

    public void setShareLogo(String shareLogo) {
        this.shareLogo = shareLogo;
    }
}
