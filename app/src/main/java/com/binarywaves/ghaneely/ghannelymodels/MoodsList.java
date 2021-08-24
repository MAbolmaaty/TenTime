package com.binarywaves.ghaneely.ghannelymodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class MoodsList {
    @JsonProperty
    private String PersonalDJID;
    @JsonProperty
    private String PersonalDJName;
    @JsonProperty
    private String DJImage;
    @JsonProperty
    private String PersonalDJArName;


    public String getPersonalDJIDAr() {
        return PersonalDJArName;
    }

    public void setPersonalDJIDAr(String personalDJIDAr) {
        PersonalDJArName = personalDJIDAr;
    }

    public String getDjImage() {
        return DJImage;
    }

    public void setDjImage(String djImage) {
        DJImage = djImage;
    }

    public String getPersonalDJID() {
        return PersonalDJID;
    }

    public void setPersonalDJID(String personalDJID) {
        PersonalDJID = personalDJID;
    }

    public String getPersonalDJName() {
        return PersonalDJName;
    }

    public void setPersonalDJName(String personalDJName) {
        PersonalDJName = personalDJName;
    }


}
