package com.binarywaves.ghaneely.ghannelyresponse;

import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class MoodsListResponse extends ArrayList<TrackObject> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private ArrayList<TrackObject> InstitutionBasicInfo;


}
