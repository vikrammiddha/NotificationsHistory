package com.bun.notificationshistory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AppListnerService extends Service{
	
	private Context ctx;
	
	private Handler mPeriodicEventHandler;
	
	private final int PERIODIC_EVENT_TIMEOUT = 1000;
	
	Boolean isServiceRunning = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		 
		return null;
	}
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mPeriodicEventHandler = new Handler();
	      mPeriodicEventHandler.postDelayed(doPeriodicTask, PERIODIC_EVENT_TIMEOUT);

	}
	
	private Runnable doPeriodicTask = new Runnable()
    {
        public void run() 
        {
        	//Toast.makeText(ctx, 
  				//  "Thread =====" + Utils.notMap.keySet().size(), 
  		          // Toast.LENGTH_SHORT).show();
        	
        	ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        	// The first in the list of RunningTasks is always the foreground task.
        	RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
        	
        	String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
        	
        	if(foregroundTaskPackageName.equals("com.google.android.talk")){
        		foregroundTaskPackageName = "com.google.android.gsf";
        	}
        	
        	Utils.notMap.remove(foregroundTaskPackageName);
        	
        	Log.d("package name =============", foregroundTaskPackageName);
        	/*Toast.makeText(ctx, 
    				  "Thread =====" + Utils.notMap.keySet().size() + "--" +  foregroundTaskPackageName, 
    		           Toast.LENGTH_SHORT).show();*/
        	
            //your action here
        	if(Utils.notMap.keySet().size() != 0 && isServiceRunning == true)
        		mPeriodicEventHandler.postDelayed(doPeriodicTask, PERIODIC_EVENT_TIMEOUT);
        	else
        		stopSelf();
            
           
        }
    };

    


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
		
	}



	@Override
    public void onStart(Intent intent, int startId) {
		ctx = this;
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        
       
        
    }
    @Override
    public void onDestroy() {
    	isServiceRunning = false;
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
