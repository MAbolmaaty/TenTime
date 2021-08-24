package com.binarywaves.ghaneely.ghannelymodels;

/**
 * Created by Amany on 19-Sep-17.
 */

public class TrackDownloadObject {
    private String TrackId;


    private String TrackEnName;

    private String TrackArName;

    private String TrackPath;

    private String IsFavourite;

    private String AlbumId;


    private String SingerEnName;

    private String SingerArName;

    private String AlbumEnName;

    private String AlbumArName;

    private String IsRBT;

    private String SingerId;

    private String LikesCount;

    private String ListenCount;

    private byte[] TrackImage;


    private String TrackLength;
    private String IsPremium;
    private String HasLyrics;
    private String LyricFile;
    private String SingerImg;
    private String VideoID;


    public TrackDownloadObject(String trackId, String trackEnName, String trackArName, String trackPath, String isFavourite, String albumId, String singerEnName, String singerArName, String albumEnName, String albumArName, String isRBT, String singerId, String likesCount, String listenCount, byte[] trackImage, String trackLength, String hasLyrics, String lyricFile, String singerImg, String videoID, String isPremium) {
        TrackId = trackId;
        TrackEnName = trackEnName;
        TrackArName = trackArName;
        TrackPath = trackPath;
        IsFavourite = isFavourite;
        AlbumId = albumId;
        SingerEnName = singerEnName;
        SingerArName = singerArName;
        AlbumEnName = albumEnName;
        AlbumArName = albumArName;
        IsRBT = isRBT;
        SingerId = singerId;
        LikesCount = likesCount;
        ListenCount = listenCount;
        TrackImage = trackImage;
        TrackLength = trackLength;
        HasLyrics = hasLyrics;
        LyricFile = lyricFile;
        SingerImg = singerImg;
        VideoID = videoID;
        IsPremium = isPremium;
    }

    public TrackDownloadObject() {
        super();
    }

    public TrackDownloadObject( String trackEnName,String trackId, String trackArName, String singerEnName, String singerArName, String trackLength, byte[] trackImage) {
        TrackId = trackId;
        TrackEnName = trackEnName;
        TrackArName = trackArName;
        SingerEnName = singerEnName;
        SingerArName = singerArName;
        TrackLength = trackLength;
        TrackImage = trackImage;

    }

    public String getTrackId() {
        return TrackId;
    }

    public void setTrackId(String trackId) {
        TrackId = trackId;
    }

    public String getTrackEnName() {
        return TrackEnName;
    }

    public void setTrackEnName(String trackEnName) {
        TrackEnName = trackEnName;
    }

    public String getTrackArName() {
        return TrackArName;
    }

    public void setTrackArName(String trackArName) {
        TrackArName = trackArName;
    }

    public String getTrackPath() {
        return TrackPath;
    }

    public void setTrackPath(String trackPath) {
        TrackPath = trackPath;
    }

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
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

    public String getAlbumEnName() {
        return AlbumEnName;
    }

    public void setAlbumEnName(String albumEnName) {
        AlbumEnName = albumEnName;
    }

    public String getAlbumArName() {
        return AlbumArName;
    }

    public void setAlbumArName(String albumArName) {
        AlbumArName = albumArName;
    }

    public String getIsRBT() {
        return IsRBT;
    }

    public void setIsRBT(String isRBT) {
        IsRBT = isRBT;
    }

    public String getSingerId() {
        return SingerId;
    }

    public void setSingerId(String singerId) {
        SingerId = singerId;
    }

    public String getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(String likesCount) {
        LikesCount = likesCount;
    }

    public String getListenCount() {
        return ListenCount;
    }

    public void setListenCount(String listenCount) {
        ListenCount = listenCount;
    }

    public byte[] getTrackImage() {
        return TrackImage;
    }

    public void setTrackImage(byte[] trackImage) {
        TrackImage = trackImage;
    }

    public String getTrackLength() {
        return TrackLength;
    }

    public void setTrackLength(String trackLength) {
        TrackLength = trackLength;
    }

    public String getHasLyrics() {
        return HasLyrics;
    }

    public void setHasLyrics(String hasLyrics) {
        HasLyrics = hasLyrics;
    }

    public String getLyricFile() {
        return LyricFile;
    }

    public void setLyricFile(String lyricFile) {
        LyricFile = lyricFile;
    }

    public String getSingerImg() {
        return SingerImg;
    }

    public void setSingerImg(String singerImg) {
        SingerImg = singerImg;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getIsPremium() {
        return IsPremium;
    }

    public void setIsPremium(String isPremium) {
        IsPremium = isPremium;
    }
}
