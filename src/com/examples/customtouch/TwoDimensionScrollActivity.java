/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TwoDimensionScrollActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TwoDimensionScrollView scrollView = new TwoDimensionScrollView(this);
		
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.ic_launcher);
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		scrollView.addView(iv, lp);
		setContentView(scrollView);
	}
}
