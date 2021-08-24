package com.binarywaves.ghaneely.ghannelyaodioplayer.controls;

import android.content.Context;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;

import java.util.Random;

/**
 * Created by Amany on 02-Jul-17.
 */

public class ControlVideo {
    // --Commented out by Inspection (11-Dec-17 10:53 AM):static String LOG_CLASS = "Controls";

    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));

    }


    public static void nextControl(Context context) {
        boolean isServiceRunning = UtilFunctions.isServiceRunning(VideoService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LISTVideo.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LISTVideo.size() - 1)) {
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

        boolean isServiceRunning = UtilFunctions.isServiceRunning(VideoService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LISTVideo.size() > 0) {
            if (PlayerConstants.SONG_NUMBER > 0) {
                PlayerConstants.SONG_NUMBER--;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = PlayerConstants.SONGS_LISTVideo.size() - 1;
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

        boolean isServiceRunning = UtilFunctions.isServiceRunning(VideoService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LISTVideo.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LISTVideo.size() - 1)) {
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            } else {
                PlayerConstants.SONG_NUMBER = 0;
                PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }
        }
        //	PlayerConstants.SONG_PAUSED = false;
    }

    public static void shuffleControl(Context context) {
        //MainActivity.loading.setVisibility(View.VISIBLE);
        //	MainActivity.progBar.setVisibility(View.VISIBLE);

        boolean isServiceRunning = UtilFunctions.isServiceRunning(VideoService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.SONGS_LISTVideo.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONGS_LISTVideo.size() - 1)) {
                Random rand = new Random();
                PlayerConstants.SONG_NUMBER = rand.nextInt((PlayerConstants.SONGS_LISTVideo.size() - 1) + 1);
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
