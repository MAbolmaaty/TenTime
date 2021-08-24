package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlVideo;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 02-Jul-17.
 */

public class VideoPlayerActivity<T> extends ActivityMainRunnuing implements SeekBar.OnSeekBarChangeListener {
    public static SeekBar seek_bar;
    public static Handler seekHandler = new Handler();
    public static ImageView next;
    public static ImageView prev;
    public static ImageView play;
    public static Button playslider;
    public static RelativeLayout progBar;
    public static TextView textBufferDuration;
    public static TextView textDuration;
    public static boolean isShuffle;
    public static boolean isRepeat;
    public static Activity activity;
    public static RelativeLayout relbackground;
    public static ImageView album_menu, ivenlarge;
    public static Runnable run = () -> seekUpdation();
    public static TextView likescount;
    public static PlayerView surfaceView;
    static ImageView pause;
    static ImageView Like;
    static ImageView dislike, calltone, download;
    static Context context;
    static ImageView btnrepeat;
    static ImageView btnshuffle;
    static ProgressWheel pb1;
    static ArrayList<VideoObjectResponse> mPlaylistObjects;
    static Bitmap croppedBmp;
    static Boolean backflag;
    private static Application activity1;
    private static TextView songname, albumname, listencount;
    public Runnable Update;
    RelativeLayout rlcon, song_titles_relative, top_header_relative;
    String trackID;
    ArrayList<TrackObject> NewmtrackList;
    VideoObjectResponse mtrack;
    ImageView close_player, add_img;
    String isfav;
    int progressstop;
    Handler handler = new Handler();
    SeekBar music = null;
    AudioManager mgr = null;
    CallbackManager callbackManager;
    String like;
    String language;
    Dialog dialog;
    Boolean mfullscreen = false;
    private Runnable runnable;
    private boolean pause_stop;

    public static void seekUpdation() {


        if (VideoService.isReady) {
            VideoService.updateVideoProgress();
            textDuration.setText(" " + UtilFunctions.getDuration(VideoService.totalDuration));
            // Displaying time completed playing
            textBufferDuration.setText(" " + UtilFunctions.getDuration(VideoService.getCurrentPosition()));
        }
        seekHandler.postDelayed(run, 1000);

    }

    public static void changeUI() {

        updateUI();
        changeButton();
    }

