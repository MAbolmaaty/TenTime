package com.binarywaves.ghaneely.ghannelyadaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amany on 03-Jul-17.
 */

public class VideoGalleryAdapter<T> extends BaseAdapter {
    public static View view;
    static ArrayList<VideoObjectResponse> mList;
    private static boolean isInfiniteLoop;
    public ImageView imageItem;
    LayoutInflater inflater;
    TextureView surface_view;
    TextView songname;
    TextView albumname;
    TextView likescount;
    TextView listencount;
    private Context mContext;

    public VideoGalleryAdapter(Context context, int resource, int textViewResourceId, ArrayList<VideoObjectResponse> objects) {
        super();
        this.mContext = context;
        mList = objects;
        Log.e("testAdaptor", "here");
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isInfiniteLoop = false;
    }

    public static int getPosition(int position) {
        return isInfiniteLoop ? position % mList.size() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customplayer_video, parent, false);
            imageItem = view.findViewById(R.id.Gallery01);

            surface_view = view.findViewById(R.id.surface_view);
            surface_view.invalidate();
            surface_view.setWillNotDraw(false);
            songname = view.findViewById(R.id.song_title);

            albumname = view.findViewById(R.id.album_title);
            likescount = view.findViewById(R.id.likescount);
            listencount = view.findViewById(R.id.listencount);

        }
        VideoObjectResponse item = mList.get(getPosition(position));
        Log.e("imagepath on server", ServerConfig.Video_Imagepath + item.getVideoPoster());
        if (item.getVideoPoster() != null || item.getVideoPoster() != "") {
            Picasso.with(mContext).load((ServerConfig.Video_Imagepath + item.getVideoPoster())).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(imageItem);
        } else {
            Picasso.with(mContext).load(R.mipmap.defualt_img).error(R.mipmap.defualt_img)
                    .placeholder(R.mipmap.defualt_img).into(imageItem);

        }

        String language = LanguageHelper.getCurrentLanguage(mContext);


        if (language.equalsIgnoreCase("ar")) {
            songname.setText(item.getSingerArName());
            albumname.setText(item.getVideoArName());
            likescount.setText(item.getLikesCount() + "   ");
            listencount.setText(item.getViewCount());

        }
        if (language.equalsIgnoreCase("en")) {
            songname.setText(item.getSingerEnName());
            albumname.setText(item.getVideoEnName());
            likescount.setText(item.getLikesCount());
            listencount.setText(item.getViewCount());

        }

        return view;

    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : mList.size();
    }

    /**
     * get really position
     *
     * @param
     * @return
     */

    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public VideoGalleryAdapter<T> setInfiniteLoop(boolean isInfiniteLoop) {
        VideoGalleryAdapter.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder1 {

        public SurfaceView surface_view;
        public ImageView imageItem;
        TextView songname;
        TextView albumname;
        TextView likescount;
        TextView listencount;


    }


}
