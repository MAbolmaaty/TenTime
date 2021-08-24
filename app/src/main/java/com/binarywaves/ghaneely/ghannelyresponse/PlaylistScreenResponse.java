package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Amany on 10-May-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class PlaylistScreenResponse {
    @JsonProperty("AutoPlaylists")
    private ArrayList<PlaylistnotificationResponse> AutoPlaylists;


    @JsonProperty("UserPlaylists")
    private ArrayList<Playlistlst> UserPlaylists;

    public ArrayList<PlaylistnotificationResponse> getAutoPlaylists() {
        return AutoPlaylists;
    }

    public void setAutoPlaylists(ArrayList<PlaylistnotificationResponse> autoPlaylists) {
        AutoPlaylists = autoPlaylists;
    }

    public ArrayList<Playlistlst> getUserPlaylists() {
        return UserPlaylists;
    }

    public void setUserPlaylists(ArrayList<Playlistlst> userPlaylists) {
        UserPlaylists = userPlaylists;
    }
}