    public static void changeButton() {
        if (PlayerConstants.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    public static void updateUI() {
        try {

            VideoObjectResponse data = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER);

            String language = LanguageHelper.getCurrentLanguage(context);


            if (language.equalsIgnoreCase("ar")) {
                songname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getSingerArName());
                albumname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoArName());
                likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount() + "   ");
                listencount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getViewCount());

            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getSingerEnName());
                albumname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoEnName());
                likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());
                listencount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getViewCount());

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

            String imgpath = ServerConfig.Video_Imagepath + PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoPoster();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            Log.e("vpath", final_imgpath);
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

        if (VideoPlayerActivity.seekHandler != null) {
            VideoPlayerActivity.seekHandler.removeCallbacks(VideoPlayerActivity.run);
        }
    }

    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")

    private static void setBg(RelativeLayout layout, BitmapDrawable TileMe) {
        layout.setBackground(TileMe);
    }

    public void playsong_click() {
        StopService();
        Log.e("list clicked ", "here");
        startwhellprogress();

        String language = LanguageHelper.getCurrentLanguage(context);


        if (language.equalsIgnoreCase("ar")) {
            songname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getSingerArName());
            albumname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoArName());
            likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount() + "   ");
            listencount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getViewCount());

        }
        if (language.equalsIgnoreCase("en")) {
            songname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getSingerEnName());
            albumname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoEnName());
            likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());
            listencount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getViewCount());

        }
        Log.e("list clicked ", "here");
        String favo = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getIsFavourite();
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

        PlayerConstants.SONG_PAUSED = false;
        boolean isServiceRunning = UtilFunctions.isServiceRunning(VideoService.class.getName(), context);
        if (!isServiceRunning) {
            Intent i = new Intent(context, VideoService.class);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        FacebookSdk.sdkInitialize(this); // ####### Facebook Sign In Coding
        setContentView(R.layout.videoplayerscreen);
        context = getApplicationContext();
        pause_stop = false;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("track")) {
                mtrack = getIntent().getExtras().getParcelable("track");
                mPlaylistObjects = new ArrayList<>();
                mPlaylistObjects.add(mtrack);

            }
        }
        activity1 = getApplication();
        Applicationmanager.setContext(VideoPlayerActivity.this);

        language = LanguageHelper.getCurrentLanguage(context);
        backflag = false;
        callbackManager = CallbackManager.Factory.create();
        InitializeUi();

    }

    private void InitializeUi() {
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        music = findViewById(R.id.seekBar1);
        initBar(music, AudioManager.STREAM_MUSIC);
        relbackground = findViewById(R.id.relbackground);
        Log.e("track ID inplayerscreen", trackID + "here");
        album_menu = findViewById(R.id.album_menu);
        surfaceView = findViewById(R.id.Gallery01);
        songname = findViewById(R.id.song_title);
        albumname = findViewById(R.id.album_title);
        likescount = findViewById(R.id.likescount);
        listencount = findViewById(R.id.listencount);
        ivenlarge = findViewById(R.id.ivenlarge);
        rlcon = findViewById(R.id.rlcon);
        song_titles_relative = findViewById(R.id.song_titles_relative);
        top_header_relative = findViewById(R.id.top_header_relative);
        seek_bar = findViewById(R.id.seek_bar);
        seek_bar.setOnSeekBarChangeListener(this);
        btnrepeat = findViewById(R.id.btnrepeat);
        if (isRepeat) {

            btnrepeat.setImageResource(R.mipmap.reapeaton);
        }

        btnshuffle = findViewById(R.id.btnshuffle);
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

        setListeners();


        add_img = findViewById(R.id.add_img);
        add_img.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent img = new Intent(VideoPlayerActivity.this, AddtoVideoActivity.class);
            VideoObjectResponse trask = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER);
            if (PlayerConstants.SONGS_LISTVideo != null & PlayerConstants.SONGS_LISTVideo.size() > 0) {
                img.putExtra("trask", trask);
                img.putExtra("FromPlayer", "no");
                startActivity(img);
            } else {
                StopService();
                Intent intent = new Intent(VideoPlayerActivity.this, HomeActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }

        });
        close_player.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            boolean isServiceRunning7 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                    getApplicationContext());


            if (isServiceRunning7) {
                StopService();
            }

            Intent intent = new Intent(VideoPlayerActivity.this, HomeActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        });
        ivenlarge.setOnClickListener(v -> {
            // TODO Auto-generated method stub\

            if (mfullscreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                mfullscreen = false;

            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                mfullscreen = true;

            }

            if (Settings.System.getInt(getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    }
                }, 10000);
            }
        });
        if (PlayerConstants.SONGS_LISTVideo != null && PlayerConstants.SONGS_LISTVideo.size() > 0) {
            playsong_click();
        }

    }

    private void changeToLandscape() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rlcon.setVisibility(View.GONE);
        song_titles_relative.setVisibility(View.GONE);
        top_header_relative.setVisibility(View.GONE);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {

        super.onConfigurationChanged(newConfig);

// Checks the orientation of the screen for landscape and portrait
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           // Toast.makeText(this, "“landscape”", Toast.LENGTH_SHORT).show();
            surfaceView.setPlayer(VideoService.mp);
                surfaceView.setControllerAutoShow(true);
                surfaceView.setUseController(true);
                changeToLandscape();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this," “portrait”", Toast.LENGTH_SHORT).show();
            rlcon.setVisibility(View.VISIBLE);
                song_titles_relative.setVisibility(View.VISIBLE);
                top_header_relative.setVisibility(View.VISIBLE);
                surfaceView.setControllerAutoShow(false);
                surfaceView.setUseController(false);
        }
