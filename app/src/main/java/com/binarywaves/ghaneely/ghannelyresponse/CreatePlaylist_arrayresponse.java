package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class CreatePlaylist_arrayresponse {
    @JsonProperty

    private String UserPlaylistId;
    @JsonProperty

    private String UserPlaylistName;
    @JsonProperty

    private String UserPlaylistImgPath;

    public String getUserPlaylistId() {
        return UserPlaylistId;
    }

    public void setUserPlaylistId(String userPlaylistId) {
        UserPlaylistId = userPlaylistId;
    }

    public String getUserPlaylistName() {
        return UserPlaylistName;
    }

    public void setUserPlaylistName(String userPlaylistName) {
        UserPlaylistName = userPlaylistName;
    }

    public String getUserPlaylistImgPath() {
        return UserPlaylistImgPath;
    }

    public void setUserPlaylistImgPath(String userPlaylistImgPath) {
        UserPlaylistImgPath = userPlaylistImgPath;
    }
}