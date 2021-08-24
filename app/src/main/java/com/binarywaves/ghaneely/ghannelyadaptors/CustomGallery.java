package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class CustomGallery extends Gallery {

    public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGallery(Context context) {
        super(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float velMax = 2500f;
        float velMin = 1000f;
        float velX = Math.abs(velocityX);
        if (velX > velMax) {
            velX = velMax;
        } else if (velX < velMin) {
            velX = velMin;
        }
        velX -= 600;
        int k = 500000;
        int speed = (int) Math.floor(1f / velX * k);

        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            // Check if scrolling left
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
            // setbackground();
        } else {
            // Otherwise scrolling right
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
            // setbackground();
        }
        onKeyDown(kEvent, null);
        //	setbackground();
        return true;
    }


    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

}