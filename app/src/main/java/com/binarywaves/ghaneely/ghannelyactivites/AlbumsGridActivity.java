package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.AlbumGridAdapter;
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
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 13-Aug-17.
 */

public class AlbumsGridActivity extends ActivityMainRunnuing {
    public static RelativeLayout audioRelative;
    public static RelativeLayout progBar;
    private static TrackObject data;
    public static Activity activity;
    public static GridView mFavTrack;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static Context context;
    static String trackid;
    private static ProgressWheel pb1;
    private static String language;
    private static ImageView play;
    private static TextView songname;
    private static TextView albumname;
    private static ImageView player_image;
    private ArrayList<SlideAlbumObject> mtrack;

    private String like;
    private ImageView navigation_up;
    private TextView title;

    private static void updateUI() {
        try {
            // PlayerConstants.SONGS_LIST = mtrackList;

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
            Like.setVisibility(View.GONE);

            dislike.setVisibility(View.GONE);

            String imgpath = ServerConfig.Radio_Url + HomeActivity.dataradio.getRadioImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
            // linearplayer.setVisibility(View.VISIBLE);
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


            String imgpath = ServerConfig.Image_path + data.getTrackImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
            // linearplayer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static void changeButton1() {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        Applicationmanager.setContext(AlbumsGridActivity.this);

        context = getApplicationContext();
        language = LanguageHelper.getCurrentLanguage(context);
        progBar = findViewById(R.id.progress);

        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        startwhellprogress();


        mFavTrack = findViewById(R.id.gridview);

        mFavTrack.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(AlbumsGridActivity.this)) {
                if (position >= mtrack.size()) {
                    position = position % mtrack.size();
                }
                Intent albumIntent = new Intent(AlbumsGridActivity.this, AlbumTabsActivity.class);
                albumIntent.putExtra("album", mtrack.get(position));
                startActivity(albumIntent);

            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("albumlist")) {
                // extract the extra-data in the Notification
                mtrack = extras.getParcelableArrayList("albumlist");


            }
        }


        audioRelative = findViewById(R.id.audioplayer);
        play = findViewById(R.id.play_img);

        songname = findViewById(R.id.song_name);
        albumname = findViewById(R.id.album_name);
        player_image = findViewById(R.id.player_image);

        audioRelative = findViewById(R.id.audioplayer);
        audioRelative.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getPlayerScreen();

        });
        title = findViewById(R.id.title_txt);
        navigation_up = findViewById(R.id.navigation_up);
        navigation_up.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            finish();
        });
        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);

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
        if (mtrack != null && mtrack.size() > 0) {
            SetOtherAlbumLst(mtrack);
        }
        changeUI1(getApplicationContext());


    }


    private void SetOtherAlbumLst(ArrayList<SlideAlbumObject> mtrackList1) {
        AlbumGridAdapter songAdaptor = new AlbumGridAdapter(AlbumsGridActivity.this, R.layout.grid_item, mtrackList1);
        mFavTrack.setAdapter(songAdaptor);
        progBar.setVisibility(View.GONE);


    }

    private void getPlayerScreen() {


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning3) {

            Intent player = new Intent(AlbumsGridActivity.this, PlayerAcreenActivity.class);
            TrackObject mTrack = Constants.Convertto_Track();
            player.putExtra("track", mTrack);

            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

    }

    public void StopService() {
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
        if (response != null) {

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
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            changeUI1(getApplicationContext());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}