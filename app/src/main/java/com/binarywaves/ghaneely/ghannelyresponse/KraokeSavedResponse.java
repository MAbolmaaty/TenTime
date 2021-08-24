package com.binarywaves.ghaneely.ghannelyresponse;

import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Amany on 24-Aug-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KraokeSavedResponse {
    @JsonProperty("SavedkaraokeTracks")

    private ArrayList<KarokeSavedObject> SavedkaraokeTracks;

    @JsonProperty("KaraokeTracks")

    private ArrayList<TrackObject> KaraokeTracks;

    public ArrayList<KarokeSavedObject> getSavedkaraokeTracks() {
        return SavedkaraokeTracks;
    }

    public void setSavedkaraokeTracks(ArrayList<KarokeSavedObject> savedkaraokeTracks) {
        SavedkaraokeTracks = savedkaraokeTracks;
    }

    public ArrayList<TrackObject> getKaraokeTracks() {
        return KaraokeTracks;
    }

    public void setKaraokeTracks(ArrayList<TrackObject> karaokeTracks) {
        KaraokeTracks = karaokeTracks;
    }


}