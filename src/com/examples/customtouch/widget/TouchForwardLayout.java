/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TouchForwardLayout extends FrameLayout {

	public TouchForwardLayout(Context context) {
		super(context);
	}

	public TouchForwardLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchForwardLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//Don't let any touches be passed down to the children automatically
		return true;
	}
	
	/*
	 * In onTouchEvent(), we pass all the touches we receive directly to the
	 * first child by calling its dispatchTouchEvent() method.
	 * 
	 * Note that, because we do not modify the event at all, the touch's location
	 * will still report as being outside the bounds of the view we are forwarding
	 * to. This may have consequences depending on the reasoning for forwarding the event.
	 * MotionEvent.setLocation() can be used to modify the x/y before forwarding.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Forward all touch events to the first child
		View child = getChildAt(0);
		if(child == null) {
			return false;
		}
		
		return child.dispatchTouchEvent(event);		
	}
}
