package com.binarywaves.ghaneely.ghannelyaodioplayer.controls;

import android.content.Context;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;

import java.util.Random;

public class Controls {
    // --Commented out by Inspection (11-Dec-17 10:53 AM):static String LOG_CLASS = "Controls";

    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));

    }


    public static void nextControl(Context context) {


        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LIST.size() - 1)) {
                PlayerConstants.SONG_NUMBER++;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = 0;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
        PlayerConstants.SONG_CHANGED = false;
        PlayerConstants.SONGnext = true;


    }

    public static void previousControl(Context context) {

        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER > 0) {
                PlayerConstants.SONG_NUMBER--;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = PlayerConstants.SONGS_LIST.size() - 1;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        PlayerConstants.SONG_PAUSED = false;
        PlayerConstants.SONG_CHANGED = false;
        PlayerConstants.SONGnext = true;


    }

    public static void repeatControl(Context context) {
        //MainActivity.loading.setVisibility(View.VISIBLE);
        //	MainActivity.progBar.setVisibility(View.VISIBLE);

        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LIST.size() - 1)) {
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = 0;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        //	PlayerConstants.SONG_PAUSED = false;
    }

    public static void shuffleControl(Context context) {

        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LIST.size() - 1)) {
                Random rand = new Random();
                PlayerConstants.SONG_NUMBER = rand.nextInt((PlayerConstants.SONGS_LIST.size() - 1) + 1);
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = 0;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        //	PlayerConstants.SONG_PAUSED = false;
    }


    private static void sendMessage(String message) {
        try {

            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
