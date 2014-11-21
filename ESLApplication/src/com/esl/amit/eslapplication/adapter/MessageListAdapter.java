package com.esl.amit.eslapplication.adapter;

import java.util.ArrayList;

import org.json.JSONStringer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esl.amit.eslapplication.R;
import com.esl.amit.eslapplication.activity.MessageActivity;
import com.esl.amit.eslapplication.activity.MessageDetailActivity;
import com.esl.amit.eslapplication.bo.UserBo;


public class MessageListAdapter extends ArrayAdapter<UserBo>{
	private ArrayList<UserBo> entries;
	private Activity context;


	public MessageListAdapter(Activity context, int textViewResourceId, ArrayList<UserBo> entries) {
		super(context, textViewResourceId, entries);
		this.entries = entries;
		this.context = context;
	}

	public static class ViewHolder{
		public TextView nameMsgTx, msgDateTxt;		
		public CheckBox checkStatus;
		RelativeLayout layRow;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			LayoutInflater vi =
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_row, null);
			holder = new ViewHolder();
			holder.layRow=(RelativeLayout) v.findViewById(R.id.layRow);
			holder.nameMsgTx = (TextView) v.findViewById(R.id.name_msg_txt);
			holder.msgDateTxt = (TextView) v.findViewById(R.id.msg_date_txt);		
			holder.checkStatus = (CheckBox) v.findViewById(R.id.check_status);
			v.setTag(holder);
		}
		else{
			holder=(ViewHolder)v.getTag();
		}
		
		final UserBo ubo = entries.get(position);
		
		holder.checkStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ubo.isStatus()){					
					ubo.setStatus(false);
				}else{
					ubo.setStatus(true);
				}
				System.out.println("status is --->"+ entries.get(position).isStatus()+ " id is -->"+ ubo.getId());
			}
		});
		
		
		
		
		holder.nameMsgTx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserBo ubo = entries.get(position);
				Intent intent = new Intent(context, MessageDetailActivity.class);
				intent.putExtra("constant", ubo);
				context.startActivity(intent);	
			}
		});
		
		if(ubo.isStatus()){
			holder.checkStatus.setChecked(true);
		}else{
			holder.checkStatus.setChecked(false);
		}
		holder.nameMsgTx.setText(ubo.getMsgHeading()+"\n"+ubo.getFullName());
		holder.msgDateTxt.setText(ubo.getDateTime());
		if(ubo.isReadStatus()){
			
			holder.layRow.setBackgroundColor(context.getResources().getColor(R.color.candidate_other));
		}else{
			
			holder.layRow.setBackgroundColor(context.getResources().getColor(R.color.candidate_background));
		}
		
		return v;
	}
	
	public ArrayList<String> getSelectedData(){
		
		JSONStringer data = null;
		ArrayList<String> messageIds=new ArrayList<String>();
		for (int i = 0; i < entries.size(); i++) {
			if(entries.get(i).isStatus()){
				messageIds.add(entries.get(i).getId());
			}
			
		}
		
		 return messageIds;
	}

	@Override
	public UserBo getItem(int position) {
		// TODO Auto-generated method stub
		return entries.get(position);
	}
}