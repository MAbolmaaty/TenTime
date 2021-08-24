package com.binarywaves.ghaneely.ghannelyactivites.SearchTabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyactivites.VideoPlayerActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.VideoSearchAdapter;
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
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 25-Dec-17.
 */

public class SearchTab_VideoActivity  extends Fragment {
    public static RelativeLayout audioRelative;
    public static TrackObject CurrentTrack;
    public static TrackObject data1;
    public static RelativeLayout progBar;
    private static Activity activity;
    private static String favo;
    private static VideoSearchAdapter myVideoadAdaptor;
    public static int selectessong = -1;
    private static ImageView play;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    static ImageView back;
    private static Context context;
    static ImageView topimage;
    private static ProgressWheel pb1;
    private static String language;
    private static TextView songname;
    private static TextView albumname;
    private static ImageView player_image;
    FrameLayout frameLayout;
    private View activityView;
    private ListView lvtrack;
    private ArrayList<VideoObjectResponse> mvideolist;
    private String like;
    private String UserSearchID;
    private RelativeLayout rlno_search;
    private Bundle bundle;
    private ArrayList<VideoObjectResponse> newtrackobject;

    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    public static void changeUI1(final Context context) {

        boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(), context);
        if (isServiceRunning) {
            changeUI();

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

            Picasso.with(context).load(final_imgpath).error(R.mipmap.defualt_img)
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

    private static void updateUI() {
        try {
            if (language.equalsIgnoreCase("ar")) {
                songname.setText(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackArName());
                albumname.setText(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumArName());
            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackEnName());
                albumname.setText(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumEnName());
            }
            // Bitmap albumArt = UtilFunctions.getAlbumart(context,
            // data.getAlbumId());
            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite().equalsIgnoreCase("true")) {
                Like.setVisibility(View.GONE);

                dislike.setVisibility(View.VISIBLE);
            }

            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite().equalsIgnoreCase("false")) {

                dislike.setVisibility(View.GONE);

                Like.setVisibility(View.VISIBLE);
                // make button enable
            }
            String imgpath = ServerConfig.Image_path + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage();
            String final_imgpath = imgpath.replaceAll(" ", "%20");


            Picasso.with(context).load(final_imgpath).error(R.mipmap.defualt_img)
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

    private static void changeUI() {
        updateUI();
        changeButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        activity = getActivity();
        // Inflate the layout for this fragment
        activityView = inflater.inflate(R.layout.playlist_search_activity, container, false);
        context = getContext();
        language = LanguageHelper.getCurrentLanguage(context);

        bundle = getArguments();
        if (bundle != null) {
            mvideolist = bundle.getParcelableArrayList("videoLst");
            UserSearchID = bundle.getString("UsersearchID");

            Log.e("bundle", mvideolist + "");
        }
        newtrackobject = new ArrayList<>();
        InitlizeUi();
        changeUI1(context);

        return activityView;
    }

