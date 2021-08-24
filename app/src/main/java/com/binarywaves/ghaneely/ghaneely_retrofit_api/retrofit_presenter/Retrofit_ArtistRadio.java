package com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.MoodsListResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Retrofit_ArtistRadio{

 private static RetrofitPresenterListener mListener;
        private final Context context;
        private ServiceGenerator retrofitService = null;
        private int statuscode;

        public Retrofit_ArtistRadio(RetrofitPresenterListener listener, Context context) {
            mListener = listener;
            this.context = context;
            this.retrofitService = new ServiceGenerator();
        }

        public void getArtistRadioResponse(String url) {
            ServiceGenerator
                    .createService()
                    .GetSingerRadioTracks(url)
                    .enqueue(new Callback<MoodsListResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<MoodsListResponse> call, @NonNull Response<MoodsListResponse> response) {
                            MoodsListResponse result = response.body();
                            statuscode = response.code();
                            if (result != null)
                                mListener.responseRadioReady(result, response.code(),0);
                        }

                        @Override
                        public void onFailure(@NonNull Call<MoodsListResponse> call, @NonNull Throwable t) {
                            try {

                                ApiUtils.handelErrorCode(context, statuscode);


                                throw new InterruptedException("Erro na comunicação com o servidor!");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }





public interface RetrofitPresenterListener {
    void responseRadioReady(MoodsListResponse MoodsListresponses, int code, int functiontype);

}

}