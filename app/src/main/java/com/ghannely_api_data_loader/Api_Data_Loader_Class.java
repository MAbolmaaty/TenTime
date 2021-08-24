package com.ghannely_api_data_loader;

import android.content.Context;
import android.os.AsyncTask;

import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannelyactivites.LoginScreen;
import com.binarywaves.ghaneely.ghannelyactivites.SplashScreenActivity;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 09-Nov-17.
 */

class Api_Data_Loader_Class extends AsyncTask<Void, Void, Void> {
    private SplashScreenActivity spalchactivity;
    LoginScreen loginactivity;
    private Context context;
    private int activity_number;
    private String langtype;
    private User FinalResponse;

    public Api_Data_Loader_Class(SplashScreenActivity activitymain, Context cont, int activity, String langid) {
        activity_number = activity;
        langtype = langid;
        context = cont;
        spalchactivity = activitymain;

    }


    @Override
    protected Void doInBackground(Void... params) {
        if (activity_number == 0) {
            Api_Interface service = ServiceGenerator.createService();

            service.LoginAppVersion(SharedPrefHelper.getSharedString(context, Constants.MOBILE), SharedPrefHelper.getSharedString(context, Constants.SIM_NO),
                    langtype, SharedPrefHelper.getSharedString(context, Constants.PASSWORD),
                    FirebaseInstanceId.getInstance().getToken(), SharedPrefHelper.getSharedString(context, Constants.PROPERTY_APP_VERSION)).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {

                        FinalResponse = response.body();
                        //convert json string to object

                    } else {
                        ApiUtils.handelErrorCode(context, response.code());
                        System.out.println("onFailure");

                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                }

            });
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (activity_number == 0) {
            spalchactivity.handleResponseUi(FinalResponse);
        }
    }

}
