package com.esl.amit.eslapplication.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esl.amit.eslapplication.R;
import com.esl.amit.eslapplication.bo.UserBo;
import com.esl.amit.eslapplication.util.Utils;
import com.web.JsonParser;

public class MessageDetailActivity extends Activity {
	private TextView senderTxt, dateTimeTxt, msgDescTxt, msgHeaderTxt;
	Button delete_btn;
	
	JSONObject obj;
	Handler handler;
	JSONStringer TestApp;
	UserBo ubo;
	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail_screen);
		setTitleColor();
		Bundle bundle = getIntent().getExtras();
		
		senderTxt = (TextView) findViewById(R.id.sender_txt);
		dateTimeTxt = (TextView) findViewById(R.id.date_time_txt);
		msgHeaderTxt = (TextView) findViewById(R.id.msg_header_txt);
		msgDescTxt = (TextView) findViewById(R.id.msg_desc_txt);
		
		delete_btn=(Button) findViewById(R.id.delete_btn);
		prefs=this.getSharedPreferences("userdata", Context.MODE_PRIVATE);
		if(bundle != null){
			 ubo = (UserBo) bundle.get("constant");
			if(ubo != null){
				senderTxt.setText(ubo.getFullName());
				dateTimeTxt.setText(/*ubo.getDateStamp()+" - "+*/ ubo.getDateTime());
				msgHeaderTxt.setText(ubo.getMsgHeading());
				msgDescTxt.setText(ubo.getMsgDesc());
			}
		}
		
		delete_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					 TestApp = new JSONStringer().object().key("UserID").value(prefs.getString("userid", "noname"))
					        .key("MessageID").value(ubo.getId())
					        .endObject();
					 
					 deleteMessage(TestApp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
	}
	
	
	private void setTitleColor(){
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.GRAY); 
	}
	
	
	
	private void deleteMessage(final JSONStringer data){
		
		 
		 
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_delete_message,data);
					
					handler.sendEmptyMessage(0);
				}
			}.start();
			
			handler=new Handler(){
				
				public void handleMessage(android.os.Message msg) {
					/*if(pd!=null & pd.isShowing()){
						pd.dismiss();
					}*/
					try {
						boolean status=obj.getBoolean("Status");
						if(status){
							Toast.makeText(MessageDetailActivity.this, obj.getString("Message"),2000).show();
							finish();
						}else{
							Toast.makeText(MessageDetailActivity.this, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}

}
