package com.binarywaves.ghaneely.ghannelymodels;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class SliderAds implements Parcelable {
    public static final Parcelable.Creator<SliderAds> CREATOR = new Parcelable.Creator<SliderAds>() {

        @Override
        public SliderAds createFromParcel(Parcel source) {

            return new SliderAds(source);
        }

        @Override
        public SliderAds[] newArray(int size) {
            // TODO Auto-generated method stub
            return new SliderAds[size];
        }
    };
    // "AlbumId":809,"SingerEnName":null,"SingerArName":null,"AlbumEnName":"Fares Karam 2013","AlbumArName":"ÙØ§Ø±Ø³ ÙƒØ±Ù… 2013Â ","AlbumImgPath":"s_822.jpg"},
    @JsonProperty
    private String AdsFileName;
    @JsonProperty
    private String AdsURL;
    @JsonProperty
    private String AdsURLAr;
    @JsonProperty
    private String AdsFileID;
    private Context mcontext;

    public SliderAds(Context mcontext) {
        super();
        this.mcontext = mcontext;
    }

    public SliderAds(String AdsFileName, String AdsURL, String AdsURLAr, String AdsFileID) {
        super();
        this.AdsFileName = AdsFileName;

        this.AdsURL = AdsURL;
        this.AdsURLAr = AdsURLAr;
        this.AdsFileID = AdsFileID;

    }

    public SliderAds() {
        super();
    }

    private SliderAds(Parcel source) {
        AdsFileName = source.readString();

        AdsURL = source.readString();
        AdsURLAr = source.readString();
        AdsFileID = source.readString();

    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(AdsFileName);

        dest.writeString(AdsURL);
        dest.writeString(AdsFileID);

        dest.writeString(AdsURLAr);

    }

    public String getAdsURLAr() {
        return AdsURLAr;
    }

    public void setAdsURLAr(String adsURLAr) {
        AdsURLAr = adsURLAr;
    }

    public String getAdsFileName() {
        return AdsFileName;
    }

    public void setAdsFileName(String adsFileName) {
        AdsFileName = adsFileName;
    }

    public String getAdsURL() {
        return AdsURL;
    }

    public void setAdsURL(String adsURL) {
        AdsURL = adsURL;
    }

    public String getAdsFileID() {
        return AdsFileID;
    }

    public void setAdsFileID(String adsFileID) {
        AdsFileID = adsFileID;
    }

}

