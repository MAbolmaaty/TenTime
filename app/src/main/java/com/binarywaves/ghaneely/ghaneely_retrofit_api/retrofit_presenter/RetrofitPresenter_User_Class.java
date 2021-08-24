package com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 07-Dec-17.
 */

public class RetrofitPresenter_User_Class {
    private static RetrofitPresenterListener mListener;
    private final Context context;
    private ServiceGenerator retrofitService = null;
    private int statuscode;

    public RetrofitPresenter_User_Class(RetrofitPresenterListener listener, Context context) {
        mListener = listener;
        this.context = context;
        this.retrofitService = new ServiceGenerator();
    }

    public void getResendActivationResponse() {
        ServiceGenerator
                .createService()
                .ResendActivation(SharedPrefHelper.getSharedString(context, Constants.USERID),
                        SharedPrefHelper.getSharedString(context, Constants.accesstoken))
                .enqueue(new Callback<Addrbtresponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> response) {
                        Addrbtresponse result = response.body();
                        statuscode = response.code();
                        if (result != null)
                            mListener.responseRbtReady(result, response.code(),0);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                        try {

                            ApiUtils.handelErrorCode(context, statuscode);


                            throw new InterruptedException("Erro na comunicação com o servidor!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getActivationResponse(String activatecode) {
        ServiceGenerator
                .createService()
                .Activation_PATH(activatecode, SharedPrefHelper.getSharedString(context, Constants.IMEI),
                        SharedPrefHelper.getSharedString(context, Constants.USERID), SharedPrefHelper.getSharedString(context, Constants.accesstoken)
                ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User result = response.body();
                statuscode = response.code();
                if (result != null)
                    mListener.responseUserReady(result, response.code(), 0);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                try {

                    ApiUtils.handelErrorCode(context, statuscode);


                    throw new InterruptedException("Erro na comunicação com o servidor!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getLoginResponse(String langtype) {
        ServiceGenerator
                .createService()
                .LoginAppVersion(SharedPrefHelper.getSharedString(context, Constants.MOBILE), SharedPrefHelper.getSharedString(context, Constants.SIM_NO),
                        langtype, SharedPrefHelper.getSharedString(context, Constants.PASSWORD),
                        FirebaseInstanceId.getInstance().getToken(), SharedPrefHelper.getSharedString(context, Constants.PROPERTY_APP_VERSION)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User result = response.body();
                statuscode = response.code();
                if (result != null)
                    mListener.responseUserReady(result, response.code(), 0);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                try {

                    ApiUtils.handelErrorCode(context, statuscode);


                    throw new InterruptedException("Erro na comunicação com o servidor!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void getRegisterResponse(String buf, String mobilenum, String isFaceReg1, String fbname, String fbid, String newpass1, String langtype) {
        ServiceGenerator
                .createService()
                .RegisterWithFace(buf, mobilenum, isFaceReg1,
                        fbname, fbid, "", newpass1, SharedPrefHelper.getSharedString(context, Constants.SIM_NO), SharedPrefHelper.getSharedString(context, Constants.IMEI),
                        FirebaseInstanceId.getInstance().getToken(), langtype, "1"
                ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User result = response.body();
                statuscode = response.code();
                if (result != null)
                    mListener.responseUserReady(result, response.code(), 0);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                try {

                    ApiUtils.handelErrorCode(context, statuscode);


                    throw new InterruptedException("Erro na comunicação com o servidor!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getloginResponse( String mobilenum, String newpass1, String langtype,String tokenfcm) {
        ServiceGenerator
                .createService()
                .LoginAppVersion(mobilenum, SharedPrefHelper.getSharedString(context, Constants.SIM_NO),
                        langtype, newpass1,
                        tokenfcm, SharedPrefHelper.getSharedString(context, Constants.PROPERTY_APP_VERSION)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User result = response.body();
                statuscode = response.code();
                if (result != null)
                    mListener.responseUserReady(result, response.code(), 0);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                try {

                    ApiUtils.handelErrorCode(context, statuscode);


                    throw new InterruptedException("Erro na comunicação com o servidor!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void GetSubscribeOption(String url) {
        ServiceGenerator
                .createService()
                .GetUserSubInfo(url).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User result = response.body();
                statuscode = response.code();
                if (result != null)
                    mListener.responseUserReady(result, response.code(), 1);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                try {

                    ApiUtils.handelErrorCode(context, statuscode);


                    throw new InterruptedException("Erro na comunicação com o servidor!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void SendSubsribedata(String url) {
        ServiceGenerator
                .createService()
                .SubscribePremium(url).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User result = response.body();
                statuscode = response.code();
                if (result != null)
                    mListener.responseUserReady(result, response.code(), 2);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                try {

                    ApiUtils.handelErrorCode(context, statuscode);


                    throw new InterruptedException("Erro na comunicação com o servidor!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void SendForgetPassCode(String mobilenum, String imie) {
        ServiceGenerator
                .createService()
                .SendForgetPassCode(mobilenum,
                        imie)
                .enqueue(new Callback<Addrbtresponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> response) {
                        Addrbtresponse result = response.body();
                        statuscode = response.code();
                        if (result != null)
                            mListener.responseRbtReady(result, response.code(),1);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                        try {

                            ApiUtils.handelErrorCode(context, statuscode);


                            throw new InterruptedException("Erro na comunicação com o servidor!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void UpdatePasswordFromCode(String mobilenum, String imie,String code,String newpass) {
        ServiceGenerator
                .createService()
                .UpdatePasswordFromCode(mobilenum,
                        imie,code,newpass)
                .enqueue(new Callback<Addrbtresponse>() {
                    @Override
                    public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> response) {
                        Addrbtresponse result = response.body();
                        statuscode = response.code();
                        if (result != null)
                            mListener.responseRbtReady(result, response.code(),2);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
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
        void responseRbtReady(Addrbtresponse addrbtresponses, int code, int functiontype);

        void responseUserReady(User mUser, int code, int functiontype);


    }


}