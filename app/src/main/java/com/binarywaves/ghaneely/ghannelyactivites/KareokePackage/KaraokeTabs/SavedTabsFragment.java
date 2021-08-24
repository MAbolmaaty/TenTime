package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyadaptors.SavedTAbs_Adapter;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.ControlKaraoke;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.KaraokeSongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyresponse.KarokeSavedObject;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;

import java.util.List;

/**
 * Created by Amany on 24-Aug-17.
 */

public class SavedTabsFragment extends Fragment {

    public static RelativeLayout audioRelative;
    public static RelativeLayout progBar;
    private static ImageView play;
    private static ImageView pause;
    private static ProgressWheel pb1;
    private static TextView albumname;
    private static Context context;
    private static KarokeSavedObject data;
    private static List<KarokeSavedObject> songdata_list;
    private View rootView;
    private Bundle bundle;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private SavedTAbs_Adapter adapter;

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    public static void changeUI() {
        updateUI();
        changeButton();
    }

    private static void updateUI() {
        try {

            data = PlayerConstants.Karaoke_LIST.get(PlayerConstants.SONG_NUMBER);


            albumname.setText(data.getKaraokeName());


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

    public static void PlaySavedSong(int position) {
        if (Constants.isNetworkOnline(context)) {

            // audioRelative.setVisibility(View.VISIBLE);
            PlayerConstants.Karaoke_LIST = songdata_list;
            Log.e("list clicked ", "here");
            // streamService = new Intent(MainActivity.this,
            // StreamService.class);
            // startService(streamService);
            startwhellprogress();
            audioRelative.setEnabled(false);

            // intiPlayer(mtrackList.get(position).getTrackPath(),false);
            Log.e("list clicked ", "here");

            PlayerConstants.SONG_PAUSED = false;
            PlayerConstants.SONG_NUMBER = position;
            boolean isServiceRunning = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                    context);
            if (!isServiceRunning) {
                Intent i = new Intent(context, KaraokeSongService.class);
                context.startService(i);

            } else {
                PlayerConstants.SONG_CHANGE_HANDLER
                        .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }

            changeUI();

            Log.d("TAG", "TAG Tapped INOUT(OUT)");

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.saved, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        bundle = getArguments();
        if (bundle != null) {
            songdata_list = bundle.getParcelableArrayList("savedsong");
            Log.e("savedsong", songdata_list + "");
        }
        Init_UI();

        if (songdata_list != null && songdata_list.size() > 0) {

            SetTrackList(songdata_list);
        }

        return rootView;
    }

    private void Init_UI() {
        context = getContext();
        progBar = rootView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        audioRelative = rootView.findViewById(R.id.audioplayer);

        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);
        albumname = audioRelative.findViewById(R.id.album_name);
        String language = LanguageHelper.getCurrentLanguage(getContext());


        if (language.equalsIgnoreCase("ar")) {
            recyclerView.setRotationY(180);
            audioRelative.setRotationY(180);
        }

        InitializeOnClickAction();

    }

    private void SetTrackList(List<KarokeSavedObject> mtrackList1) {

        adapter = new SavedTAbs_Adapter(getActivity(), mtrackList1);
        recyclerView.setAdapter(adapter);


    }

    private void InitializeOnClickAction() {

        play.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                    getContext());
            if (isServiceRunning3) {

                ControlKaraoke.playControl(getContext());
            }


        });
        pause.setOnClickListener(v -> {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                    getContext());
            if (isServiceRunning3) {

                ControlKaraoke.pauseControl(getContext());

            }


        });

    }

/*

            boolean isServiceRunning2 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                    context);
            if (isServiceRunning2) {

                Intent i = new Intent(context, KaraokeSongService.class);

                context.stopService(i);
                audioRelative.setVisibility(View.GONE);
 }
       */


}



