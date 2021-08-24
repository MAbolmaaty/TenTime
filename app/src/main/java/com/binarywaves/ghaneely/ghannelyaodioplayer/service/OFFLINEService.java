package com.binarywaves.ghaneely.ghannelyaodioplayer.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaMetadataCompat;
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
import com.binarywaves.ghaneely.ghannelyactivites.DownloadActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.receiver.NotificationBroadcast;
import com.binarywaves.ghaneely.ghannelyaodioplayer.receiver.NotificationOffline;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.NotificationUtils;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackDownloadObject;
import com.binarywaves.ghaneely.ghannelyutils.DbBitmapUtility;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
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

import java.io.File;
import java.util.Timer;

/**
 * Created by Amany on 24-Sep-17.
 */

@SuppressWarnings("ConstantConditions")
public class OFFLINEService extends Service implements AudioManager.OnAudioFocusChangeListener, Player.EventListener {

    public static final String NOTIFY_PREVIOUS = "com.tutorialsface.audioplayer.previous";
    public static final String NOTIFY_DELETE = "com.tutorialsface.audioplayer.delete";
    public static final String NOTIFY_PAUSE = "com.tutorialsface.audioplayer.pause";
    public static final String NOTIFY_PLAY = "com.tutorialsface.audioplayer.play";
    public static final String NOTIFY_NEXT = "com.tutorialsface.audioplayer.next";
    public static final String NOTIFY_open = "com.tutorialsface.audioplayer.open";
    private  NotificationOffline receiver ;

    private static final String TAG = "TELEPHONESERVICE";
    private static Timer timer;
    private static Bitmap bitmap;
    private static String songName;
    // testing audiotrack
    private static ExoPlayer mp;
    private static boolean isReady;
    private static Notification notification;
    private static boolean currentVersionSupportLockScreenControls = false;
    private static boolean currentVersionSupportBigNotification = false;
    public  static final int NOTIFICATION_ID = 1111;
    ProgressDialog pDialog;
    private static Encryption_DecryptionAudio enc;
    private static String ENCRYPTED_FILE_NAME;

    public static NotificationUtils mNotificationUtils;
    Notification nb;
    int amany;
    /**
     * Play song, Update Lockscreen fields
     *
     * @param songPath
     * @param data
     */
    TrackDownloadObject data1;
    private AudioManager audioManager;
    private FileCache fileCache;
    private RemoteViews simpleContentView;
    private RemoteViews expandedView;
    private MediaSessionCompat mMediaSessionCompat;
    private boolean isPausedInCall = false;
    private boolean intialStage = true;

    private static Activity getActivity() {

        return Applicationmanager.getCurrentActivity();
    }

