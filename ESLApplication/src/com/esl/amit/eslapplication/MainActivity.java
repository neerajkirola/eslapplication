package com.esl.amit.eslapplication;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;

import com.esl.amit.eslapplication.activity.LoginActivity;
import com.esl.amit.eslapplication.util.Utils;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		setTitleColor();
		
		
		
		
		Message msg = new Message();
		msg.what = getResources().getInteger(R.integer.STOPSPLASH);
		Utils.getRegIdFromGCM(this);
		handler.sendMessageDelayed(msg, getResources().getInteger(R.integer.SPLASHTIME));
	}
	
	private void setTitleColor(){
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.RED); 
	}
	
	
	
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		
			switch (msg.what) {
			case 0:		
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
				break;

			
			}
		}
	};

}
