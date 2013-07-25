package com.bun.notificationshistory;

import java.util.HashMap;
import java.util.HashSet;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.ListView;

public class Ignored_Apps_Activity extends Activity{
	
	ListView layout;
	DBController controller;
	Ignored_Apps_Adapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ignored_apps);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				
		adapter = new Ignored_Apps_Adapter();
		layout = (ListView) findViewById(R.id.ignoredAppsListViewId);		
		controller = new DBController(this);
		populateListView();
	}
	
	public void removeIgnoredApps(View view){
		HashSet<String> ignoredApps = new HashSet<String>();
		
		for(Notification n : adapter.getNotifications()){
			if(n.isRowChecked != null && n.isRowChecked){
				ignoredApps.add(n.getAppName());
			}
		}
		
		if(ignoredApps.size() > 0){
			controller.deleteIgnoredApps(ignoredApps);        	
        	//adapter.clearNotifications();
        	//populateListView();
        	//adapter.notifyDataSetChanged();
        	//layout.setAdapter(adapter);
			
		}
		
		finish();
		
	}

	private void populateListView(){
		HashSet<String> ignoredApps = new HashSet<String>();
		ignoredApps = controller.getAllIgnoredApps();
		HashMap<String,String> appPackageMap = new HashMap<String,String>();
		appPackageMap = controller.getAppPackageMap();
		
		for(String app : ignoredApps){
			Notification n = new Notification();
			n.setAppName(app);
			Drawable icon;
			try{
				if(app.equals("Google Talk")){
					icon = getResources().getDrawable( R.drawable.googletalk );
				}else{
					icon = this.getPackageManager().getApplicationIcon(appPackageMap.get(app));
					
				}
				n.setAppIcon(icon);
			}catch(Exception e){
				e.printStackTrace();
			}
			adapter.addNotification(n);
		}
		
		layout.setAdapter(adapter);
	}

}
