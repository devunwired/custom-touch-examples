/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

public class TwoDimensionScrollView extends FrameLayout {
	
	public TwoDimensionScrollView(Context context) {
		super(context);
		init();
	}

	public TwoDimensionScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TwoDimensionScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private GestureDetector mDetector;
	private ScaleGestureDetector mScaleDetector;
	private void init() {
		mDetector = new GestureDetector(mListener);
		mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
		
		setStaticTransformationsEnabled(true);
	}
	
	/*
	 * SimpleOnScaleGestureListener handles the zoom events using its onScale() method
	 */
	private SimpleOnScaleGestureListener mScaleListener = new SimpleOnScaleGestureListener() {
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			//getScaleFactor() returns a multiplier based on whether the fingers moved
			// closer or farther since the last event.  This value is usually between 0.9 and 1.1
			mZoomScale *= detector.getScaleFactor();
			//Don't allow zoom level to go below original size
			mZoomScale = Math.max(mZoomScale, 1.0f);
			//Force a redraw with the new scale factor
			invalidate();
			return true;
		}
		
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mPivotX = detector.getFocusX();
			mPivotY = detector.getFocusY();
			return true;
		}
	};
	
	/*
	 * SimpleOnGestureListener handles the pan events using its onScroll() method
	 */
	private SimpleOnGestureListener mListener = new SimpleOnGestureListener() {
		public boolean onDown(android.view.MotionEvent e) {
			return true;
		}
		
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			//Any view can be scrolled by simply calling its scrollBy() method
			scrollBy((int)distanceX, (int)distanceY);
			return true;
		}
	};
	
	/*
	 * In this example, we intercept all touch events, so interactive child
	 * views would not work here.  We could rewrite this to be smarter and only
	 * intercept when a pan or zoom begins.
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
	
	/*
	 * onTouchEvent() checks whether this is a pan or zoom gesture
	 * based on the number of fingers on the screen.  It hands the event
	 * to the appropriate detector.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getPointerCount()) {
		case 1:
			return mDetector.onTouchEvent(event);
		default:
			return mScaleDetector.onTouchEvent(event);
		}
	}
	
	/*
	 * We are using static transformations to zoom the contents of whatever view is placed
	 * within this container
	 */
	private float mZoomScale = 1.0f;
	private float mPivotX, mPivotY;
	private Matrix mMatrix;
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		t.clear();
		mMatrix = t.getMatrix();
		mMatrix.postScale(mZoomScale, mZoomScale, mPivotX, mPivotY);
		
		return true;
	}
}
