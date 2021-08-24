package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.EncreptionClass;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.BaseActivity;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.ChangepassResponse;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.facebook.login.LoginManager;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ConstantConditions")
public class SeetingActivity extends BaseActivity {
    private static RelativeLayout progBar;
    private static ProgressWheel pb1;
    private static int RESULT_LOAD_IMG = 1;
    Button change;
    Button btdailysub, btmonthsub;
    ImageView back;
    String id;
    private Activity activity;
    private LinearLayout logout_layout;
    private LinearLayout upload_layout;
    private LinearLayout changepass_layout;
    private Dialog dialog;
    private String encodedString;
    private byte[] byte_arr;
    private String imgPath;
    private String fileName;
    private Bitmap bitmap;
    private Button btarabic;
    private Button btenglish;
    private Button arabicactive;
    private Button englishactive;
    private LinearLayout subscripe_layout;
    private EncreptionClass enc;
    private String langtype;
    private String language;
    private TextView footertxt;
    private ImageView ivfooter;
    private View activityView;
    private Dialog dialogcamera;
    private ProgressDialog mProgressDialog;

    private  void callFacebookLogout(Context context) {
        LoginManager.getInstance().logOut();
        SharedPreferences prefs = context.getSharedPreferences("GhaniliPreffacebook", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        Log.i("logoutfromfb", "logout");

    }

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_favorites);
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;

        if (layoutInflater != null) {
            activityView = layoutInflater.inflate(R.layout.activity_setting_screen, nullparent, false);
        }
        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        context = getApplicationContext();
        language = LanguageHelper.getCurrentLanguage(context);

        progBar.setClickable(false);
        language = LanguageHelper.getCurrentLanguage(getApplicationContext());
        activity = SeetingActivity.this;
        Applicationmanager.setContext(SeetingActivity.this);

        selectedListItem = 9;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }
        }
        init();
        frameLayout.addView(activityView);


    }

    private void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }




    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = null;
                if (selectedImage != null) {
                    cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                }
                // Move to first row

                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                }
                if (cursor != null) {
                    imgPath = cursor.getString(columnIndex);
                }
                cursor.close();
                ImageView imgView = dialogcamera.findViewById(R.id.ivprofile);
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php
                // web app
                // params.put("fileName", fileName);

            } else {

                Toast.makeText(this, R.string.notpicfound, Toast.LENGTH_LONG).show();


            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.wrongthing, Toast.LENGTH_LONG).show();
        }

    }
    private static int setColor(Context context, int id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return context.getColor(id);
            } else {
                //noinspection deprecation
                return context.getResources().getColor(id);
            }
    }
    private void init() {
        // TODO Auto-generated method stub
        subscripe_layout = activityView.findViewById(R.id.subscripe_layout);
        footertxt = subscripe_layout.findViewById(R.id.tv);
        ivfooter = subscripe_layout.findViewById(R.id.ivfooter);

        if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
            subscripe_layout.setBackgroundColor(setColor(context,R.color.darkgray));
            footertxt.setText(getResources().getString(R.string.unsubghannelypremuim));
            ivfooter.setVisibility(View.GONE);


        } else   if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {

            subscripe_layout.setBackgroundColor(setColor(context,R.color.darkgray));
            footertxt.setText(getResources().getString(R.string.unsubghannelypremuim));
            ivfooter.setVisibility(View.GONE);
        } else{
            subscripe_layout.setBackgroundColor(setColor(context,R.color.ghaneely_orange));

            footertxt.setText(getResources().getString(R.string.getghannelypremuim));


        }
        subscripe_layout.setOnClickListener(v -> {
            if (footertxt.getText().toString().equalsIgnoreCase(getResources().getString(R.string.getghannelypremuim))) {

                GetSubscribeOption(getApplicationContext());
            }


            if (footertxt.getText().toString().equalsIgnoreCase(getResources().getString(R.string.unsubghannelypremuim))) {
                ShowConfirmationUnsubscribe(activity);
            }

        });


        arabicactive = activityView.findViewById(R.id.arabicactive);
        englishactive = activityView.findViewById(R.id.Englishactive);

        btarabic = activityView.findViewById(R.id.arabic);
        btarabic.setOnClickListener(v -> showAlertlanguage(0));
        btenglish = activityView.findViewById(R.id.English);
        btenglish.setOnClickListener(v -> showAlertlanguage(1));

        if (language.equalsIgnoreCase("ar")) {
            btarabic.setVisibility(View.GONE);
            arabicactive.setVisibility(View.VISIBLE);
        }

        if (language.equalsIgnoreCase("en")) {
            btenglish.setVisibility(View.GONE);
            englishactive.setVisibility(View.VISIBLE);
        }

        changepass_layout = activityView.findViewById(R.id.changepass_layout);
        changepass_layout.setOnClickListener(v -> Show_Changepassword_Dialog());

        upload_layout = activityView.findViewById(R.id.upload_layout);
        String isfbRegisterd = SharedPrefHelper.getSharedString(getApplicationContext(), Constants.isFaceReg);
        if (isfbRegisterd.equalsIgnoreCase("false")) {
            upload_layout.setVisibility(View.VISIBLE);
        }
        if (isfbRegisterd.equalsIgnoreCase("true")) {
            upload_layout.setVisibility(View.GONE);

        }
        upload_layout.setOnClickListener(v -> Showupload_popup());


        logout_layout = activityView.findViewById(R.id.logout_layout);
        logout_layout.setOnClickListener(v -> showAlert());
