package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KareokeView;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Amany on 22-Aug-17.
 */

public class NonFocusableNestedScrollView extends NestedScrollView {

    public NonFocusableNestedScrollView(Context context) {
        super(context);
    }

    public NonFocusableNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonFocusableNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }
}