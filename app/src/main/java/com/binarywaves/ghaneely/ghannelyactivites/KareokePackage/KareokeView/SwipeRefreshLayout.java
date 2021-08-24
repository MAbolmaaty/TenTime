package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KareokeView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Amany on 22-Aug-17.
 */

public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {
    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(23)
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dxConsumed, null);
        if (dyUnconsumed < 0) {
            //CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getParent().getParent().getParent();
            //   coordinatorLayout.onNestedScroll(coordinatorLayout.findViewById(R.id.toolbar_layout),
            //        dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
            //  if (coordinatorLayout.getScrollY() == 0 && !isRefreshing())
            super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed / 2);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !isRefreshing() && super.onTouchEvent(ev);
    }
}