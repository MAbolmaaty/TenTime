package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdaptor extends ArrayAdapter<SlideAlbumObject> {
    private int layoutResourceId;
    private int hint;
    private ArrayList<SlideAlbumObject> data = new ArrayList<>();
    private Context mContext;

    public GalleryAdaptor(Context context, int layoutResourceId,
                          ArrayList<SlideAlbumObject> data, int hint) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        this.hint = hint;
        Log.i("category size in image", data.size() + "");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {

            Log.i("NULL VIEW", "here");
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = row.findViewById(R.id.item_text);
            holder.imageItem = row.findViewById(R.id.item_image);


            row.setTag(holder);
        }
        Log.i("Not NULL VIEW", "here");
        holder = (RecordHolder) row.getTag();
        if (position >= data.size()) {
            position = position % data.size();
            //  position=0;

        } else {


        }
        SlideAlbumObject mplaylist = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);

        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mplaylist.getAlbumArName());

        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mplaylist.getAlbumEnName());

        }
        switch (hint) {
            case 1:
//			imageLoader.DisplayImage(ServerConfig.Image_path + mplaylist.getAlbumImgPath() ,holder.imageItem);
//			Log.e("imagepath is ",ServerConfig.Image_path + mplaylist.getAlbumImgPath());
                Picasso.with(mContext)
                        .load((ServerConfig.Image_path + mplaylist.getAlbumImgPath())).error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(holder.imageItem);


                break;
            case 2:
                Picasso.with(mContext)
                        .load((ServerConfig.Image_path + mplaylist.getAlbumImgPath())).error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
                break;
            default:
                break;
        }
        //imageLoader.DisplayImage(ServerConfig.Image_path + mplaylist.getAlbumImgPath() ,holder.imageItem);

        //


        return row;


    }

    @Override
    public int getCount() {
        // Infinite loop
        return data.size();
    }

    static class RecordHolder {
        public TextView tvsongname;
        TextView txtTitle, albumTitle, txttime, txtSong, tvsingerName, AlbumGridAdapter;
        ImageView imageItem, check_image, imageViewIcon, ivoverlay, ivvideoicon, ivdelete,ivplay;
        FrameLayout framecon;
        public RelativeLayout controller;
    }
}
