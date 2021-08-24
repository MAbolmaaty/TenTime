package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.AddToActivity;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class TrackGalleryAdaptor extends ArrayAdapter<TrackObject> implements
        Swappable, UndoAdapter, OnDismissCallback, OnClickListener {

    private int layoutResourceId;
    //	public ImageLoader imageLoader;
    int hint;
    private ArrayList<TrackObject> data = new ArrayList<>();
    private Context mContext;
    private int background;

    public TrackGalleryAdaptor(Context context, int layoutResourceId,
                               ArrayList<TrackObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        //	imageLoader = new ImageLoader(context);
        Log.i("category size in imagea", data.size() + "");
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TrackObject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(data.get(position).getTrackId());
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(data, positionOne, positionTwo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = convertView;
        GalleryAdaptor.RecordHolder holder = null;
        if (row == null) {

            Log.i("NULL VIEW", "here");
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GalleryAdaptor.RecordHolder();
            holder.txtTitle = row.findViewById(R.id.item_text);
            holder.imageItem = row.findViewById(R.id.item_image);
            holder.ivvideoicon = row.findViewById(R.id.ivvideoicon);

            row.setTag(holder);
        }
        Log.i("Not NULL VIEW", "here");
        holder = (GalleryAdaptor.RecordHolder) row.getTag();
        if (position >= data.size()) {
            position = position % data.size();
            //  position=0;

        }
        final TrackObject mTrack = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mTrack.getTrackArName());
        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mTrack.getTrackEnName());
        }


        if (mTrack.getTrackImage() != null || mTrack.getTrackImage() != "") {
            Picasso.with(mContext)
                    .load((ServerConfig.ALBUM_IMAGE_PATH + mTrack.getTrackImage())).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
            if (Boolean.valueOf(mTrack.getIsPremium())) {
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
            Picasso.with(mContext)
                    .load(R.mipmap.defualt_img).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
            if (Boolean.valueOf(mTrack.getIsPremium())) {
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
        holder.imageViewIcon = row.findViewById(R.id.imageViewIcon);

        holder.imageViewIcon.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent img = new Intent(mContext, AddToActivity.class);

            Log.e("Selected gallarey item", mTrack.getSingerEnName() + "ff");
            img.putExtra("trask", mTrack);
            img.putExtra("FromPlayer", "No");
            mContext.startActivity(img);
        });

        if (!mTrack.getVideoID().equalsIgnoreCase("0")) {
            holder.ivvideoicon.setVisibility(View.VISIBLE);
        } else {
            holder.ivvideoicon.setVisibility(View.GONE);

        }


        return row;
    }

    @Override
    @NonNull
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undo_button_media);
    }

    @Override
    @NonNull
    public View getUndoView(final int position, final View convertView,
                            @NonNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item_undo_media, parent, false);
        }
        return view;
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView,
                          @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            remove(position);
        }
    }

    private void remove(int position) {
        data.remove(position);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
