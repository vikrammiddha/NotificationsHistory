package com.bun.notificationshistory;

import java.util.ArrayList;
import java.util.HashMap;
 
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
    super(applicationcontext, "notificationhistorysqlite.db", null, 4);
    Log.d(LOGCAT,"Created");
    ctx = applicationcontext;
  }
 
  @Override
  public void onCreate(SQLiteDatabase database) {
    String query;
    query = "CREATE TABLE not_hist ( notId INTEGER PRIMARY KEY, notDate Text, notTime Text, message Text, " +
    				"appName String, packageName String, additionalInfo Text)";
    
    String prefTableQuer = "CREATE TABLE preferences (key Text, value Text)";
    database.execSQL(query);
    database.execSQL(prefTableQuer);
    
    populateDefaultPreferences(database);
    
    Log.d(LOGCAT,"not history Created");
  }
  @Override
  public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
    String query;
    query = "DROP TABLE IF EXISTS not_hist";
    String prefQuery = "DROP TABLE IF EXISTS preferences";
    database.execSQL(query);
    database.execSQL(prefQuery);
    onCreate(database);
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
    database.close();
    context.sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
  }
  
  public void insertPreference(HashMap<String, String> queryValues, Context context) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("key", queryValues.get("key"));
    values.put("value", queryValues.get("value"));
    
    database.insert("preferences", null, values);
    database.close();
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
    return database.update("preferences", values, "key" + " = ?", new String[] { k });
  }
 
  /*public void deleteAnimal(String id) {
    Log.d(LOGCAT,"delete");
    SQLiteDatabase database = this.getWritableDatabase();  
    String deleteQuery = "DELETE FROM  animals where animalId='"+ id +"'";
    Log.d("query",deleteQuery);   
    database.execSQL(deleteQuery);
  }*/
 
  public ArrayList<HashMap<String, String>> getAllNotifications() {
    ArrayList<HashMap<String, String>> wordList;
    wordList = new ArrayList<HashMap<String, String>>();
    String selectQuery = "SELECT  * FROM not_hist order by notTime desc";
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
	    return wordList;
	  }
 
  	public void deleteAllNotifications(){
  		
	    SQLiteDatabase database = this.getWritableDatabase();
	    database.delete("not_hist", null, null);
  	}

  	public void populateDefaultPreferences(SQLiteDatabase database){
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("NotificationGroupBy", "GroupByDay");
		
		ContentValues values = new ContentValues();
		for(String key : map.keySet()){
			values.put("key", key);
		    values.put("value", map.get(key));
		}
	    
		
		database.insert("preferences", null, values);
	}
}