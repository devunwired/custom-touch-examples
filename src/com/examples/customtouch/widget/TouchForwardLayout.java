/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TouchForwardLayout extends FrameLayout {

    private Point mTouchOffsetPoint = new Point();

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
	 * Note that, we modify the event so the initial location of the touch will
	 * will report as being centered in the view we are forwarding to.  Each event after
	 * this will be offset by the same amount, which creates the effect that the finger. This may have consequences depending on the reasoning for forwarding the event.
	 * MotionEvent.setLocation() can be used to modify the x/y before forwarding.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Forward all touch events to the first child
		View child = getChildAt(0);
		if(child == null) {
			return false;
		}

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //Update this as the offset point
            mTouchOffsetPoint.x = (int)event.getX();
            mTouchOffsetPoint.y = (int)event.getY();
        }

        //Massage the event to be offset from the first touch
        event.offsetLocation(-mTouchOffsetPoint.x + child.getWidth() / 2,
                -mTouchOffsetPoint.y + child.getHeight() / 2);
		
		return child.dispatchTouchEvent(event);		
	}
}
