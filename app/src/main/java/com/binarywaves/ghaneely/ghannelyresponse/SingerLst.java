package com.binarywaves.ghaneely.ghannelyresponse;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SingerLst implements Parcelable {
    public static final Parcelable.Creator<SingerLst> CREATOR = new Parcelable.Creator<SingerLst>() {

        @Override
        public SingerLst createFromParcel(Parcel source) {

            return new SingerLst(source);
        }

        @Override
        public SingerLst[] newArray(int size) {
            // TODO Auto-generated method stub
            return new SingerLst[size];
        }
    };
    private Context mcontext;
    @JsonProperty
    private int SingerId;
    @JsonProperty
    private String SingerEnName;
    @JsonProperty
    private String SingerArName;
    @JsonProperty
    private String NationalityEnName;
    @JsonProperty
    private String NationalityArName;
    @JsonProperty
    private String SingerImgPath;

    public SingerLst(Context mcontext) {
        super();
        this.mcontext = mcontext;
    }

    public SingerLst(int SingerId, String SingerEnName, String SingerArName, String SingerImgPath) {
        super();
        this.SingerId = SingerId;
        this.SingerEnName = SingerEnName;
        this.SingerArName = SingerArName;
        this.SingerImgPath = SingerImgPath;

        this.SingerEnName = SingerEnName;
        this.SingerArName = SingerArName;


    }

    public SingerLst() {
        super();
    }

    private SingerLst(Parcel source) {
        SingerId = source.readInt();
        SingerEnName = source.readString();
        SingerArName = source.readString();
        NationalityEnName = source.readString();
        SingerImgPath = source.readString();


    }

    public int getSingerId() {
        return SingerId;
    }

    public void setSingerId(int singerId) {
        SingerId = singerId;
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

    public String getNationalityEnName() {
        return NationalityEnName;
    }

    public void setNationalityEnName(String nationalityEnName) {
        NationalityEnName = nationalityEnName;
    }

    public String getNationalityArName() {
        return NationalityArName;
    }

    public void setNationalityArName(String nationalityArName) {
        NationalityArName = nationalityArName;
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
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(SingerId);
        dest.writeString(SingerEnName);

        dest.writeString(SingerArName);

        dest.writeString(NationalityEnName);

        dest.writeString(SingerImgPath);


    }

}
