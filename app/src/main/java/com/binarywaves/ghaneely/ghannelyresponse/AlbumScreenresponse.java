package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumScreenresponse {
    @JsonProperty("OtherAlbums")

    private ArrayList<AlbumLst> otheralbumLst;

    @JsonProperty("SimilarAlbums")
    private ArrayList<AlbumLst> similaralbum;
    @JsonProperty
    private String SingerEnName;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String TrackImage;
    @JsonProperty
    private String AlbumImgPath;
    @JsonProperty
    private String AlbumEnName;
    @JsonProperty
    private String TrackLength;
    @JsonProperty
    private String AlbumArName;
    @JsonProperty
    private String AlbumId;
    @JsonProperty("AlbumTracks")
    private ArrayList<TrackLst> trackLst;

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

    public String getTrackImage() {
        return TrackImage;
    }

    public void setTrackImage(String trackImage) {
        TrackImage = trackImage;
    }

    public ArrayList<AlbumLst> getOtheralbumLst() {
        return otheralbumLst;
    }

    public void setOtheralbumLst(ArrayList<AlbumLst> otheralbumLst) {
        this.otheralbumLst = otheralbumLst;
    }

    public String getTrackLength() {
        return TrackLength;
    }

    public void setTrackLength(String trackLength) {
        TrackLength = trackLength;
    }

    public ArrayList<AlbumLst> getSimilaralbum() {
        return similaralbum;
    }

    public void setSimilaralbum(ArrayList<AlbumLst> similaralbum) {
        this.similaralbum = similaralbum;
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

    public ArrayList<AlbumLst> getotherAlbumLst() {
        return otheralbumLst;
    }

    public void setotherAlbumLst(ArrayList<AlbumLst> albumLst) {
        this.otheralbumLst = albumLst;
    }

    public ArrayList<AlbumLst> getsimilarLst() {
        return similaralbum;
    }

    public void setSimilarLst(ArrayList<AlbumLst> singerLst) {
        this.similaralbum = singerLst;
    }

    public ArrayList<TrackLst> getTrackLst() {
        return trackLst;
    }

    public void setTrackLst(ArrayList<TrackLst> trackLst) {
        this.trackLst = trackLst;
    }


}
