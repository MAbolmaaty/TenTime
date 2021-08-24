package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class FavouriteLstTracks {
    @JsonProperty
    private int TrackId;

    @JsonProperty
    private String AlbumArName;
    @JsonProperty
    private String AlbumEnName;
    @JsonProperty
    private String AlbumId;
    @JsonProperty
    private String AlbumImgPath;
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
    private Boolean IsFavourite;

    public Boolean getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        IsFavourite = isFavourite;
    }

    public int getTrackId() {
        return TrackId;
    }

    public void setTrackId(int trackId) {
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
}
