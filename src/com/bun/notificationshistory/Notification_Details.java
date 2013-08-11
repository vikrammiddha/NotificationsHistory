package com.bun.notificationshistory;

import java.util.ArrayList;
import java.util.HashMap;

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
			Notification n = Utils.getNotificationData(hm, this, true);
			adapter.addNotification(n);
		}
			
		layout.setAdapter(adapter);
		
	}


}
