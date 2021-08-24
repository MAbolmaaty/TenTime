package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)

public class GetSuggestedTrackLstsResponse {
    @JsonProperty("ListenTracks")

    private ArrayList<TrackLst> recentplayed;

    @JsonProperty("LikesTracks")
    private ArrayList<TrackLst> likes;
    @JsonProperty("TopTracks")
    private ArrayList<TrackLst> featured;

    public ArrayList<TrackLst> getRecentplayed() {
        return recentplayed;
    }

    public void setRecentplayed(ArrayList<TrackLst> recentplayed) {
        this.recentplayed = recentplayed;
    }

    public ArrayList<TrackLst> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<TrackLst> likes) {
        this.likes = likes;
    }

    public ArrayList<TrackLst> getFeatured() {
        return featured;
    }

    public void setFeatured(ArrayList<TrackLst> featured) {
        this.featured = featured;
    }
}
