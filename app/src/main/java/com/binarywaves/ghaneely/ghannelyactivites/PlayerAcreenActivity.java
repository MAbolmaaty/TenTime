package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.DataBaseHandler;
import com.Ghannely_Encrypt_Decrypt_Tracks.DownloadAndEncryptFileTask;
import com.Ghannely_Encrypt_Decrypt_Tracks.Encryption_DecryptionAudio;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.customBlock.CustomHintContentHolder;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghaneelycashing.Utils;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.KaraokeTabActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelyadaptors.PlayergalleryAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.SimilartrackStrip;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OFFLINEService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackDownloadObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackres;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackstripresponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.binarywaves.ghaneely.ghannelyutils.DbBitmapUtility;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.joanfuentes.hintcase.HintCase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerAcreenActivity<T> extends ActivityMainRunnuing implements SeekBar.OnSeekBarChangeListener {
    private static FancyCoverFlow gallery;
    public static SeekBar seek_bar;
    public static Handler seekHandler = new Handler();
    public static ImageView next;
    public static ImageView prev;
    private static ImageView play;
    public static Button playslider;
    public static RelativeLayout progBar;
    private static TextView textBufferDuration;
    private static TextView textDuration;
    private static boolean isShuffle;
    private static boolean isRepeat;
    public static Activity activity;
    private static RelativeLayout relbackground;
    private static ImageView album_menu;
    public static AutoScrollViewPager SlideGallaery;
    private static ArrayList<Similartrackstripresponse> mSlideList;
    public static ArrayList<Similartrackstripresponse> listoftrack;

    public static Runnable run = PlayerAcreenActivity::seekUpdation;
    private static String ENCRYPTED_FILE_NAME = "encrypted.m4a";
    public static CircleProgressView mCircleView;
    public static LinearLayout lincricle;
    static PlayergalleryAdaptor<TrackObject> SlideAdaptor;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static ImageView calltone;
    private static ImageView download;
    private static Context context;
    private static Context cx;
    private static ImageView btnrepeat;
    private static ImageView btnshuffle;
    static Bitmap bitmap;
    private static ProgressWheel pb1;
    public static SimilartrackStrip similaradapter;
    private static ArrayList<TrackObject> mPlaylistObjects;
    private static Application activity1;
    private static HintCase hint;
    private String trackID;
    private TrackObject mtrack;
    private ImageView close_player;
    private ImageView add_img;
    private String isfav;
    private int progressstop;
    Handler handler = new Handler();
    private SeekBar music = null;
    private AudioManager mgr = null;
    private CallbackManager callbackManager;
    private String like;
    private String language;
    private Dialog dialog;
    private Encryption_DecryptionAudio enc;

    @SuppressLint("SetTextI18n")
    private static void seekUpdation() {

        if (SongService.isReady && SongService.mp != null) {
            SongService.updateVideoProgress();

            textDuration.setText(" " + UtilFunctions.getDuration(SongService.totalDuration));
            // Displaying time completed playing
            textBufferDuration.setText(" " + UtilFunctions.getDuration(SongService.mp.getCurrentPosition()));
        }
        seekHandler.postDelayed(run, 1000);

    }

    public static void changeUI() {

        updateUI();
        changeButton();
    }


    private static void changeButton() {
        if (PlayerConstants.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    public static void playsong_click() {
        PlayerConstants.SONG_NUMBER = 0;
        StopService();

        Log.e("list clicked ", "here");
        // streamService = new Intent(MainActivity.this,
        // StreamService.class);
        // startService(streamService);
        startwhellprogress();

        // intiPlayer(mtrackList.get(position).getTrackPath(),false);
        Log.e("list clicked ", "here");
        String favo = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite();
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
        if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsDownloaded().equalsIgnoreCase("true")) {
            // make button disabled
            download.setImageResource(R.mipmap.downloadmainon);

        }
        if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsDownloaded()
                .equalsIgnoreCase("false")) {


            download.setImageResource(R.mipmap.downloadmain);

            // make button enable
        }

        // linearplayer.setVisibility(View.VISIBLE);
        PlayerConstants.SONG_PAUSED = false;
        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (!isServiceRunning) {
            Intent i = new Intent(context, SongService.class);
            context.startService(i);

        } else {
            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
        }

        updateUI();

        changeButton();
        seekUpdation();
        // updater = new VideoProgressUpdater();

        Log.d("TAG", "TAG Tapped INOUT(OUT)");

    }

    public static void updateUI() {
        try {

            SlideAdaptor = new PlayergalleryAdaptor<TrackObject>(cx, R.layout.playerpageradapter, R.id.Gallery01,
                    PlayerConstants.SONGS_LIST).setInfiniteLoop(true);
            gallery.setAdapter(SlideAdaptor);
            gallery.setSelected(true);
            gallery.setSelection(PlayerConstants.SONG_NUMBER);
            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsRBT().equalsIgnoreCase("true")) {
                long INTERVAL_TIME = 200000;
                new Handler().postDelayed(() -> {
                    if (hint != null && !hint.getView().isShown()) {
                        showHint(calltone);
                    }
                }, INTERVAL_TIME);
            }
            gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    if (view != null) {
                        final ImageView iv = view.findViewById(R.id.Gallery01);
                        if (position >= PlayerConstants.SONGS_LIST.size()) {
                            position = position % PlayerConstants.SONGS_LIST.size();
                            view.requestLayout();
                        }
                        Picasso.with(context).load(((ServerConfig.ALBUM_IMAGE_PATH + PlayerConstants.SONGS_LIST.get(position).getTrackImage()))).error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(iv, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();
                                GaussianBlur gaussian = new GaussianBlur(context);
                                gaussian.setMaxImageSize(60);
                                gaussian.setRadius(25); // max

                                Bitmap output = gaussian.render(bitmap, true);
                                final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);
                                setBg(relbackground, ob);

                            }

                            @Override
                            public void onError() {
                                BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();
                                GaussianBlur gaussian = new GaussianBlur(context);
                                gaussian.setMaxImageSize(60);
                                gaussian.setRadius(25); // max

                                Bitmap output = gaussian.render(bitmap, true);
                                final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);
                                setBg(relbackground, ob);

                            }
                        });
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });
            gallery.setOnItemClickListener((parent, view, position, id) -> {
                        // TODO Auto-generated method stub
                        if (position == PlayerConstants.SONG_NUMBER) {
                            Intent img = new Intent(context, AddToActivity.class);
                            TrackObject trask = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
                            img.putExtra("trask", trask);
                            img.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // clear
                            // all

                            img.putExtra("FromPlayer", "yes");
                            context.startActivity(img);

                        } else {
                            if (Constants.isNetworkOnline(context)) {
                                if (position >= PlayerConstants.SONGS_LIST.size()) {
                                    position = position % PlayerConstants.SONGS_LIST.size();
                                }
                                // audioRelative.setVisibility(View.VISIBLE);
                                StopService();

                                Log.e("list clicked ", "here");
                                // streamService = new Intent(MainActivity.this,
                                // StreamService.class);
                                // startService(streamService);
                                startwhellprogress();

                                // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                                Log.e("list clicked ", "here");
                                String favo = PlayerConstants.SONGS_LIST.get(position).getIsFavourite();
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
                                getsimilartrack(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId());

                                boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                                        context);
                                if (!isServiceRunning) {
                                    Intent i = new Intent(context, SongService.class);
                                    context.startService(i);
                                    // progBar.setVisibility(View.GONE);

                                } else {
                                    PlayerConstants.SONG_CHANGE_HANDLER
                                            .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                                }

                                updateUI();

                                changeButton();
                                seekUpdation();

                                Log.d("TAG", "TAG Tapped INOUT(OUT)");

                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.gnrl_internet_error),
                                        Toast.LENGTH_LONG).show();
                            }

                        }

                    }

            );

            String imgpath = ServerConfig.Image_path
                    + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            // imageLoader.DisplayImage(ServerConfig.Image_path
            // +mtrackList.get(position).getAlbumImgPath(),player_image);
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(album_menu, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    BitmapDrawable drawable = (BitmapDrawable) album_menu.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    GaussianBlur gaussian = new GaussianBlur(context);
                    gaussian.setMaxImageSize(60);
                    gaussian.setRadius(25); // max

                    Bitmap output = gaussian.render(bitmap, true);
                    final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);
                    setBg(relbackground, ob);

                }

                @Override
                public void onError() {
                    BitmapDrawable drawable = (BitmapDrawable) album_menu.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    GaussianBlur gaussian = new GaussianBlur(context);
                    gaussian.setMaxImageSize(60);
                    gaussian.setRadius(25); // max

                    Bitmap output = gaussian.render(bitmap, true);
                    final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);
                    setBg(relbackground, ob);

                }
            });
            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite().equalsIgnoreCase("true")) {
                // make button disabled
                Like.setVisibility(View.GONE);

                dislike.setVisibility(View.VISIBLE);
            }
            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite()
                    .equalsIgnoreCase("false")) {
                Like.setVisibility(View.VISIBLE);

                dislike.setVisibility(View.GONE);
                // make button enable
            }
            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsDownloaded().equalsIgnoreCase("true")) {
                // make button disabled
                download.setImageResource(R.mipmap.downloadmainon);

            }
            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsDownloaded()
                    .equalsIgnoreCase("false")) {


                download.setImageResource(R.mipmap.downloadmain);

                // make button enable
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, activity1);
        tracklisiten.StopService();

    }

    public static void Remove_handlers() {
        // TODO Auto-generated method stub

        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
        }
    }

    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    private static void getsimilartrack(String string) {

        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSimilarTrackLst + "?trackId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        service.getsimilartrack(fav_url).enqueue(new Callback<Similartrackres>() {

            @Override
            public void onResponse(@NonNull Call<Similartrackres> call, @NonNull Response<Similartrackres> mresult) {


                if (mresult.isSuccessful()) {
                    Similartrackres response = mresult.body();
                    if (response != null) {
                        mSlideList = response;
                    }
                    progBar.setVisibility(View.GONE);
                    if (mSlideList != null && mSlideList.size() > 0) {

                        SetSimilartrackStrip();
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Similartrackres> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    private static void SetSimilartrackStrip() {
        similaradapter = new SimilartrackStrip(cx, R.layout.sliderlayout,
                R.id.player_image, mSlideList).setInfiniteLoop(true);
        SlideGallaery.setAdapter(similaradapter);



        SlideGallaery.setInterval(20000);
        SlideGallaery.startAutoScroll();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private static void setBg(RelativeLayout layout, BitmapDrawable TileMe) {
        layout.setBackground(TileMe);
    }

    private static void showHint(final View view) {
        int background;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            background = context.getResources().getColor(R.color.darkgray, context.getTheme());

        } else {
            background = context.getResources().getColor(R.color.darkgray);
        }
        CustomHintContentHolder blockInfo = new CustomHintContentHolder.Builder(view.getContext())

                .setContentText(cx.getResources().getString(R.string.calltonehint))
                .setBorder(R.dimen.bubble_border, R.color.ghaneely_orange)
                .setArrowSize(R.dimen.arrow_width, R.dimen.arrow_height)
                .setBackgroundColor(background)

                .setMargingByResourcesId(R.dimen.activity_vertical_margin,
                        R.dimen.activity_horizontal_margin,
                        R.dimen.activity_vertical_margin,
                        R.dimen.activity_horizontal_margin)
                .setContentPaddingByResourcesId(R.dimen.small_margin,
                        R.dimen.small_margin,
                        R.dimen.small_margin,
                        R.dimen.small_margin)
                .setContentStyle(R.style.content_dark)
                .build();
        hint = new HintCase(((Activity) cx).getWindow().getDecorView());

        hint.setTarget(view, R.dimen.zero_margin)
                .setBackgroundColor(0x00000000)
                .setHintBlock(blockInfo)
                .setCloseOnTouchView(true)
                .show();
    }

    private static void AddUserDownloadTrack(String trackid, int isDownloaded, String downloadtype) {


        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddUserDownloadTrack + "?trackId=" + trackid + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&isDownloaded="
                + isDownloaded + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken) + "&downloadType="
                + downloadtype;
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddUserDownloadTrack(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {


                if (mresult.isSuccessful()) {
                    Addrbtresponse response = mresult.body();

                    Log.i("response", response + "");
                    progBar.setVisibility(View.GONE);
                    if (response != null && response.getResultCode().equalsIgnoreCase("true")) {
                        if (isDownloaded == 1) {
                            download.setImageResource(R.mipmap.downloadon);
                            PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsDownloaded("true");
                            // make button disabled

                        } else {
                            download.setImageResource(R.mipmap.downloadplus);
                            PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsDownloaded("false");

                        }
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });

    }

    public static void ADD_DOWNLOAD_2_DATABASE() {
        DataBaseHandler mDbHandler;
        mDbHandler = DataBaseHandler.getInstance(context);
        Log.d("Insert: ", "Inserting ..");
        BitmapDrawable drawable = (BitmapDrawable) album_menu.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        DbBitmapUtility.getBytes(bitmap);
        TrackDownloadObject trackobject = new TrackDownloadObject();
        trackobject.setTrackImage(DbBitmapUtility.getBytes(bitmap));
        mDbHandler.addItem(new TrackDownloadObject(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackEnName(),PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackArName(), PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingerEnName(), PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getSingerArName(), PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackLength(), trackobject.getTrackImage()));

        // Reading all shops
        Log.d("Reading: ", "Reading all shops..");
//        List<TrackDownloadObject> shops = mDbHandler.getAllDownloads();
//
//        for (TrackDownloadObject shop : shops) {
//            String log = "Id: " + shop.getTrackArName() + " ,Name: " + shop.getTrackEnName() + " ,Address: " + shop.getTrackLength();
//            // Writing shops  to log
//            Log.d("Shop: : ", log);
//        }
        AddUserDownloadTrack(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), 1, "1");

        Toast.makeText(context, context.getResources().getString(R.string.downloadsuccess), Toast.LENGTH_LONG).show();

    }

    private static void DeletefromSdcard_sql(String contentDisposition, File mEncryptedFile) {
        boolean isServiceRunning1 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                context);
        if (isServiceRunning1) {
            if (PlayerConstants.OfflineSONGS_LIST != null && PlayerConstants.OfflineSONGS_LIST.size() > 0) {
                if (PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(contentDisposition)) {


                    Intent i = new Intent(context, OFFLINEService.class);
                    context.stopService(i);

                }
            }
        }
        Log.i("file", mEncryptedFile + "");

        Uri uri = Uri.fromFile(mEncryptedFile);
        File fdelete = new File(uri.getPath());
        if (fdelete.exists()) {
            Boolean delete = FileCache.delete(fdelete);
            if (delete) {
                System.out.println("file Deleted :" + uri.getPath());
                DataBaseHandler mDbHandler;
                mDbHandler = DataBaseHandler.getInstance(context);
                mDbHandler.removeSingleItem(contentDisposition);


            }

        }


    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this); // ####### Facebook Sign In Coding
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_palyer);
        Applicationmanager.setContext(PlayerAcreenActivity.this);

        context = getApplicationContext();
        cx = PlayerAcreenActivity.this;

        mtrack = getIntent().getExtras().getParcelable("track");
        activity1 = getApplication();
        language = LanguageHelper.getCurrentLanguage(context);
        hint = new HintCase(((Activity) cx).getWindow().getDecorView());
        callbackManager = CallbackManager.Factory.create();
        InitializeUi();


    }

    private void InitializeUi() {
        if (!SharedPrefHelper.getSharedString(context, Constants.DownloadKey).equalsIgnoreCase("")) {


            enc = new Encryption_DecryptionAudio(SharedPrefHelper.getSharedString(context, Constants.DownloadKey));
        }
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        music = findViewById(R.id.seekBar1);
        initBar(music, AudioManager.STREAM_MUSIC);
        relbackground = findViewById(R.id.relbackground);

        Log.e("track ID inplayerscreen", trackID + "here");

        album_menu = findViewById(R.id.album_menu);

        gallery = findViewById(R.id.Gallery01);

        SlideGallaery = findViewById(R.id.slider);

        mPlaylistObjects = new ArrayList<>();

        mPlaylistObjects.add(mtrack);
        seek_bar = findViewById(R.id.seek_bar);
        seek_bar.setOnSeekBarChangeListener(this);

        btnrepeat = findViewById(R.id.btnrepeat);
        if (isRepeat) {

            btnrepeat.setImageResource(R.mipmap.reapeaton);
        }

        btnshuffle = findViewById(R.id.btnshuffle);
        isShuffle = SongService.isShuffle;
        isRepeat = SongService.isRepeat;
        if (isShuffle) {
            btnshuffle.setImageResource(R.mipmap.shuffleon);
        }
        pause = findViewById(R.id.pause_img);
        next = findViewById(R.id.next_img);
        prev = findViewById(R.id.prev_img);

        play = findViewById(R.id.play_img);

        Like = findViewById(R.id.like_img);
        dislike = findViewById(R.id.btnlikesdis);
        close_player = findViewById(R.id.close_player);
        textBufferDuration = findViewById(R.id.textBufferDuration);
        textDuration = findViewById(R.id.textDuration);
        if (mtrack.getIsFavourite().equalsIgnoreCase("true")) {
            // change icon
            // make button disabled
            Like.setVisibility(View.GONE);

            dislike.setVisibility(View.VISIBLE);
        }
        if (mtrack.getIsFavourite().equalsIgnoreCase("false")) {
            Like.setVisibility(View.VISIBLE);

            dislike.setVisibility(View.GONE);
            // make button enable
        }
        seekUpdation();
        // updater = new VideoProgressUpdater();

        setListeners();
        if(PlayerConstants.SONGS_LIST!=null&&PlayerConstants.SONGS_LIST.size()>0) {
            getsimilartrack(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId());
        }

        add_img = findViewById(R.id.add_img);
        add_img.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            Intent img = new Intent(PlayerAcreenActivity.this, AddToActivity.class);
            TrackObject trask = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
            img.putExtra("trask", trask);
            img.putExtra("FromPlayer", "yes");
            startActivity(img);
            finish();

        });
        close_player.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            finish();
        });


        calltone = findViewById(R.id.calltone);
        calltone.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (mtrack.getIsRBT().equalsIgnoreCase("true")) {

                showAlert();
            } else {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.calltonenotavailable),
                        Toast.LENGTH_SHORT).show();

            }
        });

        download = findViewById(R.id.download);
        download.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsDownloaded().equalsIgnoreCase("true")) {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.alreadydownloaded),
                            Toast.LENGTH_SHORT).show();

                } else {
                    //  startwhellprogress();
                    lincricle.setVisibility(View.VISIBLE);
                    mCircleView.spin();
                    String str = ServerConfig.AudioUrl + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackPath().replaceAll(" ", "%20");
                    Log.e("audiourl", str);
                    SetupCiper2Download(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), str);
                }
            } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {
                DrawerActivity.showGracePopup(getApplicationContext());
            } else {
                DrawerActivity.Show_GhannelyExtra_Dialog(getApplicationContext());

            }


        });

        lincricle = findViewById(R.id.cirrcleprogress);
        mCircleView = lincricle.findViewById(R.id.circleView);
        mCircleView.setShowTextWhileSpinning(false);
        mCircleView.setTextColorAuto(false);
        mCircleView.setDirection(Direction.CW);

    }

    private void setListeners() {
        prev.setOnClickListener(v -> {
            prev.setEnabled(false);
            prev.setClickable(false);
            next.setEnabled(false);
            next.setClickable(false);

            Controls.previousControl(getApplicationContext());
        });

        pause.setOnClickListener(v -> {

            PlayerConstants.SONGnext = false;

            Controls.pauseControl(getApplicationContext());
        });

        play.setOnClickListener(v -> {
            PlayerConstants.SONGnext = false;

            Controls.playControl(getApplicationContext());
        });

        next.setOnClickListener(v -> {
            prev.setEnabled(false);
            prev.setClickable(false);
            next.setEnabled(false);
            next.setClickable(false);
            Controls.nextControl(getApplicationContext());
        });

        btnrepeat.setOnClickListener(arg0 -> {
            if (isRepeat) {
                isRepeat = false;
                SongService.isRepeat = false;
                btnrepeat.setImageResource(R.mipmap.contenous);
            } else {
                // make repeat to true
                isRepeat = true;
                // make shuffle to false
                isShuffle = false;
                btnrepeat.setImageResource(R.mipmap.reapeaton);
                SongService.isShuffle = false;
                SongService.isRepeat = true;

                btnshuffle.setImageResource(R.mipmap.shuffel);
            }
        });
        btnshuffle.setOnClickListener(arg0 -> {
            if (isShuffle) {
                isShuffle = false;
                SongService.isShuffle = false;
                btnshuffle.setImageResource(R.mipmap.shuffel);
            } else {
                // make repeat to true
                isShuffle = true;
                SongService.isRepeat = false;
                SongService.isShuffle = true;

                // make shuffle to false
                isRepeat = false;
                btnshuffle.setImageResource(R.mipmap.shuffleon);
                btnrepeat.setImageResource(R.mipmap.contenous);
            }

        });


        Like.setOnClickListener(v -> {
            startwhellprogress();

            Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                    getApplicationContext(), "1");

        });

        dislike.setOnClickListener(v -> {
            startwhellprogress();

            Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                    getApplicationContext(), "0");

        });

    }

    private void initBar(SeekBar bar, final int stream) {
        bar.setMax(mgr.getStreamMaxVolume(stream));
        bar.setProgress(mgr.getStreamVolume(stream));

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                mgr.setStreamVolume(stream, progress, AudioManager.FLAG_PLAY_SOUND);
            }

            public void onStartTrackingTouch(SeekBar bar) {
                // no-op
            }

            public void onStopTrackingTouch(SeekBar bar) {
                // no-op
            }
        });
    }


    private void Add_Removefavourite(String trackid2, final Context context2, final String favourite) {
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
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addfavourite_Response> call, @NonNull Throwable t) {
                System.out.println("onFailure");
                progBar.setVisibility(View.GONE);

            }
        });
    }

    private void HandleFavUi(Addfavourite_Response response, final String favourite) {
        like = response.getResultDesc();
        progBar.setVisibility(View.GONE);
        if (favourite.equalsIgnoreCase("1")) {
            showCustomAlert(1);
            PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsFavourite("true");
            isfav = "true";
            HomeActivity.favo = "true";

            SharedPreferences sharedpreferences = getSharedPreferences("fav", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("fav", isfav);

            editor.apply();
            Like.setVisibility(View.GONE);

            dislike.setVisibility(View.VISIBLE);
            if (language.equalsIgnoreCase("ar")) {
                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                Log.i("likecount", like + "");

                SlideAdaptor.notifyDataSetChanged();
                // gallery.invalidate();

            }
            if (language.equalsIgnoreCase("en")) {

                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                Log.i("likecount", like + "");

                SlideAdaptor.notifyDataSetChanged();
                // gallery.invalidate();
            }

        } else {
            if (favourite.equalsIgnoreCase("0")) {
                showCustomAlert(0);
                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsFavourite("false");
                HomeActivity.favo = "false";
                isfav = "false";
                SharedPreferences sharedpreferences = getSharedPreferences("fav", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("fav", isfav);

                editor.apply();
                Like.setVisibility(View.VISIBLE);

                dislike.setVisibility(View.GONE);
                if (language.equalsIgnoreCase("ar")) {
                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                    SlideAdaptor.notifyDataSetChanged();
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
                    SlideAdaptor.notifyDataSetChanged();
                    // gallery.invalidate();

                }
            }
        }
    }

    private void showCustomAlert(int fav) {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullparent = null;
        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, nullparent);
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

    @Override
    protected void onResume() {
        super.onResume();
        SlideGallaery.startAutoScroll();

        if (isRepeat) {

            btnrepeat.setImageResource(R.mipmap.contenouson);
        }

        if (isShuffle) {
            btnshuffle.setImageResource(R.mipmap.shuffleon);
        }
        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext());
        if (isServiceRunning) {
            if (HomeActivity.progBar != null) {
                HomeActivity.progBar.setVisibility(View.GONE);
            }


            updateUI();
            changeButton();

        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        if (fromUser) {
            progressstop = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        SongService.seekVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        SlideGallaery.stopAutoScroll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("ConstantConditions")
    private void showAlert() {
        dialog = new Dialog(PlayerAcreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.customcalltune_popup);

        Button cancel = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            Addtorbt(mtrack.getTrackId(), getApplicationContext());
            dialog.dismiss();

        });

        ImageView image = dialog.findViewById(R.id.ivimage);
        String imgpath = ServerConfig.Image_path
                + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage();
        String final_imgpath = imgpath.replaceAll(" ", "%20");
        // imageLoader.DisplayImage(ServerConfig.Image_path
        // +mtrackList.get(position).getAlbumImgPath(),player_image);
        Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                .placeholder(R.mipmap.defualt_img).into(image);

        dialog.show();
    }

    private void Addtorbt(String string, Context context) {
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.ADDRBTREQUEST + "?trackId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);


        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.ADDRBTREQUEST(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {


                if (mresult.isSuccessful()) {
                    Addrbtresponse response = mresult.body();
                    Log.i("response", response + "");
                    progBar.setVisibility(View.GONE);

                    //convert json string to object

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    private void SetupCiper2Download(String contentDisposition, String audiolink) {
        ENCRYPTED_FILE_NAME = contentDisposition;
        Pattern regex = Pattern.compile("(?<=contentDisposition=\").*?(?=\")");
        Matcher regexMatcher = regex.matcher(contentDisposition);
        if (regexMatcher.find()) {
            ENCRYPTED_FILE_NAME = regexMatcher.group();
        }

        if (Checkexternalstorage() >= 0) {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Not sure if the / is on the path or not
            Encryption_DecryptionAudio.mEncryptedFile = new File(baseDir + File.separator + ENCRYPTED_FILE_NAME);

            Log.i("file", Encryption_DecryptionAudio.mEncryptedFile + "");

            encryptVideo_Audio(audiolink);
        } else {

            Log.i(getClass().getCanonicalName(), "encrypted file found, no need to recreate");
            progBar.setVisibility(View.GONE);
            Toast.makeText(PlayerAcreenActivity.this, "No Space", Toast.LENGTH_LONG).show();
        }


    }

    private Double Checkexternalstorage() {

        Double finalspace = Utils.getAvailableExternalMemorySize();

        Log.i("value", finalspace + "vvv" + finalspace);


        return finalspace;
    }

    private boolean hasFile() {
        return Encryption_DecryptionAudio.mEncryptedFile != null
                && Encryption_DecryptionAudio.mEncryptedFile.exists()
                && Encryption_DecryptionAudio.mEncryptedFile.length() > 0;
    }

    private void encryptVideo_Audio(String url) {

        if (hasFile()) {
            Log.d(getClass().getCanonicalName(), "encrypted file found, no need to recreate");
            DeletefromSdcard_sql(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), Encryption_DecryptionAudio.mEncryptedFile);
        }

        try {
            if (!SharedPrefHelper.getSharedString(context, Constants.DownloadKey).equalsIgnoreCase("")) {
                enc = new Encryption_DecryptionAudio(SharedPrefHelper.getSharedString(context, Constants.DownloadKey));
                Encryption_DecryptionAudio.mCipher.init(Cipher.ENCRYPT_MODE, Encryption_DecryptionAudio.mSecretKeySpec, Encryption_DecryptionAudio.mIvParameterSpec);
                // TODO:
                // you need to encrypt a video somehow with the same key and iv...  you can do that yourself and update
                // the ciphers, key and iv used in this demo, or to see it from top to bottom,
                // supply a url to a remote unencrypted file - this method will downloadplus and encrypt it
                // this first argument needs to be that url, not null or empty...
                new DownloadAndEncryptFileTask(url, Encryption_DecryptionAudio.mEncryptedFile, Encryption_DecryptionAudio.mCipher, 1).execute();
            } else {
                lincricle.setVisibility(View.GONE);
                Toast.makeText(PlayerAcreenActivity.this, getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}