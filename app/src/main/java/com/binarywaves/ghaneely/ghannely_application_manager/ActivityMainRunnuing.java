package com.binarywaves.ghaneely.ghannely_application_manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyutils.Utils;

/**
 * Created by Amany on 17-Aug-17.
 */

@SuppressLint("Registered")
public class ActivityMainRunnuing extends FragmentActivity implements Applicationmanager.IMemoryInfo {
    private Applicationmanager mMyApp;
    private BroadcastReceiver mNetworkReceiver;

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
        String language = LanguageHelper.getCurrentLanguage(this);
        Log.i("applang+ActiviRunnuing", language);
        LocaleHelper.setLocale(this, language);
        if (Applicationmanager.dialogPoPAds == null) {
            mMyApp.CreateNewDialog();
        }
        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(NetworkChangeReceiver.class.getName(),
                getApplicationContext());
        if (!isServiceRunning2) {
            Intent i = new Intent(getApplicationContext(), NetworkChangeReceiver.class);
            startService(i);


        }
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
        Applicationmanager.registerMemoryListener(this);

    }
    /*
    @Override
    public void finish() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        }
        else {
            super.finish();
        }
    }
*/


//    @Override
//    protected void onRestart() {
//        mMyApp.setCurrentActivity(this);
//        if(Applicationmanager.dialogPoPAds !=null){
//            if(Applicationmanager.dialogPoPAds.isShowing()){
//                Applicationmanager.dialogPoPAds.dismiss();
//            }
//            Applicationmanager.dialogPoPAds =null;
//
//            mMyApp.CreateNewDialog();
//        }
//        super.onRestart();
//    }

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
        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(NetworkChangeReceiver.class.getName(),
                getApplicationContext());
        if (isServiceRunning2) {
            Intent i = new Intent(getApplicationContext(), NetworkChangeReceiver.class);
            stopService(i);


        }
    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        clearReferences();
//        if (Applicationmanager.dialogPoPAds != null) {
//            if (Applicationmanager.dialogPoPAds.isShowing()) {
//                Applicationmanager.dialogPoPAds.dismiss();
//            }
//            Applicationmanager.dialogPoPAds = null;
//        }
//        boolean isServiceRunning7 = UtilFunctions.isServiceRunning(NetworkChangeReceiver.class.getName(),
//                getApplicationContext());
//        if (isServiceRunning7) {
//            Intent i = new Intent(getApplicationContext(), NetworkChangeReceiver.class);
//            stopService(i);
//
//
//        }
//        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
//                getApplicationContext());
//
//
//        boolean isServiceRunning6 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
//                getApplicationContext());
//
//
//        boolean isServiceRunning7 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
//                getApplicationContext());
//        boolean isServiceRunning1 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
//                getApplicationContext());
//        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(OfflineVideoService.class.getName(),
//                getApplicationContext());
//
//        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
//                getApplicationContext());
//
//        if (isServiceRunning1) {
//            Intent i = new Intent(getApplicationContext(), OFFLINEService.class);
//            stopService(i);
//        }
//
//        if (isServiceRunning2) {
//            Intent i = new Intent(getApplicationContext(), OfflineVideoService.class);
//            stopService(i);
//        }
//
//        if (isServiceRunning4) {
//            Intent i = new Intent(getApplicationContext(), KaraokeSongService.class);
//            stopService(i);
//        }
//
//
//        if (isServiceRunning7) {
//            StopServicefromnotification(VideoService.class);
//        }
//
//
//        if (isServiceRunning3) {
//            StopServicefromnotification(SongService.class);
//        }
//
//        if (isServiceRunning6) {
//            StopServicefromnotification(SongServiceradio.class);
//        }
//
//        android.os.Process.killProcess(android.os.Process.myPid());
//
//        System.exit(0);
//    }

    private void StopServicefromnotification(Class name) {
        Intent i = new Intent(getApplicationContext(), name);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        getApplicationContext().stopService(i);
    }
    private void clearReferences() {
        Activity currActivity = Applicationmanager.getCurrentActivity();
        if (this.equals(currActivity))
            mMyApp.setCurrentActivity(null);
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

    @Override
    public void goodTimeToReleaseMemory() {

    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            Applicationmanager.unregisterMemoryListener(this);
        } catch (Exception e) {
            e.getMessage();
        }
    }


}