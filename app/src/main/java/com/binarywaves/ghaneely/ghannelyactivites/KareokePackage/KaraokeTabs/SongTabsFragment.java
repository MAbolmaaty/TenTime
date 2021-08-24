package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyadaptors.SongsTAbs_Adapter;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.KaraokeSongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amany on 24-Aug-17.
 */

public class SongTabsFragment extends Fragment {

    private GridView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private SongsTAbs_Adapter adapter;
    private List<TrackObject> songdata_list;
    private Bundle bundle;
    private RelativeLayout rlmore;
    private String flag;
    private TextView tvmore;
    private Button ivmore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.songs_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        rlmore = rootView.findViewById(R.id.rlmore);
        tvmore = rootView.findViewById(R.id.tvmore);
        ivmore = rootView.findViewById(R.id.ivmore);

        bundle = getArguments();
        if (bundle != null) {
            songdata_list = bundle.getParcelableArrayList("songplayed");
            Log.e("songplayed", songdata_list + "");
        }
        if (songdata_list != null && songdata_list.size() > 0) {
            SetTrackList(songdata_list);
        }


        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                getContext());
        if (isServiceRunning2) {

            Intent i = new Intent(getContext(), KaraokeSongService.class);

            getContext().stopService(i);
        }
        rlmore.setOnClickListener(view -> {
            if (songdata_list != null && songdata_list.size() > 0) {
                if (flag.equalsIgnoreCase("more")) {
                    adapter = new SongsTAbs_Adapter(getActivity(), R.layout.songs_row, songdata_list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetInvalidated();
                    tvmore.setText(getResources().getString(R.string.viewless));
                    ivmore.setBackgroundResource(R.mipmap.dropup);
                    flag = "less";

                } else {
                    if (flag.equalsIgnoreCase("less")) {
                        SetTrackList(songdata_list);
                        tvmore.setText(getResources().getString(R.string.viewmore));
                        ivmore.setBackgroundResource(R.mipmap.dropdown);
                        flag = "more";

                    }
                }
            }
        });
        String language = LanguageHelper.getCurrentLanguage(getContext());


        if (language.equalsIgnoreCase("ar")) {
            recyclerView.setRotationY(180);
            tvmore.setRotationY(180);
           // ivmore.setRotationY(180);
        }

        return rootView;
    }

    private void SetTrackList(List<TrackObject> mtrackList1) {
        if (mtrackList1.size() > 20) {
            List<TrackObject> mtrackList2 = new ArrayList<>(mtrackList1).subList(0, 20);
            mtrackList2.subList(0, 20);

            adapter = new SongsTAbs_Adapter(getActivity(), R.layout.songs_row, mtrackList2);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetInvalidated();

            tvmore.setText(getResources().getString(R.string.viewmore));
            ivmore.setBackgroundResource(R.mipmap.dropdown);
            flag = "more";

        }else{
            adapter = new SongsTAbs_Adapter(getActivity(), R.layout.songs_row, mtrackList1);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetInvalidated();
            tvmore.setVisibility(View.GONE);
            ivmore.setVisibility(View.GONE);
        }
    }





}
