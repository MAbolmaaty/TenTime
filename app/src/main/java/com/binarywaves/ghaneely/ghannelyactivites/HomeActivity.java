package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.BaseActivity;
import com.binarywaves.ghaneely.ghannely_application_manager.LocaleHelper;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.AbumGallareyAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.AlbumHomeAdapter;
import com.binarywaves.ghaneely.ghannelyadaptors.ArtistRadioAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.DashBoard_pagerAdapter;
import com.binarywaves.ghaneely.ghannelyadaptors.GridViewAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.RadioAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.SliderAdsAdapter;
import com.binarywaves.ghaneely.ghannelyadaptors.TopTwentyAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.TrackGalleryAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.TrackListAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.VideoListPagerAdapter;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.KaraokeSongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OFFLINEService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OfflineVideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongServiceradio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.RadioConstant;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.MoodsList;
import com.binarywaves.ghaneely.ghannelymodels.Moods_dilist;
import com.binarywaves.ghaneely.ghannelymodels.Radio;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.SliderAds;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.GetVideoDataFrmIDResponse;
import com.binarywaves.ghaneely.ghannelyresponse.HomeListObjectResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackres;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackstripresponse;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.AdsPopUpService;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.LinearDividerItemDecoration;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.binarywaves.ghaneely.ghannelystyles.RecyclerTouchListenerClass;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 18-Jan-17.
 */
@SuppressWarnings("ConstantConditions")
@SuppressLint("StaticFieldLeak")

