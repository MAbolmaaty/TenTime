package com.binarywaves.ghaneely.ghannelymodels;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {
    public static final String SHARED_PREFERENCE_NAME = "GhaniliPref";
    public static final String SHARED_PREFERENCE_USERId = "userid";
    public static final String SHARED_PREFERENCE_IS_REGISTERED_BY_FACEBOOK = "isFaceReg";
    public static final String SHARED_PREFERENCE_IS_REGISTERED = "isregisterd";
    public static final String SHARED_PREFERENCE_USER_PASSWORD = "password";
    public static final String SHARED_PREFERENCE_USER_EMAIL = "emailAddress";
    public static final String SHARED_PREFERENCE_USER_NAME = "passengername";
    public static final String SHARED_PREFERENCE_STATUS_ID = "statusid";
    public static final String SHARED_PREFERENCE_TOKEN = "token";

    public static final String SHARED_PREFERENCE_LANGUAGE_KEY = "en";
    public static final String SHARED_PREFERENCE_IS_INSIDE_TRIPE = "isInTrip";
    public static final String SHARED_PREFERENCE_CURRENT_TRIP_ID = "tripId";

    public static String getSharedString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");

    }

    public static int getSharedInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public static float getSharedFloat(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    public static boolean getSharedBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void setSharedString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void setSharedInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static void setSharedBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void deleteAllSharePrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        sharedPreferences.edit().clear().apply();
    }

    public void setSharedFloat(Context context, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putFloat(key, value).apply();
    }
}
