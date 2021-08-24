package com.binarywaves.ghaneely.ghannelyresponse;

/**
 * Created by Amany on 02-Nov-17.
 */

public class VideoDownloadObject {
    public String IsPremium;
    public String IsDownloaded;
    private String VideoID;
    private String VideoEnName;
    private String VideoArName;
    private String VideoPath;
    private byte[] VideoPoster;
    private String SingerId;
    private String SingerEnName;
    private String SingerArName;
    private String LikesCount;
    private String ViewCount;
    private String IsFavourite;

    public VideoDownloadObject() {
        super();
    }

    public VideoDownloadObject(String videoEnName,String videoID , String videoArName, String singerEnName, String singerArName, byte[] videoposter) {
        VideoEnName = videoEnName;

        VideoID = videoID;
        VideoArName = videoArName;
        SingerEnName = singerEnName;
        SingerArName = singerArName;
        VideoPoster = videoposter;
    }

    public String getIsPremium() {
        return IsPremium;
    }

    public void setIsPremium(String isPremium) {
        IsPremium = isPremium;
    }

    public String getIsDownloaded() {
        return IsDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        IsDownloaded = isDownloaded;
    }

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getVideoEnName() {
        return VideoEnName;
    }

    public void setVideoEnName(String videoEnName) {
        VideoEnName = videoEnName;
    }

    public String getVideoArName() {
        return VideoArName;
    }

    public void setVideoArName(String videoArName) {
        VideoArName = videoArName;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }

    public byte[] getVideoPoster() {
        return VideoPoster;
    }

    public void setVideoPoster(byte[] videoPoster) {
        VideoPoster = videoPoster;
    }

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
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

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

}
