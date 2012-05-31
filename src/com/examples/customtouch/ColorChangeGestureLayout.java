/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ColorChangeGestureLayout extends FrameLayout {

	public ColorChangeGestureLayout(Context context) {
		super(context);
		init();
	}

	public ColorChangeGestureLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorChangeGestureLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private GestureDetector mDetector;
	private void init() {
		mDetector = new GestureDetector(mListener);
		//If you are not using the long-press feature, it is a good idea to disable it
		// The timers used can create some undesired behavior in handling other gestures.
		mDetector.setIsLongpressEnabled(false);
	}
	
	/*
	 * SimpleOnGestureListener here only implements the onScroll() method, since we
	 * are only interested in whether or not the user begins dragging their finger.
	 */
	private SimpleOnGestureListener mListener = new SimpleOnGestureListener() {
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float hPercent = e2.getX() / getWidth();
			float vPercent = e2.getY() / getHeight();
			int red = (int)(255 * hPercent);
			int blue = (int)(255 * vPercent);
			int green = (red + blue) / 3;
			setBackgroundColor( Color.argb(255, red, green, blue) );
			
			return true;
		}
	};
	
	/*
	 * In onInterceptTouchEvent() we utilize the GestureDetector to let us
	 * know when the user begins scrolling, at which point this view will
	 * take over all future events.
	 * 
	 * We can use the detector here as well as in onTouchEvent() because we don't
	 * explicitly handle onDown() there, which would intercept everything.  This way,
	 * clickable child views can still handle touch events normally.
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mDetector.onTouchEvent(ev);
	}
	
	/*
	 * In onTouchEvent(), we pass all events to the GestureDetector, which
	 * evaluates all the parameters for us as to whether the user is dragging
	 * their finger.
	 * 
	 * We also explicitly handle ACTION_DOWN in case one of our children does not.
	 * This could be done in OnGestureListener.onDown(), but we want to re-use our
	 * detector for intercepts as well.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			//If no child handles the initial touch, capture it
			return true;
		}
		if(mDetector.onTouchEvent(event)) {
			return true;
		}
		
		return super.onTouchEvent(event);
	}
}