public class HomeActivity extends BaseActivity {
    public static ArrayList<SlideAlbumObject> mSlideList;
    public static RelativeLayout audioRelative;
    public static ImageView Like;
    public static ImageView dislike;
    public static RelativeLayout progBar;
    public static ArrayList<Similartrackstripresponse> pushlist;
    public static String favo = null;
    public static Radio dataradio;
    public static int selectessong = -1;
    public static int selecttopsong = -1;
    public static ArrayList<VideoObjectResponse> mtopVideoLst;
    public static AbumGallareyAdaptor ListTrackadAdaptor;
    public static TopTwentyAdaptor topTwentyAdaptor;
    private static ArrayList<SliderAds> slideads;
    private static ArrayList<SliderAds> Popupads;
    private static TrackObject data;
    private static String trackid;
    private static Activity activity;
    private static Context cx;
    private static String AccSettingNameEn;
    private static String AccSettingNameAr;
    private static Context context;
    private static ProgressWheel pb1;
    private static String language;
    private static ArrayList<TrackObject> NewmtrackList;
    private static Applicationmanager Applicationmanagerclass;
    private static ArrayList<TrackObject> mtrackList;
    private static ArrayList<TrackObject> NewmtrackList_lv;
    private static ArrayList<MoodsList> mDJLIst;
    private static ImageView play;
    private static ImageView pause;
    private static TextView songname;
    private static TextView albumname;
    private static ImageView player_image;
    TrackListAdaptor international_adaptor;
    ImageView close;
    LayoutInflater inflater;
    JSONObject json;
    String message;
    ArrayList<Moods_dilist> mAlbumListmood;
    Runnable run;
    RelativeLayout video_relative;
    LinearLayout video_linear;
    private RecyclerView rvtoplist;
    private ArrayList<SlideAlbumObject> mPlaylistObjects;
    private RecyclerView rvnewtrack;
    private RecyclerView rvalbum;
    private RecyclerView rvjustforu;
    private HorizontalListView rvartistradio;
    private HorizontalListView international_radio_gallery;
    private RecyclerView gridview;
    private HorizontalListView undergroundgrid;
    private ListView rlnewtracklist;
    private String ads;
    private TrackListAdaptor myTrackadAdaptor;
    private ArtistRadioAdaptor artist_radio_adaptor;
    private GridViewAdaptor grideViewAdaptor;
    private ArrayList<Radio> mInternationalRadioList;
    private AutoScrollViewPager topic_image;
    private AutoScrollViewPager video_pager;
    private TrackGalleryAdaptor trackgalleryAdaptor;
    private ArrayList<ArtistRadio> mArtistRadioList;
    private RadioAdaptor internationalRadioAdaptor;
    private AutoScrollViewPager SlideGallaery;
    private DashBoard_pagerAdapter SlideAdaptor;
    private ArrayList<Similartrackstripresponse> listoftrack;
    private Dialog dialog;
    private String fbname;
    private String fbid;
    private String isFaceReg1 = "false";
    private String userID;
    private String msisdn;
    private String UserImage;
    private String IsFacebookReg;
    private String accesstoken;
    private boolean ISACTIVE;
    private Handler handler;
    private ScrollView scrollView1;
    private View activityView;
    private FrameLayout frameLayout;
    private String isfbRegisterd = "";
    private LinearLayout playist_linear;
    private LinearLayout top_linear;
    private ImageView playist_arrow;
    private ImageView top_arrow;
    private String like;
    private AlbumHomeAdapter songAdaptor;
    private ArrayList<VideoObjectResponse> listofvideos;
    private VideoObjectResponse mvideolistobject;
    private RelativeLayout rlmore;
    private RelativeLayout rltopmore;
    private RelativeLayout rltoplist;
    private RelativeLayout rlnewlist;
    private TextView tvmore;
    private TextView tvtopmore;
    private Button ivmore;
    private Button ivtopmore;
    private LinearLayout new_album_linear;
    private ArrayList<TrackObject> UndergroundTrackLst;
    private ArrayList<TrackObject> JustForUTrackLst;
    private CallbackManager callbackManager;
    private SliderAdsAdapter SliderAdsAdaptor;
    private ArrayList<TrackObject> mAlbumList;
    private ListView rllvtoptrack;
    private VideoListPagerAdapter videoListPagerAdapter;
    private ArrayList<VideoObjectResponse> mVideoslist;
    private String flag, flogtop;
    private Dialog dialogPoPAds;
    private long INTERVAL_TIME = 600000;
    private TrackObject mPlayist;

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            if (dialog != null) {
                dialog.dismiss();
                dialog.hide();
                handler = null;
            }
            // App code
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    (object, response) -> {

                        Log.e("response: ", response + "");
                        try {

                            isFaceReg1 = "true";
                            fbname = object.getString("name");
                            fbid = object.getString("id");
                            SharedPreferences prefsfacebook = getSharedPreferences("GhaniliPreffacebook",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = prefsfacebook.edit(); // params.putString("isFaceReg",
                            // isFaceReg1.toString());
                            editor1.putString("facebookUersname", fbname);
                            editor1.putString(Constants.facebookId, fbid);
                            editor1.apply();

                            SharedPrefHelper.setSharedString(context, Constants.facebookId, fbid);
                            SharedPrefHelper.setSharedString(context, Constants.isFaceReg, "true");
                            SharedPrefHelper.setSharedString(context, Constants.imagefb,
                                    "http://graph.facebook.com/" + fbid + "/picture?type=thumbnail"

                            );


                            connectfacebook();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {
            // progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            // progressDialog.dismiss();
        }
    };
    private LinearLayoutManager horizontalLayoutManager;


    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    public static void changeUI1(final Context context) {

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(), context);
        if (isServiceRunning3) {
            updateUIradio();
            changeButtonradio();

        }


        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (isServiceRunning4) {
            updateUI();
            changeButton();

        }

    }

    public static void updateUI() {
        try {

            data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);


            if (language.equalsIgnoreCase("ar")) {
                songname.setText(data.getTrackArName());
                albumname.setText(data.getAlbumArName());
            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(data.getTrackEnName());
                albumname.setText(data.getAlbumEnName());
            }
            if (data.getIsFavourite().equalsIgnoreCase("true")) {
                Like.setVisibility(View.GONE);

                dislike.setVisibility(View.VISIBLE);
            }

            if (data.getIsFavourite().equalsIgnoreCase("false")) {
                dislike.setVisibility(View.GONE);

                Like.setVisibility(View.VISIBLE);
                // make button enable
            }

            String imgpath = ServerConfig.Image_path
                    + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            // imageLoader.DisplayImage(ServerConfig.Image_path
            // +mtrackList.get(position).getAlbumImgPath(),player_image);
            // imageLoader.DisplayImage(final_imgpath, player_image);
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
            // linearplayer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeButton() {
        if (ListTrackadAdaptor != null) {
            ListTrackadAdaptor.notifyDataSetInvalidated();

        }

        if (topTwentyAdaptor != null) {
            topTwentyAdaptor.notifyDataSetInvalidated();

        }

        if (pause != null && play != null) {
            if (PlayerConstants.SONG_PAUSED) {

                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            } else {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
            }

        }
    }

    public static void changeUI() {
        updateUI();
        changeButton();
    }

    private static void updateUIradio() {
        try {

            dataradio = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER);


            if (language.equalsIgnoreCase("ar")) {
                songname.setText(dataradio.getRadioArName());
                albumname.setText(dataradio.getRadioArName());
            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(dataradio.getRadioName());
                albumname.setText(dataradio.getRadioName());
            }

            Like.setVisibility(View.GONE);

            dislike.setVisibility(View.GONE);

            String imgpath = ServerConfig.Radio_Url + dataradio.getRadioImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");

            Log.d("final_imgpathfinal_imgpath", final_imgpath);
            // imageLoader.DisplayImage(ServerConfig.Image_path
            // +mtrackList.get(position).getAlbumImgPath(),player_image);
            // imageLoader.DisplayImage(final_imgpath, player_image);
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
            // linearplayer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeButtonradio() {
        if (RadioConstant.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    public static void changeUIradio() {
        updateUIradio();
        changeButtonradio();
    }

    private static void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);

        }
    }

    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    private static void justifyListViewHeightBasedOnChildren(ListView listView) {


        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you
                // won't get measured height at all. The listitem needs to be
                // drawn first to know the height.
                listItem.setLayoutParams(
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();


    }

    @SuppressWarnings("unused")
    public static void callFacebookLogout(Context context) {
        LoginManager.getInstance().logOut();
        SharedPreferences prefs = context.getSharedPreferences("GhaniliPreffacebook", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        Log.i("logoutfromfb", "logout");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Applicationmanagerclass = new Applicationmanager();
        dialog = new Dialog(HomeActivity.this);
        OnCreateFunction();

    }

    @SuppressWarnings("ConstantConditions")
    private void OnCreateFunction() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout
        final ViewGroup nullParent = null;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        activityView = layoutInflater.inflate(R.layout.activity_home_screen, nullParent, false);

        context = getApplicationContext();

        activity = HomeActivity.this;
        cx = HomeActivity.this;
        Applicationmanager.setContext(HomeActivity.this);
        ivorangelogo.setVisibility(View.VISIBLE);
        language = LanguageHelper.getCurrentLanguage(cx);
        Context context = LocaleHelper.setLocale(cx, language);

        isfbRegisterd = SharedPrefHelper.getSharedString(context, Constants.isFaceReg);
        Log.i("isfbRegisterd", isfbRegisterd + "");
        //DisplayMetrics metrics = new DisplayMetrics();
        //HomeActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        if (dialogPoPAds != null) {
            if (dialogPoPAds.isShowing()) {
                dialogPoPAds.dismiss();
            }
            dialogPoPAds = null;

        }
        InitializaUi();
        if (Constants.isNetworkOnline(HomeActivity.this)) {
            GetHomeList();

        } else {
            Toast.makeText(HomeActivity.this,
                    getResources().getString(R.string.gnrl_internet_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Applicationmanagerclass = new Applicationmanager();
        //    dialog = new Dialog(HomeActivity.this);
        Log.i("ontestart", "onrestart");
        HomeActivity.progBar.setVisibility(View.GONE);

        HomeActivity.audioRelative.setVisibility(View.VISIBLE);
        HomeActivity.audioRelative.setEnabled(true);
        if (HomeActivity.ListTrackadAdaptor != null && selectessong != -1) {
            HomeActivity.selectessong = PlayerConstants.SONG_NUMBER;

            HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
        }
        if (HomeActivity.topTwentyAdaptor != null && selecttopsong != -1) {
            HomeActivity.selecttopsong = PlayerConstants.SONG_NUMBER;


            HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
        }
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    private void GetHomeList() {
        startwhellprogress();

        Api_Interface service = ServiceGenerator.createService();
        service.GetHomelistsWithGenre(SharedPrefHelper.getSharedInt(context, Constants.GenreId),
                SharedPrefHelper.getSharedString(context, Constants.USERID),
                SharedPrefHelper.getSharedString(context, Constants.accesstoken)).enqueue(new Callback<HomeListObjectResponse>() {

            @Override
            public void onResponse(@NonNull Call<HomeListObjectResponse> call, @NonNull Response<HomeListObjectResponse> mresult) {
                if (mresult.isSuccessful()) {

                    setHomeScreenUi(mresult.body());

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<HomeListObjectResponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }


    private void setHomeScreenUi(HomeListObjectResponse body) {


        mSlideList = new ArrayList<>();
        if (body.getSliderLst() != null && body.getSliderLst().size() > 0) {
            for (int i = 0; i < body.getSliderLst().size(); i++) {
                SlideAlbumObject mSlideAlbum = new SlideAlbumObject();
                mSlideAlbum.setAlbumId(body.getSliderLst().get(i).getContentID());
                mSlideAlbum.setSingerEnName(body.getSliderLst().get(i).getContentTypeID());
                mSlideAlbum.setSingerArName(body.getSliderLst().get(i).getContentTypeID());
                mSlideAlbum.setAlbumEnName(body.getSliderLst().get(i).getSliderEnName());
                mSlideAlbum.setAlbumArName(body.getSliderLst().get(i).getSliderArName());
                mSlideAlbum.setAlbumImgPath(body.getSliderLst().get(i).getSliderImage());
                mSlideAlbum.setAlbumARImgPath(body.getSliderLst().get(i).getSliderArImage());
                mSlideAlbum.setIsPremium(body.getSliderLst().get(i).getIsPremium());

                mSlideList.add(mSlideAlbum);

            }
        }
        // // the New Album array ////

        mPlaylistObjects = new ArrayList<>();
        mPlaylistObjects = body.getAlbumLst();


        // ///////newTrackList //////////////new tracks

        NewmtrackList = new ArrayList<>();
        NewmtrackList = body.getNewTrackLst();


        progBar.setVisibility(View.GONE);

        ads = body.getAdFileName();
        AccSettingNameEn = body.getAccSettingNameEn();
        AccSettingNameAr = body.getAccSettingNameAr();

        slideads = new ArrayList<>();
        slideads = body.getAdsFileLst();
//
        Log.i("slides", slideads + "jj");

        //popup Ads
        Popupads = new ArrayList<>();
        Popupads = body.getPopupAdsFileLst();

        Log.i("popup", Popupads + "jj");


        // /////////// tracklist //////////////Top songs
        // //top songs

        mtrackList = new ArrayList<>();
        mtrackList = body.getTrackLst();


        // just for you


        JustForUTrackLst = new ArrayList<>();
        JustForUTrackLst = body.getJustForUTrackLst();


        // //////singerRadioList ///////////

        mArtistRadioList = new ArrayList<>();
        mArtistRadioList = body.getSingerRadioLst();


        // /////////// online  Radio ////////////

        mInternationalRadioList = new ArrayList<>();
        mInternationalRadioList = body.getOnlineRadioLst();

        // /////////Moods List ///////////
        mDJLIst = new ArrayList<>();
        mDJLIst = body.getPersonalDJLst();


        //TopVideo list
        mtopVideoLst = new ArrayList<>();
        mtopVideoLst = body.getTopVideoLst();

        Log.e("mtopVideoLst", mtopVideoLst.size() + "here");
        Log.e("mInternationalRadioList", mInternationalRadioList.size() + "here");
        Log.e("MY mtracklist", mtrackList.size() + "here");
        Log.e("MY mPlaylistObjectsis", mPlaylistObjects.size() + "here");
        Log.e("MY slide album is", mSlideList.size() + "here");
        Log.e("MY slideads", slideads.size() + "here");
        Log.e("MY NewmtrackList album", NewmtrackList.size() + "here");
        if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && !SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {

            if (Popupads != null && Popupads.size() > 0) {
                boolean isServiceRunning2 = UtilFunctions.isServiceRunning(AdsPopUpService.class.getName(),
                        getApplicationContext());
                if (isServiceRunning2) {
                    AdsPopUpService.Popupads = Popupads;

                    Intent i = new Intent(getApplicationContext(), AdsPopUpService.class);
                    stopService(i);
                    Intent i1 = new Intent(getApplicationContext(), AdsPopUpService.class);
                    startService(i1);


                } else {
                    AdsPopUpService.Popupads = Popupads;

                    Intent i1 = new Intent(getApplicationContext(), AdsPopUpService.class);
                    startService(i1);

                }
            }
        }


        setHomeScreenDesign();

    }


    private void setHomeScreenDesign() {
        if (slideads != null && slideads.size() > 0) {
            SliderAdsAdaptor = new SliderAdsAdapter(HomeActivity.this, R.layout.sliderads_row, R.id.topic_image,
                    slideads).setInfiniteLoop(true);
            topic_image.setAdapter(SliderAdsAdaptor);
            topic_image.setInterval(10000);
            topic_image.startAutoScroll();
        }


        if (mtopVideoLst != null && mtopVideoLst.size() > 0) {

            video_relative.setVisibility(View.VISIBLE);
            video_linear.setVisibility(View.VISIBLE);
            videoListPagerAdapter = new VideoListPagerAdapter(HomeActivity.this, R.layout.custumvideopager,
                    R.id.ivposter, mtopVideoLst).setInfiniteLoop(true);
            video_pager.setAdapter(videoListPagerAdapter);
            video_pager.setInterval(4000);
            video_pager.setCurrentItem(2000 / 2 - 2000 / 2 % mtopVideoLst.size());
            PlayerConstants.SONGS_LISTVideo = mtopVideoLst;
        } else if (mtopVideoLst != null && mtopVideoLst.size() == 0) {

            video_relative.setVisibility(View.GONE);
            video_linear.setVisibility(View.GONE);

        }
        if (mSlideList != null && mSlideList.size() > 0) {
            SlideAdaptor = new DashBoard_pagerAdapter(HomeActivity.this, R.layout.slide_item_row,
                    R.id.slide_image_view, mSlideList).setInfiniteLoop(true);
            SlideGallaery.setAdapter(SlideAdaptor);
            SlideGallaery.setInterval(8000);
            SlideGallaery.startAutoScroll();

            //     SlideGallaery.setCurrentItem(10/ 2 - 10 / 2 % mSlideList.size());

        }


        if (mArtistRadioList != null && mArtistRadioList.size() > 0) {
            artist_radio_adaptor = new ArtistRadioAdaptor(HomeActivity.this, R.layout.moodsitems_list,
                    mArtistRadioList);
            rvartistradio.setAdapter(artist_radio_adaptor);


        }
// else {
//            rvartistradio.setAdapter(null);
//
//        }
        if (mPlaylistObjects != null && mPlaylistObjects.size() > 0) {
            songAdaptor = new AlbumHomeAdapter(HomeActivity.this, R.layout.song_row_item, mPlaylistObjects, 2);
            rvalbum.setAdapter(songAdaptor);
            setRecycleViewDesign(rvalbum);
        } else {
            rvalbum.setAdapter(null);

        }

        if (JustForUTrackLst != null && JustForUTrackLst.size() > 0) {

            myTrackadAdaptor = new TrackListAdaptor(HomeActivity.this, R.layout.song_row_item,
                    JustForUTrackLst);

            rvjustforu.setAdapter(myTrackadAdaptor);
            setRecycleViewDesign(rvjustforu);
        } else {
            rvjustforu.setAdapter(null);

        }
        if (NewmtrackList != null && NewmtrackList.size() > 0) {

            myTrackadAdaptor = new TrackListAdaptor(HomeActivity.this, R.layout.song_row_item, NewmtrackList);
            rvnewtrack.setAdapter(myTrackadAdaptor);
            setRecycleViewDesign(rvnewtrack);

        } else {
            rvnewtrack.setAdapter(null);

        }

        if (mInternationalRadioList != null && mInternationalRadioList.size() > 0) {
            internationalRadioAdaptor = new RadioAdaptor(HomeActivity.this, R.layout.custumeradio_home,
                    mInternationalRadioList);
            international_radio_gallery.setAdapter(internationalRadioAdaptor);
            RadioConstant.SONGS_LIST = mInternationalRadioList;

        }

        if (mtrackList != null && mtrackList.size() > 0) {
            myTrackadAdaptor = new TrackListAdaptor(HomeActivity.this, R.layout.song_row_item, mtrackList);
            rvtoplist.setAdapter(myTrackadAdaptor);
            setRecycleViewDesign(rvtoplist);

        } else {
            rvtoplist.setAdapter(null);

        }
        if (mDJLIst != null && mDJLIst.size() > 0) {
            grideViewAdaptor = new GridViewAdaptor(HomeActivity.this, R.layout.custom_moodsitem, mDJLIst);
            gridview.setAdapter(grideViewAdaptor);
            setRecycleViewDesign(gridview);

            progBar.setVisibility(View.GONE);
            Log.d("yeeees" , "yees");

        } else {
            gridview.setAdapter(null);
            Log.d("nooooo" , "nnooooo");
        }
        onNewIntent(getIntent());
        Log.d("dsdssds" , "ydsdsdsdsdees");

    }

    private void setRecycleViewDesignforlist(RecyclerView gridview) {

        if (language.equalsIgnoreCase("ar")) {
            horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        }
        if (language.equalsIgnoreCase("en")) {
            horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        gridview.setLayoutManager(horizontalLayoutManager);
// add the decoration to the recyclerView
        LinearDividerItemDecoration decoration = new LinearDividerItemDecoration(context, getResources().getColor(R.color.black), 2);
        gridview.addItemDecoration(decoration);
    }

    private void setRecycleViewDesign(RecyclerView gridview) {

        if (language.equalsIgnoreCase("ar")) {
            horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        }
        if (language.equalsIgnoreCase("en")) {
            horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        }
        gridview.setLayoutManager(horizontalLayoutManager);
// add the decoration to the recyclerView

        LinearDividerItemDecoration decoration = new LinearDividerItemDecoration(context, getResources().getColor(R.color.black), 30);
        gridview.addItemDecoration(decoration);
    }

    private void InitializaUi() {
        selectedListItem = 0;
        selectessong = -1;
        selecttopsong = -1;

        scrollView1 = activityView.findViewById(R.id.albums_scroll);
        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        SlideGallaery = activityView.findViewById(R.id.Gallery01);
        topic_image = activityView.findViewById(R.id.topic_image1);
        video_pager = activityView.findViewById(R.id.video_pager);
        video_relative = activityView.findViewById(R.id.video_relative);
        video_linear = activityView.findViewById(R.id.video_linear);

        rvalbum = activityView.findViewById(R.id.album_gallery);
        rvnewtrack = activityView.findViewById(R.id.newtracklist);
        rlnewtracklist = activityView.findViewById(R.id.lvnewtrack);
        playist_linear = activityView.findViewById(R.id.playist_linear);
        rlmore = activityView.findViewById(R.id.rlmore);
        rltopmore = activityView.findViewById(R.id.rltopmore);
        rlnewlist = activityView.findViewById(R.id.rlnewlist);
        rltoplist = activityView.findViewById(R.id.rltoplist);
        playist_arrow = activityView.findViewById(R.id.playist_arrow);
        tvmore = activityView.findViewById(R.id.tvmore);
        ivmore = activityView.findViewById(R.id.ivmore);
        new_album_linear = activityView.findViewById(R.id.new_album_linear);

        new_album_linear.setOnClickListener(view -> {


            Intent albumIntent = new Intent(HomeActivity.this, AlbumsGridActivity.class);
            albumIntent.putParcelableArrayListExtra("albumlist", mPlaylistObjects);
            startActivity(albumIntent);

        });

        dialogPoPAds = new Dialog(HomeActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        tvtopmore = activityView.findViewById(R.id.tvtopmore);
        ivtopmore = activityView.findViewById(R.id.ivtopmore);

        playist_linear.setOnClickListener(view -> {
            if (rvnewtrack.getVisibility() == View.VISIBLE) {
                startwhellprogress();
                rvnewtrack.setVisibility(View.GONE);
                playist_arrow.setImageResource(R.mipmap.arrow);
                SetDatainList();
                progBar.setVisibility(View.GONE);
            } else {
                startwhellprogress();
                rlnewlist.setVisibility(View.GONE);
                rvnewtrack.setVisibility(View.VISIBLE);
                //  lvnewtrack.setAdapter(null);
                playist_arrow.setImageResource(R.mipmap.downarrow);

                if (NewmtrackList != null && NewmtrackList.size() > 0) {

                    myTrackadAdaptor = new TrackListAdaptor(HomeActivity.this, R.layout.song_row_item, NewmtrackList);
                    rvnewtrack.setAdapter(myTrackadAdaptor);
                }
                progBar.setVisibility(View.GONE);

            }
        });

        rlmore.setOnClickListener(view -> {
            if (NewmtrackList != null && NewmtrackList.size() > 0) {
                if (flag.equalsIgnoreCase("more")) {
                    ListTrackadAdaptor = new AbumGallareyAdaptor(HomeActivity.this, R.layout.albumlist_item, NewmtrackList);
                    rlnewtracklist.setAdapter(ListTrackadAdaptor);
                    justifyListViewHeightBasedOnChildren(rlnewtracklist);
                    ListTrackadAdaptor.notifyDataSetInvalidated();
                    rlnewtracklist.invalidate();
                    tvmore.setText(getResources().getString(R.string.viewless));
                    ivmore.setBackgroundResource(R.mipmap.dropup);
                    flag = "less";

                } else {
                    if (flag.equalsIgnoreCase("less")) {
                        SetDatainList();
                        scrollView1.post(() -> scrollView1.scrollTo(scrollView1.getScrollY(), rlnewtracklist.getHeight()));

                    }
                }
            }
        });

        rvjustforu = activityView.findViewById(R.id.Editor_gallery);
        rvartistradio = activityView.findViewById(R.id.artist_radio_gallery);
        undergroundgrid = activityView.findViewById(R.id.undergroundgridview);
        international_radio_gallery = activityView.findViewById(R.id.international_radio_gallery);
        gridview = activityView.findViewById(R.id.gridview);
        rvtoplist = activityView.findViewById(R.id.list1);
        rllvtoptrack = activityView.findViewById(R.id.lvtoptrack);
        top_linear = activityView.findViewById(R.id.top_linear);
        top_arrow = activityView.findViewById(R.id.top_arrow);
        top_linear.setOnClickListener((View view) -> {
            if (rvtoplist.getVisibility() == View.VISIBLE) {
                startwhellprogress();
                rvtoplist.setVisibility(View.GONE);
                top_arrow.setImageResource(R.mipmap.arrow);
                SetTopList();
                progBar.setVisibility(View.GONE);
            } else {
                startwhellprogress();

                rltoplist.setVisibility(View.GONE);
                rvtoplist.setVisibility(View.VISIBLE);
                top_arrow.setImageResource(R.mipmap.downarrow);

                if (mtrackList != null && mtrackList.size() > 0) {
                    myTrackadAdaptor = new TrackListAdaptor(HomeActivity.this, R.layout.song_row_item, mtrackList);
                    rvtoplist.setAdapter(myTrackadAdaptor);
                    setRecycleViewDesign(rvtoplist);
                }
                progBar.setVisibility(View.GONE);

            }
        });

        rltopmore.setOnClickListener(view -> {
            if (mtrackList != null && mtrackList.size() > 0) {
                if (flogtop.equalsIgnoreCase("more")) {

                    topTwentyAdaptor = new TopTwentyAdaptor(HomeActivity.this, R.layout.albumlist_item, mtrackList);
                    rllvtoptrack.setAdapter(topTwentyAdaptor);

                    justifyListViewHeightBasedOnChildren(rllvtoptrack);
                    topTwentyAdaptor.notifyDataSetInvalidated();
                    rllvtoptrack.invalidate();
                    tvtopmore.setText(getResources().getString(R.string.viewless));
                    ivtopmore.setBackgroundResource(R.mipmap.dropup);
                    flogtop = "less";

                } else {
                    if (flogtop.equalsIgnoreCase("less")) {
                        SetTopList();
                        scrollView1.post(() -> scrollView1.scrollTo(0, rltopmore.getBottom()));

                    }
                }
            }

        });

        audioRelative = activityView.findViewById(R.id.audioplayer);

        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);
        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);
        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);

        InitializeOnClickAction();
        frameLayout.addView(activityView);
        handler = new Handler();


        if (isfbRegisterd.equalsIgnoreCase("false")) {

            handler.postDelayed(() -> {
                // close your dialog
                if (dialog != null && !dialog.isShowing())
                    facebookpopup();
            }, INTERVAL_TIME); // 10,000 ms delay
        } else {
            if (isfbRegisterd.equalsIgnoreCase("true")) {
                handler = null;
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    dialog.hide();
                    handler = null;
                }
            }
        }

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());

        boolean isServiceRunning6 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                getApplicationContext());

        if (!isServiceRunning3
                && !isServiceRunning6) {
            audioRelative.setVisibility(View.GONE);
        } else {
            audioRelative.setVisibility(View.VISIBLE);
            audioRelative.setEnabled(true);

        }
        try {
            boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning) {
                changeUI();
            }


            boolean isServiceRunning7 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                    getApplicationContext());
            if (isServiceRunning7) {
                changeUIradio();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void SetTopList() {

        rltoplist.setVisibility(View.VISIBLE);
        if (mtrackList != null && mtrackList.size() > 0) {

            if (mtrackList.size() > 7) {
                List<TrackObject> mtrackList1 = new ArrayList<>(mtrackList).subList(0, 7);
                mtrackList1.subList(0, 7);

                topTwentyAdaptor = new TopTwentyAdaptor(HomeActivity.this, R.layout.albumlist_item, mtrackList1);
                rllvtoptrack.setAdapter(topTwentyAdaptor);
                justifyListViewHeightBasedOnChildren(rllvtoptrack);
                tvtopmore.setText(getResources().getString(R.string.viewmore));
                ivtopmore.setBackgroundResource(R.mipmap.dropdown);
                flogtop = "more";
            } else {
                topTwentyAdaptor = new TopTwentyAdaptor(HomeActivity.this, R.layout.albumlist_item, mtrackList);
                rllvtoptrack.setAdapter(topTwentyAdaptor);
                justifyListViewHeightBasedOnChildren(rllvtoptrack);

                tvtopmore.setVisibility(View.GONE);
                ivtopmore.setVisibility(View.GONE);
            }
        }

    }

    private void SetDatainList() {
        rlnewlist.setVisibility(View.VISIBLE);
        if (NewmtrackList != null && NewmtrackList.size() > 0) {
            NewmtrackList_lv = new ArrayList<>();

            NewmtrackList_lv = NewmtrackList;
            if (NewmtrackList_lv.size() > 7) {
                List<TrackObject> mtrackList1 = new ArrayList<>(NewmtrackList_lv).subList(0, 7);
                mtrackList1.subList(0, 7);

                ListTrackadAdaptor = new AbumGallareyAdaptor(HomeActivity.this, R.layout.albumlist_item, mtrackList1);
                rlnewtracklist.setAdapter(ListTrackadAdaptor);
                justifyListViewHeightBasedOnChildren(rlnewtracklist);

                tvmore.setText(getResources().getString(R.string.viewmore));
                ivmore.setBackgroundResource(R.mipmap.dropdown);
                flag = "more";

            } else {

                ListTrackadAdaptor = new AbumGallareyAdaptor(HomeActivity.this, R.layout.albumlist_item, NewmtrackList_lv);
                rlnewtracklist.setAdapter(ListTrackadAdaptor);
                justifyListViewHeightBasedOnChildren(rlnewtracklist);


                tvmore.setVisibility(View.GONE);
                ivmore.setVisibility(View.GONE);
            }
        }

    }

    @SuppressLint("HandlerLeak")
    private void InitializeOnClickAction() {
        audioRelative.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            getPlayerScreen();
        });
        play.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                PlayerConstants.SONGnext = false;

                Controls.playControl(getApplicationContext());
            }
            boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                    getApplicationContext());
            if (isServiceRunning4) {
                RadioConstant.SONGnext = false;

                ControlRadio.playControl(getApplicationContext());

            }

        });
        pause.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                PlayerConstants.SONGnext = false;

