package com.bun.notificationshistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.graphics.drawable.Drawable;

import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


public class Notification_Activity extends Activity{
	
	Notification_Adapter adapter;
	DBController controller;
	ListView layout;
	private String accServiceId = "com.bun.notificationshistory/.Notification_Service";
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_main);
		adapter = new Notification_Adapter();
		layout = (ListView) findViewById(R.id.notificationsListViewId);
		controller = new DBController(this);	
		populateNotificationAdapter(layout);
		Boolean serviceStatus = isAccessibilityEnabled(this, accServiceId);
		if(serviceStatus == false){
			showServiceAlert();
		}
		
        	
	}
	

	private DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {
	   public void onReceive(Context context, Intent intent) {
		   layout = (ListView) findViewById(R.id.notificationsListViewId);
		   adapter.clearNotifications();
		   populateNotificationAdapter(layout);
		   adapter.notifyDataSetChanged();
	   }

	};
	
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification_main_menu, menu);
		return true;
	}
	
	public boolean onPrepareOptionsMenu (Menu menu) {    
	    
		for(int i=0; i< menu.size(); i++){
			MenuItem mi = menu.getItem(i);
			if(i == 0){
				
				HashMap<String,String> preferences = controller.getAllPreferences();
				
				if("GroupByDay".equals(preferences.get("NotificationGroupBy"))){
					
					mi.setTitle("Group by Apps");
					
				}else if("GroupByApp".equals(preferences.get("NotificationGroupBy"))){
					
					mi.setTitle("Group by Days");
					
				}
				
			}
		}
			        
	    return super.onPrepareOptionsMenu(menu);
	}
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	 	 
        switch (item.getItemId())
        {
        case R.id.menu_clear:	            
        	showClearWarning();

 
        case R.id.meni_Exit:
        		        
        	
        case R.id.group_notifications:
        	updateGroupNotificationPreference();
        	
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
	
	private void showClearWarning(){
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
		        Notification_Activity.this);

		// Setting Dialog Title
		alertDialog2.setTitle("Warning");

		// Setting Dialog Message
		alertDialog2.setMessage(R.string.clear_warning);

		// Setting Icon to Dialog
		//alertDialog2.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("YES",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	if(layout == null){
		            		layout = (ListView) findViewById(R.id.notificationsListViewId);
		            	}
		            	controller.deleteAllNotifications();
		            	adapter.clearNotifications();
		            	populateDefaultMessage(layout);
		            	adapter.notifyDataSetChanged();
			        	layout.setAdapter(adapter);
			        	
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("NO",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               
		                dialog.cancel();
		            }
		        });

		// Showing Alert Dialog
		alertDialog2.show();
	}
	
	private void showServiceAlert(){
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
		        Notification_Activity.this);

		// Setting Dialog Title
		alertDialog2.setTitle("Notifications History service");

		// Setting Dialog Message
		alertDialog2.setMessage(R.string.service_warning);

		// Setting Icon to Dialog
		//alertDialog2.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("ACTIVATE",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS); 
		            	startActivityForResult(intent, 0);
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("LATER",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               
		                dialog.cancel();
		            }
		        });

		// Showing Alert Dialog
		alertDialog2.show();
	}
	
	private void populateNotificationAdapter(ListView layout){

		HashMap<String,String> preferences = controller.getAllPreferences();
		
		if("GroupByDay".equals(preferences.get("NotificationGroupBy"))){
			
			populateAdapterGroupedByDay();
			
		}else if("GroupByApp".equals(preferences.get("NotificationGroupBy"))){
			
			populateAdapterGroupedByApp();
			
		}
		
		
		layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	Notification n = (Notification)adapter.getItem(position);
		    	Intent intent=new Intent(getApplicationContext(), Notification_Details.class);
		    	intent.putExtra("date", n.getNotTime());
		    	intent.putExtra("app", n.getAppName());
		    	if(n.getIsSectionHeader() == null)
		    		startActivity(intent);
		    }
		});
		layout.setAdapter(adapter);
		
	}
	
	private void populateAdapterGroupedByDay(){
		Notification n ;
		ArrayList<HashMap<String, String>> data = controller.getAllNotifications();
		HashMap<String,String> appPackageMap = new HashMap<String,String>();
		HashMap<String,String> appLastTimeMap = new HashMap<String,String>();
		
		if(data != null && data.size() > 0){
			Boolean isSectionHeader = true;
			String initialDate = data.get(0).get("notTime");
			LinkedHashMap<String,Integer> appMap = new LinkedHashMap<String,Integer>();
			
			Integer counter = 0;
			
			for(HashMap<String,String> hm : data){
				
				appPackageMap.put(hm.get("appName"), hm.get("packageName"));
			
				counter ++;
				
				if(isSectionHeader){
					n = new Notification();
					n.setIsSectionHeader(true);
					n.setSectionHeaderValue(hm.get("notTime"));
					isSectionHeader = false;
					adapter.addNotification(n);
				}
				
				if(initialDate.equals(hm.get("notTime"))){
					if(appMap.get(hm.get("appName")) != null){
						appMap.put(hm.get("appName"), appMap.get(hm.get("appName")) + 1);
						appLastTimeMap.put(hm.get("appName"), hm.get("notTime") + "  " +hm.get("notDate"));
					}else{
						appMap.put(hm.get("appName") , 1);
					}
					
					if(counter == data.size()){
						if(appMap.size() > 0){
							appMap = Utils.sortHashMapByValuesD(appMap);
							for(String app : appMap.keySet()){
								Notification nn = new Notification();
								nn.setAppName(app);
								nn.setNotificationCount(appMap.get(app));
								nn.setNotTime(initialDate);
								nn.setLastActivityDate(appLastTimeMap.get(app));
								try{
									
									Drawable icon;
									if(app.equals("Google Talk")){
										icon = getResources().getDrawable( R.drawable.googletalk );
									}else{
										icon = this.getPackageManager().getApplicationIcon(appPackageMap.get(app));
									}
									nn.setAppIcon(icon);
								}
								catch (PackageManager.NameNotFoundException ne)
								 {

								 }
								adapter.addNotification(nn);
							}
						}
					}
				}else{
					isSectionHeader = true;
					if(appMap.size() > 0){
						appMap = Utils.sortHashMapByValuesD(appMap);
						for(String app : appMap.keySet()){
							Notification nn = new Notification();
							nn.setAppName(app);							
							nn.setNotificationCount(appMap.get(app));
							nn.setNotTime(initialDate);
							nn.setLastActivityDate(appLastTimeMap.get(app));
							try{
								Drawable icon;
								if(app.equals("Google Talk")){
									
									icon = getResources().getDrawable( R.drawable.googletalk );
								}else{
									icon = this.getPackageManager().getApplicationIcon(appPackageMap.get(app));
								}
								nn.setAppIcon(icon);
							}
							catch (PackageManager.NameNotFoundException ne)
							 {

							 }
							adapter.addNotification(nn);
						}
					}
					appMap.clear();
					appLastTimeMap.clear();
					initialDate = hm.get("notTime");
				}
				
			}
		}else{
			populateDefaultMessage(layout);
		}
	}
	
	
	
	private void populateAdapterGroupedByApp(){
		
		ArrayList<HashMap<String, String>> data = controller.getAllNotifications();
		HashMap<String,String> appPackageMap = new HashMap<String,String>();
		HashMap<String,String> appLastTimeMap = new HashMap<String,String>();
		
		if(data != null && data.size() > 0){
			
			LinkedHashMap<String,Integer> appMap = new LinkedHashMap<String,Integer>();
			
			Integer counter = 0;
			
			for(HashMap<String,String> hm : data){
				
				appPackageMap.put(hm.get("appName"), hm.get("packageName"));
				
				if(appMap.get(hm.get("appName")) != null){
					appMap.put(hm.get("appName"), appMap.get(hm.get("appName")) + 1);
					appLastTimeMap.put(hm.get("appName"), hm.get("notTime") + "  " +hm.get("notDate"));
				}else{
					appMap.put(hm.get("appName") , 1);
				}
				
			}
			
			if(appMap.size() > 0){
				appMap = Utils.sortHashMapByValuesD(appMap);
				for(String app : appMap.keySet()){
					Notification nn = new Notification();
					nn.setAppName(app);
					nn.setNotificationCount(appMap.get(app));					
					nn.setLastActivityDate(appLastTimeMap.get(app));
					try{
						
						Drawable icon;
						if(app.equals("Google Talk")){
							icon = getResources().getDrawable( R.drawable.googletalk );
						}else{
							icon = this.getPackageManager().getApplicationIcon(appPackageMap.get(app));
						}
						nn.setAppIcon(icon);
					}
					catch (PackageManager.NameNotFoundException ne)
					 {

					 }
					adapter.addNotification(nn);
				}
			}
		}else{
			populateDefaultMessage(layout);
		}
		
		
	}

	
	
	
	private void populateDefaultMessage(ListView layout){
		Notification n1 = new Notification();
		Notification n2 = new Notification();
		n1.setIsSectionHeader(true);
		n1.setSectionHeaderValue("Today");
		n2.setAppName("No Notifications till now. ");
		n2.setNotificationCount(-1);
		adapter.addNotification(n1);
		adapter.addNotification(n2);
		layout.setAdapter(adapter);
	}
	
	
	
	public static boolean isAccessibilityEnabled(Context context, String id) {

	    AccessibilityManager am = (AccessibilityManager) context
	            .getSystemService(Context.ACCESSIBILITY_SERVICE);

	    List<AccessibilityServiceInfo> runningServices = am
	            .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
	    for (AccessibilityServiceInfo service : runningServices) {
	        if (id.equals(service.getId())) {
	            return true;
	        }
	    }

	    return false;
	}
	
	private void updateGroupNotificationPreference(){
		
		
		
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> preferences = controller.getAllPreferences();
		
		if("GroupByDay".equals(preferences.get("NotificationGroupBy"))){
			
			map.put("NotificationGroupBy", "GroupByApp");
			
		}else if("GroupByApp".equals(preferences.get("NotificationGroupBy"))){
			
			map.put("NotificationGroupBy", "GroupByDay");
			
		}
		
		controller.updatePreferences(map);
		
		adapter.clearNotifications();
    	populateNotificationAdapter(layout);
    	adapter.notifyDataSetChanged();
    	layout.setAdapter(adapter);
		
	}

}
