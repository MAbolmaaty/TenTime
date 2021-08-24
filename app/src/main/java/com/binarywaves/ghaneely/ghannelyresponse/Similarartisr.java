package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Similarartisr {

    @JsonProperty("SingerAlbums")

    private ArrayList<AlbumLst> albumLst;


    @JsonProperty("SingerTracks")
    private ArrayList<TrackLst> trackLst;
    @JsonProperty
    private int SingerId;

    @JsonProperty
    private String SingerEnName;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String NationalityEnName;
    @JsonProperty
    private String NationalityArName;
    @JsonProperty
    private String SingerImgPath;

    public ArrayList<AlbumLst> getAlbumLst() {
        return albumLst;
    }

    public void setAlbumLst(ArrayList<AlbumLst> albumLst) {
        this.albumLst = albumLst;
    }

    public ArrayList<TrackLst> getTrackLst() {
        return trackLst;
    }

    public void setTrackLst(ArrayList<TrackLst> trackLst) {
        this.trackLst = trackLst;
    }

    public int getSingerId() {
        return SingerId;
    }

    public void setSingerId(int singerId) {
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

    public String getNationalityEnName() {
        return NationalityEnName;
    }

    public void setNationalityEnName(String nationalityEnName) {
        NationalityEnName = nationalityEnName;
    }

    public String getNationalityArName() {
        return NationalityArName;
    }

    public void setNationalityArName(String nationalityArName) {
        NationalityArName = nationalityArName;
    }

    public String getSingerImgPath() {
        return SingerImgPath;
    }

    public void setSingerImgPath(String singerImgPath) {
        SingerImgPath = singerImgPath;
    }

}

