package com.binarywaves.ghaneely.ghannelyaodioplayer.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.EncryptedFileDataSourceFactory;
import com.Ghannely_Encrypt_Decrypt_Tracks.Encryption_DecryptionAudio;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannelyactivites.Activity_fullvideodownload;
import com.binarywaves.ghaneely.ghannelyactivites.DownloadActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.receiver.NotificationBroadcast;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelyresponse.VideoDownloadObject;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.io.File;

/**
 * Created by Amany on 01-Nov-17.
 */

@SuppressWarnings("ConstantConditions")
public class OfflineVideoService extends Service implements AudioManager.OnAudioFocusChangeListener, Player.EventListener, SimpleExoPlayer.VideoListener {

    private static final String TAG = "TELEPHONESERVICE";
    public static SimpleExoPlayer mp;
    public static Bitmap bitmap;
    public static Notification notification;
    public static int totalDuration;
    private static boolean currentVersionSupportLockScreenControls = false;
    private static int mBufferPosition;
    private static boolean isReady = false;
    private static long resumePosition;
    private static int resumeWindow;
    private static AdaptiveTrackSelection.Factory videoTrackSelectionFactory;
    private static DefaultTrackSelector trackSelector;
    private static MediaSource videoSource;
    // 96kbps*10secs/8bits
    // per byte
    // public static Timer timer;
    private static boolean currentVersionSupportBigNotification = false;
    private static int percent;
    private static boolean shouldAutoPlay;
    private static DefaultExtractorsFactory extractorsFactory;
    private static EncryptedFileDataSourceFactory dataSourceFactory;
    private static boolean inErrorState;
    private static Player.EventListener lisinter;
    private static SimpleExoPlayer.VideoListener lisintervideo;
    public Activity activity;
    String LOG_CLASS = "SongService";
    int NOTIFICATION_ID = 1111;
    RemoteViews simpleContentView;
    RemoteViews expandedView;
    Bitmap mDummyAlbumArt;
    int progress;
    String albumName;
    MediaSessionManager mediaSessionManager;
    /**
     * Play song, Update Lockscreen fields
     *
     * @param songPath
     * @param data
     */
    VideoDownloadObject data1;
    String songPath1;
    private Context con;
    private AudioManager audioManager;
    private FileCache fileCache;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private boolean intialStage;
    private MediaSessionCompat mMediaSessionCompat;


    // @Override
    public static int getDuration() {

        if (isReady && mp != null) {
            try {
                // if(ConnectionHandler.IS_TESTING_MOOD) Log.d(TAG,
                // "Call get Duration");
                return (int) mp.getDuration();
            } catch (Exception e) {
                e.printStackTrace();


            }
        }
        return 0;
    }

    // @Override
    private static int getCurrentPosition() {
        if (isReady && mp != null) {
            try {
                return (int) mp.getCurrentPosition();
            } catch (Exception e) {
                // Log.e(TAG, "getCurrentPosition", e);
                return 0;
            }
        } else
            return 0;
    }


    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    /**
     * Send message from timer
     *
     * @author jonty.ankit
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }
    public static void initializePlayer() {
        if (mp != null) {
            mp.removeListener(lisinter);

        }
        if (mp == null) {
            shouldAutoPlay = true;
            videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mp = ExoPlayerFactory.newSimpleInstance(Applicationmanager.getContext(), trackSelector);
        }


        mp.addListener(lisinter);
        mp.addVideoListener(lisintervideo);
        DownloadActivity.SetupCiper2Decrypt_files(PlayerConstants.OfflineVideo_LIST.get(PlayerConstants.SONG_NUMBER).getVideoID());

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        dataSourceFactory = new EncryptedFileDataSourceFactory(Encryption_DecryptionAudio.mCipher, Encryption_DecryptionAudio.mSecretKeySpec, Encryption_DecryptionAudio.mIvParameterSpec, bandwidthMeter);
        extractorsFactory = new DefaultExtractorsFactory();
        Uri uri = Uri.fromFile(DownloadActivity.mEncryptedFile);
        File ffound = new File(uri.getPath());
        if (ffound.exists() && ffound.length() > 0) {
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            // Build the ExoPlayer and start playback
            mp.prepare(videoSource);
            Activity_fullvideodownload.surfaceView.requestFocus();

            mp.setPlayWhenReady(true);
            Activity_fullvideodownload.surfaceView.setPlayer(mp);

            Activity_fullvideodownload.surfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);


        } else {
            DownloadActivity.progBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.filenotfound), Toast.LENGTH_LONG).show();


        }    // Build the ExoPlayer and start playback
        inErrorState = false;

        boolean haveResumePosition = VideoService.resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mp.seekTo(VideoService.resumeWindow, resumePosition);
        }
        mp.prepare(videoSource, !haveResumePosition, false);

        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
            if (mpackagename
                    .equalsIgnoreCase("Activity_fullvideodownload"))

            {
                Activity_fullvideodownload.surfaceView.requestFocus();

                mp.setPlayWhenReady(true);
                Activity_fullvideodownload.surfaceView.setPlayer(mp);

                Activity_fullvideodownload.surfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

                Activity_fullvideodownload.changeUi();

            }
        }
    }

    public static void releasePlayer() {
        if (mp != null) {
            shouldAutoPlay = mp.getPlayWhenReady();
            updateResumePosition();
            mp.release();
            mp = null;
            trackSelector = null;
        }
    }

    private static void updateResumePosition() {
        resumeWindow = mp.getCurrentWindowIndex();
        resumePosition = Math.max(0, mp.getContentPosition());
    }

    private static void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mp.stop();
        mp.removeListener(this);
        return false;
    }

    @Override
    public void onCreate() {

        con = OfflineVideoService.this;
        shouldAutoPlay = true;

        if (mp == null) {
            shouldAutoPlay = true;
            clearResumePosition();
            videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mp = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            lisinter = this;
            lisintervideo = this;
        }


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        fileCache = new FileCache(getBaseContext());

        currentVersionSupportBigNotification = UtilFunctions.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilFunctions.currentVersionSupportLockScreenControls();

        //Set mediaSession's MetaData
        super.onCreate();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (currentVersionSupportLockScreenControls) {
            RegisterRemoteClient();

            mMediaSessionCompat.setActive(true);
        }


        playSong();

        PlayerConstants.SONG_CHANGE_HANDLER = new Handler(msg -> {
            try {
                playSong();
                if (getActivity() != null) {
                    String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
                    Log.d("topActivity", "CURRENT Activity ::" + mpackagename);


                    if (mpackagename
                            .equalsIgnoreCase("Activity_fullvideodownload"))

                    {

                    }


                }

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
                if (currentVersionSupportLockScreenControls) {
                    mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, getCurrentPosition(), 1.0f)
                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
                }


                mp.setPlayWhenReady(true);
                intialStage = false;

            } else if (message.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.pause))) {
                PlayerConstants.SONG_PAUSED = true;
                if (currentVersionSupportLockScreenControls) {
//							remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);

                    mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PAUSED, getCurrentPosition(), 1.0f)
                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
                }
                mp.setPlayWhenReady(false);
                intialStage = false;
            }
            //   newNotification();

            try {
                if (getActivity() != null) {
                    String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
                    Log.d("topActivity", "CURRENT Activity ::" + mpackagename);


                    if (mpackagename
                            .equalsIgnoreCase("Activity_fullvideodownload"))

                    {
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            Log.d("TAG", "TAG Pressed: " + message);
            return false;
        });


        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {

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
                        if (!isReady) {
                            if (mp != null) {
                                mp.setPlayWhenReady(false);

                                isPausedInCall = true;
                            }
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:// call finish
                        // Phone idle. Start playing.
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
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;

    }

    /**
     * Notification Custom Bignotification is available from API 16
     */


