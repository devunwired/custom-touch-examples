package com.examples.customtouch;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by Dave Smith
 * Xcellent Creations, Inc.
 * Date: 9/5/12
 * PanZoomImageView
 */
public class PanZoomImageView extends ImageView {

    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;
    private Scroller mScroller;

    private Matrix mScaleMatrix;

    public PanZoomImageView(Context context) {
        super(context);
        init(context);
    }

    public PanZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PanZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mDetector = new GestureDetector(context, mListener);
        mScaleDetector = new ScaleGestureDetector(context, mScaleListener);
        mScroller = new Scroller(context);

        setScaleType(ScaleType.MATRIX);
        mScaleMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            Rect bounds = getDrawable().getBounds();
            int contentWidth = bounds.right - bounds.left;
            int contentHeight = bounds.top - bounds.bottom;

            mScaleMatrix.setTranslate((w - contentWidth)/2, (h - contentHeight)/2);
            setImageMatrix(mScaleMatrix);
        }
    }

    /*
    * SimpleOnScaleGestureListener handles the zoom events using its onScale() method
    */
    private float mZoomScale = 1.0f;
    private float mPivotX, mPivotY;
    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //getScaleFactor() returns a multiplier based on whether the fingers moved
            // closer or farther since the last event.  This value is usually between 0.9 and 1.1
            mZoomScale = detector.getScaleFactor();
            //Don't allow zoom level to go below original size
//            mZoomScale = Math.max(mZoomScale, 1.0f);
            //Force a redraw with the new scale factor
            mScaleMatrix.postScale(mZoomScale, mZoomScale, mPivotX, mPivotY);
            setImageMatrix(mScaleMatrix);

            return true;
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mPivotX = detector.getFocusX();
            mPivotY = detector.getFocusY();
            return true;
        }
    };

    private GestureDetector.SimpleOnGestureListener mListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            //Cancel any current fling
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Call a helper method to start the scroller animation
            fling((int) -velocityX, (int) -velocityY);
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //Any view can be scrolled by simply calling its scrollBy() method
            scrollBy((int) distanceX, (int) distanceY);
            return true;
        }
    };

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), getScaledContentWidth());
            y = clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), getScaledContentHeight());
            if (x != oldX || y != oldY) {
                scrollTo(x, y);
            }

            // Keep on drawing until the animation has finished.
            postInvalidate();
        }
    }

    //Override scrollTo to do bounds checks on any scrolling request
    @Override
    public void scrollTo(int x, int y) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        x = clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), getScaledContentWidth());
        y = clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), getScaledContentHeight());
        if (x != getScrollX() || y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    private RectF mScaledRect = new RectF();
    private RectF mSourceRect = new RectF();
    private int getScaledContentWidth() {
        Rect bounds = getDrawable().getBounds();
        mSourceRect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        mScaleMatrix.mapRect(mScaledRect, mSourceRect);

        return (int)(mScaledRect.right - mScaledRect.left);
    }

    private int getScaledContentHeight() {
        Rect bounds = getDrawable().getBounds();
        mSourceRect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        mScaleMatrix.mapRect(mScaledRect, mSourceRect);

        return (int)(mScaledRect.bottom - mScaledRect.top);
    }

    /*
      * Utility method to initialize the Scroller and start redrawing
      */
    public void fling(int velocityX, int velocityY) {
        int height = getHeight() - getPaddingBottom() - getPaddingTop();
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int bottom = getDrawable().getBounds().bottom;
        int right = getDrawable().getBounds().right;

        mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY,
                0, Math.max(0, right - width),
                0, Math.max(0, bottom - height));

        invalidate();
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
        if ((my + n) > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child - my;
        }
        return n;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getPointerCount()) {
            case 1:
                return mDetector.onTouchEvent(event);
            default:
                return mScaleDetector.onTouchEvent(event);
        }
    }
}
