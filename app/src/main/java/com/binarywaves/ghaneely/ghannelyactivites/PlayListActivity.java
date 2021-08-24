package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.binarywaves.ghaneely.ghannelyadaptors.MyPlaylistAdaptor;
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
import com.binarywaves.ghaneely.ghannelyresponse.CreatePlaylist_arrayresponse;
import com.binarywaves.ghaneely.ghannelyresponse.CreatePlaylistresponse;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistScreenResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Playlistlst;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistnotificationResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("StatementWithEmptyBody")
public class PlayListActivity extends BaseActivity {
    public static RelativeLayout audioRelative;
    public static TrackObject data1;
    public static Activity activity;
    public static RelativeLayout progBar;
    private static ImageView play;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static Context context;
    private static ProgressWheel pb1;
    private static String language;
    private static TextView albumname;
    private static ImageView player_image;
    private static TextView songname;
    private MyPlaylistAdaptor mAdaptor;
    private DynamicListView mList;
    private DynamicListView autoList;
    private Button create_btn;
    String isfav;
    EditText playlistname;
    Dialog dialog;
    private ArrayList<CreatePlaylist_arrayresponse> mPlaylistList1 = new ArrayList<>();
    private FrameLayout frameLayout;
    private View activityView;
    private LinearLayout lin_userplaylist,lin_noplayist;
    private LinearLayout lin_autoplaylist;

    private ArrayList<PlaylistnotificationResponse> autoplaylist;
    private ArrayList<Playlistlst> userplaylist;
    private String like;

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

    private static void updateUI1() {
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

            }                                                        // albumArt
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

        frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;
        if (layoutInflater != null) {
            activityView = layoutInflater.inflate(R.layout.activity_mainplaylist, nullparent, false);
        }
        context = getApplicationContext();
        Applicationmanager.setContext(PlayListActivity.this);

