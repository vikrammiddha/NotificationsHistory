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
 
  public DBController(Context applicationcontext) {
    super(applicationcontext, "notificationhistorysqlite.db", null, 2);
    Log.d(LOGCAT,"Created");
  }
 
  @Override
  public void onCreate(SQLiteDatabase database) {
    String query;
    query = "CREATE TABLE not_hist ( notId INTEGER PRIMARY KEY, notDate Text, notTime Text, message Text, " +
    				"appName String, packageName String, additionalInfo Text)";
    database.execSQL(query);
    Log.d(LOGCAT,"not history Created");
  }
  @Override
  public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
    String query;
    query = "DROP TABLE IF EXISTS not_hist";
    database.execSQL(query);
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
 
 /* public int updateAnimal(HashMap<String, String> queryValues) {
    SQLiteDatabase database = this.getWritableDatabase();  
    ContentValues values = new ContentValues();
    values.put("animalName", queryValues.get("animalName"));
    return database.update("animals", values, "animalId" + " = ?", new String[] { queryValues.get("animalId") });
  }*/
 
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
  
  
  public ArrayList<HashMap<String, String>> getNotificationDetails(String date, String app) {
	  	if("Google Talk".toUpperCase().equals(app.toUpperCase())){
	  		app = "Google Services Framework";
	  	}
	    ArrayList<HashMap<String, String>> wordList;
	    wordList = new ArrayList<HashMap<String, String>>();
	    String selectQuery = "SELECT  * FROM not_hist where notTime = '" + date + "' and appName = '" + app + "' order by notTime desc";
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
 /* public HashMap<String, String> getNotInfo(String id) {
    HashMap<String, String> wordList = new HashMap<String, String>();
    SQLiteDatabase database = this.getReadableDatabase();
    String selectQuery = "SELECT * FROM animals where animalId='"+id+"'";
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        wordList.put("animalName", cursor.getString(1));
      } while (cursor.moveToNext());
    }           
    return wordList;
  }*/ 
}