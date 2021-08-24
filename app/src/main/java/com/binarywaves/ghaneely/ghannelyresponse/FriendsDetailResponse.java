package com.binarywaves.ghaneely.ghannelyresponse;

import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendsDetailResponse {
    @JsonProperty("ListenTracks")

    private ArrayList<TrackObject> recentplayed;

    @JsonProperty("LikesTracks")
    private ArrayList<TrackObject> likes;
    @JsonProperty("FolloweredSingers")
    private ArrayList<ArtistRadio> followeredSingers;
    @JsonProperty
    private String FriendFacebookName;

    public ArrayList<ArtistRadio> getFolloweredSingers() {
        return followeredSingers;
    }

    public void setFolloweredSingers(ArrayList<ArtistRadio> followeredSingers) {
        this.followeredSingers = followeredSingers;
    }

    public ArrayList<TrackObject> getRecentplayed() {
        return recentplayed;
    }

    public void setRecentplayed(ArrayList<TrackObject> recentplayed) {
        this.recentplayed = recentplayed;
    }

    public ArrayList<TrackObject> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<TrackObject> likes) {
        this.likes = likes;
    }

    public String getFriendFacebookName() {
        return FriendFacebookName;
    }

    public void setFriendFacebookName(String friendFacebookName) {
        FriendFacebookName = friendFacebookName;
    }
}
