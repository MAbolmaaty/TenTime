package com.binarywaves.ghaneely.ghannelyaodioplayer.controls;

import android.content.Context;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;

/**
 * Created by Amany on 05-Sep-17.
 */

public class ControlKaraoke {
    // --Commented out by Inspection (11-Dec-17 10:53 AM):static String LOG_CLASS = "Controls";

    public static void playControl(Context context) {
        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));

    }


    private static void sendMessage(String message) {
        try {
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
