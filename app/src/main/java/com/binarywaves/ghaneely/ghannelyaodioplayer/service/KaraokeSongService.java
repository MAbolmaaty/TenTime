package com.binarywaves.ghaneely.ghannelyaodioplayer.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.KaraokeTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.SavedTabsFragment;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlKaraoke;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelyresponse.KarokeSavedObject;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.Timer;

/**
 * Created by Amany on 05-Sep-17.
 */

public class KaraokeSongService extends Service implements AudioManager.OnAudioFocusChangeListener, Player.EventListener {

    private static final String TAG = "TELEPHONESERVICE";
    public static Bitmap bitmap;
    public static Notification notification;
    private static Timer timer;
    // testing audiotrack
    private static ExoPlayer mp;
    private static boolean isReady;
    ////
    String LOG_CLASS = "SongService";
    ProgressDialog pDialog;
    int amany;
    private AudioManager audioManager;
    private FileCache fileCache;
    /**
     * Play song, Update Lockscreen fields
     *
     * @param songPath
     * @param data
     */
    private KarokeSavedObject data1;
    private boolean isPausedInCall = false;
    private boolean intialStage = true;

    public static Activity getActivity() {

        return Applicationmanager.getCurrentActivity();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl

        mp = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        mp.removeListener(this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        fileCache = new FileCache(getBaseContext());
        timer = new Timer();

        super.onCreate();
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }
    /**
     * Send message from timer
     *
     * @author jonty.ankit
     */


    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            KarokeSavedObject data = PlayerConstants.Karaoke_LIST.get(PlayerConstants.SONG_NUMBER);

            final String songPath = ServerConfig.KARAOKE_Saved_PATH + data.getKaraokePath();
            playSong(songPath, data);

            PlayerConstants.SONG_CHANGE_HANDLER = new Handler(msg -> {
                KarokeSavedObject data2 = PlayerConstants.Karaoke_LIST.get(PlayerConstants.SONG_NUMBER);
                final String songPath1 = ServerConfig.KARAOKE_Saved_PATH + data2.getKaraokePath();

                try {
                    playSong(songPath1, data2);
                    SavedTabsFragment.changeUI();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            });

            PlayerConstants.PLAY_PAUSE_HANDLER = new Handler(msg -> {
                String message = (String) msg.obj;
                if (mp == null)
                    return false;
                if (message.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.play))) {
                    PlayerConstants.SONG_PAUSED = false;

                    mp.setPlayWhenReady(true);
                    intialStage = false;
                } else if (message.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.pause))) {
                    PlayerConstants.SONG_PAUSED = true;

                    mp.setPlayWhenReady(false);
                    intialStage = false;
                }

                try {
                    SavedTabsFragment.changeUI();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                Log.d("TAG", "TAG Pressed: " + message);
                return false;
            });

        } catch (

                Exception e)

        {
            e.printStackTrace();
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                // String stateString = "N/A";
                Log.v(TAG, "Starting CallStateChange");
                switch (state) {

                    case TelephonyManager.CALL_STATE_OFFHOOK:

                        if (mp != null) {
                            mp.setPlayWhenReady(false);
                            isPausedInCall = true;
                        }
                        if (!isReady) {
                            if (mp != null) {
                                mp.setPlayWhenReady(false);
                                isPausedInCall = true;
                            }
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:// incoming call
                        if (mp != null) {
                            mp.setPlayWhenReady(false);
                            isPausedInCall = true;
                        }
                        if (!isReady)
                            if (mp != null) {
                                mp.setPlayWhenReady(false);
                                isPausedInCall = true;
                            }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:// call finish
                        // Phone idle. Start playing.
                        if (mp != null && PlayerConstants.SONG_PAUSED && isPausedInCall) {
                            mp.setPlayWhenReady(false);

                        }
                        if (mp != null && !PlayerConstants.SONG_PAUSED && isPausedInCall) {
                            isPausedInCall = false;
                            mp.setPlayWhenReady(true);

                        }
                        break;
                }
            }

        };
        // Register the listener with the telephony manager
        if (telephonyManager != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        return START_STICKY;

    }

    /**
     * Notification Custom Bignotification is available from API 16
     * <p>
     * <p>
     * /**
     * Notification click listeners
     */
    private void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
        }
    }

    @Override
    public void onDestroy() {
        if (mp != null) {
            mp.release();
            mp.removeListener(this);

            mp = null;
        }
        audioManager.abandonAudioFocus(this);

        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void playSong(String songPath, final KarokeSavedObject data) {
        Remove_handlers();
        mp.removeListener(this);
        data1 = data;
        mp.addListener(this);
        String str;
        str = songPath.replaceAll(" ", "%20");
        Log.d("testing", str);
        try {
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ExoPlayerDemo");
            MediaSource audio = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(str));
            mp.prepare(audio);
            mp.setPlayWhenReady(true);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mp != null) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS: {
                    if (mp.getPlayWhenReady()) {
                        ControlKaraoke.pauseControl(getApplicationContext());

                        SavedTabsFragment.changeUI();

                    }
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                    ControlKaraoke.pauseControl(getApplicationContext());

                    SavedTabsFragment.changeUI();
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                    if (mp != null) {
                    }
                    break;
                }
                case AudioManager.AUDIOFOCUS_GAIN: {
                    if (mp != null) {
                        if (mp != null && !mp.getPlayWhenReady()) {
                            mp.setPlayWhenReady(false);

                        }
                        if (mp != null && mp.getPlayWhenReady()) {
                            isPausedInCall = false;
                            mp.setPlayWhenReady(true);



                        }
                    }
                    break;
                }
            }
        }
    }




    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
            if (mp.getPlayWhenReady()) {
                isReady = true;
                mp.setPlayWhenReady(true);
                if(SavedTabsFragment.progBar!=null)
                SavedTabsFragment.progBar.setVisibility(View.GONE);
                if(SavedTabsFragment.audioRelative!=null)
                    SavedTabsFragment.audioRelative.setVisibility(View.VISIBLE);

            }
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                Toast.makeText(getApplicationContext(), error.getSourceException().getMessage(), Toast.LENGTH_LONG).show();
                Handle_error();
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                Toast.makeText(getApplicationContext(), error.getSourceException().getMessage(), Toast.LENGTH_LONG).show();
                Handle_error();

                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                Toast.makeText(getApplicationContext(), error.getSourceException().getMessage(), Toast.LENGTH_LONG).show();
                Handle_error();


                break;
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    private void Handle_error() {
        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning4) {
            Intent i = new Intent(getApplicationContext(), KaraokeSongService.class);

            getApplicationContext().stopService(i);
            Remove_handlers();

        }

        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
            if (mpackagename
                    .equalsIgnoreCase("KareokePackage.KaraokeTabs.KaraokeTabActivity")) {

                switch (KaraokeTabActivity.selectedfragment) {


                    case 1:
                        SavedTabsFragment.progBar.setVisibility(View.GONE);

                        SavedTabsFragment.audioRelative.setVisibility(View.GONE);
                        SavedTabsFragment.audioRelative.setEnabled(true);
                        break;
                }}}

                }


    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}

