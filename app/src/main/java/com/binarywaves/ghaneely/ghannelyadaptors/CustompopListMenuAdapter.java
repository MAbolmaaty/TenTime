package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;

/**
 * Created by Amany on 14-Aug-17.
 */

public class CustompopListMenuAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int layoutResourceId;
    private String[] data = null;
    private int background;

    public CustompopListMenuAdapter(Context mContext, int layoutResourceId, String[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_ghannely_menu, parent, false);
        TextView name = row.findViewById(R.id.textViewName);

        ImageView icon = row.findViewById(R.id.imageViewIcon);

        if (DrawerActivity.selected_popup_menu == position) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                background = mContext.getResources().getColor(R.color.ghaneely_orange, mContext.getTheme());

            } else {
                background = mContext.getResources().getColor(R.color.ghaneely_orange);
            }
            name.setTextColor(background);
            icon.setVisibility(View.VISIBLE);


        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                background = mContext.getResources().getColor(R.color.white, mContext.getTheme());

            } else {
                background = mContext.getResources().getColor(R.color.white);
            }
            name.setTextColor(background);
            icon.setVisibility(View.INVISIBLE);


        }
        name.setText(data[position]);


        return row;

    }

    /*
        Integer[] pics = { R.mipmap.like, R.mipmap.playlist, R.mipmap.setascalltone, // rbt

                R.mipmap.lyrics, R.mipmap.downloadplus, R.mipmap.share, R.mipmap.radio, R.mipmap.gotoartist, R.mipmap.album };// artist

        Integer[] picsunlike = { R.mipmap.unlike, R.mipmap.playlist, R.mipmap.setascalltone, // rbt

                R.mipmap.lyrics,R.mipmap.downloadplus, R.mipmap.share, R.mipmap.radio, R.mipmap.gotoartist, R.mipmap.album };// artist




        Integer[] picsvideo = { R.mipmap.like,  // rbt

                R.mipmap.downloadplus, R.mipmap.share, R.mipmap.radio, R.mipmap.gotoartist };// artist

        Integer[] picsunlikevideo = { R.mipmap.unlike, // rbt
                R.mipmap.downloadplus, R.mipmap.share, R.mipmap.radio, R.mipmap.gotoartist };// artist
    */
    public void setListItems() {

    }

}
