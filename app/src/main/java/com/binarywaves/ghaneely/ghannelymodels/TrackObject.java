package com.binarywaves.ghaneely.ghannelymodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class TrackObject implements Parcelable {
    public static final Parcelable.Creator<TrackObject> CREATOR = new Parcelable.Creator<TrackObject>() {

        @Override
        public TrackObject createFromParcel(Parcel source) {

            return new TrackObject(source);
        }

        @Override
        public TrackObject[] newArray(int size) {
            // TODO Auto-generated method stub
            return new TrackObject[size];
        }
    };
    //	TrackLst":[{"TrackId":4298,"TrackEnName":"Shoft El Ayam","TrackArName":"","TrackPath":"26602_Shoft El Ayam_Ringtone.mp3","IsFavourite":false,"AlbumId":844,"SingerEnName":"Amr Diab","SingerArName":"Ø¹Ù…Ø±Ùˆ Ø¯ÙŠØ§Ø¨","AlbumEnName":"Shoft El Ayam","AlbumArName":"Ø´ÙØª Ø§Ù„Ø§ÙŠØ§Ù…","AlbumImgPath":"848.jpg"},
    @JsonProperty
    public String TrackId;
    @JsonProperty

    public String TrackEnName;
    @JsonProperty

    public String TrackArName;
    @JsonProperty

    public String TrackPath;
    @JsonProperty

    public String IsFavourite;
    @JsonProperty

    public String AlbumId;
    @JsonProperty

    public String SingerEnName;
    @JsonProperty

    public String SingerArName;
    @JsonProperty

    public String AlbumEnName;
    @JsonProperty

    public String AlbumArName;
    @JsonProperty

    public String IsRBT;
    @JsonProperty

    public String SingerId;
    @JsonProperty

    public String LikesCount;
    @JsonProperty

    public String ListenCount;
    @JsonProperty

    public String TrackImage;
    @JsonProperty


    public String TrackLength;
    @JsonProperty
    public String IsDownloaded;
    @JsonProperty
    public String IsPremium;
    @JsonProperty

    private String HasLyrics;
    @JsonProperty

    private String LyricFile;
    @JsonProperty

    private String SingerImg;
    @JsonProperty


    private String VideoID;

    public TrackObject() {
        super();
    }

    public TrackObject(Parcel source) {
        TrackId = source.readString();
        TrackEnName = source.readString();
        TrackArName = source.readString();
        TrackPath = source.readString();
        IsFavourite = source.readString();


        AlbumId = source.readString();
        SingerEnName = source.readString();
        SingerArName = source.readString();
        AlbumEnName = source.readString();
        AlbumArName = source.readString();

        IsRBT = source.readString();
        SingerId = source.readString();
        LikesCount = source.readString();
        ListenCount = source.readString();
        TrackImage = source.readString();

        SingerImg = source.readString();
        TrackLength = source.readString();

        HasLyrics = source.readString();

        LyricFile = source.readString();

        VideoID = source.readString();
        IsPremium = source.readString();
        IsDownloaded = source.readString();

    }

    public String getIsDownloaded() {
        return IsDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        IsDownloaded = isDownloaded;
    }

    public String getIsPremium() {
        return IsPremium;
    }

    public void setIsPremium(String isPremium) {
        IsPremium = isPremium;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getHasLyrics() {
        return HasLyrics;
    }

    public void setHasLyrics(String hasLyrics) {
        HasLyrics = hasLyrics;
    }

    public String getLyricFile() {

        return LyricFile;
    }

    public void setLyricFile(String lyricFile) {
        LyricFile = lyricFile;
    }

    public String getTrackLength() {
        return TrackLength;
    }

    public void setTrackLength(String trackLength) {
        TrackLength = trackLength;
    }

    public String getTrackImage() {
        return TrackImage;
    }

    public void setTrackImage(String trackImage) {
        TrackImage = trackImage;
    }

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
    }

    public String getListenCount() {
        return ListenCount;
    }

    public void setListenCount(String listenCount) {
        ListenCount = listenCount;
    }

    public String getSingerImg() {
        return SingerImg;
    }

    public void setSingerImg(String singerImg) {
        SingerImg = singerImg;
    }

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
        SingerId = singerId;
    }

    public String getTrackId() {
        return TrackId;
    }

    public void setTrackId(String trackId) {
        TrackId = trackId;
    }

    public String getTrackEnName() {
        return TrackEnName;
    }

    public void setTrackEnName(String trackEnName) {
        TrackEnName = trackEnName;
    }

    public String getTrackArName() {
        return TrackArName;
    }

    public void setTrackArName(String trackArName) {
        TrackArName = trackArName;
    }

    public String getTrackPath() {
        return TrackPath;
    }

    public void setTrackPath(String trackPath) {
        TrackPath = trackPath;
    }

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
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

    //rackId":4298,"TrackEnName":"Shoft El Ayam","TrackArName":"","TrackPath":"26602_Shoft El Ayam_Ringtone.mp3","IsFavourite":false,"AlbumId":844,"SingerEnName":"Amr Diab","SingerArName":"Ø¹Ù…Ø±Ùˆ Ø¯ÙŠØ§Ø¨","AlbumEnName":"Shoft El Ayam","AlbumArName":"Ø´ÙØª Ø§Ù„Ø§ÙŠØ§Ù…","AlbumImgPath":"848.jpg"
    public String getIsRBT() {
        return IsRBT;
    }

    public void setIsRBT(String isRBT) {
        IsRBT = isRBT;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        dest.writeString(TrackId);
        dest.writeString(TrackEnName);
        dest.writeString(TrackArName);
        dest.writeString(TrackPath);
        dest.writeString(IsFavourite);

        dest.writeString(AlbumId);
        dest.writeString(SingerEnName);
        dest.writeString(SingerArName);
        dest.writeString(AlbumEnName);
        dest.writeString(AlbumArName);
        dest.writeString(IsRBT);
        dest.writeString(SingerId);
        dest.writeString(LikesCount);
        dest.writeString(ListenCount);
        dest.writeString(TrackImage);
        dest.writeString(SingerImg);
        dest.writeString(TrackLength);
        dest.writeString(HasLyrics);
        dest.writeString(LyricFile);
        dest.writeString(VideoID);
        dest.writeString(IsPremium);
        dest.writeString(IsDownloaded);


    }
}
