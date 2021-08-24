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
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.GIFDemo;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amany on 25-Dec-17.
 */

public class VideoSearchAdapter extends ArrayAdapter<VideoObjectResponse> {
    private final int layoutResourceId;
    // public ImageLoader imageLoader;
    private final List<VideoObjectResponse> data;
    private final Context mContext;
    int selectedfav;
    private GIFDemo gifimage;
    private int background;

    public VideoSearchAdapter(Context context, int layoutResourceId,
                               List<VideoObjectResponse> data) {
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
    public VideoObjectResponse getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return Long.parseLong(data.get(position).getVideoID());
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        View row = convertView;
        GalleryAdaptor.RecordHolder holder = null;
        if (row == null) {

            Log.i("NULL VIEW", "here");
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new GalleryAdaptor.RecordHolder();
            holder.txtTitle = row.findViewById(R.id.tvsongName);
            holder.txttime = row.findViewById(R.id.tvtime);
            holder.imageItem = row
                    .findViewById(R.id.imagesingerIcon);
            holder.tvsingerName = row.findViewById(R.id.tvsingerName);
            holder.imageViewIcon = row
                    .findViewById(R.id.ivadd);
            holder. controller = row
                    .findViewById(R.id.controller);
            holder. controller.setVisibility(View.GONE);
            holder. imageViewIcon.setVisibility(View.GONE);
            holder. ivplay = row
                    .findViewById(R.id.ivplay);
            holder. ivplay.setVisibility(View.GONE);
            row.setTag(holder);
        }
        Log.i("Not NULL VIEW", "here");
        holder = (GalleryAdaptor.RecordHolder) row.getTag();
        VideoObjectResponse mtTrack = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);
        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mtTrack.getVideoArName());
            holder.tvsingerName.setText(mtTrack.getSingerArName());


        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mtTrack.getVideoEnName());
            holder.tvsingerName.setText(mtTrack.getSingerEnName());


        }
        if (mtTrack.getVideoPoster() != null || mtTrack.getVideoPoster() != "") {

            Picasso.with(mContext)
                    .load((ServerConfig.Video_Imagepath + mtTrack.getVideoPoster()))
                    .error(R.mipmap.defualt_img)
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
            Picasso.with(mContext).load(R.mipmap.defualt_img)
                    .error(R.mipmap.defualt_img)
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
