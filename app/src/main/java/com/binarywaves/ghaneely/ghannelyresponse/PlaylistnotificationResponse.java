package com.binarywaves.ghaneely.ghannelyresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

@JsonSerialize

public class PlaylistnotificationResponse implements Parcelable {
    public static final Parcelable.Creator<PlaylistnotificationResponse> CREATOR = new Parcelable.Creator<PlaylistnotificationResponse>() {

        @Override
        public PlaylistnotificationResponse createFromParcel(Parcel source) {

            return new PlaylistnotificationResponse(source);
        }

        @Override
        public PlaylistnotificationResponse[] newArray(int size) {
            // TODO Auto-generated method stub
            return new PlaylistnotificationResponse[size];
        }
    };
    @JsonProperty("PlaylstTracks")

    private ArrayList<Similartrackstripresponse> PlaylstTracks;
    @JsonProperty
    private String PlaylistArDesc;
    @JsonProperty
    private String PlaylistArName;
    @JsonProperty
    private String PlaylistEnDesc;
    @JsonProperty
    private String PlaylistId;
    @JsonProperty
    private String PlaylistImgPath;
    @JsonProperty
    private String PlaylistName;

    public PlaylistnotificationResponse() {
        super();
    }

    private PlaylistnotificationResponse(Parcel source) {
        PlaylistId = source.readString();
        PlaylistName = source.readString();
        PlaylistImgPath = source.readString();


    }

    public ArrayList<Similartrackstripresponse> getPlaylstTracks() {
        return PlaylstTracks;
    }

    public void setPlaylstTracks(ArrayList<Similartrackstripresponse> playlstTracks) {
        PlaylstTracks = playlstTracks;
    }

    public String getPlaylistArDesc() {
        return PlaylistArDesc;
    }

    public void setPlaylistArDesc(String playlistArDesc) {
        PlaylistArDesc = playlistArDesc;
    }

    public String getPlaylistArName() {
        return PlaylistArName;
    }

    public void setPlaylistArName(String playlistArName) {
        PlaylistArName = playlistArName;
    }

    public String getPlaylistEnDesc() {
        return PlaylistEnDesc;
    }

    public void setPlaylistEnDesc(String playlistEnDesc) {
        PlaylistEnDesc = playlistEnDesc;
    }

    public String getPlaylistId() {
        return PlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        PlaylistId = playlistId;
    }

    public String getPlaylistImgPath() {
        return PlaylistImgPath;
    }

    public void setPlaylistImgPath(String playlistImgPath) {
        PlaylistImgPath = playlistImgPath;
    }

    public String getPlaylistName() {
        return PlaylistName;
    }

    public void setPlaylistName(String playlistName) {
        PlaylistName = playlistName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(PlaylistId);
        dest.writeString(PlaylistName);

        dest.writeString(PlaylistImgPath);


    }
}
