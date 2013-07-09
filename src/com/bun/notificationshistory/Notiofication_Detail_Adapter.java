package com.bun.notificationshistory;
import java.util.ArrayList;



import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Notiofication_Detail_Adapter extends BaseAdapter{
	
	private ArrayList<Notification> nList = new ArrayList<Notification>();
	
	public void addNotification(Notification n){
		nList.add(n);
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
		Notification n = nList.get(position);
		if(view == null){
			LayoutInflater inflater =
					LayoutInflater.from(parent.getContext());
			view = inflater.inflate(
					R.layout.notification_detail_row, parent, false);
		}
		View v = view.findViewById(R.id.notoficationRowDetailTextViewId);
		TextView timeTextView = (TextView)v;
		
		timeTextView.setText(n.getMessage());
		if(position%2 == 0){
			timeTextView.setBackgroundColor(Color.WHITE);
			timeTextView.setTextColor(Color.BLACK);
		}else{
			timeTextView.setBackgroundColor(Color.WHITE);
			timeTextView.setTextColor(Color.BLACK);
		}
		
		v = view.findViewById(R.id.dateTimeTextViewId);
		
		TextView dateTimeTextView = (TextView)v;
		dateTimeTextView.setText(n.getNotDate());
		dateTimeTextView.setTextColor(Color.BLACK);
		//timeTextView.setTextColor(Color.rgb(255,255,255));
		//ImageView iv = (ImageView)view.findViewById(R.id.imageView1);
		//iv.setImageBitmap(c.getPhoto());                                                                                          
		return view;
	}

}
