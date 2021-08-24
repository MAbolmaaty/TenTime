package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.GIFDemo;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_AllActivity;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyresponse.GetRecentUserSearchesListResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amany on 07-Oct-17.
 */

public class SearchResultAdapter extends ArrayAdapter<GetRecentUserSearchesListResponse> {
    private int layoutResourceId;
    // public ImageLoader imageLoader;
    private List<GetRecentUserSearchesListResponse> data;
    private Context mContext;

    public SearchResultAdapter(Context context, int layoutResourceId,
                               List<GetRecentUserSearchesListResponse> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        // imageLoader = new ImageLoader(context);
        Log.i("category size in ", data.size() + "");
    }

    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
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
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = convertView;
        GalleryAdaptor.RecordHolder holder = null;
        if (row == null) {

            Log.i("NULL VIEW", "here");
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GalleryAdaptor.RecordHolder();
            holder.txtTitle = row.findViewById(R.id.tvsongName);
            holder.imageItem = row
                    .findViewById(R.id.imagesingerIcon);
            holder.ivoverlay = row
                    .findViewById(R.id.ivoverlay);
            holder.framecon = row
                    .findViewById(R.id.framecon);
            row.setTag(holder);
        }

        Log.i("Not NULL VIEW", "here");
        holder = (GalleryAdaptor.RecordHolder) row.getTag();
        GetRecentUserSearchesListResponse mtTrack = data.get(position);

        String language = LanguageHelper.getCurrentLanguage(mContext);
        ImageView ivplay = row
                .findViewById(R.id.ivplay);
        GIFDemo gifimage = row.findViewById(R.id.gifimage);
        gifimage.setImageResource(R.mipmap.equalizer);
        String mpackageName;
        if (getActivity() != null) {
            mpackageName = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackageName
                    .equalsIgnoreCase("SearchTabActivity")) {
                switch (SearchTabActivity.selectedfragment) {
                    case 0:

                        if (SearchTab_AllActivity.selectessongsearch == position) {
                            ivplay.setVisibility(View.GONE);
                            gifimage.setVisibility(View.VISIBLE);


                        } else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }
                        break;

                }
            }


            if (language.equalsIgnoreCase("ar")) {
                holder.txtTitle.setText(mtTrack.getContentArName());
            }
            if (language.equalsIgnoreCase("en")) {
                holder.txtTitle.setText(mtTrack.getContentEnName());
            }

            if (mtTrack.getContentImage() == null || mtTrack.getContentImage() == "") {


                Picasso.with(mContext).load(R.mipmap.defualt_img)
                        .error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(holder.imageItem);


            } else {
                switch (Integer.valueOf(mtTrack.getContentTypeId())) {
                    //singer
                    case 1:
                        Picasso.with(mContext)
                                .load((ServerConfig.Image_pathSinger + mtTrack.getContentImage()))
                                .error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
                        holder.framecon.setVisibility(View.GONE);
                        break;

                    //album
                    case 2:
                        Picasso.with(mContext)
                                .load((ServerConfig.Image_path + mtTrack.getContentImage()))
                                .error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
                        Picasso.with(mContext)
                                .load(R.mipmap.albumsearch)
                                .error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(holder.ivoverlay);
                        break;

                    //track
                    case 3:
                        Picasso.with(mContext)
                                .load((ServerConfig.Image_path + mtTrack.getContentImage()))
                                .error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
                        Picasso.with(mContext)
                                .load(R.mipmap.songsearch)
                                .error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(holder.ivoverlay);
                        break;

                    //playlist
                    case 4:
                        Picasso.with(mContext)
                                .load((ServerConfig.PLAYLIST_IMAGE_PATH + mtTrack.getContentImage()))
                                .error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
                        Picasso.with(mContext)
                                .load(R.mipmap.playlistsearch)
                                .error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(holder.ivoverlay);
                        break;
                }
            }


        }
        return row;
    }
}