    @SuppressWarnings("EmptyMethod")
    private void Remove_handlers() {
        // TODO Auto-generated method stub
        // handler.removeCallbacksAndMessages(PlayerConstants.SONG_CHANGE_HANDLER);


    }

    @Override
    public void onDestroy() {
        if (mp != null) {
            mp.release();
            mp.removeListener(this);

            mp = null;

        }
        if (mMediaSessionCompat != null)
            mMediaSessionCompat.release();
        audioManager.abandonAudioFocus(this);

        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void playSong() {

        Remove_handlers();
        if(mp!=null) {
            mp.removeListener(this);
            mp.addListener(this);
        }

        try {
            initializePlayer();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void RegisterRemoteClient() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        ComponentName mRemoteControlResponder = new ComponentName(getPackageName(),
                NotificationBroadcast.class.getName());


        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(mRemoteControlResponder);

        mMediaSessionCompat = new MediaSessionCompat(getApplication(), "JairSession", mRemoteControlResponder, null);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        PlaybackStateCompat playbackStateCompat = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)

                .build();
        mMediaSessionCompat.setPlaybackState(playbackStateCompat);
        //	mMediaSessionCompat.setCallback(mMediaSessionCallback);


    }

    /**
     * Updates the lockscreen controls, if enabled.
     */

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mp != null) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS: {
                    if (mp!=null&&mp.getPlayWhenReady()) {
                        mp.setPlayWhenReady(false);
                        Activity_fullvideodownload.changeUi();

                    }
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                    if (mp != null) {
                    mp.setPlayWhenReady(false);
                    Activity_fullvideodownload.changeUi();}
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
            //noinspection ConstantConditions
            if (playbackState == Player.STATE_READY) {
                if (mp.getPlayWhenReady()) {
                    isReady = true;
                    mp.setPlayWhenReady(true);
                    if(  Activity_fullvideodownload.progBar!=null)
                    Activity_fullvideodownload.progBar.setVisibility(View.GONE);

                }
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
        inErrorState = true;
        if (isBehindLiveWindow(error)) {
            clearResumePosition();
            initializePlayer();
        } else {
            updateResumePosition();
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (inErrorState) {
            // This will only occur if the user has performed a seek whilst in the error state. Update the
            // resume position so that if the user then retries, playback will resume from the position to
            // which they seeked.
            updateResumePosition();
        }
    }

    private void Handle_error() {

        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                getApplicationContext());
        if (isServiceRunning4) {
            Intent i = new Intent(getApplicationContext(), VideoService.class);

            getApplicationContext().stopService(i);
            Remove_handlers();

        }

        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");


            if (mpackagename
                    .equalsIgnoreCase("Activity_fullvideodownload")) {
                Activity_fullvideodownload.progBar.setVisibility(View.GONE);

            }


        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public void onRenderedFirstFrame() {

    }


}

