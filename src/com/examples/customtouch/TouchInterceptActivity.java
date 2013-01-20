package com.examples.customtouch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by Dave Smith
 * Xcellent Creations, Inc.
 * Date: 1/19/13
 * TouchInterceptActivity
 */
public class TouchInterceptActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ScrollView mScrollView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_intercept);

        mViewPager = (ViewPager) findViewById(R.id.pager_header);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);

        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(new HeaderAdapter(this));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) {
        //While the ViewPager is scrolling, disable the ScrollView touch intercept
        // so it cannot take over and try to vertical scroll
        boolean isScrolling = state != ViewPager.SCROLL_STATE_IDLE;
        mScrollView.requestDisallowInterceptTouchEvent(isScrolling);
    }

    /*
     * Simple PagerAdapter that just draws a small group of colored views
     */
    private static class HeaderAdapter extends PagerAdapter {
        private static final int[] COLORS = {0xFFAAAA00, 0xFFAA00AA, 0xFF00AAAA, 0xFFAAAAAA};

        private Context mContext;

        public HeaderAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = new View(mContext);
            v.setBackgroundColor(COLORS[position]);
            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return COLORS.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
}