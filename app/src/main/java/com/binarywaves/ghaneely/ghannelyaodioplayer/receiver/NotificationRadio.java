package com.binarywaves.ghaneely.ghannelyaodioplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongServiceradio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.RadioConstant;

public class NotificationRadio extends BroadcastReceiver {

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
                    if (!RadioConstant.SONG_PAUSED) {
                        ControlRadio.pauseControl(context);
                    } else {
                        ControlRadio.playControl(context);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
                    break;
            }
        } else {
            if (intent.getAction().equals(SongServiceradio.NOTIFY_PLAY)) {
                ControlRadio.playControl(context);
            } else if (intent.getAction().equals(SongServiceradio.NOTIFY_PAUSE)) {
                ControlRadio.pauseControl(context);
            } else if (intent.getAction()
                    .equals(SongServiceradio.NOTIFY_DELETE)) {
                Intent intent1 = new Intent(context, HomeActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will clear all the stack
                intent1.putExtra("Exit", true);
                context.startActivity(intent1);
            } else if (intent.getAction().equals(SongServiceradio.NOTIFY_open)) {
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
