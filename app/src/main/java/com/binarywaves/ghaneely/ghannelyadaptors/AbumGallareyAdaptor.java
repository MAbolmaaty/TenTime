package com.binarywaves.ghaneely.ghannelyadaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.AddToActivity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTab_Tracks_Activity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_SingleActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_TopActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FavoritesActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_LikesActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_RecentlyplayedActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendsTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.GIFDemo;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_AllActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_TracksActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.GalleryAdaptor.RecordHolder;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AbumGallareyAdaptor extends ArrayAdapter<TrackObject> {
    private final int layoutResourceId;
    // public ImageLoader imageLoader;
    private final List<TrackObject> data;
    private GIFDemo gifimage;
    int selectedfav;
    private final Context mContext;
    private int background;

    public AbumGallareyAdaptor(Context context, int layoutResourceId,
                               List<TrackObject> data) {
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
        // TODO Auto-generated method stub
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {

            Log.i("NULL VIEW", "here");
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = row.findViewById(R.id.tvsongName);
            holder.txttime = row.findViewById(R.id.tvtime);
            holder.imageItem = row
                    .findViewById(R.id.imagesingerIcon);
            holder.tvsingerName = row.findViewById(R.id.tvsingerName);
            row.setTag(holder);
        }
        Log.i("Not NULL VIEW", "here");
        holder = (RecordHolder) row.getTag();
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

        ImageView imageViewIcon = row
                .findViewById(R.id.ivadd);
        RelativeLayout controller = row
                .findViewById(R.id.controller);

        ImageView ivplay = row
                .findViewById(R.id.ivplay);
        gifimage = row.findViewById(R.id.gifimage);
        gifimage.setImageResource(R.mipmap.equalizer);
        String mpackageName;
        imageViewIcon.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent img = new Intent(mContext, AddToActivity.class);

            TrackObject trask = data.get(position);
            Log.e("Selected gallarey item", trask.getSingerEnName()
                    + "ff");
            img.putExtra("trask", trask);
            img.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear
            // all
            img.putExtra("FromPlayer", "yes");
            mContext.startActivity(img);

        });
        if (getActivity() != null) {
            mpackageName = getActivity().getComponentName().getClassName().replace("com.binarywaves.ghaneely.ghannelyactivites.", "");

            if (mpackageName
                    .equalsIgnoreCase("HomeActivity")) {


                    if (HomeActivity.selectessong == position) {
                        if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                        ivplay.setVisibility(View.GONE);
                        gifimage.setVisibility(View.VISIBLE);
                    }
                    else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                        gifimage.setVisibility(View.GONE);
                        ivplay.setVisibility(View.VISIBLE);


                    }}
                 else {
                    gifimage.setVisibility(View.GONE);
                    ivplay.setVisibility(View.VISIBLE);


                }

           }

            if (mpackageName
                    .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
                switch (AlbumTabsActivity.selectedfragment) {
                    case 0:
                        if (AlbumTab_Tracks_Activity.selectessong == position) {

                            if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                ivplay.setVisibility(View.GONE);
                                gifimage.setVisibility(View.VISIBLE);
                            }
                            else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);


                            }} else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }

                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))
                            {
                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }} else {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);

                            }

                        break;

                }}
            }
            if (mpackageName
                    .equalsIgnoreCase("FavoritesActivity")) {

                if (FavoritesActivity.selectessong == position) {

                    if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                        ivplay.setVisibility(View.GONE);
                        gifimage.setVisibility(View.VISIBLE);
                    }
                    else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                        gifimage.setVisibility(View.GONE);
                        ivplay.setVisibility(View.VISIBLE);


                    }} else {
                    gifimage.setVisibility(View.GONE);
                    ivplay.setVisibility(View.VISIBLE);


                }
                if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                    if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))
                    {
                        if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                            ivplay.setVisibility(View.GONE);
                            gifimage.setVisibility(View.VISIBLE);
                        }
                        else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }} else {
                        gifimage.setVisibility(View.GONE);
                        ivplay.setVisibility(View.VISIBLE);


                }
            }}


            if (mpackageName
                    .equalsIgnoreCase("ArtistTabs.ArtistTabsActivity")) {
                switch (ArtistTabsActivity.selectedfragment) {
                    case 0:
                        if (ArtistTab_TopActivity.selectessong == position) {

                            if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                ivplay.setVisibility(View.GONE);
                                gifimage.setVisibility(View.VISIBLE);
                            }
                            else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);


                            }} else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }

                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))
                            {
                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }}else {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);
                            }}

                        break;


                    case 3:
                        if (ArtistTab_SingleActivity.selectessong == position) {
                            if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                ivplay.setVisibility(View.GONE);
                                gifimage.setVisibility(View.VISIBLE);
                            }
                            else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);


                            }} else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }

                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))
                            {
                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }} else {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);

                            }
                        }
                        break;

                }
            }


            if (mpackageName
                    .equalsIgnoreCase("FriendsTabs.FriendsTabActivity")) {
                switch (FriendsTabActivity.selectedfragment) {
                    case 0:
                        if (FriendTab_RecentlyplayedActivity.selectessong == position)

                        {
                            if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                ivplay.setVisibility(View.GONE);
                                gifimage.setVisibility(View.VISIBLE);
                            }
                            else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);


                            }}else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }
                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))

                            {
                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }} else {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);

                            }
                        }
                        break;


                    case 1:
                        if (FriendTab_LikesActivity.selectessong == position)

                        {
                            if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                ivplay.setVisibility(View.GONE);
                                gifimage.setVisibility(View.VISIBLE);
                            }
                            else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);


                            }} else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }
                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))

                            {
                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }} else {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);

                            }
                        }
                        break;

                }
            }


            if (mpackageName
                    .equalsIgnoreCase("SearchTabs.SearchTabActivity")) {
                switch (SearchTabActivity.selectedfragment) {
                    case 0:
                        if (SearchTab_AllActivity.selectessong == position)

                        {
                            if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                ivplay.setVisibility(View.GONE);
                                gifimage.setVisibility(View.VISIBLE);
                            }
                            else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);


                            }} else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }

                        if (SearchTab_AllActivity.selectessongsearch == position)

                        {
                            if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                ivplay.setVisibility(View.GONE);
                                gifimage.setVisibility(View.VISIBLE);
                            }
                            else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);


                            }}else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }
                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))

                            {
                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }} else {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);

                            }
                        }
                        break;


                    case 1:
                        if (SearchTab_TracksActivity.selectessong == position) {


                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }} else {
                            gifimage.setVisibility(View.GONE);
                            ivplay.setVisibility(View.VISIBLE);


                        }
                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0 && data != null && data.size() > 0) {
                            if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(mtTrack.getTrackId()))

                            {
                                if (SongService.mp!=null&&SongService.mp.getPlayWhenReady()) {
                                    ivplay.setVisibility(View.GONE);
                                    gifimage.setVisibility(View.VISIBLE);
                                }
                                else if (SongService.mp!=null&&!SongService.mp.getPlayWhenReady()) {
                                    gifimage.setVisibility(View.GONE);
                                    ivplay.setVisibility(View.VISIBLE);


                                }} else {
                                gifimage.setVisibility(View.GONE);
                                ivplay.setVisibility(View.VISIBLE);

                            }
                        }
                        break;

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

}
