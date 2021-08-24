package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.Inboxadapter;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongServiceradio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.RadioConstant;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.Favorite;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.GetVideoDataFrmIDResponse;
import com.binarywaves.ghaneely.ghannelyresponse.InboxPropertyResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackres;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackstripresponse;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxActivity extends BaseActivity
        implements SwipyRefreshLayout.OnRefreshListener, View.OnClickListener {
    public static RelativeLayout audioRelative;
    public static RelativeLayout progBar;
    private static TrackObject data;
    public static Activity activity;
    private static TrackObject mPlayist;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static String favo;
    private static Context context;
    private static String trackid;
    private static ProgressWheel pb1;
    private static String language;
    private static ArrayList<TrackObject> mtrackList;
    private static ArrayList<TrackObject> mtrackList1;
    private static ImageView play;
    private static TextView songname;
    private static TextView albumname;
    private static ImageView player_image;
    ArrayList<Favorite> mFavList;
    private DynamicListView mFavTrack;
    private TrackObject mTrack;
    private ArrayList<Similartrackstripresponse> listoftrack;
    String idtrack;
    private ArrayList<String> arrayList;
    private Boolean isOlder;
    private String like;
    private Inboxadapter mListAdaptor;
    private ArrayList<TrackObject> mAlbumList;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private String pushNotifLogId = "0";
    private ArrayList<VideoObjectResponse> mVideoslist;

    private ArrayList<VideoObjectResponse> listofvideos;
    private VideoObjectResponse mvideolistobject;
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
        // setContentView(R.layout.activity_favorites);
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;

        View activityView = null;
        if (layoutInflater != null) {
            activityView = layoutInflater.inflate(R.layout.activity_inbox, nullparent, false);
        }
        assert activityView != null;
        progBar = activityView.findViewById(R.id.progress);
        context = getApplicationContext();
        language = LanguageHelper.getCurrentLanguage(context);
        Applicationmanager.setContext(InboxActivity.this);

        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        startwhellprogress();
        frameLayout.addView(activityView);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }
        }
        mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);

        mFavTrack = findViewById(R.id.favTrackList);
        arrayList = new ArrayList<>();

        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);

        mSwipyRefreshLayout.setOnRefreshListener(direction -> {
            Log.d("MainActivity",
                    "Refresh triggered at " + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                if (mtrackList != null && mtrackList.size() > 0) {
                    isOlder = false;
                    pushNotifLogId = mtrackList.get(0).getAlbumArName();
                    getinbox(pushNotifLogId, isOlder);
                }
            }

            if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                if (mtrackList != null && mtrackList.size() > 0) {

                    isOlder = true;
                    pushNotifLogId = mtrackList.get(mtrackList.size() - 1).getAlbumArName();

                    getinbox(pushNotifLogId, isOlder);

                }
            }
        });
        mFavTrack.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(InboxActivity.this)) {
                SharedPreferences prefs1 = getSharedPreferences("Inboxopened", Context.MODE_PRIVATE);
                String httpParamJSONList1 = prefs1.getString("Inboxopened1", "");

                arrayList = new Gson().fromJson(httpParamJSONList1, new TypeToken<List<String>>() {
                }.getType());
                if (arrayList != null && arrayList.size() > 0) {

                    boolean contains = arrayList.contains(mtrackList.get(position).getAlbumArName());
                    if (!contains) {

                        arrayList.add(mtrackList.get(position).getAlbumArName());
                        Gson gson = new Gson();
                        String httpParamJSONList = gson.toJson(arrayList);

                        SharedPreferences prefs = getSharedPreferences("Inboxopened", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Inboxopened1", httpParamJSONList);

                        editor.apply();
                    }
                }
                String typemessage = mtrackList.get(position).getTrackArName();
                String msg;
                Log.e("list clickedtypemessage", typemessage);
                mListAdaptor.notifyDataSetChanged();
                mFavTrack.invalidateViews();
                switch (Integer.parseInt(typemessage)) {
                    case 1:
                    GetSingerTracksFrmTrack(mtrackList.get(position).getTrackId());
                    break;

                    case 2:
                         msg = mtrackList.get(position).getTrackId();
                        Intent albumIntent = new Intent(InboxActivity.this, AlbumTabsActivity.class);
                        albumIntent.putExtra("id", msg);
                        startActivity(albumIntent);
                        break;

                    case 3:
                         msg = mtrackList.get(position).getTrackId();
                        Intent albumIntent1 = new Intent(InboxActivity.this, PlayListTracksActivity.class);
                        albumIntent1.putExtra("id", msg);
                        startActivity(albumIntent1);
                        break;

                    case 4:
                         msg = mtrackList.get(position).getTrackId();
                        Intent albumIntent2 = new Intent(InboxActivity.this, ArtistTabsActivity.class);
                        albumIntent2.putExtra("playlistId", Integer.valueOf(msg));
                        startActivity(albumIntent2);
                        break;


                    case 5 :
                        msg = extras.getString("id");
                        Log.i("pushimtent", msg + typemessage);
                        GetSingerVideoFrmTrack(mtrackList.get(position).getTrackId());
                        break;

                    case 6:
                        msg = extras.getString("id");
                        Log.i("pushimtent", msg + typemessage);
                        Intent radioIntent = new Intent(InboxActivity.this, FavoritesRadio.class);
                        radioIntent.putExtra("titlemenu", getResources().getString(R.string.inter_title));

                        radioIntent.putExtra("radioIntent", Integer.valueOf(mtrackList.get(position).getTrackId()));
                        startActivity(radioIntent);
                        break;


                }

            }
        });

        if (Constants.isNetworkOnline(InboxActivity.this))

        {
            getinbox("0", false);
        } else

        {

            Toast.makeText(InboxActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }
        audioRelative = findViewById(R.id.audioplayer);
        play = findViewById(R.id.play_img);

        songname = findViewById(R.id.song_name);
        albumname = findViewById(R.id.album_name);
        player_image = findViewById(R.id.player_image);

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
        changeUI1(getApplicationContext());


    }

    private void getPlayerScreen() {


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning3) {

            Intent player = new Intent(InboxActivity.this, PlayerAcreenActivity.class);
            TrackObject mTrack = Constants.Convertto_Track();
            player.putExtra("track", mTrack);

            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

    }

    private void deleteinbox(String notificationid) {
        startwhellprogress();


        Api_Interface service = ServiceGenerator.createService();
        service.DelUserNotification(notificationid, SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)).enqueue(new Callback<ArrayList<InboxPropertyResponse>>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<ArrayList<InboxPropertyResponse>> call, @NonNull Response<ArrayList<InboxPropertyResponse>> response) {
                if (response.isSuccessful()) {
                    mtrackList1 = new ArrayList<>();

                    for (int i = 0; i < response.body().size(); i++) {
                        mTrack = new TrackObject();

                        mTrack.setTrackId(response.body().get(i).getTrackId());
                        mTrack.setTrackEnName(response.body().get(i).getMessage());
                        mTrack.setTrackArName(response.body().get(i).getMsgType());
                        mTrack.setAlbumEnName(response.body().get(i).getTitle());
                        mTrack.setTrackImage(response.body().get(i).getImgFile());
                        mTrack.setAlbumArName(response.body().get(i).getPushNotifLogId());

                        mtrackList1.add(mTrack);

                    }
                    if (mtrackList1.size() > 0) {
                        progBar.setVisibility(View.GONE);
                        audioRelative.setEnabled(true);


                        SetListData();


                    } else {
                        Toast.makeText(InboxActivity.this,
                                R.string.nodata,
                                Toast.LENGTH_LONG).show();
                        progBar.setVisibility(View.GONE);
                        finish();


                    }
                } else {
                    ApiUtils.handelErrorCode(context, response.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);
                    mSwipyRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<InboxPropertyResponse>> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);
                mSwipyRefreshLayout.setRefreshing(false);
            }


        });

    }

    private void getinbox(String pushNotifLogId2, Boolean isOlder2) {
        startwhellprogress();

        Api_Interface service = ServiceGenerator.createService();
        service.GetUserNotifLogLimit(Integer.parseInt(pushNotifLogId2), isOlder2, SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID)
                , SharedPrefHelper.getSharedString(getApplicationContext(),
                        Constants.accesstoken)).enqueue(new Callback<ArrayList<InboxPropertyResponse>>() {


                                                            @SuppressWarnings("ConstantConditions")
                                                            @Override
                                                            public void onResponse(@NonNull Call<ArrayList<InboxPropertyResponse>> call, @NonNull Response<ArrayList<InboxPropertyResponse>> response) {
                                                                if (response.isSuccessful()) {
                                                                    if (response.body().size() > 0) {
                                                                        mtrackList1 = new ArrayList<>();


                                                                        for (int i = 0; i < response.body().size(); i++) {
                                                                            mTrack = new TrackObject();

                                                                            mTrack.setTrackId(response.body().get(i).getTrackId());
                                                                            mTrack.setTrackEnName(response.body().get(i).getMessage());
                                                                            mTrack.setTrackArName(response.body().get(i).getMsgType());
                                                                            mTrack.setAlbumEnName(response.body().get(i).getTitle());
                                                                            mTrack.setTrackImage(response.body().get(i).getImgFile());
                                                                            mTrack.setAlbumArName(response.body().get(i).getPushNotifLogId());

                                                                            mtrackList1.add(mTrack);

                                                                        }
                                                                        if (mtrackList1.size() > 0) {
                                                                            progBar.setVisibility(View.GONE);
                                                                            audioRelative.setEnabled(true);


                                                                            SetListData();


                                                                        }
                                                                    } else {

                                                                        Toast.makeText(InboxActivity.this,
                                                                                R.string.noinboxfound,
                                                                                Toast.LENGTH_LONG).show();
                                                                        progBar.setVisibility(View.GONE);
                                                                        mSwipyRefreshLayout.setRefreshing(false);


                                                                    }
                                                                } else {
                                                                    ApiUtils.handelErrorCode(context, response.code());
                                                                    System.out.println("onFailure");
                                                                    progBar.setVisibility(View.GONE);
                                                                    mSwipyRefreshLayout.setRefreshing(false);
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(@NonNull Call<ArrayList<InboxPropertyResponse>> call, @NonNull Throwable t) {
                                                                progBar.setVisibility(View.GONE);
                                                                mSwipyRefreshLayout.setRefreshing(false);
                                                            }

                                                        }
        );
    }


    private void SetListData() {
        if (mtrackList1.size() > 0) {
            mtrackList = mtrackList1;
            progBar.setVisibility(View.GONE);
            audioRelative.setEnabled(true);
            mListAdaptor = new Inboxadapter(InboxActivity.this, R.layout.inboxitem, mtrackList);
            SharedPreferences prefs = getSharedPreferences("Inboxopened", Context.MODE_PRIVATE);
            if (!prefs.getBoolean("firstTime", false)) {
                // run your one time code
                for (int i = 0; i < mtrackList.size(); i++) {
                    arrayList.add(mtrackList.get(i).getAlbumArName());
                    Gson gson = new Gson();
                    String httpParamJSONList = gson.toJson(arrayList);

                    SharedPreferences prefs1 = getSharedPreferences("Inboxopened", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs1.edit();
                    editor.putString("Inboxopened1", httpParamJSONList);

                    editor.apply();
                    SharedPreferences.Editor editor1 = prefs.edit();
                    editor1.putBoolean("firstTime", true);
                    editor1.apply();
                    ShowPopupHint();

                }
            }
            SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(mListAdaptor, this,
                    (listView, reverseSortedPositions) -> {
                        for (int position : reverseSortedPositions) {
                            deleteinbox(mtrackList.get(position).getAlbumArName());

                            mListAdaptor.remove(position);
                        }
                    });

            swipeUndoAdapter.setAbsListView(mFavTrack);
            mFavTrack.setAdapter(swipeUndoAdapter);
            mFavTrack.enableSimpleSwipeUndo();
            mSwipyRefreshLayout.setRefreshing(false);
        } else {
            mSwipyRefreshLayout.setRefreshing(false);

            if (language.equalsIgnoreCase("ar")) {
                Toast.makeText(InboxActivity.this, R.string.nomesage, Toast.LENGTH_SHORT).show();
            }
            if (language.equalsIgnoreCase("en")) {
                Toast.makeText(InboxActivity.this, R.string.nomesage, Toast.LENGTH_SHORT).show();
            }

            progBar.setVisibility(View.GONE);

        }
    }

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

    private void GetSingerTracksFrmTrack(String string) {
        startwhellprogress();
        // Constants.SERVER_URl + Constants.REGISTER_PATH
        SharedPreferences prefs = getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSingerTracksFrmTrack + "?trackId=" + string + "&userId="
                + prefs.getString(Constants.USERID, "") + "&UserToken=" + prefs.getString(Constants.accesstoken, "");
        String fav_url = fav.replaceAll(" ", "%20");
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
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        // TODO Auto-generated method stub

    }


    private void SetSimilartrackres() {
        mAlbumList = new ArrayList<>();
        // Albumconstant.SONGS_LIST = mAlbumList;
        if (listoftrack.size() > 0) {
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
                mPlayist.setLikesCount(listoftrack.get(j)
                        .getLikesCount());
                mPlayist.setListenCount(listoftrack.get(j)
                        .getListenCount());
                mPlayist.setLyricFile(listoftrack.get(j).getLyricFile());
                mPlayist.setHasLyrics(listoftrack.get(j).getHasLyrics());
                mPlayist.setTrackLength(listoftrack.get(j).getTrackLength());
                mPlayist.setVideoID(listoftrack.get(j).getVideoID());
                mPlayist.setIsPremium(listoftrack.get(j).getIsPremium());

                mAlbumList.add(mPlayist);

            }
        }
        PlayerConstants.SONGS_LIST = mAlbumList;
        PlayerConstants.SONG_NUMBER = 0;

        progBar.setVisibility(View.GONE);
        if (Constants.isNetworkOnline(InboxActivity.this)) {

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
            trackid = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId();
            favo = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite();
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

            updateUI();

            changeButton();


            Log.d("TAG", "TAG Tapped INOUT(OUT)");

        } else {
            Toast.makeText(InboxActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void ShowPopupHint() {
        final Dialog dialoginbox = new Dialog(InboxActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialoginbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoginbox.setCanceledOnTouchOutside(false);
        dialoginbox.setContentView(R.layout.inboxpopup);
        Button cancel = dialoginbox.findViewById(R.id.btclose);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialoginbox.dismiss());

        // if button is clicked, close the custom dialog
        // do stuff
        new Handler().postDelayed(dialoginbox::dismiss, 5000);
        dialoginbox.show();
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

            mVideoslist = new ArrayList<>();
            // Albumconstant.SONGS_LIST = mAlbumList;
            for (int j = 0; j < listofvideos.size(); j++) {
                mvideolistobject = new VideoObjectResponse();

                mvideolistobject.setVideoID(listofvideos.get(j).getVideoID());
                mvideolistobject.setSingerEnName(listofvideos.get(j).getSingerEnName());
                mvideolistobject.setSingerArName(listofvideos.get(j).getSingerArName());

                mvideolistobject.setVideoArName(listofvideos.get(j).getVideoArName());
                mvideolistobject.setVideoEnName(listofvideos.get(j).getVideoEnName());

                mvideolistobject.setVideoPoster(listofvideos.get(j).getVideoPoster());
                mvideolistobject.setVideoPath(listofvideos.get(j).getVideoPath());

                mvideolistobject.setIsFavourite(listofvideos.get(j).getIsFavourite());

                mvideolistobject.setViewCount(listofvideos.get(j).getViewCount());
                mvideolistobject.setLikesCount(listofvideos.get(j).getLikesCount());
                mvideolistobject.setSingerId(listofvideos.get(j).getSingerId());
                mvideolistobject.setIsPremium(listofvideos.get(j).getIsPremium());
                mvideolistobject.setIsDownloaded(listofvideos.get(j).getIsDownloaded());


                mVideoslist.add(mvideolistobject);

            }

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
}
