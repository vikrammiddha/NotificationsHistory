package com.bun.notificationshistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class Notification_Adapter extends BaseAdapter{
	
	private ArrayList<Notification> nList = new ArrayList<Notification>();
	private Context context;
	
	public Notification_Adapter(Context context) {
		super();
		this.context = context;
		
	}
	
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
		
		Boolean isUnread = false;
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
			View v = view.findViewById(R.id.unreadCountTextId);
			
			TextView unreadCountTextView = (TextView)v;
			
			if(Utils.notMap.get(n.getPackageName()) != null && n.getNotTime().equals(Utils.notMap.get(n.getPackageName()).getNotTime())){
				isUnread = true;
				String count = Integer.toString(Utils.notMap.get(n.getPackageName()).getNotificationCount());
				if(count.length() == 1){
					count += " ";
				}
				unreadCountTextView.setText(count);
			}else{
				unreadCountTextView.setText("");
			}
			
			v = view.findViewById(R.id.notificationRowTextViewId);
			TextView timeTextView = (TextView)v;
			
			timeTextView.setText(n.getAppName() );
			
			if(isUnread){
				timeTextView.setVisibility(View.GONE);
			}
			
			v = view.findViewById(R.id.appImageViewId);
			
			ImageView iv = (ImageView)v;
			
			if(n.getAppIcon() != null)
				iv.setImageDrawable(n.getAppIcon());
			
			iv.setTag(n.getPackageName());
			
			if(isUnread){
				AlphaAnimation  blinkanimation= new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
				blinkanimation.setDuration(500); // duration - half a second
				blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
				blinkanimation.setRepeatCount(500); // Repeat animation infinitely
				blinkanimation.setRepeatMode(Animation.REVERSE);
				iv.setAnimation(blinkanimation);
			}
			
			v = view.findViewById(R.id.notificationCountId);
			
			TextView countText = (TextView)v;
			
			if(n.getNotificationCount() != null && n.getNotificationCount() > -1){
				countText.setText(String.valueOf(n.getNotificationCount()));
			}else{
				countText.setVisibility(View.GONE);
				iv.setVisibility(View.GONE);
			}
			
			if(isUnread){
				countText.setVisibility(View.GONE);
			}
			
			v = view.findViewById(R.id.lastActivityDateId);
			
			TextView lastActivityDateText = (TextView)v;
			
			if(isUnread){
				lastActivityDateText.setVisibility(View.GONE);
			}else{
			
				lastActivityDateText.setText( n.getLastActivityDate() != null ? ("Last: " + n.getLastActivityDate())  : "- * -");
			
			}
			
			v = view.findViewById(R.id.trashImageViewId);
			
			ImageView trashImageView = (ImageView)v;
			
			trashImageView.setTag(n.getAppName() + "##" + n.getPackageName());
			
			if(n.getAppName().startsWith("No Notifications")){
				trashImageView.setImageDrawable(null);
			}
			
			v = view.findViewById(R.id.unreadTextId);
			TextView unreadText = (TextView)v;	
			
			if(isUnread){
				String unreadMessge = Utils.notMap.get(n.getPackageName()).getSender() + " : " + Utils.notMap.get(n.getPackageName()).getMessage();
				System.out.println("message unreda ====================" + unreadMessge);
				unreadText.setText(unreadMessge);
				unreadText.setTypeface(Typeface.DEFAULT_BOLD);
				unreadText.setSelected(true);
				unreadText.setTag(n.getPackageName());
			}else{
				unreadText.setVisibility(View.GONE);
			}
			
			//timeTextView.setTextColor(Color.rgb(255,255,255));
		}
		
		//Animation animation = null;
		
		//animation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
		
		//view.startAnimation(animation);
		
		                                                                                      
		return view;
	}

}
