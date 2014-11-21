package com.esl.amit.eslapplication.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.esl.amit.eslapplication.R;
import com.esl.amit.eslapplication.bo.ModelCode;
import com.esl.amit.eslapplication.util.Utils;
import com.web.JsonParser;

public class ActivityEditRegistrationCode extends Activity implements OnClickListener {
	private Button add, remove;
	private ListView myList;
	ArrayList<String> listContent ;
	JSONObject obj;
	Handler handler;
	JSONStringer TestApp=null;
	SharedPreferences prefs;
	ArrayAdapter<String> adapter;
	ArrayList<ModelCode> listCode;
	ArrayList<String> mSelectedCodeIds;
	int deleteCount;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_registration);
		setTitleColor();
		pd=new ProgressDialog(this);
		prefs=this.getSharedPreferences("userdata", Context.MODE_PRIVATE);
		add = (Button) findViewById(R.id.addr_add);
		remove = (Button) findViewById(R.id.addr_remove);
		add.setOnClickListener(this);
		remove.setOnClickListener(this);
		myList = (ListView) findViewById(R.id.addr_list_code);
		
		mSelectedCodeIds=new ArrayList<String>();
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(mSelectedCodeIds.contains(listCode.get(arg2).getCodeId())){
					mSelectedCodeIds.remove(arg2);
				}else{
					mSelectedCodeIds.add(listCode.get(arg2).getCodeId());
				}
				
			}
		});
		
	}
	
	private void setTitleColor(){
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.GRAY); 
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		createParameter();
	}

	@Override
	public void onClick(View v) {
		String selected = "";
		int cntChoice=0;
		SparseBooleanArray sparseBooleanArray;
		switch (v.getId()) {
		case R.id.addr_add:
			addCodeDialog();
			/*cntChoice = myList.getCount();

			sparseBooleanArray = myList.getCheckedItemPositions();

			for (int i = 0; i < cntChoice; i++) {

				if (sparseBooleanArray.get(i)) {

					selected += myList.getItemAtPosition(i).toString() + "\n";

				}

			}
*/
			/*Toast.makeText(ActivityEditRegistrationCode.this,

			selected,

			Toast.LENGTH_LONG).show();*/

			break;
		case R.id.addr_remove:
			
			
			deleteCodes();
			/*cntChoice = myList.getCount();

			sparseBooleanArray = myList.getCheckedItemPositions();

			for (int i = 0; i < cntChoice; i++) {

				if (sparseBooleanArray.get(i)) {

					selected += myList.getItemAtPosition(i).toString() + "\n";

				}

			}

			Toast.makeText(ActivityEditRegistrationCode.this,

			selected,

			Toast.LENGTH_LONG).show();*/

			break;
		}

	}
	
	
	private void setData(){
		ArrayList<String> list=fetchCodeOnly();
		if(list==null){
			remove.setEnabled(false);
			return;
		}else if(list.size()<=0){
			remove.setEnabled(false);
			return;
		}
		remove.setEnabled(true);
		 adapter = new ArrayAdapter<String>(this,

					android.R.layout.simple_list_item_multiple_choice,list);
					myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

					myList.setAdapter(adapter);
					
		
	
	}
	
	private ArrayList<String> fetchCodeOnly(){
		if(listCode.size()<=0)return null;
		listContent=new ArrayList<String>();
		
		for (int i = 0; i < listCode.size(); i++) {
			
			listContent.add(listCode.get(i).getCode());
		}
		
		return listContent;
	}
	
	private void createParameter(){
		
		try {
			 TestApp = new JSONStringer().object()
					 .key("UserID").value(prefs.getString("userid", "noname")).endObject();
			 
			 getRegistrationCode(TestApp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getRegistrationCode(final JSONStringer data){

		pd.show();
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_registration_code,data);
					
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
						Toast.makeText(ActivityEditRegistrationCode.this, obj.getString("Message"),2000).show();
									} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	
	
	private void parsing(){
		listCode = new ArrayList<ModelCode>();
		try{
		JSONArray array=obj.getJSONArray("List");
		for (int i = 0; i < array.length(); i++) {
			ModelCode bo=new ModelCode();
			JSONObject jObj=array.getJSONObject(i);
			
			 String code=jObj.getString("Code");
			 String codeId=jObj.getString("CodeID");
			
			bo.setCode(code);
			bo.setCodeId(codeId);
			 listCode.add(bo);
			
		}
		
		 
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void deleteCodes(){
		for (int i = 0; i < mSelectedCodeIds.size(); i++) {
			deleteCount++;
			try {
				 TestApp = new JSONStringer().object()
						 .key("UserID").value(prefs.getString("userid", "noname"))
						 .key("CodeIDs").value(mSelectedCodeIds.get(i))
						 .endObject();
				 
				 requestDeleteCode(TestApp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void requestDeleteCode(final JSONStringer data){

		pd.show();
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_delete_registration_code,data);
					
					handler.sendEmptyMessage(0);
				}
			}.start();
			
			handler=new Handler(){
				
				
				public void handleMessage(android.os.Message msg) {
					deleteCount--;
					try {
						boolean status=obj.getBoolean("Status");
						if(status&& deleteCount<=0){
							pd.dismiss();
							createParameter();
							Toast.makeText(ActivityEditRegistrationCode.this, obj.getString("Message"),2000).show();
						}
						
									} catch (JSONException e) {
						// TODO Auto-generated catch block
										pd.dismiss();
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	private void addCode(String code){
		
		try {
			 TestApp = new JSONStringer().object()
					 .key("UserID").value(prefs.getString("userid", "noname"))
					 .key("Code").value(code)
					 .endObject();
			 
			 requestAddCode(TestApp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void requestAddCode(final JSONStringer data){

		pd.show();
		 new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					obj= new JsonParser().callWebService(Utils.url_add_registration_code,data);
					
					handler.sendEmptyMessage(0);
				}
			}.start();
			
			handler=new Handler(){
				
				
				public void handleMessage(android.os.Message msg) {
					pd.dismiss();
					try {
						boolean status=obj.getBoolean("Status");
						if(status){
							createParameter();
							
						}
						Toast.makeText(ActivityEditRegistrationCode.this, obj.getString("Message"),2000).show();
									} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
			};
			
			
		 
	}
	
	
	private void addCodeDialog(){
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter Registration Code");
		

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString().trim();
		  addCode(value);
		  
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}
	

}
