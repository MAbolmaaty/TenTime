package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.GIFDemo;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrackListAdaptor extends RecyclerView.Adapter<TrackListAdaptor.ViewHolder> {

    int layoutResourceId;
    ArrayList<TrackObject> data = new ArrayList<>();
    private Context mContext;
    private int background;

    public TrackListAdaptor(Context context, int layoutResourceId,
                            ArrayList<TrackObject> data) {
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        Log.i("category size in imag", data.size() + "");
    }

    @Override
    public TrackListAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);
        return new TrackListAdaptor.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackListAdaptor.ViewHolder holder, int position) {
        TrackObject mtTrack = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);

        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mtTrack.getSingerArName());
            holder.txtSong.setText(mtTrack.getTrackArName());

        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mtTrack.getSingerEnName());
            holder.txtSong.setText(mtTrack.getTrackEnName());


        }
        holder.gifimage.setImageResource(R.mipmap.equalizer);

        if (mtTrack.getTrackImage() != null || !mtTrack.getTrackImage().equalsIgnoreCase("")) {
            Picasso.with(mContext)
                    .load((ServerConfig.Image_path + mtTrack.getTrackImage())).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
            if (Boolean.valueOf(mtTrack.getIsPremium())) {
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
            if (Boolean.valueOf(mtTrack.getIsPremium())) {
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


        if (!mtTrack.getVideoID().equalsIgnoreCase("0")) {
            holder.ivvideoicon.setVisibility(View.VISIBLE);
        } else {
            holder.ivvideoicon.setVisibility(View.GONE);

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout controller;
        TextView txtTitle;
        TextView txttime;
        TextView txtSong;

        ImageView imageItem,  ivvideoicon, ivplay;
        GIFDemo gifimage;

        ViewHolder(View row) {
            super(row);
            txtTitle = row.findViewById(R.id.tvsingername);
            txtSong = row.findViewById(R.id.tvsongname);

            imageItem = row.findViewById(R.id.item_image);
            ivvideoicon = row.findViewById(R.id.ivvideoicon);
            ivplay = row
                    .findViewById(R.id.ivplay);
            gifimage = row.findViewById(R.id.gifimage);


        }
    }

}
