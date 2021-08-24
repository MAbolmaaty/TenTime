package com.binarywaves.ghaneely.ghannelyresponse;

import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
    @JsonProperty
    private String UserSearchID;
    @JsonProperty("AlbumLst")

    private ArrayList<AlbumLst> albumLst;
    @JsonProperty("SingerLst")
    private ArrayList<ArtistRadio> singerLst;
    @JsonProperty("TrackLst")
    private ArrayList<TrackLst> trackLst;
    @JsonProperty("Playlsts")
    private ArrayList<PlaylistnotificationResponse> Playlsts;

    public ArrayList<VideoObjectResponse> getVideoLsts() {
        return VideoLsts;
    }

    public void setVideoLsts(ArrayList<VideoObjectResponse> videoLsts) {
        VideoLsts = videoLsts;
    }

    @JsonProperty("VideoLsts")
    private ArrayList<VideoObjectResponse> VideoLsts;

    public String getUsersearchID() {
        return UserSearchID;
    }

    public void setUsersearchID(String usersearchID) {
        UserSearchID = usersearchID;
    }

    public ArrayList<PlaylistnotificationResponse> getPlaylistLst() {
        return Playlsts;
    }

    public void setPlaylistLst(ArrayList<PlaylistnotificationResponse> playlistLst) {
        Playlsts = playlistLst;
    }

    public ArrayList<AlbumLst> getAlbumLst() {
        return albumLst;
    }

    public void setAlbumLst(ArrayList<AlbumLst> albumLst) {
        this.albumLst = albumLst;
    }

    public ArrayList<ArtistRadio> getSingerLst() {
        return singerLst;
    }

    public void setSingerLst(ArrayList<ArtistRadio> singerLst) {
        this.singerLst = singerLst;
    }

    public ArrayList<TrackLst> getTrackLst() {
        return trackLst;
    }

    public void setTrackLst(ArrayList<TrackLst> trackLst) {
        this.trackLst = trackLst;
    }

}
