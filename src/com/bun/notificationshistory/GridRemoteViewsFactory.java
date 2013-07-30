package com.bun.notificationshistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class GridRemoteViewsFactory implements RemoteViewsFactory{

	private static final int mCount = 10;
    private List<Notification> notList = new ArrayList<Notification>();    
    private Context mContext;
    private int mAppWidgetId;
    private DBController controller;
    
    public GridRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        controller = new DBController(mContext);
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notList.size();
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public RemoteViews getLoadingView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_cell);
	    rv.setTextViewText(R.id.widgetCountId, String.valueOf(notList.get(position).getNotificationCount()));	    
	    rv.setImageViewBitmap(R.id.widgetImageId, ((BitmapDrawable) notList.get(position).getAppIcon()).getBitmap());
	   
	    return rv;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onCreate() {
		HashMap<String,Integer> widgetData = controller.getPieGraphData();
		HashMap<String,String> appPackage = controller.getAppPackageMap();
		LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
		
		sortedMap.putAll(widgetData);
		
		sortedMap = Utils.sortHashMapByValuesD(sortedMap);
		
		for(String app : sortedMap.keySet()){
			Notification n = new Notification();
			n.setAppName(app);
			try{
				Drawable icon;
				if(app.equals("Google Talk")){
					icon = mContext.getResources().getDrawable( R.drawable.googletalk );
				}else{
					icon = mContext.getPackageManager().getApplicationIcon(appPackage.get(app));
				}
				n.setAppIcon(icon);
				n.setNotificationCount(sortedMap.get(app));
				notList.add(n);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public void onDataSetChanged() {
		Log.d("changed", "changed");
		notList.clear();
		HashMap<String,Integer> widgetData = controller.getPieGraphData();
		HashMap<String,String> appPackage = controller.getAppPackageMap();
		LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
		
		sortedMap.putAll(widgetData);
		
		sortedMap = Utils.sortHashMapByValuesD(sortedMap);
		
		for(String app : sortedMap.keySet()){
			Notification n = new Notification();
			n.setAppName(app);
			try{
				Drawable icon;
				if(app.equals("Google Talk")){
					icon = mContext.getResources().getDrawable( R.drawable.googletalk );
				}else{
					icon = mContext.getPackageManager().getApplicationIcon(appPackage.get(app));
				}
				n.setAppIcon(icon);
				n.setNotificationCount(sortedMap.get(app));
				notList.add(n);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
}
