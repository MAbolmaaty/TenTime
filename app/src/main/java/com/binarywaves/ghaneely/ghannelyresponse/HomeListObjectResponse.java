package com.binarywaves.ghaneely.ghannelyresponse;

import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.MoodsList;
import com.binarywaves.ghaneely.ghannelymodels.Radio;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.SliderAds;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Amany on 16-Oct-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class HomeListObjectResponse {

    @JsonProperty("AdsFileLst")

    private ArrayList<SliderAds> AdsFileLst;

    @JsonProperty("PopupAdsFileLst")

    private ArrayList<SliderAds> PopupAdsFileLst;

    @JsonProperty("SliderLst")

    private ArrayList<DashBoardResponse> SliderLst;


    @JsonProperty("AlbumLst")

    private ArrayList<SlideAlbumObject> AlbumLst;

    @JsonProperty("TrackLst")

    private ArrayList<TrackObject> TrackLst;


    @JsonProperty("JustForUTrackLst")

    private ArrayList<TrackObject> JustForUTrackLst;

    @JsonProperty("NewTrackLst")

    private ArrayList<TrackObject> NewTrackLst;

    @JsonProperty("SingerRadioLst")

    private ArrayList<ArtistRadio> SingerRadioLst;


    @JsonProperty("OnlineRadioLst")

    private ArrayList<Radio> OnlineRadioLst;

    @JsonProperty("PersonalDJLst")

    private ArrayList<MoodsList> PersonalDJLst;


    @JsonProperty("TopVideoLst")

    private ArrayList<VideoObjectResponse> TopVideoLst;


    @JsonProperty
    private String AdFileName;
    @JsonProperty
    private String AccSettingNameEn;
    @JsonProperty
    private String AccSettingNameAr;

    @JsonProperty
    private String SliderAdEnFile;
    @JsonProperty
    private String SliderAdArFile;
    @JsonProperty
    private String PlaylistName;

    public ArrayList<ArtistRadio> getSingerRadioLst() {
        return SingerRadioLst;
    }

    public void setSingerRadioLst(ArrayList<ArtistRadio> singerRadioLst) {
        SingerRadioLst = singerRadioLst;
    }

    public ArrayList<SliderAds> getAdsFileLst() {
        return AdsFileLst;
    }

    public void setAdsFileLst(ArrayList<SliderAds> adsFileLst) {
        AdsFileLst = adsFileLst;
    }

    public ArrayList<SliderAds> getPopupAdsFileLst() {
        return PopupAdsFileLst;
    }

    public void setPopupAdsFileLst(ArrayList<SliderAds> popupAdsFileLst) {
        PopupAdsFileLst = popupAdsFileLst;
    }

    public ArrayList<DashBoardResponse> getSliderLst() {
        return SliderLst;
    }

    public void setSliderLst(ArrayList<DashBoardResponse> sliderLst) {
        SliderLst = sliderLst;
    }

    public ArrayList<SlideAlbumObject> getAlbumLst() {
        return AlbumLst;
    }

    public void setAlbumLst(ArrayList<SlideAlbumObject> albumLst) {
        AlbumLst = albumLst;
    }

    public ArrayList<TrackObject> getTrackLst() {
        return TrackLst;
    }

    public void setTrackLst(ArrayList<TrackObject> trackLst) {
        TrackLst = trackLst;
    }

    public ArrayList<TrackObject> getJustForUTrackLst() {
        return JustForUTrackLst;
    }

    public void setJustForUTrackLst(ArrayList<TrackObject> justForUTrackLst) {
        JustForUTrackLst = justForUTrackLst;
    }

    public ArrayList<TrackObject> getNewTrackLst() {
        return NewTrackLst;
    }

    public void setNewTrackLst(ArrayList<TrackObject> newTrackLst) {
        NewTrackLst = newTrackLst;
    }

    public ArrayList<Radio> getOnlineRadioLst() {
        return OnlineRadioLst;
    }

    public void setOnlineRadioLst(ArrayList<Radio> onlineRadioLst) {
        OnlineRadioLst = onlineRadioLst;
    }

    public ArrayList<MoodsList> getPersonalDJLst() {
        return PersonalDJLst;
    }

    public void setPersonalDJLst(ArrayList<MoodsList> personalDJLst) {
        PersonalDJLst = personalDJLst;
    }

    public ArrayList<VideoObjectResponse> getTopVideoLst() {
        return TopVideoLst;
    }

    public void setTopVideoLst(ArrayList<VideoObjectResponse> topVideoLst) {
        TopVideoLst = topVideoLst;
    }

    public String getAdFileName() {
        return AdFileName;
    }

    public void setAdFileName(String adFileName) {
        AdFileName = adFileName;
    }

    public String getAccSettingNameEn() {
        return AccSettingNameEn;
    }

    public void setAccSettingNameEn(String accSettingNameEn) {
        AccSettingNameEn = accSettingNameEn;
    }

    public String getAccSettingNameAr() {
        return AccSettingNameAr;
    }

    public void setAccSettingNameAr(String accSettingNameAr) {
        AccSettingNameAr = accSettingNameAr;
    }

    public String getSliderAdEnFile() {
        return SliderAdEnFile;
    }

    public void setSliderAdEnFile(String sliderAdEnFile) {
        SliderAdEnFile = sliderAdEnFile;
    }

    public String getSliderAdArFile() {
        return SliderAdArFile;
    }

    public void setSliderAdArFile(String sliderAdArFile) {
        SliderAdArFile = sliderAdArFile;
    }

    public String getPlaylistName() {
        return PlaylistName;
    }

    public void setPlaylistName(String playlistName) {
        PlaylistName = playlistName;
    }


}
