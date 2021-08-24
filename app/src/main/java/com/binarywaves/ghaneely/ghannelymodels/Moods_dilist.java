package com.binarywaves.ghaneely.ghannelymodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class Moods_dilist {
    @JsonProperty
    private String TrackId;
    @JsonProperty
    private String TrackEnName;
    @JsonProperty
    private String TrackArName;
    @JsonProperty
    private String TrackPath;
    @JsonProperty
    private String IsFavourite;
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
    private String TrackImage;
    @JsonProperty
    private String IsRBT;
    @JsonProperty
    private String TrackRating;
    @JsonProperty
    private String SingerId;
    @JsonProperty
    private String LikesCount;
    @JsonProperty
    private String SingerImg;

    @JsonProperty

    private String HasVideo;
    @JsonProperty

    private String VideoID;
    @JsonProperty
    private String TrackLength;
    @JsonProperty
    private String LyricFile;
    @JsonProperty
    private String HasLyrics;
    @JsonProperty
    private String ListenCount;

    public String getHasVideo() {
        return HasVideo;
    }

    public void setHasVideo(String hasVideo) {
        HasVideo = hasVideo;
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

    public String getTrackLength() {
        return TrackLength;
    }

    public void setTrackLength(String trackLength) {
        TrackLength = trackLength;
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

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
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


    public String getTrackImage() {
        return TrackImage;
    }

    public void setTrackImage(String trackImage) {
        TrackImage = trackImage;
    }

    public String getIsRBT() {
        return IsRBT;
    }

    public void setIsRBT(String isRBT) {
        IsRBT = isRBT;
    }

    public String getTrackRating() {
        return TrackRating;
    }

    public void setTrackRating(String trackRating) {
        TrackRating = trackRating;
    }

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
        SingerId = singerId;
    }


}
