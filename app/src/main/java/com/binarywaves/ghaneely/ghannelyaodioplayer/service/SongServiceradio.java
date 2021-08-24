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

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsGridActivity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTab_Otheralbum_Activity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTab_Relatedalbum_Activity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTab_Tracks_Activity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_AlbumsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_SimilarActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_SingleActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_TopActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistradioActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FavoritesActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FavoritesRadio;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_LikesActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_RecentlyplayedActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_followArtActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendsTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.InboxActivity;
import com.binarywaves.ghaneely.ghannelyactivites.MoodsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayListActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayListTracksActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_AlbumActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_AllActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_ArtistActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_PlaylistActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_TracksActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.receiver.NotificationBroadcast;
import com.binarywaves.ghaneely.ghannelyaodioplayer.receiver.NotificationRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.NotificationRadioUtils;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.NotificationUtils;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.RadioConstant;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.Radio;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelyutils.Utils;
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

import java.io.IOException;
import java.util.Timer;

public class SongServiceradio extends Service implements AudioManager.OnAudioFocusChangeListener, Player.EventListener {
    public static final String NOTIFY_DELETE = "com.tutorialsface.audioplayer.delete3";
    public static final String NOTIFY_PAUSE = "com.tutorialsface.audioplayer.pause";
    public static final String NOTIFY_PLAY = "com.tutorialsface.audioplayer.play";
    public static final String NOTIFY_open = "com.tutorialsface.audioplayer.open";
    private static final String TAG = "TELEPHONESERVICE";
    private  NotificationRadio receiver ;

    private static Timer timer;
    private static boolean currentVersionSupportLockScreenControls = false;
    private static Bitmap bitmap;
    private static String songName;
    private static Notification notification;
    // testing audiotrack
    private static ExoPlayer mp;
    private static boolean isReady;
    private static boolean currentVersionSupportBigNotification = false;
    ////
    private static int percent;
    final String songPath = "";
    public static final int NOTIFICATION_ID = 1111;
    String LOG_CLASS = "SongService";
    Bitmap mDummyAlbumArt;
    ProgressDialog pDialog;
    int amany;
    private RemoteViews simpleContentView;
    private RemoteViews expandedView;
    private AudioManager audioManager;
    private FileCache fileCache;
    /**
     * Play song, Update Lockscreen fields
     *
     * @param songPath
     * @param data
     */
    private Radio data1;
    private ComponentName remoteComponentName;
    private boolean isPausedInCall = false;
    private boolean intialStage = true;
    private MediaSessionCompat mMediaSessionCompat;
    public static NotificationRadioUtils mNotificationUtils;
    Notification nb;

    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
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
        mNotificationUtils = new NotificationRadioUtils(this);

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

