package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter.Retrofit_ArtistRadio;
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
import com.binarywaves.ghaneely.ghannelyresponse.MoodsListResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.binarywaves.ghaneely.ghannelyutils.ArtistRadio;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistradioActivity extends ActivityMainRunnuing implements Retrofit_ArtistRadio.RetrofitPresenterListener {
    public static RelativeLayout audioRelative;
    public static TrackObject data1;
    public static RelativeLayout progBar;
    public static Activity activity;
    public static int selectessong = -1;
    private static Context context;
    private static ImageView play;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static ImageView back;
    private static ProgressWheel pb1;
    private static String language;
    private static TextView albumname, tvplaylistname, tvsongcount, tvduration;
    private static ImageView player_image, new_song_slider;
    private static TextView songname;
    private static ArrayList<TrackObject> mtrackList;
    private DynamicListView gridview;
    private TextView title_txt;
    //RelativeLayout pager_relative;
    private ImageView playplaylist;
    private ImageView pauseplaylist;
    private LinearLayout imagecontainer;
    private String title;
    private String MyslideAlbume;
    private static String like;
    public static PlaylistTrackAdaptor trackgalleryAdaptor;

    private static void updateUI() {
        try {
            TrackObject data1 = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);


            if (language.equalsIgnoreCase("ar")) {
                songname.setText(data1.getTrackArName());
                albumname.setText(data1.getAlbumArName());
            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(data1.getTrackEnName());
                albumname.setText(data1.getAlbumEnName());
            }
            // Bitmap albumArt = UtilFunctions.getAlbumart(context,
            // data.getAlbumId());
            if (data1.getIsFavourite().equalsIgnoreCase("true")) {
                Like.setVisibility(View.GONE);

                dislike.setVisibility(View.VISIBLE);
            }

            if (data1.getIsFavourite().equalsIgnoreCase("false")) {

                dislike.setVisibility(View.GONE);

                Like.setVisibility(View.VISIBLE);
                // make button enable
            }
            String imgpath = ServerConfig.Image_path + data1.getTrackImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");

            Picasso.with(context).load(final_imgpath).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(player_image);
            // linearLayoutPlayingSong.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeButton() {

        if (trackgalleryAdaptor != null) {
            trackgalleryAdaptor.notifyDataSetInvalidated();
        }
        if (PlayerConstants.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }

    }

    private static void changeUI() {
        updateUI();
        changeButton();
    }

    public static void changeUI1(final Context context) {


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(), context);
        if (isServiceRunning3) {
            updateUI3();
            changeButton3();

        }

        boolean isServiceRunning44 = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (isServiceRunning44) {
            changeUI();

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

            Picasso.with(context).load(final_imgpath).error(R.mipmap.defualt_img)
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

    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    private static void changeUI(final Context context) {

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

    public void setartistdata(MoodsListResponse response) {
        mtrackList = response;
        if (mtrackList != null && mtrackList.size() > 0) {
            setGridlist(mtrackList);
            imagecontainer.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_playlisttrackscroll);
        Applicationmanager.setContext(ArtistradioActivity.this);

        context = getApplicationContext();
        activity = this;
        language = LanguageHelper.getCurrentLanguage(context);

        StopService();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("album")) {
                MyslideAlbume = getIntent().getExtras().getString("album");
            }
        }


        if (extras != null) {
            if (extras.containsKey("title")) {
                title = getIntent().getExtras().getString("title");
            }
        }

        Init_Ui();


        changeUI(context);
        if (MyslideAlbume != null && !MyslideAlbume.equalsIgnoreCase("")) {
            GetListdj(MyslideAlbume);
        }
    }

    private void Init_Ui() {
        selectessong = -1;
        gridview = findViewById(R.id.gridview);
        title_txt = findViewById(R.id.title_txt);
        if (title != null)
            title_txt.setText(title);

        new_song_slider = findViewById(R.id.new_song_slider);
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        startwhellprogress();
        StopService();


        imagecontainer = findViewById(R.id.imagecontainer);
        back = findViewById(R.id.navigation_up);

        back.setOnClickListener(v -> {
            finish();
        });
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
        tvplaylistname.setText(title);
        tvsongcount = findViewById(R.id.tvcount);
        tvduration = findViewById(R.id.tvduration);
        PlayerItem_Onclick();

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
                        context, "1");
                // data.setIsFavourite(true);

            }

        });

        dislike.setOnClickListener(v -> {
            startwhellprogress();
            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                        context, "0");

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

            if (Constants.isNetworkOnline(ArtistradioActivity.this)) {
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
                Toast.makeText(ArtistradioActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPlayerScreen() {

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                getApplicationContext());
        if (isServiceRunning3) {

            Intent player = new Intent(ArtistradioActivity.this, PlayerAcreenActivity.class);
            TrackObject mTrack = Constants.Convertto_Track();

            player.putExtra("track", mTrack);
            startActivity(player);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
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

    private void GetListdj(String myString) {
        startwhellprogress();
        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSingerRadioTracks + "?singerId=" + myString + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Retrofit_ArtistRadio retrofitPresenter = new Retrofit_ArtistRadio(this, this);
        retrofitPresenter.getArtistRadioResponse(fav_url);


    }

    private void setGridlist(ArrayList<TrackObject> mtrackList) {
        // TODO Auto-generated method stub
        if (mtrackList != null && mtrackList.size() > 0) {

            playplaylist.setActivated(true);
            playplaylist.setClickable(true);
            appearanceAnimate();
            String text = String.valueOf(mtrackList.size()) + getResources().getString(R.string.blank) + getResources().getString(R.string.song);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvsongcount.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));


            } else {
                tvsongcount.setText(Html.fromHtml(text));
            }

            tvduration.setText(Constants.getDurationString(mtrackList));

            Picasso.with(getBaseContext()).load(ServerConfig.Image_pathSinger + mtrackList.get(0).getSingerImg())
                    .error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(new_song_slider, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    BitmapDrawable drawable = (BitmapDrawable) new_song_slider.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    GaussianBlur gaussian = new GaussianBlur(context);
                    gaussian.setMaxImageSize(60);
                    gaussian.setRadius(25); // max

                    Bitmap output = gaussian.render(bitmap, true);
                    final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);

                    imagecontainer.setBackground(ob);
                }

                @Override
                public void onError() {
                    BitmapDrawable drawable = (BitmapDrawable) new_song_slider.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    GaussianBlur gaussian = new GaussianBlur(context);
                    gaussian.setMaxImageSize(60);
                    gaussian.setRadius(25); // max

                    Bitmap output = gaussian.render(bitmap, true);
                    final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);

                    imagecontainer.setBackground(ob);
                }
            });

            boolean isServiceRunning11 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());


            boolean isServiceRunning2111 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                    getApplicationContext());
            if (!isServiceRunning11 && !isServiceRunning2111

                    ) {
                if (gridview.getAdapter() != null) {
                    gridview.performItemClick(gridview.getAdapter().getView(0, null, null), 0, 0);
                }
                playplaylist.setVisibility(View.GONE);
                pauseplaylist.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(ArtistradioActivity.this, getResources().getString(R.string.no_search_result),
                    Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void appearanceAnimate() {
        trackgalleryAdaptor = new PlaylistTrackAdaptor(ArtistradioActivity.this, R.layout.albumlist_item,
                mtrackList);
        gridview.setAdapter(trackgalleryAdaptor);
        setListViewHeightBasedOnChildren1(gridview);

    }

    private void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

    }


    public static void HandleFavUi(Addfavourite_Response response, final String favourite) {
        like = response.getResultDesc();
        progBar.setVisibility(View.GONE);
        if (favourite.equalsIgnoreCase("1")) {

            showCustomAlert(1);


            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    context);
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
                        context);
                if (isServiceRunning3) {


                    if (language.equalsIgnoreCase("ar")) {
                        PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);


                    }
                    if (language.equalsIgnoreCase("en")) {
                        PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);

                    }
                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsFavourite("false");

                }


                Like.setVisibility(View.VISIBLE);

                dislike.setVisibility(View.GONE);
            }
        }
    }


    private static void showCustomAlert(int fav) {

        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = activity.getLayoutInflater();

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
            changeUI(getApplicationContext());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Remove_handlers() {
        // TODO Auto-generated method stub
        PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(0, null, null), 0, 0);

    }


    private void Add_Removefavourite(String trackid2, final Context context2, final String favourite) {
        startwhellprogress();
        Constants.Add_Removefavourite(trackid2, context2, favourite);

    }

    @Override
    public void responseRadioReady(MoodsListResponse MoodsListresponses, int code, int functiontype) {
        progBar.setVisibility(View.GONE);
        if (MoodsListresponses != null && MoodsListresponses.size() > 0) {
            mtrackList = MoodsListresponses;

        }
        if (mtrackList != null && mtrackList.size() > 0) {
            setGridlist(mtrackList);
            imagecontainer.setVisibility(View.VISIBLE);
        } else {
            progBar.setVisibility(View.GONE);

            ApiUtils.handelErrorCode(getApplicationContext(), code);
            System.out.println("onFailure");

        }


    }
}
