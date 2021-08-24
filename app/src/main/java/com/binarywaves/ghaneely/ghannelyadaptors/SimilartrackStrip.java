package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.Moods_dilist;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackres;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackstripresponse;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilartrackStrip extends RecyclingPagerAdapter {
    public static ArrayList<Similartrackstripresponse> mSlideList;
    private static ArrayList<Similartrackstripresponse> listoftrack;
    public static Activity activity;
    public RelativeLayout progBar;
    // Declare Variables
    private Context context;
    TextView tvtitle;
    TextView Descreption;
    Button play;
    ProgressDialog dialog;
    private LayoutInflater inflater;
    ArrayList<Moods_dilist> mAlbumListmood;
    private ArrayList<TrackObject> mAlbumList;
    private TrackObject mPlayist;
    private LinearLayout loadingLayout;
    // ArrayList<AlbumLst> dataObjects;
    private int resource;
    private List<Similartrackstripresponse> dataObjects;
    private boolean enabled;
    private boolean isInfiniteLoop;
    private int background;
    private String mpackageName;

    public SimilartrackStrip(Context context, int layoutResourceId, int textViewResourceId,
                             ArrayList<Similartrackstripresponse> objects) {
        super();
        this.context = context;
        this.dataObjects = objects;
        this.resource = layoutResourceId;
        isInfiniteLoop = false;
    }

    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    @Override
    public int getCount() {
        // Infinite loop
        return dataObjects.size();
    }

    // Add viewpager_item.xml to ViewPager

    @Override
    public View getView(final int position, View convertview, ViewGroup container) {

        final ViewHolder holder;

        View view = null;
        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, container, false);

            holder = new ViewHolder();
            holder.progBar = view.findViewById(R.id.progress);
            //holder.pb1 = (ProgressWheel) ViewHolder.progBar.findViewById(R.id.progress_bar_2);
            holder.Image = view.findViewById(R.id.player_image);


            holder.recommtext = view.findViewById(R.id.song_name);
            holder.songname = view.findViewById(R.id.album_name);
            holder.playslider = view.findViewById(R.id.play_imgslider);
            holder.relcontainer = view.findViewById(R.id.relcontainer);



            String language = LanguageHelper.getCurrentLanguage(context);


            if (language.equalsIgnoreCase("ar")) {
                holder.songname.setText(dataObjects.get(position).getTrackArName().toString() + " " + "ل "
                        + dataObjects.get(position).getSingerArName().toString());
                holder.recommtext.setText(dataObjects.get(position).getRecommendedArText().toString());
            }
            if (language.equalsIgnoreCase("en")) {
                holder.songname.setText(dataObjects.get(position).getTrackEnName().toString() + " " + "By "
                        + dataObjects.get(position).getSingerEnName().toString());
                holder.recommtext.setText(dataObjects.get(position).getRecommendedText().toString());
            }
            if (dataObjects.get(position).getAlbumImgPath() != null || dataObjects.get(position).getAlbumImgPath() != "") {

                Picasso.with(context).load((ServerConfig.Image_path + dataObjects.get(position).getAlbumImgPath()))
                        .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(holder.Image);
                if (Boolean.valueOf(dataObjects.get(position).getIsPremium())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        background = context.getResources().getColor(R.color.dimmedcolor, context.getTheme());
                        holder.Image.setColorFilter(background);


                    } else {
                        background = context.getResources().getColor(R.color.dimmedcolor);
                        holder.Image.setColorFilter(background);

                    }
                } else {
                    holder.Image.setColorFilter(null);


                }
            } else {

                Picasso.with(context).load(R.mipmap.defualt_img)
                        .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(holder.Image);
                if (Boolean.valueOf(dataObjects.get(position).getIsPremium())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        background = context.getResources().getColor(R.color.dimmedcolor, context.getTheme());
                        holder.Image.setColorFilter(background);


                    } else {
                        background = context.getResources().getColor(R.color.dimmedcolor);
                        holder.Image.setColorFilter(background);

                    }
                } else {
                    holder.Image.setColorFilter(null);


                }
            }

            holder.playslider.setOnClickListener(v -> {
                holder.playslider.setVisibility(View.GONE);

                GetSingerTracksFrmTrack(dataObjects.get(position).getTrackId());
            });


            holder.relcontainer.setOnClickListener((View v) -> {
                // this will log the page number that was click
                holder.playslider.setVisibility(View.VISIBLE);
                PlayerAcreenActivity.similaradapter.notifyDataSetChanged();

            });
            view.setTag(holder);

        }
            return view;

    }
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public SimilartrackStrip setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    private void GetSingerTracksFrmTrack(String string) {
        if (getActivity() != null) {
            mpackageName = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");


            if (mpackageName
                    .equalsIgnoreCase("PlayerAcreenActivity")) {
                PlayerAcreenActivity.startwhellprogress();

                //
            }
        }


        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSingerTracksFrmTrack + "?trackId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();

        service.GetSingerTracksFrmTrack(fav_url).enqueue(new Callback<Similartrackres>() {

            @Override
            public void onResponse(@NonNull Call<Similartrackres> call, @NonNull Response<Similartrackres> mresult) {
                if (mresult.isSuccessful()) {
                    Similartrackres response = mresult.body();
                    if (response != null) {
                        listoftrack = response;
                    }
                    mAlbumList = new ArrayList<>();
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
                            mPlayist.setIsDownloaded(listoftrack.get(j).getIsDownloaded());

                            mAlbumList.add(mPlayist);

                        }
                    }
                    if (getActivity() != null) {
                        mpackageName = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

                        if (mpackageName
                                .equalsIgnoreCase("PlayerAcreenActivity")) {
                            PlayerConstants.SONGS_LIST = mAlbumList;
                            PlayerAcreenActivity.progBar.setVisibility(View.GONE);

                            PlayerAcreenActivity.updateUI();
                            PlayerAcreenActivity.playsong_click();

                        }
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    PlayerAcreenActivity.progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(Call<Similartrackres> call, Throwable t) {
                PlayerAcreenActivity.progBar.setVisibility(View.GONE);

            }
        });


    }

    public static class ViewHolder {

        public ImageView Image;
        public ImageView playslider;
        public TextView recommtext;
        public TextView songname;
        public RelativeLayout progBar, relcontainer;
        public ProgressWheel pb1;
    }

}
