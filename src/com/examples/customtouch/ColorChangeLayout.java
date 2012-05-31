/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class ColorChangeLayout extends FrameLayout {

	public ColorChangeLayout(Context context) {
		super(context);
	}

	public ColorChangeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColorChangeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/*
	 * In onInterceptTouchEvent(), we are checking to see when a user drag begins.  When
	 * the events have moved significantly enough to be considered a drag, this view
	 * starts intercepting the touch events.
	 * 
	 * Notice we are monitoring ACTION_DOWN to get the initial touch location, but do not
	 * return true so the normal touch flow is allowed to continue at this point.
	 */
	private float mInitialX, mInitialY;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mInitialX = event.getX();
			mInitialY = event.getY();
			return false;
		case MotionEvent.ACTION_MOVE:
			int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
			//Verify touch has moved enough
			if(Math.abs(event.getX() - mInitialX) > slop
					|| Math.abs(event.getY() - mInitialY) > slop) {
				//Start capturing events
				return true;
			}
			return false;
		default:
			return false;
		}
	}
	
	/*
	 * onTouchEvent() will be called either if no children handle the incoming event, or if
	 * we have intercepted it.  We monitor ACTION_DOWN to account for the first case, to guarantee
	 * we continue to receive touch events.
	 * 
	 * Once we start intercepting views, the motion generates a background color based on the
	 * current touch point location.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//Show interest in events not handled by one of our children
			return true;
		case MotionEvent.ACTION_MOVE:
			float hPercent = event.getX() / getWidth();
			float vPercent = event.getY() / getHeight();
			int red = (int)(255 * hPercent);
			int blue = (int)(255 * vPercent);
			int green = (red + blue) / 3;
			setBackgroundColor( Color.argb(255, red, green, blue) );
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}
}
