package com.firebaseService;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("deprecation")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String ANDROID_CHANNEL_ID;
    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager notificationManager;
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    private String pushid;
    private String MsgType;
    private String message;
    private String date;
    private NotificationCompat.Builder notification;
    Notification nb;
    private Uri defaultSoundUri;
    long[] vibrate;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = s;
        Log.d("FCN TOKEN GET", "Refreshed token: " + refreshedToken);

                 //   sendRegistrationToServer(refreshedToken);
        SharedPrefHelper.setSharedString(getApplicationContext(), Constants.tokenfcm, refreshedToken);

    }




    private void sendRegistrationToServer(final String token) {
        // You can implement this method to store the token on your server
        // Not required for current project
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);


        Api_Interface service = ServiceGenerator.createService();
        service.UpdateUserRegID(token, prefs.getString(Constants.USERID, ""), prefs.getString(Constants.accesstoken, "")).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    if (token != null) {
                        SharedPrefHelper.setSharedString(getApplicationContext(), Constants.tokenfcm, token);
                    }
                } else {
                    ApiUtils.handelErrorCode(getApplicationContext(), mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {

            }
        });


    }


    @SuppressWarnings("unused")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();


        }
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        message = remoteMessage.getData().get("Message");
        pushid = remoteMessage.getData().get("TrackId");
        MsgType = remoteMessage.getData().get("MsgType");
        date = remoteMessage.getData().get("NotifDate");


        //Calling method to generate notification
        sendNotification(message, pushid, MsgType, date);
        //  handleNotificationType(Integer.parseInt(RideStatus));
    }

    private void sendNotification(String msg, String pushid2, String messageType2, String Date) {

        Intent backIntent = new Intent(getApplicationContext(), HomeActivity.class);
        backIntent.putExtra("id", pushid2);
        backIntent.putExtra("messagetype", messageType2);

        Intent myintent = new Intent(getApplicationContext(), HomeActivity.class);
        myintent.putExtra("id", pushid2);
        myintent.putExtra("messagetype", messageType2);
        myintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivities(getApplicationContext(), new java.util.Random().nextInt(),
                new Intent[]{backIntent, myintent}, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notificationlayout);

        remoteViews.setTextViewText(R.id.tvcontent, msg);
        remoteViews.setTextViewText(R.id.tvadte, Date);

        remoteViews.setOnClickPendingIntent(R.id.listlay, contentIntent);
        boolean currentVersionSupportBigNotification = UtilFunctions.currentVersionSupportBigNotification();
        defaultSoundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        vibrate = new long[]{0, 100, 200, 300};

        if (currentVersionSupportBigNotification) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              ///  createChannels();
                nb = getAndroidChannelNotification(remoteViews);


            } else {
                notification =
                        new NotificationCompat.Builder(this, "")
                                .setSmallIcon(R.mipmap.icon1).setContentText("")
                                .setContent(remoteViews).setAutoCancel(true).setSound(defaultSoundUri).setVibrate(vibrate);

            }
        } else {
            notification =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.icon1)
                            .setContent(remoteViews).setAutoCancel(true).setSound(defaultSoundUri).setVibrate(vibrate);

        }


        notificationManager = getManager();
        if (nb != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationManager.notify(0, nb);
        } else {



            notificationManager.notify(0, notification.build());
        }


    }


    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @SuppressWarnings("unused")
    public void cancelNotification() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification getAndroidChannelNotification(RemoteViews remoteViews) {
//        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).setSmallIcon(R.mipmap.icon1).setChannelId(ANDROID_CHANNEL_ID)
//                .setContent(remoteViews) .setSound(defaultSoundUri).setVibrate(vibrate).setAutoCancel(true);
        return   new Notification.Builder(getApplicationContext(),ANDROID_CHANNEL_ID ).setChannelId(ANDROID_CHANNEL_ID) .setSmallIcon(R.mipmap.playlistdrw)
            .setCustomBigContentView(remoteViews).setCustomContentView(remoteViews).build();

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels () {

        // create android channel
         ANDROID_CHANNEL_ID = getApplicationContext().getString(R.string.default_notification_channel_id);
        @SuppressLint("WrongConstant") NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(false);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);

        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);


    }


}
