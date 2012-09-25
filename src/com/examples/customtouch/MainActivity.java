/*
 * Copyright (c) 2012 Wireless Designs, LLC
 *
 * See the file license.txt for copying permission.
 */
package com.examples.customtouch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity implements OnItemClickListener {

	private static final String[] ITEMS = {"Pan Example", "Pan Gesture Example",
		"Multi-Touch Example", "Multi-Touch Gesture Example", "Touch Forward Example", "Touch Delegate Example"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ITEMS);
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: //2D Scrolling
                startActivity(new Intent(this, TwoDimensionScrollActivity.class));
                break;
            case 1: //2D GestureDetector Scrolling
                startActivity(new Intent(this, TwoDimensionGestureScrollActivity.class));
                break;
            case 2: //Multi-Touch Image View
                startActivity(new Intent(this, MultitouchActivity.class));
                break;
            case 3: //Multi-Touch Gesture Image View
                startActivity(new Intent(this, MultitouchGestureActivity.class));
                break;
            case 4: //Touch Forwarding
                startActivity(new Intent(this, TouchForwardActivity.class));
                break;
            case 5: //Touch Delegate
                startActivity(new Intent(this, TouchDelegateActivity.class));
                break;
            default:
                break;
        }
    }
}