/*
        about_layout = (RelativeLayout) findViewById(R.id.about_layout);
		about_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(SeetingActivity.this, AboutGhaneelyActivity.class);

				startActivity(mIntent);
			}
		});
*/
    }

    @SuppressWarnings("ConstantConditions")
    private void Showupload_popup() {
        dialogcamera = new Dialog(SeetingActivity.this);
        dialogcamera.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcamera.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogcamera.setCanceledOnTouchOutside(false);
        dialogcamera.setContentView(R.layout.uploadpicture_popup);

        Button cancel = dialogcamera.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialogcamera.dismiss());

        Button upload = dialogcamera.findViewById(R.id.upload);
        // if button is clicked, close the custom dialog
        upload.setOnClickListener(this::uploadImage);

        ImageView ivcamera = dialogcamera.findViewById(R.id.ivcamera);
        ivcamera.setOnClickListener(this::loadImagefromGallery);

        dialogcamera.show();
    }

    @SuppressWarnings("ConstantConditions")
    private void Show_Changepassword_Dialog() {
        dialog = new Dialog(SeetingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.changepassword_popup);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.getWindow().setLayout(width, height);
        Log.v("width", width + "");
        Button cancel = dialog.findViewById(R.id.cancel);
        final EditText oldPassword = dialog.findViewById(R.id.old_password);
        final EditText newPassword = dialog.findViewById(R.id.new_password);

        final EditText retypeNewPassword = dialog.findViewById(R.id.retype_new_password);

        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            if (!oldPassword.getText().toString().equalsIgnoreCase("")
                    && !newPassword.getText().toString().equalsIgnoreCase("")
                    && !retypeNewPassword.getText().toString().equalsIgnoreCase("")) {
                if (newPassword.getText().toString().equalsIgnoreCase(retypeNewPassword.getText().toString())) {
                    try {
                        updatepass(oldPassword.getText().toString(), newPassword.getText().toString());

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), R.string.password_notmatch, Toast.LENGTH_LONG).show();


                }

            } else {
                if (language.equalsIgnoreCase("ar")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.requiredfields,
                            Toast.LENGTH_LONG).show();

                }
                if (language.equalsIgnoreCase("en")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.requiredfields, Toast.LENGTH_LONG).show();
                }

            }

        });

        dialog.show();
    }

    // change languageactive dialog
    private void showAlertlanguage(final int i) {
        dialog = new Dialog(SeetingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.changelang_message);

        Button cancel = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            if (i == 0) {
                new LanguageHelper().changeLanguage(this, "ar");

                dialog.dismiss();
                langtype = "2";
                updatelanguage(langtype, getApplicationContext());
                // reload login page to set it by new languageactive
                Intent mIntent = new Intent(SeetingActivity.this, HomeActivity.class);
                mIntent.putExtra("changelanguage", true);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(mIntent);
                startActivity(mIntent);


            }
            if (i == 1) {
                new LanguageHelper().changeLanguage(this, "en");

                dialog.dismiss();

                langtype = "1";
                updatelanguage(langtype, getApplicationContext());
                // reload login page to set it by new languageactive
                Intent mIntent = new Intent(SeetingActivity.this, HomeActivity.class);
                mIntent.putExtra("changelanguage", true);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(mIntent);

            }
        });

        dialog.show();
    }

    @SuppressWarnings("ConstantConditions")
    private void showAlert() {
        dialog = new Dialog(SeetingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.logoutdialog);

        Button cancel = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            StopService();
            dialog.dismiss();
            setLogout(context);

        });

        dialog.show();
    }

    private void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

    }

    private void updatepass(String oldPassword2, final String newPassword2) {
        startwhellprogress();
        enc = new EncreptionClass("gh@neely@b!n@ryw@ves");
        enc.encryptAsBase64(oldPassword2.getBytes());
        String oldpass1 = enc.encryptdatanew;
        enc = new EncreptionClass("gh@neely@b!n@ryw@ves");
        enc.encryptAsBase64(newPassword2.getBytes());
        final String newpass1 = enc.encryptdatanew;
        String fav = ServerConfig.SERVER_URl + ServerConfig.Changepass + "?oldPass=" + oldpass1 + "&userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)
                + "&newPass=" + newpass1;
        String fav_url = fav.replaceAll(" ", "%20");

        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        service.Changepass(fav_url).enqueue(new Callback<ChangepassResponse>() {

            @Override
            public void onResponse(@NonNull Call<ChangepassResponse> call, @NonNull Response<ChangepassResponse> mresult) {
                if (mresult.isSuccessful()) {
                    ChangepassResponse response = mresult.body();
                    if (response != null) {
                        if (response.getResultCode().equalsIgnoreCase("2")) {


                            Toast.makeText(getApplicationContext(), R.string.passwordchanged, Toast.LENGTH_LONG)
                                    .show();


                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.PASSWORD, newpass1);

                            View view = getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                            }
                            dialog.dismiss();
                            progBar.setVisibility(View.GONE);

                        } else {

                            Toast.makeText(getApplicationContext(), R.string.checkdata,
                                    Toast.LENGTH_LONG).show();


                        }
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);
                    dialog.dismiss();

                }
            }


            @Override
            public void onFailure(@NonNull Call<ChangepassResponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);
                dialog.dismiss();

            }
        });


    }

    private void uploadImage(View v) {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getResources().getString(R.string.uploading));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {

            Toast.makeText(getApplicationContext(), R.string.selectpic,
                    Toast.LENGTH_LONG).show();
        }


    }

    // AsyncTask - To convert Image to String
    @SuppressLint("StaticFieldLeak")
    private void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options;
                options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeFile(imgPath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload
                // easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte_arr = stream.toByteArray();
                // Encode Image to String

                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    private void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    private void makeHTTPCall() {
        startwhellprogress();
        Api_Interface service = ServiceGenerator.createService();
        service.UpdateUserImage(encodedString, SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    String imagename;
                    imagename = SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + ".jpg";
                    SharedPrefHelper.setSharedString(getApplicationContext(), Constants.UserImage, imagename);

                    progBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), R.string.Uploadsuccess, Toast.LENGTH_LONG).show();

                    mProgressDialog.dismiss();
                    dialogcamera.dismiss();
                    Intent mIntent = new Intent(SeetingActivity.this, HomeActivity.class);
                    mIntent.putExtra("uoloadimg", true);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(mIntent);

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        progBar.setVisibility(View.GONE);

    }

    private void setLogout(Context context) {
        startwhellprogress();
        String fav = ServerConfig.SERVER_URl + ServerConfig.UserLogoutFrmFacebook + "?&userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)
           ;
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.UserLogoutFrmFacebook(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    progBar.setVisibility(View.GONE);

                    SharedPrefHelper.deleteAllSharePrefs(getApplicationContext());

                    Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear all

                    SharedPrefHelper.setSharedBoolean(getApplicationContext(), Constants.islogout, true);
                    callFacebookLogout(getApplicationContext());
                    finish();
                    startActivity(intent);

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    private void updatelanguage(String string, Context context) {
        startwhellprogress();
        String fav = ServerConfig.SERVER_URl + ServerConfig.UpdateUserLang + "?&userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)
                + "&langId=" + string;
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.UpdateUserLang(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    progBar.setVisibility(View.GONE);

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(SeetingActivity.this, HomeActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(mIntent);
    }
}
