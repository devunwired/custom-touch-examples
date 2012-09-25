package com.examples.customtouch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dave Smith
 * Xcellent Creations, Inc.
 * Date: 9/24/12
 * MultitouchActivity
 */
public class MultitouchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RotateZoomImageView iv = new RotateZoomImageView(this);

        Bitmap image;
        try {
            InputStream in = getAssets().open("android.jpg");
            image = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            image = null;
        }
        iv.setImageBitmap(image);

        setContentView(iv);
    }
}
