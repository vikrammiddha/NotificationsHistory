package com.bun.notificationshistory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DatabaseChangedReceiver extends BroadcastReceiver{
	
	public static String ACTION_DATABASE_CHANGED = "com.bun.notificationshistory.DATABASE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.v("Broadcast recevier", "entered into receiver");
    }
}
