package com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs;

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
import com.binarywaves.ghaneely.ghannelyadaptors.AbumGallareyAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.GalleryAdaptor;
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
 * Created by Amany on 10-Apr-17.
 */

@SuppressWarnings("EmptyMethod")
public class AlbumTab_Otheralbum_Activity extends Fragment {
    public static RelativeLayout audioRelative;
    public static TrackObject CurrentTrack;
    public static TrackObject data1;
    public static RelativeLayout progBar;
    private static Activity activity;
    private static String favo;
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
    private String like;
    private AbumGallareyAdaptor myTrackadAdaptor;
    private ArrayList<SlideAlbumObject> otheralbumLst;
    private Bundle bundle;
    private GalleryAdaptor madaptor, songAdaptor;

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

    @SuppressWarnings("EmptyMethod")
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

        language = LanguageHelper.getCurrentLanguage(context);

        bundle = getArguments();
        if (bundle != null)
            otheralbumLst = bundle.getParcelableArrayList("otheralbumLst");
        Log.e("bundle", otheralbumLst + "");

        InitlizeUi();
        return activityView;
    }

    private void InitlizeUi() {
        AlbumTabsActivity.selectedfragment=1;

        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        audioRelative = activityView.findViewById(R.id.audioplayer);

        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);

        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);
        //End of player initilaization
        SetOnClick();
        lvtrack = activityView.findViewById(R.id.lvtrack);
        String language = LanguageHelper.getCurrentLanguage(getContext());


        if (language.equalsIgnoreCase("ar")) {
            lvtrack.setRotationY(180);
            audioRelative.setRotationY(180);
        }
        lvtrack.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(context)) {
                if (position >= otheralbumLst.size()) {
                    position = position % otheralbumLst.size();
                }
                Intent albumIntent = new Intent(context, AlbumTabsActivity.class);
                albumIntent.putExtra("album", otheralbumLst.get(position));
                startActivity(albumIntent);
                getActivity().finish();

            } else {
                Toast.makeText(context, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        });

        changeUI1(context);

        if (otheralbumLst != null && otheralbumLst.size() > 0)
            SetOtherAlbumLst(otheralbumLst);

    }

    public void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, activity.getApplication());
        tracklisiten.StopService();

    }

    @Override
    public void onResume() {
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

    private void SetOtherAlbumLst(ArrayList<SlideAlbumObject> mtrackList1) {
        songAdaptor = new GalleryAdaptor(context, R.layout.other_relatedalbumlistitem, mtrackList1, 2);
        lvtrack.setAdapter(songAdaptor);

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
        ViewGroup nullparent = null;
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
}
