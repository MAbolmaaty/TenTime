package com.binarywaves.ghaneely.ghannelymodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)


public class ArtistRadio implements Parcelable {
//{"SingerId":156,"NationalityArName":"عمرو دياب","NationalityEnName":"Masri","SingerEnName":"Amr Diab","SingerArName":"عمرو دياب","SingerImgPath":"391.jpg"}

    public static final Creator<ArtistRadio> CREATOR = new Creator<ArtistRadio>() {
        @Override
        public ArtistRadio createFromParcel(Parcel in) {
            return new ArtistRadio(in);
        }

        @Override
        public ArtistRadio[] newArray(int size) {
            return new ArtistRadio[size];
        }
    };
    @JsonProperty
    private int SingerId;
    @JsonProperty
    private String NationalityArName;
    @JsonProperty
    private String NationalityEnName;
    @JsonProperty
    private String SingerEnName;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String SingerImgPath;

    private ArtistRadio(Parcel in) {
        SingerId = in.readInt();
        NationalityArName = in.readString();
        NationalityEnName = in.readString();
        SingerEnName = in.readString();
        SingerArName = in.readString();
        SingerImgPath = in.readString();
    }

    public ArtistRadio() {
        super();
    }

    public int getSingerId() {
        return SingerId;
    }

    public void setSingerId(int singerId) {
        SingerId = singerId;
    }

    public String getNationalityArName() {
        return NationalityArName;
    }

    public void setAtionalityArName(String NationalityArName) {
        this.NationalityArName = NationalityArName;
    }

    public String getNationalityEnName() {
        return NationalityEnName;
    }

    public void setNationalityEnName(String nationalityEnName) {
        NationalityEnName = nationalityEnName;
    }

    public String getSingerEnName() {
        return SingerEnName;
    }

    public void setSingerEnName(String singerEnName) {
        SingerEnName = singerEnName;
    }

    public String getSingerArName() {
        return SingerArName;
    }

    public void setSingerArName(String singerArName) {
        SingerArName = singerArName;
    }

    public String getSingerImgPath() {
        return SingerImgPath;
    }

    public void setSingerImgPath(String singerImgPath) {
        SingerImgPath = singerImgPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(SingerId);
        parcel.writeString(NationalityArName);
        parcel.writeString(NationalityEnName);
        parcel.writeString(SingerEnName);
        parcel.writeString(SingerArName);
        parcel.writeString(SingerImgPath);
    }
}
