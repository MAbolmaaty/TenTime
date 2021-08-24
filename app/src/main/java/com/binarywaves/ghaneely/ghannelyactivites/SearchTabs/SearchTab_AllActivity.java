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
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayListTracksActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyactivites.VideoPlayerActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.RecentSearchAdapter;
import com.binarywaves.ghaneely.ghannelyadaptors.SearchResultAdapter;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlRadio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongServiceradio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.RadioConstant;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.AlbumLst;
import com.binarywaves.ghaneely.ghannelyresponse.GetRecentUserSearchesListResponse;
import com.binarywaves.ghaneely.ghannelyresponse.GetVideoDataFrmIDResponse;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistnotificationResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackres;
import com.binarywaves.ghaneely.ghannelyresponse.TrackLst;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 11-May-17.
 */

@SuppressWarnings({"EmptyMethod", "MismatchedQueryAndUpdateOfCollection"})
public class SearchTab_AllActivity extends Fragment {
    public static RelativeLayout audioRelative;
    public static TrackObject CurrentTrack;
    public static TrackObject data1;
    public static RelativeLayout progBar;
    private static Activity activity;
    private static String favo;
    private static RecentSearchAdapter recentSearchAdapter;
    private static SearchResultAdapter searchResultAdapter;
    public static int selectessong = -1;
    public static int selectessongsearch = -1;
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
    ListView lvtrack;
    private ArrayList<GetRecentUserSearchesListResponse> RecentUserSearchesList;
    private String like;
    private String UserSearchID;
    private ListView lvrecent;
    private ListView lvressearch;
    private LinearLayout linresult;
    private LinearLayout con;
    private ArrayList<GetRecentUserSearchesListResponse> AllResultSearchList;
    private RelativeLayout rlno_search;
    private LinearLayout linsearch;
    private Bundle bundle;
    private ArrayList<AlbumLst> albumLst;
    private ArrayList<ArtistRadio> singerLst;
    private ArrayList<TrackLst> trackLst;
    private ArrayList<VideoObjectResponse> videoLst;

    private ArrayList<PlaylistnotificationResponse> playlistlst;
    private ArrayList<GetRecentUserSearchesListResponse> newtracklst;
    private ArrayList<GetRecentUserSearchesListResponse> newalbumLst;
    private ArrayList<GetRecentUserSearchesListResponse> newsingerLst;
    private ArrayList<GetRecentUserSearchesListResponse> newplaylistlst;
    private ArrayList<GetRecentUserSearchesListResponse> newvideotlst;

    private ArrayList<TrackObject> newtrackobject;
    private ArrayList<SlideAlbumObject> malbumObjects;
    private ArrayList<TrackObject> newtrackobjectsearch;
    private ArrayList<VideoObjectResponse>newvideoobjectsearch ;
    private ArrayList<VideoObjectResponse> listofvideos;

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
        if (recentSearchAdapter != null) {
            recentSearchAdapter.notifyDataSetInvalidated();
        }
        if (searchResultAdapter != null) {
            searchResultAdapter.notifyDataSetInvalidated();
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
        activityView = inflater.inflate(R.layout.activity_searchall, container, false);
        context = getContext();
        language = LanguageHelper.getCurrentLanguage(context);
        bundle = getArguments();
        if (bundle != null) {
            RecentUserSearchesList = bundle.getParcelableArrayList("RecentUserSearchesList");
            albumLst = bundle.getParcelableArrayList("albumLst");
            singerLst = bundle.getParcelableArrayList("singerLst");

            trackLst = bundle.getParcelableArrayList("trackLst");

            playlistlst = bundle.getParcelableArrayList("playlistlst");
            videoLst = bundle.getParcelableArrayList("videolst");

            UserSearchID = bundle.getString("UsersearchID");
            Log.e("RecentUserSearchesList", RecentUserSearchesList + "");
        }

        InitlizeUi();
        changeUI1(context);
        return activityView;
    }

