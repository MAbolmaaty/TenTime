package com.binarywaves.ghaneely.ghannelymodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Radio {

    @JsonProperty
    private String RadioId;
    @JsonProperty

    private String RadioName;
    @JsonProperty

    private String RadioArName;
    @JsonProperty

    private String RadioURL;
    @JsonProperty


    private String RadioImage;

    public String getRadioArName() {
        return RadioArName;
    }

    public void setRadioArName(String radioArName) {
        RadioArName = radioArName;
    }

    public String getRadioImage() {
        return RadioImage;
    }

    public void setRadioImage(String radioImage) {
        RadioImage = radioImage;
    }

    public String getRadioId() {
        return RadioId;
    }

    public void setRadioId(String radioId) {
        RadioId = radioId;
    }

    public String getRadioName() {
        return RadioName;
    }

    public void setRadioName(String radioName) {
        RadioName = radioName;
    }

    public String getRadioURL() {
        return RadioURL;
    }

    public void setRadioURL(String radioURL) {
        RadioURL = radioURL;
    }


}
