package com.binarywaves.ghaneely.ghannelyaodioplayer.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.binarywaves.ghaneely.R;

public class NotificationRadioUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.binarywaves.ghaneely.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    public NotificationRadioUtils(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannels();}
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {

        // create android channel
        @SuppressLint("WrongConstant") NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME,NotificationManager.IMPORTANCE_LOW);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(false);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(false);
        androidChannel.setSound(null, null);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);


    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }


    ////// For Song Service
    public Notification getAndroidNotification(String songName, RemoteViews expandedView, RemoteViews simpleContentView, String trackImage, Bitmap bitmap, boolean songPaused, String albumName) {
//        return new NotificationCompat.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).setSmallIcon(R.mipmap.playlistdrw).setChannelId(ANDROID_CHANNEL_ID)
//                .setContentText(songName) .setContentTitle(songName).setCustomBigContentView(expandedView).setCustomContentView(simpleContentView).build();
//
        if (RadioConstant.SONG_PAUSED) {

            expandedView.setViewVisibility(R.id.btnPause, View.GONE);
            expandedView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            simpleContentView.setViewVisibility(R.id.btnPause, View.GONE);
            simpleContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

            expandedView.setTextViewText(R.id.textSongName, songName);
            expandedView.setTextViewText(R.id.textAlbumName, albumName);
            simpleContentView.setTextViewText(R.id.textSongName, songName);
            simpleContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        else{

            expandedView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            expandedView.setViewVisibility(R.id.btnPlay, View.GONE);
            simpleContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            simpleContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            expandedView.setTextViewText(R.id.textSongName, songName);
            expandedView.setTextViewText(R.id.textAlbumName, albumName);
            simpleContentView.setTextViewText(R.id.textSongName, songName);
            simpleContentView.setTextViewText(R.id.textAlbumName, albumName);
        }

        if (trackImage.equalsIgnoreCase("")||bitmap==null) {
            expandedView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.defualt_img);
            simpleContentView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.defualt_img);
            return new NotificationCompat.Builder(getApplicationContext(),ANDROID_CHANNEL_ID ).setContentText(songName) .setSmallIcon(R.mipmap.playlistdrw)
                    .setContentTitle(songName).setCustomBigContentView(expandedView).setCustomContentView(simpleContentView).build();

        }
        else{
            expandedView.setImageViewBitmap(R.id.imageViewAlbumArt, bitmap);
            simpleContentView.setImageViewBitmap(R.id.imageViewAlbumArt, bitmap);

            return new NotificationCompat.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).setContentText(songName) .setSmallIcon(R.mipmap.playlistdrw)
                    .setChannelId(ANDROID_CHANNEL_ID).setContentTitle(songName).setCustomBigContentView(expandedView).setCustomContentView(simpleContentView).build();

        }

    }



    public Notification getAndroidNotification_notsuppbn(String songName, RemoteViews expandedView, RemoteViews simpleContentView, String trackImage, Bitmap bitmap, boolean songPaused, String albumName) {
        if (RadioConstant.SONG_PAUSED) {

            expandedView.setViewVisibility(R.id.btnPause, View.GONE);
            expandedView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            simpleContentView.setViewVisibility(R.id.btnPause, View.GONE);
            simpleContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

            expandedView.setTextViewText(R.id.textSongName, songName);
            expandedView.setTextViewText(R.id.textAlbumName, albumName);
            simpleContentView.setTextViewText(R.id.textSongName, songName);
            simpleContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        else{

            expandedView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            expandedView.setViewVisibility(R.id.btnPlay, View.GONE);
            simpleContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            simpleContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            expandedView.setTextViewText(R.id.textSongName, songName);
            expandedView.setTextViewText(R.id.textAlbumName, albumName);
            simpleContentView.setTextViewText(R.id.textSongName, songName);
            simpleContentView.setTextViewText(R.id.textAlbumName, albumName);
        }

        if (trackImage.equalsIgnoreCase("")||bitmap==null) {
            expandedView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.defualt_img);
            simpleContentView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.defualt_img);
            return new NotificationCompat.Builder(getApplicationContext()).setContentText(songName) .setSmallIcon(R.mipmap.playlistdrw)
                    .setContentTitle(songName).setCustomBigContentView(expandedView).setCustomContentView(simpleContentView).build();

        }
        else{
            expandedView.setImageViewBitmap(R.id.imageViewAlbumArt, bitmap);
            simpleContentView.setImageViewBitmap(R.id.imageViewAlbumArt, bitmap);

            return new NotificationCompat.Builder(getApplicationContext()).setContentText(songName) .setSmallIcon(R.mipmap.playlistdrw)
                    .setChannelId(ANDROID_CHANNEL_ID).setContentTitle(songName).setCustomBigContentView(expandedView).setCustomContentView(simpleContentView).build();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification getAndroidChannelNotification(String songName, RemoteViews expandedView, RemoteViews simpleContentView, String trackImage, Bitmap bitmap, boolean songPaused, String albumName) {
        if (RadioConstant.SONG_PAUSED) {

            expandedView.setViewVisibility(R.id.btnPause, View.GONE);
            expandedView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            simpleContentView.setViewVisibility(R.id.btnPause, View.GONE);
            simpleContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

            expandedView.setTextViewText(R.id.textSongName, songName);
            expandedView.setTextViewText(R.id.textAlbumName, albumName);
            simpleContentView.setTextViewText(R.id.textSongName, songName);
            simpleContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        else{

            expandedView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            expandedView.setViewVisibility(R.id.btnPlay, View.GONE);
            simpleContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            simpleContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            expandedView.setTextViewText(R.id.textSongName, songName);
            expandedView.setTextViewText(R.id.textAlbumName, albumName);
            simpleContentView.setTextViewText(R.id.textSongName, songName);
            simpleContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        if (trackImage.equalsIgnoreCase("")||bitmap==null) {
            expandedView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.defualt_img);
            simpleContentView.setImageViewResource(R.id.imageViewAlbumArt, R.mipmap.defualt_img);
            return new Notification.Builder(getApplicationContext(),ANDROID_CHANNEL_ID ).setContentText(songName) .setSmallIcon(R.mipmap.playlistdrw)
                    .setContentTitle(songName).setCustomBigContentView(expandedView).setCustomContentView(simpleContentView).build();

        }
        else{
            expandedView.setImageViewBitmap(R.id.imageViewAlbumArt, bitmap);
            simpleContentView.setImageViewBitmap(R.id.imageViewAlbumArt, bitmap);

            return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).setContentText(songName) .setSmallIcon(R.mipmap.playlistdrw)
                    .setChannelId(ANDROID_CHANNEL_ID).setContentTitle(songName).setCustomBigContentView(expandedView).setCustomContentView(simpleContentView).build();

        }





    }






}