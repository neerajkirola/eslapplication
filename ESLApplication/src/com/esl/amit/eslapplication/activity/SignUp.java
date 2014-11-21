package com.esl.amit.eslapplication.activity;

import java.lang.ref.SoftReference;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.esl.amit.eslapplication.GCMIntentService;
import com.esl.amit.eslapplication.R;

import com.esl.amit.eslapplication.util.Utils;
import com.esl.amit.eslapplication.util.Validator;
import com.google.android.gcm.GCMRegistrar;
import com.web.JsonParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SignUp extends Activity implements OnClickListener{
	
	String TAG = "gcm";
	EditText txtUsername,txtPass,txtRePass,txtEmail,txtPhone,txtUdid;
	JSONObject obj;
	Handler handler;
	JSONStringer TestApp;
	Button btnSignUP;
	ProgressDialog pd;
	LinearLayout layMain;
	String registrationId="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.signup);
		setTitleColor();
		txtUsername=(EditText) findViewById(R.id.txtUserName);
		txtPass=(EditText)findViewById(R.id.txtPassword);
		txtEmail=(EditText)findViewById(R.id.txtEmail);
		txtPhone=(EditText)findViewById(R.id.txtPhone);
		txtUdid=(EditText)findViewById(R.id.txtUdid);
		txtRePass=(EditText)findViewById(R.id.txtRePassword);
		
		btnSignUP=(Button) findViewById(R.id.btnDone);
		Utils.getRegIdFromGCM(this);
		
		layMain=(LinearLayout) findViewById(R.id.layMain);
		pd=new ProgressDialog(this);
		btnSignUP.setOnClickListener(this);
		
		layMain.setOnClickListener(this);
		
		
		
	}
	
	
	private void setTitleColor(){
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.GRAY); 
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnSignUP){
			collectData();
		}else{
			hideSoftKeyboard();
		}
		
	}
	
	private void hideSoftKeyboard(){
	    //if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
	        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(txtUsername.getWindowToken(), 0);
	  //  }
	}
	
	private String getDeviceId(){
		String id=Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
		
		Log.d("tag",id );
		return id;
	}
	public void collectData(){
		
		if(!Utils.isConnectingToInternet(this)){
			Toast.makeText(this, "Please check your internet connection", 2000).show();
			return;
		}
		
		String name=txtUsername.getText().toString();
		String pass=txtPass.getText().toString();
		String email=txtEmail.getText().toString();
		String phone=txtPhone.getText().toString();
		String uid=txtUdid.getText().toString();
		
		if (!validateField() ) {
			
			 
			 return;
			
		}
		if(Utils.getRegIdFromGCM(this).equalsIgnoreCase("")){
			return;
		}
		
		try {
			 TestApp = new JSONStringer().object().key("UserName").value(name )
			        .key("Password").value(pass)
			        .key("Email").value(email)
			        .key("Phone").value(phone)
			        .key("UDID").value(Utils.getRegIdFromGCM(this)).endObject();
			 
			 signUp(TestApp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean validateField(){
		Validator validator = new Validator();
		if(validator.validateFields(txtUsername.getText().toString())){
			if(validator.validateFields(txtPass.getText().toString())){
				if(validator.validateFields(txtEmail.getText().toString())){
					if(validator.validateFields(txtPhone.getText().toString())){
						//if(validator.validateFields(txtUdid.getText().toString())){
							if(txtPass.getText().toString().equals(txtRePass.getText().toString())){
								return true;
							}else{
								Toast.makeText(SignUp.this, "Password Not Matched", Toast.LENGTH_LONG).show();
							}
							
							
						/*}else{
							Toast.makeText(SignUp.this, "Enter UDID !", Toast.LENGTH_LONG).show();
						}*/
					}else{
						Toast.makeText(SignUp.this, "Enter Phone Number !", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(SignUp.this, "Enter Email !", Toast.LENGTH_LONG).show();
				}
				
			}else{
				Toast.makeText(SignUp.this, "Enter Password !", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(SignUp.this, "Enter Username !", Toast.LENGTH_LONG).show();
		}
		return false;
	}
	
	private void signUp(final JSONStringer data){
		
	 pd.setTitle("Please wait");
		pd.show();
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_signup,data);
					
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
							Toast.makeText(SignUp.this, obj.getString("Message"),2000).show();
							finish();
						}else{
							Toast.makeText(SignUp.this, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	
	
	

}