        currentVersionSupportBigNotification = UtilFunctions.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilFunctions.currentVersionSupportLockScreenControls();
        timer = new Timer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            receiver=new NotificationRadio();
            IntentFilter filter = new IntentFilter();

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
        try {

            Radio data = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER);
            if (currentVersionSupportLockScreenControls) {
                RegisterRemoteClient();

                mMediaSessionCompat.setActive(true);
                updateMediaSessionMetaData(RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER));
            }
            final String songPath = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioURL();
            newNotification();
            playSong(songPath, data);

            RadioConstant.SONG_CHANGE_HANDLER = new Handler(msg -> {
                Radio data2 = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER);
                String songPath1 = data2.getRadioURL();

                newNotification();
                try {
                    playSong(songPath1, data2);
                   changePlayerButton();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            });

            RadioConstant.PLAY_PAUSE_HANDLER = new Handler(msg -> {
                String message = (String) msg.obj;
                if (mp == null)
                    return false;
                if (message.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.play))) {
                    RadioConstant.SONG_PAUSED = false;
                    if (currentVersionSupportLockScreenControls) {
                        mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
                    }

                    mp.setPlayWhenReady(true);
                    intialStage = false;
                } else if (message.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.pause))) {
                    RadioConstant.SONG_PAUSED = true;
                    if (currentVersionSupportLockScreenControls) {
                        mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
                    }

                    mp.setPlayWhenReady(false);
                    intialStage = false;
                }
                newNotification();

                try {
                  changePlayerButton();
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
                        if (mp != null && RadioConstant.SONG_PAUSED && isPausedInCall) {
                            mp.setPlayWhenReady(false);

                        }
                        if (mp != null && !RadioConstant.SONG_PAUSED && isPausedInCall) {
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

    private void changePlayerButton() {
        HomeActivity.changeUIradio();
        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
            if (mpackagename
                    .equalsIgnoreCase("PlayListTracksActivity"))

            {
                PlayListTracksActivity.changeUI(getApplicationContext());
            }

            if (mpackagename
                    .equalsIgnoreCase("HomeActivity"))

            {
                HomeActivity.changeUI1(getApplicationContext());
            }


            if (mpackagename
                    .equalsIgnoreCase("FriendsTabs.FriendsTabActivity")) {
                Log.i("selectedfragment", FriendsTabActivity.selectedfragment + "");

                switch (FriendsTabActivity.selectedfragment) {

                    case 0:
                        FriendTab_RecentlyplayedActivity.changeUI1(getApplicationContext());

                        break;
                    case 1:
                        FriendTab_LikesActivity.changeUI1(getApplicationContext());
                        break;
                    case 2:
                        FriendTab_followArtActivity.changeUI1(getApplicationContext());
                        break;


                }
            }
            if (mpackagename
                    .equalsIgnoreCase(
                            "ArtistradioActivity"))

            {
                ArtistradioActivity
                        .changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("FavoritesRadio")) {
                FavoritesRadio.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase(
                            "PlayListActivity"))

            {
                PlayListActivity
                        .changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("FavoritesActivity")) {
                FavoritesActivity.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("InboxActivity"))

            {
                InboxActivity.changeUI1(getApplicationContext());
            }

            if (mpackagename
                    .equalsIgnoreCase("AlbumsGridActivity"))

            {
                AlbumsGridActivity.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
                switch (AlbumTabsActivity.selectedfragment) {
                    case 0:
                        AlbumTab_Tracks_Activity.changeUI1(getApplicationContext());
                        break;

                    case 1:
                        AlbumTab_Otheralbum_Activity.changeUI1(getApplicationContext());
                        break;

                    case 2:
                        AlbumTab_Relatedalbum_Activity.changeUI1(getApplicationContext());
                        break;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("SearchTabs.SearchTabActivity")) {
                switch (SearchTabActivity.selectedfragment) {
                    case 0:
                        SearchTab_AllActivity.changeUI1(getApplicationContext());
                        break;
                    case 1:
                        SearchTab_TracksActivity.changeUI1(getApplicationContext());
                        break;
                    case 2:
                        SearchTab_PlaylistActivity.changeUI1(getApplicationContext());
                        break;
                    case 3:
                        SearchTab_ArtistActivity.changeUI1(getApplicationContext());
                        break;
                    case 4:
                        SearchTab_AlbumActivity.changeUI1(getApplicationContext());
                        break;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("ArtistTabs.ArtistTabsActivity")) {
                switch (ArtistTabsActivity.selectedfragment) {
                    case 0:
                        ArtistTab_TopActivity.changeUI1(getApplicationContext());
                        break;
                    case 1:
                        ArtistTab_AlbumsActivity.changeUI1(getApplicationContext());

                        break;
                    case 2:
                        ArtistTab_SimilarActivity.changeUI1(getApplicationContext());

                        break;

                    case 3:
                        ArtistTab_SingleActivity.changeUI1(getApplicationContext());

                        break;
                }
            }
            if (mpackagename
                    .equalsIgnoreCase(
                            "MoodsActivity"))

            {
                MoodsActivity
                        .changeUI(getApplicationContext());
            }
            // PlayerAcreenActivity.changeUI();

        }
    }

    /**
     * Notification Custom Bignotification is available from API 16
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void newNotification() {
        String albumName = null;
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());

        if (language.equalsIgnoreCase("ar")) {
            songName = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioArName();
            albumName = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioArName();

        }
        if (language.equalsIgnoreCase("en")) {
            songName = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioName();
            albumName = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioName();

        }

        String final_imgpath = ServerConfig.Radio_Url
                + RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioImage();
        String final_imgpath1 = final_imgpath.replaceAll(" ", "%20");

        try {
            bitmap = Constants.drawableFromUrl(getApplicationContext(), final_imgpath1).getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);

        setListeners(simpleContentView);
        setListeners(expandedView);


        if (currentVersionSupportBigNotification) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                nb = mNotificationUtils.
                        getAndroidChannelNotification(songName, expandedView,simpleContentView,RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioImage(),bitmap,PlayerConstants.SONG_PAUSED,albumName);


            }else{
                notification = mNotificationUtils.
                        getAndroidNotification(songName, expandedView,simpleContentView,RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioImage(),bitmap,PlayerConstants.SONG_PAUSED,albumName);
            }

        } else {
            notification = mNotificationUtils.getAndroidNotification_notsuppbn(songName, expandedView,simpleContentView,RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER).getRadioImage(),bitmap,RadioConstant.SONG_PAUSED,albumName);

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
                    .equalsIgnoreCase("FavoritesActivity"))

            {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    FavoritesActivity.progBar.setVisibility(View.GONE);
                    FavoritesActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    FavoritesActivity.progBar.setVisibility(View.VISIBLE);
                    FavoritesActivity.audioRelative.setEnabled(false);

                }

            }


            if (mpackagename
                    .equalsIgnoreCase("InboxActivity"))

            {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    InboxActivity.progBar.setVisibility(View.GONE);
                    InboxActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    InboxActivity.progBar.setVisibility(View.VISIBLE);
                    InboxActivity.audioRelative.setEnabled(false);

                }

            }

            if (mpackagename
                    .equalsIgnoreCase("PlayListTracksActivity"))

            {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    PlayListTracksActivity.progBar.setVisibility(View.GONE);
                    PlayListTracksActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    PlayListTracksActivity.startwhellprogress();
                    PlayListTracksActivity.audioRelative.setEnabled(false);

                }

            }


            if (mpackagename
                    .equalsIgnoreCase("PlayListActivity"))

            {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    PlayListActivity.progBar.setVisibility(View.GONE);
                    PlayListActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    PlayListActivity.startwhellprogress();
                    PlayListActivity.audioRelative.setEnabled(false);

                }

            }
            if (mpackagename
                    .equalsIgnoreCase("FavoritesRadio"))

            {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    FavoritesRadio.progBar.setVisibility(View.GONE);
                    FavoritesRadio.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    FavoritesRadio.startwhellprogress();
                    FavoritesRadio.audioRelative.setEnabled(false);

                }

            }

            if (mpackagename
                    .equalsIgnoreCase("AlbumsGridActivity"))

            {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    AlbumsGridActivity.progBar.setVisibility(View.GONE);
                    AlbumsGridActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    AlbumsGridActivity.startwhellprogress();
                    AlbumsGridActivity.audioRelative.setEnabled(false);

                }

            }

            if (mpackagename
                    .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
                switch (AlbumTabsActivity.selectedfragment) {
                    case 0:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            AlbumTab_Tracks_Activity.progBar.setVisibility(View.GONE);
                            AlbumTab_Tracks_Activity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            AlbumTab_Tracks_Activity.startwhellprogress();
                            AlbumTab_Tracks_Activity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            AlbumTab_Tracks_Activity.startwhellprogress();
                            AlbumTab_Tracks_Activity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                    case 1:

                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            AlbumTab_Otheralbum_Activity.progBar.setVisibility(View.GONE);
                            AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            AlbumTab_Otheralbum_Activity.startwhellprogress();
                            AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            AlbumTab_Otheralbum_Activity.startwhellprogress();
                            AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                    case 2:


                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            AlbumTab_Relatedalbum_Activity.progBar.setVisibility(View.GONE);
                            AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            AlbumTab_Relatedalbum_Activity.startwhellprogress();
                            AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            AlbumTab_Relatedalbum_Activity.startwhellprogress();
                            AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("HomeActivity")) {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    HomeActivity.progBar.setVisibility(View.GONE);
                    HomeActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    HomeActivity.startwhellprogress();
                    HomeActivity.audioRelative.setEnabled(false);

                }

            }

            if (mpackagename
                    .equalsIgnoreCase("PlayerAcreenActivity")) {
                Remove_handlers();

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    PlayerAcreenActivity.startwhellprogress();

                }
                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    PlayerAcreenActivity.progBar.setVisibility(View.GONE);

                }
            }


            if (mpackagename
                    .equalsIgnoreCase("MoodsActivity")) {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    MoodsActivity.progBar.setVisibility(View.GONE);
                    MoodsActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    MoodsActivity.startwhellprogress();
                    MoodsActivity.audioRelative.setEnabled(false);
                }
                if (RadioConstant.SONGnext) {
                    MoodsActivity.startwhellprogress();
                    MoodsActivity.audioRelative.setEnabled(false);
                    RadioConstant.SONGnext = false;

                }
            }


            if (mpackagename
                    .equalsIgnoreCase("ArtistradioActivity")) {
                Remove_handlers();

                if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                    ArtistradioActivity.progBar.setVisibility(View.GONE);
                    ArtistradioActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !RadioConstant.SONG_CHANGED) {
                    ArtistradioActivity.startwhellprogress();
                    ArtistradioActivity.audioRelative.setEnabled(false);
                }
                if (RadioConstant.SONGnext) {
                    ArtistradioActivity.startwhellprogress();
                    ArtistradioActivity.audioRelative.setEnabled(false);
                    RadioConstant.SONGnext = false;

                }
            }


            if (mpackagename
                    .equalsIgnoreCase("ArtistTabs.ArtistTabsActivity")) {
                switch (ArtistTabsActivity.selectedfragment) {
                    case 0:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_TopActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_TopActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_TopActivity.startwhellprogress();
                            ArtistTab_TopActivity.audioRelative.setEnabled(false);
                        }
                        if (RadioConstant.SONGnext) {
                            ArtistTab_TopActivity.startwhellprogress();
                            ArtistTab_TopActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;


                        }
                        break;
                    case 1:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_AlbumsActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_AlbumsActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_AlbumsActivity.startwhellprogress();
                            ArtistTab_AlbumsActivity.audioRelative.setEnabled(false);
                        }
                        if (RadioConstant.SONGnext) {
                            ArtistTab_AlbumsActivity.startwhellprogress();
                            ArtistTab_AlbumsActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;


                        }
                        break;
                    case 2:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_SimilarActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_SimilarActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_SimilarActivity.startwhellprogress();
                            ArtistTab_SimilarActivity.audioRelative.setEnabled(false);
                        }
                        if (RadioConstant.SONGnext) {
                            ArtistTab_SimilarActivity.startwhellprogress();
                            ArtistTab_SimilarActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;


                        }
                        break;

                    case 3:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_SingleActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_SingleActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            ArtistTab_SingleActivity.startwhellprogress();
                            ArtistTab_SingleActivity.audioRelative.setEnabled(false);
                        }
                        if (RadioConstant.SONGnext) {
                            ArtistTab_SingleActivity.startwhellprogress();
                            ArtistTab_SingleActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;


                        }
                        break;
                }
            }


            if (mpackagename
                    .equalsIgnoreCase("SearchTabs.SearchTabActivity")) {
                switch (SearchTabActivity.selectedfragment) {
                    case 0:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            SearchTab_AllActivity.progBar.setVisibility(View.GONE);
                            SearchTab_AllActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            SearchTab_AllActivity.startwhellprogress();
                            SearchTab_AllActivity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            SearchTab_AllActivity.startwhellprogress();
                            SearchTab_AllActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                    case 1:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            SearchTab_TracksActivity.progBar.setVisibility(View.GONE);
                            SearchTab_TracksActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            SearchTab_TracksActivity.startwhellprogress();
                            SearchTab_TracksActivity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            SearchTab_TracksActivity.startwhellprogress();
                            SearchTab_TracksActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                    case 2:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            SearchTab_PlaylistActivity.progBar.setVisibility(View.GONE);
                            SearchTab_PlaylistActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            SearchTab_PlaylistActivity.startwhellprogress();
                            SearchTab_PlaylistActivity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            SearchTab_PlaylistActivity.startwhellprogress();
                            SearchTab_PlaylistActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                    case 3:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            SearchTab_ArtistActivity.progBar.setVisibility(View.GONE);
                            SearchTab_ArtistActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            SearchTab_ArtistActivity.startwhellprogress();
                            SearchTab_ArtistActivity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            SearchTab_ArtistActivity.startwhellprogress();
                            SearchTab_ArtistActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                    case 4:
                        Remove_handlers();

                        if (RadioConstant.SONG_PAUSED && !RadioConstant.SONG_CHANGED) {
                            SearchTab_AlbumActivity.progBar.setVisibility(View.GONE);
                            SearchTab_AlbumActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !RadioConstant.SONG_CHANGED) {
                            SearchTab_AlbumActivity.startwhellprogress();
                            SearchTab_AlbumActivity.audioRelative.setEnabled(false);

                        }
                        if (RadioConstant.SONGnext) {
                            SearchTab_AlbumActivity.startwhellprogress();
                            SearchTab_AlbumActivity.audioRelative.setEnabled(false);
                            RadioConstant.SONGnext = false;
                        }
                        break;
                }
            }


        }
    }

    /**
     * Notification click listeners
     */
    private void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
        }
       // mNotificationUtils.getManager().cancel(NOTIFICATION_ID);

    }

    private void setListeners(RemoteViews view) {
        Remove_handlers();
        if (isReady && RadioConstant.SONGnext && RadioConstant.SONG_PAUSED) {
            mp.setPlayWhenReady(false);

        }

        if (RadioConstant.SONGnext) {
            mp.setPlayWhenReady(false);

        }
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent play = new Intent(NOTIFY_PLAY);
        Intent open = new Intent(NOTIFY_open);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete,
                PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pOpen = PendingIntent.getBroadcast(getApplicationContext(), 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.open, pOpen);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause,
                PendingIntent.FLAG_UPDATE_CURRENT);

        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play,
                PendingIntent.FLAG_UPDATE_CURRENT);

        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

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
        if (mMediaSessionCompat != null)
            mMediaSessionCompat.release();
        audioManager.abandonAudioFocus(this);

        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void playSong(String songPath, final Radio data) {
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

    private void RegisterRemoteClient() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
        }

        ComponentName mRemoteControlResponder = new ComponentName(getPackageName(),
                NotificationRadio.class.getName());


        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(mRemoteControlResponder);

        mMediaSessionCompat = new MediaSessionCompat(getApplication(), "JairSession", mRemoteControlResponder, null);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        PlaybackStateCompat playbackStateCompat = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)

                .build();
        mMediaSessionCompat.setPlaybackState(playbackStateCompat);
        //	mMediaSessionCompat.setCallback(mMediaSessionCallback);


    }

    /**
     * Updates the lockscreen controls, if enabled.
     */
    private void updateMediaSessionMetaData(Radio data) {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, data.getRadioName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, data.getRadioName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, data.getRadioName());
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
        mMediaSessionCompat.setMetadata(builder.build());
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mp != null) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS: {
                    if (mp.getPlayWhenReady()) {
                        ControlRadio.pauseControl(getApplicationContext());
changePlayerButton();                    }
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                    ControlRadio.pauseControl(getApplicationContext());
                    changePlayerButton();                                  break;
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
                mp.setPlayWhenReady(true);

                isReady = true;
                TrackLisinterService.start = true;

                if (getActivity() != null) {
                    String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
                    Utils.handle_ScreenViews(mpackagename);
                }
                isReady = true;
                if (currentVersionSupportLockScreenControls) {
                    mMediaSessionCompat.setActive(true);
                    updateMediaSessionMetaData(data1);
                    mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
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

    private void Handle_error() {
        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                getApplicationContext());
        if (isServiceRunning4) {
            Intent i = new Intent(getApplicationContext(), SongServiceradio.class);
            SongServiceradio.mNotificationUtils.getManager().cancel(SongServiceradio.NOTIFICATION_ID);

            getApplicationContext().stopService(i);
            Remove_handlers();

        }

        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackagename
                    .equalsIgnoreCase("PlayListTracksActivity")) {

                PlayListTracksActivity.progBar.setVisibility(View.GONE);

                PlayListTracksActivity.audioRelative.setVisibility(View.GONE);
                PlayListTracksActivity.audioRelative.setEnabled(false);
            }
            if (mpackagename
                    .equalsIgnoreCase("MoodsActivity"))

            {
                MoodsActivity.progBar.setVisibility(View.GONE);

                MoodsActivity.audioRelative.setVisibility(View.GONE);
                MoodsActivity.audioRelative.setEnabled(true);
           }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsGridActivity")) {

                AlbumsGridActivity.progBar.setVisibility(View.GONE);

                AlbumsGridActivity.audioRelative.setVisibility(View.GONE);
                AlbumsGridActivity.audioRelative.setEnabled(true);
            }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
                Log.e("selectedfragment", AlbumTabsActivity.selectedfragment + "");

                switch (AlbumTabsActivity.selectedfragment) {
                    case 0:
                        AlbumTab_Tracks_Activity.progBar.setVisibility(View.GONE);

                        AlbumTab_Tracks_Activity.audioRelative.setVisibility(View.GONE);
                        AlbumTab_Tracks_Activity.audioRelative.setEnabled(true);
                        AlbumTab_Tracks_Activity.selectessong = PlayerConstants.SONG_NUMBER;
                        AlbumTab_Tracks_Activity.myTrackadAdaptor.notifyDataSetChanged();
                        break;

                    case 1:
                        AlbumTab_Otheralbum_Activity.progBar.setVisibility(View.GONE);

                        AlbumTab_Otheralbum_Activity.audioRelative.setVisibility(View.GONE);
                        AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(true);
                        break;


                    case 2:
                        AlbumTab_Relatedalbum_Activity.progBar.setVisibility(View.GONE);

                        AlbumTab_Relatedalbum_Activity.audioRelative.setVisibility(View.GONE);
                        AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(true);
                        break;


                }
            }

            if (mpackagename
                    .equalsIgnoreCase("FriendsTabs.FriendsTabActivity")) {
                Log.i("selectedfragment", FriendsTabActivity.selectedfragment + "");

                switch (FriendsTabActivity.selectedfragment) {

                    case 0:
                        FriendTab_RecentlyplayedActivity.progBar.setVisibility(View.GONE);

                        FriendTab_RecentlyplayedActivity.audioRelative.setVisibility(View.GONE);

                        break;
                    case 1:
                        FriendTab_LikesActivity.progBar.setVisibility(View.GONE);

                        FriendTab_LikesActivity.audioRelative.setVisibility(View.GONE);
                        break;
                    case 2:
                        FriendTab_followArtActivity.progBar.setVisibility(View.GONE);

                        FriendTab_followArtActivity.audioRelative.setVisibility(View.GONE);
                        break;


                }
            }


            if (mpackagename
                    .equalsIgnoreCase("HomeActivity")) {
                HomeActivity.progBar.setVisibility(View.GONE);

                HomeActivity.audioRelative.setVisibility(View.GONE);
            }


            if (mpackagename
                    .equalsIgnoreCase("ArtistTabs.ArtistTabsActivity")) {
                switch (ArtistTabsActivity.selectedfragment) {
                    case 0:

                        ArtistTab_TopActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_TopActivity.audioRelative.setVisibility(View.GONE);
                        ArtistTab_TopActivity.myTrackadAdaptor.notifyDataSetChanged();

                        break;
                    case 1:
                        ArtistTab_AlbumsActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_AlbumsActivity.audioRelative.setVisibility(View.GONE);

                        break;
                    case 2:
                        ArtistTab_SimilarActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_SimilarActivity.audioRelative.setVisibility(View.GONE);

                        break;

                    case 3:

                        ArtistTab_SingleActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_SingleActivity.audioRelative.setVisibility(View.GONE);

                        ArtistTab_SingleActivity.myTrackadAdaptor.notifyDataSetChanged();
                        break;
                }
            }


            if (mpackagename
                    .equalsIgnoreCase("FavoritesActivity")) {
                FavoritesActivity.progBar.setVisibility(View.GONE);

                FavoritesActivity.audioRelative.setVisibility(View.GONE);
            }
            if (mpackagename
                    .equalsIgnoreCase("SearchTabs.SearchTabActivity")) {
                switch (SearchTabActivity.selectedfragment) {
                    case 0:
                        SearchTab_AllActivity.progBar.setVisibility(View.GONE);

                        SearchTab_AllActivity.audioRelative.setVisibility(View.GONE);
                        break;
                    case 1:
                        SearchTab_TracksActivity.progBar.setVisibility(View.GONE);

                        SearchTab_TracksActivity.audioRelative.setVisibility(View.GONE);
                        break;
                    case 2:
                        SearchTab_PlaylistActivity.progBar.setVisibility(View.GONE);

                        SearchTab_PlaylistActivity.audioRelative.setVisibility(View.GONE);

                        break;
                    case 3:
                        SearchTab_ArtistActivity.progBar.setVisibility(View.GONE);

                        SearchTab_ArtistActivity.audioRelative.setVisibility(View.GONE);

                        break;
                    case 4:
                        SearchTab_AlbumActivity.progBar.setVisibility(View.GONE);

                        SearchTab_AlbumActivity.audioRelative.setVisibility(View.GONE);


                        break;
                }
            }


            if (mpackagename
                    .equalsIgnoreCase("PlayerAcreenActivity")) {
                PlayerAcreenActivity.progBar.setVisibility(View.GONE);
                // MainActivity.audioRelative.setEnabled(false);
            }


            if (mpackagename
                    .equalsIgnoreCase("FavoritesRadio")) {
                FavoritesRadio.progBar.setVisibility(View.GONE);

                FavoritesRadio.audioRelative.setVisibility(View.GONE);
            }
        }
    }
}

