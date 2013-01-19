package com.examples.customtouch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

public class TwoDimensionGestureScrollView extends FrameLayout {

	private GestureDetector mDetector;
	private OverScroller mScroller;
	
	/* Positions of the last motion event */
	private float mInitialX, mInitialY;
	/* Drag threshold */
	private int mTouchSlop;
	
	public TwoDimensionGestureScrollView(Context context) {
		super(context);
		init(context);
	}

	public TwoDimensionGestureScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TwoDimensionGestureScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mDetector = new GestureDetector(context, mListener);
		mScroller = new OverScroller(context);
		//Get system constants for touch thresholds
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

    /*
    * Override the measureChild... implementations to guarantee that the child view
    * gets measured to be as large as it wants to be.  The default implementation will
    * force some children to be only as large as this view.
    */
    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    //Listener to handle all the touch events
	private SimpleOnGestureListener mListener = new SimpleOnGestureListener() {
		public boolean onDown(MotionEvent e) {
			//Cancel any current fling
            if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			return true;
		}
		
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			//Call a helper method to start the scroller animation
            fling((int)-velocityX/3, (int)-velocityY/3);
			return true;
		}
		
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			//Any view can be scrolled by simply calling its scrollBy() method
			scrollBy((int)distanceX, (int)distanceY);
			return true;
		}
	};
	
	@Override
	public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We use
            // this method to keep the fling animation going through
            // to completion.
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (getChildCount() > 0) {
                View child = getChildAt(0);
                x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
                y = clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), child.getHeight());
                if (x != oldX || y != oldY) {
                    scrollTo(x, y);
                }
            }

            // Keep on drawing until the animation has finished.
            postInvalidate();
        }
	}
	
	//Override scrollTo to do bounds checks on any scrolling request
	@Override
    public void scrollTo(int x, int y) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
            y = clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), child.getHeight());
            if (x != getScrollX() || y != getScrollY()) {
                super.scrollTo(x, y);
            }
        }
    }

    /*
     * Monitor touch events passed down to the children and
     * intercept as soon as it is determined we are dragging
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialX = event.getX();
                mInitialY = event.getY();
                //Feed the down event to the detector so it has
                // context when/if dragging begins
                mDetector.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = event.getX();
                final float y = event.getY();
                final int yDiff = (int) Math.abs(y - mInitialY);
                final int xDiff = (int) Math.abs(x - mInitialX);
                //Verify that either difference is enough to be a drag
                if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
                    //Start capturing events
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    /*
     * Feed all touch events we receive to the detector for
     * processing.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
	
	/*
	 * Utility method to initialize the Scroller and start redrawing
	 */
	public void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            int height = getHeight() - getPaddingBottom() - getPaddingTop();
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int bottom = getChildAt(0).getHeight();
            int right = getChildAt(0).getWidth();

            mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY,
            		0, Math.max(0, right - width),
            		0, Math.max(0, bottom - height));
    
            invalidate();
        }
    }
	
	/*
	 * Utility method to assist in doing bounds checking
	 */
    private int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
            return 0;
        }
        if ((my+n) > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child-my;
        }
        return n;
    }
}
