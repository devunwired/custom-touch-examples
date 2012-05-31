/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class CustomTouchActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LongPressImageView iv = new LongPressImageView(this);
        iv.setImageResource(R.drawable.ic_launcher);
        
        ColorChangeLayout layout = new ColorChangeLayout(this);
        layout.addView(iv, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        
        setContentView(layout);
    }
    
    public static String getNameForAction(int action) {
    	switch(action) {
    	case MotionEvent.ACTION_DOWN:
    		return "DOWN";
    	case MotionEvent.ACTION_MOVE:
    		return "MOVE";
    	case MotionEvent.ACTION_CANCEL:
    		return "CANCEL";
    	case MotionEvent.ACTION_UP:
    		return "UP";
    	default:
    		return "OTHER";
    	}
    }
}