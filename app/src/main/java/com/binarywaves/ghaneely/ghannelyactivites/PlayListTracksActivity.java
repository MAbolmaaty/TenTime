package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.binarywaves.ghaneely.ghannelyadaptors.PlaylistTrackAdaptor;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongServiceradio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.RadioConstant;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistnotificationResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackstripresponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayListTracksActivity extends ActivityMainRunnuing {
    public static PlaylistTrackAdaptor trackgalleryAdaptor;
    public static RelativeLayout audioRelative;
    public static TrackObject data1;
    public static RelativeLayout progBar;
    public static Activity activity;
    public static int selectessong = -1;
    private static ArrayList<Similartrackstripresponse> listoftrack;
    public static ArrayList<TrackObject> mPlaylist;
    private static TrackObject data;
    private static ImageView play;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static Context context;
    private static ImageView back;
    private static ProgressWheel pb1;
    private static String language;
    private static TextView albumname, tvplaylistname, tvsongcount, tvduration;
    private static ImageView player_image, new_song_slider;
    private static TextView songname;
    private String playlistId;
    private ArrayList<TrackObject> mtrackList;
    private DynamicListView gridview;
    String isfav;
    private TextView title_txt;
    private String playlistname;
    private String playlistimage;
    //RelativeLayout pager_relative;
    private ImageView playplaylist;
    private ImageView pauseplaylist;
    private LinearLayout imagecontainer;
    private PlaylistnotificationResponse playlistnotificationResponse;
    private String like;
    private ArrayList<TrackObject> TrackArray;

    // set player in all application
    public static void changeUI(final Context context) {

        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (isServiceRunning) {
            updateUI();
            changeButton();

        }


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(), context);
        if (isServiceRunning3) {
            updateUI3();
            changeButton3();

        }


    }

    private static void updateUI3() {
        try {

            HomeActivity.dataradio = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER);

            if (language.equalsIgnoreCase("ar")) {
                songname.setText(HomeActivity.dataradio.getRadioArName());
                albumname.setText(HomeActivity.dataradio.getRadioArName());
            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(HomeActivity.dataradio.getRadioName());
                albumname.setText(HomeActivity.dataradio.getRadioName());
            }
            Like.setVisibility(View.GONE);

            dislike.setVisibility(View.GONE);

            String imgpath = ServerConfig.Radio_Url + HomeActivity.dataradio.getRadioImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeButton3() {
        if (RadioConstant.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    private static void updateUI() {
        try {
            TrackObject data;
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

            String imgpath = ServerConfig.Image_path + data.getTrackImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeButton() {
        if(trackgalleryAdaptor!=null){
        trackgalleryAdaptor.notifyDataSetInvalidated();}

        if (PlayerConstants.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }

    }

    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
//          requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlisttrackscroll);
        context = getApplicationContext();
        language = LanguageHelper.getCurrentLanguage(context);
        Applicationmanager.setContext(PlayListTracksActivity.this);


        Init_Ui();


        changeUI(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("playlistId")) {
                playlistId = extras.getString("playlistId");
                if (Constants.isNetworkOnline(PlayListTracksActivity.this)) {
                    getTrackList(playlistId);
                    playlistname = getIntent().getExtras().getString("playlistname");
                    title_txt.setText(playlistname);
                    tvplaylistname.setText(playlistname);
                    playlistimage = getIntent().getExtras().getString("playlistimage");
                    Picasso.with(getBaseContext()).load(ServerConfig.PLAYLIST_IMAGE_PATH + playlistimage).error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(new_song_slider);
                    SetBlurryImage();
                } else {
                    Toast.makeText(PlayListTracksActivity.this,
                            getResources().getString(R.string.gnrl_internet_error), Toast.LENGTH_LONG).show();
                }

            }
        }
        if (extras != null) {
            if (extras.containsKey("id")) {
                String id = extras.getString("id");
                if (Constants.isNetworkOnline(PlayListTracksActivity.this)) {
                    getplaylistnot(id);
                } else {

                    Toast.makeText(PlayListTracksActivity.this,
                            getResources().getString(R.string.gnrl_internet_error), Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void Init_Ui() {
        selectessong = -1;

        gridview = findViewById(R.id.gridview);
        title_txt = findViewById(R.id.title_txt);

        new_song_slider = findViewById(R.id.new_song_slider);
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        startwhellprogress();
        StopService();


        imagecontainer = findViewById(R.id.imagecontainer);


        back = findViewById(R.id.navigation_up);

        back.setOnClickListener(v -> finish());

        audioRelative = findViewById(R.id.audioplayer);
        audioRelative.setEnabled(false);
        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);
        playplaylist = findViewById(R.id.playplaylist);
        playplaylist.setActivated(false);
        playplaylist.setClickable(false);
        pauseplaylist = findViewById(R.id.pauseplaylist);
        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);
        tvplaylistname = findViewById(R.id.tvsongName);


        tvsongcount = findViewById(R.id.tvcount);
        tvduration = findViewById(R.id.tvduration);

        PlayerItem_Onclick();

    }

    private void SetBlurryImage() {
        BitmapDrawable drawable = (BitmapDrawable) new_song_slider.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        GaussianBlur gaussian = new GaussianBlur(context);
        gaussian.setMaxImageSize(60);
        gaussian.setRadius(25); // max

        Bitmap output = gaussian.render(bitmap, true);
        final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);

        imagecontainer.setBackground(ob);

    }

    private void PlayerItem_Onclick() {
        play.setOnClickListener(v -> {
            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                PlayerConstants.SONGnext = false;

                Controls.playControl(getApplicationContext());
                playplaylist.setVisibility(View.GONE);
                pauseplaylist.setVisibility(View.VISIBLE);
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
                playplaylist.setVisibility(View.VISIBLE);
                pauseplaylist.setVisibility(View.GONE);
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
                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                        getApplicationContext(), "1");
                // data.setIsFavourite(true);

            }

        });

        dislike.setOnClickListener(v -> {
            startwhellprogress();
            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                        getApplicationContext(), "0");

            }

        });
        audioRelative.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getPlayerScreen();

        });

        playplaylist.setOnClickListener(v -> {
            if (mtrackList != null) {
                if (mtrackList.size() > 0) {
                    playplaylist.setVisibility(View.GONE);
                    pauseplaylist.setVisibility(View.VISIBLE);
                    PlayerConstants.SONGnext = false;

                    boolean isServiceRunning2 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                            getApplicationContext());
                    if (isServiceRunning2) {
                        PlayerConstants.SONGnext = false;

                        Controls.playControl(getApplicationContext());

                    } else {
                        gridview.performItemClick(gridview.getAdapter().getView(0, null, null), 0, 0);

                    }
                }
            } else {
                finish();
            }
        });
        pauseplaylist.setOnClickListener(v -> {
            if (mtrackList != null) {
                if (mtrackList.size() > 0) {
                    playplaylist.setVisibility(View.VISIBLE);
                    pauseplaylist.setVisibility(View.GONE);
                    boolean isServiceRunning2 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                            getApplicationContext());
                    if (isServiceRunning2) {
                        PlayerConstants.SONGnext = false;

                        Controls.pauseControl(getApplicationContext());

                    }

                }
            } else {
                finish();
            }
        });

        gridview.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub

            if (Constants.isNetworkOnline(PlayListTracksActivity.this)) {
                StopService();
                // intiPlayer(mtrackList.get(position).getTrackPath());
                Log.e("Trackpath", mtrackList.get(position).getTrackPath() + "here");
                PlayerConstants.SONGS_LIST = mtrackList;
                selectessong = position;
                trackgalleryAdaptor.notifyDataSetInvalidated();

                startwhellprogress();
                audioRelative.setEnabled(false);
                if (mtrackList.get(position).getIsFavourite().equalsIgnoreCase("true")) {
                    // change icon
                    // make button disabled
                    Like.setVisibility(View.GONE);

                    dislike.setVisibility(View.VISIBLE);
                } else {
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
                    Intent in1 = new Intent(getApplicationContext(), SongService.class);
                    startService(in1);

                } else {
                    PlayerConstants.SONG_CHANGE_HANDLER
                            .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }
                changeUI(getApplicationContext());

                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {
                Toast.makeText(PlayListTracksActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void StopService() {
        // TODO Auto-generated method stub


        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

    }

    private void getPlayerScreen() {
        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning3) {

            Intent player = new Intent(PlayListTracksActivity.this, PlayerAcreenActivity.class);
            TrackObject mTrack = Constants.Convertto_Track();

            player.putExtra("track", mTrack);
            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

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

        if (trackgalleryAdaptor != null)
            trackgalleryAdaptor.notifyDataSetInvalidated();

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());


        boolean isServiceRunning6 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                getApplicationContext());

        if (!isServiceRunning3

                && !isServiceRunning6) {
            audioRelative.setVisibility(View.GONE);
        } else {
            audioRelative.setVisibility(View.VISIBLE);
        }
        super.onResume();

        try {
            changeUI(getApplicationContext());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTrackList(String playlistId) {
        startwhellprogress();

        Api_Interface service = ServiceGenerator.createService();
        service.GET_PlayLIST_TRACK(playlistId, SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)
        ).enqueue(new Callback<ArrayList<TrackObject>>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ArrayList<TrackObject>> call, @NonNull Response<ArrayList<TrackObject>> mresult) {
                if (mresult.isSuccessful()) {
                    progBar.setVisibility(View.GONE);
                    audioRelative.setEnabled(true);
                    // TODO Auto-generated method stub

                    TrackArray = mresult.body();
                    mtrackList = new ArrayList<>();
                    // Playlistconstant.SONGS_LIST1 = mtrackList;
                    if (TrackArray.size() > 0) {
                        for (int i = 0; i < TrackArray.size(); i++) {
                            TrackObject mTrack = new TrackObject();
                            mTrack.setTrackId(TrackArray.get(i).getTrackId());
                            mTrack.setTrackEnName(TrackArray.get(i).getTrackEnName());
                            mTrack.setTrackArName(TrackArray.get(i).getTrackArName());
                            mTrack.setTrackPath(TrackArray.get(i).getTrackPath());
                            mTrack.setIsFavourite(TrackArray.get(i).getIsFavourite());
                            mTrack.setAlbumId(TrackArray.get(i).getAlbumId());
                            mTrack.setSingerEnName(TrackArray.get(i).getSingerEnName());
                            mTrack.setSingerArName(TrackArray.get(i).getSingerArName());
                            mTrack.setAlbumEnName(TrackArray.get(i).getAlbumEnName());
                            mTrack.setAlbumArName(TrackArray.get(i).getAlbumArName());
                            mTrack.setTrackImage(TrackArray.get(i).getTrackImage());
                            mTrack.setSingerId(TrackArray.get(i).getSingerId());
                            mTrack.setLikesCount(TrackArray.get(i).getLikesCount());
                            mTrack.setIsRBT(TrackArray.get(i).getIsRBT());
                            mTrack.setListenCount(TrackArray.get(i).getListenCount());
                            mTrack.setTrackLength(String.valueOf(TrackArray.get(i).getTrackLength()));
                            mTrack.setHasLyrics(TrackArray.get(i).getHasLyrics());

                            mTrack.setLyricFile(TrackArray.get(i).getLyricFile());
                            mTrack.setVideoID(TrackArray.get(i).getVideoID());
                            mTrack.setIsPremium(TrackArray.get(i).getIsPremium());
                            mTrack.setIsDownloaded(TrackArray.get(i).getIsDownloaded());

                            mtrackList.add(mTrack);
                        }
                        imagecontainer.setVisibility(View.VISIBLE);

                        if (mtrackList != null) {
                            if (mtrackList.size() > 0) {
                                playplaylist.setActivated(true);
                                playplaylist.setClickable(true);
                                appearanceAnimate();
                                tvsongcount.setText(String.valueOf(mtrackList.size()) + "  " + getResources().getString(R.string.song));
                                tvduration.setText(Constants.getDurationString(mtrackList));

                                boolean isServiceRunning11 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                                        getApplicationContext());


                                boolean isServiceRunning2111 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                                        getApplicationContext());
                                if (!isServiceRunning11 && !isServiceRunning2111

                                        ) {
                                    gridview.performItemClick(gridview.getAdapter().getView(0, null, null), 0, 0);
                                    playplaylist.setVisibility(View.GONE);
                                    pauseplaylist.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(PlayListTracksActivity.this, getResources().getString(R.string.no_search_result),
                                Toast.LENGTH_LONG).show();
                        finish();
                    }


                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<ArrayList<TrackObject>> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    private void appearanceAnimate() {
        trackgalleryAdaptor = new PlaylistTrackAdaptor(PlayListTracksActivity.this, R.layout.albumlist_item,
                mtrackList);
        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(trackgalleryAdaptor, this,
                (listView, reverseSortedPositions) -> {
                    for (int position : reverseSortedPositions) {

                        if (PlayerConstants.SONGS_LIST.get(position) == PlayerConstants.SONGS_LIST
                                .get(PlayerConstants.SONG_NUMBER)) {
                            boolean isServiceRunning1 = UtilFunctions
                                    .isServiceRunning(SongService.class.getName(), getApplicationContext());
                            if (isServiceRunning1) {
                                Intent i = new Intent(getApplicationContext(), SongService.class);
                                stopService(i);
                                Remove_handlers();
                                audioRelative.setVisibility(View.GONE);
                                selectessong = -1;
                                trackgalleryAdaptor.notifyDataSetInvalidated();


                            }

                        }
                        Removeplaylisttrack(mtrackList.get(position).getTrackId());
                        trackgalleryAdaptor.remove(position);
                        trackgalleryAdaptor.notifyDataSetInvalidated();
                        gridview.invalidateViews();
                        break;

                    }
                });

        swipeUndoAdapter.setAbsListView(gridview);
        gridview.setAdapter(swipeUndoAdapter);
        setListViewHeightBasedOnChildren1(gridview);

        gridview.enableSimpleSwipeUndo();
    }

    private void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
        }
    }

    private void Removeplaylisttrack(String trackid) {
        // TODO Auto-generated method stub
        startwhellprogress();
        String fav = ServerConfig.SERVER_URl + ServerConfig.DelUserPlaylistTrack + "?usrPlaylstId=" + playlistId
                + "&trackId=" + trackid + "&userId=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");

        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.DelUserPlaylistTrack(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    progBar.setVisibility(View.GONE);
                    getTrackList(playlistId);
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    private void setListViewHeightBasedOnChildren1(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you
                // won't get measured height at all. The listitem needs to be
                // drawn first to know the height.
                listItem.setLayoutParams(
                        new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void getplaylistnot(String string) {
        startwhellprogress();
        // Constants.SERVER_URl + Constants.REGISTER_PATH
        SharedPreferences prefs = getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetPlaylistInfo + "?playlistId=" + string + "&userId="
                + prefs.getString(Constants.USERID, "") + "&UserToken=" + prefs.getString(Constants.accesstoken, "");
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.GetPlaylistInfo(fav_url).enqueue(new Callback<PlaylistnotificationResponse>() {

            @Override
            public void onResponse(@NonNull Call<PlaylistnotificationResponse> call, @NonNull Response<PlaylistnotificationResponse> mresult) {
                if (mresult.isSuccessful()) {
                    playlistnotificationResponse = mresult.body();
                    if (playlistnotificationResponse != null) {
                        listoftrack = playlistnotificationResponse.getPlaylstTracks();
                        mtrackList = new ArrayList<>();
                    }
                    if (listoftrack != null && listoftrack.size() > 0) {
                        imagecontainer.setVisibility(View.VISIBLE);

                        SetPlayList(listoftrack);

                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<PlaylistnotificationResponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void SetPlayList(ArrayList<Similartrackstripresponse> listoftrack) {
        for (int j = 0; j < listoftrack.size(); j++) {
            data = new TrackObject();

            data.setAlbumId(listoftrack.get(j).getAlbumId());
            data.setAlbumEnName(listoftrack.get(j).getAlbumEnName());
            data.setAlbumArName(listoftrack.get(j).getAlbumArName());
            data.setSingerEnName(listoftrack.get(j).getSingerEnName());
            data.setSingerArName(listoftrack.get(j).getSingerArName());
            data.setTrackImage(listoftrack.get(j).getTrackImage());
            data.setTrackEnName(listoftrack.get(j).getTrackEnName());
            data.setTrackArName(listoftrack.get(j).getTrackArName());

            data.setTrackId(listoftrack.get(j).getTrackId());
            data.setTrackPath(listoftrack.get(j).getTrackPath());
            data.setIsFavourite(listoftrack.get(j).getIsFavourite());
            data.setIsRBT(listoftrack.get(j).getIsRBT());
            data.setSingerId(listoftrack.get(j).getSingerId());
            data.setLikesCount(listoftrack.get(j).getLikesCount());
            data.setListenCount(listoftrack.get(j).getListenCount());
            data.setTrackLength(listoftrack.get(j).getTrackLength());
            data.setHasLyrics(listoftrack.get(j).getHasLyrics());
            data.setLyricFile(listoftrack.get(j).getLyricFile());
            data.setVideoID(listoftrack.get(j).getVideoID());
            data.setIsDownloaded(listoftrack.get(j).getIsDownloaded());
            data.setIsPremium(listoftrack.get(j).getIsPremium());

            mtrackList.add(data);

        }
        trackgalleryAdaptor = new PlaylistTrackAdaptor(PlayListTracksActivity.this, R.layout.albumlist_item,
                mtrackList);

        gridview.setAdapter(trackgalleryAdaptor);
        setListViewHeightBasedOnChildren1(gridview);

        playplaylist.setActivated(true);
        playplaylist.setClickable(true);
        tvsongcount.setText(String.valueOf(listoftrack.size()) + "  " + getResources().getString(R.string.song));

        if (language.equalsIgnoreCase("ar")) {
            title_txt.setText(playlistnotificationResponse.getPlaylistArName());
            tvplaylistname.setText(playlistnotificationResponse.getPlaylistArName());


        }
        if (language.equalsIgnoreCase("en")) {
            title_txt.setText(playlistnotificationResponse.getPlaylistName());
            tvplaylistname.setText(playlistnotificationResponse.getPlaylistName());


        }
        Picasso.with(getBaseContext())
                .load(ServerConfig.PLAYLIST_IMAGE_PATH + playlistnotificationResponse.getPlaylistImgPath())
                .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img)
                .into(new_song_slider, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        SetBlurryImage();

                    }

                    @Override
                    public void onError() {
                        SetBlurryImage();

                    }
                });

        progBar.setVisibility(View.GONE);
        boolean isServiceRunning11 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());

        boolean isServiceRunning2111 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                getApplicationContext());
        if (!isServiceRunning11 && !isServiceRunning2111

                ) {
            gridview.performItemClick(gridview.getAdapter().getView(0, null, null), 0, 0);
            playplaylist.setVisibility(View.GONE);
            pauseplaylist.setVisibility(View.VISIBLE);
        }


    }
}
