package com.bun.notificationshistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class Notification_Adapter extends BaseAdapter{
	
	private ArrayList<Notification> nList = new ArrayList<Notification>();
	
	public void addNotification(Notification notf){
		nList.add(notf);
	}
	
	public void clearNotifications(){
		nList.clear();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return nList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Notification n = nList.get(position);
		if(view == null){
			LayoutInflater inflater =
					LayoutInflater.from(parent.getContext());
			if(n.getIsSectionHeader() != null && n.getIsSectionHeader()){
				view = inflater.inflate(
						R.layout.section_header, parent, false);
				
			}else{
				view = inflater.inflate(
						R.layout.notification_row, parent, false);
				
			}
			
		}
		if(n.getIsSectionHeader() != null && n.getIsSectionHeader()){
			LayoutInflater inflater =
					LayoutInflater.from(parent.getContext());
			view = inflater.inflate(
					R.layout.section_header, parent, false);
			View v = view.findViewById(R.id.sectionHeaderTextViewId);
			TextView timeTextView = (TextView)v;
			String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			if(position == 0 && n.getSectionHeaderValue().equals(date)){
				timeTextView.setText("Today");
			}else{
				timeTextView.setText(n.getSectionHeaderValue() != null ? n.getSectionHeaderValue() : "Previous Days");
			}
			
			timeTextView.setTextColor(Color.rgb(255,255,255));
		}else{
			LayoutInflater inflater =
					LayoutInflater.from(parent.getContext());
			view = inflater.inflate(
					R.layout.notification_row, parent, false);
			View v = view.findViewById(R.id.notificationRowTextViewId);
			TextView timeTextView = (TextView)v;
			
			timeTextView.setText(n.getAppName() );
			
			v = view.findViewById(R.id.appImageViewId);
			
			ImageView iv = (ImageView)v;
			
			if(n.getAppIcon() != null)
				iv.setImageDrawable(n.getAppIcon());
			
			v = view.findViewById(R.id.notificationCountId);
			
			TextView countText = (TextView)v;
			
			if(n.getNotificationCount() != null && n.getNotificationCount() > -1){
				countText.setText(String.valueOf(n.getNotificationCount()));
			}else{
				countText.setVisibility(View.GONE);
				iv.setVisibility(View.GONE);
			}
			
			//timeTextView.setTextColor(Color.rgb(255,255,255));
		}
		
		
		
		                                                                                      
		return view;
	}

}
