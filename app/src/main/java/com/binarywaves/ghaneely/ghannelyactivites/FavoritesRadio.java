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
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.binarywaves.ghaneely.ghannelyadaptors.RadioAdaptor;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongServiceradio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.RadioConstant;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.Radio;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesRadio extends BaseActivity {
    public static RelativeLayout progBar;
    public static RelativeLayout audioRelative;
    public static Activity activity;
    private static ProgressWheel pb1;
    private static ImageView play;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static ImageView player_image;
    private static Radio dataradio;
    private static Context context;
    private static String language;
    private static TextView songname;
    private static TextView albumname;
    private ListView lvtrack;
    private String like;

    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    private static void updateUIradio() {
        try {
            HomeActivity.dataradio = RadioConstant.SONGS_LIST.get(RadioConstant.SONG_NUMBER);

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
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
            // linearplayer.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeButtonradio() {
        if (RadioConstant.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("unused")
    public static void changeUIradio() {
        updateUIradio();
        changeButtonradio();
    }

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
                albumname.setText(data.getAlbumArName()); // Bitmap


            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(data.getTrackEnName());
                albumname.setText(data.getAlbumEnName()); // Bitmap

            }
            // albumArt
            // =
            // UtilFunctions.getAlbumart(context,
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;
        assert layoutInflater != null;
        View activityView = layoutInflater.inflate(R.layout.activity_radio, nullparent, false);
        Applicationmanager.setContext(FavoritesRadio.this);

        lvtrack = activityView.findViewById(R.id.lvtrack);
        lvtrack.setOnItemClickListener((parent, view, position, id) -> {
            // playSong();
            if (Constants.isNetworkOnline(FavoritesRadio.this)) {

                Log.e("list clicked ", "here");
                startwhellprogress();
                audioRelative.setEnabled(false);

                StopService();

                RadioConstant.SONG_PAUSED = false;
                RadioConstant.SONG_NUMBER = position;

                boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                        getApplicationContext());
                if (!isServiceRunning3) {
                    Remove_handlers();

                    Intent i = new Intent(getApplicationContext(), SongServiceradio.class);
                    startService(i);

                    // progBar.setVisibility(View.GONE);

                } else {
                    RadioConstant.SONG_CHANGE_HANDLER
                            .sendMessage(RadioConstant.SONG_CHANGE_HANDLER.obtainMessage());
                }

                updateUIradio();

                changeButtonradio();


                Log.d("TAG", "TAG Tapped INOUT(OUT)");

            } else {
                Toast.makeText(FavoritesRadio.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
                progBar.setVisibility(View.GONE);
            }


        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }
        }

        context = getApplicationContext();

        language = LanguageHelper.getCurrentLanguage(context);

        audioRelative = activityView.findViewById(R.id.audioplayer);

        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);
        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);
        progBar = activityView.findViewById(R.id.progress);

        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        frameLayout.addView(activityView);
        setGridlist();
        play.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(
                    SongService.class.getName(), getApplicationContext());
            if (isServiceRunning3) {
                PlayerConstants.SONGnext = false;

                Controls.playControl(getApplicationContext());
            }
            boolean isServiceRunning4 = UtilFunctions.isServiceRunning(
                    SongServiceradio.class.getName(),
                    getApplicationContext());
            if (isServiceRunning4) {
                RadioConstant.SONGnext = false;

                ControlRadio.playControl(getApplicationContext());

            }

        });
        pause.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(
                    SongService.class.getName(), getApplicationContext());
            if (isServiceRunning3) {
                PlayerConstants.SONGnext = false;

                Controls.pauseControl(getApplicationContext());

            }

            boolean isServiceRunning4 = UtilFunctions.isServiceRunning(
                    SongServiceradio.class.getName(),
                    getApplicationContext());
            if (isServiceRunning4) {
                RadioConstant.SONGnext = false;

                ControlRadio.pauseControl(getApplicationContext());

            }

        });

        Like.setOnClickListener(v -> {
            startwhellprogress();

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(
                    SongService.class.getName(), getApplicationContext());
            if (isServiceRunning3) {
                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                        getApplicationContext(), "1");
                // data.setIsFavourite(true);

            }

        });

        dislike.setOnClickListener(v -> {
            startwhellprogress();


            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(
                    SongService.class.getName(), getApplicationContext());
            if (isServiceRunning3) {
                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                        getApplicationContext(), "0");

            }

        });

        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative
                .findViewById(R.id.player_image);
        changeUI1(getApplicationContext());

        audioRelative.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getPlayerScreen();

        });

    }

    private void getRadioItem(int radioIntent) {
        selectedListItem=6;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }}
        for (int i=0;i<RadioConstant.SONGS_LIST.size();i++){
            if(RadioConstant.SONGS_LIST.get(i).getRadioId().equalsIgnoreCase(String.valueOf(radioIntent)))
            {
                int selecteditem=i;
                lvtrack.performItemClick(lvtrack.getAdapter().getView(selecteditem, null, null), selecteditem, selecteditem);

            }

        }
    }

    private void getPlayerScreen() {


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(
                SongService.class.getName(), getApplicationContext());
        if (isServiceRunning3) {

            Intent player = new Intent(FavoritesRadio.this,
                    PlayerAcreenActivity.class);

            TrackObject mTrack = Constants.Convertto_Track();

            player.putExtra("track", mTrack);
            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up,
                    R.anim.slide_out_up);
        }
    }

    private void setGridlist() {
        // TODO Auto-generated method stub
        if (RadioConstant.SONGS_LIST.size() > 0) {
            RadioAdaptor internationalRadioAdaptor = new RadioAdaptor(FavoritesRadio.this, R.layout.customradioitem,
                    RadioConstant.SONGS_LIST);

            lvtrack.setAdapter(internationalRadioAdaptor);
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                if (extras.containsKey("radioIntent")) {
                    int radioid=extras.getInt("radioIntent");
                    if(RadioConstant.SONGS_LIST!=null&&RadioConstant.SONGS_LIST.size()>0) {
                        getRadioItem(radioid);
                    }
                }}
            /*
        boolean isServiceRunning11 = UtilFunctions.isServiceRunning(SongService.class.getName(), getApplicationContext());

		boolean isServiceRunning2111 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
				getApplicationContext());
		if (!isServiceRunning11&&!isServiceRunning2111) {
			lvtrack.performItemClick(lvtrack.getAdapter().getView(0, null, null), 0, 0);
		}
		*/
        }


    }

    private void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler
                    .removeCallbacks(PlayerAcreenActivity.run);

        }
    }

    private void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

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