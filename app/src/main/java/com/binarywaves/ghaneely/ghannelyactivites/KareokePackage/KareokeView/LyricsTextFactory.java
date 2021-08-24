package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KareokeView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelystyles.CustomTextView;

/**
 * Created by Amany on 22-Aug-17.
 */

class LyricsTextFactory implements ViewSwitcher.ViewFactory {

    private final Context mContext;

    public LyricsTextFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public View makeView() {
        CustomTextView t = new CustomTextView(mContext);
        t.setGravity(Gravity.CENTER_HORIZONTAL);

        TypedValue colorValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.textColorPrimary, colorValue, true);
        t.setTextColor(Color.BLACK);
        t.setLineSpacing(mContext.getResources().getDimensionPixelSize(R.dimen.line_spacing), 1);
        setSelectable(t);
        t.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.txt_size));
        return t;
    }

    @SuppressLint("newAPI")
    private void setSelectable(TextView t) {
        t.setTextIsSelectable(true);
    }


}