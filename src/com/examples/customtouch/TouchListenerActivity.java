package com.examples.customtouch;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Dave Smith
 * Xcellent Creations, Inc.
 * Date: 9/25/12
 * TouchListenerActivity
 */
public class TouchListenerActivity extends Activity {

    /* Views to display last seen touch event */
    TextView mParentState, mChildState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_listener);

        mParentState = (TextView) findViewById(R.id.text_parent);
        mChildState = (TextView) findViewById(R.id.text_child);

        findViewById(R.id.container).setOnTouchListener(mParentTouchListener);
        findViewById(R.id.button).setOnTouchListener(mChildTouchListener);
        findViewById(R.id.view).setOnTouchListener(mChildTouchListener);
    }

    /*
     * This method is a great place to monitor all touch events that come in to the
     * Activity, because it is always called first.  Here we use it to reset our state
     * display views on each new gesture.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //Clear status fields on a new gesture
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mParentState.setText(null);
            mChildState.setText(null);
        }

        //Continue normally
        return super.dispatchTouchEvent(event);
    }

    private View.OnTouchListener mParentTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mParentState.setText( getNameForEvent(event) );
            //Don't interrupt normal processing
            return false;
        }
    };

    private View.OnTouchListener mChildTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mChildState.setText( getNameForEvent(event) );
            //Don't interrupt normal processing
            return false;
        }
    };

    private String getNameForEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
            default:
                return null;
        }

        return String.format("%s\n%.1f, %.1f", action, event.getX(), event.getY());
    }
}
