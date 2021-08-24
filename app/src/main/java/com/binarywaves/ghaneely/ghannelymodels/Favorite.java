package com.binarywaves.ghaneely.ghannelymodels;

import java.util.ArrayList;

public class Favorite {
    //{"FavouriteLstId":2,"FavouriteLstName":"","FavouriteLstImgPath":"","FavouriteLstTracks":[
    private String FavouriteLstId;
    private String FavouriteLstName;
    private String FavouriteLstImgPath;
    private ArrayList<TrackObject> mfavoritesTrackList;

    public String getFavouriteLstId() {
        return FavouriteLstId;
    }

    public void setFavouriteLstId(String favouriteLstId) {
        FavouriteLstId = favouriteLstId;
    }

    public String getFavouriteLstName() {
        return FavouriteLstName;
    }

    public void setFavouriteLstName(String favouriteLstName) {
        FavouriteLstName = favouriteLstName;
    }

    public String getFavouriteLstImgPath() {
        return FavouriteLstImgPath;
    }

    public void setFavouriteLstImgPath(String favouriteLstImgPath) {
        FavouriteLstImgPath = favouriteLstImgPath;
    }

    public ArrayList<TrackObject> getMfavoritesTrackList() {
        return mfavoritesTrackList;
    }

    public void setMfavoritesTrackList(ArrayList<TrackObject> mfavoritesTrackList) {
        this.mfavoritesTrackList = mfavoritesTrackList;
    }


}
