/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

public class LongPressImageView extends ImageView {

	public LongPressImageView(Context context) {
		super(context);
		init();
	}

	public LongPressImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LongPressImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private Matrix mIdentity;
	private void init() {
		setScaleType(ScaleType.MATRIX);
		mIdentity = new Matrix();
		setImageMatrix(mIdentity);
        
		setBackgroundColor(Color.WHITE);
	}
	
	private static final int LONG_PRESS = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			//Handle long press event
			Matrix half = new Matrix();
			half.postScale(0.5f, 0.5f, getWidth()/2, getHeight()/2);
			setImageMatrix(half);
			//Fire the system long-click event
			performLongClick();
		}
	};
	
	private void cancelPressEvent() {
		//Remove any pending events
		mHandler.removeMessages(LONG_PRESS);
		//Reset the scale
		setImageMatrix(mIdentity);
		//Reset the pressed state
		setBackgroundColor(Color.WHITE);
	}
	
	/*
	 * In onTouchEvent() we create a custom implementation of a long-press gesture, using
	 * the system's standard timeout value.  If the user drags their finger outside the bounds
	 * of the view, releases their finger, or if the action is cancelled by our parent, the
	 * long-press timer will cancel and the event will not fire.
	 * 
	 * We would receive an ACTION_CANCEL if the parent view we are attached to decides to intercept
	 * touch events at any time after we had started to process them.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mHandler.sendMessageDelayed(Message.obtain(mHandler, LONG_PRESS), ViewConfiguration.getLongPressTimeout());
			//Show pressed state
			setBackgroundColor(Color.GRAY);
			return true;
		case MotionEvent.ACTION_MOVE:
			//Check if touch moves outside view bounds
			Rect hit = new Rect();
			getHitRect(hit);
			int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
			hit.inset(-slop, -slop);
			if(hit.contains((int)event.getRawX(), (int)event.getRawY())) {
				return false;
			}
			return true;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			cancelPressEvent();
			return false;
		default:
			return super.onTouchEvent(event);
		}
	}

}
