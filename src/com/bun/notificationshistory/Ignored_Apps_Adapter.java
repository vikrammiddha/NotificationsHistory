package com.bun.notificationshistory;
import java.util.ArrayList;



import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;


public class Ignored_Apps_Adapter extends BaseAdapter{
	
	private ArrayList<Notification> nList = new ArrayList<Notification>();
	
	public ArrayList<Notification> getNotifications(){
		return nList;
	}
	
	public void addNotification(Notification n){
		nList.add(n);
	}
	
	public void clearNotifications(){
		nList.clear();
	}
	
	@Override
	public int getCount() {
		return nList.size();
	}

	@Override
	public Object getItem(int position) {
		return nList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Notification n = nList.get(position);
		if(view == null){
			LayoutInflater inflater =
					LayoutInflater.from(parent.getContext());
			view = inflater.inflate(
					R.layout.ignored_apps_row, parent, false);
		}
		View v = view.findViewById(R.id.ignoredAppIconImageViewId);
		final ImageView imageView = (ImageView)v;
		
		imageView.setImageDrawable(n.getAppIcon());
		
		v = view.findViewById(R.id.ignoredAppNameTextViewId);
		final TextView appTextView = (TextView)v;
		
		appTextView.setText(n.getAppName());
		
		appTextView.setTextColor(Color.BLACK);
		
		v = view.findViewById(R.id.ignoredAppCheckBoxId);
		
		final CheckBox cb = (CheckBox)v;
		
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	        @Override
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	            if(isChecked ){
	            	n.setIsRowChecked(true);
	            	appTextView.setBackgroundColor(Color.parseColor("#0099FF"));
	            	cb.setBackgroundColor(Color.parseColor("#0099FF"));
	            	imageView.setBackgroundColor(Color.parseColor("#0099FF"));
	            }else
	            {
	            	n.setIsRowChecked(false);
	            	appTextView.setBackgroundColor(Color.WHITE);
	            	cb.setBackgroundColor(Color.WHITE);
	            	imageView.setBackgroundColor(Color.WHITE);
	            }
	        }
	    });
						                                                                                         
		return view;
	}

}
