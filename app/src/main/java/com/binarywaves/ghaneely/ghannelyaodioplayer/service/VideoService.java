package com.binarywaves.ghaneely.ghannelyaodioplayer.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.AuthorizationMediaLinkClass;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyactivites.VideoPlayerActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlVideo;
import com.binarywaves.ghaneely.ghannelyaodioplayer.receiver.NotificationBroadcast;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.TimerTask;

/**
 * Created by Amany on 02-Jul-17.
 */

public class VideoService extends Service implements AudioManager.OnAudioFocusChangeListener, Player.EventListener, SimpleExoPlayer.VideoListener {

    private static final String TAG = "TELEPHONESERVICE";
    private static final int INTIAL_KB_BUFFER = 96 * 10 / 8;// assume
    public static SimpleExoPlayer mp;
    public static RemoteControlClient remoteControlClient;
    public static String songName;
    public static Notification notification;
    public static boolean isRepeat = false;
    public static boolean isShuffle = false;
    public static int totalDuration;
    public static boolean isReady = false;
    // 96kbps*10secs/8bits
    // per byte
    public static int counter = 0;
    public static int resumeWindow;
    private static boolean currentVersionSupportLockScreenControls = false;
    private static Bitmap bitmap;
    private static int mBufferPosition;
    private static boolean shouldAutoPlay;
    private static long resumePosition;
    private static Factory videoTrackSelectionFactory;
    private static DefaultTrackSelector trackSelector;
    private static DataSource.Factory dataSourceFactory;
    private static String songPath1;
    private static int percent;
    private static Player.EventListener lisinter;
    private static SimpleExoPlayer.VideoListener lisintervideo;
    private static boolean inErrorState;
    private final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private final int BUFFER_SEGMENT_COUNT = 256;
    public Activity activity;
    String LOG_CLASS = "SongService";
    int NOTIFICATION_ID = 1111;
    RemoteViews simpleContentView;
    RemoteViews expandedView;
    Bitmap mDummyAlbumArt;
    ProgressDialog pDialog;
    int amany;
    String albumName;
    MediaSessionManager mediaSessionManager;
    private Context con;
    private AudioManager audioManager;
    private FileCache fileCache;
    private int progress;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mp != null) {

                progress = (int) ((mp.getCurrentPosition() * 100) / totalDuration);
                Integer i[] = new Integer[3];
                i[0] = (int) mp.getCurrentPosition();
                i[1] = (int) mp.getDuration();
                i[2] = progress;
                try {
                    PlayerConstants.PROGRESSBAR_HANDLER
                            .sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, i));
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    };
    /**
     * Play song, Update Lockscreen fields
     *
     * @param songPath
     * @param data
     */
    private VideoObjectResponse data1;
    private boolean isPausedInCall = false;
    private boolean intialStage;
    private File downloadingMediaFile;
    private boolean isInterrupted;
    // Track for display by progressBar
    private long mediaLengthInKb, mediaLengthInSeconds;
    private int totalKbRead = 0;
    private int RENDERER_COUNT = 300000;
    private int minBufferMs = 250000;
    private MediaSessionCompat mMediaSessionCompat;

    // @Override
    private static int getBufferPercentage() {
        return mBufferPosition;
    }

    // @Override
    private static int getDuration() {

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
    public static int getCurrentPosition() {
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

    public static void updateVideoProgress() {
        if (VideoPlayerActivity.seek_bar != null) {
            if (totalDuration > 0 && mp != null) {

                int videoProgress = (int) (mp.getCurrentPosition() * 100 / totalDuration);

                VideoPlayerActivity.seek_bar.setProgress(videoProgress);
                VideoPlayerActivity.seek_bar.setSecondaryProgress(getBufferPercentage());

            }
        }
    }


    public static void seekVideo() {
        if (mp != null && totalDuration > 0) {

            int videoPosition = (int) (mp.getDuration() * VideoPlayerActivity.seek_bar.getProgress() / 100);
            mp.seekTo(videoPosition);

        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }
    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    public static void initializePlayer() {

        videoTrackSelectionFactory = new Factory(new DefaultBandwidthMeter());
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DefaultBandwidthMeter bandwidthMeter2 = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(Applicationmanager.getContext(),
                Util.getUserAgent(Applicationmanager.getContext(), "ExoPlayer"), bandwidthMeter2);

        VideoObjectResponse data = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER);
        String songPath = ServerConfig.Video_Url + data.getVideoPath();                ///    newNotification();

        MediaSource audio = new ExtractorMediaSource(Uri.parse(songPath.replaceAll(" ", "%20")), dataSourceFactory, new DefaultExtractorsFactory(), null, null);
        // Build the ExoPlayer and start playback
        inErrorState = false;
        if (mp != null) {
            mp.removeListener(lisinter);

        }
        if (mp == null) {
            mp = ExoPlayerFactory.newSimpleInstance(Applicationmanager.getContext(), trackSelector);

        }


        mp.addListener(lisinter);
        mp.addVideoListener(lisintervideo);
        boolean haveResumePosition = VideoService.resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            VideoService.mp.seekTo(VideoService.resumeWindow, VideoService.resumePosition);
        }
        mp.prepare(audio, !haveResumePosition, false);
        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
            if (mpackagename
                    .equalsIgnoreCase("VideoPlayerActivity"))

            {
                VideoPlayerActivity.surfaceView.requestFocus();

                mp.setPlayWhenReady(true);
                VideoPlayerActivity.surfaceView.setPlayer(mp);

                VideoPlayerActivity.surfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);


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

        con = VideoService.this;
        shouldAutoPlay = true;

        if (mp == null) {
            shouldAutoPlay = true;
            clearResumePosition();
            videoTrackSelectionFactory = new Factory(new DefaultBandwidthMeter());
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mp = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            lisinter = this;
            lisintervideo = this;
        }
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        fileCache = new FileCache(getBaseContext());

        boolean currentVersionSupportBigNotification = UtilFunctions.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilFunctions.currentVersionSupportLockScreenControls();

        //Set mediaSession's MetaData
        super.onCreate();
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (PlayerConstants.SONGS_LISTVideo != null && PlayerConstants.SONGS_LISTVideo.size() > 0) {
            VideoObjectResponse data = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER);
            if (currentVersionSupportLockScreenControls) {
                RegisterRemoteClient();

                mMediaSessionCompat.setActive(true);
                updateMediaSessionMetaData(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER));
            }

            String songPath = ServerConfig.Video_Url + data.getVideoPath();
            Log.d("videopath", songPath);
            playSong(songPath, data);

            PlayerConstants.SONG_CHANGE_HANDLER = new Handler(msg -> {
                VideoObjectResponse data2 = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER);
                String songPath2 = ServerConfig.Video_Url + data2.getVideoPath();                ///    newNotification();
                try {
                    playSong(songPath2, data2);
                    if (getActivity() != null) {
                        String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
                        Log.d("topActivity", "CURRENT Activity ::" + mpackagename);


                        if (mpackagename
                                .equalsIgnoreCase("VideoPlayerActivity"))

                        {
                            VideoPlayerActivity.changeUI();
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
                                .equalsIgnoreCase("VideoPlayerActivity"))

                        {
                            VideoPlayerActivity.changeUI();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                Log.d("TAG", "TAG Pressed: " + message);
                return false;
            });


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
            if (telephonyManager != null) {
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }


        } else {
            Toast.makeText(getApplicationContext(), con.getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();
            PlayerAcreenActivity.StopService();
        }
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
    private void playSong(String songPath, final VideoObjectResponse data) {

        // if (SongService.isReady) {
        // TrackLisinterService.start = true;

        // }
        data1 = data;
        songPath1 = songPath;
        Remove_handlers();


        AuthorizationMediaLinkClass auth = new AuthorizationMediaLinkClass();
        //noinspection UnusedAssignment
        String str = songPath.replaceAll(" ", "%20");
        try {
            initializePlayer();


            if (getActivity() != null) {
                String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");


                if (mpackagename
                        .equalsIgnoreCase("VideoPlayerActivity"))

                {
                    VideoPlayerActivity.surfaceView.requestFocus();

                    mp.setPlayWhenReady(true);
                    VideoPlayerActivity.surfaceView.setPlayer(mp);

                    VideoPlayerActivity.surfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);


                }
            }


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
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
    private void updateMediaSessionMetaData(VideoObjectResponse data) {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, data.getSingerEnName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, data.getSingerEnName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, data.getSingerEnName());
        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, getDuration());
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
        mMediaSessionCompat.setMetadata(builder.build());
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mp != null) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS: {
                    if (mp.getPlayWhenReady()) {
                        ControlVideo.pauseControl(getApplicationContext());

                        VideoPlayerActivity.changeUI();
                    }
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                    ControlVideo.pauseControl(getApplicationContext());

                    VideoPlayerActivity.changeUI();
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


    private void setBufferPosition(int progress) {
        mBufferPosition = progress;
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

        if (playbackState == Player.STATE_BUFFERING) {
            setBufferPosition((percent * totalDuration / 100));


        }

        if (playbackState == Player.STATE_ENDED) {
            if (mp != null) {
                mp.removeListener(this);
                Remove_handlers();
                if (isRepeat) {
                    ControlVideo.repeatControl(getApplicationContext());

                }
                if (isShuffle) {
                    ControlVideo.shuffleControl(getApplicationContext());

                }
                if (!isRepeat && !isShuffle) {

                    ControlVideo.nextControl(getApplicationContext());
                }


            }
        }

        if (playbackState == Player.STATE_READY) {
            if (mp != null && mp.getPlayWhenReady()) {
                mp.setPlayWhenReady(true);

                isReady = true;
                totalDuration = (int) mp.getDuration();
                Log.d("mono", totalDuration + "");
                if (currentVersionSupportLockScreenControls) {
                    mMediaSessionCompat.setActive(true);
                    updateMediaSessionMetaData(data1);
                    mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, getCurrentPosition(), 1.0f)
                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
                }


                if (getActivity() != null) {
                    String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");


                    if (mpackagename
                            .equalsIgnoreCase("VideoPlayerActivity")) {
                        if (VideoPlayerActivity.progBar != null && VideoPlayerActivity.next != null && VideoPlayerActivity.prev != null) {

                            VideoPlayerActivity.progBar.setVisibility(View.GONE);
                            VideoPlayerActivity.next.setEnabled(true);
                            VideoPlayerActivity.prev.setEnabled(true);
                            VideoPlayerActivity.next.setClickable(true);
                            VideoPlayerActivity.prev.setClickable(true);
                        }
                    }

                }

                if (mp.getCurrentPosition() > 0) {
                    handler.sendEmptyMessage(0);
                }
                if (data1.getIsPremium() != null && Boolean.valueOf(data1.getIsPremium())) {
                    if (DrawerActivity.dialogdimmed != null && !DrawerActivity.dialogdimmed.isShowing()) {

                        DrawerActivity.show_Alert_Dimmed_Track(getApplicationContext());
                    }
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
            VideoPlayerActivity.changeUI();
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
                    .equalsIgnoreCase("VideoPlayerActivity")) {
                VideoPlayerActivity.progBar.setVisibility(View.GONE);
                VideoPlayerActivity.next.setEnabled(false);
                VideoPlayerActivity.prev.setEnabled(false);
                VideoPlayerActivity.next.setClickable(false);
                VideoPlayerActivity.prev.setClickable(false);
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


    /**
     * Send message from timer
     *
     * @author jonty.ankit
     */
    private class MainTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }
}

