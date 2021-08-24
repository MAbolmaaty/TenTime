package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class Similartrackstripresponse {
    @JsonProperty
    private String IsPremium;
    @JsonProperty
    private String IsDownloaded;
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
    private String AlbumImgPath;

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
    private String IsRBT;
    @JsonProperty
    private String RecommendedText;
    @JsonProperty
    private String TrackLength;
    @JsonProperty
    private String LikesCount;
    @JsonProperty
    private String TrackRating;
    @JsonProperty
    private String RecommendedArText;
    @JsonProperty
    private String ListenCount;
    @JsonProperty
    private String LyricFile;
    @JsonProperty
    private String HasLyrics;
    @JsonProperty

    private String VideoID;

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
        SingerId = singerId;
    }

    public String getTrackImage() {
        return TrackImage;
    }

    public void setTrackImage(String trackImage) {
        TrackImage = trackImage;
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

    public String getListenCount() {
        return ListenCount;
    }

    public void setListenCount(String listenCount) {
        ListenCount = listenCount;
    }

    public String getTrackId() {
        return TrackId;
    }

    public void setTrackId(String trackId) {
        TrackId = trackId;
    }

    public String getRecommendedArText() {
        return RecommendedArText;
    }

    public void setRecommendedArText(String recommendedArText) {
        RecommendedArText = recommendedArText;
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

    public String getAlbumImgPath() {
        return AlbumImgPath;
    }

    public void setAlbumImgPath(String albumImgPath) {
        AlbumImgPath = albumImgPath;
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

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
    }

    public String getIsRBT() {
        return IsRBT;
    }

    public void setIsRBT(String isRBT) {
        IsRBT = isRBT;
    }

    public String getRecommendedText() {
        return RecommendedText;
    }

    public void setRecommendedText(String recommendedText) {
        RecommendedText = recommendedText;
    }

    public String getTrackLength() {
        return TrackLength;
    }

    public void setTrackLength(String trackLength) {
        TrackLength = trackLength;
    }

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
    }

    public String getTrackRating() {
        return TrackRating;
    }

    public void setTrackRating(String trackRating) {
        TrackRating = trackRating;
    }


}
