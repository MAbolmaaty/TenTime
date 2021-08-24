package com.binarywaves.ghaneely.ghannelyresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumLst implements Parcelable {
    public static final Parcelable.Creator<AlbumLst> CREATOR = new Parcelable.Creator<AlbumLst>() {

        @Override
        public AlbumLst createFromParcel(Parcel source) {

            return new AlbumLst(source);
        }

        @Override
        public AlbumLst[] newArray(int size) {
            // TODO Auto-generated method stub
            return new AlbumLst[size];
        }
    };
    @JsonProperty
    private String AlbumId;
    @JsonProperty
    private String AlbumArName;
    @JsonProperty
    private String AlbumEnName;
    @JsonProperty
    private String AlbumImgPath;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String SingerEnName;

    public AlbumLst() {
        super();
    }

    private AlbumLst(Parcel source) {
        AlbumId = source.readString();
        AlbumArName = source.readString();
        AlbumEnName = source.readString();
        AlbumImgPath = source.readString();
        SingerArName = source.readString();
        SingerEnName = source.readString();


    }

    public String getAlbumImgPath() {
        return AlbumImgPath;
    }

    public void setAlbumImgPath(String albumImgPath) {
        AlbumImgPath = albumImgPath;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public String getAlbumArName() {
        return AlbumArName;
    }

    public void setAlbumArName(String albumArName) {
        AlbumArName = albumArName;
    }

    public String getAlbumEnName() {
        return AlbumEnName;
    }

    public void setAlbumEnName(String albumEnName) {
        AlbumEnName = albumEnName;
    }

    public String getSingerArName() {
        return SingerArName;
    }

    public void setSingerArName(String singerArName) {
        SingerArName = singerArName;
    }

    public String getSingerEnName() {
        return SingerEnName;
    }

    public void setSingerEnName(String singerEnName) {
        SingerEnName = singerEnName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(AlbumId);
        dest.writeString(AlbumArName);

        dest.writeString(AlbumEnName);

        dest.writeString(AlbumImgPath);

        dest.writeString(SingerArName);
        dest.writeString(SingerEnName);


    }

}
