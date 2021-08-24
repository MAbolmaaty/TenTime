package com.binarywaves.ghaneely.ghannelystyles;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;

public class SplashTextBold extends android.support.v7.widget.AppCompatTextView {

    public SplashTextBold(Context context) {
        super(context);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public SplashTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public SplashTextBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    private void applyCustomFont(Context context) {


        Typeface customFont = null;
        String language = LanguageHelper.getCurrentLanguage(context);

        if (language.equalsIgnoreCase("ar"))
            customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

        else
            customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");


        setTypeface(customFont);
    }
}