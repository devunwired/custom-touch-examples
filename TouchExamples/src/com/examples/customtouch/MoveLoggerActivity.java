package com.examples.customtouch;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by Dave Smith
 * Double Encore, Inc.
 * Date: 9/24/12
 * MoveLoggerActivity
 */
public class MoveLoggerActivity extends Activity implements View.OnTouchListener {

    public static final String TAG = "MoveLoggerActivity";

    /* Slop constant for this device */
    private int mTouchSlop;
    /* Initial touch point */
    private Point mInitialTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_logger);

        findViewById(R.id.view_logall).setOnTouchListener(this);
        findViewById(R.id.view_logslop).setOnTouchListener(this);

        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        mInitialTouch = new Point();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            mInitialTouch.set((int)event.getX(), (int)event.getY());
            //Must declare interest to get more events
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            switch (v.getId()) {
                case R.id.view_logall:
                    Log.i(TAG, String.format("Top Move: %.1f,%.1f", event.getX(), event.getY()));
                    break;
                case R.id.view_logslop:
                    if ( Math.abs(event.getX() - mInitialTouch.x) > mTouchSlop
                            || Math.abs(event.getY() - mInitialTouch.y) > mTouchSlop ) {
                        Log.i(TAG, String.format("Bottom Move: %.1f,%.1f", event.getX(), event.getY()));
                    }
                    break;
                default:
                    break;
            }
        }
        //Don't interefere when not necessary
        return false;
    }
}
