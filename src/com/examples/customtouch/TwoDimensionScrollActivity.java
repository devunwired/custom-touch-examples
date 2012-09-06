/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TwoDimensionScrollActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TwoDimensionScrollView scrollView = new TwoDimensionScrollView(this);
//        TwoDimensionGestureScrollView scrollView = new TwoDimensionGestureScrollView(this);

//		ImageView iv = new ImageView(this);
//		iv.setImageResource(R.drawable.ic_launcher);
//
//		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(800, 1500);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        for(int i=0; i < 5; i++) {
    		ImageView iv = new ImageView(this);
	    	iv.setImageResource(R.drawable.ic_launcher);
            layout.addView(iv, new LinearLayout.LayoutParams(1000, 500));
        }

		scrollView.addView(layout);
		setContentView(scrollView);
	}
}