    /**
     * Send message from timer
     *
     * @author jonty.ankit
     */

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

    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }
    @Override
    public void onCreate() {
        mNotificationUtils = new NotificationUtils(this);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        mp = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        mp.removeListener(this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        currentVersionSupportBigNotification = UtilFunctions.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilFunctions.currentVersionSupportLockScreenControls();
        fileCache = new FileCache(getBaseContext());
        timer = new Timer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            receiver=new NotificationOffline();
            IntentFilter filter = new IntentFilter();
            filter.addAction(NOTIFY_PREVIOUS);
            filter.addAction(NOTIFY_NEXT);
            filter.addAction(NOTIFY_DELETE);

            filter.addAction(NOTIFY_open);

            filter.addAction(NOTIFY_PAUSE);
            filter.addAction(NOTIFY_PLAY);



            registerReceiver(receiver, filter);
        }
        super.onCreate();
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (currentVersionSupportLockScreenControls) {
            RegisterRemoteClient();

            mMediaSessionCompat.setActive(true);
            updateMediaSessionMetaData(PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER));
        }
        newNotification();

        try {


            playSong();

            PlayerConstants.SONG_CHANGE_HANDLER = new Handler(msg -> {
                newNotification();

                try {
                    playSong();
                    DownloadActivity.changeUI();


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

                        mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PAUSED, getCurrentPosition(), 1.0f)
                                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
                    }
                    mp.setPlayWhenReady(false);
                    intialStage = false;
                }
                newNotification();

                try {
                    DownloadActivity.changeUI();

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
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void newNotification() {
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());
        if (language.equalsIgnoreCase("ar")) {
            songName = PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackArName();

        }
        if (language.equalsIgnoreCase("en")) {
            songName = PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackEnName();

        }


        bitmap = DbBitmapUtility.getImage(PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage());

        simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);


        setListeners(simpleContentView);
        setListeners(expandedView);

            if (currentVersionSupportBigNotification) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    nb = mNotificationUtils.
                            getAndroidChannelNotification(songName, expandedView,simpleContentView,PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage().toString(),bitmap,PlayerConstants.SONG_PAUSED,"");


                }else{
                    notification = mNotificationUtils.
                            getAndroidNotification(songName, expandedView,simpleContentView,PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage().toString(),bitmap,PlayerConstants.SONG_PAUSED,"");
                }

            } else {
                notification = mNotificationUtils.getAndroidNotification_notsuppbn(songName, expandedView,simpleContentView,PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage().toString(),bitmap,PlayerConstants.SONG_PAUSED,"");

            }


            if (nb!=null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nb.flags |= Notification.FLAG_ONGOING_EVENT;
                mNotificationUtils.getManager().notify(NOTIFICATION_ID, nb);}
            else{

                notification.flags |= Notification.FLAG_ONGOING_EVENT;
                startForeground(NOTIFICATION_ID, notification);
            }


        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackagename
                    .equalsIgnoreCase("DownloadActivity"))

            {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    DownloadActivity.progBar.setVisibility(View.GONE);
                    DownloadActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    DownloadActivity.startwhellprogress();
                    DownloadActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    DownloadActivity.startwhellprogress();
                    DownloadActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }
        }
    }

    /**
     * Notification Custom Bignotification is available from API 16
     * <p>
     * <p>
     * /**
     * Notification click listeners
     */
    private void RegisterRemoteClient() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        ComponentName mRemoteControlResponder = new ComponentName(getPackageName(),
                NotificationOffline.class.getName());


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
    private void updateMediaSessionMetaData(TrackDownloadObject data) {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, data.getSingerEnName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, data.getAlbumEnName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, data.getAlbumEnName());
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
        mMediaSessionCompat.setMetadata(builder.build());
    }


    private void setListeners(RemoteViews view) {
        Remove_handlers();
        if (isReady && PlayerConstants.SONGnext && PlayerConstants.SONG_PAUSED) {
            mp.removeListener(this);
        }

        if (PlayerConstants.SONGnext) {
            mp.removeListener(this);

        }
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);
        Intent open = new Intent(NOTIFY_open);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous,
                PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pOpen = PendingIntent.getBroadcast(getApplicationContext(), 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.open, pOpen);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete,
                PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause,
                PendingIntent.FLAG_UPDATE_CURRENT);

        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next,
                PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play,
                PendingIntent.FLAG_UPDATE_CURRENT);

        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

    }

    @SuppressWarnings("EmptyMethod")
    private void Remove_handlers() {
        // TODO Auto-generated method stub
     //   mNotificationUtils.getManager().cancel(NOTIFICATION_ID);

    }

    @Override
    public void onDestroy() {
        mNotificationUtils.getManager().cancel(NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            unregisterReceiver(receiver);

        }
        if (mp != null) {
            mp.release();
            mp.removeListener(this);

            mp = null;
        }
        audioManager.abandonAudioFocus(this);

        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void playSong() {
        Remove_handlers();
        mp.removeListener(this);
        mp.addListener(this);
        DownloadActivity.SetupCiper2Decrypt_files(PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId());
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        EncryptedFileDataSourceFactory dataSourceFactory = new EncryptedFileDataSourceFactory(Encryption_DecryptionAudio.mCipher, Encryption_DecryptionAudio.mSecretKeySpec, Encryption_DecryptionAudio.mIvParameterSpec, bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        try {
            Uri uri = Uri.fromFile(DownloadActivity.mEncryptedFile);
            File ffound = new File(uri.getPath());
            if (ffound.exists() && ffound.length() > 0) {
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

                mp.prepare(videoSource);
                mp.setPlayWhenReady(true);
            } else {
                DownloadActivity.progBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.filenotfound), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mp != null) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS: {
                    if (mp.getPlayWhenReady()) {
                        Controls.pauseControl(getApplicationContext());

                        DownloadActivity.changeUI();

                    }
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                    Controls.pauseControl(getApplicationContext());

                    DownloadActivity.changeUI();
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
                if(DownloadActivity.progBar!=null)
                DownloadActivity.progBar.setVisibility(View.GONE);
                if(DownloadActivity.audioRelative!=null)

                    DownloadActivity.audioRelative.setVisibility(View.VISIBLE);

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
        mNotificationUtils.getManager().cancel(NOTIFICATION_ID);


    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }


    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}

