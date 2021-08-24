package com.binarywaves.ghaneely.ghannelyactivites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter.RetrofitPresenter_User_Class;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.NetworkChangeReceiver;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebaseService.MyFirebaseInstanceIDService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SplashScreenActivity extends ActivityMainRunnuing implements RetrofitPresenter_User_Class.RetrofitPresenterListener {
    private final String TAG = SplashScreenActivity.class.getSimpleName();
    private String userID;
    private String msisdn;
    private String UserImage;
    private String IsFacebookReg;
    private String accesstoken;
    private String UserStatusId;
    private String ServiceID;
    private boolean ISACTIVE;
    private MyFirebaseInstanceIDService myFirebaseInstanceIdService;
    private Boolean checkdate = true;
    private Context context;
    private Thread thread2;
    private String firsttrack = "";
    private String firstvideo = "";
    private String langtype = "1";
    private String tokenfcm;
    private Date today;
    private Date enddate;

    private static void callFacebookLogout() {
        LoginManager.getInstance().logOut();
    }

    public static void showHashKey(Context context) {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = context.getPackageManager().getPackageInfo("com.binarywaves.Ghaneely",
                    PackageManager.GET_SIGNATURES); // Your package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            SharedPrefHelper.setSharedString(context, Constants.PROPERTY_APP_VERSION, packageInfo.versionName);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Splash Screen On Create");
        FacebookSdk.sdkInitialize(getApplicationContext());
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_spalsh_screen);
        Applicationmanager.setContext(SplashScreenActivity.this);

        context = getApplicationContext();
        showHashKey(context);
        // FirebaseApp.initializeApp(this);
        //noinspection StatementWithEmptyBody
        if (Constants.isNetworkOnline(SplashScreenActivity.this)) {
            //FirebaseCrash.report(new Exception("Ghannely Android non-fatal error"));
            //
        }

        // showHashKey(getApplicationContext());
        getAppVersion(context);

        new_sim();
        try {
            myFirebaseInstanceIdService = new MyFirebaseInstanceIDService();
            Intent intent = new Intent(getApplicationContext(), myFirebaseInstanceIdService.getClass());
            startService(intent); //invoke onCreate
        } catch (Exception e) {
            //  tokenfcm = FirebaseInstanceId.getInstance().getToken();
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                tokenfcm = instanceIdResult.getToken();
                Log.e("newToken", tokenfcm);

            }
        });
