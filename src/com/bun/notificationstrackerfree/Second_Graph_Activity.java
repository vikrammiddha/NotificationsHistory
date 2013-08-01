package com.bun.notificationstrackerfree;


import java.util.HashMap;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.bun.notificationstrackerfree.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class Second_Graph_Activity extends Activity{
	
	    
    public static final String TYPE = "type";
    private static int[] COLORS = new int[] { Color.BLACK,Color.BLUE,Color.CYAN,Color.GREEN,Color.LTGRAY,Color.MAGENTA,Color.RED,Color.YELLOW, 
		Color.parseColor("#FF6C0A"), Color.parseColor("#DBB407"), Color.parseColor("#9E9197") };
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
	
	private GestureDetector gestureDetector;
	DBController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.second_graph);
		
		controller = new DBController(this);
		
		gestureDetector = new GestureDetector(
                new SwipeGestureDetector());
		
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
              LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
              mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
              mRenderer.setClickEnabled(true);
              mRenderer.setSelectableBuffer(10);
              layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                  LayoutParams.FILL_PARENT));
            } else {
              mChartView.repaint();
            }
        fillPieChart();

		
	}
	
	public void fillPieChart(){
		HashMap<String,Integer> pieChartValues = controller.getPieGraphData();
		Integer count = 0;
        
		for(String app : pieChartValues.keySet())
        {
			int c =0;
			
			try{
				c = COLORS[count];
			
			}catch(Exception e){
				Random r = new Random();
				c = Color.argb( 255, r.nextInt(255), r.nextInt(255), r.nextInt(255) );
			}
			
			count++;
             mSeries.add(app, pieChartValues.get(app));
                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(c);
                mRenderer.addSeriesRenderer(renderer);
                if (mChartView != null)
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
		  Intent intentG=new Intent(getApplicationContext(), Bar_Chart_Activity.class);	    		    	
		  startActivity(intentG);	
		  finish();
	  }

	  private void onRightSwipe() {
		  
		  Intent intentG=new Intent(getApplicationContext(), First_Graph_Activity.class);	    		    	
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
	           Second_Graph_Activity.this.onLeftSwipe();

	        // Right swipe
	        } else if (-diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	Second_Graph_Activity.this.onRightSwipe();
	        }
	      } catch (Exception e) {
	        Log.e("YourActivity", "Error on gestures");
	      }
	      return false;
	    }
	  }
	
	
	
}
