package com.binarywaves.ghaneely.ghannelyactivites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.EncreptionClass;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter.RetrofitPresenter_User_Class;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.provider.Settings.Secure;

public class RegisterActivity extends ActivityMainRunnuing implements RetrofitPresenter_User_Class.RetrofitPresenterListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static String accesstoken;
    private static ProgressWheel pb1;
    private final String regexStr = "^(01)\\d{9}$";
    private final String langtype = "1";
    private RelativeLayout progBar;
    private Activity activity;
    private Context context;
    private Button mRegister;
    private Button face_btn;
    public static EditText mobilenumber_et;
    private EditText etpassword;
    private String userID;
    private boolean ISACTIVE;
    private String fbname = "";
    private String fbid = "";
    private String iisFacereg;
    private String isFaceReg1 = "false";
    private EncreptionClass enc;
    private Button btarabic;
    private Button btenglish;
    private Button arabicactive;
    private Button englishactive;
    private String trackid;
    private String tokenfcm;
    private Boolean mper_granted = false;
    private LoginButton loginBtn;
    private CallbackManager callbackManager;
    private String videoid;
    private RxPermissions mPermissions;
    String mobilenumber;
    private String android_id;
    private final FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
        @Override
        public void onSuccess(LoginResult loginResult) {

            // progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    (object, response) -> {

                        Log.e("response: ", response + "");
                        try {

                            isFaceReg1 = "true";
                            fbname = object.getString("name");
                            fbid = object.getString("id");
                            SharedPreferences prefs = getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(Constants.facebookId, fbid);
                            editor.putString(Constants.imagefb,
                                    "http://graph.facebook.com/" + fbid + "/picture?type=thumbnail"

                            );

                            editor.apply();
                            Register();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            // progressDialog.dismiss();
        }
    };

    @SuppressWarnings("unused")
    public static void showHashKey(Context context) {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = context.getPackageManager().getPackageInfo("com.binarywaves.Ghaneely",
                    PackageManager.GET_SIGNATURES); // Your package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }
    private static void setLastLogin( Context context) {
        try {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
            String formattedDate = df.format(c.getTime());
            c.setTime(df.parse(formattedDate));
            String dateInString = formattedDate;
//            c.add(Calendar.DATE, i);
//            df = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
//            Date resultdate = new Date(c.getTimeInMillis());
//            String dateInString = df.format(resultdate);
            System.out.println("String date:" + dateInString);
            SharedPrefHelper.setSharedString(context, Constants.DATE_LASTLOGIN, dateInString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Register Activity Created");
        FacebookSdk.sdkInitialize(getApplicationContext());
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);
//        new_sim();
        context = getApplicationContext();
        activity = RegisterActivity.this;
        mPermissions = new RxPermissions(this);

        Log.v("test abdo permission 11" , "test abdo permission 11");


        InitialzeUi();
        if( SharedPrefHelper.getSharedString(getApplicationContext(), Constants.tokenfcm)!=null&& !SharedPrefHelper.getSharedString(getApplicationContext(), Constants.tokenfcm).equalsIgnoreCase(""))
        {
            AddDeviceToken(SharedPrefHelper.getSharedString(getApplicationContext(), Constants.tokenfcm));

        }
        else{
            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    try {
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                tokenfcm = instanceIdResult.getToken();
                                Log.e("newToken", tokenfcm);
                                Log.e("TAG", "Refreshed token: " + tokenfcm);

                            }
                        });

                        SharedPrefHelper.setSharedString(getApplicationContext(), Constants.tokenfcm, tokenfcm);
                        Log.e("TAG", "SharedPrefHelper token: " + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.tokenfcm));

                        if (tokenfcm == null) {
                            Thread.sleep(3000);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
            thread2.start();}
        onNewIntent(getIntent());
    }

    private void AddDeviceToken(String deviceToken) {


        String fav = ServerConfig.SERVER_URl + ServerConfig.AddDeviceToken + "?&deviceToken="
                + deviceToken;
        String fav_url = fav.replaceAll(" ", "%20");
        Api_Interface service = ServiceGenerator.createService();

        service.AddDeviceToken(fav_url).enqueue(new Callback<Addrbtresponse>() {
            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> response) {

                Log.v("successtoken", response.body() + "");
                if (response.isSuccessful()) {
                    if (tokenfcm != null)
                        SharedPrefHelper.setSharedString(context, Constants.tokenfcm, tokenfcm);
                } else {
                    progBar.setVisibility(View.GONE);
                    //  ApiUtils.handelErrorCode(getApplicationContext(), response.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {

            }


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void InitialzeUi() {
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        arabicactive = findViewById(R.id.Arabicactive);
        englishactive = findViewById(R.id.englishactive);

        btarabic = findViewById(R.id.Arabic);
        btenglish = findViewById(R.id.english);

        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


        if (language.equalsIgnoreCase("ar")) {
            btarabic.setVisibility(View.GONE);
            arabicactive.setVisibility(View.VISIBLE);
        }

        if (language.equalsIgnoreCase("en")) {
            btenglish.setVisibility(View.GONE);
            englishactive.setVisibility(View.VISIBLE);
        }
        btarabic.setOnClickListener(v -> {
            new LanguageHelper().changeLanguage(activity, "ar");
            // reload login page to set it by new languageactive
            Intent mIntent = new Intent(RegisterActivity.this, RegisterActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(mIntent);
            finish();

        });
        btenglish.setOnClickListener(v -> {
            new LanguageHelper().changeLanguage(activity, "en");
            // reload login page to set it by new languageactive
            Intent mIntent = new Intent(RegisterActivity.this, RegisterActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(mIntent);
            finish();
        });
        // showAlertsubscripe();
        mobilenumber_et = findViewById(R.id.mobilenumber);
//        Constants.GetHeaderInrech(context);




        etpassword = findViewById(R.id.etpassword);

        etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mRegister = findViewById(R.id.register_btn);
        face_btn = findViewById(R.id.face_btn);

        getID();
        //  showHashKey(getApplicationContext());
        // Log.e("Register in RegisterActivity",
        // prefs.getString(Constants.PROPERTY_REG_ID, "") );
        mRegister.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            isFaceReg1 = "false";
            startwhellprogress();
            mRegister.setEnabled(false);
            // //(false);
            Register();

        });

        loginBtn = findViewById(R.id.fb_login_button);
        face_btn.setVisibility(View.GONE);
        loginBtn.setVisibility(View.GONE);
        loginBtn.setReadPermissions(Collections.singletonList("email"));
        loginBtn.setReadPermissions(Collections.singletonList("user_friends"));
        loginBtn.setOnClickListener(v -> {

            // progressDialog = new ProgressDialog(RegisterActivity.this);
            // progressDialog.setMessage("Loading...");
            // progressDialog.show();
            loginBtn.registerCallback(callbackManager, mCallBack);
            // loginBtn.setVisibility(View.VISIBLE);

            // Your query to fetch Data

        });

        face_btn.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (!isEmpty()) {
                // face_btn.setVisibility(View.GONE);
                loginBtn.performClick();
                // Your query to fetch Data
            } else {

                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.EmptyField),
                        Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        callbackManager = CallbackManager.Factory.create();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
    */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint("HardwareIds")
    @SuppressWarnings({"ResourceType"})
    private void getID() {
        if (Build.VERSION.SDK_INT >= 23) {
            start();
        } else {
            TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            // String imsi = mTelephonyMgr.getSubscriberId();
            assert mTelephonyMgr != null;

//            String imei = mTelephonyMgr.getDeviceId();

            String imei = "10101010101001010010101010110";
            SubscriptionManager sm = SubscriptionManager.from(context);

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
          String  iccId = si.getIccId();

            Log.d("SubscriptionInfo iccId " ,"" +iccId);
         String   simno =  iccId ;

        //  @SuppressLint("HardwareIds") String simno = mTelephonyMgr.getSimSerialNumber();
            Log.v("simno", "11" + simno);
            Log.v("imei", "11" + imei);

            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.SIM_NO, simno);
            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.IMEI, imei);
            mper_granted = true;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void Register() {
        getID();

        if (isEmpty() && isFaceReg1.equalsIgnoreCase("false")) {
            progBar.setVisibility(View.GONE);
            mRegister.setEnabled(true);
            // (true);
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.EmptyField), Toast.LENGTH_LONG)
                    .show();
        } else {
            if (Constants.isNetworkOnline(this)) {

                if (!SharedPrefHelper.getSharedString(context, Constants.SIM_NO).equalsIgnoreCase("") && !SharedPrefHelper.getSharedString(context, Constants.IMEI).equalsIgnoreCase("")) { // username

                    if (isValid() && mper_granted) {

                        String buf = Build.VERSION.RELEASE + "," + Build.VERSION.INCREMENTAL + "," + Build.VERSION.SDK_INT + ","
                                + Build.FINGERPRINT + "," + Build.BOARD + "," + Build.BRAND + "," + Build.DEVICE + ","
                                + Build.MANUFACTURER + "," + Build.MODEL + "," + SharedPrefHelper.getSharedString(context, Constants.PROPERTY_APP_VERSION);
                        Log.i("devicedata", buf + "");

                        enc = new EncreptionClass("gh@neely@b!n@ryw@ves");
                        enc.encryptAsBase64(etpassword.getText().toString().getBytes());
                        String newpass1 = enc.encryptdatanew;
                        RetrofitPresenter_User_Class retrofitPresenter = new RetrofitPresenter_User_Class(this, this);
                        retrofitPresenter.getRegisterResponse(buf, mobilenumber_et.getText().toString(), isFaceReg1,
                                fbname, fbid, newpass1, langtype);


                    } } else {
                    progBar.setVisibility(View.GONE);
                    mRegister.setEnabled(true);
                    // (true);
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.no_sim),
                            Toast.LENGTH_SHORT).show();
                    getID();

                }
            } else {
                progBar.setVisibility(View.GONE);
                mRegister.setEnabled(true);
                // (true);
                Log.d("no internet found", "FOUND");
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_SHORT).show();


            }
        }
    }


    private void handleResponseUi(User mresult) {
        User mUser = new User();
        SharedPrefHelper.setSharedBoolean(context, Constants.FirstTimeTODownload, true);
        if (!SharedPrefHelper.getSharedString(context, Constants.SIM_NO).equalsIgnoreCase("") && !SharedPrefHelper.getSharedString(context, Constants.IMEI).equalsIgnoreCase("")) { // username
            mRegister.setEnabled(true);
            // (true);
            Log.e("Success ", "response is " + mresult + "");

            userID = mresult.getUserID();
            String userimage = mresult.getUserImage();
            ISACTIVE = mresult.getIsActivated();
            iisFacereg = mresult.getIsFacebookReg();
            accesstoken = mresult.getUserToken();
            String UserStatusId = mresult.getUserStatusId();
            String ServiceID = mresult.getServiceID();
            String genreId = mresult.getGenreId();
            String downloadKey = mresult.getDownloadKey();
            String SubscEndDate = mresult.getSubscEndDate();
            String ServiceDailyPrice = mresult.getServiceDailyPrice();

            String ServiceMonthlyPrice = mresult.getServiceMonthlyPrice();
            Boolean AllowInternational = mresult.getAllowInternational();

            mUser.setIsSubsc(UserStatusId);
            mUser.setServiceID(ServiceID);
            mUser.setGenreId(genreId);
            mUser.setIsSubsc(UserStatusId);
            mUser.setServiceID(ServiceID);
            mUser.setIsFacebookReg(iisFacereg);
            mUser.setGenreId(genreId);
            mUser.setUserToken(accesstoken);
            mUser.setDownloadKey(downloadKey);
            mUser.setSubscEndDate(SubscEndDate);
            mUser.setServiceDailyPrice(ServiceDailyPrice);
            mUser.setServiceMonthlyPrice(ServiceMonthlyPrice);
            mUser.setAllowInternational(AllowInternational);
            SharedPrefHelper.setSharedBoolean(context, Constants.AllowInternational, AllowInternational);

            SharedPrefHelper.setSharedString(context, Constants.DATE_SUBSCRIBE, SubscEndDate);
            SharedPrefHelper.setSharedString(context, Constants.ServiceDailyPrice, ServiceDailyPrice);

            SharedPrefHelper.setSharedString(context, Constants.ServiceMonthlyPrice, ServiceMonthlyPrice);
            SharedPrefHelper.setSharedBoolean(context, Constants.ISREGISTERD, true);
            SharedPrefHelper.setSharedString(context, Constants.MOBILE, mobilenumber_et.getText().toString());
            SharedPrefHelper.setSharedString(context, Constants.isFaceReg, mUser.getIsFacebookReg());
            SharedPrefHelper.setSharedInt(context, Constants.GenreId, Integer.parseInt(mUser.getGenreId()));

            String oldpass;
            try {
                enc = new EncreptionClass("gh@neely@b!n@ryw@ves");
                enc.encryptAsBase64(etpassword.getText().toString().getBytes());
                oldpass = enc.encryptdatanew;
                SharedPrefHelper.setSharedString(context, Constants.PASSWORD, oldpass);

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            SharedPrefHelper.setSharedString(context, Constants.USERID, userID);
            SharedPrefHelper.setSharedString(context, Constants.accesstoken, accesstoken);
            SharedPrefHelper.setSharedString(context, Constants.DownloadKey, downloadKey);

            SharedPrefHelper.setSharedString(context, Constants.UserImage, userimage);
            SharedPrefHelper.setSharedString(context, Constants.UserStatusId, mUser.getIsSubsc());
            SharedPrefHelper.setSharedString(context, Constants.ServiceID, mUser.getServiceID());
            if (SharedPrefHelper.getSharedString(context, Constants.UserStatusId) != null && !SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {

                setLastLogin(context);
            }
            // shimaa update we comment here to prevent send sms to user and view
            // directly mainscreen
            Log.i("isfbRegisterd", SharedPrefHelper.getSharedString(context, Constants.isFaceReg));

            int number = 0;

            try {
                if(!userID.equalsIgnoreCase(null))
                    number = Integer.parseInt(userID);

            } catch (NumberFormatException e) {

                System.out.println("parse value is not valid : " + e);

            }

            if (UserStatusId.equalsIgnoreCase("-1")) {
                Log.i("false", "here");
                progBar.setVisibility(View.GONE);
                mRegister.setEnabled(true);
                showAlertsubscripe();

            } else {
                if (ISACTIVE && number > 0) {
                    progBar.setVisibility(View.GONE);
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    if (trackid != null && !trackid.equalsIgnoreCase(""))
                        intent.putExtra("Trackid", trackid);
                    if (videoid != null && !videoid.equalsIgnoreCase(""))

                        intent.putExtra("Videoid", videoid);

                    startActivity(intent);
                    finish();

                } else if (!ISACTIVE && number > 0) {
                    progBar.setVisibility(View.GONE);
                    Intent intent = new Intent(RegisterActivity.this, ActivationScreen.class);

                    intent.putExtra("Trackid", trackid);
                    intent.putExtra("Videoid", videoid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (number <= 0) {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.userlesszero), Toast.LENGTH_SHORT)
                            .show();

                }
            }


        }
        else  {
            progBar.setVisibility(View.GONE);
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.no_sim), Toast.LENGTH_SHORT)
                    .show();

        }
    }


    private boolean isValid() {

        Log.e("number", mobilenumber_et.getText().toString());
        if (Pattern.matches(regexStr, mobilenumber_et.getText().toString())) {
            // if (mMobile_et.getText().toString().matches(regexStr)){
            Log.i("true", "here");
            return true;
        } else {
            Log.i("false", "here");
            progBar.setVisibility(View.GONE);
            mRegister.setEnabled(true);
            // (true);
            showAlertsubscripe();
            return false;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showAlertsubscripe() {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.ghaneely_mobilenumber);

        Button cancel = dialog.findViewById(R.id.close_player);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        // if button is clicked, close the custom dialog

        dialog.show();
    }

    private boolean isEmpty() {
        return TextUtils.isEmpty(mobilenumber_et.getText().toString())
                || TextUtils.isEmpty(etpassword.getText().toString());
    }


    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("Trackid")) {
                trackid = extras.getString("Trackid");

            }
            if (extras.containsKey("Videoid")) {
                videoid = extras.getString("Videoid");

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint("HardwareIds")
    @SuppressWarnings({"ResourceType"})
    private void start() {
        boolean isPermissionsGranted
                = mPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && mPermissions.isGranted(Manifest.permission.READ_PHONE_STATE) && mPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!isPermissionsGranted) {
            mPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        // not record first time to request permission
                        if (granted) {
                            TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            // String imsi = mTelephonyMgr.getSubscriberId();
                            assert mTelephonyMgr != null;


                        //    String imei = mTelephonyMgr.getDeviceId();
                  //      String simno = mTelephonyMgr.getSimSerialNumber();
                            SubscriptionManager sm = SubscriptionManager.from(context);

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
                            String  iccId = si.getIccId();

                            Log.d("SubscriptionInfo iccId " ,"" +iccId);
                            String   simno =  iccId ;
                            String imei = "10101010101001010010101010110";
                          //  String simno = "1203222990";


                            Log.v("simno", "22" + simno);
                            Log.v("iccId", "22" + iccId);

                            Log.v("imei", "22" + imei);
                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.SIM_NO, simno);
                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.IMEI, imei);
                            mper_granted = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Permission not granted",
                                    Toast.LENGTH_SHORT).show();
                            mper_granted = false;
                        }
                    }, Throwable::printStackTrace);
        } else {
            TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            assert mTelephonyMgr != null;


//            String imei = mTelephonyMgr.getDeviceId();
        //   String simno = mTelephonyMgr.getSimSerialNumber();

            String imei = "10101010101001010010101010110";
           //  String simno = "1203222990";
            SubscriptionManager sm = SubscriptionManager.from(context);

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
            String  iccId = si.getIccId();

            Log.d("SubscriptionInfo iccId " ,"" +iccId);
            String   simno =  iccId ;

            Log.v("simno", "" + simno);
            Log.v("imei", "" + imei);
            SharedPreferences prefs = getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.SIM_NO, simno);
            editor.putString(Constants.IMEI, imei);
            editor.apply();
            mper_granted = true;
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

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
//    public  void new_sim(){
//
//    }

}