    private void InitlizeUi() {
        selectessong = -1;
        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        lvtrack = activityView.findViewById(R.id.lvtrack);
        audioRelative = activityView.findViewById(R.id.audioplayer);
        String language = LanguageHelper.getCurrentLanguage(getContext());


        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);

        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);
        rlno_search = activityView.findViewById(R.id.rlno_search);
        if (language.equalsIgnoreCase("ar")) {
            lvtrack.setRotationY(180);
            audioRelative.setRotationY(180);
            rlno_search.setRotationY(180);

        }
        SetOnClick();
        lvtrack.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(context)) {
                StopService();
                startwhellprogress();
                audioRelative.setEnabled(false);

                Intent player = new Intent(context, VideoPlayerActivity.class);
                PlayerConstants.SONGS_LISTVideo = newtrackobject;
                if (PlayerConstants.SONGS_LISTVideo != null && PlayerConstants.SONGS_LISTVideo.size() > 0) {
                    player.putExtra("track", PlayerConstants.SONGS_LISTVideo.get(position));
                    PlayerConstants.SONG_NUMBER = position;
                    context.startActivity(player);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();
                }


                Log.d("TAG", "TAG Tapped INOUT(OUT)");
                AddUserSearchResult(newtrackobject.get(position).getVideoID(), Integer.valueOf("5"), UserSearchID);


            } else {
                Toast.makeText(context, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        });

        if (mvideolist != null && mvideolist.size() > 0) {
            for (int i = 0; i < mvideolist.size(); i++) {
                VideoObjectResponse mTrack = new VideoObjectResponse();
                mTrack.setSingerId(mvideolist.get(i).getSingerId());

                mTrack.setVideoID(mvideolist.get(i).getVideoID());
                mTrack.setVideoArName(mvideolist.get(i).getVideoArName());
                mTrack.setVideoEnName(mvideolist.get(i).getVideoEnName());
                mTrack.setSingerEnName(mvideolist.get(i).getSingerEnName());
                mTrack.setSingerArName(mvideolist.get(i).getSingerArName());

                mTrack.setVideoPoster(mvideolist.get(i).getVideoPoster());
                mTrack.setVideoPath(mvideolist.get(i).getVideoPath());
                mTrack.setIsFavourite(mvideolist.get(i).getIsFavourite());
                mTrack.setLikesCount(mvideolist.get(i).getLikesCount());
                mTrack.setViewCount(mvideolist.get(i).getViewCount());

                mTrack.setIsPremium(mvideolist.get(i).getIsPremium());
                mTrack.setIsDownloaded(mvideolist.get(i).getIsDownloaded());

                newtrackobject.add(mTrack);
            }
            SetTrackList(newtrackobject);
        } else {
            lvtrack.setVisibility(View.GONE);
            rlno_search.setVisibility(View.VISIBLE);
        }


    }

    private void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, activity.getApplication());
        tracklisiten.StopService();

    }

    @Override
    public void onResume() {
        if (myVideoadAdaptor != null)
            myVideoadAdaptor.notifyDataSetInvalidated();

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                context);

        boolean isServiceRunning6 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                context);


        if (!isServiceRunning3
                && !isServiceRunning6)
            audioRelative.setVisibility(View.GONE);
        else
            audioRelative.setVisibility(View.VISIBLE);
        super.onResume();

        try {

            changeUI1(context);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setListViewHeightBasedOnChildren1(ListView listView) {
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

    private void SetTrackList(List<VideoObjectResponse> mtrackList1) {

        myVideoadAdaptor = new VideoSearchAdapter(context, R.layout.albumlist_item, mtrackList1);
        lvtrack.setAdapter(myVideoadAdaptor);
        setListViewHeightBasedOnChildren1(lvtrack);


    }

    private void SetOnClick() {


        play.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    context);
            if (isServiceRunning3) {
                PlayerConstants.SONGnext = false;

                Controls.playControl(context);
            }
            boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                    context);
            if (isServiceRunning4) {
                RadioConstant.SONGnext = false;

                ControlRadio.playControl(context);

            }

        });
        pause.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    context);
            if (isServiceRunning3) {
                PlayerConstants.SONGnext = false;

                Controls.pauseControl(context);

            }

            boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                    context);
            if (isServiceRunning4) {
                RadioConstant.SONGnext = false;

                ControlRadio.pauseControl(context);

            }

        });

        Like.setOnClickListener(v -> {
            startwhellprogress();

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    context);
            if (isServiceRunning3) {
                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                        context, "1");
                // data.setIsFavourite(true);

            }

        });

        dislike.setOnClickListener(v -> {
            startwhellprogress();


            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    context);
            if (isServiceRunning3) {
                Add_Removefavourite(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(),
                        context, "0");

            }

        });


        audioRelative.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getPlayerScreen();

        });
    }

    @SuppressWarnings("ConstantConditions")
    private void getPlayerScreen() {


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                context);
        if (isServiceRunning3) {

            Intent player = new Intent(context, PlayerAcreenActivity.class);
            TrackObject mTrack = Constants.Convertto_Track();


            player.putExtra("track", mTrack);

            startActivity(player);
            getActivity().finish();


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
                    HandleFavUi(mresult.body(), favourite, context2);

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

    private void HandleFavUi(Addfavourite_Response response, final String favourite, Context context2) {
        like = response.getResultDesc();
        progBar.setVisibility(View.GONE);
        if (favourite.equalsIgnoreCase("1")) {

            showCustomAlert(1);


            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    context2);
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
                        context2);
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
    }

    private void showCustomAlert(int fav) {

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

    private void AddUserSearchResult(String contentid, int contenttypeid, String userSearchID) {
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddUserSearchResult + "?userSearchId=" + userSearchID + "&userId=" + SharedPrefHelper.getSharedString(getContext(), Constants.USERID)
                + "&contentId=" + contentid + "&contentTypeId=" + contenttypeid + "&UserToken=" + SharedPrefHelper.getSharedString(getContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddUserSearchResult(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    progBar.setVisibility(View.GONE);

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


}