                Controls.pauseControl(getApplicationContext());

            }
            boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                    getApplicationContext());
            if (isServiceRunning4) {
                RadioConstant.SONGnext = false;

                ControlRadio.pauseControl(getApplicationContext());

            }

        });

        Like.setOnClickListener(v -> {
            startwhellprogress();

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {

                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), getApplicationContext(),
                        "1");
                // data.setIsFavourite(true);

            }

        });

        dislike.setOnClickListener(v -> {
            startwhellprogress();


            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                trackid = data.getTrackId();

                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), getApplicationContext(), "0");

            }

        });
        rvtoplist.addOnItemTouchListener(new RecyclerTouchListenerClass(this, new RecyclerTouchListenerClass.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO Auto-generated method stub
                if (Constants.isNetworkOnline(HomeActivity.this)) {
                    if (position >= mtrackList.size()) {
                        position = position % mtrackList.size();
                    }
                    // audioRelative.setVisibility(View.VISIBLE);
                    StopService();
                    PlayerConstants.SONGS_LIST = mtrackList;

                    Log.e("list clicked ", "here");
                    // streamService = new Intent(MainActivity.this,
                    // StreamService.class);
                    // startService(streamService);
                    startwhellprogress();
                    audioRelative.setEnabled(false);

                    // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                    Log.e("list clicked ", "here");
                    trackid = mtrackList.get(position).getTrackId();
                    favo = mtrackList.get(position).getIsFavourite();
                    if (favo.equalsIgnoreCase("true")) {
                        // change icon
                        // make button disabled
                        Like.setVisibility(View.GONE);

                        dislike.setVisibility(View.VISIBLE);
                    }

                    if (favo.equalsIgnoreCase("false")) {
                        dislike.setVisibility(View.GONE);

                        Like.setVisibility(View.VISIBLE);
                        // make button enable

                    }
                    // linearplayer.setVisibility(View.VISIBLE);
                    PlayerConstants.SONG_PAUSED = false;
                    PlayerConstants.SONG_NUMBER = position;
                    boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                            getApplicationContext());
                    if (!isServiceRunning) {
                        Intent i = new Intent(getApplicationContext(), SongService.class);
                        startService(i);
                        // progBar.setVisibility(View.GONE);

                    } else {
                        PlayerConstants.SONG_CHANGE_HANDLER
                                .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                    }

                    changeUI();


                    Log.d("TAG", "TAG Tapped INOUT(OUT)");

                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                            Toast.LENGTH_LONG).show();
                }
            }


        }));

        rllvtoptrack.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(HomeActivity.this)) {
                if (position >= mtrackList.size()) {
                    position = position % mtrackList.size();
                }
                // audioRelative.setVisibility(View.VISIBLE);
                StopService();
                PlayerConstants.SONGS_LIST = mtrackList;

                Log.e("list clicked ", "here");
                // streamService = new Intent(MainActivity.this,
                // StreamService.class);
                // startService(streamService);
                startwhellprogress();
                audioRelative.setEnabled(false);

                // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                Log.e("list clicked ", "here");
                trackid = mtrackList.get(position).getTrackId();
                favo = mtrackList.get(position).getIsFavourite();
                if (favo.equalsIgnoreCase("true")) {
                    // change icon
                    // make button disabled
                    Like.setVisibility(View.GONE);

                    dislike.setVisibility(View.VISIBLE);
                }

                if (favo.equalsIgnoreCase("false")) {
                    dislike.setVisibility(View.GONE);

                    Like.setVisibility(View.VISIBLE);
                    // make button enable

                }
                // linearplayer.setVisibility(View.VISIBLE);
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = position;
                selecttopsong = position;
                selectessong = -1;
                topTwentyAdaptor.notifyDataSetInvalidated();

                if (rlnewlist.getVisibility() == View.VISIBLE) {
                    ListTrackadAdaptor.notifyDataSetInvalidated();
                }

                boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                        getApplicationContext());
                if (!isServiceRunning) {
                    Intent i = new Intent(getApplicationContext(), SongService.class);
                    startService(i);
                    // progBar.setVisibility(View.GONE);

                } else {
                    PlayerConstants.SONG_CHANGE_HANDLER
                            .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }

                changeUI();

                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }

        });


        gridview.addOnItemTouchListener(new RecyclerTouchListenerClass(this, new RecyclerTouchListenerClass.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= mDJLIst.size()) {
                    position = position % mDJLIst.size();
                }
                if (HomeActivity.ListTrackadAdaptor != null) {
                    HomeActivity.selectessong = -1;
                    HomeActivity.selecttopsong = -1;
                    HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
                }
                if (HomeActivity.topTwentyAdaptor != null) {
                    HomeActivity.selectessong = -1;
                    HomeActivity.selecttopsong = -1;

                    HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
                }
                Intent albumIntent = new Intent(HomeActivity.this, MoodsActivity.class);
                albumIntent.putExtra("playlistId", mDJLIst.get(position).getPersonalDJID());
                albumIntent.putExtra("playlistimage", mDJLIst.get(position).getDjImage());

                Log.d("image_mooooood", mDJLIst.get(position).getDjImage());

                if (language.equalsIgnoreCase("ar")) {
                    albumIntent.putExtra("playlistname", mDJLIst.get(position).getPersonalDJIDAr());
                }
                if (language.equalsIgnoreCase("en")) {
                    albumIntent.putExtra("playlistname", mDJLIst.get(position).getPersonalDJName());

                }

                startActivity(albumIntent);
            }

        }));

        rvnewtrack.addOnItemTouchListener(
                new RecyclerTouchListenerClass(this, new RecyclerTouchListenerClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {   // TODO Auto-generated method stub
                        // audioRelative.setVisibility(View.VISIBLE);
                        if (Constants.isNetworkOnline(HomeActivity.this)) {
                            if (position >= NewmtrackList.size()) {
                                position = position % NewmtrackList.size();
                            }

                            // audioRelative.setVisibility(View.VISIBLE);
                            StopService();
                            PlayerConstants.SONGS_LIST = NewmtrackList;
                            Log.e("list clicked ", "here");
                            // streamService = new Intent(MainActivity.this,
                            // StreamService.class);
                            // startService(streamService);
                            startwhellprogress();
                            audioRelative.setEnabled(false);

                            // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                            Log.e("list clicked ", "here");
                            trackid = NewmtrackList.get(position).getTrackId();
                            favo = NewmtrackList.get(position).getIsFavourite();
                            if (favo.equalsIgnoreCase("true")) {
                                // change icon
                                // make button disabled
                                Like.setVisibility(View.GONE);

                                dislike.setVisibility(View.VISIBLE);
                            }

                            if (favo.equalsIgnoreCase("false")) {
                                dislike.setVisibility(View.GONE);

                                Like.setVisibility(View.VISIBLE);
                                // make button enable

                            }
                            // linearplayer.setVisibility(View.VISIBLE);
                            PlayerConstants.SONG_PAUSED = false;
                            PlayerConstants.SONG_NUMBER = position;
                            boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                                    getApplicationContext());
                            if (!isServiceRunning) {
                                Intent i = new Intent(getApplicationContext(), SongService.class);
                                startService(i);
                                // progBar.setVisibility(View.GONE);

                            } else {
                                PlayerConstants.SONG_CHANGE_HANDLER
                                        .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                            }

                            changeUI();


                            Log.d("TAG", "TAG Tapped INOUT(OUT)");

                        } else {

                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                }));


        rlnewtracklist.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub

            if (Constants.isNetworkOnline(HomeActivity.this)) {


                // audioRelative.setVisibility(View.VISIBLE);
                StopService();
                PlayerConstants.SONGS_LIST = NewmtrackList;
                Log.e("list clicked ", "here");

                startwhellprogress();
                audioRelative.setEnabled(false);

                // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                Log.e("list clicked ", "here");
                trackid = NewmtrackList.get(position).getTrackId();
                favo = NewmtrackList.get(position).getIsFavourite();
                if (favo.equalsIgnoreCase("true")) {
                    // change icon
                    // make button disabled
                    Like.setVisibility(View.GONE);

                    dislike.setVisibility(View.VISIBLE);
                }

                if (favo.equalsIgnoreCase("false")) {
                    dislike.setVisibility(View.GONE);

                    Like.setVisibility(View.VISIBLE);
                    // make button enable

                }
                // linearplayer.setVisibility(View.VISIBLE);
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = position;
                selectessong = position;
                selecttopsong = -1;
                ListTrackadAdaptor.notifyDataSetInvalidated();
                if (rltoplist.getVisibility() == View.VISIBLE) {
                    topTwentyAdaptor.notifyDataSetInvalidated();
                }
                boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                        getApplicationContext());
                if (!isServiceRunning) {
                    Intent i = new Intent(getApplicationContext(), SongService.class);
                    startService(i);
                    // progBar.setVisibility(View.GONE);

                } else {
                    PlayerConstants.SONG_CHANGE_HANDLER
                            .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }

                changeUI();


                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {

                Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        });


        rvalbum.addOnItemTouchListener(new RecyclerTouchListenerClass(this, new RecyclerTouchListenerClass.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO Auto-generated method stub
                // PlaylistObject
                // playSong(CurrentTrack);
                if (HomeActivity.ListTrackadAdaptor != null) {
                    HomeActivity.selectessong = -1;
                    HomeActivity.selecttopsong = -1;
                    HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
                }
                if (HomeActivity.topTwentyAdaptor != null) {
                    HomeActivity.selectessong = -1;
                    HomeActivity.selecttopsong = -1;

                    HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
                }
                if (position >= mPlaylistObjects.size()) {
                    position = position % mPlaylistObjects.size();
                }
                Intent albumIntent = new Intent(HomeActivity.this, AlbumTabsActivity.class);
                albumIntent.putExtra("album", mPlaylistObjects.get(position));
                startActivity(albumIntent);

            }


        }));
        rvjustforu.addOnItemTouchListener(new RecyclerTouchListenerClass(this, new RecyclerTouchListenerClass.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                if (Constants.isNetworkOnline(HomeActivity.this)) {
                    if (position >= JustForUTrackLst.size()) {
                        position = position % JustForUTrackLst.size();
                    }
                    // audioRelative.setVisibility(View.VISIBLE);
                    StopService();
                    if (HomeActivity.ListTrackadAdaptor != null) {
                        HomeActivity.selectessong = -1;
                        HomeActivity.selecttopsong = -1;
                        HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
                    }
                    if (HomeActivity.topTwentyAdaptor != null) {
                        HomeActivity.selectessong = -1;
                        HomeActivity.selecttopsong = -1;

                        HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
                    }
                    PlayerConstants.SONGS_LIST = JustForUTrackLst;

                    Log.e("list clicked ", "here");
                    // streamService = new Intent(MainActivity.this,
                    // StreamService.class);
                    // startService(streamService);
                    startwhellprogress();
                    audioRelative.setEnabled(false);

                    // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                    Log.e("list clicked ", "here");
                    trackid = JustForUTrackLst.get(position).getTrackId();
                    favo = JustForUTrackLst.get(position).getIsFavourite();
                    if (favo.equalsIgnoreCase("true")) {
                        // change icon
                        // make button disabled
                        Like.setVisibility(View.GONE);

                        dislike.setVisibility(View.VISIBLE);
                    }

                    if (favo.equalsIgnoreCase("false")) {
                        dislike.setVisibility(View.GONE);

                        Like.setVisibility(View.VISIBLE);
                        // make button enable

                    }
                    // linearplayer.setVisibility(View.VISIBLE);
                    PlayerConstants.SONG_PAUSED = false;
                    PlayerConstants.SONG_NUMBER = position;
                    boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                            getApplicationContext());
                    if (!isServiceRunning) {
                        Intent i = new Intent(getApplicationContext(), SongService.class);
                        startService(i);
                        // progBar.setVisibility(View.GONE);

                    } else {
                        PlayerConstants.SONG_CHANGE_HANDLER
                                .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                    }

                    changeUI();

                    Log.d("TAG", "TAG Tapped INOUT(OUT)");

                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                            Toast.LENGTH_LONG).show();
                }
            }


        }));

        rvartistradio.setOnItemClickListener((parent, view, position, id) -> {
                    if (Constants.isNetworkOnline(HomeActivity.this)) {
                        if (position >= mArtistRadioList.size()) {
                            position = position % mArtistRadioList.size();
                        }
                        if (HomeActivity.ListTrackadAdaptor != null) {
                            HomeActivity.selectessong = -1;
                            HomeActivity.selecttopsong = -1;
                            HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
                        }
                        if (HomeActivity.topTwentyAdaptor != null) {
                            HomeActivity.selectessong = -1;
                            HomeActivity.selecttopsong = -1;

                            HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
                        }
                        Intent playTrack = new Intent(HomeActivity.this, ArtistTabsActivity.class);
                        playTrack.putExtra("playlistId", mArtistRadioList.get(position).getSingerId());
                        startActivity(playTrack);
                    } else {
                        Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                                Toast.LENGTH_LONG).show();
                    }
                }


        );

        international_radio_gallery.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            // playSong();
            if (Constants.isNetworkOnline(HomeActivity.this)) {

                Log.e("list clicked ", "here");
                startwhellprogress();
                audioRelative.setEnabled(false);
                if (HomeActivity.ListTrackadAdaptor != null) {
                    HomeActivity.selectessong = -1;
                    HomeActivity.selecttopsong = -1;
                    HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
                    Log.e("5555 ", "here");

                }
                if (HomeActivity.topTwentyAdaptor != null) {
                    HomeActivity.selectessong = -1;
                    HomeActivity.selecttopsong = -1;

                    HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
                    Log.e("888888 ", "here");

                }
                StopService();
                Log.e("10000 ", "here");

                RadioConstant.SONG_PAUSED = false;
                RadioConstant.SONG_NUMBER = position;
                Log.e("55505 ", "here");

                boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                        getApplicationContext());
                if (!isServiceRunning3) {
                    Remove_handlers();
                    Log.e("8484184 ", "here");

                    Intent i = new Intent(getApplicationContext(), SongServiceradio.class);
                    startService(i);
                    Log.e("9999 ", "here");

                    // progBar.setVisibility(View.GONE);

                } else {
                    RadioConstant.SONG_CHANGE_HANDLER
                            .sendMessage(RadioConstant.SONG_CHANGE_HANDLER.obtainMessage());
                }

                updateUIradio();
                Log.e("25121 ", "here");

                changeButtonradio();


                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getPlayerScreen() {


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning3 && PlayerConstants.SONGS_LIST.size() > 0) {

            Intent player = new Intent(HomeActivity.this, PlayerAcreenActivity.class);

            player.putExtra("track", PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER));

            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void facebookpopup() {
        isfbRegisterd = SharedPrefHelper.getSharedString(context, Constants.isFaceReg);
        if (isfbRegisterd.equalsIgnoreCase("false")) {
            dialog = new Dialog(HomeActivity.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.fasebookpopup);

            Button cancel = dialog.findViewById(R.id.cancel);
            // if button is clicked, close the custom dialog
            cancel.setOnClickListener(v -> {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    handler = null;
                }
            });

            final LoginButton loginBtn = dialog.findViewById(R.id.fb_login_button);
            loginBtn.setVisibility(View.GONE);
            loginBtn.setReadPermissions(Collections.singletonList("email"));
            loginBtn.setReadPermissions(Collections.singletonList("user_friends"));
            loginBtn.setOnClickListener(v -> {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    handler = null;
                }
                loginBtn.registerCallback(callbackManager, mCallBack);
                // loginBtn.setVisibility(View.VISIBLE);

                // Your query to fetch Data

            });
            final Button connect = dialog.findViewById(R.id.connect);
            // if button is clicked, close the custom dialog
            connect.setOnClickListener(v -> {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                handler = null;
                loginBtn.performClick();
            });
            // if button is clicked, close the custom dialog

            dialog.show();// dismiss any dialog like this

        }

    }

    @SuppressWarnings("unused")
    private void playMp3(byte[] mp3SoundByteArray) {

        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();
            // Tried reusing instance of media player
            // but that resulted in system crashes...
            MediaPlayer mediaPlayer = new MediaPlayer();
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("ConstantConditions")

    private void connectfacebook() {
        // TODO Auto-generated method stub
        if (Constants.isNetworkOnline(this)) {
            startwhellprogress();


            if (!SharedPrefHelper.getSharedString(context, Constants.SIM_NO).equalsIgnoreCase("")) { // username
                // and password are present, do your stuff
                Api_Interface service = ServiceGenerator.createService();
                service.UpdateUserFacebookId(fbname, fbid, SharedPrefHelper.getSharedString(context, Constants.USERID), SharedPrefHelper.getSharedString(context, Constants.accesstoken)).enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> mresult) {
                        if (mresult.isSuccessful()) {
                            Log.i("onSuccess", "here");
                            User mUser = new User();


                            userID = mresult.body().getUserID();
                            msisdn = mresult.body().getMsisdn();
                            ISACTIVE = mresult.body().getIsActivated();
                            UserImage = mresult.body().getUserImage();
                            accesstoken = mresult.body().getUserToken();
                            UserStatusId = mresult.body().getIsSubsc();
                            ServiceID = mresult.body().getServiceID();
                            IsFacebookReg = mresult.body().getIsFacebookReg();


                            // ISACTIVE=false;
                            mUser.setUserID(userID);
                            mUser.setMsisdn(msisdn);
                            mUser.setIsActivated(ISACTIVE);
                            mUser.setUserImage(UserImage);
                            mUser.setUserToken(accesstoken);
                            mUser.setIsSubsc(UserStatusId);
                            mUser.setServiceID(ServiceID);
                            mUser.setIsFacebookReg(IsFacebookReg);

                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.isFaceReg, "true");

                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.USERID, mUser.getUserID());

                            language = LanguageHelper.getCurrentLanguage(getApplicationContext());
                            Resources resources = Applicationmanager.getContext().getResources();

                            Configuration configuration = resources.getConfiguration();
                            onConfigurationChanged(configuration);
                            DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(0, null, null), 0, 0);


                        } else {
                            ApiUtils.handelErrorCode(context, mresult.code());
                            System.out.println("onFailure");

                        }
                    }


                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                    }
                });


            } else {
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.no_sim), Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.d("no internet found", "FOUND");

            Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());


        boolean isServiceRunning6 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                getApplicationContext());


        boolean isServiceRunning7 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                getApplicationContext());
        boolean isServiceRunning1 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                getApplicationContext());
        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(OfflineVideoService.class.getName(),
                getApplicationContext());

        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                getApplicationContext());

        if (isServiceRunning1) {
            if (OFFLINEService.mNotificationUtils != null) {
                OFFLINEService.mNotificationUtils.getManager().cancel(OFFLINEService.NOTIFICATION_ID);
            }

            Intent i = new Intent(HomeActivity.this, OFFLINEService.class);
            stopService(i);
        }

        if (isServiceRunning2) {
            Intent i = new Intent(HomeActivity.this, OfflineVideoService.class);
            stopService(i);
        }

        if (isServiceRunning4) {
            Intent i = new Intent(HomeActivity.this, KaraokeSongService.class);
            stopService(i);
        }


        if (isServiceRunning7) {
            Intent i = new Intent(HomeActivity.this, VideoService.class);
            stopService(i);
            StopServicefromnotification(VideoService.class);
        }


        if (isServiceRunning3) {
            if (SongService.mNotificationUtils != null) {

                SongService.mNotificationUtils.getManager().cancel(SongService.NOTIFICATION_ID);
            }
            Intent i = new Intent(HomeActivity.this, SongService.class);
            stopService(i);

            StopServicefromnotification(SongService.class);
        }

        if (isServiceRunning6) {
            if (SongServiceradio.mNotificationUtils != null) {

                SongServiceradio.mNotificationUtils.getManager().cancel(SongServiceradio.NOTIFICATION_ID);
            }
            Intent i = new Intent(HomeActivity.this, SongServiceradio.class);
            stopService(i);

            StopServicefromnotification(SongServiceradio.class);
        }
        boolean isServiceRunning8 = UtilFunctions.isServiceRunning(AdsPopUpService.class.getName(),
                getApplicationContext());

        if (isServiceRunning8) {
            Intent i = new Intent(HomeActivity.this, AdsPopUpService.class);
            stopService(i);
            if (AdsPopUpService.timer != null) {
                if (Applicationmanager.dialogPoPAds != null && Applicationmanager.dialogPoPAds.isShowing()) {
                    Applicationmanager.dialogPoPAds.dismiss();
                    Applicationmanager.dialogPoPAds.cancel();
                }

                AdsPopUpService.timer.cancel();
                AdsPopUpService.timer.purge();
                AdsPopUpService.timer = null;
            }

        }
        if (dialog != null) {
            dialog.dismiss();
            dialog.hide();
            handler = null;
        }
        android.os.Process.killProcess(android.os.Process.myPid());

        System.exit(0);
    }

    private void StopServicefromnotification(Class name) {
        Intent i = new Intent(context, name);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (HomeActivity.activity != null) {
            HomeActivity.activity.finish();
        }
        context.stopService(i);
    }

    private void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }
