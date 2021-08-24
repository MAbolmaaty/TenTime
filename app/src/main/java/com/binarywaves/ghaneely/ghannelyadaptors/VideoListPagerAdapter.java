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
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.VideoPlayerActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amany on 02-Jul-17.
 */

public class VideoListPagerAdapter extends RecyclingPagerAdapter {
    public static Activity activity;
    // Declare Variables
    Context context;
    TextView tvtitle;
    TextView Descreption;
    Button play;
    ProgressDialog dialog;
    Resources res;
    LayoutInflater inflater;
    TrackObject mPlayist;
    private LinearLayout loadingLayout;
    // ArrayList<AlbumLst> dataObjects;
    private int resource;
    private List<VideoObjectResponse> dataObjects;
    private boolean enabled;
    private boolean isInfiniteLoop;
    private int background;

    public VideoListPagerAdapter(Context context, int resource, int textViewResourceId,
                                 ArrayList<VideoObjectResponse> objects) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custumvideopager, parent, false);
            holder = new ViewHolder();

            holder.Image = view.findViewById(R.id.ivposter);
            holder.tvsingername = view.findViewById(R.id.tvsingername);
            holder.tvsongname = view.findViewById(R.id.tvsongname);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();

        String language = LanguageHelper.getCurrentLanguage(context);

        if (language.equalsIgnoreCase("ar")) {
            holder.tvsingername.setText(dataObjects.get(getPosition(position)).getVideoArName());
            holder.tvsongname.setText(dataObjects.get(getPosition(position)).getSingerArName());

        }
        if (language.equalsIgnoreCase("en")) {
            holder.tvsingername.setText(dataObjects.get(getPosition(position)).getVideoEnName());
            holder.tvsongname.setText(dataObjects.get(getPosition(position)).getSingerEnName());


        }
        Log.e("imagevideo", ServerConfig.Video_Imagepath + dataObjects.get(getPosition(position))
                .getVideoPoster());
        Picasso.with(context)
                .load((ServerConfig.Video_Imagepath + dataObjects.get(getPosition(position))
                        .getVideoPoster())).error(R.mipmap.defaultadsimage)
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
        view.setOnClickListener(v -> {

            Intent player = new Intent(context, VideoPlayerActivity.class);
            PlayerConstants.SONGS_LISTVideo = HomeActivity.mtopVideoLst;
            if (PlayerConstants.SONGS_LISTVideo != null && PlayerConstants.SONGS_LISTVideo.size() > 0) {
                player.putExtra("track", dataObjects.get(getPosition(position)));
                PlayerConstants.SONG_NUMBER = getPosition(position);
                context.startActivity(player);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();
            }

        });


        return view;
    }

    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public VideoListPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    public void StopService() {
        // TODO Auto-generated method stub


        TrackLisinterService tracklisiten = new TrackLisinterService(context, ((Activity) context).getApplication());
        tracklisiten.StopService();

    }


    public static class ViewHolder {

        public ImageView Image;
        public TextView tvsingername, tvsongname;

    }


}