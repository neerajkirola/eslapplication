package com.esl.amit.eslapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;

import com.esl.amit.eslapplication.R;
import com.esl.amit.eslapplication.util.Utils;

public class ActivityConfiguration extends Activity implements OnClickListener{
	private CheckedTextView popupCheck,soundCheck;
	private boolean isPopupChecked,isSoundChecked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_config);
		setTitleColor();
		popupCheck=(CheckedTextView) findViewById(R.id.popup_notification);
		soundCheck=(CheckedTextView) findViewById(R.id.sound_notification);
		popupCheck.setOnClickListener(this);
		soundCheck.setOnClickListener(this);
		
		checkSound();
		
		checkVibrate();
		
	}
	private void setTitleColor(){
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.GRAY); 
	}
	
	private void checkSound(){
		if(Utils.isSound(this))
			soundCheck.setChecked(true);
		else
			soundCheck.setChecked(false);
		
	}
	
	
	private void checkVibrate(){
		if(Utils.isVibrate(this))
			popupCheck.setChecked(true);
		else
			popupCheck.setChecked(false);
	}
	
	
	private void setVibrate(){
		if(Utils.isVibrate(this)){
			Utils.setNotificationVibrate(this, false);
			popupCheck.setChecked(false);
		}else{
			Utils.setNotificationVibrate(this, true);
			popupCheck.setChecked(true);
		}
	}
	
	
	
	private void setSound(){
		if(Utils.isSound(this)){
		Utils.setNotificationSound(this,false);
			soundCheck.setChecked(false);
		}
		else{
			Utils.setNotificationSound(this,true);
			soundCheck.setChecked(true);
		}
			
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_notification:
			setVibrate();
		break;
		case R.id.sound_notification:
			setSound();
		break;
		}
		
	}

}
