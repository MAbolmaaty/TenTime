package com.binarywaves.ghaneely.ghaneely_retrofit_api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.binarywaves.ghaneely.ghannelyactivites.HomeActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SplashScreenActivity;

/**
 * Created by Amany on 12-Oct-17.
 */

public class ApiUtils {
    private static String status;

    public static String handelErrorCode(Context con, int statusCode) {
        switch (statusCode) {
            case StatusCodeConstants.SC_ACCEPTED:
                status = " Not accpted";
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(con, status, Toast.LENGTH_LONG).show());

                break;
            case StatusCodeConstants.STATUS_CODE_BAD_REQUEST:
                status = " Bad Request";
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(con, status, Toast.LENGTH_LONG).show();
                    Intent i1 = new Intent(con, HomeActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    con.startActivity(i1);
                });
                break;
            case StatusCodeConstants.STATUS_CODE_NO_CONTENT:
                status = "NO Content";
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(con, status, Toast.LENGTH_LONG).show());
                break;
            case StatusCodeConstants.STATUS_CODE_UNAUTHORIZED:
                status = " Unauthorized Request";
                new Handler(Looper.getMainLooper()).post(() -> {
                   // Toast.makeText(con, status, Toast.LENGTH_LONG).show();
                    Intent i1 = new Intent(con, SplashScreenActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(i1);
                });

                break;
            case StatusCodeConstants.STATUS_CODE_METHOD_NOT_ALLOWED:
                status = "Method not Allowed";
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(con, status, Toast.LENGTH_LONG).show());
                break;
            case StatusCodeConstants.STATUS_CODE_NOT_ACCEPTABLE:
                status = " Not accpted";
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(con, status, Toast.LENGTH_LONG).show());
                break;
            case StatusCodeConstants.STATUS_CODE_INTERNAL_SERVER_ERROR:
                status = "Internal Server Error";
                new Handler(Looper.getMainLooper()).post(() -> {
                   // Toast.makeText(con, status, Toast.LENGTH_LONG).show()
                    ;
                    Intent i1 = new Intent(con, SplashScreenActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(i1);
                });
                break;

        }
        return status;

    }

    public class StatusCodeConstants {
        public final static int STATUS_CODE_OK = 200;
        public final static int STATUS_CODE_NO_CONTENT = 204;
        public final static int STATUS_CODE_BAD_REQUEST = 400;
        public final static int STATUS_CODE_UNAUTHORIZED = 401;
        public final static int STATUS_CODE_METHOD_NOT_ALLOWED = 405;
        public final static int STATUS_CODE_NOT_ACCEPTABLE = 406;
        public final static int STATUS_CODE_INTERNAL_SERVER_ERROR = 500;


        public static final int SC_ACCEPTED = 202;
    }

}
