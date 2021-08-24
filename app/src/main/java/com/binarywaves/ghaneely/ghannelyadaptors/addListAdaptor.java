package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannelyactivites.AddToActivity;
import com.binarywaves.ghaneely.ghannelyactivites.AddtoVideoActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;

public class addListAdaptor extends ArrayAdapter<String> {
    private Context mContext;
    private int layoutResourceId;
    private String[] data = null;
    Integer[] pics;
    private Integer[] picsunlike;
    private Integer[] picsunlikevideo;
    Integer[] picsvideo;
    private String language;

    public addListAdaptor(Context mContext, int layoutResourceId, String[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        setListItems();
        data = mContext.getResources().getStringArray(R.array.player_list);
    }

    private static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.add_palyer_list_item, parent, false);
        TextView name = row.findViewById(R.id.add_player_text);

        ImageView icon = row.findViewById(R.id.add_player_icon);
        name.setText(data[position]);
        language = LanguageHelper.getCurrentLanguage(mContext);


        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                mContext);
        if (isServiceRunning3) {
            icon.setBackgroundResource(picsunlike[position]);

            if (PlayerConstants.SONGS_LIST.size() > 0) {

                if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite().equalsIgnoreCase("true")) {
                    if (position == 0) {
                        if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsFavourite().equalsIgnoreCase("true")) {
                            name.setText(mContext.getResources().getString(R.string.unlike));

                            icon.setBackgroundResource(R.mipmap.unlike);
                        } else {
                            name.setText(mContext.getResources().getString(R.string.like));

                            icon.setBackgroundResource(R.mipmap.like);

                        }

                    }
                    if (position == 4) {
                        if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getIsDownloaded().equalsIgnoreCase("true")) {

                            icon.setBackgroundResource(R.mipmap.downloadon);
                        } else {
                            icon.setBackgroundResource(R.mipmap.downloadplus);
                        }
                    }

                }


            }

        }

        if (getActivity() != null) {
            String mpackageName = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackageName
                    .equalsIgnoreCase("AddToActivity")) {
                if (AddToActivity.trask != null) {
                    icon.setBackgroundResource(picsunlike[position]);

                    if (position == 0) {
                        if (AddToActivity.trask.getIsFavourite().equalsIgnoreCase("true")) {
                            name.setText(mContext.getResources().getString(R.string.unlike));

                            icon.setBackgroundResource(R.mipmap.unlike);
                        } else {
                            name.setText(mContext.getResources().getString(R.string.like));

                            icon.setBackgroundResource(R.mipmap.like);

                        }

                    }
                    if (position == 4) {
                        if (AddToActivity.trask.getIsDownloaded().equalsIgnoreCase("true")) {

                            icon.setBackgroundResource(R.mipmap.downloadon);
                        } else {
                            icon.setBackgroundResource(R.mipmap.downloadplus);
                        }
                    }


                }
            }
        }

        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                mContext);

        if (isServiceRunning4) {
            icon.setBackgroundResource(picsunlikevideo[position]);
            if (PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER) != null) {
                if (position == 0) {
                    if (PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getIsFavourite().equalsIgnoreCase("true")) {

                            name.setText(mContext.getResources().getString(R.string.unlike));

                            icon.setBackgroundResource(R.mipmap.unlike);
                        }
                    else {
                        name.setText(mContext.getResources().getString(R.string.like));

                        icon.setBackgroundResource(R.mipmap.like);

                    }

                }

                if (position == 1) {
                    if (PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getIsDownloaded().equalsIgnoreCase("true")) {

                        icon.setBackgroundResource(R.mipmap.downloadon);
                    } else {
                        icon.setBackgroundResource(R.mipmap.downloadplus);
                    }
                }


            }
        }

        if (getActivity() != null) {
            String mpackageName = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackageName
                    .equalsIgnoreCase("AddtoVideoActivity")) {
                icon.setBackgroundResource(picsunlikevideo[position]);

                if (AddtoVideoActivity.trask != null) {
                    if (position == 0) {

                        if (AddtoVideoActivity.trask.getIsFavourite().equalsIgnoreCase("true")) {
                            name.setText(mContext.getResources().getString(R.string.unlike));

                            icon.setBackgroundResource(R.mipmap.unlike);
                        } else {
                            name.setText(mContext.getResources().getString(R.string.like));

                            icon.setBackgroundResource(R.mipmap.like);

                        }
                    }

                    if (position == 1) {
                        if (PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getIsDownloaded().equalsIgnoreCase("true")) {

                            icon.setBackgroundResource(R.mipmap.downloadon);
                        } else {
                            icon.setBackgroundResource(R.mipmap.downloadplus);
                        }
                    }


                }
            }
        }

        return row;

    }

    private void setListItems() {


        picsunlike = new Integer[]{R.mipmap.unlike, R.mipmap.playlist, R.mipmap.setascalltone, // rbt

                R.mipmap.lyrics, R.mipmap.downloadplus, R.mipmap.share, R.mipmap.radio, R.mipmap.gotoartist, R.mipmap.album};// artist


        picsunlikevideo = new Integer[]{R.mipmap.unlike, // rbt
                R.mipmap.downloadplus, R.mipmap.share, R.mipmap.radio, R.mipmap.gotoartist};


    }
}
