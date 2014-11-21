package com.esl.amit.eslapplication;



import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.esl.amit.eslapplication.util.Utils;
import com.google.android.gcm.GCMRegistrar;
import com.web.JsonParser;

public class GCMMainActivity extends Activity {

	String TAG = "gcm";
	String registrationId;
	JSONObject obj;
	Handler handler;
	JSONStringer TestApp=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		// Register Device Button
		registrationId=GCMRegistrar.getRegistrationId(this);
		Toast.makeText(GCMMainActivity.this,"registrationId= "+registrationId, 5000).show();

		Log.i(TAG, "Registering device");
		// Retrive the sender ID from GCMIntentService.java
		// Sender ID will be registered into GCMRegistrar
		if (registrationId.equals("")) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(GCMMainActivity.this,
					GCMIntentService.SENDER_ID);
			return;
		}
		
		
		try {
			  TestApp = new JSONStringer().object().key(GCMIntentService.KEY_REGISTRATIONID)
			        .value(registrationId ).endObject();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registerForGCM(TestApp);
	}
	
	
	
	
private void registerForGCM(final JSONStringer data){
		
	 
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
						boolean status=obj.getBoolean("Status");
						if(status){
							
						}else{
							Toast.makeText(GCMMainActivity.this, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	
	
}