package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class SetTrackListenedresponse {
    @JsonProperty
    private boolean resultCode;

    public boolean isResultCode() {
        return resultCode;
    }

    public void setResultCode(boolean resultCode) {
        this.resultCode = resultCode;
    }
}
