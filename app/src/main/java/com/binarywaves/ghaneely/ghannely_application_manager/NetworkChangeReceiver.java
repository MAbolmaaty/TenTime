package com.binarywaves.ghaneely.ghannely_application_manager;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.Button;

import com.binarywaves.ghaneely.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Amany on 22-Nov-17.
 */

public class NetworkChangeReceiver extends Service {

    private static Timer timer;
    public static boolean start = true;
    //15 min
    private static long UPDATE_INTERVAL = 20000;
    private static Dialog dialogoffline;

    private static void ShowOfflineconnection_Dialog(Context context) {
        if (context == null) {
            context = Applicationmanager.getContext();
        }
        if (context != null) {
            dialogoffline = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

            dialogoffline.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogoffline.setCanceledOnTouchOutside(false);
            //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

            dialogoffline.setContentView(R.layout.offlinedownload_popup);

            Button retry = dialogoffline.findViewById(R.id.change);

            retry.setOnClickListener(v -> {
                dialogoffline.dismiss();

            });


            Button cancel = dialogoffline.findViewById(R.id.cancel);
            // if button is clicked, close the custom dialog
            cancel.setOnClickListener(v -> dialogoffline.dismiss());

            // if button is clicked, close the custom dialog
            if (getActivity()!=null&&dialogoffline!=null&&!dialogoffline.isShowing())
                dialogoffline.show();
        }
    }
    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dialogoffline = new Dialog(getApplicationContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


    }
    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub
        if(timer!=null) {
            timer.cancel();
        }
        timer=null;
        return super.stopService(name);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Google", "Service Started");

        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(

                new TimerTask() {

                    public void run() {
                        new Handler(Looper.getMainLooper()).post(() -> checkPopup());


                    }
                }, 30000, UPDATE_INTERVAL);
        return START_STICKY;

    }
    @Override
    public boolean onUnbind(Intent intent) {
        timer =null;
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkPopup() {
        boolean isVisible = Applicationmanager.isActivityVisible();
        try {

            if (isOnline(getApplicationContext())) {
                //     DrawerActivity.ShowOfflineconnection_Dialog(true);
                Log.e("keshav", "Online Connect Intenet ");
            } else {
                if (dialogoffline != null && !dialogoffline.isShowing()) {
                    ShowOfflineconnection_Dialog(getActivity());
                    Log.e("keshav", "Conectivity Failure !!! ");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {

        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                assert connectivityManager != null;
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    @Override
    public void onDestroy() {
        if(timer!=null) {
            timer.cancel();
        }
        timer =null;

        super.onDestroy();
    }
}