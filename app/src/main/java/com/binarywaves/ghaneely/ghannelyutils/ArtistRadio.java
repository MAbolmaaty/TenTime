package com.binarywaves.ghaneely.ghannelyutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistradioActivity;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.MoodsListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRadio {
    static MoodsListResponse response;

    public static void GetListdj(Context ctx, String myString) {
        ArtistradioActivity.startwhellprogress();

        // Constants.SERVER_URl + Constants.REGISTER_PATH
        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSingerRadioTracks + "?singerId=" + myString + "&userId="
                + SharedPrefHelper.getSharedString(ctx, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(ctx, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        service.GetSingerRadioTracks(fav_url).enqueue(new Callback<MoodsListResponse>() {

            @Override
            public void onResponse(@NonNull Call<MoodsListResponse> call, @NonNull Response<MoodsListResponse> mresult) {
                if (mresult.isSuccessful()) {
                     response = mresult.body();
                    ArtistradioActivity.progBar.setVisibility(View.GONE);

                    if (response != null && response.size() > 0) {
                      // ArtistradioActivity.setMtrackList(response);

                    }
//                    if (mtrackList != null && mtrackList.size() > 0)
//                        setGridlist(mtrackList);
//                    imagecontainer.setVisibility(View.VISIBLE);


                } else {
                    ApiUtils.handelErrorCode(ctx, mresult.code());
                    System.out.println("onFailure");
                    ArtistradioActivity.progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<MoodsListResponse> call, @NonNull Throwable t) {
                ArtistradioActivity.progBar.setVisibility(View.GONE);

            }
        });


    }
}
