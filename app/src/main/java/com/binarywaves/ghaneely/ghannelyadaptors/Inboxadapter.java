package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class Inboxadapter extends ArrayAdapter<TrackObject> implements
        Swappable, UndoAdapter, OnDismissCallback, OnClickListener {
    private List<TrackObject> mComments;
    private Boolean flag;
    private Context mContext;
    private int background;

    public Inboxadapter(Context context, int resource, List<TrackObject> mtrackList1) {
        super(context, resource, mtrackList1);
        mContext = context;
        mComments = mtrackList1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public TrackObject getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mComments.get(position).getAlbumArName());
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(mComments, positionOne, positionTwo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.inboxitem, parent, false);
        TextView name = row.findViewById(R.id.item_text);
        TextView item_title = row.findViewById(R.id.item_title);
        ImageView pointicon = row.findViewById(R.id.pointicon);
        ImageView imageItem = row
                .findViewById(R.id.imagesingerIcon);
        ImageView ivoverlay = row
                .findViewById(R.id.ivoverlay);
        FrameLayout framecon = row
                .findViewById(R.id.framecon);
        if (mComments.get(position).getTrackImage() != null || mComments.get(position).getTrackImage() != "") {
            switch (Integer.parseInt(mComments.get(position).getTrackArName())) {
                case 1:
                    Picasso.with(mContext).load((ServerConfig.ALBUM_IMAGE_PATH + mComments.get(position).getTrackImage()))
                            .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(imageItem);
                    if (Boolean.valueOf(mComments.get(position).getIsPremium())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            background = mContext.getResources().getColor(R.color.dimmedcolor, mContext.getTheme());
                            imageItem.setColorFilter(background);


                        } else {
                            background = mContext.getResources().getColor(R.color.dimmedcolor);
                            imageItem.setColorFilter(background);

                        }
                    } else {
                        imageItem.setColorFilter(null);


                    }
                    Picasso.with(mContext)
                            .load(R.mipmap.songsearch)
                            .error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(ivoverlay);
                    break;

                case 2:
                    Picasso.with(mContext).load((ServerConfig.ALBUM_IMAGE_PATH + mComments.get(position).getTrackImage()))
                            .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(imageItem);
                    Picasso.with(mContext)
                            .load(R.mipmap.albumsearch)
                            .error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(ivoverlay);
                    break;

                case 3:
                    Picasso.with(mContext).load((ServerConfig.PLAYLIST_IMAGE_PATH + mComments.get(position).getTrackImage()))
                            .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(imageItem);
                    Picasso.with(mContext)
                            .load(R.mipmap.playlistsearch)
                            .error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(ivoverlay);

                    break;

                case 4:
                    String imgpath = ServerConfig.Image_pathSinger + mComments.get(position).getSingerImg();

                    String final_imgpath = imgpath.replaceAll(" ", "%20");

                    Picasso.with(mContext).load((final_imgpath)).error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(imageItem);
                    framecon.setVisibility(View.GONE);

                    break;
                case 5:
                    String imgpathvideo = ServerConfig.Video_Imagepath + mComments.get(position).getTrackImage();

                    String final_imgpathv = imgpathvideo.replaceAll(" ", "%20");

                    Picasso.with(mContext).load((final_imgpathv)).error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(imageItem);
                    framecon.setVisibility(View.GONE);

                    break;

                case 6:
                    String imgpathradio = ServerConfig.RADIO_IMAGE_PATH + mComments.get(position).getTrackImage();

                    String final_imgpathr = imgpathradio.replaceAll(" ", "%20");

                    Picasso.with(mContext).load((final_imgpathr)).error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(imageItem);
                    framecon.setVisibility(View.GONE);

                    break;
            }




        } else {
            Picasso.with(mContext).load(R.mipmap.defualt_img)
                    .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(imageItem);

        }
        SharedPreferences prefs1 = mContext.getSharedPreferences("Inboxopened", Context.MODE_PRIVATE);
        String httpParamJSONList = prefs1.getString("Inboxopened1", "");

        List<String> httpParamList = new Gson().fromJson(httpParamJSONList, new TypeToken<List<String>>() {
        }.getType());
        if (httpParamList != null && httpParamList.size() > 0) {
            int i;
            for (int j = 0; j < mComments.size(); j++) {
                flag = false;
                for (i = 0; i < httpParamList.size(); i++) {

                    if (httpParamList.get(i).equalsIgnoreCase(mComments.get(j).getAlbumArName())) {

                        flag = true;
                        break;

                    }
                }

                if (!flag && position == j) {
                    Typeface customFont = null;
                    String language = LanguageHelper.getCurrentLanguage(mContext);

                    if (language.equalsIgnoreCase("ar"))
                        customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/GE_SS_Two_Medium.otf");

                    else
                        customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roundo-SemiBold.otf");


                    item_title.setText(mComments.get(position).getAlbumEnName());
                    item_title.setTypeface(customFont);
                    item_title.setTextColor(mContext.getResources().getColor(R.color.white));
                    item_title.setTextSize(20);
                    pointicon.setVisibility(View.VISIBLE);
                } else {
                    Typeface customFont = null;
                    String language = LanguageHelper.getCurrentLanguage(mContext);

                    if (language.equalsIgnoreCase("ar"))
                        customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/GE_SS_Text_Light.otf");

                    else
                        customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roundo-Regular.otf");


                    item_title.setText(mComments.get(position).getAlbumEnName());

                    item_title.setTypeface(customFont);
                    item_title.setTextColor(mContext.getResources().getColor(R.color.white));
                    item_title.setTextSize(15);
                    pointicon.setVisibility(View.INVISIBLE);

                }
            }

        } else {
            item_title.setText(mComments.get(position).getAlbumEnName());
            Typeface customFont = null;

            String language = LanguageHelper.getCurrentLanguage(mContext);

            if (language.equalsIgnoreCase("ar"))
                customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/GE_SS_Text_Light.otf");

            else
                customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roundo-Regular.otf");

            item_title.setTypeface(customFont);
            item_title.setTextColor(mContext.getResources().getColor(R.color.white));
            item_title.setTextSize(15);
            pointicon.setVisibility(View.INVISIBLE);

        }
        name.setText(mComments.get(position).getSingerEnName());
        name.setText(mComments.get(position).getTrackEnName());


        return row;
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
        mComments.remove(position);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}
