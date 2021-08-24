package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

class notificationplaylist {
    @JsonProperty("d")
    private PlaylistnotificationResponse userQuestionsAttachments;

    public PlaylistnotificationResponse getUserQuestionsAttachments() {
        return userQuestionsAttachments;
    }

    public void setUserQuestionsAttachments(PlaylistnotificationResponse userQuestionsAttachments) {
        this.userQuestionsAttachments = userQuestionsAttachments;
    }

}
