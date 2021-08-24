package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Amany on 14-May-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRecentUserSearchesResponse extends ArrayList<GetRecentUserSearchesListResponse> {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private ArrayList<GetRecentUserSearchesListResponse> InstitutionBasicInfo;


}
