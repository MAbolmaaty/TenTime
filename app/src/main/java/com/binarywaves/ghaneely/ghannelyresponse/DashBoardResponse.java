package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Amany on 17-Oct-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DashBoardResponse {
    @JsonProperty
    private String ContentID;
    @JsonProperty
    private String ContentTypeID;

    @JsonProperty
    private String SliderEnName;
    @JsonProperty
    private String SliderArName;
    @JsonProperty
    private String SliderImage;
    @JsonProperty
    private String SliderArImage;

    public String getIsPremium() {
        return IsPremium;
    }

    public void setIsPremium(String isPremium) {
        IsPremium = isPremium;
    }

    @JsonProperty
    private String IsPremium;

    public String getSliderArImage() {
        return SliderArImage;
    }

    public void setSliderArImage(String sliderArImage) {
        SliderArImage = sliderArImage;
    }

    public String getContentID() {
        return ContentID;
    }

    public void setContentID(String contentID) {
        ContentID = contentID;
    }

    public String getContentTypeID() {
        return ContentTypeID;
    }

    public void setContentTypeID(String contentTypeID) {
        ContentTypeID = contentTypeID;
    }

    public String getSliderEnName() {
        return SliderEnName;
    }

    public void setSliderEnName(String sliderEnName) {
        SliderEnName = sliderEnName;
    }

    public String getSliderArName() {
        return SliderArName;
    }

    public void setSliderArName(String sliderArName) {
        SliderArName = sliderArName;
    }

    public String getSliderImage() {
        return SliderImage;
    }

    public void setSliderImage(String sliderImage) {
        SliderImage = sliderImage;
    }


}

