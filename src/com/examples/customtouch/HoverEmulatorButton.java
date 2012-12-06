package com.examples.customtouch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Dave Smith
 * Xcellent Creations, Inc.
 * Date: 12/5/12
 * HoverEmulatorButton
 * Button that transforms single pointer events into hovers and multitouch events into taps
 */
public class HoverEmulatorButton extends Button {

    public HoverEmulatorButton(Context context) {
        super(context);
    }

    public HoverEmulatorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HoverEmulatorButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getPointerCount()) {
            case 1:
                handleSingleTouchEvent(event);
                break;
            default:
                handleMultitouchEvent(event);
                break;
        }

        return true;
    }

    private void handleSingleTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                event.setAction(MotionEvent.ACTION_HOVER_ENTER);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                event.setAction(MotionEvent.ACTION_HOVER_EXIT);
                break;
            case MotionEvent.ACTION_MOVE:
                event.setAction(MotionEvent.ACTION_HOVER_MOVE);
                break;
            default:
                //Ignore unknown events
                return;
        }

        //Forward the converted event to the proper callback
        super.dispatchGenericMotionEvent(event);
    }

    /*
     * Forward all events with multiple fingers to super as if they were single pointer
     * touch events
     */
    private void handleMultitouchEvent(MotionEvent event) {
        //Construct a new event that only contains a single pointer
        MotionEvent newEvent = MotionEvent.obtain(
                event.getDownTime(),
                event.getEventTime(),
                0,
                event.getX(),
                event.getY(),
                event.getMetaState()
        );

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                newEvent.setAction(MotionEvent.ACTION_DOWN);
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                newEvent.setAction(MotionEvent.ACTION_UP);
                break;
            case MotionEvent.ACTION_MOVE:
                //Leave this even unchanged
                newEvent.setAction(MotionEvent.ACTION_MOVE);
                break;
            default:
                //Ignore unknown events
                return;
        }

        //Forward the new event to super
        super.onTouchEvent(newEvent);
    }
}
