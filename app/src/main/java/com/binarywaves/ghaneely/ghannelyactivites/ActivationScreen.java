package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter.RetrofitPresenter_User_Class;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;

@SuppressWarnings("EmptyMethod")
public class ActivationScreen extends ActivityMainRunnuing implements RetrofitPresenter_User_Class.RetrofitPresenterListener {

    private static ProgressWheel pb1;
    String language;
    private RelativeLayout progBar;
    private Activity activity;
    private Button activate_btn;
    private EditText activat_et;
    private TextView resend;
    private String trackid;
    private String userID;
    private String accesstoken;
    private boolean ISACTIVE;
    private String videoid;
    private String GenreId;
    private String DownloadKey;
    private Context context;
    private int statusCode;
    private RetrofitPresenter_User_Class retrofitPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     setContentView(R.layout.activity_activation_activity);
        context = getApplicationContext();
        activity = ActivationScreen.this;
        Init_UI();
        onNewIntent(getIntent());


    }

    private void Init_UI() {
        activate_btn = findViewById(R.id.active_btn);
        activat_et = findViewById(R.id.activationnumber);

        resend = findViewById(R.id.resend);

        String htmlString = context.getResources().getString(R.string.resendcodetext);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            resend.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));


        } else {
            resend.setText(Html.fromHtml(htmlString));
        }


        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);


        resend.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            resendActivation();

        });
        activate_btn.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            if (isEmpty()) {
                Toast.makeText(ActivationScreen.this, getResources().getString(R.string.EmptyField),
                        Toast.LENGTH_LONG).show();
            } else {
                AcivateFunction();
            }

        });

    }

    private void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @SuppressWarnings("EmptyMethod")
    @Override
    protected void onResume() {
        super.onResume();


    }

    private void resendActivation() {
        startwhellprogress();
        retrofitPresenter = new RetrofitPresenter_User_Class(this, this);
        retrofitPresenter.getResendActivationResponse();

    }


    private void AcivateFunction() {
        startwhellprogress();
        if (Constants.isNetworkOnline(this)) {
            retrofitPresenter = new RetrofitPresenter_User_Class(this, this);
            retrofitPresenter.getActivationResponse(activat_et.getText().toString().trim());


        } else {
            progBar.setVisibility(View.GONE);
            // (true);
            Log.d("no internet found", "FOUND");
            Toast.makeText(ActivationScreen.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();


        }

    }


    private void handleResponseUi(User mresult) {
        User mUser = new User();

        // (true);
        Log.e("Success ", "response is " + mresult + "");

        userID = mresult.getUserID();

        ISACTIVE = mresult.getIsActivated();
        accesstoken = mresult.getUserToken();
        GenreId = mresult.getGenreId();
        DownloadKey = mresult.getDownloadKey();
        //  String UserErrorType = mresult.getUserErrorType();
        String SubscEndDate = mresult.getSubscEndDate();
        String ServiceDailyPrice = mresult.getServiceDailyPrice();

        String ServiceMonthlyPrice = mresult.getServiceMonthlyPrice();
        Boolean AllowInternational = mresult.getAllowInternational();


        mUser.setGenreId(GenreId);

        mUser.setUserToken(accesstoken);
        mUser.setDownloadKey(DownloadKey);
        mUser.setSubscEndDate(SubscEndDate);
        mUser.setServiceDailyPrice(ServiceDailyPrice);
        mUser.setServiceMonthlyPrice(ServiceMonthlyPrice);
        mUser.setAllowInternational(AllowInternational);

        SharedPrefHelper.setSharedString(context, Constants.DATE_SUBSCRIBE, SubscEndDate);
        SharedPrefHelper.setSharedString(context, Constants.ServiceDailyPrice, ServiceDailyPrice);

        SharedPrefHelper.setSharedString(context, Constants.ServiceMonthlyPrice, ServiceMonthlyPrice);

        SharedPrefHelper.setSharedBoolean(context, Constants.ISREGISTERD, true);
        SharedPrefHelper.setSharedString(context, Constants.isFaceReg, mUser.getIsFacebookReg());

        SharedPrefHelper.setSharedInt(context, Constants.GenreId, Integer.parseInt(mUser.getGenreId()));
        if (Integer.parseInt(userID) > 0 && ISACTIVE) {
            SharedPrefHelper.setSharedBoolean(context, Constants.ISREGISTERD, true);
            SharedPrefHelper.setSharedString(context, Constants.USERID, userID);
            SharedPrefHelper.setSharedInt(context, Constants.GenreId, Integer.parseInt(GenreId));
            SharedPrefHelper.setSharedString(context, Constants.DownloadKey, DownloadKey);
            SharedPrefHelper.setSharedString(context, Constants.accesstoken, mUser.getUserToken());
            SharedPrefHelper.setSharedBoolean(context, Constants.AllowInternational, AllowInternational);
            Intent intent = new Intent(ActivationScreen.this, HomeActivity.class);
            if (trackid != null && !trackid.equalsIgnoreCase(""))
                intent.putExtra("Trackid", trackid);
            if (videoid != null && !videoid.equalsIgnoreCase(""))

                intent.putExtra("Videoid", videoid);

            startActivity(intent);
            finish();

        } else {
            Toast.makeText(ActivationScreen.this, getResources().getString(R.string.checkcode),
                    Toast.LENGTH_LONG).show();

        }
        progBar.setVisibility(View.GONE);

    }

    private boolean isEmpty() {
        return TextUtils.isEmpty(activat_et.getText().toString());
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

    @Override
    public void responseRbtReady(Addrbtresponse addrbtresponses, int code, int functiontype) {
        progBar.setVisibility(View.GONE);
        Log.i("resendcode", addrbtresponses.toString());
        if (addrbtresponses.getResultCode().equalsIgnoreCase("False")) {
            Toast.makeText(ActivationScreen.this, getResources().getString(R.string.waitmessage),
                    Toast.LENGTH_LONG).show();


        } else {
            ApiUtils.handelErrorCode(getApplicationContext(), code);
            System.out.println("onFailure");
            progBar.setVisibility(View.GONE);


        }
    }

    @Override
    public void responseUserReady(User mUser, int code, int functiontype) {
        statusCode = code;
        if (mUser != null) {

            handleResponseUi(mUser);
            //convert json string to object

        } else {
            ApiUtils.handelErrorCode(getApplicationContext(), statusCode);
            System.out.println("onFailure");

        }
    }


}