    private void InitlizeUi() {
        selectessong = -1;
        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        rlno_search = activityView.findViewById(R.id.rlno_search);
        linsearch = activityView.findViewById(R.id.linsearch);
        lvrecent = activityView.findViewById(R.id.lvrecent);
        lvrecent.setOnItemClickListener((parent, view, position, id) -> {
                    // TODO Auto-generated method stub
                    if (Constants.isNetworkOnline(context)) {

                        AddUserSearchResult(RecentUserSearchesList.get(position).getContentId(), Integer.valueOf(RecentUserSearchesList.get(position).getContentTypeId()), RecentUserSearchesList.get(position).getUsersearchID());
                        switch (Integer.valueOf(RecentUserSearchesList.get(position).getContentTypeId())) {
                            case 1://singer

                                Intent playTrack = new Intent(context, ArtistTabsActivity.class);
                                playTrack.putExtra("playlistId", Integer.parseInt(RecentUserSearchesList.get(position).getContentId()));
                                startActivity(playTrack);


                                break;
                            //album
                            case 2:
                                Intent albumIntent = new Intent(getContext(), AlbumTabsActivity.class);
                                albumIntent.putExtra("id", RecentUserSearchesList.get(position).getContentId());
                                startActivity(albumIntent);

                                break;
                            //track
                            case 3:
                                StopService();
                                startwhellprogress();
                                GetSingerTracksFrmTrack(RecentUserSearchesList.get(position).getContentId());


                                audioRelative.setEnabled(false);
                                selectessong = position;
                                selectessongsearch = -1;
                                if (recentSearchAdapter != null) {
                                    recentSearchAdapter.notifyDataSetInvalidated();
                                }
                                if (searchResultAdapter != null) {
                                    searchResultAdapter.notifyDataSetInvalidated();
                                }
                                break;
                            //playlist
                            case 4:
                                Intent playTrack1 = new Intent(context, PlayListTracksActivity.class);
                                playTrack1.putExtra("id", RecentUserSearchesList.get(position).getContentId());

                                startActivity(playTrack1);

                                break;


                            //videos
                            case 5:
                                StopService();
                                startwhellprogress();
                                GetSingerVideoFrmTrack(RecentUserSearchesList.get(position).getContentId());

                                break;
                        }
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.gnrl_internet_error),
                                Toast.LENGTH_LONG).show();
                    }

                }

        );
        lvressearch = activityView.findViewById(R.id.lvressearch);
        lvressearch.setOnItemClickListener((parent, view, position, id) -> {
                    // TODO Auto-generated method stub
                    if (Constants.isNetworkOnline(context)) {

                        AddUserSearchResult(AllResultSearchList.get(position).getContentId(), Integer.valueOf(AllResultSearchList.get(position).getContentTypeId()), UserSearchID);
                        switch (Integer.valueOf(AllResultSearchList.get(position).getContentTypeId())) {
                            case 1://singer
                                if(singerLst!=null&&singerLst.size()>0){
                                for (int i = 0; i <= singerLst.size(); i++) {

                                    if (AllResultSearchList.get(position).getContentId().equalsIgnoreCase(String.valueOf(singerLst.get(i).getSingerId()))) {

                                        Intent playTrack = new Intent(context, ArtistTabsActivity.class);
                                        playTrack.putExtra("playlistId", singerLst.get(i).getSingerId());
                                        startActivity(playTrack);
                                        break;
                                    }}
                                }

                                break;
                            //album
                            case 2:
                                if(malbumObjects!=null&&malbumObjects.size()>0){


                                for (int i = 0; i <= malbumObjects.size(); i++) {

                                    if (AllResultSearchList.get(position).getContentId().equalsIgnoreCase(malbumObjects.get(i).getAlbumId())) {

                                        Intent albumIntent = new Intent(getContext(), AlbumTabsActivity.class);
                                        albumIntent.putExtra("id", malbumObjects.get(i).getAlbumId());
                                        startActivity(albumIntent);
                                        break;
                                    }
                                }}
                                break;
                            //track
                            case 3:
                                StopService();
                                startwhellprogress();
                                if(newtrackobjectsearch!=null&&newtrackobjectsearch.size()>0){

                                    for (int i = 0; i <= newtrackobjectsearch.size(); i++) {

                                    if (AllResultSearchList.get(position).getContentId().equalsIgnoreCase(newtrackobjectsearch.get(i).getTrackId())) {

                                        GetSingerTracksFrmTrack(newtrackobjectsearch.get(i).getTrackId());
                                        break;
                                    }}
                                }
                                audioRelative.setEnabled(false);
                                selectessongsearch = position;
                                selectessong = -1;
                                if (recentSearchAdapter != null) {
                                    recentSearchAdapter.notifyDataSetInvalidated();
                                }
                                if (searchResultAdapter != null) {
                                    searchResultAdapter.notifyDataSetInvalidated();
                                }
                                break;
                            //playlist
                            case 4:
                                if(playlistlst!=null&&playlistlst.size()>0){

                                    for (int i = 0; i <= playlistlst.size(); i++) {

                                    if (AllResultSearchList.get(position).getContentId().equalsIgnoreCase(playlistlst.get(i).getPlaylistId())) {


                                        Intent playTrack1 = new Intent(context, PlayListTracksActivity.class);
                                        playTrack1.putExtra("id", playlistlst.get(i).getPlaylistId());

                                        startActivity(playTrack1);
                                        break;
                                    }
                                }}

                                break;
                        }
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.gnrl_internet_error),
                                Toast.LENGTH_LONG).show();
                    }

                }

        );

        linresult = activityView.findViewById(R.id.linresult);
        con = activityView.findViewById(R.id.con);
        String language = LanguageHelper.getCurrentLanguage(getContext());



        audioRelative = activityView.findViewById(R.id.audioplayer);
        if (language.equalsIgnoreCase("ar")) {
            linsearch.setRotationY(180);
            audioRelative.setRotationY(180);
            rlno_search.setRotationY(180);
        }
        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);

        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);

        SetOnClick();
        if (RecentUserSearchesList != null && RecentUserSearchesList.size() > 0)

        {
            Set_RecentSearchList(RecentUserSearchesList);
        }

        if (albumLst == null && singerLst == null && trackLst == null && playlistlst == null&&videoLst == null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            con.setLayoutParams(params);
            linresult.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            linresult.setLayoutParams(params);
            con.setVisibility(View.GONE);
            setGenericarray();
        }
        if (albumLst == null && singerLst == null && trackLst == null && playlistlst == null &&videoLst == null&& RecentUserSearchesList == null) {
            linsearch.setVisibility(View.GONE);
            rlno_search.setVisibility(View.VISIBLE);
        }

        if (UserSearchID != null && !UserSearchID.equalsIgnoreCase("") && albumLst == null &&videoLst == null&& singerLst == null && trackLst == null && playlistlst == null
                && albumLst.size() == 0 && singerLst.size() == 0 && trackLst.size() == 0 && playlistlst.size() == 0&& videoLst.size() == 0) {

            linsearch.setVisibility(View.GONE);
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
        if (recentSearchAdapter != null) {
            recentSearchAdapter.notifyDataSetInvalidated();
        }

        if (searchResultAdapter != null){
            searchResultAdapter.notifyDataSetInvalidated();}
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


    private void Set_RecentSearchList(ArrayList<GetRecentUserSearchesListResponse> recentUserSearchesList) {
        recentSearchAdapter = new RecentSearchAdapter(context, R.layout.activity_search_listitem, recentUserSearchesList);
        lvrecent.setAdapter(recentSearchAdapter);

    }

    private void Set_ResultSearchList(ArrayList<GetRecentUserSearchesListResponse> recentUserSearchesList) {
        searchResultAdapter = new SearchResultAdapter(context, R.layout.activity_search_listitem, recentUserSearchesList);
        lvressearch.setAdapter(searchResultAdapter);

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

            if(mTrack!=null)
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
      final  ViewGroup nullparent = null;

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

    private void setGenericarray() {
        AllResultSearchList = new ArrayList<>();

        if (trackLst != null && trackLst.size() > 0) {

            newtracklst = new ArrayList<>();
            for (int j = 0; j < trackLst.size(); j++) {
                GetRecentUserSearchesListResponse mPlayist = new GetRecentUserSearchesListResponse();

                mPlayist.setContentId(trackLst.get(j).getTrackId());
                mPlayist.setContentArName(trackLst.get(j).getTrackArName());
                mPlayist.setContentEnName(trackLst.get(j).getTrackEnName());

                mPlayist.setContentImage(trackLst.get(j).getTrackImage());
                mPlayist.setContentTypeId("3");


                newtracklst.add(mPlayist);
                AllResultSearchList.add(mPlayist);

            }

        }
        newtrackobjectsearch = new ArrayList<>();

        if (trackLst != null && trackLst.size() > 0) {
            for (int i = 0; i < trackLst.size(); i++) {
                TrackObject mTrack = new TrackObject();
                mTrack.setSingerId(trackLst.get(i).getSingerId());
                mTrack.setAlbumId(trackLst.get(i).getAlbumId());

                mTrack.setTrackId(trackLst.get(i).getTrackId());
                mTrack.setTrackEnName(trackLst.get(i).getTrackEnName());
                mTrack.setAlbumEnName(trackLst.get(i).getAlbumEnName());
                mTrack.setSingerEnName(trackLst.get(i).getSingerEnName());
                mTrack.setTrackArName(trackLst.get(i).getTrackArName());
                mTrack.setAlbumArName(trackLst.get(i).getAlbumArName());
                mTrack.setSingerArName(trackLst.get(i).getSingerArName());

                mTrack.setTrackImage(trackLst.get(i).getTrackImage());
                mTrack.setTrackPath(trackLst.get(i).getTrackPath());
                mTrack.setIsFavourite(trackLst.get(i).getIsFavourite());
                mTrack.setIsRBT(trackLst.get(i).getIsRBT());
                mTrack.setLikesCount(trackLst.get(i).getLikesCount());
                mTrack.setListenCount(trackLst.get(i).getListenCount());
                mTrack.setTrackLength(trackLst.get(i).getTrackLength());

                mTrack.setHasLyrics(trackLst.get(i).getHasLyrics());
                mTrack.setLyricFile(trackLst.get(i).getLyricFile());
                mTrack.setVideoID(trackLst.get(i).getVideoID());
                mTrack.setIsDownloaded(trackLst.get(i).getIsDownloaded());
                mTrack.setIsPremium(trackLst.get(i).getIsPremium());

                newtrackobjectsearch.add(mTrack);
            }
        }
        if (albumLst != null && albumLst.size() > 0) {

            newalbumLst = new ArrayList<>();
            for (int j = 0; j < albumLst.size(); j++) {
                GetRecentUserSearchesListResponse mPlayist = new GetRecentUserSearchesListResponse();

                mPlayist.setContentId(albumLst.get(j).getAlbumId());
                mPlayist.setContentArName(albumLst.get(j).getAlbumArName());
                mPlayist.setContentEnName(albumLst.get(j).getAlbumEnName());

                mPlayist.setContentImage(albumLst.get(j).getAlbumImgPath());
                mPlayist.setContentTypeId("2");


                newalbumLst.add(mPlayist);
                AllResultSearchList.add(mPlayist);


            }
        }
        malbumObjects = new ArrayList<>();

        for (int j = 0; j < albumLst.size(); j++) {
            SlideAlbumObject mPlayist = new SlideAlbumObject();

            mPlayist.setAlbumId(albumLst.get(j).getAlbumId());
            mPlayist.setAlbumEnName(albumLst.get(j).getAlbumEnName());
            mPlayist.setAlbumArName(albumLst.get(j).getAlbumArName());
            mPlayist.setSingerEnName(albumLst.get(j).getSingerArName());
            mPlayist.setSingerArName(albumLst.get(j).getSingerEnName());
            mPlayist.setAlbumImgPath(albumLst.get(j).getAlbumImgPath());
            malbumObjects.add(mPlayist);
        }

        if (singerLst != null && singerLst.size() > 0) {

            newsingerLst = new ArrayList<>();
            for (int j = 0; j < singerLst.size(); j++) {
                GetRecentUserSearchesListResponse mPlayist = new GetRecentUserSearchesListResponse();

                mPlayist.setContentId(String.valueOf(singerLst.get(j).getSingerId()));
                mPlayist.setContentArName(singerLst.get(j).getSingerArName());
                mPlayist.setContentEnName(singerLst.get(j).getSingerEnName());

                mPlayist.setContentImage(singerLst.get(j).getSingerImgPath());
                mPlayist.setContentTypeId("1");


                newsingerLst.add(mPlayist);
                AllResultSearchList.add(mPlayist);


            }
        }


        if (playlistlst != null && playlistlst.size() > 0) {

            newplaylistlst = new ArrayList<>();
            for (int j = 0; j < playlistlst.size(); j++) {
                GetRecentUserSearchesListResponse mPlayist = new GetRecentUserSearchesListResponse();

                mPlayist.setContentId(playlistlst.get(j).getPlaylistId());
                mPlayist.setContentArName(playlistlst.get(j).getPlaylistName());
                mPlayist.setContentEnName(playlistlst.get(j).getPlaylistName());

                mPlayist.setContentImage(playlistlst.get(j).getPlaylistImgPath());
                mPlayist.setContentTypeId("4");


                newplaylistlst.add(mPlayist);
                AllResultSearchList.add(mPlayist);


            }
        }


        if (videoLst != null && videoLst.size() > 0) {

            newvideotlst = new ArrayList<>();
            for (int j = 0; j < videoLst.size(); j++) {
                GetRecentUserSearchesListResponse mPlayist = new GetRecentUserSearchesListResponse();

                mPlayist.setContentId(videoLst.get(j).getVideoID());
                mPlayist.setContentArName(videoLst.get(j).getVideoArName());
                mPlayist.setContentEnName(videoLst.get(j).getVideoEnName());

                mPlayist.setContentImage(videoLst.get(j).getVideoPoster());
                mPlayist.setContentTypeId("5");


                newvideotlst.add(mPlayist);
                AllResultSearchList.add(mPlayist);

            }

        }
        newvideoobjectsearch= new ArrayList<>();

        if (videoLst != null && videoLst.size() > 0) {
            for (int i = 0; i < videoLst.size(); i++) {
                VideoObjectResponse mTrack = new VideoObjectResponse();
                mTrack.setSingerId(videoLst.get(i).getSingerId());

                mTrack.setVideoEnName(videoLst.get(i).getVideoEnName());
                mTrack.setSingerEnName(videoLst.get(i).getSingerEnName());
                mTrack.setVideoArName(videoLst.get(i).getVideoArName());
                mTrack.setSingerArName(videoLst.get(i).getSingerArName());

                mTrack.setVideoPoster(videoLst.get(i).getVideoPoster());
                mTrack.setVideoPath(videoLst.get(i).getVideoPath());
                mTrack.setIsFavourite(videoLst.get(i).getIsFavourite());
                mTrack.setLikesCount(videoLst.get(i).getLikesCount());
                mTrack.setViewCount(videoLst.get(i).getViewCount());

                mTrack.setVideoID(videoLst.get(i).getVideoID());
                mTrack.setIsDownloaded(videoLst.get(i).getIsDownloaded());
                mTrack.setIsPremium(videoLst.get(i).getIsPremium());

                newvideoobjectsearch.add(mTrack);
            }
        }
        if (AllResultSearchList != null && AllResultSearchList.size() > 0) {
            Set_ResultSearchList(AllResultSearchList);
        }


    }


    private void GetSingerTracksFrmTrack(String string) {
        // Constants.SERVER_URl + Constants.REGISTER_PATH
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSingerTracksFrmTrack + "?trackId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(getContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Api_Interface service = ServiceGenerator.createService();

        service.GetSingerTracksFrmTrack(fav_url).enqueue(new Callback<Similartrackres>() {

            @Override
            public void onResponse(@NonNull Call<Similartrackres> call, @NonNull Response<Similartrackres> mresult) {
                if (mresult.isSuccessful()) {
                    Similartrackres response = mresult.body();

                    if (response != null) {
                        if (response.size() > 0) {
                            newtrackobject = new ArrayList<>();

                            for (int i = 0; i < response.size(); i++) {
                                TrackObject mTrack = new TrackObject();
                                mTrack.setSingerId(response.get(i).getSingerId());
                                mTrack.setAlbumId(response.get(i).getAlbumId());

                                mTrack.setTrackId(response.get(i).getTrackId());
                                mTrack.setTrackEnName(response.get(i).getTrackEnName());
                                mTrack.setAlbumEnName(response.get(i).getAlbumEnName());
                                mTrack.setSingerEnName(response.get(i).getSingerEnName());
                                mTrack.setTrackArName(response.get(i).getTrackArName());
                                mTrack.setAlbumArName(response.get(i).getAlbumArName());
                                mTrack.setSingerArName(response.get(i).getSingerArName());
                                mTrack.setTrackImage(response.get(i).getTrackImage());
                                mTrack.setTrackPath(response.get(i).getTrackPath());
                                mTrack.setIsFavourite(response.get(i).getIsFavourite());
                                mTrack.setIsRBT(response.get(i).getIsRBT());
                                mTrack.setLikesCount(response.get(i).getLikesCount());
                                mTrack.setListenCount(response.get(i).getListenCount());
                                mTrack.setTrackLength(response.get(i).getTrackLength());
                                mTrack.setHasLyrics(response.get(i).getHasLyrics());
                                mTrack.setLyricFile(response.get(i).getLyricFile());
                                mTrack.setVideoID(response.get(i).getVideoID());
                                mTrack.setIsPremium(response.get(i).getIsPremium());
                                mTrack.setIsDownloaded(response.get(i).getIsDownloaded());

                                newtrackobject.add(mTrack);
                            }

                            PlayerConstants.SONGS_LIST = newtrackobject;

                            if (newtrackobject.get(0).getIsFavourite().equalsIgnoreCase("true")) {
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
                            PlayerConstants.SONG_NUMBER = 0;

                            boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                                    context);
                            if (!isServiceRunning) {
                                Intent in1 = new Intent(context, SongService.class);
                                context.startService(in1);

                            } else {
                                PlayerConstants.SONG_CHANGE_HANDLER
                                        .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                            }
                            updateUI();
                            // progBar.setVisibility(View.GONE);

                            changeButton();

                            Log.d("TAG", "TAG Tapped INOUT(OUT)");
                        }
                        progBar.setVisibility(View.GONE);
                        if(recentSearchAdapter!=null) {
                            recentSearchAdapter.notifyDataSetInvalidated();
                        }

                    }
                    progBar.setVisibility(View.GONE);
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


    private void AddUserSearchResult(String contentid, int contenttypeid, String userSearchID) {
        // Constants.SERVER_URl + Constants.REGISTER_PATH
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

    public void setListViewHeightBasedOnChildren1(ListView listView) {
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

    private void GetSingerVideoFrmTrack(String string) {
      startwhellprogress();
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

            videoLst = new ArrayList<>();
            // Albumconstant.SONGS_LIST = mAlbumList;
            for (int j = 0; j < listofvideos.size(); j++) {
                VideoObjectResponse mvideolistobject = new VideoObjectResponse();

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


                videoLst.add(mvideolistobject);

            }

            PlayerConstants.SONGS_LISTVideo = videoLst;
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
