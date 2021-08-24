package com.binarywaves.ghaneely.ghannelyactivites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.EncreptionClass;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter.RetrofitPresenter_User_Class;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.facebook.FacebookSdk;
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
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class LoginScreen extends ActivityMainRunnuing implements RetrofitPresenter_User_Class.RetrofitPresenterListener {

    private final String regexStr = "^(012)\\d{8}$";
    private final String TAG = LoginScreen.class.getSimpleName();
    private Activity activity;
    private RelativeLayout progBar;
    private ProgressWheel pb1;
    private Button mRegister;
    public static EditText mobilenumber_et;
    private EditText etpassword;
    private String iisFacereg;
    private TextView tvforgetpass,tvregister;
    private Dialog dialog;
    private String userID;
    private String msisdn;
    private String UserImage;
    private String accesstoken;
    private boolean ISACTIVE;
    private EncreptionClass enc;
    private Context mcontext;
    private Button btarabic;
    private Button btenglish;
    private Button arabicactive;
    private Button englishactive;
    private String langtype = "1";
    private String trackid;
    private String videoid;
    private Boolean mper_granted = false;
    private RxPermissions mPermissions;
    private int statusCode;
    private String tokenfcm;

    private static void showHashKey(Context context) {
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
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            SharedPrefHelper.setSharedString(context, Constants.PROPERTY_APP_VERSION,
                    packageInfo.versionName);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Login Screen On Create");
        FacebookSdk.sdkInitialize(getApplicationContext());
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.loginscreen);

        mcontext = getApplicationContext();
        activity = LoginScreen.this;
        mPermissions = new RxPermissions(this);

        InitialzeUi();
        onNewIntent(getIntent());
        getID();
        getAppVersion(mcontext);
        showHashKey(getApplicationContext());
        Thread thread2 = new Thread() {
            @Override
            public void run() {

                try {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginScreen.this,
                            new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            tokenfcm = instanceIdResult.getToken();
                            Log.e("newToken", tokenfcm);
                            Log.e("TAG", "Refreshed token: " + tokenfcm);
                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.tokenfcm, tokenfcm);
                            Log.e("TAGLogin", "SharedPrefHelper token: " + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.tokenfcm));

                        }
                    });


                    if (tokenfcm == null) {
                        Thread.sleep(3000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        thread2.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void InitialzeUi() {
        arabicactive = findViewById(R.id.Arabicactive);
        englishactive = findViewById(R.id.englishactive);
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);

        btarabic = findViewById(R.id.Arabic);
        btenglish = findViewById(R.id.english);
        tvforgetpass = findViewById(R.id.tvforgetpass);
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


        if (language.equalsIgnoreCase("ar")) {
            btarabic.setVisibility(View.GONE);
            arabicactive.setVisibility(View.VISIBLE);
            langtype = "2";
        }

        if (language.equalsIgnoreCase("en")) {
            btenglish.setVisibility(View.GONE);
            englishactive.setVisibility(View.VISIBLE);
            langtype = "1";

        }
        btarabic.setOnClickListener(v -> {
            new LanguageHelper().changeLanguage(activity, "ar");
            // reload login page to set it by new languageactive
            Intent mIntent = new Intent(LoginScreen.this, LoginScreen.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(mIntent);

        });
        btenglish.setOnClickListener(v -> {
            new LanguageHelper().changeLanguage(activity, "en");
            // reload login page to set it by new languageactive
            Intent mIntent = new Intent(LoginScreen.this, LoginScreen.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(mIntent);
        });

        mobilenumber_et = findViewById(R.id.mobilenumber);
//        Constants.GetHeaderInrech(mcontext);
        etpassword = findViewById(R.id.etpassword);
        etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mRegister = findViewById(R.id.register_btn);
        mRegister.setOnClickListener(v -> {
            // TODO Auto-generated method stub

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
           // startwhellprogress();
            mRegister.setEnabled(false);
            if (Constants.isNetworkOnline(LoginScreen.this)) {
                login();
            } else {
                Toast.makeText(LoginScreen.this,
                        getResources().getString(R.string.gnrl_internet_error), Toast.LENGTH_LONG).
                        show();
            }
        });
        tvforgetpass.setOnClickListener(v -> {
            showAlert_forgetpassword();

        });
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    protected void onResume() {
        super.onResume();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressWarnings({"ResourceType"})
    private void getID() {
        if (Build.VERSION.SDK_INT >= 23) {
            start();

        } else {
            TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            // String imsi = mTelephonyMgr.getSubscriberId();
//            @SuppressLint("HardwareIds") String imei = mTelephonyMgr != null ? mTelephonyMgr.getDeviceId() : null;
         //  @SuppressLint("HardwareIds") String simno = mTelephonyMgr != null ? mTelephonyMgr.getSimSerialNumber() : null;
            String imei = "10101010101001010010101010110";
            SubscriptionManager sm = SubscriptionManager.from(mcontext);

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
            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.SIM_NO, simno);
            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.IMEI, imei);
            mper_granted = true;


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
            // mRegister.setActivated(true);
            showAlertsubscripe();
            return false;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showAlertsubscripe() {
        dialog = new Dialog(LoginScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.nonmobpopup);

        ImageView cancel = dialog.findViewById(R.id.close_player);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        // if button is clicked, close the custom dialog

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressWarnings("ConstantConditions")
    private void showAlert_forgetpassword() {
        Dialog dialog = new Dialog(LoginScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.forgetpassword_popup);
        EditText etmobilenumber = dialog.findViewById(R.id.mobilenumber);
        Button change = dialog.findViewById(R.id.change);
        change.setOnClickListener(view -> {
            if (etmobilenumber.getText().length() > 0) {
                SharedPrefHelper.setSharedString(mcontext, Constants.MOBILE, etmobilenumber.getText().toString());

                SendForgetPassCode(etmobilenumber.getText().toString());
            } else {
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.EmptyField), Toast.LENGTH_LONG).show();

            }
            dialog.dismiss();
        });
        // if button is clicked, close the custom dialog
        ImageView cancel = dialog.findViewById(R.id.close_player);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        // if button is clicked, close the custom dialog

        dialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressWarnings("ConstantConditions")
    private void showAlert_UpdatePasswordFromCode() {
        Dialog dialog = new Dialog(LoginScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.updatepassword_popup);
        EditText etactivate = dialog.findViewById(R.id.etactivate);
        EditText etnewpass = dialog.findViewById(R.id.etnewpass);

        Button change = dialog.findViewById(R.id.change);
        change.setOnClickListener(view -> {
            if (etactivate.getText().length() > 0 && etnewpass.getText().length() > 0) {

                enc = new EncreptionClass("gh@neely@b!n@ryw@ves");
                enc.encryptAsBase64(etnewpass.getText().toString().getBytes());
                String encryptedpass = enc.encryptdatanew;
                UpdatePasswordFromCode(SharedPrefHelper.getSharedString(mcontext, Constants.MOBILE),
                        etactivate.getText().toString(), encryptedpass);
            } else {
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.EmptyField), Toast.LENGTH_LONG).show();

            }
            dialog.dismiss();
        });
        // if button is clicked, close the custom dialog
        ImageView cancel = dialog.findViewById(R.id.close_player);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        // if button is clicked, close the custom dialog

        dialog.show();
    }


    private boolean isEmpty() {
        return TextUtils.isEmpty(mobilenumber_et.getText().toString())
                || TextUtils.isEmpty(etpassword.getText().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void login() {
        getID();
        startwhellprogress();

        Log.d("Login Found", "heree");
        if (isEmpty()) {
            progBar.setVisibility(View.GONE);
            mRegister.setEnabled(true);
            Toast.makeText(LoginScreen.this, getResources().getString(R.string.EmptyField), Toast.LENGTH_LONG).show();

        } else {

            if (Constants.isNetworkOnline(this)) {
                if (!SharedPrefHelper.getSharedString(LoginScreen.this, Constants.SIM_NO).equalsIgnoreCase("") && !SharedPrefHelper.getSharedString(LoginScreen.this, Constants.IMEI).equalsIgnoreCase("")) { // username

                  // if (isValid() && mper_granted) {
                       if (true) {
                           enc = new EncreptionClass("gh@neely@b!n@ryw@ves");
                           enc.encryptAsBase64(etpassword.getText().toString().getBytes());
                           String newpass1 = enc.encryptdatanew;


                           RetrofitPresenter_User_Class retrofitPresenter = new RetrofitPresenter_User_Class(this, this);
                           retrofitPresenter.getloginResponse(mobilenumber_et.getText().toString(), newpass1, langtype,tokenfcm);


                    } else {
                        progBar.setVisibility(View.GONE);
                        mRegister.setEnabled(true);
                        // (true);
                        Toast.makeText(LoginScreen.this, getResources().getString(R.string.no_sim),
                                Toast.LENGTH_SHORT).show();
                        getID();

                    }
                } } else {
                progBar.setVisibility(View.GONE);
                mRegister.setEnabled(true);
                Log.d("no internet found", "FOUND");
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_SHORT).show();


            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void SendForgetPassCode(String text) {
        getID();
        startwhellprogress();
        Log.d("SendForgetPass Found", "heree");


        if (Constants.isNetworkOnline(this)) {
            if (mper_granted) {
                RetrofitPresenter_User_Class retrofitPresenter = new RetrofitPresenter_User_Class(this, this);
                retrofitPresenter.SendForgetPassCode(text.toString(), SharedPrefHelper.getSharedString(getApplicationContext(), Constants.IMEI));


            } else {
                progBar.setVisibility(View.GONE);
                // (true);
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.no_sim),
                        Toast.LENGTH_SHORT).show();
                getID();

            }
        } else {
            progBar.setVisibility(View.GONE);
            mRegister.setEnabled(true);
            Log.d("no internet found", "FOUND");
            Toast.makeText(LoginScreen.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();


        }
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
    private void UpdatePasswordFromCode(String mobilenum, String activate, String newpass) {
        getID();
        startwhellprogress();
        Log.d("SendForgetPass Found", "heree");


        if (Constants.isNetworkOnline(this)) {
            if (mper_granted) {
                RetrofitPresenter_User_Class retrofitPresenter = new RetrofitPresenter_User_Class(this, this);
                retrofitPresenter.UpdatePasswordFromCode(mobilenum.toString(), SharedPrefHelper.getSharedString(getApplicationContext(), Constants.IMEI), activate, newpass);


            } else {
                progBar.setVisibility(View.GONE);
                // (true);
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.no_sim),
                        Toast.LENGTH_SHORT).show();
                getID();

            }
        } else {
            progBar.setVisibility(View.GONE);
            mRegister.setEnabled(true);
            Log.d("no internet found", "FOUND");
            Toast.makeText(LoginScreen.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();


        }
    }

    private void handleResponseUi(User mresult) {
        User mUser = new User();

        if (!SharedPrefHelper.getSharedString(mcontext, Constants.SIM_NO).equalsIgnoreCase("") && !SharedPrefHelper.getSharedString(mcontext, Constants.IMEI).equalsIgnoreCase("")) { // username
            Log.i("onSuccess", "here");
            progBar.setVisibility(View.GONE);

            mRegister.setEnabled(true);
            // finish();

            progBar.setVisibility(View.GONE);
            userID = mresult.getUserID();
            String userimage = mresult.getUserImage();
            ISACTIVE = mresult.getIsActivated();
            iisFacereg = mresult.getIsFacebookReg();
            accesstoken = mresult.getUserToken();
            String UserStatusId = mresult.getUserStatusId();
            String ServiceID = mresult.getServiceID();
            String genreId = mresult.getGenreId();
            String downloadKey = mresult.getDownloadKey();
            Boolean AllowInternational = mresult.getAllowInternational();

            msisdn = mresult.getMsisdn();
            String UserErrorType = mresult.getUserErrorType();
            String ServiceDailyPrice = mresult.getServiceDailyPrice();

            String ServiceMonthlyPrice = mresult.getServiceMonthlyPrice();

            mUser.setIsSubsc(UserStatusId);
            mUser.setServiceID(ServiceID);
            mUser.setGenreId(genreId);

            mUser.setServiceID(ServiceID);
            mUser.setIsFacebookReg(iisFacereg);
            mUser.setGenreId(genreId);
            mUser.setUserToken(accesstoken);
            mUser.setDownloadKey(downloadKey);
            String SubscEndDate = mresult.getSubscEndDate();

            mUser.setServiceDailyPrice(ServiceDailyPrice);
            mUser.setServiceMonthlyPrice(ServiceMonthlyPrice);
            mUser.setSubscEndDate(SubscEndDate);
            mUser.setAllowInternational(AllowInternational);
            SharedPrefHelper.setSharedBoolean(mcontext, Constants.AllowInternational, AllowInternational);
            SharedPrefHelper.setSharedString(mcontext, Constants.DATE_SUBSCRIBE, SubscEndDate);

            SharedPrefHelper.setSharedString(mcontext, Constants.ServiceDailyPrice, ServiceDailyPrice);

            SharedPrefHelper.setSharedString(mcontext, Constants.ServiceMonthlyPrice, ServiceMonthlyPrice);

            SharedPrefHelper.setSharedBoolean(mcontext, Constants.ISREGISTERD, true);
            SharedPrefHelper.setSharedString(mcontext, Constants.MOBILE, mobilenumber_et.getText().toString());
            SharedPrefHelper.setSharedString(mcontext, Constants.isFaceReg, mUser.getIsFacebookReg());

            SharedPrefHelper.setSharedInt(mcontext, Constants.GenreId, Integer.parseInt(mUser.getGenreId()));
            int number = 0;

            try {

                number = Integer.parseInt(userID);

            } catch (NumberFormatException e) {

                System.out.println("parse value is not valid : " + e);

            }

            // diff sim
            if (number <= 0 && UserErrorType.equalsIgnoreCase("1")) {
                showAlert();
            }

            //incorrect passwort
            else if (number <= 0 && UserErrorType.equalsIgnoreCase("2")) {
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.invalidpassword),
                        Toast.LENGTH_LONG).show();
            } else if (ISACTIVE && number > 0) {
                // finish();
                Log.e("ACTIVE", " msg");
                SharedPrefHelper.setSharedBoolean(mcontext, Constants.islogout, false);
                SharedPrefHelper.setSharedString(mcontext, Constants.DownloadKey, downloadKey);

                SharedPrefHelper.setSharedBoolean(mcontext, Constants.ISREGISTERD, true);
                SharedPrefHelper.setSharedString(mcontext, Constants.MOBILE, mobilenumber_et.getText().toString());
                SharedPrefHelper.setSharedString(mcontext, Constants.isFaceReg, iisFacereg);

                SharedPrefHelper.setSharedString(mcontext, Constants.USERID, mUser.getUserID());
                SharedPrefHelper.setSharedString(mcontext, Constants.accesstoken, mUser.getUserToken());
                SharedPrefHelper.setSharedString(mcontext, Constants.UserStatusId, mUser.getIsSubsc());
                SharedPrefHelper.setSharedString(mcontext, Constants.ServiceID, mUser.getServiceID());
                SharedPrefHelper.setSharedInt(mcontext, Constants.GenreId, Integer.parseInt(genreId));
                if (SharedPrefHelper.getSharedString(mcontext, Constants.UserStatusId) != null && !SharedPrefHelper.getSharedString(mcontext, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(mcontext, Constants.UserStatusId).equalsIgnoreCase("1")) {

                    setLastLogin(mcontext);
                }

                String oldpass;
                try {
                    enc = new EncreptionClass("gh@neely@b!n@ryw@ves");
                    enc.encryptAsBase64(etpassword.getText().toString().getBytes());
                    oldpass = enc.encryptdatanew;
                    // oldpass = encryptText(etpassword.getText().toString());
                    // String oldpass1 = URLEncoder.encode(oldpass, "UTF-8");
                    SharedPrefHelper.setSharedString(mcontext, Constants.PASSWORD, oldpass);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                SharedPrefHelper.setSharedString(mcontext, Constants.USERID, userID);
                SharedPrefHelper.setSharedString(mcontext, Constants.UserImage, UserImage);

                Intent intent = new Intent(LoginScreen.this, HomeActivity.class);
                // mIntent.putExtra("uoloadimg", true);

                intent.putExtra("Trackid", trackid);
                intent.putExtra("Videoid", videoid);


                startActivity(intent);
            } else if (!ISACTIVE && number > 0) {
                Intent intent = new Intent(LoginScreen.this, ActivationScreen.class);

                intent.putExtra("Trackid", trackid);
                intent.putExtra("Videoid", videoid);

                startActivity(intent);
            }

        }
        else  {
            progBar.setVisibility(View.GONE);
            Toast.makeText(LoginScreen.this, getResources().getString(R.string.no_sim), Toast.LENGTH_SHORT)
                    .show();

        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showAlert() {
        final Dialog dialog1 = new Dialog(LoginScreen.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog1.setContentView(R.layout.loginpopup);

        Button cancel = dialog1.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog1.dismiss());

        Button confirm = dialog1.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, RegisterActivity.class);
            intent.putExtra("Trackid", trackid);
            intent.putExtra("Videoid", videoid);

            startActivity(intent);
            finish();
        });

        if(dialog1!=null&&!dialog1.isShowing())
        {
            //show dialog
            dialog1.show();

        }
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
//                            @SuppressLint("HardwareIds") String imei = mTelephonyMgr != null ? mTelephonyMgr.getDeviceId() : null;
                         //  @SuppressLint("HardwareIds") String simno = mTelephonyMgr != null ? mTelephonyMgr.getSimSerialNumber() : null;

                            String imei = "10101010101001010010101010110";
                            SubscriptionManager sm = SubscriptionManager.from(mcontext);

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
            // String imsi = mTelephonyMgr.getSubscriberId();
//            @SuppressLint("HardwareIds") String imei = mTelephonyMgr != null ? mTelephonyMgr.getDeviceId() : null;
     //        @SuppressLint("HardwareIds") String simno = mTelephonyMgr != null ? mTelephonyMgr.getSimSerialNumber() : null;
            String imei = "10101010101001010010101010110";
            SubscriptionManager sm = SubscriptionManager.from(mcontext);

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
            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.SIM_NO, simno);
            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.IMEI, imei);
            mper_granted = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void responseRbtReady(Addrbtresponse addrbtresponses, int code, int functiontype) {
        if (functiontype == 1) {
            if (addrbtresponses.getResultCode().equalsIgnoreCase("true")) {
                showAlert_UpdatePasswordFromCode();
                progBar.setVisibility(View.GONE);

            } else {
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.tryagain),
                        Toast.LENGTH_LONG).show();
                progBar.setVisibility(View.GONE);

            }
        }

        if (functiontype == 2) {
            if (addrbtresponses.getResultCode().equalsIgnoreCase("true")) {
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.sucess),
                        Toast.LENGTH_LONG).show();
                progBar.setVisibility(View.GONE);



            } else {
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.tryagain),
                        Toast.LENGTH_LONG).show();
                progBar.setVisibility(View.GONE);

            }
        }
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
//        SubscriptionManager sm = SubscriptionManager.from(LoginScreen.this);
//
//// it returns a list with a SubscriptionInfo instance for each simcard
//// there is other methods to retrieve SubscriptionInfos (see [2])
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();
//
//// getting first SubscriptionInfo
//        SubscriptionInfo si = sis.get(0);
//
//
//        Log.d("SubscriptionInfo" ,"" +si);
//
//// getting iccId
//         iccId = si.getIccId();
//
//        Log.d("SubscriptionInfo iccId " ,"" +iccId);
//    }

}
