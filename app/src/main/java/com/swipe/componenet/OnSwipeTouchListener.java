package com.swipe.componenet;

import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector = new GestureDetector(
            new GestureListener());

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(event);

    }

    private final class GestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return super.onFling(e1, e2, velocityX, velocityY);
        }


    }
}
