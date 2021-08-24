package com.binarywaves.ghaneely.ghannely_application_manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.KaraokeSongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyutils.Utils;

/**
 * Created by Amany on 17-Aug-17.
 */

public class BaseActivity extends DrawerActivity {
    private Applicationmanager mMyApp;


    @Override
    protected void attachBaseContext(Context base) {
        String language = LanguageHelper.getCurrentLanguage(base);
        Log.i("applang+ActiviRunnuing", language);

        super.attachBaseContext(LocaleHelper.onAttach(base, language));
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = (Applicationmanager) this.getApplicationContext();
        new LanguageHelper().initLanguage(this);
        if (Applicationmanager.dialogPoPAds == null) {
            mMyApp.CreateNewDialog();
        }
        String language = LanguageHelper.getCurrentLanguage(this);
        Log.i("applang+ActiviRunnuing", language);
        LocaleHelper.setLocale(this, language);
        Utils.changeStatusBarColor(this,this);

    }

    @Override

    protected void onResume() {
        super.onResume();

        mMyApp.setCurrentActivity(this);
        String language = LanguageHelper.getCurrentLanguage(this);
        Log.i("applang+ActiviRunnuing", language);
        LocaleHelper.setLocale(this, language);
        if (Applicationmanager.dialogPoPAds == null) {
            mMyApp.CreateNewDialog();
        }

        Applicationmanager.activityResumed();
    }


    @Override

    protected void onPause() {
        super.onPause();

        clearReferences();

        if (Applicationmanager.dialogPoPAds != null) {
            if (Applicationmanager.dialogPoPAds.isShowing()) {
                Applicationmanager.dialogPoPAds.dismiss();
            }
            Applicationmanager.dialogPoPAds = null;
        }
        Applicationmanager.activityPaused();

    }

    @Override

    protected void onDestroy() {
        super.onDestroy();

        clearReferences();
        if (Applicationmanager.dialogPoPAds != null) {
            if (Applicationmanager.dialogPoPAds.isShowing()) {
                Applicationmanager.dialogPoPAds.dismiss();
            }
            Applicationmanager.dialogPoPAds = null;
        }

    }

    private void clearReferences() {
        Activity currActivity = Applicationmanager.getCurrentActivity();
        if (this.equals(currActivity)) {
            mMyApp.setCurrentActivity(null);
        }

        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                context);
        if (isServiceRunning2) {

            Intent i = new Intent(context, KaraokeSongService.class);
            context.stopService(i);
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(
                    "ar".equals(LanguageHelper.getCurrentLanguage(getBaseContext())) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }


}