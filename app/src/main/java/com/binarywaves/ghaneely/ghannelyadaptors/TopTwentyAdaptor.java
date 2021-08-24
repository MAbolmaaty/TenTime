package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.AddToActivity;
import com.binarywaves.ghaneely.ghannelyactivites.GIFDemo;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Amany on 19-Jun-17.
 */

public class TopTwentyAdaptor extends ArrayAdapter<TrackObject> {
    private int layoutResourceId;
    // public ImageLoader imageLoader;
    private List<TrackObject> data;
    private Context mContext;
    private int background;

    public TopTwentyAdaptor(Context context, int layoutResourceId,
                            List<TrackObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
        // imageLoader = new ImageLoader(context);
        Log.i("category size in ", data.size() + "");
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
            holder.txttime = row.findViewById(R.id.tvtime);
            holder.imageItem = row
                    .findViewById(R.id.imagesingerIcon);
            holder.tvsingerName = row.findViewById(R.id.tvsingerName);

            row.setTag(holder);
        }
        Log.i("Not NULL VIEW", "here");
        holder = (GalleryAdaptor.RecordHolder) row.getTag();
        TrackObject mtTrack = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mtTrack.getTrackArName());
            holder.tvsingerName.setText(mtTrack.getSingerArName());


        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mtTrack.getTrackEnName());
            holder.tvsingerName.setText(mtTrack.getSingerEnName());


        }

        holder.txttime.setText(getDurationString(Double.parseDouble(mtTrack.getTrackLength())));
        if (mtTrack.getTrackImage() != null || mtTrack.getTrackImage() != "") {

            Picasso.with(mContext)
                    .load((ServerConfig.Image_path + mtTrack.getTrackImage()))
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

        // imageLoader.DisplayImage(
        // ServerConfig.Image_path + mtTrack.getAlbumImgPath(),
        // holder.imageItem);
        ImageView imageViewIcon = row
                .findViewById(R.id.ivadd);

        ImageView ivplay = row
                .findViewById(R.id.ivplay);
        GIFDemo gifimage = row.findViewById(R.id.gifimage);
        gifimage.setImageResource(R.mipmap.equalizer);
        String mpackageName;
        imageViewIcon.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent img = new Intent(mContext, AddToActivity.class);

            TrackObject trask = data.get(position);
            Log.e("Selected gallarey item", trask.getSingerEnName()
                    + "ff");
            img.putExtra("trask", trask);
            img.putExtra("FromPlayer", "No");
            mContext.startActivity(img);

        });
        if (getActivity() != null) {
            mpackageName = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackageName
                    .equalsIgnoreCase("HomeActivity")) {

                if (HomeActivity.selecttopsong == position) {

                    if (SongService.mp != null && SongService.mp.getPlayWhenReady()) {
                        ivplay.setVisibility(View.GONE);
                        gifimage.setVisibility(View.VISIBLE);
                    } else if (SongService.mp != null && !SongService.mp.getPlayWhenReady()) {
                        gifimage.setVisibility(View.GONE);
                        ivplay.setVisibility(View.VISIBLE);


                    }

                } else {
                    gifimage.setVisibility(View.GONE);
                    ivplay.setVisibility(View.VISIBLE);


                }
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
        private static Activity getActivity() {


            return Applicationmanager.getCurrentActivity();
        }

    }
