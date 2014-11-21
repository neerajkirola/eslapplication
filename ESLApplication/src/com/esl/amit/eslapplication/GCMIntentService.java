package com.esl.amit.eslapplication;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import com.esl.amit.eslapplication.activity.MessageActivity;
import com.esl.amit.eslapplication.util.Utils;
import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "gcm";
	Handler handler;
	JSONStringer data=null;
	public static String KEY_REGISTRATIONID="id";
	
	JSONObject obj;
	// Use your PROJECT ID from Google API into SENDER_ID
	public static final String SENDER_ID = "997246685025";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {

		Log.i(TAG, "onRegistered: registrationId=" + registrationId);
		Utils.REGISTRATION_ID=registrationId;
		/*try {
			data = new JSONStringer().object().key(KEY_REGISTRATIONID)
			        .value(registrationId ).endObject();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		registerForGCM( context,data);*/
		
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {

		Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
	}
	
	

	@Override
	protected void onMessage(Context context, Intent data) {
		String message;
		Notification notification;
		// Message from PHP server
		message = data.getStringExtra("message");
		// Open a new activity called GCMMessageView
		Intent intent = new Intent(this, MessageActivity.class);
		// Pass data to the new activity
		intent.putExtra("message", message);
		// Starts the activity on notification click
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		// Create the notification with a notification builder
		
		Notification.Builder notif=new Notification.Builder(this);
		long[] pattern = {500,500,500,500,500,500,500,500,500};
		
		notif.setSmallIcon(R.drawable.icon);
				notif.setWhen(System.currentTimeMillis());
				notif.setContentTitle("ESL Messages");
				notif.setContentText(message);
				notif.setContentIntent(pIntent);
				
				if(Utils.isSound(context))
					notif.setSound(soundUri);
				if(Utils.isVibrate(context))
					notif.setVibrate(pattern);
				notification=notif.getNotification();
		
		
		// Remove the notification on click
		notification.flags |= Notification.FLAG_AUTO_CANCEL|Notification.DEFAULT_LIGHTS;
	

		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.notify(R.string.app_name, notification);

		{
			// Wake Android Device when notification received
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			final PowerManager.WakeLock mWakelock = pm.newWakeLock(
					PowerManager.FULL_WAKE_LOCK
							| PowerManager.ACQUIRE_CAUSES_WAKEUP, "GCM_PUSH");
			mWakelock.acquire();

			// Timer before putting Android Device to sleep mode.
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					mWakelock.release();
				}
			};
			timer.schedule(task, 5000);
		}

	}

	@Override
	protected void onError(Context arg0, String errorId) {

		Log.e(TAG, "onError: errorId=" + errorId);
	}
	
	
	
	/*private void registerForGCM(final Context c,final JSONStringer data){
		
		 
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_registration_gcm,data);
					
					handler.sendEmptyMessage(0);
				}
			}.start();
			
			handler=new Handler(){
				
				
				public void handleMessage(android.os.Message msg) {
					
					try {
						Toast.makeText(c, "register finish",2000).show();
						boolean status=obj.getBoolean("Status");
						if(status){
							
						}else{
							Toast.makeText(c, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	*/
	

}