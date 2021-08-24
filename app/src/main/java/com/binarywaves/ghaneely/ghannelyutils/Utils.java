package com.binarywaves.ghaneely.ghannelyutils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsGridActivity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTab_Otheralbum_Activity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTab_Relatedalbum_Activity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTab_Tracks_Activity;
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_AlbumsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_SimilarActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_SingleActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTab_TopActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistradioActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FavoritesActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FavoritesRadio;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_LikesActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_RecentlyplayedActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendTab_followArtActivity;
import com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs.FriendsTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.InboxActivity;
import com.binarywaves.ghaneely.ghannelyactivites.MoodsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayListActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayListTracksActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_AlbumActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_AllActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_ArtistActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_PlaylistActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTab_TracksActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;

public class Utils {
    public static void changeStatusBarColor(Activity activity,Context ctx){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColorWrapper(ctx,R.color.black));

        }
    }


    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }



    public static void handle_ScreenViews(String mpackagename) {
        if (mpackagename
                .equalsIgnoreCase("PlayListTracksActivity")) {
            if( PlayListTracksActivity.progBar!=null)

                PlayListTracksActivity.progBar.setVisibility(View.GONE);
            if( PlayListTracksActivity.trackgalleryAdaptor!=null)

                PlayListTracksActivity.trackgalleryAdaptor.notifyDataSetInvalidated();
            if( PlayListTracksActivity.audioRelative!=null){

                PlayListTracksActivity.audioRelative.setVisibility(View.VISIBLE);
                PlayListTracksActivity.audioRelative.setEnabled(true);}
        }

        if (mpackagename
                .equalsIgnoreCase("PlayListActivity")) {
            if( PlayListActivity.progBar!=null)
                PlayListActivity.progBar.setVisibility(View.GONE);
            if( PlayListActivity.audioRelative!=null){

                PlayListActivity.audioRelative.setVisibility(View.VISIBLE);
                PlayListActivity.audioRelative.setEnabled(true);}
        }
        if (mpackagename
                .equalsIgnoreCase("FriendsTabs.FriendsTabActivity")) {
            Log.i("selectedfragment", FriendsTabActivity.selectedfragment + "");

            switch (FriendsTabActivity.selectedfragment) {

                case 0:
                    if( FriendTab_RecentlyplayedActivity.progBar!=null)

                        FriendTab_RecentlyplayedActivity.progBar.setVisibility(View.GONE);
                    if( FriendTab_RecentlyplayedActivity.audioRelative!=null){

                        FriendTab_RecentlyplayedActivity.audioRelative.setVisibility(View.VISIBLE);
                        FriendTab_RecentlyplayedActivity.audioRelative.setEnabled(true);}
                    FriendTab_RecentlyplayedActivity.selectessong = PlayerConstants.SONG_NUMBER;
                    if( FriendTab_RecentlyplayedActivity.myTrackadAdaptor!=null)
                        FriendTab_RecentlyplayedActivity.myTrackadAdaptor.notifyDataSetInvalidated();
                    break;
                case 1:
                    if( FriendTab_LikesActivity.progBar!=null)

                        FriendTab_LikesActivity.progBar.setVisibility(View.GONE);
                    if( FriendTab_LikesActivity.audioRelative!=null){

                        FriendTab_LikesActivity.audioRelative.setVisibility(View.VISIBLE);
                        FriendTab_LikesActivity.audioRelative.setEnabled(true);}
                    FriendTab_LikesActivity.selectessong = PlayerConstants.SONG_NUMBER;
                    if( FriendTab_LikesActivity.myTrackadAdaptor!=null)
                        FriendTab_LikesActivity.myTrackadAdaptor.notifyDataSetInvalidated();

                    break;
                case 2:
                    if( FriendTab_followArtActivity.progBar!=null)

                        FriendTab_followArtActivity.progBar.setVisibility(View.GONE);
                    if( FriendTab_followArtActivity.audioRelative!=null){

                        FriendTab_followArtActivity.audioRelative.setVisibility(View.VISIBLE);
                        FriendTab_followArtActivity.audioRelative.setEnabled(true);}
                    break;


            }
        }
        if (mpackagename
                .equalsIgnoreCase("InboxActivity")) {
            if(InboxActivity.progBar!=null)
                InboxActivity.progBar.setVisibility(View.GONE);
            if(InboxActivity.audioRelative!=null)
            {
                InboxActivity.audioRelative.setVisibility(View.VISIBLE);
                InboxActivity.audioRelative.setEnabled(true);
            }}


        if (mpackagename
                .equalsIgnoreCase("SearchTabs.SearchTabActivity")) {
            switch (SearchTabActivity.selectedfragment) {
                case 0:
                    if (SearchTab_AllActivity.progBar != null && SearchTab_AllActivity.audioRelative != null) {
                        SearchTab_AllActivity.progBar.setVisibility(View.GONE);

                        SearchTab_AllActivity.audioRelative.setVisibility(View.VISIBLE);
                        SearchTab_AllActivity.audioRelative.setEnabled(true);
                    }
                    break;
                case 1:
                    if (SearchTab_TracksActivity.progBar != null && SearchTab_TracksActivity.audioRelative != null) {

                        SearchTab_TracksActivity.progBar.setVisibility(View.GONE);

                        SearchTab_TracksActivity.audioRelative.setVisibility(View.VISIBLE);
                        SearchTab_TracksActivity.audioRelative.setEnabled(true);
                        SearchTab_TracksActivity.selectessong = PlayerConstants.SONG_NUMBER;}
                        if( SearchTab_TracksActivity.myTrackadAdaptor!=null)
                        SearchTab_TracksActivity.myTrackadAdaptor.notifyDataSetInvalidated();

                    break;
                case 2:
                    if (SearchTab_PlaylistActivity.progBar != null && SearchTab_PlaylistActivity.audioRelative != null) {

                        SearchTab_PlaylistActivity.progBar.setVisibility(View.GONE);

                        SearchTab_PlaylistActivity.audioRelative.setVisibility(View.VISIBLE);
                        SearchTab_PlaylistActivity.audioRelative.setEnabled(true);
                    }

                    break;
                case 3:
                    if (SearchTab_ArtistActivity.progBar != null && SearchTab_ArtistActivity.audioRelative != null) {

                        SearchTab_ArtistActivity.progBar.setVisibility(View.GONE);

                        SearchTab_ArtistActivity.audioRelative.setVisibility(View.VISIBLE);
                        SearchTab_ArtistActivity.audioRelative.setEnabled(true);
                    }
                    break;
                case 4:
                    if (SearchTab_AlbumActivity.progBar != null && SearchTab_AlbumActivity.audioRelative != null) {

                        SearchTab_AlbumActivity.progBar.setVisibility(View.GONE);

                        SearchTab_AlbumActivity.audioRelative.setVisibility(View.VISIBLE);
                        SearchTab_AlbumActivity.audioRelative.setEnabled(true);
                    }

                    break;
            }
        }
        if (mpackagename
                .equalsIgnoreCase("ArtistradioActivity")) {
            if (ArtistradioActivity.progBar != null)
                ArtistradioActivity.progBar.setVisibility(View.GONE);
            if (ArtistradioActivity.audioRelative != null){

                ArtistradioActivity.audioRelative.setVisibility(View.VISIBLE);
                ArtistradioActivity.audioRelative.setEnabled(true);}
            if (ArtistradioActivity.trackgalleryAdaptor != null){
                ArtistradioActivity.selectessong = PlayerConstants.SONG_NUMBER;


                ArtistradioActivity.trackgalleryAdaptor.notifyDataSetInvalidated();}
        }
        if (mpackagename
                .equalsIgnoreCase("FavoritesActivity")) {
            if (FavoritesActivity.progBar != null)

                FavoritesActivity.progBar.setVisibility(View.GONE);
            if (FavoritesActivity.audioRelative != null){

                FavoritesActivity.audioRelative.setVisibility(View.VISIBLE);
                FavoritesActivity.audioRelative.setEnabled(true);
            }}

        if (mpackagename
                .equalsIgnoreCase("MoodsActivity")) {
            if (MoodsActivity.progBar != null)
                MoodsActivity.progBar.setVisibility(View.GONE);
            if (MoodsActivity.audioRelative != null){

                MoodsActivity.audioRelative.setVisibility(View.VISIBLE);
                MoodsActivity.audioRelative.setEnabled(true);}
            MoodsActivity.gridview.setEnabled(true);
            MoodsActivity.selectessong = PlayerConstants.SONG_NUMBER;
            if (MoodsActivity.trackgalleryAdaptor != null)

                MoodsActivity.trackgalleryAdaptor.notifyDataSetInvalidated();


        }

        if (mpackagename
                .equalsIgnoreCase("AlbumsGridActivity")) {
            if (AlbumsGridActivity.progBar != null)
                AlbumsGridActivity.progBar.setVisibility(View.GONE);
            if (AlbumsGridActivity.audioRelative != null)
                AlbumsGridActivity.audioRelative.setVisibility(View.VISIBLE);
            AlbumsGridActivity.audioRelative.setEnabled(true);
            if (AlbumsGridActivity.mFavTrack != null)
                AlbumsGridActivity.mFavTrack.setEnabled(true);

        }

        if (mpackagename
                .equalsIgnoreCase("AlbumsTabs.AlbumTabsActivity")) {
            Log.e("selectedfragment", AlbumTabsActivity.selectedfragment + "");

            switch (AlbumTabsActivity.selectedfragment) {
                case 0:
                    if (AlbumTab_Tracks_Activity.progBar != null && AlbumTab_Tracks_Activity.audioRelative != null) {
                        AlbumTab_Tracks_Activity.progBar.setVisibility(View.GONE);

                        AlbumTab_Tracks_Activity.audioRelative.setVisibility(View.VISIBLE);
                        AlbumTab_Tracks_Activity.audioRelative.setEnabled(true);
                        AlbumTab_Tracks_Activity.selectessong = PlayerConstants.SONG_NUMBER;}
                        if(AlbumTab_Tracks_Activity.myTrackadAdaptor!=null)
                        AlbumTab_Tracks_Activity.myTrackadAdaptor.notifyDataSetInvalidated();

                    break;
                case 1:
                    if (AlbumTab_Otheralbum_Activity.progBar != null && AlbumTab_Otheralbum_Activity.audioRelative != null) {

                        AlbumTab_Otheralbum_Activity.progBar.setVisibility(View.GONE);

                        AlbumTab_Otheralbum_Activity.audioRelative.setVisibility(View.VISIBLE);
                        AlbumTab_Otheralbum_Activity.audioRelative.setEnabled(true);
                    }

                    break;

                case 2:
                    if (AlbumTab_Relatedalbum_Activity.progBar != null && AlbumTab_Relatedalbum_Activity.audioRelative != null) {

                        AlbumTab_Relatedalbum_Activity.progBar.setVisibility(View.GONE);

                        AlbumTab_Relatedalbum_Activity.audioRelative.setVisibility(View.VISIBLE);
                        AlbumTab_Relatedalbum_Activity.audioRelative.setEnabled(true);
                    }
                    break;

            }
        }


        if (mpackagename
                .equalsIgnoreCase("FavoritesRadio")) {
            if (FavoritesRadio.progBar != null && FavoritesRadio.audioRelative != null) {

                FavoritesRadio.progBar.setVisibility(View.GONE);

                FavoritesRadio.audioRelative.setVisibility(View.VISIBLE);
                FavoritesRadio.audioRelative.setEnabled(true);
            }
        }
        if (mpackagename
                .equalsIgnoreCase("ArtistTabs.ArtistTabsActivity")) {
            switch (ArtistTabsActivity.selectedfragment) {
                case 0:
if(ArtistTab_TopActivity.progBar!=null)
                        ArtistTab_TopActivity.progBar.setVisibility(View.GONE);
                    if(ArtistTab_TopActivity.audioRelative!=null) {

                        ArtistTab_TopActivity.audioRelative.setVisibility(View.VISIBLE);
                        ArtistTab_TopActivity.audioRelative.setEnabled(true);
                    }
                    if(ArtistTab_TopActivity.myTrackadAdaptor!=null)

                        ArtistTab_TopActivity.myTrackadAdaptor.notifyDataSetInvalidated();

                    break;
                case 1:

                    if (ArtistTab_AlbumsActivity.progBar != null && ArtistTab_AlbumsActivity.audioRelative != null) {

                        ArtistTab_AlbumsActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_AlbumsActivity.audioRelative.setVisibility(View.VISIBLE);
                        ArtistTab_AlbumsActivity.audioRelative.setEnabled(true);
                    }
                    break;
                case 2:
                    if (ArtistTab_SimilarActivity.progBar != null && ArtistTab_SimilarActivity.audioRelative != null) {

                        ArtistTab_SimilarActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_SimilarActivity.audioRelative.setVisibility(View.VISIBLE);
                        ArtistTab_SimilarActivity.audioRelative.setEnabled(true);
                    }
                    break;

                case 3:
                    if (ArtistTab_SingleActivity.progBar != null && ArtistTab_SingleActivity.audioRelative != null) {

                        ArtistTab_SingleActivity.progBar.setVisibility(View.GONE);

                        ArtistTab_SingleActivity.audioRelative.setVisibility(View.VISIBLE);
                        ArtistTab_SingleActivity.audioRelative.setEnabled(true);}
                    if (ArtistTab_SingleActivity.myTrackadAdaptor != null)

                    ArtistTab_SingleActivity.myTrackadAdaptor.notifyDataSetInvalidated();

                    break;
            }
        }


        if (mpackagename
                .equalsIgnoreCase("PlayerAcreenActivity")) {
            if (PlayerAcreenActivity.progBar != null && PlayerAcreenActivity.next != null && PlayerAcreenActivity.prev != null) {
                PlayerAcreenActivity.progBar.setVisibility(View.GONE);
                PlayerAcreenActivity.next.setEnabled(true);
                PlayerAcreenActivity.prev.setEnabled(true);
                PlayerAcreenActivity.next.setClickable(true);
                PlayerAcreenActivity.prev.setClickable(true);
            }
        }
        if (mpackagename
                .equalsIgnoreCase("HomeActivity")) {
            if(HomeActivity.progBar!=null)
                HomeActivity.progBar.setVisibility(View.GONE);
            if(HomeActivity.audioRelative!=null)
                HomeActivity.audioRelative.setVisibility(View.VISIBLE);

            HomeActivity.audioRelative.setEnabled(true);
            if (HomeActivity.ListTrackadAdaptor != null && HomeActivity.selectessong != -1) {
                HomeActivity.selectessong = PlayerConstants.SONG_NUMBER;

                HomeActivity.ListTrackadAdaptor.notifyDataSetInvalidated();
            }
            if (HomeActivity.topTwentyAdaptor != null && HomeActivity.selecttopsong != -1) {
                HomeActivity.selecttopsong = PlayerConstants.SONG_NUMBER;


                HomeActivity.topTwentyAdaptor.notifyDataSetInvalidated();
            }
        }}
}
