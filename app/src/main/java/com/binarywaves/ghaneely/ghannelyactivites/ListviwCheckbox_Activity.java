package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyadaptors.ListviwCheckbox_Adapter;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.GetSuggestedTrackLstsResponse;
import com.binarywaves.ghaneely.ghannelyresponse.TrackLst;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListviwCheckbox_Activity extends ActivityMainRunnuing {
    private static RelativeLayout progBar;
    public static ArrayList<String> selectedcount;
    private static String language;
    private static ProgressWheel pb1;
    private DynamicListView recent_list;
    private DynamicListView likeslist;
    private DynamicListView song_list;
    private Context context;
    private ImageView navigation_up;
    private Button Done;
    private ListviwCheckbox_Adapter lvadapter;
    private Dialog dialog;
    private EditText playlistname;
    private ArrayList<TrackLst> recentplayed;
    private ArrayList<TrackLst> likes;
    private ArrayList<TrackLst> featured;
    private GetSuggestedTrackLstsResponse getSuggestedTrackLstsResponse;

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    private static void justifyListViewHeightBasedOnChildren(ListView listView) {


        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you
                // won't get measured height at all. The listitem needs to be
                // drawn first to know the height.
                listItem.setLayoutParams(
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listviewwithcheckbox_activity);

        // Remove notification bar
        selectedcount = new ArrayList<>();
        Applicationmanager.setContext(ListviwCheckbox_Activity.this);
 context = Applicationmanager.getContext();
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        startwhellprogress();
        language = LanguageHelper.getCurrentLanguage(context);

        navigation_up = findViewById(R.id.navigation_up);
        navigation_up.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent player = new Intent(this, PlayListActivity.class);
            startActivity(player);
            finish();
        });


        Done = findViewById(R.id.btdone);


        Done.setOnClickListener(v -> {

            showAlert();

            //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

        });
        recent_list = findViewById(R.id.recent_list);

        likeslist = findViewById(R.id.likeslist);

        song_list = findViewById(R.id.song_list);
        startwhellprogress();
        getlist();
    }

    private void getlist() {
        if (Constants.isNetworkOnline(this)) {

            startwhellprogress();
            SharedPreferences prefs = getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);

            String search = ServerConfig.SERVER_URl + ServerConfig.GetSuggestedTrackLsts + "?userId="
                    + prefs.getString(Constants.USERID, "") + "&UserToken="
                    + prefs.getString(Constants.accesstoken, "");

            String search_url = search.replaceAll(" ", "%20");
            Log.d("amanttest", search_url);

            Api_Interface service = ServiceGenerator.createService();
            service.GetSuggestedTrackLsts(search_url).enqueue(new Callback<GetSuggestedTrackLstsResponse>() {

                @Override
                public void onResponse(@NonNull Call<GetSuggestedTrackLstsResponse> call, @NonNull Response<GetSuggestedTrackLstsResponse> mresult) {
                    if (mresult.isSuccessful()) {
                        //convert json string to object
                        getSuggestedTrackLstsResponse = mresult.body();

                        progBar.setVisibility(View.GONE);
                        if (getSuggestedTrackLstsResponse != null) {
                            recentplayed = getSuggestedTrackLstsResponse.getRecentplayed();
                            likes = getSuggestedTrackLstsResponse.getLikes();
                            featured = getSuggestedTrackLstsResponse.getFeatured();
                        }
                        if (recentplayed.size() > 0) {

                            setrecentplayedList();
                        }
                        if (likes.size() > 0) {
                            setlikesList();
                        }
                        if (featured.size() > 0) {
                            detfeaturedList();
                        }
                        Done.setVisibility(View.VISIBLE);
                        progBar.setVisibility(View.GONE);


                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");
                        progBar.setVisibility(View.GONE);

                    }
                }


                @Override
                public void onFailure(@NonNull Call<GetSuggestedTrackLstsResponse> call, @NonNull Throwable t) {
                    progBar.setVisibility(View.GONE);

                }
            });


        } else {

            // no_timeline_data.setVisibility(View.VISIBLE);
            progBar.setVisibility(View.GONE);
            Toast.makeText(ListviwCheckbox_Activity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();
            // Constants.confirmationDialouge(mcontext, "No_internet", "", "",
            // "");
        }
    }

    private void setrecentplayedList() {
        if (recentplayed.size() > 0) {
            lvadapter = new ListviwCheckbox_Adapter(context, R.layout.listwithcheck_row, recentplayed);
            // list.setVisibility(View.VISIBLE);

            // Binds the Adapter to the ListView
            recent_list.setAdapter(lvadapter);
            justifyListViewHeightBasedOnChildren(recent_list);
        }

    }

    private void setlikesList() {
        if (likes.size() > 0) {
            lvadapter = new ListviwCheckbox_Adapter(context, R.layout.listwithcheck_row, likes);
            // list.setVisibility(View.VISIBLE);

            // Binds the Adapter to the ListView
            likeslist.setAdapter(lvadapter);
            justifyListViewHeightBasedOnChildren(likeslist);

        }
    }

    private void detfeaturedList() {
        if (featured.size() > 0) {
            lvadapter = new ListviwCheckbox_Adapter(context, R.layout.listwithcheck_row, featured);
            // list.setVisibility(View.VISIBLE);

            // Binds the Adapter to the ListView
            song_list.setAdapter(lvadapter);
            justifyListViewHeightBasedOnChildren(song_list);

        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showAlert() {
        dialog = new Dialog(ListviwCheckbox_Activity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.create_playlist);
        playlistname = dialog.findViewById(R.id.editText1);
        Button cancel = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            if (selectedcount != null) {
                if (selectedcount.size() > 0) {
                    if (!playlistname.getText().toString().equalsIgnoreCase("")) {
                        dialog.dismiss();

                        Creareplaylist(playlistname.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.enterplaylist_name,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    dialog.dismiss();

                    Toast.makeText(ListviwCheckbox_Activity.this, getResources().getString(R.string.notrackselected),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
    }

    private void Creareplaylist(String playlistname) {
        startwhellprogress();
        Log.d("Login Found", "heree");
        String fav_url = ServerConfig.SERVER_URl + ServerConfig.AddUserPlaylistWithTracks + "?userId=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken) + "&usrPlaylstName=" + playlistname
                + "&trackIDs=" + selectedcount;
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();

        service.AddUserPlaylistWithTracks(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    progBar.setVisibility(View.GONE);
                    showCustomAlertPlaylist_added();
                    Intent player = new Intent(ListviwCheckbox_Activity.this, PlayListActivity.class);
                    startActivity(player);
                    finish();

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });

    }

    private void showCustomAlertPlaylist_added() {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        final ViewGroup nullparent = null;

        View toastRoot = inflater.inflate(R.layout.toast, nullparent);
        ImageView image = toastRoot.findViewById(R.id.ivtoast);
        image.setImageResource(R.mipmap.donetick);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

}