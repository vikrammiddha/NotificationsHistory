package com.bun.notificationshistory;


import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import java.util.HashMap;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.LineAndPointFormatter;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYLegendWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;


import android.os.Bundle;

import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import android.view.GestureDetector.SimpleOnGestureListener;

import android.widget.LinearLayout;

import android.widget.Toast;


public class First_Graph_Activity extends Activity{
	private GestureDetector gestureDetector;
	private XYPlot plot;
	DBController controller;
	
	static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;
 
    PointF firstFinger;
    float lastScrolling;
    float distBetweenFingers;
    float lastZooming;
	
    private PointF minXY;
    private PointF maxXY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.first_graph);
		controller = new DBController(this);
		
		gestureDetector = new GestureDetector(
                new SwipeGestureDetector());
		
		ArrayList<HashMap<String,String>> graphData = controller.getLineGraphData();
		
		final ArrayList<String> days = controller.getLineGraphDays(); 
		
		final String[] daysArr;// = new String[days.size()];
		
		//daysArr = (String[]) days.toArray();
		
		String initApp = graphData.get(0).get("appName");
		
		ArrayList<String> topTenApps = controller.getTopTenApps();
		
		ArrayList<Number> seriesList = new ArrayList<Number>();
				
		Boolean populateSeries = false;
		
		Integer count = 0;
		
		Integer[] colorArr = {Color.BLACK,Color.BLUE,Color.CYAN,Color.GREEN,Color.LTGRAY,Color.MAGENTA,Color.RED,Color.YELLOW, 
									Color.parseColor("#FF6C0A"), Color.parseColor("#DBB407"), Color.parseColor("#9E9197")};
		
		plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		
		Integer tempSeriesNumber = 0;
		
		Integer colorCounter = 0;
		
		String prevApp = "";
		
		ArrayList<String> daysCovered = new ArrayList<String>();
		
		for(HashMap<String,String> hm : graphData){
			
			count++;
			
			prevApp = initApp;
	        
			if(initApp.equals(hm.get("appName"))){
				daysCovered.add(hm.get("notTime"));
				if(days.contains(hm.get("notTime"))){
					seriesList.add((Number)Integer.valueOf(hm.get("count")));
					//seriesList.add(getDaysIndex(days), (Number)Integer.valueOf(hm.get("count")));
				}else{
					seriesList.add(0);
				}
				
				if(count == graphData.size()){
					populateSeries = true;
					if(daysCovered.size() != days.size()){
						ArrayList<Number> tempList = new ArrayList<Number>();
						Integer index = 0;
						for(String d : days){
							if(daysCovered.contains(d)){
								tempList.add(seriesList.get(index++));
							}else{
								tempList.add(0);
							}
						}
						
						seriesList.clear();
						seriesList.addAll(tempList);
						
					}
				}
			}else{
				populateSeries = true;
				//fillZeroesInSeries(seriesList);
				initApp = hm.get("appName");
				if(days.contains(hm.get("notTime"))){
					
					tempSeriesNumber = Integer.valueOf(hm.get("count"));
				}else{
					tempSeriesNumber = 0;
				}
				
				if(daysCovered.size() != days.size()){
					ArrayList<Number> tempList = new ArrayList<Number>();
					Integer index = 0;
					for(String d : days){
						if(daysCovered.contains(d)){
							tempList.add(seriesList.get(index++));
						}else{
							tempList.add(0);
						}
					}
					
					seriesList.clear();
					seriesList.addAll(tempList);
					
				}
				daysCovered.clear();
				daysCovered.add(hm.get("notTime"));
			}
		
	        
			if(populateSeries){
				
				//seriesNumbers = (Number[])seriesSet.toArray();
				
				XYSeries series2 = new SimpleXYSeries(seriesList, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, prevApp);
				 
				Integer c;
				
				try{
					c = colorArr[colorCounter];
				}catch(Exception e){
					colorCounter = 0;
					c = colorArr[colorCounter];
				}
				
		        LineAndPointFormatter series2Format = new LineAndPointFormatter(c,Color.BLACK, 
		        											Color.rgb(200, 200, 200),null);
		        
		        		        
		        series2Format.setFillPaint(null);
		        if(topTenApps.contains(prevApp)){
		        	plot.addSeries(series2, series2Format);
		        	colorCounter++;
		        }
		        
		        
		        
		        seriesList.clear();		        
		        
		        seriesList.add(tempSeriesNumber);
		        
		        populateSeries = false;
		        
		        //daysCovered.clear();
			}
		}
	        // same as above
	        
		
		
			
		 // initialize our XYPlot reference:
        
		//plot.getLegendWidget().setSize(new SizeMetrics(300, SizeLayoutType.ABSOLUTE, 600, SizeLayoutType.ABSOLUTE));
		
		plot.getLegendWidget().setTableModel(new DynamicTableModel(3, 4));
		 
        // adjust the legend size so there is enough room
        // to draw the new legend grid:
		plot.getLegendWidget().setSize(new SizeMetrics(200, SizeLayoutType.ABSOLUTE, 600, SizeLayoutType.ABSOLUTE));
 
        // add a semi-transparent black background to the legend
        // so it's easier to see overlaid on top of our plot:
        
 
        // adjust the padding of the legend widget to look a little nicer:
        plot.getLegendWidget().setPadding(10, 1, 1, 1);       
        
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 20);
 
        // reposition the grid so that it rests above the bottom-left
        // edge of the graph widget:
       
        // reduce the number of range labels
        plot.setTicksPerRangeLabel(1);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
         
        //plot.getBackgroundPaint().setColor(Color.WHITE);
        
        
        Format f = new Format() {
 
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            	String s = String.valueOf(days.get( ( (Number)obj).intValue())) ;
            	int ind = (int)Math.ceil((Double)obj);
            	String val = days.get( ind ).split("-")[0] + "-" + days.get( ind ).split("-")[1]; 
                return new StringBuffer( val);
            }
 
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }

        };
        
        plot.setDomainStep(XYStepMode.SUBDIVIDE, days.size());
        plot.setDomainValueFormat(f);
		
        plot.setPlotMarginBottom(15);
        plot.setDomainLabel("");
       		
        minXY=new PointF(plot.getCalculatedMinX().floatValue(),plot.getCalculatedMinY().floatValue());
        maxXY=new PointF(plot.getCalculatedMaxX().floatValue(),plot.getCalculatedMaxY().floatValue());
        
        
		
	}
	
	private void drawGraph(Context ctx, LinearLayout layout){

	
	}
	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    if (gestureDetector.onTouchEvent(event)) {
	    	this.overridePendingTransition(R.anim.animation_leave,
	                R.anim.animation_enter);
	      return true;
	    }
	    return super.onTouchEvent(event);
	  }

	  private void onLeftSwipe() {
		  Toast.makeText(First_Graph_Activity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
		  Intent intentG=new Intent(getApplicationContext(), Second_Graph_Activity.class);	    		    	
		  startActivity(intentG);	
		  finish();
		  
	  }

	  private void onRightSwipe() {
		  
	  }

	  // Private class for gestures
	  private class SwipeGestureDetector 
	          extends SimpleOnGestureListener {
	    // Swipe properties, you can change it to make the swipe 
	    // longer or shorter and speed
	    private static final int SWIPE_MIN_DISTANCE = 120;
	    private static final int SWIPE_MAX_OFF_PATH = 200;
	    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2,
	                         float velocityX, float velocityY) {
	      try {
	        float diffAbs = Math.abs(e1.getY() - e2.getY());
	        float diff = e1.getX() - e2.getX();

	        if (diffAbs > SWIPE_MAX_OFF_PATH)
	          return false;
	        
	        // Left swipe
	        if (diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	           First_Graph_Activity.this.onLeftSwipe();

	        // Right swipe
	        } else if (-diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	First_Graph_Activity.this.onRightSwipe();
	        }
	      } catch (Exception e) {
	        Log.e("YourActivity", "Error on gestures");
	      }
	      return false;
	    }
	  }

}
