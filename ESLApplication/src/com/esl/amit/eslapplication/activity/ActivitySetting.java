package com.esl.amit.eslapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.esl.amit.eslapplication.R;

public class ActivitySetting extends Activity implements OnClickListener{
	private Button configure,register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_setting);
		setTitleColor();
		configure=(Button) findViewById(R.id.appc_configure);
		register=(Button) findViewById(R.id.appc_register);
		configure.setOnClickListener(this);
		register.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.appc_configure:
			startActivity(new Intent(this,ActivityConfiguration.class ));
			break;
		case R.id.appc_register:
			startActivity(new Intent(this, ActivityEditRegistrationCode.class));
			break;
		}
		
	}
	
	private void setTitleColor(){
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.GRAY); 
	}
	

}
