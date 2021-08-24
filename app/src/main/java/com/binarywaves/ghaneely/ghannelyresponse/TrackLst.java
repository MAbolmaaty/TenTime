package com.binarywaves.ghaneely.ghannelyresponse;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackLst implements Parcelable {
    public static final Parcelable.Creator<TrackLst> CREATOR = new Parcelable.Creator<TrackLst>() {

        @Override
        public TrackLst createFromParcel(Parcel source) {

            return new TrackLst(source);
        }

        @Override
        public TrackLst[] newArray(int size) {
            // TODO Auto-generated method stub
            return new TrackLst[size];
        }
    };
    @JsonProperty
    public String IsDownloaded;
    @JsonProperty

    public String IsPremium;
    boolean selected = false;
    @JsonProperty
    private String TrackId;
    @JsonProperty
    private String SingerId;
    @JsonProperty
    private String AlbumArName;
    @JsonProperty
    private String AlbumEnName;
    @JsonProperty
    private String AlbumId;
    @JsonProperty
    private String TrackImage;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String SingerEnName;
    @JsonProperty
    private String TrackArName;
    @JsonProperty
    private String TrackEnName;
    @JsonProperty
    private String TrackPath;
    @JsonProperty
    private String IsFavourite;
    @JsonProperty
    private String LikesCount;
    @JsonProperty
    private String TrackLength;
    @JsonProperty
    private String ListenCount;
    @JsonProperty
    private String IsRBT;
    @JsonProperty
    private String LyricFile;
    @JsonProperty
    private String HasLyrics;

    @JsonProperty

    private String VideoID;

    public TrackLst(Context mcontext) {
        super();
    }

    public TrackLst(String TrackId, String TrackEnName, String TrackArName, String TrackPath, String IsFavourite, String AlbumId, String SingerEnName, String SingerArName, String AlbumEnName, String AlbumArName, String AlbumImgPath, String isRBT, String singerId
            , String likescount, String lisitencount, String trackimage, String SingerImg, String TrackLength, String hasVideo, String videoID, String ispremuim, String isDownloaded) {
        super();
        this.TrackId = TrackId;
        this.TrackEnName = TrackEnName;
        this.TrackArName = TrackArName;
        this.TrackPath = TrackPath;
        this.IsFavourite = IsFavourite;
        this.AlbumId = AlbumId;

        this.SingerEnName = SingerEnName;
        this.SingerArName = SingerArName;
        this.AlbumEnName = AlbumEnName;
        this.AlbumArName = AlbumArName;
        this.IsRBT = isRBT;
        this.SingerId = singerId;
        this.LikesCount = likescount;
        this.ListenCount = lisitencount;
        this.TrackImage = trackimage;

        this.VideoID = videoID;
        this.TrackLength = TrackLength;
        this.IsPremium = ispremuim;
        this.IsDownloaded = isDownloaded;

    }

    public TrackLst() {
        super();
    }

    public TrackLst(Parcel source) {
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

        TrackLength = source.readString();

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

    public String getLyricFile() {
        return LyricFile;
    }

    public void setLyricFile(String lyricFile) {
        LyricFile = lyricFile;
    }

    public String getHasLyrics() {
        return HasLyrics;
    }

    public void setHasLyrics(String hasLyrics) {
        HasLyrics = hasLyrics;
    }

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
        SingerId = singerId;
    }

    public String getListenCount() {
        return ListenCount;
    }

    public void setListenCount(String listenCount) {
        ListenCount = listenCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
    }

    public String getIsRBT() {
        return IsRBT;
    }

    public void setIsRBT(String isRBT) {
        IsRBT = isRBT;
    }

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
    }

    public String getTrackId() {
        return TrackId;
    }

    public void setTrackId(String trackId) {
        TrackId = trackId;
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

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public String getTrackImage() {
        return TrackImage;
    }

    public void setTrackImage(String trackImage) {
        TrackImage = trackImage;
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

    public String getTrackArName() {
        return TrackArName;
    }

    public void setTrackArName(String trackArName) {
        TrackArName = trackArName;
    }

    public String getTrackEnName() {
        return TrackEnName;
    }

    public void setTrackEnName(String trackEnName) {
        TrackEnName = trackEnName;
    }

    public String getTrackPath() {
        return TrackPath;
    }

    public void setTrackPath(String trackPath) {
        TrackPath = trackPath;
    }

    public String getTrackLength() {
        return TrackLength;
    }

    public void setTrackLength(String trackLength) {
        TrackLength = trackLength;
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
        dest.writeString(TrackLength);

        dest.writeString(VideoID);

        dest.writeString(IsPremium);
        dest.writeString(IsDownloaded);


    }
}
