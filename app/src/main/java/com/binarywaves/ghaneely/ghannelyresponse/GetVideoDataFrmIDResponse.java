package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Amany on 04-Jul-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class GetVideoDataFrmIDResponse extends ArrayList<VideoObjectResponse> {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private ArrayList<VideoObjectResponse> InstitutionBasicInfo;

}
