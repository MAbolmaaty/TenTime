package com.binarywaves.ghaneely.ghannelyservice;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SliderAds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 20-Jul-17.
 */

public class AdsPopUpService extends Service {

    public static Timer timer;
    public static boolean start = true;
    public static ArrayList<SliderAds> Popupads;
    //15 min
    private static long UPDATE_INTERVAL = 900000;
    //   990000; // default
    protected Application application;
    private SliderAds PopupadsObject;

    @Override
    public void onCreate() {
        super.onCreate();


    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Google", "Service Started");

        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(

                new TimerTask() {

                    public void run() {
                        new Handler(Looper.getMainLooper()).post(() -> {


                            if (Applicationmanager.dialogPoPAds != null) {
                                if (!Applicationmanager.dialogPoPAds.isShowing()) {
                                    ShowPoPAds();
                                }

                            } else {

                                Applicationmanager.CreateNewDialog();
                                if (Applicationmanager.dialogPoPAds != null) {
                                    if (!Applicationmanager.dialogPoPAds.isShowing()) {
                                        ShowPoPAds();
                                    }

                            }}
                        });


                    }
                }, 600000, UPDATE_INTERVAL);
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void ShowPoPAds() {

        if (Applicationmanager.dialogPoPAds != null) {

            Applicationmanager.dialogPoPAds.setCanceledOnTouchOutside(false);
            Applicationmanager.dialogPoPAds.setContentView(R.layout.custum_ads_dialog);

            ImageView ivpopads = Applicationmanager.dialogPoPAds.findViewById(R.id.ivpopads);
            if (Popupads != null && Popupads.size() > 0) {
                String imgpath = ServerConfig.Image_pathads + Popupads.get(0).getAdsFileName();
                String final_imgpath = imgpath.replaceAll(" ", "%20");
                Picasso.with(Applicationmanager.getContext()).load((final_imgpath)).error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(ivpopads, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        PopupadsObject = new SliderAds();
                        PopupadsObject = Popupads.get(0);
                        Popupads.remove(0);
                        if (Popupads.size() == 0) {

                            GetAdsPopupLst(getApplicationContext());


                        }
                    }

                    @Override
                    public void onError() {


                    }
                });
            }


            ivpopads.setOnClickListener(v -> {
                if (PopupadsObject != null && !PopupadsObject.getAdsFileID().equalsIgnoreCase("")) {
                    Constants.Adsclick(PopupadsObject.getAdsFileID(), getApplicationContext());
                    String language = LanguageHelper.getCurrentLanguage(getApplicationContext());

                    if (language.equalsIgnoreCase("ar")) {
                        if (!PopupadsObject.getAdsURLAr().equalsIgnoreCase("")) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(PopupadsObject.getAdsURLAr()));
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            if (Applicationmanager.dialogPoPAds != null) {
                                Applicationmanager.dialogPoPAds.dismiss();

                            }


                            getApplicationContext().startActivity(browserIntent);
                        }
                    }
                    if (language.equalsIgnoreCase("en")) {
                        if (!PopupadsObject.getAdsURL().equalsIgnoreCase("")) {
                            if (Applicationmanager.dialogPoPAds != null) {
                                Applicationmanager.dialogPoPAds.dismiss();

                            }

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(PopupadsObject.getAdsURL()));
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            getApplicationContext().startActivity(browserIntent);
                        }

                    }
                }
            });


            Button btremoveads = Applicationmanager.dialogPoPAds.findViewById(R.id.btremoveads);
            btremoveads.setOnClickListener(v -> {
                if (Applicationmanager.dialogPoPAds != null) {
                    Applicationmanager.dialogPoPAds.dismiss();
                }
                DrawerActivity.Show_GhannelyExtra_Dialog(getApplicationContext());

            });

            Button cancel = Applicationmanager.dialogPoPAds.findViewById(R.id.btclose);
            // if button is clicked, close the custom dialog
            cancel.setOnClickListener(v -> {
                if (Applicationmanager.dialogPoPAds != null) {
                    Applicationmanager.dialogPoPAds.dismiss();
                }
            });

            Applicationmanager.dialogPoPAds.show();


        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Applicationmanager.dialogPoPAds != null) {
            Applicationmanager.dialogPoPAds.dismiss();
            Applicationmanager.dialogPoPAds = null;
        }
    }

    private void GetAdsPopupLst(final Context context) {

        // Constants.SERVER_URl + Constants.REGISTER_PATH
        SharedPreferences prefs = context.getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetAdsPopupLst + "?userId="
                + prefs.getString(Constants.USERID, "") + "&UserToken=" + prefs.getString(Constants.accesstoken, "");
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        service.GetAdsPopupLst(fav_url).enqueue(new Callback<ArrayList<SliderAds>>() {


            @Override
            public void onResponse(@Nullable Call<ArrayList<SliderAds>> call, @Nullable Response<ArrayList<SliderAds>> mresult) {
                if (mresult.isSuccessful()) {
                    if (mresult.body().size() > 0) {
                        ArrayList<SliderAds> Popupadss = new ArrayList<>();
                        for (int i = 0; i < mresult.body().size(); i++) {
                            SliderAds mSlideAlbum = new SliderAds();
                            mSlideAlbum.setAdsFileName(mresult.body().get(i).getAdsFileName());
                            mSlideAlbum.setAdsURL(mresult.body().get(i).getAdsURL());
                            mSlideAlbum.setAdsURLAr(mresult.body().get(i).getAdsURLAr());
                            mSlideAlbum.setAdsFileID(mresult.body().get(i).getAdsFileID());
                            Popupadss.add(mSlideAlbum);

                        }
                        if (Popupadss != null && Popupadss.size() > 0) {
                            boolean isServiceRunning2 = UtilFunctions.isServiceRunning(AdsPopUpService.class.getName(),
                                    context);
                            if (isServiceRunning2) {
                                AdsPopUpService.Popupads = Popupadss;


                            }

                        }
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                }
            }

            @Override
            public void onFailure(@Nullable Call<ArrayList<SliderAds>> call, @Nullable Throwable t) {

            }

        });

    }
}
