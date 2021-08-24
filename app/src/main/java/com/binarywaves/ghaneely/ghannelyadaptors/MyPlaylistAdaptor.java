package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyresponse.Playlistlst;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistnotificationResponse;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class MyPlaylistAdaptor extends ArrayAdapter<Playlistlst> implements
        Swappable, UndoAdapter, OnDismissCallback, OnClickListener {
    private ArrayList<Playlistlst> mComments;
    private ArrayList<PlaylistnotificationResponse> playlistnotificationResponse;
    private Context mContext;


    public MyPlaylistAdaptor(Context context, int resource,
                             ArrayList<Playlistlst> objects, ArrayList<PlaylistnotificationResponse> objects2) {
        super(context, resource, objects);
        mContext = context;
        mComments = objects;
        playlistnotificationResponse = objects2;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        if (mComments != null)
            return mComments.size();
        else
            return playlistnotificationResponse.size();
    }

    @Override
    public Playlistlst getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mComments != null)
            return Long.parseLong(mComments.get(position).getPlaylistId());
        else
            return Long.parseLong(playlistnotificationResponse.get(position).getPlaylistId());

    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(mComments, positionOne, positionTwo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.myplaylist_item_list, parent, false);
        TextView name = row.findViewById(R.id.tvsongName);
        ImageView icon = row.findViewById(R.id.imagesingerIcon);

        if (mComments != null) {
            name.setText(mComments.get(position).getPlaylistName());
            Picasso.with(mContext).load((ServerConfig.PLAYLIST_IMAGE_PATH + mComments.get(position).getPlaylistImgPath())).error(R.mipmap.defualt_img)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.defualt_img).into(icon);
        }

        if (playlistnotificationResponse != null) {
            String language = LanguageHelper.getCurrentLanguage(mContext);


            if (language.equalsIgnoreCase("ar")) {
                name.setText(playlistnotificationResponse.get(position).getPlaylistArName());

            }
            if (language.equalsIgnoreCase("en")) {
                name.setText(playlistnotificationResponse.get(position).getPlaylistName());

            }
            Picasso.with(mContext).load((ServerConfig.PLAYLIST_IMAGE_PATH + playlistnotificationResponse.get(position).getPlaylistImgPath())).error(R.mipmap.defualt_img)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.defualt_img).into(icon);
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

    public void remove(int position) {
        if (mComments != null) {
            mComments.remove(position);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}


