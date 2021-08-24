package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SliderAds;
import com.jakewharton.salvage.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdsAdapter extends RecyclingPagerAdapter {
    public static Activity activity;
    // Declare Variables
    private Context context;
    TextView tvtitle;
    TextView Descreption;
    Button play;
    ProgressDialog dialog;
    Resources res;
    private LayoutInflater inflater;
    private LinearLayout loadingLayout;
    // ArrayList<AlbumLst> dataObjects;
    private int resource;
    private List<SliderAds> dataObjects;
    private boolean enabled;
    private boolean isInfiniteLoop;

    public SliderAdsAdapter(Context context, int resource, int textViewResourceId, ArrayList<SliderAds> objects) {
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
        return isInfiniteLoop ? Integer.MAX_VALUE : 50;
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

            // final SlideAlbumObject Item = dataObjects.get(position);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sliderads_row, container, false);

            holder.Image = view.findViewById(R.id.topic_image);
            dataObjects.get(getPosition(position)).getAdsFileName();
            String imgpath = ServerConfig.Image_pathads + dataObjects.get(getPosition(position)).getAdsFileName();
            String final_imgpath = imgpath.replaceAll(" ", "%20");
            Picasso.with(context).load((final_imgpath)).error(R.mipmap.defaultadsimage)
                    .placeholder(R.mipmap.defaultadsimage).into(holder.Image);

            view.setOnClickListener(v -> {
                Constants.Adsclick(dataObjects.get(getPosition(position)).getAdsFileID(), context);

                String language = LanguageHelper.getCurrentLanguage(context);

                if (dataObjects.get(getPosition(position)).getAdsURLAr() != "") {
                    if (language.equalsIgnoreCase("ar")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(dataObjects.get(getPosition(position)).getAdsURLAr()));
                        context.startActivity(browserIntent);
                    }
                    if (language.equalsIgnoreCase("en")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(dataObjects.get(getPosition(position)).getAdsURL()));
                        context.startActivity(browserIntent);
                    }

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
    public SliderAdsAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    public static class ViewHolder {

        public ImageView Image;

    }


}
