package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.GetVideoDataFrmIDResponse;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayergalleryAdaptor<T> extends FancyCoverFlowAdapter {
    private static ArrayList<TrackObject> mList;
    private static boolean isInfiniteLoop;
    private LayoutInflater inflater;
    View view;
    private ArrayList<VideoObjectResponse> listofvideos;
    private VideoObjectResponse mvideolistobject;
    private Context mContext;
    private ArrayList<VideoObjectResponse> mVideoslist;
    private int background;

    public PlayergalleryAdaptor(Context context, int resource, int textViewResourceId, ArrayList<TrackObject> objects) {
        super();
        this.mContext = context;
        mList = objects;
        Log.e("testAdaptor", "here");
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isInfiniteLoop = false;
    }

    private static int getPosition(int position) {
        return isInfiniteLoop ? position % mList.size() : position;
    }

    @Override
    public View getCoverFlowItem(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder1 holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.playerpageradapter, parent, false);
            holder = new ViewHolder1();

            holder.imageItem = view.findViewById(R.id.Gallery01);
            holder.ivvideoicon = view.findViewById(R.id.ivvideoicon);

            holder.songname = view.findViewById(R.id.song_title);

            holder.albumname = view.findViewById(R.id.album_title);
            holder.likescount = view.findViewById(R.id.likescount);
            holder.listencount = view.findViewById(R.id.listencount);

        TrackObject item = mList.get(getPosition(position));

        Log.e("imagepath on server", ServerConfig.ALBUM_IMAGE_PATH + item.getTrackImage());
        if (item.getTrackImage() != null || item.getTrackImage() != "") {
            Picasso.with(mContext).load((ServerConfig.ALBUM_IMAGE_PATH + item.getTrackImage())).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
            if (Boolean.valueOf(item.getIsPremium())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    background = mContext.getResources().getColor(R.color.dimmedcolor, mContext.getTheme());
                    holder.imageItem.setColorFilter(background);


                } else {
                    background = mContext.getResources().getColor(R.color.dimmedcolor);
                    holder.imageItem.setColorFilter(background);

                }
            } else {
                holder.imageItem.setColorFilter(null);


            }
        } else {
            Picasso.with(mContext).load(R.mipmap.defualt_img).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
            if (Boolean.valueOf(item.getIsPremium())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    background = mContext.getResources().getColor(R.color.dimmedcolor, mContext.getTheme());
                    holder.imageItem.setColorFilter(background);


                } else {
                    background = mContext.getResources().getColor(R.color.dimmedcolor);
                    holder.imageItem.setColorFilter(background);

                }
            } else {
                holder.imageItem.setColorFilter(null);


            }
        }
        String language = LanguageHelper.getCurrentLanguage(mContext);
        if (language.equalsIgnoreCase("ar")) {
            holder.songname.setText(item.getTrackArName());
            holder.albumname.setText(item.getAlbumArName());
            holder.likescount.setText(item.getLikesCount() + "  ");
            holder.listencount.setText(item.getListenCount());

        }

        if (language.equalsIgnoreCase("en")) {
            holder.songname.setText(item.getTrackEnName());
            holder.albumname.setText(item.getAlbumEnName());
            holder.likescount.setText(item.getLikesCount());
            holder.listencount.setText(item.getListenCount());

        }

        if (!item.getVideoID().equalsIgnoreCase("0")) {
            holder.ivvideoicon.setVisibility(View.VISIBLE);
        } else {
            holder.ivvideoicon.setVisibility(View.GONE);

        }

        holder.ivvideoicon.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            GetSingerVideoFrmTrack(mList.get(getPosition(position)).getVideoID());

        });
            view.setTag(holder);
        }
        return view;

    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : mList.size();
    }

    /**
     * get really position
     *
     * @param
     * @return
     */

    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public PlayergalleryAdaptor<T> setInfiniteLoop(boolean isInfiniteLoop) {
        PlayergalleryAdaptor.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private void GetSingerVideoFrmTrack(String string) {
        HomeActivity.startwhellprogress();
        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetVideoDataFrmID + "?videoClipId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(mContext, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(mContext, Constants.accesstoken);
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
                    PlayerAcreenActivity.progBar.setVisibility(View.GONE);
                } else {
                    ApiUtils.handelErrorCode(mContext, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(Call<GetVideoDataFrmIDResponse> call, Throwable t) {
                PlayerAcreenActivity.progBar.setVisibility(View.GONE);

            }
        });


    }

    private void SetVideosPlayer(ArrayList<VideoObjectResponse> listofvideos) {
        if (listofvideos.size() > 0) {

            mVideoslist = new ArrayList<>();
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
            if (Constants.isNetworkOnline(mContext)) {

                Intent player = new Intent(mContext, VideoPlayerActivity.class);

                player.putExtra("track", PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER));

                PlayerConstants.SONG_NUMBER = 0;
                player.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(player);
                ((Activity) mContext).finish();


            } else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public static class ViewHolder1 {

        public ImageView imageItem, ivvideoicon;
        TextView songname;
        TextView albumname;
        TextView likescount;
        TextView listencount;

    }
}
