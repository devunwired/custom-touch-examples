/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.examples.customtouch.widget.TwoDimensionScrollView;

public class TwoDimensionScrollActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TwoDimensionScrollView scrollView = new TwoDimensionScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        for(int i=0; i < 5; i++) {
    		ImageButton iv = new ImageButton(this);
	    	iv.setImageResource(R.drawable.ic_launcher);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int width = getResources().getDimensionPixelSize(R.dimen.pan_content_width);
            int height = getResources().getDimensionPixelSize(R.dimen.pan_content_height);
            layout.addView(iv, new LinearLayout.LayoutParams(width, height));
        }

		scrollView.addView(layout);
		setContentView(scrollView);
	}
}
