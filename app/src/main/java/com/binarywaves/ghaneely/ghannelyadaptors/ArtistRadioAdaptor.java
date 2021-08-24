package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistRadioAdaptor extends ArrayAdapter<ArtistRadio> {

    private int layoutResourceId;
    private ArrayList<ArtistRadio> data = new ArrayList<>();
    private Context mContext;

    public ArtistRadioAdaptor(Context context, int layoutResourceId,
                              ArrayList<ArtistRadio> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        Log.i("category size in imagea", data.size() + "");
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
            // holder.albumTitle = (TextView) row.findViewById(R.id.item_text1);
            holder.imageItem = row.findViewById(R.id.item_image);

            row.setTag(holder);
        }
        Log.i("Not NULL VIEW", "here");
        holder = (GalleryAdaptor.RecordHolder) row.getTag();
        if (position >= data.size()) {
            position = position % data.size();
            // position=0;

        } else {

        }
        ArtistRadio mtTrack = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mtTrack.getSingerArName());

        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mtTrack.getSingerEnName());

        }
        String imgpath = ServerConfig.Image_pathSinger + mtTrack
                .getSingerImgPath();
        if (mtTrack.getSingerImgPath() != null || mtTrack.getSingerImgPath() != "") {
            String final_imgpath = imgpath.replaceAll(" ", "%20");

            Picasso.with(mContext).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);

        } else {
            Picasso.with(mContext).load(R.mipmap.defualt_img).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);

        }
        return row;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return data.size();
    }
}
