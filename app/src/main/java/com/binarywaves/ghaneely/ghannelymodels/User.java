package com.binarywaves.ghaneely.ghannelymodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class User {
    @JsonProperty
    private String ServiceId;
    @JsonProperty
    private String UserID;
    @JsonProperty
    private String Email;
    @JsonProperty
    private String Msisdn;
    @JsonProperty
    private boolean IsActivated;
    @JsonProperty
    private String LangId;
    @JsonProperty
    private String Favourites;
    @JsonProperty
    private String UserImage;
    @JsonProperty
    private String IsFacebookReg;
    @JsonProperty
    private String UserToken;
    @JsonProperty
    private String UserSubscResultDesc;
    @JsonProperty
    private String UserStatusId;
    @JsonProperty
    private String DownloadKey;
    @JsonProperty
    private String SubscEndDate;
    @JsonProperty
    private String GenreId;
    @JsonProperty
    private String UserSubscTypeID;
    @JsonProperty
    private String UserErrorType;
    @JsonProperty
    private String ServiceDailyPrice;

    @JsonProperty
    private String ServiceMonthlyPrice;

    public Boolean getAllowInternational() {
        return AllowInternational;
    }

    public void setAllowInternational(Boolean allowInternational) {
        AllowInternational = allowInternational;
    }

    @JsonProperty
    private Boolean  AllowInternational;

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getServiceDailyPrice() {
        return ServiceDailyPrice;
    }

    public void setServiceDailyPrice(String serviceDailyPrice) {
        ServiceDailyPrice = serviceDailyPrice;
    }

    public String getServiceMonthlyPrice() {
        return ServiceMonthlyPrice;
    }

    public void setServiceMonthlyPrice(String serviceMonthlyPrice) {
        ServiceMonthlyPrice = serviceMonthlyPrice;
    }

    public String getSubscEndDate() {
        return SubscEndDate;
    }

    public void setSubscEndDate(String subscEndDate) {
        SubscEndDate = subscEndDate;
    }


    public String getUserSubscTypeID() {
        return UserSubscTypeID;
    }

    public void setUserSubscTypeID(String userSubscTypeID) {
        UserSubscTypeID = userSubscTypeID;
    }

    public String getUserErrorType() {
        return UserErrorType;
    }

    public void setUserErrorType(String userErrorType) {
        UserErrorType = userErrorType;
    }

    public String getServiceID() {
        return ServiceId;
    }

    public void setServiceID(String serviceID) {
        ServiceId = serviceID;
    }

    public String getUserSubscResultDesc() {
        return UserSubscResultDesc;
    }

    public void setUserSubscResultDesc(String userSubscResultDesc) {
        UserSubscResultDesc = userSubscResultDesc;
    }

    public String getDownloadKey() {
        return DownloadKey;
    }

    public void setDownloadKey(String downloadKey) {
        DownloadKey = downloadKey;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public String getUserStatusId() {
        return UserStatusId;
    }

    public void setUserStatusId(String userStatusId) {
        UserStatusId = userStatusId;
    }

    public String getGenreId() {
        return GenreId;
    }

    public void setGenreId(String genreId) {
        GenreId = genreId;
    }

    public boolean isActivated() {
        return IsActivated;
    }

    public void setActivated(boolean activated) {
        IsActivated = activated;
    }

    public String getIsSubsc() {
        return UserStatusId;
    }

    public void setIsSubsc(String isSubsc1) {
        UserStatusId = isSubsc1;
    }


    public String getIsFacebookReg() {
        return IsFacebookReg;
    }

    public void setIsFacebookReg(String isFacebookReg) {
        IsFacebookReg = isFacebookReg;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMsisdn() {
        return Msisdn;
    }

    public void setMsisdn(String msisdn) {
        Msisdn = msisdn;
    }

    public boolean getIsActivated() {
        return IsActivated;
    }

    public void setIsActivated(boolean isActivated) {
        IsActivated = isActivated;
    }

    public String getLangId() {
        return LangId;
    }

    public void setLangId(String langId) {
        LangId = langId;
    }

    public String getFavourites() {
        return Favourites;
    }

    public void setFavourites(String favourites) {
        Favourites = favourites;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }


}
