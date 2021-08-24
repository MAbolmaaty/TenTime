package com.binarywaves.ghaneely.ghannelyresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlistlst implements Parcelable {
    public static final Parcelable.Creator<Playlistlst> CREATOR = new Parcelable.Creator<Playlistlst>() {

        @Override
        public Playlistlst createFromParcel(Parcel source) {

            return new Playlistlst(source);
        }

        @Override
        public Playlistlst[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Playlistlst[size];
        }
    };
    @JsonProperty
    private String UserPlaylistId;
    @JsonProperty
    private String UserPlaylistName;
    @JsonProperty
    private String UserPlaylistImgPath;

    public Playlistlst() {
        super();
    }

    private Playlistlst(Parcel source) {
        UserPlaylistId = source.readString();
        UserPlaylistName = source.readString();
        UserPlaylistImgPath = source.readString();


    }

    public String getPlaylistId() {
        return UserPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        UserPlaylistId = playlistId;
    }

    public String getPlaylistName() {
        return UserPlaylistName;
    }

    public void setPlaylistName(String playlistName) {
        UserPlaylistName = playlistName;
    }

    public String getPlaylistImgPath() {
        return UserPlaylistImgPath;
    }

    public void setPlaylistImgPath(String playlistImgPath) {
        UserPlaylistImgPath = playlistImgPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(UserPlaylistId);
        dest.writeString(UserPlaylistName);

        dest.writeString(UserPlaylistImgPath);


    }
}
