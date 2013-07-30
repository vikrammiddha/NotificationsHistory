package com.bun.notificationshistory;

import java.util.Calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


public class Not_Widget extends AppWidgetProvider{
	
	private PendingIntent service = null;  
	
	@Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
    {
		
		
		 // update each of the app widgets with the remote adapter
	    for (int i = 0; i < appWidgetIds.length; ++i) {
	    	
	        
	        Intent intent = new Intent(context, GridWidgetService.class);
	        
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
	        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
	        
	        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	        
	        rv.setRemoteAdapter(appWidgetIds[i], R.id.widgetGridViewId, intent);
	        
	        
	        rv.setEmptyView(R.id.widgetGridViewId, R.id.enptyWidgetId);
	        
	        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widgetGridViewId);
	        	        
	        appWidgetManager.updateAppWidget(appWidgetIds[i], rv);   
	    }
	            
	    super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		super.onReceive(context, intent);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	    ComponentName thisAppWidget = new ComponentName(context.getPackageName(), Not_Widget.class.getName());
	    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
	        
	    onUpdate(context, appWidgetManager, appWidgetIds);
	   
	}

	@Override  
    public void onDisabled(Context context)  
    {  
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
  
        m.cancel(service);  
    }  
	
	 @Override
    public void onEnabled(Context context)
    {
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 10000);

        Intent alarmIntent = new Intent("AUTO_UPDATE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1800000, pendingIntent);
    }
	
}
