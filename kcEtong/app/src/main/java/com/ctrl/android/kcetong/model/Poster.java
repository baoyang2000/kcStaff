package com.ctrl.android.kcetong.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Poster implements Parcelable{

	private String actId;
	private String actName;
	private String adAppImg;
	private String appImg;//图片地址
	private String popupAd;
	private String wapUrl;//要跳转的url

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getAdAppImg() {
		return adAppImg;
	}

	public void setAdAppImg(String adAppImg) {
		this.adAppImg = adAppImg;
	}

	public String getAppImg() {
		return appImg;
	}

	public void setAppImg(String appImg) {
		this.appImg = appImg;
	}

	public String getPopupAd() {
		return popupAd;
	}

	public void setPopupAd(String popupAd) {
		this.popupAd = popupAd;
	}

	public String getWapUrl() {
		return wapUrl;
	}

	public void setWapUrl(String wapUrl) {
		this.wapUrl = wapUrl;
	}

	@Override
	public String toString() {
		return "Poster{" +
				"actId='" + actId + '\'' +
				", actName='" + actName + '\'' +
				", adAppImg='" + adAppImg + '\'' +
				", appImg='" + appImg + '\'' +
				", popupAd='" + popupAd + '\'' +
				", wapUrl='" + wapUrl + '\'' +
				'}';
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.actId);
		dest.writeString(this.actName);
		dest.writeString(this.adAppImg);
		dest.writeString(this.appImg);
		dest.writeString(this.popupAd);
		dest.writeString(this.wapUrl);
	}

	public Poster() {
	}

	protected Poster(Parcel in) {
		this.actId = in.readString();
		this.actName = in.readString();
		this.adAppImg = in.readString();
		this.appImg = in.readString();
		this.popupAd = in.readString();
		this.wapUrl = in.readString();
	}

	public static final Creator<Poster> CREATOR = new Creator<Poster>() {
		@Override
		public Poster createFromParcel(Parcel source) {
			return new Poster(source);
		}

		@Override
		public Poster[] newArray(int size) {
			return new Poster[size];
		}
	};
}
