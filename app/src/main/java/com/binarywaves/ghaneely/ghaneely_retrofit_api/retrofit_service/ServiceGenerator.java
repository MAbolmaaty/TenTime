package com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service;

import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Amany on 11-Oct-17.
 */

public class ServiceGenerator {
    private static final int TIMEOUT = 50;
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private static Interceptor logging = interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS). // connect timeout
            readTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(logging)
            .build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(ServerConfig.SERVER_URl)
                    .addConverterFactory(JacksonConverterFactory.create());

    public static Api_Interface createService() {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(Api_Interface.class);
    }

// Execute the call asynchronously. Get a positive or negative callback.

}




