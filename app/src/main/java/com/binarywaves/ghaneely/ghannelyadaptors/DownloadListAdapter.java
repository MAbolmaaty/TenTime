package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackDownloadObject;
import com.binarywaves.ghaneely.ghannelyutils.DbBitmapUtility;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;

import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by Amany on 20-Sep-17.
 */

public class DownloadListAdapter extends ArrayAdapter<TrackDownloadObject> implements
        Swappable, UndoAdapter, OnDismissCallback, View.OnClickListener {
    private int layoutResourceId;
    // public ImageLoader imageLoader;
    private List<TrackDownloadObject> data;
    private Context mContext;
    private int background;
    private String ENCRYPTED_FILE_NAME;
    private FileOutputStream fileOutputStream;

    public DownloadListAdapter(Context context, int layoutResourceId,
                               List<TrackDownloadObject> data) {
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
    public TrackDownloadObject getItem(int position) {
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

        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(data.get(position).getTrackArName());
            holder.tvsingerName.setText(data.get(position).getSingerArName());


        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(data.get(position).getTrackEnName());
            holder.tvsingerName.setText(data.get(position).getSingerEnName());


        }

        holder.txttime.setText(getDurationString(Double.parseDouble(data.get(position).getTrackLength())));
        holder.imageItem.setImageBitmap(DbBitmapUtility.getImage(data.get(position).getTrackImage()));


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
        data.remove(position);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(data, positionOne, positionTwo);
    }

}