        language = LanguageHelper.getCurrentLanguage(context);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }
        }
        Init_ui();
        if (Constants.isNetworkOnline(PlayListActivity.this))

        {
            getPlayList();
        } else

        {

            Toast.makeText(PlayListActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }

        changeUI1(getApplicationContext());


    }

    private void Init_ui() {
        mList = activityView.findViewById(R.id.playlist_list);
        autoList = activityView.findViewById(R.id.autoplaylist_list);
        lin_userplaylist = activityView.findViewById(R.id.lin_userplaylist);
        lin_noplayist=activityView.findViewById(R.id.lin_noplayist);
        lin_autoplaylist = activityView.findViewById(R.id.lin_autoplaylist);
        autoList.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            Intent playTrack = new Intent(PlayListActivity.this, PlayListTracksActivity.class);
            playTrack.putExtra("id", autoplaylist.get(position).getPlaylistId());
            startActivity(playTrack);

        });

        mList.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            Intent playTrack = new Intent(PlayListActivity.this, PlayListTracksActivity.class);
            playTrack.putExtra("playlistId", userplaylist.get(position).getPlaylistId());
            playTrack.putExtra("playlistname", userplaylist.get(position).getPlaylistName());
            playTrack.putExtra("playlistimage", userplaylist.get(position).getPlaylistImgPath());
            startActivity(playTrack);

        });
        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        startwhellprogress();
        create_btn = activityView.findViewById(R.id.create_btn);

        frameLayout.addView(activityView);


        audioRelative = findViewById(R.id.audioplayer);
        audioRelative.setEnabled(false);
        audioRelative.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getPlayerScreen();

        });
        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);
        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);


        create_btn.setOnClickListener(v -> {
            //showAlert();
            Intent player = new Intent(PlayListActivity.this, ListviwCheckbox_Activity.class);
            startActivity(player);
            finish();
        });

        PlayerItem_Onclick();
    }

    private void PlayerItem_Onclick() {

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

    private void getPlayList() {
        startwhellprogress();

        Api_Interface service = ServiceGenerator.createService();
        service.GetPlaylistScreen(SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)).enqueue(new Callback<PlaylistScreenResponse>() {

            @Override
            public void onResponse(@NonNull Call<PlaylistScreenResponse> call, @NonNull Response<PlaylistScreenResponse> mresult) {
                if (mresult.isSuccessful()) {
                    PlaylistScreenResponse response = mresult.body();
                    if (response != null) {
                        autoplaylist = response.getAutoPlaylists();

                        userplaylist = response.getUserPlaylists();
                        Log.i("allplaylist", autoplaylist + "user" + userplaylist);
                        if (userplaylist.size() > 0) {
                            lin_userplaylist.setVisibility(View.VISIBLE);

                            lin_noplayist.setVisibility(View.GONE);
                            progBar.setVisibility(View.GONE);

                            audioRelative.setEnabled(true);

                            appearanceAnimate(0);

                        } else {
                            lin_userplaylist.setVisibility(View.GONE);

                            lin_noplayist.setVisibility(View.VISIBLE);
                            progBar.setVisibility(View.GONE);
                        }
                        if (autoplaylist.size() > 0) {
                            // setautoplaylistData(0);
                        }


                        progBar.setVisibility(View.GONE);

                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");
                        progBar.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistScreenResponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    @SuppressWarnings("unused")
    private void setautoplaylistData(int key) {
        mAdaptor = new MyPlaylistAdaptor(PlayListActivity.this, R.layout.myplaylist_item_list, null, autoplaylist);

        AnimationAdapter animAdapter;

        switch (key) {
            default:
            case 0:
                animAdapter = new AlphaInAnimationAdapter(mAdaptor);
                break;
            case 1:
                animAdapter = new ScaleInAnimationAdapter(mAdaptor);
                break;
            case 2:
                animAdapter = new SwingBottomInAnimationAdapter(mAdaptor);
                break;
            case 3:
                animAdapter = new SwingLeftInAnimationAdapter(mAdaptor);
                break;
            case 4:
                animAdapter = new SwingRightInAnimationAdapter(mAdaptor);
                break;
        }
        animAdapter.setAbsListView(autoList);

        autoList.setAdapter(animAdapter);

    }

    private void appearanceAnimate(int key) {
        if (userplaylist != null && userplaylist.size() > 0) {
            mAdaptor = new MyPlaylistAdaptor(PlayListActivity.this, R.layout.myplaylist_item_list, userplaylist, null);
            SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(mAdaptor, this,
                    (listView, reverseSortedPositions) -> {
                        for (int position : reverseSortedPositions) {
                            Removeplaylist(userplaylist.get(position).getPlaylistId());
                            mAdaptor.remove(position);


                        }
                    });
            AnimationAdapter animAdapter;

            switch (key) {
                default:
                case 0:
                    animAdapter = new AlphaInAnimationAdapter(swipeUndoAdapter);
                    break;
                case 1:
                    animAdapter = new ScaleInAnimationAdapter(swipeUndoAdapter);
                    break;
                case 2:
                    animAdapter = new SwingBottomInAnimationAdapter(swipeUndoAdapter);
                    break;
                case 3:
                    animAdapter = new SwingLeftInAnimationAdapter(swipeUndoAdapter);
                    break;
                case 4:
                    animAdapter = new SwingRightInAnimationAdapter(swipeUndoAdapter);
                    break;
            }
            animAdapter.setAbsListView(mList);

            mList.setAdapter(animAdapter);
            mList.enableSimpleSwipeUndo();
        }

    }

    private void Removeplaylist(String playlistid) {
        // TODO Auto-generated method stub
        startwhellprogress();

        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.DelUserPlaylist + "?usrPlaylstId=" + playlistid + "&userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");

        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        service.DelUserPlaylist(fav_url).enqueue(new Callback<CreatePlaylistresponse>() {

            @Override
            public void onResponse(@NonNull Call<CreatePlaylistresponse> call, @NonNull Response<CreatePlaylistresponse> mresult) {
                if (mresult.isSuccessful()) {
                    CreatePlaylistresponse response = mresult.body();
                    if (response != null) {
                        mPlaylistList1 = response;
                        if (mPlaylistList1.size() > 0) {
                            audioRelative.setEnabled(true);
                            lin_userplaylist.setVisibility(View.VISIBLE);

                            lin_noplayist.setVisibility(View.GONE);
                            getPlayList();
                        } else {
                            lin_userplaylist.setVisibility(View.GONE);

                            lin_noplayist.setVisibility(View.VISIBLE);
                            progBar.setVisibility(View.GONE);
                        }
                    }
                    progBar.setVisibility(View.GONE);

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<CreatePlaylistresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    public void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
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
            showCustomAlert(0);
            if (favourite.equalsIgnoreCase("0")) {
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

            changeUI1(getApplicationContext());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPlayerScreen() {

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning3) {

            Intent player = new Intent(PlayListActivity.this, PlayerAcreenActivity.class);

            TrackObject mTrack = Constants.Convertto_Track();
            player.putExtra("track", mTrack);
            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

    }
}
