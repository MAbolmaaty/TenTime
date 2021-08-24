package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class Personaldjlist_Adapter extends ArrayAdapter<TrackObject> {
    private int layoutResourceId;
    // public ImageLoader imageLoader;
    private ArrayList<TrackObject> data = new ArrayList<>();
    private Context mContext;
    private int background;

    public Personaldjlist_Adapter(Context context, int layoutResourceId, ArrayList<TrackObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        // imageLoader = new ImageLoader(context);
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
            holder.txtTitle = row.findViewById(R.id.textViewName);
            holder.imageItem = row.findViewById(R.id.imagesingerIcon);
            holder.txttime = row.findViewById(R.id.tvtime);


            row.setTag(holder);
        }

        Log.i("Not NULL VIEW", "here");
        holder = (GalleryAdaptor.RecordHolder) row.getTag();
        TrackObject mtTrack = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mtTrack.getTrackArName());

        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mtTrack.getTrackEnName());

        }
        holder.txttime.setText(getDurationString(Double.parseDouble(mtTrack.getTrackLength())));

        if (mtTrack.getTrackImage() != null || mtTrack.getTrackImage() != "") {
            Picasso.with(mContext).load((ServerConfig.Image_path + mtTrack.getTrackImage())).error(R.mipmap.defualt_img)
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
            Picasso.with(mContext).load(R.mipmap.defualt_img).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
        }
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
        return row;
    }

    private String getDurationString(Double seconds) {
        String str = "";
        String strsec = "";

        Double hours = seconds / 3600;
        Double minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        str = Double.valueOf(minutes).toString().substring(0, Double.toString(minutes).indexOf('.'));
        double v = Double.valueOf(str);

        strsec = Double.valueOf(seconds).toString().substring(0, Double.toString(seconds).indexOf('.'));
        double vstrsec = Double.valueOf(strsec);
        return twoDigitString((int) v) + ":" + twoDigitString((int) vstrsec);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

}
