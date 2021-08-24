package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.BaseActivity;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyadaptors.AbumGallareyAdaptor;
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
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.LinearDividerItemDecoration;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoritesActivity extends BaseActivity {
    public static RelativeLayout audioRelative;
    public static RelativeLayout progBar;
    private static TrackObject data;
    public static Activity activity;
    public static int selectessong = -1;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static String favo;
    private static Context context;
    static String trackid;
    private static ProgressWheel pb1;
    private static String language;
    private static ArrayList<TrackObject> mtrackList;
    private static ImageView play;
    private static TextView songname;
    private static TextView albumname;
    private static ImageView player_image;
    private ListView mFavTrack;
    private TrackObject mTrack;
    private String like;
    private View activityView;
    private LinearLayout shuffle_btn;
    private static AbumGallareyAdaptor mListAdaptor;
    private LinearLayoutManager horizontalLayoutManager;

    private static void updateUI() {
        try {
            PlayerConstants.SONGS_LIST = mtrackList;

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
            // linearplayer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeButton() {
        if(mListAdaptor!=null) {
            mListAdaptor.notifyDataSetInvalidated();
        }
        if (PlayerConstants.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    public static void changeUI() {
        updateUI();
        changeButton();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        Log.i("ontestart","onrestart");
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        if (Constants.isNetworkOnline(FavoritesActivity.this)) {
            getFavorites();
        } else {

            Toast.makeText(FavoritesActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }
        changeUI1(getApplicationContext());

    }
    // set player in all application
    public static void changeUI1(final Context context) {

        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (isServiceRunning) {
            updateUI1();
            changeButton1();

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
            // imageLoader.DisplayImage(ServerConfig.Image_path
            // +mtrackList.get(position).getAlbumImgPath(),player_image);
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);            // linearplayer.setVisibility(View.VISIBLE);
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

    private static void updateUI1() {
        try {
            TrackObject data;
            data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
            if (data.getIsFavourite().equalsIgnoreCase("true")) {
                Like.setVisibility(View.GONE);

                dislike.setVisibility(View.VISIBLE);
            }

            if (data.getIsFavourite().equalsIgnoreCase("false")) {
                dislike.setVisibility(View.GONE);

                Like.setVisibility(View.VISIBLE);
                // make button enable
            }

            if (language.equalsIgnoreCase("ar")) {
                songname.setText(data.getTrackArName());
                albumname.setText(data.getAlbumArName());
            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(data.getTrackEnName());
                albumname.setText(data.getAlbumEnName());
            }

            // Bitmap albumArt = UtilFunctions.getAlbumart(context,

            String imgpath = ServerConfig.Image_path + data.getTrackImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);            // linearplayer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeButton1() {
        if(mListAdaptor!=null) {
            mListAdaptor.notifyDataSetInvalidated();
        }
        if (PlayerConstants.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }

    }

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_favorites);
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;
        assert layoutInflater != null;
        activityView = layoutInflater.inflate(R.layout.activity_favorites, nullparent, false);
        progBar = activityView.findViewById(R.id.progress);
        context = getApplicationContext();
        Applicationmanager.setContext(FavoritesActivity.this);

        language = LanguageHelper.getCurrentLanguage(context);

        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        startwhellprogress();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }
        }
        InitUi();
        frameLayout.addView(activityView);


    }

    private void InitUi() {
        mFavTrack = activityView.findViewById(R.id.favTrackList);
        mFavTrack.setOnItemClickListener((parent, view, position, id) -> {

                // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(FavoritesActivity.this)) {

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
                favo = mtrackList.get(position).getIsFavourite();
                if (favo.equalsIgnoreCase("true")) {
                    Like.setVisibility(View.GONE);
                    dislike.setVisibility(View.VISIBLE);
                }

                if (favo.equalsIgnoreCase("false")) {
                    dislike.setVisibility(View.GONE);
                    Like.setVisibility(View.VISIBLE);

                }
                // linearplayer.setVisibility(View.VISIBLE);
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER = position;
                selectessong = position;

                mListAdaptor.notifyDataSetInvalidated();
                updateUI();

                changeButton();

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


                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {
                Toast.makeText(FavoritesActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        }

           );

        if (Constants.isNetworkOnline(FavoritesActivity.this)) {
            getFavorites();
        } else {

            Toast.makeText(FavoritesActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }
        audioRelative = activityView.findViewById(R.id.audioplayer);
        audioRelative.setEnabled(false);

        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);


        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);

        PlayerOnClick();


        shuffle_btn = activityView.findViewById(R.id.shuffle_btn);
        shuffle_btn.setActivated(false);
        shuffle_btn.setClickable(false);
        shuffle_btn.setEnabled(false);

        shuffle_btn.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(FavoritesActivity.this)) {

                StopService();

                Log.e("list clicked ", "here");

                startwhellprogress();
                audioRelative.setEnabled(false);

                // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                Log.e("list clicked ", "here");
                Random random = new Random();
                int index = random.nextInt(mtrackList.size());
                favo = mtrackList.get(index).getIsFavourite();
                if (favo.equalsIgnoreCase("true")) {
                    Like.setVisibility(View.GONE);
                    dislike.setVisibility(View.VISIBLE);
                }

                if (favo.equalsIgnoreCase("false")) {
                    dislike.setVisibility(View.GONE);
                    Like.setVisibility(View.VISIBLE);

                }
                // linearplayer.setVisibility(View.VISIBLE);
                PlayerConstants.SONG_PAUSED = false;
                PlayerConstants.SONG_NUMBER =index;
                selectessong = index;
                mListAdaptor.notifyDataSetChanged();
                updateUI();

                changeButton();
                SongService.isRepeat = false;
                SongService.isShuffle = true;

                // make shuffle to false

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


                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {
                Toast.makeText(FavoritesActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();

            }
        });
        changeUI1(getApplicationContext());
    }

    private void PlayerOnClick() {
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
    }

    private void getPlayerScreen() {

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning3) {

            Intent player = new Intent(FavoritesActivity.this, PlayerAcreenActivity.class);

            player.putExtra("track", mTrack);


            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

    }

    private void getFavorites() {
        startwhellprogress();

        Api_Interface service = ServiceGenerator.createService();
        service.GET_FAV_SONGS(SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)).enqueue(new Callback<ArrayList<TrackObject>>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<ArrayList<TrackObject>> call, @NonNull Response<ArrayList<TrackObject>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        mtrackList = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            mTrack = new TrackObject();
                            mTrack.setSingerId(response.body().get(i).getSingerId());

                            mTrack.setTrackId(response.body().get(i).getTrackId());
                            mTrack.setTrackEnName(response.body().get(i).getTrackEnName());
                            mTrack.setTrackArName(response.body().get(i).getTrackArName());
                            mTrack.setTrackPath(response.body().get(i).getTrackPath());
                            mTrack.setIsFavourite(response.body().get(i).getIsFavourite());
                            mTrack.setAlbumId(response.body().get(i).getAlbumId());
                            mTrack.setSingerEnName(response.body().get(i).getSingerEnName());
                            mTrack.setSingerArName(response.body().get(i).getSingerArName());
                            mTrack.setAlbumEnName(response.body().get(i).getAlbumEnName());
                            mTrack.setAlbumArName(response.body().get(i).getAlbumArName());
                            mTrack.setTrackImage(response.body().get(i).getTrackImage());
                            mTrack.setIsRBT(response.body().get(i).getIsRBT());
                            mTrack.setLikesCount(response.body().get(i).getLikesCount());
                            mTrack.setListenCount(response.body().get(i).getListenCount());
                            mTrack.setTrackLength(response.body().get(i).getTrackLength());
                            mTrack.setHasLyrics(response.body().get(i).getHasLyrics());

                            mTrack.setLyricFile(response.body().get(i).getLyricFile());
                            mTrack.setVideoID(response.body().get(i).getVideoID());
                            mTrack.setIsPremium(response.body().get(i).getIsPremium());
                            mTrack.setIsDownloaded(response.body().get(i).getIsDownloaded());


                            mtrackList.add(mTrack);
                        }
                        if (mtrackList != null && mtrackList.size() > 0) {
                            progBar.setVisibility(View.GONE);
                            audioRelative.setEnabled(true);
                            mListAdaptor = new AbumGallareyAdaptor(FavoritesActivity.this, R.layout.albumlist_item, mtrackList);
                            mFavTrack.setAdapter(mListAdaptor);
                            shuffle_btn.setActivated(true);
                            shuffle_btn.setClickable(true);
                            shuffle_btn.setEnabled(true);

                        }
                    } else {
                        Toast.makeText(FavoritesActivity.this,
                                R.string.nolike,
                                Toast.LENGTH_SHORT).show();
                        progBar.setVisibility(View.GONE);
                        finish();

                    }
                } else {
                    ApiUtils.handelErrorCode(context, response.code());
                    System.out.println("onFailure");


                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<TrackObject>> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }


        });
    }

    private void setRecycleViewDesign(RecyclerView gridview) {

        if (language.equalsIgnoreCase("ar")) {
            horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);        }
        if (language.equalsIgnoreCase("en")) {
            horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        gridview.setLayoutManager(horizontalLayoutManager);
// add the decoration to the recyclerView
        LinearDividerItemDecoration decoration = new LinearDividerItemDecoration(context, getResources().getColor(R.color.black),2);
        gridview.addItemDecoration(decoration);    }
    private void StopService() {
        // TODO Auto-generated method stub


        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

    }

    public void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
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
                favo = "true";


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
                    favo = "false";

                }


                Like.setVisibility(View.VISIBLE);

                dislike.setVisibility(View.GONE);
            }
        }

        ReloadActivity();
    }

    private void ReloadActivity() {

        getFavorites();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mListAdaptor != null)
            mListAdaptor.notifyDataSetChanged();

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        boolean isServiceRunning6 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                getApplicationContext());
        if (!isServiceRunning3
                && !isServiceRunning6) {
            audioRelative.setVisibility(View.GONE);
            selectessong = -1;
        } else {
            audioRelative.setVisibility(View.VISIBLE);
            //	audioRelative.setEnabled(true);

        }


        try {
            changeUI1(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isServiceRunning3) {
            if (mtrackList != null && mtrackList.size() > 0)
                if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0) {
                    for (int i = 0; i < mtrackList.size(); i++) {
                    if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtrackList.get(i).getTrackId())) {
                        selectessong = PlayerConstants.SONG_NUMBER;
                        if (mFavTrack!=null&&mListAdaptor != null) {
                           mFavTrack.setItemChecked(PlayerConstants.SONG_NUMBER, true);
                            mListAdaptor.notifyDataSetChanged();
                        }


                    }}
                }
        }
    }
}
