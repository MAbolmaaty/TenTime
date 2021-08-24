package com.binarywaves.ghaneely.ghannelyresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Amany on 22-Jun-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class VideoObjectResponse implements Parcelable {
    public static final Parcelable.Creator<VideoObjectResponse> CREATOR = new Parcelable.Creator<VideoObjectResponse>() {

        @Override
        public VideoObjectResponse createFromParcel(Parcel source) {

            return new VideoObjectResponse(source);
        }

        @Override
        public VideoObjectResponse[] newArray(int size) {
            // TODO Auto-generated method stub
            return new VideoObjectResponse[size];
        }
    };
    @JsonProperty
    public String IsPremium;
    @JsonProperty
    public String IsDownloaded;
    @JsonProperty
    private String VideoID;
    @JsonProperty
    private String VideoEnName;
    @JsonProperty
    private String VideoArName;
    @JsonProperty
    private String VideoPath;
    @JsonProperty
    private String VideoPoster;
    @JsonProperty
    private String SingerId;
    @JsonProperty
    private String SingerEnName;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String LikesCount;
    @JsonProperty
    private String ViewCount;
    @JsonProperty
    private String IsFavourite;

    protected VideoObjectResponse(Parcel in) {
        VideoID = in.readString();
        VideoEnName = in.readString();
        VideoArName = in.readString();
        VideoPath = in.readString();
        VideoPoster = in.readString();
        SingerId = in.readString();
        SingerEnName = in.readString();
        SingerArName = in.readString();
        LikesCount = in.readString();
        ViewCount = in.readString();
        IsFavourite = in.readString();
        IsPremium = in.readString();
        IsDownloaded = in.readString();


    }


    public VideoObjectResponse() {
        super();
    }

    public VideoObjectResponse(String videoID, String videoEnName, String videoArName, String singerEnName, String singerArName, byte[] trackImage) {
        VideoID = videoID;
        VideoEnName = videoEnName;
        VideoArName = videoArName;
        SingerEnName = singerEnName;
        SingerArName = singerArName;
    }

    public String getIsPremium() {
        return IsPremium;
    }

    public void setIsPremium(String isPremium) {
        IsPremium = isPremium;
    }

    public String getIsDownloaded() {
        return IsDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        IsDownloaded = isDownloaded;
    }

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getVideoEnName() {
        return VideoEnName;
    }

    public void setVideoEnName(String videoEnName) {
        VideoEnName = videoEnName;
    }

    public String getVideoArName() {
        return VideoArName;
    }

    public void setVideoArName(String videoArName) {
        VideoArName = videoArName;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }

    public String getVideoPoster() {
        return VideoPoster;
    }

    public void setVideoPoster(String videoPoster) {
        VideoPoster = videoPoster;
    }

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
        SingerId = singerId;
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

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(VideoID);
        parcel.writeString(VideoEnName);
        parcel.writeString(VideoArName);
        parcel.writeString(VideoPath);
        parcel.writeString(VideoPoster);
        parcel.writeString(SingerId);
        parcel.writeString(SingerEnName);
        parcel.writeString(SingerArName);
        parcel.writeString(LikesCount);
        parcel.writeString(ViewCount);
        parcel.writeString(IsFavourite);
        parcel.writeString(IsPremium);
        parcel.writeString(IsDownloaded);

    }
}