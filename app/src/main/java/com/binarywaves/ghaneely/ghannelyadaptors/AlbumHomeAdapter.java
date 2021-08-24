package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
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
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amany on 19-Jun-17.
 */

public class AlbumHomeAdapter extends RecyclerView.Adapter<AlbumHomeAdapter.ViewHolder>  {
    private int layoutResourceId;
    private int hint;
    private ArrayList<SlideAlbumObject> data = new ArrayList<>();
    private Context mContext;

    public AlbumHomeAdapter(Context context, int layoutResourceId,
                            ArrayList<SlideAlbumObject> data, int hint) {
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.hint = hint;
        Log.i("category size in image", data.size() + "");
    }
    @Override
    public AlbumHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);
        return new AlbumHomeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlbumHomeAdapter.ViewHolder holder, int position) {
        if (position >= data.size()) {
            position = position % data.size();
            //  position=0;

        } else {


        }
        SlideAlbumObject mplaylist = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);

        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mplaylist.getSingerArName());
            holder.txtSong.setText(mplaylist.getAlbumArName());

        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mplaylist.getSingerEnName());
            holder.txtSong.setText(mplaylist.getAlbumEnName());


        }
        holder.gifimage.setImageResource(R.mipmap.equalizer);

        switch (hint) {
            case 1:
//			imageLoader.DisplayImage(ServerConfig.Image_path + mplaylist.getAlbumImgPath() ,holder.imageItem);
//			Log.e("imagepath is ",ServerConfig.Image_path + mplaylist.getAlbumImgPath());
                Picasso.with(mContext)
                        .load((ServerConfig.Image_path + mplaylist.getAlbumImgPath())).error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(holder.imageItem);


                break;
            case 2:
                Log.i("albumhomeimage", ServerConfig.Image_path + mplaylist.getAlbumImgPath());
                Picasso.with(mContext)
                        .load((ServerConfig.Image_path + mplaylist.getAlbumImgPath())).error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
                break;
            default:
                break;
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
