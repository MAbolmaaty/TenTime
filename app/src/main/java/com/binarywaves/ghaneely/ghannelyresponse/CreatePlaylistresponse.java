package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class CreatePlaylistresponse extends ArrayList<CreatePlaylist_arrayresponse> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private ArrayList<CreatePlaylist_arrayresponse> InstitutionBasicInfo;


}
