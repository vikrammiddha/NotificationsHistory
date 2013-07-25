package com.bun.notificationshistory;

import android.graphics.drawable.Drawable;

public class Notification {
	
	Boolean isRowChecked;
	
	public Boolean getIsRowChecked() {
		return isRowChecked;
	}
	public void setIsRowChecked(Boolean isRowChecked) {
		this.isRowChecked = isRowChecked;
	}
	String sender;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	String lastActivityDate;
	
	public String getLastActivityDate() {
		return lastActivityDate;
	}
	public void setLastActivityDate(String lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}
	Drawable appIcon;
	
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	private Integer notificationCount;
	
	public Integer getNotificationCount() {
		return notificationCount;
	}
	public void setNotificationCount(Integer notificationCount) {
		this.notificationCount = notificationCount;
	}
	private String sectionHeaderValue;
	public String getSectionHeaderValue() {
		return sectionHeaderValue;
	}
	public void setSectionHeaderValue(String sectionHeaderValue) {
		this.sectionHeaderValue = sectionHeaderValue;
	}
	private Boolean isSectionHeader;
	public Boolean getIsSectionHeader() {
		return isSectionHeader;
	}
	public void setIsSectionHeader(Boolean isSectionHeader) {
		this.isSectionHeader = isSectionHeader;
	}
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNotDate() {
		return notDate;
	}
	public void setNotDate(String notDate) {
		this.notDate = notDate;
	}
	public String getNotTime() {
		return notTime;
	}
	public void setNotTime(String notTime) {
		this.notTime = notTime;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	private String notDate;
	private String notTime;
	private String appName;
	private String packageName;

}
