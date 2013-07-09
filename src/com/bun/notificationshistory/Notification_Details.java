package com.bun.notificationshistory;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;


public class Notification_Details extends Activity{
	
	Notiofication_Detail_Adapter adapter;
	DBController controller;
	ListView layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_details);
		adapter = new Notiofication_Detail_Adapter();
		layout = (ListView) findViewById(R.id.notificationsDetailsListViewId);
		controller = new DBController(this);
		populateNotificationDetails(layout);
	}
	
	public void populateNotificationDetails(ListView layout){
		
		String notDate = getIntent().getExtras().getString("date");
		String appName = getIntent().getExtras().getString("app");
		
		ArrayList<HashMap<String, String>> data = controller.getNotificationDetails(notDate, appName);
		
		for(HashMap<String, String> hm : data){
			
			Notification n = new Notification();
			
			String message = getAppSpecificMessage(hm.get("additionalInfo"), hm.get("appName"));
			
			message += hm.get("message");
			
			if(message.length() > 100){
				message = message.substring(0, 100);
			}
			n.setMessage(message.replaceAll("\n", ""));
			n.setNotDate(hm.get("notDate"));
			
			adapter.addNotification(n);
		}
		
		layout.setAdapter(adapter);
		
	}
	
	private String getAppSpecificMessage(String message, String app){
		
		if("GOOGLE TALK".equals(app.toUpperCase())){
			return "";
		}else{
			return "";
		}
	}

}