//        thread2 = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    tokenfcm = FirebaseInstanceId.getInstance().getToken();
//                    if (tokenfcm == null) {
//                        Thread.sleep(3000);
//                    } // As I am using LENGTH_LONG
//                    // in
//                    // Toast
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        };
//        thread2.start();

        InitialzeData();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        tokenfcm = instanceIdResult.getToken();
                        Log.e("newToken", tokenfcm);
                        Log.e("TAG", "Refreshed token: " + tokenfcm);
                        SharedPrefHelper.setSharedString(getApplicationContext(), Constants.tokenfcm, tokenfcm);

                        Log.e("TAG", "SharedPrefHelper token: " + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.tokenfcm));

                    }
                });


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isRegisterd = SharedPrefHelper.getSharedBoolean(context, Constants.ISREGISTERD);

            Log.e("Is registered", isRegisterd + "");

            if (Constants.isNetworkOnline(SplashScreenActivity.this)) {
                if (isRegisterd) {
                    Log.i("is register", "found");
                    SplashScreenActivity.this.runOnUiThread(this::login);
                } else if (!isRegisterd) {
                    // Log.i("Not register", "found");


                    boolean islogout = SharedPrefHelper.getSharedBoolean(context, Constants.islogout);
                    if (islogout) {
                        Intent intent1 = new Intent(SplashScreenActivity.this, LoginScreen.class);
                        if (!firsttrack.equalsIgnoreCase("")) {
                            intent1.putExtra("Trackid", firsttrack);
                        }
                        if (!firstvideo.equalsIgnoreCase("")) {
                            intent1.putExtra("Videoid", firstvideo);
                        }
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent1);
                    } else {
                        Intent intent1 = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                        if (!firsttrack.equalsIgnoreCase("")) {
                            intent1.putExtra("Trackid", firsttrack);
                        }
                        if (!firstvideo.equalsIgnoreCase("")) {
                            intent1.putExtra("Videoid", firstvideo);
                        }
                        callFacebookLogout();
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent1);
                    }


                }


            } else {

                Log.i("NOooooooooooooooooo", "found");
                SplashScreenActivity.this.runOnUiThread(() -> {
                    // check user subscribe or no

                    if (SharedPrefHelper.getSharedString(context, Constants.UserStatusId) != null && !SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                        if (!checkOfflineFeature()) {
                            boolean isServiceRunning2 = UtilFunctions.isServiceRunning(NetworkChangeReceiver.class.getName(),
                                    context);
                            if (isServiceRunning2) {
                                Intent i = new Intent(context, NetworkChangeReceiver.class);
                                context.stopService(i);


                            }
                            Intent intent1 = new Intent(SplashScreenActivity.this, DownloadActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            finish();
                        } else {

                            Toast.makeText(SplashScreenActivity.this,
                                    getResources().getString(R.string.gnrl_internet_error), Toast.LENGTH_LONG).show();
                            SplashScreenActivity.this.finish();
                        }
                    } else {
                        Toast.makeText(SplashScreenActivity.this,
                                getResources().getString(R.string.gnrl_internet_error), Toast.LENGTH_LONG).show();
                        SplashScreenActivity.this.finish();
                    }
                });

            }
        }).start();





    }

    private static void setLastLogin(Context context) {
        try {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
            String formattedDate = df.format(c.getTime());
            c.setTime(df.parse(formattedDate));
            String dateInString = formattedDate;
//            c.add(Calendar.DATE, i);
//            df = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
            Date resultdate = new Date(c.getTimeInMillis());
//            String dateInString = df.format(resultdate);
            System.out.println("String date:" + dateInString);
            SharedPrefHelper.setSharedString(context, Constants.DATE_LASTLOGIN, dateInString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private Boolean checkOfflineFeature() {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());

        try {
            Date lastlogindate = simpleDateFormat.parse(SharedPrefHelper.getSharedString(context, Constants.DATE_LASTLOGIN));

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());

            Calendar calendar = Calendar.getInstance();
            Date todaydate = calendar.getTime();
            String todayAsString = dateFormat.format(todaydate);
            Date todaydatee = simpleDateFormat.parse(todayAsString);

            //Comparing dates
            long difference = Math.abs(todaydatee.getTime() - lastlogindate.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            System.out.println("differenceDates=" + differenceDates);

            String ServiceID = SharedPrefHelper.getSharedString(context, Constants.ServiceID);
            System.out.println("ServiceID=" + ServiceID);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);
            System.out.println("dayDifference=" + dayDifference);

            if (ServiceID.equalsIgnoreCase("11"))
            //monthly
            {
                if (differenceDates > 30) {
                    checkdate = true;
                } else {
                    checkdate = false;
                }
            } else if (ServiceID.equalsIgnoreCase("12"))
            //daily
            {
                if (differenceDates > 1) {
                    checkdate = true;
                } else {
                    checkdate = false;
                }
            }
            Log.e("HERE", "HEREdiffdate: " + dayDifference);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return checkdate;

    }

    private Boolean check_Serviceid() {
        String yesterday = SharedPrefHelper.getSharedString(context, Constants.DATE_LASTLOGIN);
        if (yesterday != null && !yesterday.equalsIgnoreCase("")) {

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());

            Calendar calendar = Calendar.getInstance();
            Date todaydate = calendar.getTime();

            System.out.println(todaydate);

            String todayAsString = dateFormat.format(todaydate);
            System.out.println(todayAsString);
            try {
                enddate = dateFormat.parse(yesterday);
                today = dateFormat.parse(todayAsString);
                System.out.println(enddate);
                System.out.println(today);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (today.after(enddate)) {
                System.out.println(true);
                checkdate = true;
            } else {
                System.out.println(false);
                checkdate = false;
            }
        } else {
            checkdate = true;
        }
        return checkdate;
    }


    private void InitialzeData() {
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());

        if (language.equalsIgnoreCase("ar")) {

            langtype = "2";
        }

        if (language.equalsIgnoreCase("en")) {

            langtype = "1";

        }
        HandleSchema();

        // Apps can easily check the Extras from the App Link as well.

    }

    private void HandleSchema() {
        Uri data = getIntent().getData();
        if (data != null) {
            @SuppressWarnings("unused") String scheme = data.getScheme(); // "http"

            @SuppressWarnings("unused") String host = data.getHost(); // "twitter.com"
            List<String> params = data.getPathSegments();
            // "status" (trackid)

            String firstparam = params.get(0);
            if (firstparam.equalsIgnoreCase("track")) {
                firsttrack = params.get(1);
            }
            // "status" (videoid)

            if (firstparam.equalsIgnoreCase("video")) {
                firstvideo = params.get(1);
            }
            Log.i("Activitydata", "App Link Target URL: " + firsttrack);
            Log.i("Activitydata", "App Link Target URL video: " + firstvideo);


        }
    }

    public void login() {
        Log.d("Login Found", "heree");
        RetrofitPresenter_User_Class retrofitPresenter = new RetrofitPresenter_User_Class(this, this);
        retrofitPresenter.getLoginResponse(langtype);

    }


    @SuppressWarnings("EmptyMethod")
    @Override
    protected void onResume() {
        super.onResume();

    }

    public void handleResponseUi(User mresult) {
        User mUser = new User();

        userID = mresult.getUserID();
        msisdn = mresult.getMsisdn();
        ISACTIVE = mresult.getIsActivated();
        UserImage = mresult.getUserImage();
        accesstoken = mresult.getUserToken();
        UserStatusId = mresult.getIsSubsc();
        ServiceID = mresult.getServiceID();
        IsFacebookReg = mresult.getIsFacebookReg();
        String genreId = mresult.getGenreId();
        String downloadKey = mresult.getDownloadKey();
        String UserErrorType = mresult.getUserErrorType();
        String SubscEndDate = mresult.getSubscEndDate();
        String ServiceDailyPrice = mresult.getServiceDailyPrice();
        Boolean AllowInternational = mresult.getAllowInternational();

        String ServiceMonthlyPrice = mresult.getServiceMonthlyPrice();
        System.out.println("ServiceMonthlyPrice" + ServiceMonthlyPrice);
        System.out.println("ServiceDailyPrice" + ServiceDailyPrice);

        // ISACTIVE=false;
        mUser.setUserID(userID);
        mUser.setMsisdn(msisdn);
        mUser.setIsActivated(ISACTIVE);
        mUser.setUserImage(UserImage);
        mUser.setUserToken(accesstoken);
        mUser.setIsSubsc(UserStatusId);
        mUser.setServiceID(ServiceID);
        mUser.setIsFacebookReg(IsFacebookReg);
        mUser.setGenreId(genreId);
        mUser.setDownloadKey(downloadKey);
        mUser.setSubscEndDate(SubscEndDate);
        mUser.setServiceDailyPrice(ServiceDailyPrice);
        mUser.setServiceMonthlyPrice(ServiceMonthlyPrice);
        mUser.setAllowInternational(AllowInternational);
        SharedPrefHelper.setSharedBoolean(context, Constants.AllowInternational, mUser.getAllowInternational());
        SharedPrefHelper.setSharedString(context, Constants.DATE_SUBSCRIBE, mUser.getSubscEndDate());
        SharedPrefHelper.setSharedString(context, Constants.ServiceDailyPrice, mUser.getServiceDailyPrice());

        SharedPrefHelper.setSharedString(context, Constants.ServiceMonthlyPrice, mUser.getServiceMonthlyPrice());

        SharedPrefHelper.setSharedInt(context, Constants.GenreId, Integer.parseInt(mUser.getGenreId()));
        SharedPrefHelper.setSharedString(context, Constants.isFaceReg, mUser.getIsFacebookReg());

        SharedPrefHelper.setSharedString(context, Constants.DownloadKey, mUser.getDownloadKey());

        SharedPrefHelper.setSharedString(context, Constants.USERID, mUser.getUserID());
        SharedPrefHelper.setSharedString(context, Constants.UserImage, mUser.getUserImage());
        SharedPrefHelper.setSharedString(context, Constants.accesstoken, mUser.getUserToken());
        SharedPrefHelper.setSharedString(context, Constants.UserStatusId, mUser.getIsSubsc());
        SharedPrefHelper.setSharedString(context, Constants.ServiceID, mUser.getServiceID());
        SharedPrefHelper.getSharedString(context, Constants.UserStatusId);
        if (SharedPrefHelper.getSharedString(context, Constants.UserStatusId) != null && !SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {

            setLastLogin(context);
        }
        SharedPrefHelper.setSharedString(context, Constants.UserImage, mUser.getUserImage());


        int number = 0;

        try {

            number = Integer.parseInt(userID);

        } catch (NumberFormatException e) {

            System.out.println("parse value is not valid : " + e);

        }
        // diff sim
        if (number <= 0 && UserErrorType != null && UserErrorType.equalsIgnoreCase("1")) {
            showAlert();
        }

        //incorrect passward

        else if (number <= 0 && UserErrorType.equalsIgnoreCase("2")) {
            Toast.makeText(SplashScreenActivity.this, getResources().getString(R.string.invalidpassword),
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SplashScreenActivity.this, LoginScreen.class);
            thread2 = null;


            if (!firsttrack.equalsIgnoreCase("")) {
                intent.putExtra("Trackid", firsttrack);
            }


            if (!firstvideo.equalsIgnoreCase("")) {
                intent.putExtra("Videoid", firstvideo);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        } else if (number <= 0) {
            Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
            thread2 = null;


            if (!firsttrack.equalsIgnoreCase("")) {
                intent.putExtra("Trackid", firsttrack);
            }


            if (!firstvideo.equalsIgnoreCase("")) {
                intent.putExtra("Videoid", firstvideo);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();

        }

        if (ISACTIVE && number > 0) {
            // finish();
            Log.e("ACTIVE", " msg");
            // finish();
            Log.e("ACTIVE", " msg");
            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);

            if (!firsttrack.equalsIgnoreCase("")) {
                intent.putExtra("Trackid", firsttrack);
            }

            if (!firstvideo.equalsIgnoreCase("")) {
                intent.putExtra("Videoid", firstvideo);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        } else if (!ISACTIVE && number > 0) {
            Log.d(TAG, "Nav to ActivationScreen");
            Intent intent = new Intent(SplashScreenActivity.this, ActivationScreen.class);
            if (!firsttrack.equalsIgnoreCase("")) {
                intent.putExtra("Trackid", firsttrack);
            }

            if (!firstvideo.equalsIgnoreCase("")) {
                intent.putExtra("Videoid", firstvideo);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            startActivity(intent);
            finish();

        }


    }

    @SuppressWarnings("ConstantConditions")
    private void showAlert() {
        Dialog dialog1 = new Dialog(SplashScreenActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog1.setContentView(R.layout.loginpopup);

        Button cancel = dialog1.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog1.dismiss());

        Button confirm = dialog1.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
            intent.putExtra("Trackid", firsttrack);
            intent.putExtra("Videoid", firstvideo);
            dialog1.dismiss();
            startActivity(intent);
        });
        if (dialog1 != null && !dialog1.isShowing()) {
            //show dialog
            dialog1.show();

        }
    }

    @Override
    public void responseRbtReady(Addrbtresponse addrbtresponses, int code, int functiontype) {

    }

    @Override
    public void responseUserReady(User mUser, int code, int functiontype) {
        if (mUser != null) {

            handleResponseUi(mUser);
            //convert json string to object

        } else {
            ApiUtils.handelErrorCode(getApplicationContext(), code);
            System.out.println("onFailure");

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public  void new_sim(){
        SubscriptionManager sm = SubscriptionManager.from(SplashScreenActivity.this);

// it returns a list with a SubscriptionInfo instance for each simcard
// there is other methods to retrieve SubscriptionInfos (see [2])
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();

// getting first SubscriptionInfo
        SubscriptionInfo si = sis.get(0);


        Log.d("SubscriptionInfo" ,"" +si);

// getting iccId
        String iccId = si.getIccId();

        Log.d("SubscriptionInfo iccId " ,"" +iccId);
    }

}