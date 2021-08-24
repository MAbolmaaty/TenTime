package com.binarywaves.ghaneely.ghannelyactivites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.BaseActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.FacebookFriendsAdapter;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.FacebookResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Friendlist_Activity extends BaseActivity  implements View.OnClickListener {
    private static ArrayList<FacebookResponse> NewmtrackList;
    Map<String, Integer> mapIndex;
    private ListView mList;
    private FacebookFriendsAdapter facebookFriendsAdapter;
    private LinearLayout facebookpopup;
    private String fbname;
    private String fbid;
    private String isFaceReg1 = "false";
    private String userID;
    private String msisdn;
    private String UserImage;
    private String IsFacebookReg;
    private String Accesstoken;
    private boolean ISACTIVE;
    private TextView text;
    private Context context;
    LinearLayout friendcon;
    private static EditText search_text;
    private ImageView mXMark;

    private final FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            // progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    (object, response) -> {

                        Log.e("response: ", response + "");
                        try {

                            isFaceReg1 = "true";
                            if (object.getString("name") != null || !object.getString("name").equalsIgnoreCase("")) {
                                fbname = object.getString("name");
                            }
                            fbid = object.getString("id");
                            SharedPreferences prefsfacebook = getSharedPreferences("GhaniliPreffacebook",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = prefsfacebook.edit();
                            // params.putString("isFaceReg", isFaceReg1.toString());
                            editor1.putString("facebookUersname", fbname);
                            editor1.putString(Constants.facebookId, fbid);
                            editor1.apply();
                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.isFaceReg, "true");

                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.facebookId, fbid);
                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.imagefb,
                                    "http://graph.facebook.com/" + fbid + "/picture?type=thumbnail"

                            );


                            connectfacebook();

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
    private View activityView;
    private RelativeLayout container;
    private Button invite;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FrameLayout frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;
        activityView = layoutInflater != null ? layoutInflater.inflate(R.layout.activity_friendlist, nullparent, false) : null;
        DrawerActivity.selectedListItem = 5;
        Applicationmanager.setContext(Friendlist_Activity.this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                DrawerActivity.header_image_view.setVisibility(View.GONE);
                DrawerActivity.title_txt.setVisibility(View.VISIBLE);
                DrawerActivity.title_txt.setText(extras.getString("titlemenu"));
            }
        }
        InitUI();

        frameLayout.addView(activityView);


        // check

    }

    private void InitUI() {
        mList = activityView.findViewById(R.id.gridview);
        friendcon=activityView.findViewById(R.id.friendcon);
        context = getApplicationContext();
        Log.i("Constants.accesstoken", SharedPrefHelper.getSharedString(context, Constants.accesstoken));
        Accesstoken = SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        facebookpopup = activityView.findViewById(R.id.facebooksceen);
        callbackManager = CallbackManager.Factory.create();

        text = activityView.findViewById(R.id.text);
        container = findViewById(R.id.con);
        container.setVisibility(View.GONE);
        invite = findViewById(R.id.add);
        invite.setVisibility(View.GONE);
        invite.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(Friendlist_Activity.this)) {
                sendRequestDialog();
            } else {
                Log.d("no internet found", "FOUND");
                Toast.makeText(Friendlist_Activity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_SHORT).show();

            }
        });
        search_text = activityView.findViewById(R.id.search_et);
        search_text.setOnKeyListener(this);

        mXMark = activityView.findViewById(R.id.search_x);

        mXMark.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            search_text.setText(null);

        });
        // Add Text Change Listener to EditText
        search_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                if( s.length()>0)
                facebookFriendsAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final LoginButton loginBtn = activityView.findViewById(R.id.fb_login_button);
        loginBtn.setVisibility(View.GONE);
        loginBtn.setReadPermissions(Collections.singletonList("email"));
        loginBtn.setReadPermissions(Collections.singletonList("user_friends"));
        loginBtn.setOnClickListener(v -> loginBtn.registerCallback(callbackManager, mCallBack));
        final Button connect = activityView.findViewById(R.id.connect);
        // if button is clicked, close the custom dialog
        connect.setOnClickListener(v -> loginBtn.performClick());

        SetFriensListdata();

    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void SetFriensListdata() {
        String isfbRegisterd = SharedPrefHelper.getSharedString(getApplicationContext(), Constants.isFaceReg);
        if (isfbRegisterd.equalsIgnoreCase("true")) {
            facebookpopup.setVisibility(View.GONE);
            friendcon.setVisibility(View.VISIBLE);
            //text.setVisibility(View.VISIBLE);
            if (Constants.isNetworkOnline(Friendlist_Activity.this)) {
                GraphRequestBatch batch = new GraphRequestBatch(GraphRequest.newMyFriendsRequest(
                        AccessToken.getCurrentAccessToken(), (JSONArray jsonArray, GraphResponse response) -> {
                            System.out.println("jsonArray: " + jsonArray);
                            System.out.println("GraphResponse: " + response);
                            try {
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    NewmtrackList = new ArrayList<>();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);

                                        Log.i("TAG", "id = " + c.get("id"));
                                        FacebookResponse mTrack = new FacebookResponse(c.getString("id"),c.getString("name"));
                                        mTrack.setId(c.getString("id"));
                                        mTrack.setName(c.getString("name"));
                                        NewmtrackList.add(mTrack);

                                    }
                                }
                                if (NewmtrackList != null && NewmtrackList.size() > 0) {
                                    facebookFriendsAdapter = new FacebookFriendsAdapter(Friendlist_Activity.this,
                                            R.layout.friendlistitem, R.id.textViewName, NewmtrackList);
                                    mList.setAdapter(facebookFriendsAdapter);
                                    setAlphalist(NewmtrackList);
//                                    mList.setOnItemClickListener((parent, view, position, id) -> {
//                                        // TODO Auto-generated method
//                                        // stub
//
//                                        Intent albumIntent = new Intent(Friendlist_Activity.this,
//                                                FriendsTabActivity.class);
//                                        albumIntent.putExtra("profileid", NewmtrackList.get(position).getId());
//                                        albumIntent.putExtra("profilename",
//                                                NewmtrackList.get(position).getName());
//
//                                        startActivity(albumIntent);
//                                    });
                                } else {

                                    Toast.makeText(Friendlist_Activity.this,
                                            R.string.nofriendsfound,
                                            Toast.LENGTH_SHORT).show();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }), GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        (object, response) -> {
                            System.out.println("meJSONObject: " + object);
                            System.out.println("meGraphResponse: " + response);

                        }));
                batch.addCallback(graphRequests -> {
                    // Log.i(TAG, "onCompleted: graphRequests "+
                    // graphRequests);
                });
                batch.executeAsync();
            } else {
                Log.d("no internet found", "FOUND");
                Toast.makeText(Friendlist_Activity.this, getResources().getString(R.string.gnrl_internet_error),
                        Toast.LENGTH_SHORT).show();
                // Constants.confirmationDialouge(RegisterActivity.this,
                // "No_internet", "", "", "");

            }
        } else {
            container.setVisibility(View.VISIBLE);
            invite.setVisibility(View.GONE);
            facebookpopup.setVisibility(View.VISIBLE);
            friendcon.setVisibility(View.GONE);
        }
    }

    private void setAlphalist(ArrayList<FacebookResponse> newmtrackList) {
        ArrayList<String> mylist = new ArrayList<String>();

        for (int i = 0; i < newmtrackList.size(); i++) {
            String friendsname = newmtrackList.get(i).getName();
            mylist.add(friendsname);
        }
        Arrays.asList(mylist);

    }






    /////////////////////////////////////////////////////////////////////////////
    // to connect facebook from popup

    // When Send Message button is clicked
    private void sendRequestDialog() {
        String appLinkUrl;
        // https://fb.me/521214491394758
        appLinkUrl = "https://fb.me/521214491394758";
        // previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder().setApplinkUrl(appLinkUrl).setPreviewImageUrl(null)
                    .build();
            AppInviteDialog.show(Friendlist_Activity.this, content);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void connectfacebook() {
        // TODO Auto-generated method stub
        if (Constants.isNetworkOnline(this)) {
            SharedPreferences prefs = getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);
            if (!prefs.getString(Constants.SIM_NO, "").equalsIgnoreCase("")) { // username

                Api_Interface service = ServiceGenerator.createService();
                service.UpdateUserFacebookId(fbname, fbid, SharedPrefHelper.getSharedString(context, Constants.USERID), SharedPrefHelper.getSharedString(context, Constants.accesstoken)).enqueue(new Callback<User>() {

                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> mresult) {
                        if (mresult.isSuccessful()) {
                            Log.i("onSuccess", "here");

                            User mUser = new User();


                            userID = mresult.body().getUserID();
                            msisdn = mresult.body().getMsisdn();
                            ISACTIVE = mresult.body().getIsActivated();
                            UserImage = mresult.body().getUserImage();
                            DrawerActivity.accesstoken = mresult.body().getUserToken();
                            DrawerActivity.UserStatusId = mresult.body().getIsSubsc();
                            DrawerActivity.ServiceID = mresult.body().getServiceID();
                            IsFacebookReg = mresult.body().getIsFacebookReg();


                            // ISACTIVE=false;
                            mUser.setUserID(userID);
                            mUser.setMsisdn(msisdn);
                            mUser.setIsActivated(ISACTIVE);
                            mUser.setUserImage(UserImage);
                            mUser.setUserToken(DrawerActivity.accesstoken);
                            mUser.setIsSubsc(DrawerActivity.UserStatusId);
                            mUser.setServiceID(DrawerActivity.ServiceID);
                            mUser.setIsFacebookReg(IsFacebookReg);

                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.isFaceReg, "true");

                            SharedPrefHelper.setSharedString(getApplicationContext(), Constants.USERID, mUser.getUserID());


                            Intent mIntent = new Intent(Friendlist_Activity.this, HomeActivity.class);
                            mIntent.putExtra("uoloadimgfromfriends", true);

                            startActivity(mIntent);

                        } else {
                            ApiUtils.handelErrorCode(context, mresult.code());
                            System.out.println("onFailure");

                        }
                    }


                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                    }
                });


            } else {
                Toast.makeText(Friendlist_Activity.this, getResources().getString(R.string.no_sim), Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.d("no internet found", "FOUND");

            Toast.makeText(Friendlist_Activity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        callbackManager = CallbackManager.Factory.create();

        String isfbRegisterd = SharedPrefHelper.getSharedString(getApplicationContext(), Constants.isFaceReg);
        if (isfbRegisterd.equalsIgnoreCase("false")) {
            facebookpopup.setVisibility(View.VISIBLE);
            friendcon.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        mList.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}