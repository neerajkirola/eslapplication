package com.esl.amit.eslapplication.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esl.amit.eslapplication.R;
import com.esl.amit.eslapplication.adapter.MessageListAdapter;
import com.esl.amit.eslapplication.bo.UserBo;
import com.esl.amit.eslapplication.util.Utils;
import com.web.JsonParser;

public class MessageActivity extends Activity implements OnClickListener{
	private ListView msgList;
	private Button settingBtn, selectAllBtn, deleteBtn;
	private TextView msgTxt;

	private ArrayList<UserBo> listUserBo;
	private MessageListAdapter adapter ;
	
	JSONObject obj;
	Handler handler;
	JSONStringer TestApp=null;
	SharedPreferences prefs;
	int deleteCount;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_screen);
		pd=new ProgressDialog(this);
		prefs=this.getSharedPreferences("userdata", Context.MODE_PRIVATE);
		msgList = (ListView) findViewById(R.id.msg_list);
		settingBtn = (Button) findViewById(R.id.setting_btn);
		selectAllBtn = (Button) findViewById(R.id.select_all_btn);
		deleteBtn = (Button) findViewById(R.id.delete_btn);

		msgTxt = (TextView) findViewById(R.id.no_msg_txt);
		
		
		
		
		selectAllBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
		

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		createParameter();
	}
	
	private void createParameter(){
		try {
			  TestApp = new JSONStringer().object().key("UserID")
			        .value(prefs.getString("userid", "noname") )
			        .key("TimeZone").value("+5.30").endObject();
			  
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getMessageFromServer(TestApp);
		
	}

	private void setData() {
		
		
		/*for (int i = 0; i < listUserBo.size(); i++) {
			UserBo ubo = new UserBo();
			ubo.setFullName("User " + i);
			ubo.setMsgHeading("Lorem Ipsum is simply dummy text of the printing and typesetting Heading ...."
					+ i);
			ubo.setMsgDesc("Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC."
					+ " test test test " + i);			
			ubo.setReadStatus(false);			
			//Date d3 = new Date(System.currentTimeMillis());
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			Calendar cal = Calendar.getInstance();			
			ubo.setDateTime(df.format(cal.getTime()));
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd-MM-yyyy").format(date);
			//System.out.println(" time is ---->"+ ubo.getDateTime());
			ubo.setStatus(false);
			ubo.setId(i+1+"");
			ubo.setDateStamp(modifiedDate);
			
			listUserBo.add(ubo);	
		}
		if(listUserBo != null || listUserBo.size()>=0){
			msgTxt.setVisibility(View.GONE);
		}*/
		if(listUserBo==null){
			selectAllBtn.setEnabled(false);
			deleteBtn.setEnabled(false);
			return;
		}else if(listUserBo.size()==0){
			return;
		}
		adapter = new MessageListAdapter(MessageActivity.this, R.layout.message_screen, listUserBo);
		msgList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == selectAllBtn){
			
			String str = selectAllBtn.getText().toString();
			if(str.equalsIgnoreCase("Select All")){
				if(listUserBo.size()<=0)return;
				selectAllBtn.setText("Deselect All");
				
				for (UserBo ubo : listUserBo) {
					ubo.setStatus(true);
				}	
			}else if(str.equalsIgnoreCase("Deselect All")){
				selectAllBtn.setText("Select All");
				for (UserBo ubo : listUserBo) {
					ubo.setStatus(false);
				}	
			}
					
			adapter.notifyDataSetChanged();
			
		}else if(v == deleteBtn){
			if(listUserBo.size()<=0)return;
			boolean deleteStatus = false;
			ArrayList<String> messageIds=adapter.getSelectedData();
			for (int i = 0; i < messageIds.size(); i++) {
				deleteCount++;
			
			try {
				 TestApp = new JSONStringer().object().key("UserID").value(prefs.getString("userid", "noname"))
				        .key("MessageID").value(messageIds.get(i))
				        .endObject();
				 
				 deleteMessage(TestApp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			/*UserBo[] uboArray = listUserBo.toArray(new UserBo[listUserBo.size()]);
			for (UserBo ubo : uboArray) {
				if(ubo.isStatus()){
					listUserBo.remove(ubo);
					deleteStatus = true;
				}
			}*/
						
			
			
			if(listUserBo == null || listUserBo.size() <= 0){
				msgTxt.setVisibility(View.VISIBLE);
			}
			
			/*if(deleteStatus){
				Toast.makeText(MessageActivity.this, "selected item deleted!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(MessageActivity.this, "no, item selected!", Toast.LENGTH_SHORT).show();
			}*/
			
		}else if(v == settingBtn){
			Intent intent = new Intent(MessageActivity.this, ActivitySetting.class);
			startActivity(intent);
		}
	}

	
	
	private void getMessageFromServer(final JSONStringer data){

		pd.setTitle("Please wait");
		pd.show();
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_message_list,data);
					
					handler.sendEmptyMessage(0);
				}
			}.start();
			
			handler=new Handler(){
				
				
				public void handleMessage(android.os.Message msg) {
					pd.dismiss();
					try {
						boolean status=obj.getBoolean("Status");
						if(status){
							parsing();
							setData();
						}
						Toast.makeText(MessageActivity.this, obj.getString("Message"),2000).show();
									} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	
	private void parsing(){
		listUserBo = new ArrayList<UserBo>();
		try{
		JSONArray array=obj.getJSONArray("List");
		for (int i = 0; i < array.length(); i++) {
			UserBo bo=new UserBo();
			JSONObject jObj=array.getJSONObject(i);
			String fullName="Sender";//obj.getString("");
			 String msgHeading=jObj.getString("MessageHeader");
			 String msgDesc=jObj.getString("MessageBody");
			 String dateTime=jObj.getString("MessageTime");
			 String id=jObj.getString("MessageID");
			 String dateStamp=jObj.getString("MessageTime");
			
			 boolean readStatus=jObj.getBoolean("IsRead");
			 //boolean status=obj.getBoolean("");
			 
			 bo.setDateStamp(dateStamp);
			 bo.setDateTime(dateTime);
			 bo.setFullName(fullName);
			 bo.setId(id);
			 bo.setMsgDesc(msgDesc);
			 bo.setMsgHeading(msgHeading);
			 bo.setReadStatus(readStatus);
			// bo.setStatus(status);
			 listUserBo.add(bo);
			
		}
		
		 
		}catch (Exception e) {
			// TODO: handle exception
		}
		
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
					deleteCount--;
					try {
						boolean status=obj.getBoolean("Status");
						if(status && deleteCount<=0){
							Toast.makeText(MessageActivity.this, obj.getString("Message"),2000).show();
							createParameter();
						}else{
							Toast.makeText(MessageActivity.this, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	
	private void Message(final JSONStringer data){
		
		 
		 
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
					deleteCount--;
					try {
						boolean status=obj.getBoolean("Status");
						if(status && deleteCount<=0){
							Toast.makeText(MessageActivity.this, obj.getString("Message"),2000).show();
							createParameter();
						}else{
							Toast.makeText(MessageActivity.this, obj.getString("Message"),2000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	

}
