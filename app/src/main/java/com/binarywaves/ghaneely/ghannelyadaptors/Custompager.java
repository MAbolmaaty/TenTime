package com.binarywaves.ghaneely.ghannelyadaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

public class Custompager extends ViewPager {

    private float mStartDragX;
    private OnSwipeOutListener mListener;

    public Custompager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == getAdapter().getCount() - 1) {
            final int action = ev.getAction();
            float x = ev.getX();
            switch (action & MotionEventCompat.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mStartDragX = x;
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (x < mStartDragX) {
                        mListener.onSwipeOutAtEnd();
                    } else {
                        mStartDragX = 0;
                    }
                    break;
            }
        } else {
            mStartDragX = 0;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnSwipeOutListener {
        void onSwipeOutAtEnd();
    }
}