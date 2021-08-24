package com.binarywaves.ghaneely.ghannely_application_manager;

public class ServerConfig {
    //Server Function
    public static final String AddFavouriteVideoClip = "AddFavouriteVideoClip";
    public static final String SetVideoClipViewed = "SetVideoClipViewed";
    public static final String REGISTER_PATH = "Register";
    public static final String Activation_PATH = "ActivateUser";
    public static final String ResendActivation = "ResendActivation";
    public static final String LoginAppVersion = "LoginAppVersion";
    public static final String GetHomelistsWithGenre = "GetHomelistsWithGenre";
    public static final String AddUserPlaylistWithTracks = "AddUserPlaylistWithTracks";
    public static final String GetSuggestedTrackLsts = "GetSuggestedTrackLsts";
    public static final String GetHomelistsPart1 = "GetHomelistsPart1";
    public static final String GetHomelistsPart2 = "GetHomelistsPart2";


    // GetShareLink?serviceid=47&userid=18598
    public static final String GET_FAV_SONGS = "GetUserFavouriteLst";
    public static final String AddFavouriteTrack = "AddFavouriteTrack";
    public static final String GET_PlayLIST_TRACK = "GetUserPlaylstTracks";
    public static final String GetPlaylistScreen = "GetPlaylistScreen";
    public static final String ADDRBTREQUEST = "AddRBTRequest";
    public static final String AddAdsHit = "AddAdsHit";
    public static final String Getpersonaldjlist = "GetUserPersonalDJTracks";
    public static final String GetVideoDataFrmID = "GetVideoDataFrmID";
    public static final String GetSingerRadioTracks = "GetSingerRadioTracks";
    public static final String Getalbumscreen = "GetAlbumScreen";
    public static final String Changepass = "UpdateUserPassword";
    public static final String UpdateUserImage = "UpdateUserImage";
    public static final String GetUserPlaylists = "GetUserPlaylists";
    public static final String AddUserPlaylistTrack = "AddUserPlaylistTrack";
    public static final String GetSuggestedText = "GetSuggestedText";
    public static final String DelUserPlaylist = "DelUserPlaylist";
    public static final String DelUserNotification = "DelUserNotification";
    public static final String DelUserPlaylistTrack = "DelUserPlaylistTrack";
    public static final String GetAllSingerData = "GetAllSingerData";
    public static final String RegisterWithFace = "RegisterWithFace";
    public static final String SetUserFollow = "SetUserFollow";
    public static final String SetTrackListened = "SetTrackListenedDuration";
    public static final String GetSimilarTrackLst = "GetSimilarTrackLst";
    public static final String GetSingerTracksFrmTrack = "GetSingerTracksFrmTrack";

    public static final String GetUserNotifLogLimit = "GetUserNotifLogLimit";
    public static final String UpdateUserFacebookId = "UpdateUserFacebookId";
    public static final String UpdateUserLang = "UpdateUserLang";
    public static final String GetPlaylistInfo = "GetPlaylistInfo";
    public static final String GetFriendData = "GetFriendData";
    public static final String AddDeviceToken = "AddDeviceToken";
    public static final String GetRecentUserSearches = "GetRecentUserSearches";
    public static final String SearchCategoriesN = "SearchCategoriesN";
    public static final String AddUserSearchResult = "AddUserSearchResult";
    public static final String SubscribePremium = "SubscribePremium";
    public static final String GetUserSubInfo = "GetUserSubInfo";
    public static final String GetAdsPopupLst = "GetAdsPopupLst";
    public static final String SetKaraokeTrack = "SetKaraokeTrack";
    public static final String AddUserDownloadTrack = "AddUserDownloadTrack";
    public static final String SetUserDownloadTrack = "SetUserDownloadTrack";

    public static final String GetUserDownloadedLst = "GetUserDownloadedLst";
    public static final String GetkaraokeScreen = "GetkaraokeScreen";
    public static final String UpdateUserRegID = "UpdateUserRegID";
    public static final String SendForgetPassCode = "SendForgetPassCode";
    public static final String UpdatePasswordFromCode = "UpdatePasswordFromCode";
    public static final String UserLogoutFrmFacebook = "UserLogoutFrmFacebook";



    //Server Url
    // public   static final String ALBUM_IMAGE_PATH = "http://184.105.143.205/ci/albums/";
    public static final String ALBUM_IMAGE_PATH = "https://gimage.binarywaves.com/ci/Albums/";
    //	 public   static final String TRACK_PATH =" https://ghaneely.binarywaves.com/crt/";
    // public   static final String RADIO_IMAGE_PATH =" https://ghaneely.binarywaves.com/Radio/";
    public static final String RADIO_IMAGE_PATH = "https://gimage.binarywaves.com/ci/Radio/";
    public static final String DJ_IMAGE_PATH = "https://gimage.binarywaves.com/ci/DJ/";
    public static final String sharerUrl = "https://bw.ghaneely.com/share/d.aspx?";
    public static final String AudioUrl = "https://gstream.binarywaves.com/crt/";
//    "http://184.105.143.205/GhaneelyServiceTest/GhaneelyContent.asmx/"
    //https://ghaneely.binarywaves.com/GhaneelyMobile/GhaneelyContent.asmx/
    //http://104.155.104.112/GhaneelyMobileT/GhaneelyContent.asmx
    //https://gdownload.binarywaves.com/GhaneelyMobileTest/GhaneelyContent.asmx
    public static String SERVER_URl = "https://ghaneely.binarywaves.com/GhaneelyMobile/GhaneelyContent.asmx/";
    public static String Header_Url = "http://bw.ghaneely.com/GhaneelyAPI/Subscribe/CheckMSISDN";
    public static String Lyrics_Url = "https://gstream.binarywaves.com/Lyrics/";
    public static String Moods_Url = "https://gimage.binarywaves.com/ci/DJ/";
            //"http://gimage.binarywaves.com/ci/DJ/";
    public static String Radio_Url = "https://gimage.binarywaves.com/ci/radio/";
    public static String Karaoke_Url = "http://gimage.binarywaves.com/k/";
    public static String Image_path = "https://gimage.binarywaves.com/ci/Albums/";
    public static String Image_pathads = "https://gimage.binarywaves.com/ci/Ads/";
    // "https://gimage.binarywaves.com/ci/Ads/";
    //http://184.105.143.217/ci/Albums/
    public static String Image_pathSinger = "https://gimage.binarywaves.com/ci/Singers/";
    public static String Image_profile = "https://gimage.binarywaves.com/ci/UserProfile/";
    public static String Video_Url = "https://gstream.binarywaves.com/cvd/";
    public static String Video_Imagepath = "https://gimage.binarywaves.com/ci/VideoPosters/";
    public static String PLAYLIST_IMAGE_PATH = "https://gimage.binarywaves.com/ci/Playlists/";
    public static String KARAOKE_AUDIO_PATH = "https://gstream.binarywaves.com/Karaoke/Minus/";
    public static String KARAOKE_Saved_PATH = "https://gimage.binarywaves.com/Karaoke/Saved/";


    private ServerConfig() {

    }


}
