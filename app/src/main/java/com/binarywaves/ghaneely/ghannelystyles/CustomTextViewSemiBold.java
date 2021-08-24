package com.binarywaves.ghaneely.ghannelystyles;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;

/**
 * Created by Amany on 09-Aug-17.
 */

public class CustomTextViewSemiBold extends android.support.v7.widget.AppCompatTextView {

    public CustomTextViewSemiBold(Context context) {
        super(context);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public CustomTextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public CustomTextViewSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    private void applyCustomFont(Context context) {


        Typeface customFont = null;
        String language = LanguageHelper.getCurrentLanguage(context);

        if (language.equalsIgnoreCase("ar"))
            customFont = Typeface.createFromAsset(context.getAssets(), "fonts/GE_SS_Two_Medium.otf");

        else
            customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roundo-SemiBold.otf");


        setTypeface(customFont);
    }
}
