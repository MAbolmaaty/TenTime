package com.binarywaves.ghaneely.ghannelyaodioplayer.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.binarywaves.ghaneely.BuildConfig;
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
import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;
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
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.receiver.NotificationBroadcast;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.NotificationUtils;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelyutils.Utils;
import com.danikula.videocache.CacheListener;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
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

import java.io.File;
import java.io.IOException;

import static com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager.Channel_id;

@TargetApi(Build.VERSION_CODES.O)
public class SongService extends Service implements AudioManager.OnAudioFocusChangeListener, CacheListener, EventListener {
    public static final String NOTIFY_PREVIOUS = "com.tutorialsface.audioplayer.previous";
    public static final String NOTIFY_DELETE = "com.tutorialsface.audioplayer.delete";
    public static final String NOTIFY_PAUSE = "com.tutorialsface.audioplayer.pause";
    public static final String NOTIFY_PLAY = "com.tutorialsface.audioplayer.play";
    public static final String NOTIFY_NEXT = "com.tutorialsface.audioplayer.next";
    public static final String NOTIFY_open = "com.tutorialsface.audioplayer.open";
    private NotificationBroadcast receiver;


    private static final String TAG = "TELEPHONESERVICE";
    public static ExoPlayer mp;
    public static boolean isRepeat = false;
    public static boolean isShuffle = false;
    public static int totalDuration;
    public static boolean isReady;
    private static boolean currentVersionSupportLockScreenControls = false;
    private static Bitmap bitmap;
    private static String songName;
    private static Notification notification;
    private static int mBufferPosition;
    // 96kbps*10secs/8bits
    // per byte
    private static int counter = 0;
    // public static Timer timer;
    private static boolean currentVersionSupportBigNotification = false;
    private static int percent;
    public static final int NOTIFICATION_ID = 1111;
    public Activity activity;
    String LOG_CLASS = "SongService";
    Bitmap mDummyAlbumArt;
    int amany;
    private RemoteViews simpleContentView;
    private RemoteViews expandedView;
    private AudioManager audioManager;
    private FileCache fileCache;
    private int progress;

    public static NotificationUtils mNotificationUtils;
    Notification nb;


    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".provider";
    private static int NOTIFY_ID = 1337;
    private static int FOREGROUND_ID = 1338;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            Log.d("test1", "test1");


