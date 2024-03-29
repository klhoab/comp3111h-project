package com.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;


public class Alarms {
	
	final static String TITLE = "title";
	final static String ISEVENT = "isEvent";
	static private ArrayList<AlarmBean> alarmInfo = new ArrayList<AlarmBean>();
	static private ArrayList<String> alarmID =  new ArrayList<String>();
	static private int assignId = 0;
	static private int eventCount = 0;
	final static int MAXINT = 2147483646;
    static private void setAlarm(Context context, AlarmBean alarmAdd){
    		
    		Log.i("Alarm", alarmAdd.getTitle()+"  "+ alarmAdd.getID() 
    				+ "  " + alarmAdd.getMillisecond() + "  " + System.currentTimeMillis());
			Intent intent = new Intent(context, AlamrReceiver.class);
			/*transfer Info*/
			Bundle bundle = new Bundle();
			bundle.putString(TITLE, alarmAdd.getTitle());
			bundle.putBoolean(ISEVENT, alarmAdd.isEvent());
			intent.putExtras(bundle);
			
			PendingIntent pi = PendingIntent.getBroadcast(context, 
					alarmAdd.getID(), intent, 0);
			AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
			//set Alarm
			am.set(AlarmManager.RTC_WAKEUP, alarmAdd.getMillisecond(), pi);
    }
    
    static public void cancelAlarm(Context context, String eventID){
		// TODO Auto-generated method stub
    	
    	int requestCode = alarmID.indexOf(eventID);
		Intent intent = new Intent(context,AlamrReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, alarmInfo.get(requestCode).getID(), intent, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pi);
		
		alarmInfo.remove(requestCode);
		alarmID.remove(requestCode);
		
		Log.i("Alarm cancel", eventID);
    }
    
    static public void updateAlarm(Context context, 
    		String eventID, String title, long milliS, boolean isEvent){
    	
    	int requestCode = alarmID.indexOf(eventID);
    	int oldID = alarmInfo.get(requestCode).getID();
    	int newID = assignId;
    	assignId++;
    	assignId %= MAXINT;
    	
    	
		Intent intent = new Intent(context,AlamrReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, oldID, intent, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pi);
    	
		//Intent intent = new Intent(context, AlamrReceiver.class);
		/*transfer Info*/
		Bundle bundle = new Bundle();
		bundle.putString(TITLE, title);
		bundle.putBoolean(ISEVENT, isEvent);
		intent.putExtras(bundle);
		
		pi = PendingIntent.getBroadcast(context, 
				newID, intent, 0);
		am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		//set Alarm
		am.set(AlarmManager.RTC_WAKEUP, milliS, pi);
    	
		Log.i("Alarm", title +"  "+ alarmInfo.get(requestCode).getID()
				+ "  " + milliS + "  " + System.currentTimeMillis());
		
		alarmInfo.get(requestCode).setID(newID);
		alarmInfo.get(requestCode).setMillisecond(milliS);
		alarmInfo.get(requestCode).setTitle(title);
    	Log.i("Alarm update", eventID);
    }
    
    static public void addAlarm(Context context, 
    		String eventID, String title, long milliS, boolean event){
    	
    	if(!alarmID.contains(eventID) && (milliS - System.currentTimeMillis())>0){
    		
    		AlarmBean alarmAdd = new AlarmBean(assignId++, title, milliS, event);
    		assignId %= MAXINT;
    		
    		alarmInfo.add(alarmAdd);
    		alarmID.add(eventID);
    		setAlarm(context, alarmAdd);
    	}
    	else {
    		Log.i("alarm", "add fail");
    	}
    }
    
    static public int getCount(){
    	int get = eventCount;
    	eventCount++;
    	eventCount %= MAXINT;
    	return get;
    }
    
    static public String getTitle(int position){
    	return alarmInfo.get(position).getTitle();
    }
    
    static public int getID (int position){
    	return alarmInfo.get(position).getID();
    }
    
    static public boolean isEvent(int position){
    	return alarmInfo.get(position).isEvent();
    }
    
    static public boolean contains(String eventID){
    	return alarmID.contains(eventID);
    }
    static public int index(String eventID){
    	return alarmID.indexOf(eventID);
    }
}