/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        language = LanguageHelper.getCurrentLanguage(getApplicationContext());
        Context context = LocaleHelper.setLocale(this, language);
        Applicationmanager.setContext(context);

        OnCreateFunction();

    }
*/

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();

        if (extras != null) {
            // get link from googleplay and apen application then open track
            // which sent
            if (extras.containsKey("Trackid")) {
                // extract the extra-data in the Notification
                String trackid = extras.getString("Trackid");
                if (trackid != null) {
                    GetSingerTracksFrmTrack(trackid);
                    getIntent().removeExtra("Trackid");
                }
            }

        }


        if (extras != null) {
            // get link from googleplay and apen application then open track
            // which sent
            if (extras.containsKey("Videoid")) {
                // extract the extra-data in the Notification
                String videoid = extras.getString("Videoid");
                if (videoid != null) {
                    GetSingerVideoFrmTrack(videoid);
                    getIntent().removeExtra("videoid");
                }
            }

        }

        if (extras != null) {
            if (extras.containsKey("Exit me")) {
                // extract the extra-data in the Notification

                StopService();
                finish();
            }
        }

        if (extras != null) {
            if (extras.containsKey("uoloadimg")) {
                // extract the extra-data in the Notification
                language = LanguageHelper.getCurrentLanguage(getApplicationContext());
                Resources resources = Applicationmanager.getContext().getResources();

                Configuration configuration = resources.getConfiguration();
                onConfigurationChanged(configuration);
                DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(9, null, null), 9, 0);


            }
        }

        if (extras != null) {
            if (extras.containsKey("loadinternational")) {
                // extract the extra-data in the Notification
                language = LanguageHelper.getCurrentLanguage(getApplicationContext());
                Resources resources = Applicationmanager.getContext().getResources();

                Configuration configuration = resources.getConfiguration();
                onConfigurationChanged(configuration);

                //     DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(0, null, null), 0, 0);


            }
        }
        if (extras != null) {
            if (extras.containsKey("uoloadimgfromfriends")) {
                // extract the extra-data in the Notification
                language = LanguageHelper.getCurrentLanguage(getApplicationContext());
                Resources resources = Applicationmanager.getContext().getResources();

                Configuration configuration = resources.getConfiguration();
                onConfigurationChanged(configuration);

                DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(5, null, null), 5, 0);

            }
        }

        if (extras != null) {
            if (extras.containsKey("changelanguage")) {
                // extract the extra-data in the Notification
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    handler = null;

                }
                if (popupWindowlang != null && popupWindowlang.isShowing()) {
                    popupWindowlang.dismiss();
                    popupWindowlang = null;
                }

                Resources resources = Applicationmanager.getContext().getResources();

                Configuration configuration = resources.getConfiguration();
                onConfigurationChanged(configuration);

                //   DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(9, null, null), 9, 0);


            }
        }

        if (extras != null) {
            if (extras.containsKey("Exit")) {
                StopService();

                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Dismiss Notification
                notificationmanager.cancel(0);
                // finish();
            }
        }

        if (extras != null) {
            if (extras.containsKey("id")) {
                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Dismiss Notification
                notificationmanager.cancel(0);
                // extract the extra-data in the Notification
                ArrayList<String> arrayList = new ArrayList<>();

                arrayList.add(extras.getString("id"));
                Gson gson = new Gson();
                String httpParamJSONList = gson.toJson(arrayList);

                SharedPreferences prefs = getSharedPreferences("Inboxopened", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Inboxopened1", httpParamJSONList);

                editor.apply();
//track
                String typemessage = extras.getString("messagetype");
                String msg = extras.getString("id");
                getIntent().removeExtra("messagetype");
                getIntent().removeExtra("id");

                switch (Integer.parseInt(typemessage)) {
                    case 1:
                        Log.i("pushimtent", msg + typemessage);
                        GetSingerTracksFrmTrack(msg);

                        break;
                    //Album

                    case 2:
                        Intent albumIntent = new Intent(HomeActivity.this, AlbumTabsActivity.class);
                        albumIntent.putExtra("id", msg);
                        startActivity(albumIntent);
                        break;
                    //playlist

                    case 3:
                        Intent albumIntent1 = new Intent(HomeActivity.this, PlayListTracksActivity.class);
                        albumIntent1.putExtra("id", msg);
                        startActivity(albumIntent1);
                        break;
                    //artist
                    case 4:

                        Intent albumIntent2 = new Intent(HomeActivity.this, ArtistTabsActivity.class);
                        albumIntent2.putExtra("playlistId", Integer.valueOf(msg));
                        startActivity(albumIntent2);
                        break;
                    //video
                    case 5:
                        Log.i("pushimtent", msg + typemessage);
                        GetSingerVideoFrmTrack(msg);
                        break;

                    case 6:
                        Log.i("pushimtent", msg + typemessage);
                        Intent radioIntent = new Intent(HomeActivity.this, FavoritesRadio.class);
                        radioIntent.putExtra("radioIntent", Integer.valueOf(msg));
                        startActivity(radioIntent);
                        break;
                }
            }
        }
    }

    private void GetSingerTracksFrmTrack(String string) {

        startwhellprogress();
        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSingerTracksFrmTrack + "?trackId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.GetSingerTracksFrmTrack(fav_url).enqueue(new Callback<Similartrackres>() {

            @Override
            public void onResponse(@NonNull Call<Similartrackres> call, @NonNull Response<Similartrackres> mresult) {
                if (mresult.isSuccessful()) {
                    Similartrackres response = mresult.body();
                    if (response != null) {
                        listoftrack = response;
                        SetSimilartrackres();
                    }
                    progBar.setVisibility(View.GONE);


                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Similartrackres> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        language = LanguageHelper.getCurrentLanguage(context);


        onNewIntent(getIntent());
        Applicationmanager.setContext(HomeActivity.this);
        selectedListItem = 0;

        callbackManager = CallbackManager.Factory.create();
        if (isfbRegisterd.equalsIgnoreCase("true")) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            handler = null;
        }

        SlideGallaery.startAutoScroll();
        topic_image.startAutoScroll();
        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());

        boolean isServiceRunning6 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                getApplicationContext());

        if (!isServiceRunning3
                && !isServiceRunning6) {
            audioRelative.setVisibility(View.GONE);
        } else {
            audioRelative.setVisibility(View.VISIBLE);
            audioRelative.setEnabled(true);

        }
        try {
            boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning) {
                changeUI();
            }


            boolean isServiceRunning7 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                    getApplicationContext());
            if (isServiceRunning7) {
                changeUIradio();
            }

            if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && !SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {

                if (AdsPopUpService.Popupads != null && AdsPopUpService.Popupads.size() > 0) {
                    boolean isServiceRunning2 = UtilFunctions.isServiceRunning(AdsPopUpService.class.getName(),
                            context);
                    if (!isServiceRunning2) {
                        AdsPopUpService.Popupads = Popupads;

                        Intent i = new Intent(getApplicationContext(), AdsPopUpService.class);
                        startService(i);


                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomAlert(int fav) {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, nullParent);
        ImageView image = toastRoot.findViewById(R.id.ivtoast);
        if (fav == 1) {
            image.setImageResource(R.mipmap.heart);

        }
        if (fav == 0) {
            image.setImageResource(R.mipmap.brokenheart1);

        }

        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    public void Add_Removefavourite(String trackid2, final Context context2, final String favourite) {
        startwhellprogress();

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
                    HandleFavUi(mresult.body(), favourite);

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

    private void HandleFavUi(Addfavourite_Response response, final String favourite) {
        like = response.getResultDesc();
        progBar.setVisibility(View.GONE);
        if (favourite.equalsIgnoreCase("1")) {

            showCustomAlert(1);


            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {


                if (language.equalsIgnoreCase("ar")) {
                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);


                }
                if (language.equalsIgnoreCase("en")) {
                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                }
                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsFavourite("true");

            }

            Like.setVisibility(View.GONE);

            dislike.setVisibility(View.VISIBLE);

        } else {
            if (favourite.equalsIgnoreCase("0")) {
                showCustomAlert(0);
                boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                        getApplicationContext());
                if (isServiceRunning3) {
                    if (language.equalsIgnoreCase("ar")) {
                        PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                        // PlayerAcreenActivity.SlideAdaptor.notifyDataSetChanged();
                        // gallery.invalidate();

                    }
                    if (language.equalsIgnoreCase("en")) {
                        PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                        // View wantedView
                        // =SlideAdaptor.getView(PlayerConstants.SONG_NUMBER,
                        // null, null);
                        // TextView
                        // tv=(TextView)wantedView.findViewById(R.id.likescount);

                        // tv.setText(like + " " + "Likes");
                        // PlayerAcreenActivity.SlideAdaptor.notifyDataSetChanged();
                        // gallery.invalidate();
                    }
                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsFavourite("false");

                }


                Like.setVisibility(View.VISIBLE);

                dislike.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        SlideGallaery.stopAutoScroll();
        topic_image.stopAutoScroll();
    }


    private void SetSimilartrackres() {
        if (listoftrack.size() > 0) {
            mAlbumList = new ArrayList<>();
            // Albumconstant.SONGS_LIST = mAlbumList;
            for (int j = 0; j < listoftrack.size(); j++) {
                mPlayist = new TrackObject();

                mPlayist.setAlbumId(listoftrack.get(j).getAlbumId());
                mPlayist.setAlbumEnName(listoftrack.get(j).getAlbumEnName());
                mPlayist.setAlbumArName(listoftrack.get(j).getAlbumArName());
                mPlayist.setSingerEnName(listoftrack.get(j).getSingerEnName());
                mPlayist.setSingerArName(listoftrack.get(j).getSingerArName());
                mPlayist.setTrackImage(listoftrack.get(j).getTrackImage());
                mPlayist.setTrackEnName(listoftrack.get(j).getTrackEnName());
                mPlayist.setTrackArName(listoftrack.get(j).getTrackArName());
                mPlayist.setTrackId(listoftrack.get(j).getTrackId());
                mPlayist.setTrackPath(listoftrack.get(j).getTrackPath());
                mPlayist.setIsFavourite(listoftrack.get(j).getIsFavourite());
                mPlayist.setIsRBT(listoftrack.get(j).getIsRBT());
                mPlayist.setSingerId(listoftrack.get(j).getSingerId());
                mPlayist.setLikesCount(listoftrack.get(j).getLikesCount());
                mPlayist.setListenCount(listoftrack.get(j).getListenCount());
                mPlayist.setLyricFile(listoftrack.get(j).getLyricFile());
                mPlayist.setHasLyrics(listoftrack.get(j).getHasLyrics());
                mPlayist.setTrackLength(listoftrack.get(j).getTrackLength());
                mPlayist.setVideoID(listoftrack.get(j).getVideoID());
                mPlayist.setIsPremium(listoftrack.get(j).getIsPremium());
                mPlayist.setIsDownloaded(listoftrack.get(j).getIsDownloaded());

                mAlbumList.add(mPlayist);

            }

            PlayerConstants.SONGS_LIST = mAlbumList;
            PlayerConstants.SONG_NUMBER = 0;

            if (Constants.isNetworkOnline(HomeActivity.this)) {

                // audioRelative.setVisibility(View.VISIBLE);
                StopService();

                Log.e("list clicked ", "here");
                // streamService = new Intent(MainActivity.this,
                // StreamService.class);
                // startService(streamService);
                startwhellprogress();
                audioRelative.setEnabled(false);

                // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                Log.e("list clicked ", "here");
                trackid = mAlbumList.get(PlayerConstants.SONG_NUMBER).getTrackId();
                favo = mAlbumList.get(PlayerConstants.SONG_NUMBER).getIsFavourite();
                if (favo.equalsIgnoreCase("true")) {
                    // change icon
                    // make button disabled
                    Like.setVisibility(View.GONE);

                    dislike.setVisibility(View.VISIBLE);
                }

                if (favo.equalsIgnoreCase("false")) {
                    dislike.setVisibility(View.GONE);

                    Like.setVisibility(View.VISIBLE);
                    // make button enable

                }
                // linearplayer.setVisibility(View.VISIBLE);
                PlayerConstants.SONG_PAUSED = false;
                boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                        getApplicationContext());
                if (!isServiceRunning) {
                    Intent i = new Intent(getApplicationContext(), SongService.class);
                    startService(i);
                    // progBar.setVisibility(View.GONE);

                } else {
                    PlayerConstants.SONG_CHANGE_HANDLER
                            .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }

                changeUI();


                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {
                Toast.makeText(HomeActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void GetSingerVideoFrmTrack(String string) {
        HomeActivity.startwhellprogress();
        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetVideoDataFrmID + "?videoClipId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();

        service.GetVideoDataFrmID(fav_url).enqueue(new Callback<GetVideoDataFrmIDResponse>() {

            @Override
            public void onResponse(@NonNull Call<GetVideoDataFrmIDResponse> call, @NonNull Response<GetVideoDataFrmIDResponse> mresult) {
                if (mresult.isSuccessful()) {
                    GetVideoDataFrmIDResponse response = mresult.body();
                    if (response != null) {
                        listofvideos = response;
                        SetVideosPlayer(listofvideos);
                    }
                    progBar.setVisibility(View.GONE);
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<GetVideoDataFrmIDResponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });

    }

    private void SetVideosPlayer(ArrayList<VideoObjectResponse> listofvideos) {
        if (listofvideos.size() > 0) {

            ArrayList<VideoObjectResponse> mVideoslist = new ArrayList<>();
            mVideoslist = listofvideos;
            // Albumconstant.SONGS_LIST = mAlbumList;


            PlayerConstants.SONGS_LISTVideo = mVideoslist;
            PlayerConstants.SONG_NUMBER = 0;
            if (Constants.isNetworkOnline(context)) {

                Intent player = new Intent(context, VideoPlayerActivity.class);

                player.putExtra("track", PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER));

                startActivity(player);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    // other 'case' lines to check for other
    // permissions this app might request
}