//newConfig
//        int orient = newConfig.orientation;
//        switch(orient)
//        {
//            case Configuration.ORIENTATION_LANDSCAPE:
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                surfaceView.setPlayer(VideoService.mp);
//                surfaceView.setControllerAutoShow(true);
//                surfaceView.setUseController(true);
//                changeToLandscape();
//                mfullscreen = true;
//
//                break;
//            case Configuration.ORIENTATION_PORTRAIT:
//                rlcon.setVisibility(View.VISIBLE);
//                song_titles_relative.setVisibility(View.VISIBLE);
//                top_header_relative.setVisibility(View.VISIBLE);
//                surfaceView.setControllerAutoShow(false);
//                surfaceView.setUseController(false);
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                mfullscreen = false;
//
//                break;
//
//        }
    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }

    @Override
    public void onBackPressed() {
        boolean isServiceRunning7 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                getApplicationContext());

        this.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (isServiceRunning7) {
            StopService();
        }

        Intent intent = new Intent(VideoPlayerActivity.this, HomeActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    private void setListeners() {
        prev.setOnClickListener(v -> {

            ControlVideo.previousControl(getApplicationContext());
            ChangeScreenData();

        });

        pause.setOnClickListener(v -> {

            PlayerConstants.SONGnext = false;

            ControlVideo.pauseControl(getApplicationContext());
        });

        play.setOnClickListener(v -> {
            PlayerConstants.SONGnext = false;

            ControlVideo.playControl(getApplicationContext());
        });

        next.setOnClickListener(v -> {

            ControlVideo.nextControl(getApplicationContext());
            ChangeScreenData();


        });

        btnrepeat.setOnClickListener(arg0 -> {
            if (isRepeat) {
                isRepeat = false;
                VideoService.isRepeat = false;
                btnrepeat.setImageResource(R.mipmap.contenous);
            } else {
                // make repeat to true
                isRepeat = true;
                // make shuffle to false
                isShuffle = false;
                btnrepeat.setImageResource(R.mipmap.reapeaton);
                VideoService.isShuffle = false;
                VideoService.isRepeat = true;

                btnshuffle.setImageResource(R.mipmap.shuffel);
            }
        });
        btnshuffle.setOnClickListener(arg0 -> {
            if (isShuffle) {
                isShuffle = false;
                VideoService.isShuffle = false;
                btnshuffle.setImageResource(R.mipmap.shuffel);
            } else {
                // make repeat to true
                isShuffle = true;
                VideoService.isRepeat = false;

                VideoService.isShuffle = true;

                // make shuffle to false
                isRepeat = false;
                btnshuffle.setImageResource(R.mipmap.shuffleon);
                btnrepeat.setImageResource(R.mipmap.contenous);
            }

        });


        Like.setOnClickListener(v -> {
            startwhellprogress();

            Add_Removefavourite(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID(),
                    getApplicationContext(), "1");

        });

        dislike.setOnClickListener(v -> {
            startwhellprogress();

            Add_Removefavourite(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID(),
                    getApplicationContext(), "0");

        });

    }

    private void ChangeScreenData() {
        String language = LanguageHelper.getCurrentLanguage(context);

if(PlayerConstants.SONGS_LISTVideo!=null&&PlayerConstants.SONGS_LISTVideo.size()>0){
        if (language.equalsIgnoreCase("ar")) {
            songname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getSingerArName());
            albumname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoArName());
            likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount() + "   ");
            listencount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getViewCount());

        }
        if (language.equalsIgnoreCase("en")) {
            songname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getSingerEnName());
            albumname.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoEnName());
            likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount() + "  ");
            listencount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getViewCount());

        }
        String favo = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getIsFavourite();
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

        }}
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPause", "onPause");

        if (Util.SDK_INT <= 23) {
            VideoService.releasePlayer();
            pause_stop = true;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStop", "onStop");

        boolean isServiceRunning7 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                getApplicationContext());
        if (!isServiceRunning7&&Util.SDK_INT > 23) {
            VideoService.releasePlayer();
            pause_stop = true;

        }
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

    public void Add_Removefavourite(String trackid2, final Context context2, final String favourite) {
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddFavouriteVideoClip + "?videoClipId=" + trackid2 + "&userId="
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
            PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setIsFavourite("true");
            isfav = "true";
            HomeActivity.favo = "true";

            SharedPreferences sharedpreferences = getSharedPreferences("fav", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("fav", isfav);

            editor.apply();
            Like.setVisibility(View.GONE);

            dislike.setVisibility(View.VISIBLE);
            if (language.equalsIgnoreCase("ar")) {
                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());

            }
            if (language.equalsIgnoreCase("en")) {
                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);

                likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());
            }

        } else {
            if (favourite.equalsIgnoreCase("0")) {
                showCustomAlert(0);

                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setIsFavourite("false");
                HomeActivity.favo = "false";
                isfav = "false";
                SharedPreferences sharedpreferences = getSharedPreferences("fav", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("fav", isfav);

                editor.apply();
                Like.setVisibility(View.VISIBLE);

                dislike.setVisibility(View.GONE);

                if (language.equalsIgnoreCase("ar")) {
                    PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                    likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());


                }
                if (language.equalsIgnoreCase("en")) {
                    PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                    likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());


                }
            }
        }
    }

    public void showCustomAlert(int fav) {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        final ViewGroup nullparent = null;
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
    protected void onRestart() {
        super.onRestart();
        Log.d("restart", "restart");
        Applicationmanager.setContext(VideoPlayerActivity.this);

        if (pause_stop) {
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");

        Applicationmanager.setContext(VideoPlayerActivity.this);

        if (Util.SDK_INT <= 23 && pause_stop) {
            VideoService.initializePlayer();
        }
        if (isRepeat) {

            btnrepeat.setImageResource(R.mipmap.contenouson);
        }

        if (isShuffle) {
            btnshuffle.setImageResource(R.mipmap.shuffleon);
        }
        boolean isServiceRunning = UtilFunctions.isServiceRunning(VideoService.class.getName(), getApplicationContext());
        if (isServiceRunning) {
            if (HomeActivity.progBar != null) {
                HomeActivity.progBar.setVisibility(View.GONE);
            }

            updateUI();
//            if (backflag) {
//                startwhellprogress();
//                if (VideoFullScreenActivity.surfaceView != null) {
//                    VideoFullScreenActivity.surfaceView.getPlayer().release();
//
//                }
//                surfaceView.setPlayer(VideoService.mp);
//                ChangeScreenData();
//                backflag = false;
//                progBar.setVisibility(View.GONE);
//            }

        }
        changeButton();
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
        VideoService.seekVideo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


}



