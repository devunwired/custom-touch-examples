package com.examples.customtouch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dave Smith
 * Xcellent Creations, Inc.
 * Date: 12/5/12
 * HoverEmulatorActivity
 * Transform touch events into hover events
 */
public class HoverEmulatorActivity extends Activity implements View.OnHoverListener {

    public static final String TAG = "HoverEmulatorActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hover_emulator);

        findViewById(R.id.button_hover).setOnHoverListener(this);
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        Log.i(TAG, "Hover Event "+getHoverName(event));
        return false;
    }

    private String getHoverName(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
                return "HOVER_ENTER";
            case MotionEvent.ACTION_HOVER_MOVE:
                return "HOVER_MOVE";
            case MotionEvent.ACTION_HOVER_EXIT:
                return "HOVER_EXIT";
            default:
                return "";
        }
    }
}