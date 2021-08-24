package com.binarywaves.ghaneely.ghannelyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Amany on 16-Oct-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class InboxPropertyResponse {
    @JsonProperty
    private String TrackId;
    @JsonProperty
    private String Message;
    @JsonProperty
    private String MsgType;
    @JsonProperty
    private String Title;
    @JsonProperty
    private String ImgFile;
    @JsonProperty
    private String PushNotifLogId;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTrackId() {
        return TrackId;
    }

    public void setTrackId(String trackId) {
        TrackId = trackId;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImgFile() {
        return ImgFile;
    }

    public void setImgFile(String imgFile) {
        ImgFile = imgFile;
    }

    public String getPushNotifLogId() {
        return PushNotifLogId;
    }

    public void setPushNotifLogId(String pushNotifLogId) {
        PushNotifLogId = pushNotifLogId;
    }
}
