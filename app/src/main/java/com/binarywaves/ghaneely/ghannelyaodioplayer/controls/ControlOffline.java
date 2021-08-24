package com.binarywaves.ghaneely.ghannelyaodioplayer.controls;

import android.content.Context;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OFFLINEService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;

/**
 * Created by Amany on 12-Nov-17.
 */

public class ControlOffline {

    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));

    }


    public static void nextControl(Context context) {


        boolean isServiceRunning = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.OfflineSONGS_LIST.size() > 0) {
            if (PlayerConstants.SONG_NUMBER < (PlayerConstants.OfflineSONGS_LIST.size() - 1)) {
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

        boolean isServiceRunning = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(), context);
        if (!isServiceRunning)
            return;
        if (PlayerConstants.OfflineSONGS_LIST.size() > 0) {
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


    private static void sendMessage(String message) {
        try {

            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
