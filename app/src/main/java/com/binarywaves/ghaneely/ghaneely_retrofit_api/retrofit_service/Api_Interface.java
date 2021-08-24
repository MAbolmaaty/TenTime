package com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service;

import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.SliderAds;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.AlbumScreenresponse;
import com.binarywaves.ghaneely.ghannelyresponse.ChangepassResponse;
import com.binarywaves.ghaneely.ghannelyresponse.CreatePlaylistresponse;
import com.binarywaves.ghaneely.ghannelyresponse.FriendsDetailResponse;
import com.binarywaves.ghaneely.ghannelyresponse.GetRecentUserSearchesResponse;
import com.binarywaves.ghaneely.ghannelyresponse.GetSuggestedTrackLstsResponse;
import com.binarywaves.ghaneely.ghannelyresponse.GetUserDownloadedLst;
import com.binarywaves.ghaneely.ghannelyresponse.GetVideoDataFrmIDResponse;
import com.binarywaves.ghaneely.ghannelyresponse.HomeListObjectResponse;
import com.binarywaves.ghaneely.ghannelyresponse.InboxPropertyResponse;
import com.binarywaves.ghaneely.ghannelyresponse.KraokeSavedResponse;
import com.binarywaves.ghaneely.ghannelyresponse.MoodsListResponse;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistScreenResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Playlistlst;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistnotificationResponse;
import com.binarywaves.ghaneely.ghannelyresponse.SearchResponse;
import com.binarywaves.ghaneely.ghannelyresponse.Similartrackres;
import com.binarywaves.ghaneely.ghannelyresponse.SingerfrmSingeridResponse;
import com.binarywaves.ghaneely.ghannelyresponse.SmartSearchResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;


/**
 * Created by Amany on 11-Oct-17.
 */

public interface Api_Interface {
    @GET()
    Call<Addrbtresponse> GetheaderInrch(@Url String url);
    @GET()
    Call<AlbumScreenresponse> Getalbumscreen(@Url String url);

    @GET()
    Call<Addrbtresponse> AddDeviceToken(@Url String url);

    @GET()
    Call<Addfavourite_Response> AddFavouriteTrack(@Url String url);

    @GET()
    Call<ArrayList<SliderAds>> GetAdsPopupLst(@Url String url);


    @GET()
    Call<Addrbtresponse> AddUserDownloadTrack(@Url String url);

    @GET()
    Call<Addrbtresponse> ADDRBTREQUEST(@Url String url);

    @GET()
    Call<Similartrackres> getsimilartrack(@Url String url);

    @GET()
    Call<Similartrackres> GetSingerTracksFrmTrack(@Url String url);

    @GET()
    Call<MoodsListResponse> GetSingerRadioTracks(@Url String url);

    @GET()
    Call<GetUserDownloadedLst> GetUserDownloadedLst(@Url String url);

    @GET()
    Call<Addrbtresponse> AddAdsHit(@Url String url);

    @GET()
    Call<Addrbtresponse> AddUserPlaylistTrack(@Url String url);


    @GET()
    Call<Addrbtresponse> AddUserPlaylistWithTracks(@Url String url);

    @GET()
    Call<GetSuggestedTrackLstsResponse> GetSuggestedTrackLsts(@Url String url);

    @GET()
    Call<ResponseBody> Getlyricsfile(@Url String url);

    @GET()
    Call<GetUserDownloadedLst> DeleteUserDownloadTrack(@Url String url);

    @GET()
    Call<User> GetUserSubInfo(@Url String url);

    @GET()
    Call<CreatePlaylistresponse> DelUserPlaylist(@Url String url);

    @GET()
    Call<User> SubscribePremium(@Url String url);

    @GET()
    Call<Addrbtresponse> DelUserPlaylistTrack(@Url String url);

    @GET()
    Call<PlaylistnotificationResponse> GetPlaylistInfo(@Url String url);

    @GET()
    Call<ChangepassResponse> Changepass(@Url String url);

    @GET()
    Call<Addrbtresponse> UpdateUserLang(@Url String url);
    @GET()
    Call<Addrbtresponse> UserLogoutFrmFacebook(@Url String url);
    @GET()
    Call<GetVideoDataFrmIDResponse> GetVideoDataFrmID(@Url String url);


    @GET()
    Call<FriendsDetailResponse> GetFriendData(@Url String url);


    @GET()
    Call<SingerfrmSingeridResponse> GetAllSingerData(@Url String url);


    @GET()
    Call<Addrbtresponse> SetUserFollow(@Url String url);


    @GET()
    Call<GetRecentUserSearchesResponse> GetRecentUserSearches(@Url String url);


    @GET()
    Call<SearchResponse> SearchCategoriesN(@Url String url);

    @GET()
    Call<SmartSearchResponse> GetSuggestedText(@Url String url);


    @GET()
    Call<Addrbtresponse> AddUserSearchResult(@Url String url);

    @GET()
    Call<MoodsListResponse> GellAllKaraokeTracks(@Url String url);

    @GET()
    Call<KraokeSavedResponse> GetSavedKaraokeTracks(@Url String url);

    @GET()
    Call<Addrbtresponse> SetTrackListened(@Url String url);

    @GET()
    Call<Addrbtresponse> SetVideoClipViewed(@Url String url);

    @GET()
    Call<KraokeSavedResponse> GetkaraokeScreen(@Url String url);


    @FormUrlEncoded
    @POST(ServerConfig.LoginAppVersion)
    Call<User> LoginAppVersion(
            @Field("msisdn") String msisdn,
            @Field("simId") String simId,
            @Field("langId") String langId,
            @Field("pass") String pass,
            @Field("regId") String regId,
            @Field("appVersion") String appVersion);

