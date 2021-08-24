package com.binarywaves.ghaneely.customBlock;

import android.widget.FrameLayout;

import com.joanfuentes.hintcase.ContentHolder;

/**
 * Created by Amany on 16-Jul-17.
 */
abstract class HintContentHolder extends ContentHolder {
    public FrameLayout.LayoutParams getParentLayoutParams(int width, int height, int gravity) {
        return new FrameLayout.LayoutParams(width, height, gravity);
    }

    FrameLayout.LayoutParams getParentLayoutParams(int width, int height, int gravity,
                                                   int marginLeft, int marginTop,
                                                   int marginRight, int marginBottom) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height, gravity);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        return layoutParams;
    }

}
