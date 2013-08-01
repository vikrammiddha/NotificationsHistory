package com.bun.notificationstrackerfree;


import java.util.ArrayList;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.bun.notificationstrackerfree.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class Bar_Chart_Activity extends Activity{
	
	
    private static int[] COLORS = new int[] { Color.BLACK,Color.BLUE,Color.CYAN,Color.GREEN,Color.LTGRAY,Color.MAGENTA,Color.RED,Color.YELLOW, 
		Color.parseColor("#FF6C0A"), Color.parseColor("#DBB407"), Color.parseColor("#9E9197") };
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
	
	private GestureDetector gestureDetector;
	DBController controller;
	LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.third_graph);
		
		controller = new DBController(this);
		
		gestureDetector = new GestureDetector(
                new SwipeGestureDetector());
		
		layout = (LinearLayout) findViewById(R.id.barchart);
		
		mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.GRAY);
        mRenderer.setChartTitle("App Notifications Pie Chart");
        mRenderer.setChartTitleTextSize(50);        
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(25);
        mRenderer.setLabelsTextSize(30);
        
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setShowLabels(false);
        mRenderer.setStartAngle(90);
        
         
        if (mChartView == null) {
            
        	HashMap<String,Integer> barData = controller.getPieGraphData();
        	
        	ArrayList<String> appList = new ArrayList<String>();
        	
        	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    		
        	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        	
        	Integer counter = 0;
        	
        	Integer colorCounter = 0;
        	
        	Integer maxNot = 0;
        	
        	for(String app : barData.keySet()){
        		CategorySeries series = new CategorySeries(app);
        		appList.add(app);
        		if(barData.get(app) > maxNot){
        			maxNot = barData.get(app);
        		}
        		for(int i=0; i< barData.size(); i++){
        			if(i == counter){
        				series.add(barData.get(app));
        			}else{
        				series.add(0);
        			}
        		}
        		
        		dataset.addSeries(series.toXYSeries());
        		XYSeriesRenderer renderer = new XYSeriesRenderer();
        	    renderer.setDisplayChartValues(true);  
        	    Integer c;
        	    try{
        	    	 c = COLORS[colorCounter++];
        	    }catch(Exception e){
        	    	colorCounter = 0;
        	    	c = COLORS[colorCounter++];
        	    }
        	    renderer.setColor(c);
        	    mRenderer.addSeriesRenderer(renderer);
        		counter++;
        	}
        	
    		
    		// This is how the "Graph" itself will look like
    		
    		mRenderer.setChartTitle("Demo Graph Title");
    		mRenderer.setXTitle("");
    		mRenderer.setYTitle("Notification Counts");
    		mRenderer.setAxesColor(Color.BLACK);
    	    mRenderer.setLabelsColor(Color.BLACK);
    	    mRenderer.setChartTitle("");    	   
    	    mRenderer.setLegendTextSize(30);
    	    mRenderer.setLabelsTextSize(20);
    	    mRenderer.setMargins(new int[]{5,50,80,40});
    	    mRenderer.setShowLegend(false);
    	    // Customize bar 1 		
    	    
    	    
    	    mRenderer.setXAxisMax(barData.size() + 1);
    	    mRenderer.setXAxisMin(0);
    	    mRenderer.setBarWidth(30);
    	    mRenderer.setBarSpacing(0);
    	    mRenderer.setXLabelsAngle(-90);
    	    mRenderer.setYAxisMax(maxNot + 10);
    	    mRenderer.setApplyBackgroundColor(true);
    	    mRenderer.setBackgroundColor(Color.WHITE);
    	    mRenderer.setMarginsColor(Color.WHITE);
    	    mRenderer.setXLabelsColor(Color.BLACK);
    	    mRenderer.setYLabelsColor(0,Color.BLACK);
    	    
    	    
      	    // Customize bar 2
      	    
      	  for(int i=0; i< counter;i++){
      		mRenderer.addXTextLabel(i+1, appList.get(i));          
          }
      	    
	          mChartView = ChartFactory.getBarChartView(this, dataset, mRenderer, Type.STACKED);
	          mRenderer.setClickEnabled(true);
	          mRenderer.setSelectableBuffer(10);
	          
	          layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
	              LayoutParams.FILL_PARENT));
	        } else {
	          mChartView.repaint();
	        }
        
		
	}
	


	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    if (gestureDetector.onTouchEvent(event)) {
	      return true;
	    }
	    return super.onTouchEvent(event);
	  }

	  private void onLeftSwipe() {
		  
	  }

	  private void onRightSwipe() {
		  Intent intentG=new Intent(getApplicationContext(), Second_Graph_Activity.class);	    		    	
		  startActivity(intentG);	
		  finish();
		  
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
	        	Bar_Chart_Activity.this.onLeftSwipe();

	        // Right swipe
	        } else if (-diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	Bar_Chart_Activity.this.onRightSwipe();
	        }
	      } catch (Exception e) {
	        Log.e("YourActivity", "Error on gestures");
	      }
	      return false;
	    }
	  }
	
	
	
}
