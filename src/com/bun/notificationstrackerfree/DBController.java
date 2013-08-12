package com.bun.notificationstrackerfree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
 
import android.util.Log;
 
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DBController  extends SQLiteOpenHelper {
  private static final String LOGCAT = null;
  Context ctx;
 
  public DBController(Context applicationcontext) {
    super(applicationcontext, "notificationhistorysqlite.db", null, 7);
    Log.d(LOGCAT,"Created");
    ctx = applicationcontext;
  }
 
  @Override
  public void onCreate(SQLiteDatabase database) {
    String query;
    query = "CREATE TABLE not_hist ( notId INTEGER PRIMARY KEY, notDate Text, notTime Text, message Text, " +
    				"appName String, packageName String, additionalInfo Text)";
    
    String prefTableQuer = "CREATE TABLE preferences (key Text, value Text)";
    
    String ignoreTableQuery = "CREATE TABLE ignore_list (appName Text)";
    
    database.execSQL(query);
    database.execSQL(prefTableQuer);
    database.execSQL(ignoreTableQuery);
    
    populateDefaultPreferences(database);
    
    Log.d(LOGCAT,"not history Created");
  }
  @Override
  public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
    String query;
    query = "DROP TABLE IF EXISTS not_hist";
    String prefQuery = "DROP TABLE IF EXISTS preferences";
    String ignoreTableQuery = "DROP TABLE IF EXISTS ignore_list";
    
    database.execSQL(query);
    database.execSQL(prefQuery);
    database.execSQL(ignoreTableQuery);
    onCreate(database);
  }
 
  
  public void insertIgnoredApps(HashSet<String> appSet, Context context){
		SQLiteDatabase database = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    for(String app : appSet){
		    values.put("appName", app);	    
		    database.insert("ignore_list", null, values);
		    values.clear();
	    }
	    //database.close();
	    //context.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
  }
  
  public void deleteIgnoredApps(Set<String> appSet){
	  	SQLiteDatabase database = this.getWritableDatabase();	
		
		for(String app : appSet){			
			database.delete("ignore_list",  "appName = ?" , new String[] { app });			
		}
		//database.close();  
  }
  
  public void deleteAppNotifications(String app){
	  SQLiteDatabase database = this.getWritableDatabase();	
	  //database.delete("not_hist",  "packageName = ?" , new String[] { app });	
  }
  
  public HashSet<String> getAllIgnoredApps(){
	  	HashSet<String> ignoredApps = new HashSet<String>();
	  	
	    String selectQuery = "SELECT  * FROM ignore_list ";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	do {
	    		ignoredApps.add(cursor.getString(0));	        
	      } while (cursor.moveToNext());
	    }
	    cursor.close();
	    //database.close();
	    return ignoredApps;
  }
  
  public HashMap<String,String> getAppPackageMap(){
	  
		HashMap<String,String> retMap = new HashMap<String,String>();
	  
		String selectQuery = "SELECT  distinct appName, packageName FROM not_hist ";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	    	do {
	    		retMap.put(cursor.getString(0), cursor.getString(1));	    		
	      } while (cursor.moveToNext());
	    }
	    cursor.close();
	    //database.close();
	    return retMap;
	  
  }
  
  public void insertNotification(HashMap<String, String> queryValues, Context context) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("notDate", queryValues.get("notDate"));
    values.put("notTime", queryValues.get("notTime"));
    values.put("message", queryValues.get("message"));
    values.put("appName", queryValues.get("appName"));
    values.put("packageName", queryValues.get("packageName"));
    values.put("additionalInfo", queryValues.get("additionalInfo"));
    database.insert("not_hist", null, values);
    //database.close();
    context.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
  }
  
  public void insertPreference(HashMap<String, String> queryValues, Context context) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("key", queryValues.get("key"));
    values.put("value", queryValues.get("value"));
    
    database.insert("preferences", null, values);
    //database.close();
    context.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
  }
 
  public int updatePreferences(HashMap<String, String> queryValues) {
    SQLiteDatabase database = this.getWritableDatabase();  
    ContentValues values = new ContentValues();
    String k = "";
    for(String key : queryValues.keySet()){
    	values.put("value", queryValues.get(key));
    	k = key; 
    }
    //database.close();
    return database.update("preferences", values, "key" + " = ?", new String[] { k });
  }
 
  /*public void deleteAnimal(String id) {
    Log.d(LOGCAT,"delete");
    SQLiteDatabase database = this.getWritableDatabase();  
    String deleteQuery = "DELETE FROM  animals where animalId='"+ id +"'";
    Log.d("query",deleteQuery);   
    database.execSQL(deleteQuery);
  }*/
 
  
  public ArrayList<HashMap<String,String>> getLineGraphData(){
	  	ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    String selectQuery = "SELECT notTime,appName,count(*) from not_hist group by notTime, appName order by appName,notTime";
	    SQLiteDatabase database = this.getWritableDatabase();
	    ArrayList<String> days = getLineGraphDays();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    Set<String> ignoredApps = getAllIgnoredApps();
	    if (cursor.moveToFirst()) {
	      do {
	    	
    	  if(!days.contains(cursor.getString(0)))
    		  continue;	
	    	  
	        HashMap<String, String> map = new HashMap<String, String>();
	        if(cursor.getString(1) != null && (cursor.getString(1).toUpperCase().equals("SYSTEM UI"))
								|| cursor.getString(1).toUpperCase().equals("ANDROID SYSTEM"))
			continue;
	        map.put("notTime", cursor.getString(0));
	        if(cursor.getString(1) != null && cursor.getString(1).toUpperCase().equals("GOOGLE SERVICES FRAMEWORK")){
	        	map.put("appName", "Google Talk");
	        }else{
	        	map.put("appName", cursor.getString(1));
	        }
	        if(ignoredApps.contains(map.get("appNAme"))){
	        	continue;
	        }
	        map.put("count", cursor.getString(2));	        
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    cursor.close();
	    //database.close();
	    return wordList;
  }
  
  public HashMap<String,Integer> getPieGraphData(){
	  	
	    String selectQuery = "SELECT appName,count(*) from not_hist where appName not in (Select appName from ignore_list )  group by  appName  ";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    
	    HashMap<String, Integer> map = new HashMap<String, Integer>();
	    if (cursor.moveToFirst()) {
	      do {
	    	  
	    	  if(cursor.getString(0) != null && (cursor.getString(0).toUpperCase().equals("SYSTEM UI"))
									|| cursor.getString(0).toUpperCase().equals("ANDROID SYSTEM"))
			continue;
	        String appName = "";
	        if(cursor.getString(0) != null && cursor.getString(0).toUpperCase().equals("GOOGLE SERVICES FRAMEWORK")){
	        	appName = "Google Talk";
	        	
	        }else{
	        	appName = cursor.getString(0);
	        	
	        }
	        
	        map.put(appName, Integer.valueOf(cursor.getString(1)));        
	        
	      } while (cursor.moveToNext());
	    }
	    cursor.close();
	    //database.close();
	    return map;
}
  
  public ArrayList<String> getLineGraphDays(){
	  ArrayList<String> wordList;
	    wordList = new ArrayList<String>();
	    String selectQuery = "SELECT distinct notTime from not_hist order by notId";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    
	    Integer count = cursor.getCount() - 7;
	    
	    if (cursor.moveToFirst()) {
	      do {
	    	  
	    	  if(cursor.getPosition() < count){
	    		  continue;
	    	  }
	    	 
	    	  wordList.add(cursor.getString(0));
	        
	      } while (cursor.moveToNext());
	    }
	    cursor.close();
	    //database.close();
	    return wordList;		
  }
  
  public ArrayList<String> getTopTenApps(){
	  ArrayList<String> wordList;
	    wordList = new ArrayList<String>();
	    String selectQuery = "SELECT appName,count(*) from not_hist  where appName not in (Select appName from ignore_list ) group by appName order by count(*) desc limit 10 ";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    
	    if (cursor.moveToFirst()) {
	      do {
	    	  if(cursor.getString(0) != null && cursor.getString(0).toUpperCase().equals("GOOGLE SERVICES FRAMEWORK")){
	    		  wordList.add("Google Talk");		        
		        }else{
		        	wordList.add(cursor.getString(0));
		        }
	    	  //wordList.add(cursor.getString(0));
	        
	      } while (cursor.moveToNext());
	    }
	    cursor.close();
	    //database.close();
	    return wordList;		
  }
  
  
  public ArrayList<HashMap<String, String>> getAllNotifications() {
    ArrayList<HashMap<String, String>> wordList;
    wordList = new ArrayList<HashMap<String, String>>();
    String selectQuery = "SELECT  * FROM not_hist order by notId desc";
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
    	if(cursor.getString(4) != null && (cursor.getString(4).toUpperCase().equals("SYSTEM UI"))
    										|| cursor.getString(4).toUpperCase().equals("ANDROID SYSTEM"))
    		continue;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("notId", cursor.getString(0));
        map.put("notDate", cursor.getString(1));
        map.put("notTime", cursor.getString(2));
        map.put("message", cursor.getString(3));
        if(cursor.getString(4) != null && cursor.getString(4).toUpperCase().equals("GOOGLE SERVICES FRAMEWORK")){
        	map.put("appName", "Google Talk");
        }else{
        	map.put("appName", cursor.getString(4));
        }
        
        map.put("packageName", cursor.getString(5));
        wordList.add(map);
      } while (cursor.moveToNext());
    }
    cursor.close();
    //database.close();
    return wordList;
  }
  
  public HashMap<String, String> getAllPreferences() {
    HashMap<String, String> prefMap;
    prefMap = new HashMap<String, String>();
    String selectQuery = "SELECT  * FROM preferences ";
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
    	        
    	prefMap.put(cursor.getString(0), cursor.getString(1));
    	        
      } while (cursor.moveToNext());
    }
    cursor.close();
    //database.close();
    return prefMap;
  }
  
  
  public ArrayList<HashMap<String, String>> getNotificationDetails(String date, String app) {
	  	if("Google Talk".toUpperCase().equals(app.toUpperCase())){
	  		app = "Google Services Framework";
	  	}
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    String selectQuery = "";
	    if(date != null){
	    	selectQuery = "SELECT  * FROM not_hist where notTime = '" + date + "' and appName = '" + app + "' order by notId desc";
	    }else{
	    	selectQuery = "SELECT  * FROM not_hist where appName = '" + app + "' order by notId desc";
	    }
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	      do {
	    	if(cursor.getString(4) != null && cursor.getString(4).toUpperCase().equals("SYSTEM UI"))
	    		continue;
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("notId", cursor.getString(0));
	        map.put("notDate", cursor.getString(1));
	        map.put("notTime", cursor.getString(2));
	        map.put("message", cursor.getString(3));
	        if(cursor.getString(4) != null && cursor.getString(4).toUpperCase().equals("GOOGLE SERVICES FRAMEWORK")){
	        	map.put("appName", "Google Talk");
	        }else{
	        	map.put("appName", cursor.getString(4));
	        }
	        
	        map.put("packageName", cursor.getString(5));
	        map.put("additionalInfo", cursor.getString(6));
	        wordList.add(map);
	      } while (cursor.moveToNext());
	    }
	    cursor.close();
	    //database.close();
	    return wordList;
	  }
 
  	public void deleteAllNotifications(){
  		
	    SQLiteDatabase database = this.getWritableDatabase();
	    database.delete("not_hist", null, null);
	    //database.close();
  	}

  	public void populateDefaultPreferences(SQLiteDatabase database){
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("NotificationGroupBy", "GroupByDay");
		map.put("FirstTimeTTSWarning", "No");
		map.put("Passcode", "0000");
		
		ContentValues values = new ContentValues();
		for(String key : map.keySet()){
			values.put("key", key);
		    values.put("value", map.get(key));
		    database.insert("preferences", null, values);
		    values.clear();
		}
	    
		//database.close();
		//database.insert("preferences", null, values);
	}
  	
  	
}