package com.examples.customtouch;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by Dave Smith
 * Xcellent Creations, Inc.
 * Date: 9/24/12
 * RotateZoomImageView
 */
public class RotateZoomImageView extends ImageView {
    private ScaleGestureDetector mScaleDetector;
    private Matrix mImageMatrix;
    /* Last Rotation Angle */
    private int mLastAngle = 0;
    /* Pivot Point for Transforms */
    private int mPivotX, mPivotY;

    public RotateZoomImageView(Context context) {
        super(context);
        init(context);
    }

    public RotateZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RotateZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, mScaleListener);

        setScaleType(ScaleType.MATRIX);
        mImageMatrix = new Matrix();
    }

    /*
     * Use onSizeChanged() to calculate values based on the view's size.
     * The view has no size during init(), so we must wait for this
     * callback.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            //Shift the image to the center of the view
            int translateX = (w - getDrawable().getIntrinsicWidth()) / 2;
            int translateY = (h - getDrawable().getIntrinsicHeight()) / 2;
            mImageMatrix.setTranslate(translateX, translateY);
            setImageMatrix(mImageMatrix);
            //Get the center point for future scale and rotate transforms
            mPivotX = w / 2;
            mPivotY = h / 2;
        }
    }

    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // ScaleGestureDetector calculates a scale factor based on whether
            // the fingers are moving apart or together
            float scaleFactor = detector.getScaleFactor();
            //Pass that factor to a scale for the image
            mImageMatrix.postScale(scaleFactor, scaleFactor, mPivotX, mPivotY);
            setImageMatrix(mImageMatrix);

            return true;
        }
    };

    /*
     * Operate on two-finger events to rotate the image.
     * This method calculates the change in angle between the
     * pointers and rotates the image accordingly.  As the user
     * rotates their fingers, the image will follow.
     */
    private boolean doRotationEvent(MotionEvent event) {
        //Calculate the angle between the two fingers
        float deltaX = event.getX(0) - event.getX(1);
        float deltaY = event.getY(0) - event.getY(1);
        double radians = Math.atan(deltaY / deltaX);
        //Convert to degrees
        int degrees = (int)(radians * 180 / Math.PI);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Mark the initial angle
                mLastAngle = degrees;
                break;
            case MotionEvent.ACTION_MOVE:
                // ATAN returns a converted value between -90deg and +90deg
                // which creates a point when two fingers are vertical where the
                // angle flips sign.  We handle this case by rotating a small amount
                // (5 degrees) in the direction we were traveling

                if ((degrees - mLastAngle) > 45) {
                    //Going CCW across the boundary
                    mImageMatrix.postRotate(-5, mPivotX, mPivotY);
                } else if ((degrees - mLastAngle) < -45) {
                    //Going CW across the boundary
                    mImageMatrix.postRotate(5, mPivotX, mPivotY);
                } else {
                    //Normal rotation, rotate the difference
                    mImageMatrix.postRotate(degrees - mLastAngle, mPivotX, mPivotY);
                }
                //Post the rotation to the image
                setImageMatrix(mImageMatrix);
                //Save the current angle
                mLastAngle = degrees;
                break;
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // We don't care about this event directly, but we declare
            // interest so we can get later multi-touch events.
            return true;
        }

        switch (event.getPointerCount()) {
            case 3:
                // With three fingers down, zoom the image
                // using the ScaleGestureDetector
                return mScaleDetector.onTouchEvent(event);
            case 2:
                // With two fingers down, rotate the image
                // following the fingers
                return doRotationEvent(event);
            default:
                //Ignore this event
                return super.onTouchEvent(event);
        }
    }
}
