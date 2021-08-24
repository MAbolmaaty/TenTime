package com.firebaseService;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String refreshedToken;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("FCN TOKEN GET", "Refreshed token: " + refreshedToken);

        if (SharedPrefHelper.getSharedString(context, Constants.tokenfcm) == null || !SharedPrefHelper.getSharedString(context, Constants.tokenfcm).equalsIgnoreCase(FirebaseInstanceId.getInstance().getToken())) {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("FCN TOKEN GET", "Refreshed token: " + refreshedToken);

            if (refreshedToken != null) {

                if (!SharedPrefHelper.getSharedString(context, Constants.accesstoken).equalsIgnoreCase("")) {
                    Log.d("accesstoken", "accesstoken: " + SharedPrefHelper.getSharedString(context, Constants.accesstoken));

                    sendRegistrationToServer(refreshedToken);
                }
            }
        }
    }

    private void sendRegistrationToServer(final String token) {
        // You can implement this method to store the token on your server
        // Not required for current project
        SharedPreferences prefs = context.getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);


        Api_Interface service = ServiceGenerator.createService();
        service.UpdateUserRegID(token, prefs.getString(Constants.USERID, ""), prefs.getString(Constants.accesstoken, "")).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    if (token != null) {
                        SharedPrefHelper.setSharedString(context, Constants.tokenfcm, token);
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {

            }
        });


    }


}
