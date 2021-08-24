package com.binarywaves.ghaneely.ghaneelysingleton;

import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;

import java.util.ArrayList;

/**
 * Created by Amany on 01-Feb-18.
 */

public class AppSingleTon {
    private static AppSingleTon mInstance = null;



    private ArrayList<TrackObject> recentplayed;
    private ArrayList<TrackObject> likes;

    public ArrayList<TrackObject> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<TrackObject> likes) {
        this.likes = likes;
    }

    public ArrayList<ArtistRadio> getFolloweredSingers() {
        return followeredSingers;
    }

    public void setFolloweredSingers(ArrayList<ArtistRadio> followeredSingers) {
        this.followeredSingers = followeredSingers;
    }

    private ArrayList<ArtistRadio> followeredSingers;


    private AppSingleTon(){
        recentplayed = new ArrayList<>();
        likes = new ArrayList<>();

        followeredSingers = new ArrayList<>();

    }

    public static AppSingleTon getInstance(){
        if(mInstance == null)
        {
            mInstance = new AppSingleTon();
        }
        return mInstance;
    }
    public ArrayList<TrackObject> getRecentplayed() {
        return recentplayed;
    }

    public void setRecentplayed(ArrayList<TrackObject> recentplayed) {
        this.recentplayed = recentplayed;
    }

}
