package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayListTracksActivity;
import com.binarywaves.ghaneely.ghannelyactivites.VideoPlayerActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.GetVideoDataFrmIDResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackres;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackstripresponse;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoard_pagerAdapter extends RecyclingPagerAdapter {
    public static Activity activity;
    // Declare Variables
    private Context context;
    TextView tvtitle;
    TextView Descreption;
    Button play;
    ProgressDialog dialog;
    Resources res;
    private LayoutInflater inflater;
    private ArrayList<Similartrackstripresponse> listoftrack;
    private ArrayList<VideoObjectResponse> listofvideos;
    private int background;

    private TrackObject mPlayist;
    private VideoObjectResponse mvideolistobject;

    private LinearLayout loadingLayout;
    // ArrayList<AlbumLst> dataObjects;
    private int resource;
    private List<SlideAlbumObject> dataObjects;
    private boolean enabled;
    private boolean isInfiniteLoop;
    private ArrayList<TrackObject> mAlbumList;
    private ArrayList<VideoObjectResponse> mVideoslist;

    public DashBoard_pagerAdapter(Context context, int resource, int textViewResourceId,
                                  ArrayList<SlideAlbumObject> objects) {
        super();
        this.context = context;
        this.dataObjects = objects;
        this.resource = resource;
        isInfiniteLoop = false;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : dataObjects.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % dataObjects.size() : position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup container) {
        final ViewHolder holder;

        view = null;
        ViewGroup nullparent = null;

        if (view == null) {
            holder = new ViewHolder();


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.slide_item_row, container, false);

            holder.Image = view.findViewById(R.id.slide_image_view);
            String language = LanguageHelper.getCurrentLanguage(context);

            if (language.equalsIgnoreCase("ar")) {
                Picasso.with(context)
                        .load((ServerConfig.Image_path + dataObjects.get(getPosition(position))
                                .getAlbumARImgPath())).error(R.mipmap.defaultadsimage)
                        .placeholder(R.mipmap.defaultadsimage).into(holder.Image);
                if (Boolean.valueOf(dataObjects.get(getPosition(position)).getIsPremium())) {
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
            if (language.equalsIgnoreCase("en")) {
                Picasso.with(context)
                        .load((ServerConfig.Image_path + dataObjects.get(getPosition(position))
                                .getAlbumImgPath())).error(R.mipmap.defaultadsimage)
                        .placeholder(R.mipmap.defaultadsimage).into(holder.Image);
                if (Boolean.valueOf(dataObjects.get(getPosition(position)).getIsPremium())) {
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

            view.setOnClickListener(v -> {
                //Album Type
                if (HomeActivity.mSlideList.get(getPosition(position)).getSingerEnName().equalsIgnoreCase("1")) {
                    Intent albumIntent = new Intent(context,
                            AlbumTabsActivity.class);
                    albumIntent.putExtra("album", HomeActivity.mSlideList.get(getPosition(position)));
                    context.startActivity(albumIntent);
                }

                //Tarcks type
                if (HomeActivity.mSlideList.get(getPosition(position)).getSingerEnName().equalsIgnoreCase("2")) {
                    GetSingerTracksFrmTrack(HomeActivity.mSlideList.get(getPosition(position)).getAlbumId());
                }
                // Playlist Type
                if (HomeActivity.mSlideList.get(getPosition(position)).getSingerEnName().equalsIgnoreCase("3")) {
                    Intent albumIntent = new Intent(context,
                            PlayListTracksActivity.class);
                    albumIntent.putExtra("id", HomeActivity.mSlideList.get(getPosition(position)).getAlbumId());
                    context.startActivity(albumIntent);
                }
                //Video Type
                if (HomeActivity.mSlideList.get(getPosition(position)).getSingerEnName().equalsIgnoreCase("4")) {
                    GetSingerVideoFrmTrack(HomeActivity.mSlideList.get(getPosition(position)).getAlbumId());


                }


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
    public DashBoard_pagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    private void StopService() {
        // TODO Auto-generated method stub


        TrackLisinterService tracklisiten = new TrackLisinterService(context, ((Activity) context).getApplication());
        tracklisiten.StopService();

    }

    private void GetSingerVideoFrmTrack(String string) {
        HomeActivity.startwhellprogress();
        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetVideoDataFrmID + "?videoClipId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.GetVideoDataFrmID(fav_url).enqueue(new Callback<GetVideoDataFrmIDResponse>() {

            @Override
            public void onResponse(Call<GetVideoDataFrmIDResponse> call, Response<GetVideoDataFrmIDResponse> mresult) {
                if (mresult.isSuccessful()) {
                    GetVideoDataFrmIDResponse response = mresult.body();
                    if (response != null) {
                        listofvideos = response;
                        SetVideosPlayer(listofvideos);
                    }
                    HomeActivity.progBar.setVisibility(View.GONE);
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(Call<GetVideoDataFrmIDResponse> call, Throwable t) {
                HomeActivity.progBar.setVisibility(View.GONE);

            }
        });


    }


    private void GetSingerTracksFrmTrack(String string) {
        HomeActivity.startwhellprogress();
        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSingerTracksFrmTrack + "?trackId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.GetSingerTracksFrmTrack(fav_url).enqueue(new Callback<Similartrackres>() {

            @Override
            public void onResponse(Call<Similartrackres> call, Response<Similartrackres> mresult) {
                if (mresult.isSuccessful()) {
                    Similartrackres response = mresult.body();
                    if (response != null) {
                        listoftrack = response;
                        SetSimilartrackres();
                    }
                    HomeActivity.progBar.setVisibility(View.GONE);
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(Call<Similartrackres> call, Throwable t) {
                HomeActivity.progBar.setVisibility(View.GONE);

            }
        });


    }

    private void SetSimilartrackres() {
        if (listoftrack.size() > 0) {

            mAlbumList = new ArrayList<>();
            // Albumconstant.SONGS_LIST = mAlbumList;
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
                mPlayist.setLikesCount(listoftrack.get(j).getLikesCount());
                mPlayist.setListenCount(listoftrack.get(j).getListenCount());
                mPlayist.setLyricFile(listoftrack.get(j).getLyricFile());
                mPlayist.setHasLyrics(listoftrack.get(j).getHasLyrics());
                mPlayist.setTrackLength(listoftrack.get(j).getTrackLength());
                mPlayist.setVideoID(listoftrack.get(j).getVideoID());
                mPlayist.setIsPremium(listoftrack.get(j).getIsPremium());
                mPlayist.setIsDownloaded(listoftrack.get(j).getIsDownloaded());

                mAlbumList.add(mPlayist);

            }

            PlayerConstants.SONGS_LIST = mAlbumList;
            PlayerConstants.SONG_NUMBER = 0;

                // audioRelative.setVisibility(View.VISIBLE);
                StopService();

                Log.e("list clicked ", "here");
                // streamService = new Intent(MainActivity.this,
                // StreamService.class);
                // startService(streamService);
                HomeActivity.startwhellprogress();
                HomeActivity.audioRelative.setEnabled(false);

                // intiPlayer(mtrackList.get(position).getTrackPath(),false);
                Log.e("list clicked ", "here");
                String trackid = mAlbumList.get(PlayerConstants.SONG_NUMBER).getTrackId();
                String favo = mAlbumList.get(PlayerConstants.SONG_NUMBER).getIsFavourite();
                if (favo.equalsIgnoreCase("true")) {
                    // change icon
                    // make button disabled
                    HomeActivity.Like.setVisibility(View.GONE);

                    HomeActivity.dislike.setVisibility(View.VISIBLE);
                }

                if (favo.equalsIgnoreCase("false")) {
                    HomeActivity.dislike.setVisibility(View.GONE);

                    HomeActivity.Like.setVisibility(View.VISIBLE);
                    // make button enable

                }
                // linearplayer.setVisibility(View.VISIBLE);
                PlayerConstants.SONG_PAUSED = false;
                boolean isServiceRunning = UtilFunctions.isServiceRunning(SongService.class.getName(),
                        context);
                if (!isServiceRunning) {
                    Intent i = new Intent(context, SongService.class);
                    context.startService(i);
                    // progBar.setVisibility(View.GONE);

                    if (HomeActivity.ListTrackadAdaptor!=null) {
                        HomeActivity.selectessong = -1;
                        HomeActivity.selecttopsong = -1;
                        HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();}
                    if (HomeActivity.topTwentyAdaptor!=null) {
                        HomeActivity.selectessong = -1;
                        HomeActivity.selecttopsong = -1;

                        HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
                    }
                } else {
                    PlayerConstants.SONG_CHANGE_HANDLER
                            .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
                }

                HomeActivity.updateUI();

                HomeActivity.changeButton();


                Log.d("TAG", "TAG Tapped INOUT(OUT)");


        }

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

                Intent player = new Intent(context, VideoPlayerActivity.class);
                player.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                player.putExtra("track", PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER));

                context.startActivity(player);


        }
    }

    public static class ViewHolder {

        public ImageView Image;

    }
}