    @FormUrlEncoded
    @POST(ServerConfig.GetUserPlaylists)
    Call<ArrayList<Playlistlst>> GetUserPlaylists(
            @Field("userId") String userId,
            @Field("UserToken") String UserToken);


    @FormUrlEncoded

    @POST(ServerConfig.GET_FAV_SONGS)
    Call<ArrayList<TrackObject>> GET_FAV_SONGS(
            @Field("userId") String userId,
            @Field("UserToken") String UserToken);

    @FormUrlEncoded

    @POST(ServerConfig.GetPlaylistScreen)
    Call<PlaylistScreenResponse> GetPlaylistScreen(
            @Field("userId") String userId,
            @Field("UserToken") String UserToken);

    @FormUrlEncoded

    @POST (ServerConfig.RegisterWithFace)
    Call<User> RegisterWithFace(@Header("useragent") String useragent,
                                @Field("msisdn") String msisdn,
                                @Field("isFaceReg") String isFaceReg,
                                @Field("facebookUersname") String facebookUersname,
                                @Field("facebookId") String facebookId,
                                @Field("email") String email,
                                @Field("pass") String pass,
                                @Field("simId") String simId,
                                @Field("imei") String imei,
                                @Field("regId") String regId,
                                @Field("langId") String langId,
                                @Field("deviceType") String deviceType);

    @FormUrlEncoded

    @POST(ServerConfig.SendForgetPassCode)
    Call<Addrbtresponse> SendForgetPassCode(@Field("msisdn") String msisdn,
                                    @Field("imei") String imei);

    @FormUrlEncoded

    @POST(ServerConfig.UpdatePasswordFromCode)
    Call<Addrbtresponse> UpdatePasswordFromCode(@Field("msisdn") String msisdn,
                                  @Field("imei") String imei,@Field("code")String code,@Field("newPass")String newpass);


    @FormUrlEncoded

    @POST(ServerConfig.UpdateUserFacebookId)
    Call<User> UpdateUserFacebookId(@Field("facebookUersname") String facebookUersname,
                                    @Field("facebookId") String facebookId,
                                    @Field("userId") String userId,
                                    @Field("UserToken") String UserToken);


    @FormUrlEncoded

    @POST(ServerConfig.DelUserNotification)
    Call<ArrayList<InboxPropertyResponse>> DelUserNotification(@Field("pushNotifLogId") String pushNotifLogId,

                                                               @Field("userId") String userId,
                                                               @Field("UserToken") String UserToken);

    @FormUrlEncoded

    @POST(ServerConfig.GetUserNotifLogLimit)
    Call<ArrayList<InboxPropertyResponse>> GetUserNotifLogLimit(@Field("pushNotifLogId") int pushNotifLogId,
                                                                @Field("isOlder") Boolean isOlder,
                                                                @Field("userId") String userId,
                                                                @Field("UserToken") String UserToken);

    @FormUrlEncoded

    @POST(ServerConfig.Getpersonaldjlist)
    Call<MoodsListResponse> Getpersonaldjlist(@Field("personalDJId") String personalDJId,

                                              @Field("userId") String userId,
                                              @Field("UserToken") String UserToken);

    @FormUrlEncoded

    @POST(ServerConfig.GET_PlayLIST_TRACK)
    Call<ArrayList<TrackObject>> GET_PlayLIST_TRACK(@Field("usrPlaylstId") String usrPlaylstId,

                                                    @Field("userId") String userId,
                                                    @Field("UserToken") String UserToken);


    @FormUrlEncoded
    @POST(ServerConfig.GetHomelistsWithGenre)
    Call<HomeListObjectResponse> GetHomelistsWithGenre(@Field("genreId") int genreId,
                                                       @Field("userId") String userId,
                                                       @Field("UserToken") String UserToken);
    @FormUrlEncoded
    @POST(ServerConfig.GetHomelistsPart1)
    Call<HomeListObjectResponse> GetHomelistsWithGenrePart1(@Field("genreId") int genreId,
                                                       @Field("userId") String userId,
                                                       @Field("UserToken") String UserToken);

    @FormUrlEncoded
    @POST(ServerConfig.GetHomelistsPart2)
    Call<HomeListObjectResponse> GetHomelistsWithGenrePart2(@Field("genreId") int genreId,
                                                       @Field("userId") String userId,
                                                       @Field("UserToken") String UserToken);


    @FormUrlEncoded
    @POST(ServerConfig.UpdateUserImage)
    Call<Addrbtresponse> UpdateUserImage(@Field("encodedImageStr") String encodedImageStr,

                                         @Field("userId") String userId,
                                         @Field("UserToken") String UserToken);


    @FormUrlEncoded

    @POST(ServerConfig.SetKaraokeTrack)
    Call<ChangepassResponse> SetKaraokeTrack(@Field("karaokeName") String karaokeName,
       @Field("stringInBase64") String stringInBase64,
       @Field("trackId") String trackId,
       @Field("userId") String userId,
       @Field("UserToken") String UserToken);


    @FormUrlEncoded

    @POST(ServerConfig.ResendActivation)
    Call<Addrbtresponse> ResendActivation(
            @Field("userId") String userId,
            @Field("UserToken") String UserToken);


    @FormUrlEncoded
    @POST(ServerConfig.Activation_PATH)
    Call<User> Activation_PATH(@Field("code") String code,
                               @Field("imei") String imei,
                               @Field("userId") String userId,
                               @Field("UserToken") String UserToken);


    @FormUrlEncoded

    @POST(ServerConfig.UpdateUserRegID)
    Call<Addrbtresponse> UpdateUserRegID(@Field("regId") String regId,
                                         @Field("userId") String userId,
                                         @Field("UserToken") String UserToken);
}
