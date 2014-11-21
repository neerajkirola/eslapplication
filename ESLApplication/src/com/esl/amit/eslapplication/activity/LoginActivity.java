package com.esl.amit.eslapplication.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esl.amit.eslapplication.GCMIntentService;
import com.esl.amit.eslapplication.MainActivity;
import com.esl.amit.eslapplication.R;
import com.esl.amit.eslapplication.util.Utils;
import com.esl.amit.eslapplication.util.Validator;
import com.google.android.gcm.GCMRegistrar;
import com.web.JsonParser;

public class LoginActivity extends Activity implements OnClickListener{
	
	private Button okBtn, cancelBtn,signUpBtn;
	private EditText codeTxt, pwdTxt;
	RelativeLayout layMain;
	
	Handler handler;
	JSONObject obj;
	
	SharedPreferences prefs;
	String name,pwd,userid;
	JSONStringer TestApp=null;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		setTitleColor();
		pd=new ProgressDialog(this);
		codeTxt = (EditText) findViewById(R.id.codeTxt);
		pwdTxt = (EditText) findViewById(R.id.pwdTxt);
		layMain=(RelativeLayout) findViewById(R.id.layMain);
		okBtn = (Button) findViewById(R.id.okBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		signUpBtn = (Button) findViewById(R.id.btnSignUp);
		
		prefs=this.getSharedPreferences("userdata", Context.MODE_PRIVATE);
		
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		signUpBtn.setOnClickListener(this);
		layMain.setOnClickListener(this);
	}

	
	private void setTitleColor(){
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.BLUE); 
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == okBtn){
			 name=codeTxt.getText().toString().trim();
			 pwd=pwdTxt.getText().toString().trim();
			
			if(validateField()){
				try {
					  TestApp = new JSONStringer().object().key("UserName")
					        .value(name ).key("Password").value(pwd).endObject();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				login(TestApp);
			}else{
				
			}
			
			
		}else if(v == cancelBtn){
			LoginActivity.this.finish();
		}else if(v == signUpBtn){
			startActivity(new Intent(LoginActivity.this, SignUp.class));
		}else{
			hideSoftKeyboard();
		}
	}
	
	private void next(){
		
		Intent intent =  new Intent(LoginActivity.this, MessageActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
	}
	
	private boolean validateField(){
		Validator validator = new Validator();
		if(validator.validateFields(codeTxt.getText().toString())){
			if(validator.validateFields(pwdTxt.getText().toString())){
				return true;
			}else{
				Toast.makeText(LoginActivity.this, "Enter password !", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(LoginActivity.this, "Enter code !", Toast.LENGTH_LONG).show();
		}
		return false;
	}
	
	
	private void login(final JSONStringer data){
		
		if(!Utils.isConnectingToInternet(this)){
			Toast.makeText(this, "Please check your internet connection", 2000).show();
			return;
		}
		
		
		pd.setTitle("Please wait");
		pd.show();
		 
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_login,data);
					
					handler.sendEmptyMessage(0);
				}
			}.start();
			
			handler=new Handler(){
				
				
				public void handleMessage(android.os.Message msg) {
					if(pd!=null & pd.isShowing()){
						pd.dismiss();
					}
					try {
						boolean status=obj.getBoolean("Status");
						if(status){
							userid=obj.getString("UserID");
							storeLoginData();
							next();
							
							/*try {
								  TestApp = new JSONStringer().object().key("id")
								        .value(Utils.getRegIdFromGCM(LoginActivity.this)).endObject();
								  registerGCMToServer(TestApp);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
							
							
						}else{
							Toast.makeText(LoginActivity.this, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	

	
	
private void registerGCMToServer(final JSONStringer data){
		
	
		 
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
					Toast.makeText(LoginActivity.this, "registered gcm",2000).show();
					try {
						boolean status=obj.getBoolean("Status");
						if(status){
							
						}else{
							Toast.makeText(LoginActivity.this, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	private void storeLoginData(){
		
		Editor editor=prefs.edit();
		editor.putString("username", name);
		editor.putString("pass", pwd);
		editor.putString("userid", userid);
		editor.commit();
		
	}
	
	private void hideSoftKeyboard(){
	    //if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
	        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(codeTxt.getWindowToken(), 0);
	  //  }
	}
	

}
