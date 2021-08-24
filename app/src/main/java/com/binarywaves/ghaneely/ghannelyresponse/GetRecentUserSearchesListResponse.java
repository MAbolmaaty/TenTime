package com.binarywaves.ghaneely.ghannelyresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Amany on 14-May-17.
 */

public class GetRecentUserSearchesListResponse implements Parcelable {
    public static final Parcelable.Creator<GetRecentUserSearchesListResponse> CREATOR = new Parcelable.Creator<GetRecentUserSearchesListResponse>() {

        @Override
        public GetRecentUserSearchesListResponse createFromParcel(Parcel source) {

            return new GetRecentUserSearchesListResponse(source);
        }

        @Override
        public GetRecentUserSearchesListResponse[] newArray(int size) {
            // TODO Auto-generated method stub
            return new GetRecentUserSearchesListResponse[size];
        }
    };
    @JsonProperty
    private
    String ContentId;
    @JsonProperty
    private
    String ContentTypeId;
    @JsonProperty
    private
    String ContentArName;
    @JsonProperty
    private
    String ContentEnName;
    @JsonProperty
    private
    String ContentImage;
    @JsonProperty
    private
    String SingerName;
    @JsonProperty
    private String UserSearchID;

    private GetRecentUserSearchesListResponse(Parcel source) {
        ContentId = source.readString();
        ContentTypeId = source.readString();
        ContentArName = source.readString();
        ContentEnName = source.readString();

        ContentImage = source.readString();
        SingerName = source.readString();


    }

    public GetRecentUserSearchesListResponse() {
        super();
    }

    public String getContentEnName() {
        return ContentEnName;
    }

    public void setContentEnName(String contentEnName) {
        ContentEnName = contentEnName;
    }

    public String getUsersearchID() {
        return UserSearchID;
    }

    public void setUsersearchID(String usersearchID) {
        UserSearchID = usersearchID;
    }

    public String getContentId() {
        return ContentId;
    }

    public void setContentId(String contentId) {
        ContentId = contentId;
    }

    public String getContentTypeId() {
        return ContentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        ContentTypeId = contentTypeId;
    }

    public String getContentArName() {
        return ContentArName;
    }

    public void setContentArName(String contentName) {
        ContentArName = contentName;
    }

    public String getContentImage() {
        return ContentImage;
    }

    public void setContentImage(String contentImage) {
        ContentImage = contentImage;
    }

    public String getSingerName() {
        return SingerName;
    }

    public void setSingerName(String singerName) {
        SingerName = singerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(ContentId);
        dest.writeString(ContentTypeId);

        dest.writeString(ContentArName);
        dest.writeString(ContentEnName);

        dest.writeString(ContentImage);

        dest.writeString(SingerName);


    }


}


