package com.binarywaves.ghaneely.ghannelystyles;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Amany on 15-Jun-17.
 */

public class CustomarabicTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomarabicTextView(Context context) {
        super(context);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public CustomarabicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            applyCustomFont(context);
            //whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public CustomarabicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/GE_SS_Text_Light.otf");
        setTypeface(customFont);
    }
}
