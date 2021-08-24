package com.binarywaves.ghaneely.ghannelyresponse;

import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Amany on 09-Oct-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class GetUserDownloadedLst {
    @JsonProperty("TracksLst")
    private ArrayList<TrackObject> TracksLst;
    @JsonProperty("VideosLst")
    private ArrayList<VideoObjectResponse> VideosLst;

    public ArrayList<TrackObject> getTracksLst() {
        return TracksLst;
    }

    public void setTracksLst(ArrayList<TrackObject> tracksLst) {
        TracksLst = tracksLst;
    }

    public ArrayList<VideoObjectResponse> getVideosLst() {
        return VideosLst;
    }

    public void setVideosLst(ArrayList<VideoObjectResponse> videosLst) {
        VideosLst = videosLst;
    }
}
