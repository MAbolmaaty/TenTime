package com.binarywaves.ghaneely.ghannelyaodioplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;

@SuppressWarnings("ConstantConditions")
public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(
                    Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (!PlayerConstants.SONG_PAUSED) {
                        Controls.pauseControl(context);
                    } else {
                        Controls.playControl(context);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    if (PlayerConstants.SONG_PAUSED) {

                        Controls.playControl(context);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    if (!PlayerConstants.SONG_PAUSED) {
                        Controls.pauseControl(context);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
                    Controls.nextControl(context);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
                    Controls.previousControl(context);
                    break;
            }
        } else {

            if (intent.getAction().equals(SongService.NOTIFY_PLAY)) {
                Controls.playControl(context);
            } else if (intent.getAction().equals(SongService.NOTIFY_PAUSE)) {
                Controls.pauseControl(context);
            } else if (intent.getAction().equals(SongService.NOTIFY_NEXT)) {

                Controls.nextControl(context);
            } else if (intent.getAction().equals(SongService.NOTIFY_DELETE)) {
                Intent intent1 = new Intent(context, HomeActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will clear all the stack
                intent1.putExtra("Exit", true);
                context.startActivity(intent1);
            } else if (intent.getAction().equals(SongService.NOTIFY_PREVIOUS)) {
                Controls.previousControl(context);
            } else if (intent.getAction().equals(SongService.NOTIFY_open)) {

                Intent in = new Intent(context, HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);
            }
        }
    }

// --Commented out by Inspection START (11-Dec-17 10:54 AM):
//    public String ComponentName() {
//        return this.getClass().getName();
//    }
// --Commented out by Inspection STOP (11-Dec-17 10:54 AM)
}
