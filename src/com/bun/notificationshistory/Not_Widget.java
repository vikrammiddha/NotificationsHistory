package com.bun.notificationshistory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class Not_Widget extends AppWidgetProvider{
	
	@Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
    {
		 // update each of the app widgets with the remote adapter
	    for (int i = 0; i < appWidgetIds.length; ++i) {
	        
	        // Set up the intent that starts the StackViewService, which will
	        // provide the views for this collection.
	        Intent intent = new Intent(context, GridWidgetService.class);
	        // Add the app widget ID to the intent extras.
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
	        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
	        // Instantiate the RemoteViews object for the app widget layout.
	        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	        // Set up the RemoteViews object to use a RemoteViews adapter. 
	        // This adapter connects
	        // to a RemoteViewsService  through the specified intent.
	        // This is how you populate the data.
	        rv.setRemoteAdapter(appWidgetIds[i], R.id.widgetGridViewId, intent);
	        
	        // The empty view is displayed when the collection has no items. 
	        // It should be in the same layout used to instantiate the RemoteViews
	        // object above.
	        rv.setEmptyView(R.id.widgetGridViewId, R.id.enptyWidgetId);

	        //
	        // Do additional processing specific to this app widget...
	        //
	        
	        appWidgetManager.updateAppWidget(appWidgetIds[i], rv);   
	    }
	    super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
	
}
