package com.binarywaves.ghaneely.ghannelymodels;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class SlideAlbumObject implements Parcelable {
    public static final Parcelable.Creator<SlideAlbumObject> CREATOR = new Parcelable.Creator<SlideAlbumObject>() {

        @Override
        public SlideAlbumObject createFromParcel(Parcel source) {

            return new SlideAlbumObject(source);
        }

        @Override
        public SlideAlbumObject[] newArray(int size) {
            // TODO Auto-generated method stub
            return new SlideAlbumObject[size];
        }
    };
    // "AlbumId":809,"SingerEnName":null,"SingerArName":null,"AlbumEnName":"Fares Karam 2013","AlbumArName":"ÙØ§Ø±Ø³ ÙƒØ±Ù… 2013Â ","AlbumImgPath":"s_822.jpg"},
    @JsonProperty
    private String AlbumId;
    @JsonProperty
    private String SingerEnName;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String AlbumEnName;
    @JsonProperty
    private String AlbumArName;
    @JsonProperty
    private String AlbumImgPath;
    @JsonProperty
    private String SingerId;
    @JsonProperty
    private String AlbumARImgPath;
    @JsonProperty
    private String IsPremium;
    private Context mcontext;

    public SlideAlbumObject(Context mcontext) {
        super();
        this.mcontext = mcontext;
    }

    public SlideAlbumObject(String AlbumId, String SingerEnName, String SingerArName, String AlbumEnName, String AlbumArName, String AlbumImgPath, String SingerId, String trackimage, String AlbumARImgPath,String isPremium) {
        super();
        this.AlbumId = AlbumId;
        this.SingerEnName = SingerEnName;
        this.SingerArName = SingerArName;
        this.AlbumEnName = AlbumEnName;
        this.AlbumArName = SingerEnName;
        this.AlbumImgPath = SingerArName;
        this.SingerId = SingerId;
        this.AlbumARImgPath = AlbumARImgPath;
        this.IsPremium=isPremium;
    }

    public SlideAlbumObject() {
        super();
    }

    private SlideAlbumObject(Parcel source) {
        AlbumId = source.readString();
        SingerEnName = source.readString();
        SingerArName = source.readString();
        AlbumEnName = source.readString();
        AlbumArName = source.readString();
        AlbumImgPath = source.readString();
        SingerId = source.readString();
        AlbumARImgPath = source.readString();
        IsPremium = source.readString();


    }

    public String getAlbumARImgPath() {
        return AlbumARImgPath;
    }

    public void setAlbumARImgPath(String albumARImgPath) {
        AlbumARImgPath = albumARImgPath;
    }

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
        SingerId = singerId;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public String getSingerEnName() {
        return SingerEnName;
    }

    public void setSingerEnName(String singerEnName) {
        SingerEnName = singerEnName;
    }

    public String getSingerArName() {
        return SingerArName;
    }

    public void setSingerArName(String singerArName) {
        SingerArName = singerArName;
    }

    public String getAlbumEnName() {
        return AlbumEnName;
    }

    public void setAlbumEnName(String albumEnName) {
        AlbumEnName = albumEnName;
    }

    public String getAlbumArName() {
        return AlbumArName;
    }

    public void setAlbumArName(String albumArName) {
        AlbumArName = albumArName;
    }

    public String getAlbumImgPath() {
        return AlbumImgPath;
    }

    public void setAlbumImgPath(String albumImgPath) {
        AlbumImgPath = albumImgPath;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(AlbumId);
        dest.writeString(SingerEnName);
        dest.writeString(SingerArName);
        dest.writeString(AlbumEnName);
        dest.writeString(AlbumArName);
        dest.writeString(AlbumImgPath);
        dest.writeString(SingerId);
        dest.writeString(AlbumARImgPath);
        dest.writeString(IsPremium);


    }

    public String getIsPremium() {
        return IsPremium;
    }

    public void setIsPremium(String isPremium) {
        IsPremium = isPremium;
    }
}
