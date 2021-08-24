package com.binarywaves.ghaneely.ghannely_application_manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amany on 16-Aug-17.
 */


@TargetApi(Build.VERSION_CODES.O)
public class Applicationmanager extends Application {
    public static Dialog dialogPoPAds;
    private static RelativeLayout progBar;
    private static ProgressWheel pb1;
    private static boolean activityVisible; // Variable that will check the
    private static Dialog progressdialog;
    private static Activity mCurrentActivity = null;
    private static Context mContextt;
    public static final String Channel_id = "exampleServiceChannel";

    //This my introduce OutOfMemoryException if you don't handle register and removal quiet well, better to replace it with weak reference
    private static List<IMemoryInfo> memInfoList = new ArrayList<>();
    // current activity state


    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused

    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        Applicationmanager.mCurrentActivity = mCurrentActivity;

    }

    public static void Createprogressloading() {
        progressdialog = new Dialog(getCurrentActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        progressdialog.setContentView(R.layout.progressbarwhell);
        progBar = progressdialog.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        pb1.spin();

        progressdialog.show();


    }

    public static void Dismissprogressloading() {
        if (progressdialog != null && progressdialog.isShowing()) {

            progressdialog.dismiss();
        }
    }

    public static Context getContext() {
        return mContextt;
    }

    public static void setContext(Context mContext) {
        mContextt = mContext;
    }

    /**
     * @param implementor interested listening in memory events
     */
    public static void registerMemoryListener(IMemoryInfo implementor) {
        memInfoList.add(implementor);
    }

    public static void unregisterMemoryListener(IMemoryInfo implementor) {
        memInfoList.remove(implementor);
    }

    @Override
    public void attachBaseContext(Context base) {
        String language = LanguageHelper.getCurrentLanguage(base);
        super.attachBaseContext(LocaleHelper.onAttach(base, language));
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContextt = this;
        new LanguageHelper().initLanguage(this);

        if (dialogPoPAds == null) {
            CreateNewDialog();
        }
        createNotificationChannel();
    }


    public static Dialog CreateNewDialog() {

        if (getCurrentActivity() != null) {
            dialogPoPAds = new Dialog(getCurrentActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            dialogPoPAds.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return dialogPoPAds;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//don't compare with == as intermediate stages also can be reported, always better to check >= or <=
        if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            try {
                // Activity at the front will get earliest than activity at the
                // back
                for (int i = memInfoList.size() - 1; i >= 0; i--) {
                    try {
                        memInfoList.get(i).goodTimeToReleaseMemory();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public interface IMemoryInfo {
        void goodTimeToReleaseMemory();

    }

    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel servicechannel = new NotificationChannel(Channel_id, "Example Service Channel",
                        NotificationManager.IMPORTANCE_DEFAULT);


                NotificationManager manger = getSystemService(NotificationManager.class);
                manger.createNotificationChannel(servicechannel);
            }
        } catch (Exception e) {
            Log.d("NotificationChannelssss", "" + e);
        }

    }

}