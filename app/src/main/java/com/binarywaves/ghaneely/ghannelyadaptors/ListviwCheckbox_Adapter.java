package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.ListviwCheckbox_Activity;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyresponse.TrackLst;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListviwCheckbox_Adapter extends ArrayAdapter<TrackLst> {
    private ArrayList<TrackLst> countryList;
    private Context mContext;
    private int background;
    private  int layoutResourceId;

    public ListviwCheckbox_Adapter(Context context, int textViewResourceId, ArrayList<TrackLst> countryList) {
        super(context, textViewResourceId, countryList);
        this.mContext = context;
        this.layoutResourceId = textViewResourceId;

        this.countryList = countryList;
        // this.countryList.addAll(countryList);
    }

    public static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public TrackLst getItem(int position) {
        return countryList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return Long.parseLong(countryList.get(position).getTrackId());
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

            Log.i("NULL VIEW", "here");
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);


        TextView trackname = row.findViewById(R.id.tvsongName);
        Button checkbox = row.findViewById(R.id.checkBox1);
        ImageView albumimage = row.findViewById(R.id.imagesingerIcon);
        TextView txttime = row.findViewById(R.id.tvtime);
        RelativeLayout rlcont = row.findViewById(R.id.rlcont);
        RelativeLayout controoler = row.findViewById(R.id.controoler);

        TextView tvsingerName = row.findViewById(R.id.tvsingerName);
        Button checkboxchecked = row.findViewById(R.id.checkBox2);
        TrackLst country = countryList.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            trackname.setText(country.getTrackArName());
            tvsingerName.setText(country.getSingerArName());


        }
        if (language.equalsIgnoreCase("en")) {
            trackname.setText(country.getTrackEnName());
            tvsingerName.setText(country.getSingerEnName());


        }

        txttime.setText(getDurationString(Double.parseDouble(country.getTrackLength())));
        if (country.getTrackImage() != null || country.getTrackImage() != "") {
            Picasso.with(mContext).load((ServerConfig.ALBUM_IMAGE_PATH + country.getTrackImage()))
                    .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(albumimage);
            if (Boolean.valueOf(country.getIsPremium())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    background = mContext.getResources().getColor(R.color.dimmedcolor, mContext.getTheme());
                    albumimage.setColorFilter(background);


                } else {
                    background = mContext.getResources().getColor(R.color.dimmedcolor);
                    albumimage.setColorFilter(background);

                }
            } else {
                albumimage.setColorFilter(null);


            }
        } else {
            Picasso.with(mContext).load(R.mipmap.defualt_img)
                    .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(albumimage);
            if (Boolean.valueOf(country.getIsPremium())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    background = mContext.getResources().getColor(R.color.dimmedcolor, mContext.getTheme());
                    albumimage.setColorFilter(background);


                } else {
                    background = mContext.getResources().getColor(R.color.dimmedcolor);
                    albumimage.setColorFilter(background);

                }
            } else {
                albumimage.setColorFilter(null);


            }
        }

        rlcont.setOnClickListener(v -> {

            // TODO Auto-generated method stub
            if (checkbox.getVisibility() == View.VISIBLE) {
                checkbox.setVisibility(View.GONE);
                checkboxchecked.setVisibility(View.VISIBLE);
                ListviwCheckbox_Activity.selectedcount.add(country.getTrackId().toString().trim());
            } else {
                if (checkboxchecked.getVisibility() == View.VISIBLE) {
                    checkboxchecked.setVisibility(View.GONE);
                    checkbox.setVisibility(View.VISIBLE);
                    for (int i = 0; i < ListviwCheckbox_Activity.selectedcount.size(); i++) {
                        if (ListviwCheckbox_Activity.selectedcount.get(i).equalsIgnoreCase(country.getTrackId().toString().trim())) {
                            ListviwCheckbox_Activity.selectedcount.remove(i);
                        }
                    }


                }
            }

        });
        controoler.setOnClickListener(v -> {

            // TODO Auto-generated method stub
            if (checkbox.getVisibility() == View.VISIBLE) {
                checkbox.setVisibility(View.GONE);
                checkboxchecked.setVisibility(View.VISIBLE);
                ListviwCheckbox_Activity.selectedcount.add(country.getTrackId().toString().trim());
            } else {
                if (checkboxchecked.getVisibility() == View.VISIBLE) {
                    checkboxchecked.setVisibility(View.GONE);
                    checkbox.setVisibility(View.VISIBLE);
                    for (int i = 0; i < ListviwCheckbox_Activity.selectedcount.size(); i++) {
                        if (ListviwCheckbox_Activity.selectedcount.get(i).equalsIgnoreCase(country.getTrackId().toString().trim())) {
                            ListviwCheckbox_Activity.selectedcount.remove(i);
                        }
                    }


                }
            }

        });


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
