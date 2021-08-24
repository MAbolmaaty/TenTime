package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Similartrackres extends ArrayList<Similartrackstripresponse> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private ArrayList<Similartrackstripresponse> InstitutionBasicInfo;


}