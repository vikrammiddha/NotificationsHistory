package com.bun.notificationshistory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.os.Bundle;
import android.os.Parcelable; 
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;
import android.widget.Toast;
import android.widget.TextView;


public class Notification_Service extends AccessibilityService {
	
	HashMap<String, String> mInstalledApplications = new HashMap<String, String>();
    HashMap<String, String> mLastMessage = new HashMap<String, String>();
    HashMap<String, Long> mLastTimeStamp = new HashMap<String, Long>();
    DBController controller;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		try{
			HashMap<String,String> dbMap = new HashMap<String,String>();
			String message = "";
			String appName = getApplicationName(event.getPackageName().toString());
            Log.d("NotificationHistory", "[PackageName]         " + event.getPackageName());
            Log.d("NotificationHistory", "[Application]         " + appName);
            Log.d("NotificationHistory", "[EventTime]           " + event.getEventTime());
            Log.d("NotificationHistory", "[EventText]           " + String.valueOf(event.getText()));
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            
            dbMap.put("appName", appName);
            dbMap.put("notTime", date);
            dbMap.put("packageName", event.getPackageName().toString());
            
            try{
	            Notification notification = (Notification) event.getParcelableData();
	            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            ViewGroup localView = (ViewGroup) inflater.inflate(notification.contentView.getLayoutId(), null);
	            notification.contentView.reapply(getApplicationContext(), localView);
	            
	            ArrayList<TextView> views = new ArrayList<TextView>();
	            getAllTextView(views, localView);
	            for (TextView v: views) {
	                String text = v.getText().toString();
	                if (!text.isEmpty()) {
	                    Log.d("Notification_History", "[Text]                " + text);
	                    message += text + "\n";
	                }
	            }
            }catch(Exception e){
            	message = String.valueOf(event.getText());
            }
           
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String formattedDate = formatter.format(calendar.getTime());
           
            
            dbMap.put("notDate", formattedDate);
            dbMap.put("message", String.valueOf(event.getText()));
            dbMap.put("additionalInfo", message);
            Log.d("Notification_History", "Total Records in DB till now :" + controller.getAllNotifications().size());
            if((message != null && !message.equals("[]")) && !("[]".equals(event.getText())))
            	controller.insertNotification(dbMap, this);
		}catch(Exception e){
			 Log.e("NotificationHistory", "Exception in Handlingthe Event : " + e);
		}
	   }
	@Override
	public void onInterrupt() {
	    // TODO Auto-generated method stub.
	
	}
	
	@Override
	protected void onServiceConnected() {
		try{
			refreshApplicationList();
	        mLastMessage.clear();
	        mLastTimeStamp.clear();
	        controller = new DBController(this);
	        Log.d("NotificationHistory", "notification service started.");
	        
			//AccessibilityServiceInfo info = new AccessibilityServiceInfo();
			//info.feedbackType = 1;
			//info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
			//info.notificationTimeout = 100; 
			//info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
			//setServiceInfo(info);
		}catch(Exception e){
			Log.e("NotificationHistory", "Failed to configure accessibility service", e);
		}
      
     }
	
	private String getApplicationName(String packageName) {
        String name = packageName;
        
        if (mInstalledApplications.containsKey(packageName)) {
            name = mInstalledApplications.get(packageName);
        } else {
            refreshApplicationList();
            if (mInstalledApplications.containsKey(packageName)) {
                name = mInstalledApplications.get(packageName);
            }
        }
        
        return name;
    }
	
	private void refreshApplicationList() {
        final PackageManager pm = getBaseContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        
        for (ApplicationInfo packageInfo : packages) {
            mInstalledApplications.put(packageInfo.packageName, packageInfo.loadLabel(pm).toString());
        }
    } 
	
	   private void getAllTextView(ArrayList<TextView> views, ViewGroup v)
	    {
	        if (null == views) {
	            return;
	        }
	        for (int i = 0; i < v.getChildCount(); i++)
	        {
	            Object child = v.getChildAt(i); 
	            if (child instanceof TextView)
	            {
	                views.add((TextView)child);
	            }
	            else if(child instanceof ViewGroup)
	            {
	                getAllTextView(views, (ViewGroup)child);  // Recursive call.
	            }
	        }
	    }
	
}