            if (mp != null) {

                progress = (int) ((mp.getCurrentPosition() * 100) / totalDuration);
                Integer i[] = new Integer[3];
                i[0] = (int) mp.getCurrentPosition();
                i[1] = (int) mp.getDuration();
                i[2] = progress;
                if (PlayerConstants.PROGRESSBAR_HANDLER != null) {
                    try {
                        PlayerConstants.PROGRESSBAR_HANDLER
                                .sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    private String albumName;
    /**
     * Play song, Update Lockscreen fields
     *
     * @param songPath
     * @param data
     */
    private TrackObject data1;
    private String songPath1;
    private boolean isPausedInCall = false;
    private boolean intialStage;
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

    public static void updateVideoProgress() {
        if (PlayerAcreenActivity.seek_bar != null) {
            if (totalDuration > 0 && mp != null) {

                int videoProgress = (int) (mp.getCurrentPosition() * 100 / totalDuration);

                PlayerAcreenActivity.seek_bar.setProgress(videoProgress);
                PlayerAcreenActivity.seek_bar.setSecondaryProgress(getBufferPercentage());

            }
        }
    }

    public static void seekVideo() {
        if (mp != null && totalDuration > 0) {

            int videoPosition = (int) (mp.getDuration() * PlayerAcreenActivity.seek_bar.getProgress() / 100);
            mp.seekTo(videoPosition);
        }
    }

    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        Log.d("test2", "test2");
        //Code here
        //  NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationUtils.getManager().cancel(NOTIFICATION_ID);
        Log.d("test3", "test3");

        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("swdfsd", "tesdssdsdst2");
        mp.stop();
        mp.removeListener(this);
        return false;
    }

    @Override
    public void onCreate() {
        mNotificationUtils = new NotificationUtils(this);
        Log.d("wedwewe", "twewfeest2");

        if (mp == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl

            mp = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
            Log.d("ww", "testwww2");

            mp.removeListener(this);
            Log.d("test2ff", "tessst2");

        }
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        fileCache = new FileCache(getBaseContext());

        currentVersionSupportBigNotification = UtilFunctions.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = UtilFunctions.currentVersionSupportLockScreenControls();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            receiver = new NotificationBroadcast();
            IntentFilter filter = new IntentFilter();
            filter.addAction(NOTIFY_PREVIOUS);
            filter.addAction(NOTIFY_NEXT);
            filter.addAction(NOTIFY_DELETE);

            filter.addAction(NOTIFY_open);

            filter.addAction(NOTIFY_PAUSE);
            filter.addAction(NOTIFY_PLAY);


            registerReceiver(receiver, filter);
        }
        //Set mediaSession's MetaData
        super.onCreate();
    }

    private void play_option(TrackObject data) {
        if (data.getIsPremium() != null && data.getIsPremium().equalsIgnoreCase("true")) {
            if (DrawerActivity.dialogdimmed != null && !DrawerActivity.dialogdimmed.isShowing()) {
                if (!SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("1")) {
//popup appear when user not subscribe
                    play_TracknonPremuiem(data);


                    // DrawerActivity.show_Alert_Dimmed_Track(getApplicationContext());
                } else if (!SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("3")) {
//popup appear when user in grace
                    DrawerActivity.showGracePopup(getApplicationContext());
                    boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                            getApplicationContext());
                    if (isServiceRunning4) {
                        Intent i = new Intent(getApplicationContext(), SongService.class);
                        mNotificationUtils.getManager().cancel(NOTIFICATION_ID);
                        getApplicationContext().stopService(i);
                        Remove_handlers();

                    }
                    handleActivity_Error();


                } else {
                    DrawerActivity.show_Alert_Dimmed_Track(getApplicationContext());
                    boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                            getApplicationContext());
                    if (isServiceRunning4) {
                        Intent i = new Intent(getApplicationContext(), SongService.class);
                        mNotificationUtils.getManager().cancel(NOTIFICATION_ID);
                        getApplicationContext().stopService(i);
                        Remove_handlers();

                    }
                    handleActivity_Error();


                }

            }

        } else {
            play_TracknonPremuiem(data);
        }
    }

    private void play_TracknonPremuiem(TrackObject data) {
        String songPath = ServerConfig.AudioUrl + data.getTrackPath();

        Log.d("the song path is " , songPath);
        newNotification();

        playSong(songPath, data);

        PlayerConstants.SONG_CHANGE_HANDLER = new Handler(msg -> {


            TrackObject data2 = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
            String songPath2 = ServerConfig.AudioUrl + data2.getTrackPath();
            newNotification();
            try {
                playSong(songPath2, data2);
                changeButtonPlayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
        play_PauseHandler();


    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0) {
            TrackObject data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
            play_option(data);

            try {
                Intent notificationIntent = new Intent(this, HomeActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, 0);


                Notification notification = new NotificationCompat.Builder(this, Channel_id)
                      //  .setContentTitle("")
                        //.setContentText("")
                   //     .setSmallIcon(R.drawable.iconapp)
                        .setContentIntent(pendingIntent)
                        .build();

                startForeground(1, notification);

            } catch (Exception e) {
                Log.d("startForegroundssss", "" + e);
            }
        }

        return START_NOT_STICKY;
        //  return START_STICKY;

    }

    private void changeButtonPlayer() {
        HomeActivity.changeButton();
        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

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
                    .equalsIgnoreCase("ArtistradioActivity")) {
                ArtistradioActivity.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("InboxActivity")) {
                InboxActivity.changeUI1(getApplicationContext());
            }


            if (mpackagename
                    .equalsIgnoreCase("PlayListTracksActivity")) {
                PlayListTracksActivity.changeUI(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("MoodsActivity")) {
                MoodsActivity.changeUI(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("FavoritesRadio")) {
                FavoritesRadio.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("PlayListActivity")) {
                PlayListActivity.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsGridActivity")) {
                AlbumsGridActivity.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
                Log.i("selectedfragment", AlbumTabsActivity.selectedfragment + "");

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
                    .equalsIgnoreCase("FavoritesActivity")) {
                FavoritesActivity.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("HomeActivity")) {
                HomeActivity.changeUI1(getApplicationContext());
            }
            if (mpackagename
                    .equalsIgnoreCase("PlayerAcreenActivity")) {
                PlayerAcreenActivity.changeUI();
            }
        }
    }
    // HttpProxyCacheServer proxy = App.getProxy(getApplicationContext());

    /**
     * Notification Custom Bignotification is available from API 16
     */
    @SuppressWarnings("deprecation")

    @SuppressLint("NewApi")
    private void newNotification() {
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());
        if (language.equalsIgnoreCase("ar")) {
            songName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackArName();
            albumName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumArName();

        }
        if (language.equalsIgnoreCase("en")) {
            songName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackEnName();
            albumName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumEnName();

        }

        String final_imgpath = ServerConfig.Image_path
                + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage();
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
                        getAndroidChannelNotification(songName, expandedView, simpleContentView, PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage(), bitmap, PlayerConstants.SONG_PAUSED, albumName);


            } else {
                notification = mNotificationUtils.
                        getAndroidNotification(songName, expandedView, simpleContentView, PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage(), bitmap, PlayerConstants.SONG_PAUSED, albumName);
            }

        } else {
            notification = mNotificationUtils.getAndroidNotification_notsuppbn(songName, expandedView, simpleContentView, PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage(), bitmap, PlayerConstants.SONG_PAUSED, albumName);

        }


        if (nb != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nb.flags |= Notification.FLAG_ONGOING_EVENT;
            mNotificationUtils.getManager().notify(NOTIFICATION_ID, nb);
        } else {

            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            startForeground(NOTIFICATION_ID, notification);
        }


        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackagename
                    .equalsIgnoreCase("PlayListTracksActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    PlayListTracksActivity.progBar.setVisibility(View.GONE);
                    PlayListTracksActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    PlayListTracksActivity.startwhellprogress();
                    PlayListTracksActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    PlayListTracksActivity.startwhellprogress();
                    PlayListTracksActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("FriendsTabs.FriendsTabActivity")) {
                Log.i("selectedfragment", FriendsTabActivity.selectedfragment + "");

                switch (FriendsTabActivity.selectedfragment) {

                    case 0:

                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            FriendTab_RecentlyplayedActivity.progBar.setVisibility(View.GONE);
                            FriendTab_RecentlyplayedActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            FriendTab_RecentlyplayedActivity.startwhellprogress();
                            FriendTab_RecentlyplayedActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            FriendTab_RecentlyplayedActivity.startwhellprogress();
                            FriendTab_RecentlyplayedActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }

                        break;
                    case 1:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            FriendTab_LikesActivity.progBar.setVisibility(View.GONE);
                            FriendTab_LikesActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            FriendTab_LikesActivity.startwhellprogress();
                            FriendTab_LikesActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            FriendTab_LikesActivity.startwhellprogress();
                            FriendTab_LikesActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 2:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            FriendTab_followArtActivity.progBar.setVisibility(View.GONE);
                            FriendTab_followArtActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            FriendTab_followArtActivity.startwhellprogress();
                            FriendTab_followArtActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            FriendTab_followArtActivity.startwhellprogress();
                            FriendTab_followArtActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;


                }
            }


            if (mpackagename
                    .equalsIgnoreCase("InboxActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    InboxActivity.progBar.setVisibility(View.GONE);
                    InboxActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    InboxActivity.startwhellprogress();
                    InboxActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    InboxActivity.startwhellprogress();
                    InboxActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }


            if (mpackagename
                    .equalsIgnoreCase("FavoritesRadio")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    FavoritesRadio.progBar.setVisibility(View.GONE);
                    FavoritesRadio.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    FavoritesRadio.startwhellprogress();
                    FavoritesRadio.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    FavoritesRadio.startwhellprogress();
                    FavoritesRadio.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("ArtistradioActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    ArtistradioActivity.progBar.setVisibility(View.GONE);
                    ArtistradioActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    ArtistradioActivity.startwhellprogress();
                    ArtistradioActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    ArtistradioActivity.startwhellprogress();
                    ArtistradioActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("PlayListActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    PlayListActivity.progBar.setVisibility(View.GONE);
                    PlayListActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    PlayListActivity.startwhellprogress();
                    PlayListActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    PlayListActivity.startwhellprogress();
                    PlayListActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }


            //
            if (mpackagename
                    .equalsIgnoreCase("SearchTabs.SearchTabActivity")) {
                switch (SearchTabActivity.selectedfragment) {
                    case 0:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_AllActivity.progBar.setVisibility(View.GONE);
                            SearchTab_AllActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_AllActivity.startwhellprogress();
                            SearchTab_AllActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            SearchTab_AllActivity.startwhellprogress();
                            SearchTab_AllActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 1:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_TracksActivity.progBar.setVisibility(View.GONE);
                            SearchTab_TracksActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_TracksActivity.startwhellprogress();
                            SearchTab_TracksActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            SearchTab_TracksActivity.startwhellprogress();
                            SearchTab_TracksActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 2:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_PlaylistActivity.progBar.setVisibility(View.GONE);
                            SearchTab_PlaylistActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_PlaylistActivity.startwhellprogress();
                            SearchTab_PlaylistActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            SearchTab_PlaylistActivity.startwhellprogress();
                            SearchTab_PlaylistActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 3:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_ArtistActivity.progBar.setVisibility(View.GONE);
                            SearchTab_ArtistActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_ArtistActivity.startwhellprogress();
                            SearchTab_ArtistActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            SearchTab_ArtistActivity.startwhellprogress();
                            SearchTab_ArtistActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 4:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_AlbumActivity.progBar.setVisibility(View.GONE);
                            SearchTab_AlbumActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            SearchTab_AlbumActivity.startwhellprogress();
                            SearchTab_AlbumActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            SearchTab_AlbumActivity.startwhellprogress();
                            SearchTab_AlbumActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("HomeActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    HomeActivity.progBar.setVisibility(View.GONE);
                    HomeActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    HomeActivity.startwhellprogress();
                    HomeActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    HomeActivity.startwhellprogress();
                    HomeActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }
            if (mpackagename
                    .equalsIgnoreCase("ArtistTabs.ArtistTabsActivity")) {
                switch (ArtistTabsActivity.selectedfragment) {
                    case 0:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_TopActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_TopActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_TopActivity.startwhellprogress();
                            ArtistTab_TopActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            ArtistTab_TopActivity.startwhellprogress();
                            ArtistTab_TopActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 1:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_AlbumsActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_AlbumsActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_AlbumsActivity.startwhellprogress();
                            ArtistTab_AlbumsActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            ArtistTab_AlbumsActivity.startwhellprogress();
                            ArtistTab_AlbumsActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 2:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_SimilarActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_SimilarActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_SimilarActivity.startwhellprogress();
                            ArtistTab_SimilarActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            ArtistTab_SimilarActivity.startwhellprogress();
                            ArtistTab_SimilarActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;

                    case 3:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_SingleActivity.progBar.setVisibility(View.GONE);
                            ArtistTab_SingleActivity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            ArtistTab_SingleActivity.startwhellprogress();
                            ArtistTab_SingleActivity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            ArtistTab_SingleActivity.startwhellprogress();
                            ArtistTab_SingleActivity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("FavoritesActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    FavoritesActivity.progBar.setVisibility(View.GONE);
                    FavoritesActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    FavoritesActivity.progBar.setVisibility(View.VISIBLE);
                    FavoritesActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    FavoritesActivity.progBar.setVisibility(View.VISIBLE);
                    FavoritesActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }

            if (mpackagename
                    .equalsIgnoreCase("AlbumsGridActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    AlbumsGridActivity.progBar.setVisibility(View.GONE);
                    AlbumsGridActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    AlbumsGridActivity.progBar.setVisibility(View.VISIBLE);
                    AlbumsGridActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    AlbumsGridActivity.progBar.setVisibility(View.VISIBLE);
                    AlbumsGridActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;
                }
            }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
                Log.e("selectedfragment", AlbumTabsActivity.selectedfragment + "");

                switch (AlbumTabsActivity.selectedfragment) {
                    case 0:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            if (AlbumTab_Tracks_Activity.progBar != null && AlbumTab_Tracks_Activity.audioRelative != null) {
                                AlbumTab_Tracks_Activity.progBar.setVisibility(View.GONE);
                                AlbumTab_Tracks_Activity.audioRelative.setEnabled(true);
                            }
                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {

                            AlbumTab_Tracks_Activity.startwhellprogress();
                            AlbumTab_Tracks_Activity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            AlbumTab_Tracks_Activity.startwhellprogress();
                            AlbumTab_Tracks_Activity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 1:

                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            AlbumTab_Otheralbum_Activity.progBar.setVisibility(View.GONE);
                            AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            AlbumTab_Otheralbum_Activity.startwhellprogress();
                            AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            AlbumTab_Otheralbum_Activity.startwhellprogress();
                            AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                    case 2:
                        Remove_handlers();

                        if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                            AlbumTab_Relatedalbum_Activity.progBar.setVisibility(View.GONE);
                            AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(true);

                        }

                        if (!isReady && !PlayerConstants.SONG_CHANGED) {
                            AlbumTab_Relatedalbum_Activity.startwhellprogress();
                            AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(false);

                        }
                        if (PlayerConstants.SONGnext) {
                            AlbumTab_Relatedalbum_Activity.startwhellprogress();
                            AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(false);
                            PlayerConstants.SONGnext = false;
                        }
                        break;
                }
            }


            if (mpackagename
                    .equalsIgnoreCase("com.binaywaves.ghaneely.ghannelyactivites.MainActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    HomeActivity.progBar.setVisibility(View.GONE);
                    HomeActivity.audioRelative.setEnabled(true);

                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    HomeActivity.startwhellprogress();
                    HomeActivity.audioRelative.setEnabled(false);

                }
                if (PlayerConstants.SONGnext) {
                    HomeActivity.startwhellprogress();
                    HomeActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;

                }

            }


            if (mpackagename
                    .equalsIgnoreCase("PlayerAcreenActivity")) {
                Remove_handlers();

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    PlayerAcreenActivity.startwhellprogress();

                }
                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    PlayerAcreenActivity.progBar.setVisibility(View.GONE);

                }
                if (PlayerConstants.SONGnext) {
                    PlayerAcreenActivity.startwhellprogress();
                    PlayerConstants.SONGnext = false;

                } // MainActivity.audioRelative.setEnabled(false);
            }


            if (mpackagename
                    .equalsIgnoreCase("MoodsActivity")) {
                Remove_handlers();

                if (PlayerConstants.SONG_PAUSED && !PlayerConstants.SONG_CHANGED) {
                    MoodsActivity.progBar.setVisibility(View.GONE);
                    MoodsActivity.audioRelative.setEnabled(true);
                    MoodsActivity.gridview.setEnabled(true);


                }

                if (!isReady && !PlayerConstants.SONG_CHANGED) {
                    MoodsActivity.startwhellprogress();
                    MoodsActivity.audioRelative.setEnabled(false);
                }
                if (PlayerConstants.SONGnext) {
                    MoodsActivity.startwhellprogress();
                    MoodsActivity.audioRelative.setEnabled(false);
                    PlayerConstants.SONGnext = false;

                }
            }

        }
    }

    private void Remove_handlers() {
        // TODO Auto-generated method stub
        Log.d("teswfwefwetdd2", "wefwefewsssfew");

        handler.removeCallbacksAndMessages(PlayerConstants.SONG_CHANGE_HANDLER);
        //      mNotificationUtils.getManager().cancel(NOTIFICATION_ID);
        Log.d("teswfwefwet2", "wefwefewfew");


    }

    private void setListeners(RemoteViews view) {
        Remove_handlers();
        if (mp != null && isReady && PlayerConstants.SONGnext && PlayerConstants.SONG_PAUSED) {
            mp.removeListener(this);
        }

        if (mp != null && PlayerConstants.SONGnext) {
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

    @Override
    public void onDestroy() {
        stopForeground(true);
        mNotificationUtils.getManager().cancel(NOTIFICATION_ID);
        Log.d("teswfweaaafwet2", "wefwefedcwfew");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            unregisterReceiver(receiver);
            Log.d("as", "dcx");

        }
        if (mp != null) {
            mp.release();
            mp.removeListener(this);
            Log.d("zx", "xa");

            mp = null;
            Log.d("aa", "aa");

        }
        if (mMediaSessionCompat != null)
            mMediaSessionCompat.release();
        audioManager.abandonAudioFocus(this);
        Log.d("A", "AD");


        super.onDestroy();
    }

    /*
    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }
    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "VideoStreaming"), bandwidthMeter);
    }
    */
    @SuppressLint("NewApi")
    private void playSong(String songPath, final TrackObject data) {

        data1 = data;
        songPath1 = songPath;
        Remove_handlers();
        if (mp != null) {
            mp.removeListener(this);


            mp.addListener(this);
        }

        String str;
        str = songPath.replaceAll(" ", "%20");
        try {
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ExoPlayerDemo");

            MediaSource audio = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(str));
            mp.prepare(audio);
            mp.setPlayWhenReady(true);

// Prepare the player with the source.
//            // Measures bandwidth during playback. Can be null if not required.
//            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//// Produces DataSource instances through which media data is loaded.
//            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
//                    Util.getUserAgent(this, "ExoPlayer"), bandwidthMeter);
//// This is the MediaSource representing the media to be played.
//            HlsMediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory)
//                    .createMediaSource(Uri.parse("http://35.233.26.252:8081/crt/61574_AllahumAedAlinaRamad.mp4/playlist.m3u8"));
//// Prepare the player with the source.
//            mp.prepare(videoSource);
//            mp.setPlayWhenReady(true);

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
        assert audioManager != null;
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
    private void updateMediaSessionMetaData(TrackObject data) {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, data.getSingerEnName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, data.getAlbumEnName());
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, data.getAlbumEnName());
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
                        //   mp.setPlayWhenReady(false);
                        Controls.pauseControl(getApplicationContext());

                        changeButtonPlayer();
                    }
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                    mp.setPlayWhenReady(false);
                    Controls.pauseControl(getApplicationContext());

                    changeButtonPlayer();
                    break;
                }
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {

                    break;
                }
                case AudioManager.AUDIOFOCUS_GAIN: {
                    if (mp != null && !mp.getPlayWhenReady()) {
                        mp.setPlayWhenReady(false);
                        Log.d("teswfwefwet2", "wefwefewfew");

                    }
                    if (mp != null && mp.getPlayWhenReady()) {
                        isPausedInCall = false;
                        mp.setPlayWhenReady(true);


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
    public void onCacheAvailable(File file, String url, int percentsAvailable) {
        counter = percentsAvailable;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray
            trackSelections) {

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
                TrackLisinterService tracklisiten = new TrackLisinterService(getApplicationContext(), getApplication());
                tracklisiten.settracklisten(totalDuration / 1000);
                mp.removeListener(this);
                Remove_handlers();
                if (isRepeat) {
                    Controls.repeatControl(getApplicationContext());
                }
                if (isShuffle) {
                    Controls.shuffleControl(getApplicationContext());

                }
                if (!isRepeat && !isShuffle) {

                    Controls.nextControl(getApplicationContext());
                }


            }
        }

        if (playbackState == Player.STATE_READY) {
            if (mp.getPlayWhenReady()) {
                mp.setPlayWhenReady(true);


                isReady = true;
                totalDuration = (int) mp.getDuration();
                Log.d("mono", totalDuration + "");
                TrackLisinterService.start = true;
                if (currentVersionSupportLockScreenControls) {
                    mMediaSessionCompat.setActive(true);
                    updateMediaSessionMetaData(data1);
                    mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, getCurrentPosition(), 1.0f)
                            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS).build());
                }


                if (getActivity() != null) {
                    String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");
                    Utils.handle_ScreenViews(mpackagename);
                }


                if (mp.getCurrentPosition() > 0) {
                    handler.sendEmptyMessage(0);
                }


                if (data1.getIsPremium() != null && data1.getIsPremium().equalsIgnoreCase("true")) {
                    if (DrawerActivity.dialogdimmed != null && !DrawerActivity.dialogdimmed.isShowing()) {
                        if (!SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("1")) {
//popup appear when user not subscribe

                            // DrawerActivity.show_Alert_Dimmed_Track(getApplicationContext());
                        } else if (!SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("3")) {
//popup appear when user in grace
                            DrawerActivity.showGracePopup(getApplicationContext());

                        } else {
                            DrawerActivity.show_Alert_Dimmed_Track(getApplicationContext());
                        }

                    }

                }
            } else {

//
                mp.setPlayWhenReady(false);
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
                Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage() + "");

                Handle_error();
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage() + "");
                // Toast.makeText(getApplicationContext(), error.getSourceException().getMessage(), Toast.LENGTH_LONG).show();
                Handle_error();

                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage() + "");
                // Toast.makeText(getApplicationContext(), error.getSourceException().getMessage(), Toast.LENGTH_LONG).show();
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
        if (data1.getIsPremium() != null && data1.getIsPremium().equalsIgnoreCase("true")) {
            if (DrawerActivity.dialogdimmed != null && !DrawerActivity.dialogdimmed.isShowing()) {
                if (!SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("1")) {
//popup appear when user not subscribe

                    // DrawerActivity.show_Alert_Dimmed_Track(getApplicationContext());
                } else if (!SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("3")) {
//popup appear when user in grace
                    DrawerActivity.showGracePopup(getApplicationContext());

                } else {
                    DrawerActivity.show_Alert_Dimmed_Track(getApplicationContext());
                }

            }
        }
        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning4) {
            Intent i = new Intent(getApplicationContext(), SongService.class);
            mNotificationUtils.getManager().cancel(NOTIFICATION_ID);
            getApplicationContext().stopService(i);
            Remove_handlers();

        }
        handleActivity_Error();

    }

    private void handleActivity_Error() {
        if (getActivity() != null) {
            String mpackagename = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackagename
                    .equalsIgnoreCase("PlayListTracksActivity")) {

                PlayListTracksActivity.progBar.setVisibility(View.GONE);

                PlayListTracksActivity.audioRelative.setVisibility(View.GONE);
                PlayListTracksActivity.audioRelative.setEnabled(false);
            }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsGridActivity")) {

                AlbumsGridActivity.progBar.setVisibility(View.GONE);

                AlbumsGridActivity.audioRelative.setVisibility(View.GONE);
                AlbumsGridActivity.audioRelative.setEnabled(true);
            }
            if (mpackagename
                    .equalsIgnoreCase("MoodsActivity")) {
                MoodsActivity.progBar.setVisibility(View.GONE);

                MoodsActivity.audioRelative.setVisibility(View.GONE);
                MoodsActivity.audioRelative.setEnabled(true);
            }
            if (mpackagename
                    .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
                Log.e("selectedfragment", AlbumTabsActivity.selectedfragment + "");

                switch (AlbumTabsActivity.selectedfragment) {
                    case 0:
                        AlbumTab_Tracks_Activity.progBar.setVisibility(View.GONE);

                        AlbumTab_Tracks_Activity.audioRelative.setVisibility(View.GONE);
                        AlbumTab_Tracks_Activity.audioRelative.setEnabled(true);
                        AlbumTab_Tracks_Activity.selectessong = -1;
                        AlbumTab_Tracks_Activity.myTrackadAdaptor.notifyDataSetInvalidated();
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
                        FriendTab_RecentlyplayedActivity.selectessong = -1;
                        FriendTab_RecentlyplayedActivity.myTrackadAdaptor.notifyDataSetInvalidated();

                        break;
                    case 1:
                        FriendTab_LikesActivity.progBar.setVisibility(View.GONE);

                        FriendTab_LikesActivity.audioRelative.setVisibility(View.GONE);
                        FriendTab_LikesActivity.selectessong = -1;
                        FriendTab_LikesActivity.myTrackadAdaptor.notifyDataSetInvalidated();
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
                HomeActivity.audioRelative.setEnabled(true);
                if (HomeActivity.ListTrackadAdaptor != null) {

                    HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
                }
                if (HomeActivity.topTwentyAdaptor != null) {
                    HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
                }
            }


            if (mpackagename
                    .equalsIgnoreCase("ArtistTabs.ArtistTabsActivity")) {
                switch (ArtistTabsActivity.selectedfragment) {
                    case 0:

                        ArtistTab_TopActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_TopActivity.audioRelative.setVisibility(View.GONE);
                        ArtistTab_TopActivity.selectessong = -1;

                        ArtistTab_TopActivity.myTrackadAdaptor.notifyDataSetInvalidated();

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
                        ArtistTab_SingleActivity.selectessong = -1;

                        ArtistTab_SingleActivity.myTrackadAdaptor.notifyDataSetInvalidated();
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


    private void play_PauseHandler() {
        if (currentVersionSupportLockScreenControls) {
            RegisterRemoteClient();

            mMediaSessionCompat.setActive(true);
            updateMediaSessionMetaData(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER));
        }

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
                changeButtonPlayer();

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
                        if (PlayerConstants.SONGS_LIST.size() > 0) {
                            newNotification();
                        }
                        break;
                }
            }

        };
        // Register the listener with the telephony manager
        if (telephonyManager != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

/*
    Notification notification = new Notification.Builder(mContextt, NotificationMyChannel.ID).setContentIntent(pendingactivity)
            .setSmallIcon(R.mipmap.radio1)
            .setContentTitle(MainActivity.Rname)
            .setStyle(new Notification.DecoratedMediaCustomViewStyle())
            .setCustomContentView(remoteViews)
            .build();
    startForeground(1, notification);
    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContextt);
        notificationManagerCompat.notify(1, notification);
*/

    private void raiseNotification(Intent inbound, File output,
                                   Exception e) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis());

        if (e == null) {
            b.setContentTitle(getString(R.string.album_title))
                    .setContentText(getString(R.string.albumsearch))
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setTicker(getString(R.string.albumsearch));

            Intent outbound = new Intent(Intent.ACTION_VIEW);
            Uri outputUri =
                    FileProvider.getUriForFile(this, AUTHORITY, output);

            outbound.setDataAndType(outputUri, inbound.getType());
            outbound.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    outbound, PendingIntent.FLAG_UPDATE_CURRENT);

            b.setContentIntent(pi);
        } else {
            b.setContentTitle(getString(R.string.albumsearch))
                    .setContentText(e.getMessage())
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setTicker(getString(R.string.albumsearch));
        }

        NotificationManager mgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mgr.notify(NOTIFY_ID, b.build());
    }

    private Notification buildForegroundNotification(String filename) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setOngoing(true)
                .setContentTitle(getString(R.string.albumsearch))
                .setContentText(filename)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker(getString(R.string.albumsearch));

        return (b.build());
    }

}

