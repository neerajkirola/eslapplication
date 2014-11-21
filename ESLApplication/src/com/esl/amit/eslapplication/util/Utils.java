package com.esl.amit.eslapplication.util;

import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.esl.amit.eslapplication.GCMIntentService;
import com.google.android.gcm.GCMRegistrar;

public class Utils {
	
	public static final String url_login="http://dotsoftech.com/ECL/ECLService.svc/UserLogin";
	public static final String url_signup="http://dotsoftech.com/ECL/ECLService.svc/UserSignUp";
	public static final String url_message_list="http://dotsoftech.com/ECL/ECLService.svc/GetUserMessageList";
	public static final String url_delete_message="http://dotsoftech.com/ECL/ECLService.svc/DeleteMessage";
	public static final String url_registration_code="http://dotsoftech.com/ECL/ECLService.svc/GetUserRegistrationCodes";
	public static final String url_delete_registration_code="http://dotsoftech.com/ECL/ECLService.svc/DeleteUserRegistrationCodes";
	public static final String url_add_registration_code="http://dotsoftech.com/ECL/ECLService.svc/AddUserRegistrationCodes";
	public static final String url_registration_gcm="";
	public static String timezone="";
	
	static SharedPreferences prefs;
	public static String REGISTRATION_ID;
	
	public static String getTimeZone(){
		String s=TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).replace("GMT", "");
		Log.d("time", s);
		return s;
	}
	
	public static void setGCMId(Context c,String id){
		prefs=c.getSharedPreferences("GCMDATA",Context.MODE_PRIVATE);
		prefs.edit().putString("gcmid", id).commit();
		
	}
	
	
public static String getRegIdFromGCM(Context c){
		
		
		GCMRegistrar.checkDevice(c);
		GCMRegistrar.checkManifest(c);

		// Register Device Button
		Utils.REGISTRATION_ID=GCMRegistrar.getRegistrationId(c);
		//Toast.makeText(SignUp.this,"registrationId= "+registrationId, 5000).show();

		Log.i("gcm", "Registering device");
		// Retrive the sender ID from GCMIntentService.java
		// Sender ID will be registered into GCMRegistrar
		if (Utils.REGISTRATION_ID.equals("")) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(c,
					GCMIntentService.SENDER_ID);
			
		}
		Log.i("gcm", "registrationId "+Utils.REGISTRATION_ID);
		return Utils.REGISTRATION_ID;
		
	}
	
	public static void setNotificationSound(Context c,boolean b) {
		
		prefs=c.getSharedPreferences("notif",Context.MODE_PRIVATE);
		prefs.edit().putBoolean("issound", b).commit();
	
	}
	
	public static boolean isSound(Context c){
		prefs=c.getSharedPreferences("notif",Context.MODE_PRIVATE);
		return prefs.getBoolean("issound",false);
	}
	
public static void setNotificationVibrate(Context c,boolean b) {
		
		prefs=c.getSharedPreferences("notif",Context.MODE_PRIVATE);
		prefs.edit().putBoolean("isvibrate", b).commit();
	
	}
	
	public static boolean isVibrate(Context c){
		prefs=c.getSharedPreferences("notif",Context.MODE_PRIVATE);
		return prefs.getBoolean("isvibrate",false);
	}
	
	
	public static boolean isConnectingToInternet(Context _context){
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivity != null) 
		  {
			  NetworkInfo[] info = connectivity.getAllNetworkInfo();
			  if (info != null) 
				  for (int i = 0; i < info.length; i++) 
					  if (info[i].getState() == NetworkInfo.State.CONNECTED)
					  {
						  return true;
					  }

		  }
		  return false;
	}
	
	

}
