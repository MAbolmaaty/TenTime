package com.binarywaves.ghaneely.ghannelymodels;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.NetworkChangeReceiver;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistradioActivity;
import com.binarywaves.ghaneely.ghannelyactivites.LoginScreen;
import com.binarywaves.ghaneely.ghannelyactivites.RegisterActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Constants {
    public static final String LOG_TAG = "Ghaneely MOBILE APP ";
    public static final String SIM_NO = "simno";
    public static final String IMEI = "imei";
    public static final String ISREGISTERD = "isregisterd";
    public static final String USERNAME = "name";
    public static final String MOBILE = "msisdn";
    public static final String accesstoken = "accesstoken";
    public static final String isFaceReg = "isFaceReg";
    public static final String facebookId = "facebookId";
    public static final String imagefb = "imagefb";
    public static final String session = "session";
    public static final String islogout = "islogout";
    public static final String tokenfcm = "";
    public static final String PROPERTY_APP_VERSION = "PROPERTY_APP_VERSION";
    public static final String UserStatusId = "UserStatusId";
    public static final String ServiceID = "ServiceID";
    public static final String GenreId = "GenreId";
    public static final String FirstTimeTODownload = "FirstTimeTODownload";

    public static final String DATE_SUBSCRIBE = "DATE_SUBSCRIBE";
    public static final String DATE_LASTLOGIN = "DATE_LASTLOGIN";


    public static final String ServiceDailyPrice = "ServiceDailyPrice";
    public static final String ServiceMonthlyPrice = "ServiceMonthlyPrice";
    public static final String AllowInternational = "AllowInternational";


    public static final String USERID = "userid";
    public static final String PASSWORD = "password";
    public static final String DownloadKey = "DownloadKey";
    public static boolean NOT_EXIT_FLAG = false;
    public static String UserImage = "UserImage";
    private static ArrayList<SliderAds> Popupads;
    private static BitmapDrawable finalbitmap;
    private static Addfavourite_Response mainresponse;

/*
    /////////////////////twitter//////////////

	 static String TWITTER_CONSUMER_KEY = "xqrQv9RMMSUAU7zHv2rQ";
	    static String TWITTER_CONSUMER_SECRET = "oMrCLFq7VX9NhyBXqbQxdlXpIaISWErbIOrIAHUCEA";

	    // Preference Constants
	    static String PREFERENCE_NAME = "twitter_oauth";
	    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	    // Twitter oauth urls
	    static final String URL_TWITTER_AUTH = "auth_url";
	    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";


*/


    ////////////////////////////////////////
    private static BroadcastReceiver mNetworkReceiver;
    private static String response;
    private static String finalresponse;
    private static Addfavourite_Response Responsefav;

    public static boolean
    isNetworkOnline(Context context) {

        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(NetworkChangeReceiver.class.getName(),
                context);
        if (!isServiceRunning2) {
            Intent i = new Intent(context, NetworkChangeReceiver.class);
            context.startService(i);


        }
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static void Add_Removefavourite(String trackid2, final Context context2, final String favourite) {

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddFavouriteTrack + "?trackId=" + trackid2 + "&userId="
                + SharedPrefHelper.getSharedString(context2, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context2, Constants.accesstoken)
                + "&isFavourite=" + favourite;
        String fav_url = fav.replaceAll(" ", "%20");

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddFavouriteTrack(fav_url).enqueue(new Callback<Addfavourite_Response>() {

            @Override
            public void onResponse(@NonNull Call<Addfavourite_Response> call, @NonNull Response<Addfavourite_Response> mresult) {


                if (mresult.isSuccessful()) {
                    //convert json string to object
                    ArtistradioActivity.HandleFavUi(mresult.body(), favourite);

                } else {
                    ApiUtils.handelErrorCode(context2, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addfavourite_Response> call, @NonNull Throwable t) {

            }
        });
    }

    public static TrackObject Convertto_Track() {
        TrackObject mTrack = new TrackObject();
        if(PlayerConstants.SONGS_LIST!=null&&PlayerConstants.SONGS_LIST.size()>0) {
            mTrack.setAlbumId(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumId());
            mTrack.setTrackId(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId());
            mTrack.setTrackEnName(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackEnName());
            mTrack.setAlbumEnName(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumEnName());
            mTrack.setSingerEnName(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingerEnName());
            mTrack.setTrackArName(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackArName());
            mTrack.setAlbumArName(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumArName());
            mTrack.setSingerArName(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingerArName());
            mTrack.setTrackImage(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage());
            mTrack.setTrackPath(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackPath());
            mTrack.setIsFavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite());
            mTrack.setSingerId(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingerId());
            mTrack.setLikesCount(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getLikesCount());
            mTrack.setIsRBT(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsRBT());
            mTrack.setListenCount(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getListenCount());
            mTrack.setHasLyrics(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getHasLyrics());
            mTrack.setLyricFile(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getLyricFile());
            mTrack.setTrackLength(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackLength());
            mTrack.setVideoID(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getVideoID());
            mTrack.setIsPremium(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsPremium());
            mTrack.setIsDownloaded(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsDownloaded());
        }
        return mTrack;
    }

    public static void Adsclick(String string, final Context context) {

        // Constants.SERVER_URl + Constants.REGISTER_PATH
        SharedPreferences prefs = context.getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddAdsHit + "?adsId=" + string + "&userId="
                + prefs.getString(Constants.USERID, "") + "&UserToken=" + prefs.getString(Constants.accesstoken, "");
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddAdsHit(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(Call<Addrbtresponse> call, Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(Call<Addrbtresponse> call, Throwable t) {

            }
        });

    }


    public static BitmapDrawable SetBackgroundBlurry(BitmapDrawable bm) {


        return bm;
    }

    private static double SumDurationValue(ArrayList<TrackObject> list) {

        double total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            total = total + Double.parseDouble(list.get(i).getTrackLength());
        }
        return total;
    }

    public static String getDurationString(ArrayList<TrackObject> list) {
        String str = "";
        String strsec = "";
        String strhou = "";
        Double seconds;
        Double hours = SumDurationValue(list) / 3600;
        Double minutes = (SumDurationValue(list) % 3600) / 60;
        seconds = SumDurationValue(list) % 60;

        str = Double.valueOf(minutes).toString().substring(0, Double.toString(minutes).indexOf('.'));
        double v = Double.valueOf(str);

        strsec = Double.valueOf(seconds).toString().substring(0, Double.toString(seconds).indexOf('.'));
        double vstrsec = Double.valueOf(strsec);


        strhou = Double.valueOf(hours).toString().substring(0, Double.toString(hours).indexOf('.'));
        double hou = Double.valueOf(strhou);

        return twoDigitString((int) hou) + ":" + twoDigitString((int) v) + ":" + twoDigitString((int) vstrsec);
    }

    private static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }


    public static BitmapDrawable drawableFromUrl(Context context, String url) throws IOException {
        Bitmap x;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        final int REQUIRED_SIZE = 100;

        int width_tmp = options.outWidth, height_tmp = options.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        x = BitmapFactory.decodeStream(input);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        x.compress(Bitmap.CompressFormat.JPEG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()), null, o2);
        finalbitmap = new BitmapDrawable(context.getResources(), decoded);
        input.close();
        connection.disconnect();
        return finalbitmap;
    }

    public static void GetHeaderInrech(Context context) {
        String fav = ServerConfig.Header_Url;
        String fav_url = fav.replaceAll(" ", "%20");

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url+"");

        service.GetheaderInrch(fav_url).enqueue(new Callback<Addrbtresponse>() {
            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    Log.i("headerinrech", mresult.body().toString()+"");
                    response = mresult.body().getResultCode().toString();

                    if (response != null && response.length() > 1) {
                        finalresponse = response.substring(1, response.length());

                    }

                    //convert json string to object
                    if (finalresponse != null && !finalresponse.equalsIgnoreCase("")) {
                        if (RegisterActivity.mobilenumber_et != null) {
                            RegisterActivity.mobilenumber_et.setText(finalresponse);
                        }


                        if (LoginScreen.mobilenumber_et != null) {
                            LoginScreen.mobilenumber_et.setText(finalresponse);
                        }
                    }

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("\"onFailure\"");

                }
            }

            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                System.out.println("onFailure");
                System.out.println(t.getMessage()+"\"onFailure\"");


            }

        });
    }

}