package com.binarywaves.ghaneely.ghannelyresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Amany on 24-Aug-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class KarokeSavedObject implements Parcelable {

    public static final Parcelable.Creator<KarokeSavedObject> CREATOR = new Parcelable.Creator<KarokeSavedObject>() {

        @Override
        public KarokeSavedObject createFromParcel(Parcel source) {

            return new KarokeSavedObject(source);
        }

        @Override
        public KarokeSavedObject[] newArray(int size) {
            // TODO Auto-generated method stub
            return new KarokeSavedObject[size];
        }
    };
    @JsonProperty
    private int TrackId;
    @JsonProperty
    private String KaraokeName;
    @JsonProperty
    private String KaraokePath;

    public KarokeSavedObject() {
        super();
    }

    private KarokeSavedObject(Parcel source) {

        TrackId = source.readInt();
        KaraokeName = source.readString();
        KaraokePath = source.readString();


    }

    public int getTrackId() {
        return TrackId;
    }

    public void setTrackId(int trackId) {
        TrackId = trackId;
    }

    public String getKaraokeName() {
        return KaraokeName;
    }

    public void setKaraokeName(String karaokeName) {
        KaraokeName = karaokeName;
    }

    public String getKaraokePath() {
        return KaraokePath;
    }

    public void setKaraokePath(String karaokePath) {
        KaraokePath = karaokePath;
    }

    @Override
    public int describeContents() {

        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        dest.writeInt(TrackId);
        dest.writeString(KaraokeName);
        dest.writeString(KaraokePath);


    }


}
