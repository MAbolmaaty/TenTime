package com.binarywaves.ghaneely.ghannelystyles;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;

/**
 * Created by Amany on 20-Feb-17.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            applyCustomFont(context);//whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    private void applyCustomFont(Context context) {


        Typeface customFont = null;
        String language = LanguageHelper.getCurrentLanguage(context);

        if (language.equalsIgnoreCase("ar"))
            customFont = Typeface.createFromAsset(context.getAssets(), "fonts/GE_SS_Text_Light.otf");

        else
            customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roundo-Regular.otf");


        setTypeface(customFont);
    }
}