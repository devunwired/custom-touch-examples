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

	private static final String[] ITEMS = {"Custom Touch Example", "Touch Forwarding Example",
		"GestureDetector Example", "Pan/Zoom Gesture Example", "Touch Delegate Example"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ITEMS);
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch(position) {
		case 0: //Custom Touch
			startActivity(new Intent(this, CustomTouchActivity.class));
			break;
		case 1: //Touch Forward
			startActivity(new Intent(this, TouchForwardActivity.class));
			break;
		case 2: //GestureDetector
			startActivity(new Intent(this, GestureDetectActivity.class));
			break;
		case 3: //2D Scrolling
			startActivity(new Intent(this, TwoDimensionScrollActivity.class));
			break;
		case 4: //Touch Delegate
			startActivity(new Intent(this, TouchDelegateActivity.class));
			break;
		default:
			break;
		}
	}
}
