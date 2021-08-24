package com.binarywaves.ghaneely.ghannelymodels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.binarywaves.ghaneely.ghannely_application_manager.LocaleHelper;

public class LanguageHelper {

    public static String getCurrentLanguage(Context context) {
        String value = context.getSharedPreferences(SharedPrefHelper.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getString(SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, "en");
        Log.v("languageactive", value);
        return value;
    }

    public void initLanguage(Context cont) {
        String language = new SharedPrefHelper().getSharedString(cont, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY);
        Log.v("lang", language);

        LocaleHelper.setLocale(cont.getApplicationContext(), language);

    }

    /*
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        private void forceRTLIfSupported(Activity context, boolean replace)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && replace)
                context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !replace)
                context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

    */
    public void changeLanguage(Activity context, String newLanguage) {
        SharedPrefHelper.setSharedString(context, SharedPrefHelper.SHARED_PREFERENCE_LANGUAGE_KEY, newLanguage);
        LocaleHelper.setLocale(context, newLanguage);

    }
}
