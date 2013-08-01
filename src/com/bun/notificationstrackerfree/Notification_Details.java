package com.bun.notificationstrackerfree;

import java.util.ArrayList;
import java.util.HashMap;

import com.bun.notificationstrackerfree.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;


public class Notification_Details extends Activity{
	
	Notiofication_Detail_Adapter adapter;
	DBController controller;
	ListView layout;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		unregisterReceiver(mReceiver);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		IntentFilter intentFilter = new IntentFilter(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
        registerReceiver(mReceiver, intentFilter);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_details);
		adapter = new Notiofication_Detail_Adapter();
		layout = (ListView) findViewById(R.id.notificationsDetailsListViewId);
		controller = new DBController(this);
		populateNotificationDetails(layout);
		IntentFilter intentFilter = new IntentFilter(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED);
        registerReceiver(mReceiver, intentFilter);        
        
	}
	
	private DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
	   public void onReceive(Context context, Intent intent) {
		   layout = (ListView) findViewById(R.id.notificationsDetailsListViewId);
		   adapter.clearNotifications();
		   populateNotificationDetails(layout);
		   adapter.notifyDataSetChanged();
	   }

	};

		
	public void populateNotificationDetails(ListView layout){
		
		String notDate = getIntent().getExtras().getString("date");
		String appName = getIntent().getExtras().getString("app");
		
		ArrayList<HashMap<String, String>> data = controller.getNotificationDetails(notDate, appName);
		
		for(HashMap<String, String> hm : data){
			
			try{
			
				Notification n = new Notification();
				
				String message = "";
				
				message = getMessageBody(hm);
				
				n.setMessage(message);
				
				String sender = getSender(hm, 0);
				
				if(sender == null || sender.trim().length() == 0){
					sender = getSender(hm, 1);
				}
				
				if(sender == null || sender.trim().length() == 0){
					continue;
				}
				
				n.setSender(sender);
				n.setNotDate(hm.get("notTime") + "  " + hm.get("notDate"));
				Drawable icon;
				if(hm.get("appName").equals("Google Talk")){
					icon = getResources().getDrawable( R.drawable.googletalk );
				}else{
					icon = this.getPackageManager().getApplicationIcon(hm.get("packageName"));
				}
				n.setAppIcon(icon);
				
				adapter.addNotification(n);
			}catch(Exception e){
				Log.e("Notification_Details", "Exception in Handlingthe Event : " + e);
			}
		}
		
		layout.setAdapter(adapter);
		
	}
	
	private String getSender(HashMap<String,String> hm, Integer bol){
		
		StringBuilder retMessage = new StringBuilder("");
		
		if(bol == 0){
		
			String[] strArr = hm.get("message").split(":");
			
			for(Integer i = 0; i< strArr.length ;i++){
				if(i == 0){
					retMessage.append(strArr[i]).append(" ");
				}			
			}
		}else{
			
			String[] strArr = hm.get("additionalInfo").split("\n");
			
			for(Integer i = 0; i< strArr.length ;i++){
				if(i == 0){
					retMessage.append(strArr[i]).append(" ");
				}			
			}
			
		}
		
		String retString = retMessage.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				
		return retString;
		
		
	}
	
	private String getMessageBody(HashMap<String,String> hm){
		
		StringBuilder retMessage = new StringBuilder("");
		String[] strArr = hm.get("additionalInfo").split("\n");
		
		for(Integer i = 0; i< strArr.length ;i++){
			if(i == 2){
				retMessage.append(strArr[i]).append(" ");
			}
		}
			
		
		String message = retMessage.toString();		

		if(message.length() > 100){
			message = message.substring(0, 100);
		}
		
		message += "\n\n" + hm.get("notTime") + "  " + hm.get("notDate");

		return message;
	}

}
