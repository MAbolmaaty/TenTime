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
import com.binarywaves.ghaneely.ghannelyadaptors.GalleryAdaptor.RecordHolder;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.Radio;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RadioAdaptor extends ArrayAdapter<Radio> {
    private int layoutResourceId;
    private ArrayList<Radio> data = new ArrayList<>();
    private Context mContext;

    public RadioAdaptor(Context context, int layoutResourceId,
                        ArrayList<Radio> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
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
        Radio mRadio = data.get(position);
        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            holder.txtTitle.setText(mRadio.getRadioArName());
        }
        if (language.equalsIgnoreCase("en")) {
            holder.txtTitle.setText(mRadio.getRadioName());
        }


        String imgpath = ServerConfig.Radio_Url + mRadio.getRadioImage();
        String final_imgpath = imgpath.replaceAll(" ", "%20");

        Log.e("Radio IMage Path",imgpath);
        if (mRadio.getRadioImage() != null || mRadio.getRadioImage() != "") {
            Picasso.with(mContext).load((final_imgpath)).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
        } else {
            Picasso.with(mContext).load(R.mipmap.defualt_img).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(holder.imageItem);
        }
        return row;
    }


}
