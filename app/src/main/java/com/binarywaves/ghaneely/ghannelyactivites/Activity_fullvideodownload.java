package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OfflineVideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.facebook.FacebookSdk;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by Amany on 01-Nov-17.
 */

public class Activity_fullvideodownload extends ActivityMainRunnuing {
    public static PlayerView surfaceView;
    public static RelativeLayout progBar;
    private static ProgressWheel pb1;
    static Activity activity;
    private static Context context;
    private static Application activity1;
    private int pos;
    private boolean pause_stop = false;

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    private static void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, activity1);
        tracklisiten.StopService();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_downloadvideo);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.setFinishOnTouchOutside(false);
        context = getApplicationContext();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("position")) {
                pos = extras.getInt("position");
            }
        }

        InitializeUi();

    }

    private void InitializeUi() {
        activity1 = getApplication();

        context = getApplicationContext();
        Applicationmanager.setContext(Activity_fullvideodownload.this);

        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        surfaceView = findViewById(R.id.Gallery01);

        PlaySongFromList(pos);

    }

    private void PlaySongFromList(int position) {
        Log.e("list clicked ", "here");
        startwhellprogress();
        PlayerConstants.SONG_PAUSED = false;
        PlayerConstants.SONG_NUMBER = position;

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(OfflineVideoService.class.getName(),
                getApplicationContext());
        if (!isServiceRunning3) {

            Intent i = new Intent(getApplicationContext(), OfflineVideoService.class);
            startService(i);

            // progBar.setVisibility(View.GONE);

        } else {
            PlayerConstants.SONG_CHANGE_HANDLER
                    .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
        }
        changeUi();


        Log.d("TAG", "TAG Tapped INOUT(OUT)");
    }

    public static void changeUi() {
        if(OfflineVideoService.mp!=null){

            surfaceView.setPlayer(OfflineVideoService.mp);
        surfaceView.setControllerAutoShow(true);
        surfaceView.setUseController(true);

    }}

    @Override
    public void onBackPressed() {
        boolean isServiceRunning7 = UtilFunctions.isServiceRunning(OfflineVideoService.class.getName(),
                getApplicationContext());

        this.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (isServiceRunning7) {
            StopService();
        }

        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            OfflineVideoService.releasePlayer();
            pause_stop = true;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        boolean isServiceRunning7 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                getApplicationContext());
        if (!isServiceRunning7&&Util.SDK_INT > 23) {
            OfflineVideoService.releasePlayer();
            pause_stop = true;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("restart", "restart");
        Applicationmanager.setContext(Activity_fullvideodownload.this);

        if (pause_stop) {
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Applicationmanager.setContext(Activity_fullvideodownload.this);

        if (Util.SDK_INT <= 23 && pause_stop) {
            OfflineVideoService.initializePlayer();
        }
    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }

}