package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_presenter.RetrofitPresenter_User_Class;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.KaraokeTabActivity;
import com.binarywaves.ghaneely.ghannelyactivites.SearchTabs.SearchTabActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.CustompopListMenuAdapter;
import com.binarywaves.ghaneely.ghannelyadaptors.DrawerItemCustomAdapter;
import com.binarywaves.ghaneely.ghannelyadaptors.ObjectDrawerItem;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.User;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelyutils.Utils;
import com.facebook.FacebookSdk;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

public class DrawerActivity extends ActivityMainRunnuing implements OnKeyListener, OnItemClickListener, RetrofitPresenter_User_Class.RetrofitPresenterListener {

    public static ListView mDrawerList;
    public static int selectedListItem;
    public static int selected_popup_menu = -1;
    public static Timer timer;
    public static boolean start = true;
    public static String UserStatusId;
    public static String ServiceID;
    public static Dialog dialogdimmed;
    protected static ImageView header_image_view;
    protected static TextView title_txt;
    static String accesstoken;
    private static DrawerItemCustomAdapter adapter;
    private static ObjectDrawerItem[] drawerItem;
    private static ImageView Search;
    private static String userID;
    private static Dialog dialogoffline;
    private static Dialog dialog;
    private static ImageView imageProfile;
    private static ImageView iv_preview_image;
    private static TextView footertxt;
    private static ImageView ivfooter;
    private static String issubscribe;
    private static RetrofitPresenter_User_Class.RetrofitPresenterListener mlisinter;
    private static String UserSubscResultDesc;
    private static String DownloadKey;
    public PopupWindow popupWindowlang;
    protected Context context;
    LayoutInflater inflater;
    ImageView play;
    ImageView player_image;
    private DrawerLayout mDrawerLayout;
    private ImageView upNavigation;
    private Button menu_btn;
    private EditText search_text;
    private LinearLayout linfooter;
    private String[] popUpContents;
    private String[] mAraay;
    private CustompopListMenuAdapter madaptor;
    private String[] mNavigationDrawerItemTitles;
    public ImageView ivorangelogo;

    public static void GetSubscribeOption(Context context) {
        show_progressbar();

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetUserSubInfo + "?&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        RetrofitPresenter_User_Class retrofitPresenter = new RetrofitPresenter_User_Class(mlisinter, getActivity());
        retrofitPresenter.GetSubscribeOption(fav_url);
    }

    public static void SendSubsribedata(String serviceId, final String issub, Context context) {
        show_progressbar();

        String fav = ServerConfig.SERVER_URl + ServerConfig.SubscribePremium + "?&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken) +
                "&isSubsc=" + issub + "&serviceId=" + serviceId;

        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        issubscribe = issub;
        RetrofitPresenter_User_Class retrofitPresenter = new RetrofitPresenter_User_Class(mlisinter, getActivity());
        retrofitPresenter.SendSubsribedata(fav_url);


    }

    @SuppressWarnings("unused")
    private static void setEnd_SubscribeDate(int i, Context context) {
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


    private static void Reload_App(Context context) {
        Intent mIntent = new Intent(context, HomeActivity.class);
        mIntent.putExtra("loadinternational", true);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }

    private static void showAlertsubscripe_DailyANdMonthly(String UserSubscTypeID, Context ctx) {
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)
        dialog.setContentView(R.layout.popup_daily_monthly);
        ImageView btclose = dialog.findViewById(R.id.close_player);
        btclose.setOnClickListener(v -> dialog.dismiss());

        Button btmoney = dialog.findViewById(R.id.btmoney);
        String language = LanguageHelper.getCurrentLanguage(getActivity());

        if (UserSubscTypeID.equalsIgnoreCase("1")) {

            btmoney.setText(SharedPrefHelper.getSharedString(ctx, Constants.ServiceMonthlyPrice));


        } else if (UserSubscTypeID.equalsIgnoreCase("2")) {

            btmoney.setText(SharedPrefHelper.getSharedString(ctx, Constants.ServiceDailyPrice));


        }

        Button btgetit = dialog.findViewById(R.id.btremoveads);
        btgetit.setOnClickListener(v -> {


            if (UserSubscTypeID.equalsIgnoreCase("1")) {

                dialog.dismiss();
                SendSubsribedata("11", "true", getActivity().getApplicationContext());

            } else if (UserSubscTypeID.equalsIgnoreCase("2")) {

                dialog.dismiss();
                SendSubsribedata("12", "true", getActivity().getApplicationContext());

            }
        });
        // if button is clicked, close the custom dialog

        dialog.show();

    }
//    // subscripe ghaneely extra
//    private static void showAlertsubscripe(String UserSubscTypeID, @SuppressWarnings("unused") Context ctx) {
//        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCanceledOnTouchOutside(false);
//        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)
//        dialog.setContentView(R.layout.getpremuiem_popup);
//        Button btdailysub = dialog.findViewById(R.id.btdaily);
//        Button btmonthsub = dialog.findViewById(R.id.btmonthly);
//        LinearLayout lindaily = dialog.findViewById(R.id.lindaily);
//        LinearLayout linmon = dialog.findViewById(R.id.linmon);
//        TextView tvpricedaily = dialog.findViewById(R.id.tvpricedaily);
//        TextView tvpricemonthly = dialog.findViewById(R.id.tvpricemonthly);
//        tvpricedaily.setText(SharedPrefHelper.getSharedString(ctx, Constants.ServiceDailyPrice));
//        tvpricemonthly.setText(SharedPrefHelper.getSharedString(ctx, Constants.ServiceMonthlyPrice));
//
//        if (UserSubscTypeID.equalsIgnoreCase("1")) {
//            lindaily.setVisibility(View.GONE);
//            linmon.setVisibility(View.VISIBLE);
//
//        } else if (UserSubscTypeID.equalsIgnoreCase("2")) {
//            linmon.setVisibility(View.GONE);
//            lindaily.setVisibility(View.VISIBLE);
//        } else {
//            Toast.makeText(ctx, ctx.getResources().getString(R.string.tryagain),
//                    Toast.LENGTH_LONG).show();
//        }
//
//        btdailysub.setOnClickListener(v -> {
//            dialog.dismiss();
//            SendSubsribedata("12", "true", getActivity().getApplicationContext());
//
//        });
//
//        btmonthsub.setOnClickListener(v -> {
//            dialog.dismiss();
//            SendSubsribedata("11", "true", getActivity().getApplicationContext());
//
//        });
//        ImageView cancel = dialog.findViewById(R.id.close_player);
//        // if button is clicked, close the custom dialog
//        cancel.setOnClickListener(v -> dialog.dismiss());
//
//        // if button is clicked, close the custom dialog
//
//        dialog.show();
//    }


    // subscripe ghaneely extra
    public static void show_Alert_Dimmed_Track(Context context) {
        if (getActivity() != null) {
            dialogdimmed = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

            dialogdimmed.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogdimmed.setCanceledOnTouchOutside(false);
            //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

            dialogdimmed.setContentView(R.layout.dimmedtracks_popup);

            Button btsubscribe = dialogdimmed.findViewById(R.id.btremoveads);

            btsubscribe.setOnClickListener(v -> {
                dialogdimmed.dismiss();
                GetSubscribeOption(getActivity().getApplicationContext());

            });


            Button cancel = dialogdimmed.findViewById(R.id.btclose);
            // if button is clicked, close the custom dialog
            cancel.setOnClickListener(v -> dialogdimmed.dismiss());
            ImageView image = dialogdimmed.findViewById(R.id.ivimage);
            TextView tvtext = dialogdimmed.findViewById(R.id.maintext);
            String language = LanguageHelper.getCurrentLanguage(context);

            boolean isServiceRunning2 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    context);
            if (isServiceRunning2) {
                if (language.equalsIgnoreCase("ar")) {

                    tvtext.setText(context.getString(R.string.dimmedtext));


                }
                if (language.equalsIgnoreCase("en")) {
                    tvtext.setText(context.getString(R.string.dimmedtext));
                }

                String imgpath = ServerConfig.Image_path
                        + PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage();
                String final_imgpath = imgpath.replaceAll(" ", "%20");

                Picasso.with(getActivity()).load((final_imgpath)).error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(image);
            }
            // if button is clicked, close the custom dialog
            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                    context);
            if (isServiceRunning3) {

                if (language.equalsIgnoreCase("ar")) {

                    tvtext.setText(context.getString(R.string.dimmedtext) + " " + PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoArName() + " " + context.getString(R.string.fullvideodimmed));


                }
                if (language.equalsIgnoreCase("en")) {
                    tvtext.setText(context.getString(R.string.dimmedtext) + " " + PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoEnName() + " " + context.getString(R.string.fullvideodimmed));
                }
                String imgpath = ServerConfig.Video_Imagepath
                        + PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoPoster();
                String final_imgpath = imgpath.replaceAll(" ", "%20");

                Picasso.with(getActivity()).load((final_imgpath)).error(R.mipmap.defualt_img)
                        .placeholder(R.mipmap.defualt_img).into(image);

            }
            dialogdimmed.show();
        }
    }

    public static Activity getActivity() {


        return Applicationmanager.getCurrentActivity();
    }

    private static void show_progressbar() {
        if (getActivity() != null) {
            Applicationmanager.Createprogressloading();
        }


    }

    private static void hide_progressbar() {

        if (getActivity() != null) {
            Applicationmanager.Dismissprogressloading();
        }

    }

    public static void Show_GhannelyExtra_Dialog(Context context) {

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        dialog.setContentView(R.layout.subscribe2premium);

        Button btsubscribe = dialog.findViewById(R.id.btremoveads);

        btsubscribe.setOnClickListener(v -> {
            dialog.dismiss();
            GetSubscribeOption(context);

        });


        Button cancel = dialog.findViewById(R.id.btclose);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        // if button is clicked, close the custom dialog

        dialog.show();
    }
// dialog appear when user in grace

    public static void showGracePopup(Context context) {

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        dialog.setContentView(R.layout.dialog_grace_dimmed);

        ImageView cancel = dialog.findViewById(R.id.btnIvClose);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> {
            dialog.dismiss();

        });

        // if button is clicked, close the custom dialog

        dialog.show();

    }

    private static void Show_Success_Reject_sub_Dialog(boolean success, boolean isub, String userSubscResultDesc, Context context) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        dialog.setContentView(R.layout.subscribe_success_reject);
        ImageView ivsuc = dialog.findViewById(R.id.ivsuc);
        TextView tvsuc_rej = dialog.findViewById(R.id.tvsuc_rej);
        TextView tvdetail = dialog.findViewById(R.id.tvdetail);
        if (success && isub) {
            ivsuc.setImageResource(R.mipmap.success);
            tvsuc_rej.setText(context.getResources().getString(R.string.sucess));
            tvdetail.setText(context.getResources().getString(R.string.sucesssubscribe));

        }

        if (success && !isub) {
            ivsuc.setImageResource(R.mipmap.success);
            tvsuc_rej.setText(context.getResources().getString(R.string.sucess));
            tvdetail.setText(context.getResources().getString(R.string.unsucesssubscribe));

        }
        if (!success && !isub) {
            ivsuc.setImageResource(R.mipmap.error);
            tvsuc_rej.setText(context.getResources().getString(R.string.reject));
            tvdetail.setText(userSubscResultDesc);


        }


        if (!success && isub) {
            ivsuc.setImageResource(R.mipmap.error);
            tvsuc_rej.setText(context.getResources().getString(R.string.reject));
            tvdetail.setText(userSubscResultDesc);
        }

        ImageView cancel = dialog.findViewById(R.id.btnIvClose);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            if (success && isub) {
                Reload_App(context);
            }
            if (success && !isub) {
                Reload_App(context);

            }
        });

        // if button is clicked, close the custom dialog

        dialog.show();
    }

    @SuppressWarnings("unused")
    public static void ShowOfflineconnection_Dialog(Context context) {
        dialogoffline = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        dialogoffline.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogoffline.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        dialogoffline.setContentView(R.layout.offlinedownload_popup);

        Button retry = dialogoffline.findViewById(R.id.change);

        retry.setOnClickListener(v ->
                {
                    dialogoffline.dismiss();

                    Intent mIntent = new Intent(context, SplashScreenActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mIntent);
                }
        );


        Button cancel = dialogoffline.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialogoffline.dismiss());

        // if button is clicked, close the custom dialog

        dialogoffline.show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        OcreateFunction();
    }

    private void OcreateFunction() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_drawer_screen);
        mlisinter = this;
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.requestDisallowInterceptTouchEvent(false);
        mDrawerList = findViewById(R.id.left_drawer);
        search_text = findViewById(R.id.search_et);
        search_text.setOnKeyListener(this);
        ivorangelogo = findViewById(R.id.ivorangelogo);
        title_txt = findViewById(R.id.title_txt);
        header_image_view = findViewById(R.id.header_image_view);
        menu_btn = findViewById(R.id.menu_btn);
        if (SharedPrefHelper.getSharedBoolean(context, Constants.AllowInternational)) {
            menu_btn.setVisibility(View.VISIBLE);
        } else {
            menu_btn.setVisibility(View.GONE);

        }
        mAraay = getResources().getStringArray(R.array.menupopup);
        dialogdimmed = new Dialog(DrawerActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

// convert to simple array
        popUpContents = mAraay;
        /*
         * initialize pop up window
         */
        popupWindowlang = popupWindowLangfunction();
        switch (SharedPrefHelper.getSharedInt(context, Constants.GenreId)) {
            case (1):
                selected_popup_menu = 0;
                menu_btn.setText(getResources().getString(R.string.arabicmenu));
                madaptor.notifyDataSetInvalidated();
                break;

            case (3):
                selected_popup_menu = 1;
                menu_btn.setText(getResources().getString(R.string.intment));

                madaptor.notifyDataSetInvalidated();
                break;


            case (2):
                selected_popup_menu = 2;
                menu_btn.setText(getResources().getString(R.string.arandint));

                madaptor.notifyDataSetInvalidated();
                break;
        }

        menu_btn.setOnClickListener(v -> {
            if (Constants.isNetworkOnline(DrawerActivity.this)) {

                if (popupWindowlang != null && popupWindowlang.isShowing()) {
                    popupWindowlang.dismiss();
                } else {
                    if (popupWindowlang != null) {
                        popupWindowlang.showAsDropDown(v, (int) -2.5, 0);
                    }
                }
            } else {
                ShowOfflineconnection_Dialog(getApplicationContext());
            }
        });
        imageProfile = findViewById(R.id.profile_image);
        imageProfile.setOnClickListener(v -> ShowPic_Popup());

        drawerItem = new ObjectDrawerItem[10];//
        drawerItem[0] = new ObjectDrawerItem(R.mipmap.iconsho, mNavigationDrawerItemTitles[0]);
        drawerItem[1] = new ObjectDrawerItem(R.mipmap.iconsse, mNavigationDrawerItemTitles[1]);
        drawerItem[2] = new ObjectDrawerItem(R.mipmap.iconsli, mNavigationDrawerItemTitles[2]);

        drawerItem[3] = new ObjectDrawerItem(R.mipmap.iconspl, mNavigationDrawerItemTitles[3]);
        drawerItem[4] = new ObjectDrawerItem(R.mipmap.iconsin, mNavigationDrawerItemTitles[4]);
        drawerItem[5] = new ObjectDrawerItem(R.mipmap.iconsfr, mNavigationDrawerItemTitles[5]);
        drawerItem[6] = new ObjectDrawerItem(R.mipmap.iconsra, mNavigationDrawerItemTitles[6]);
        drawerItem[7] = new ObjectDrawerItem(R.mipmap.iconska, mNavigationDrawerItemTitles[7]);
        drawerItem[8] = new ObjectDrawerItem(R.mipmap.downloadsmenu, mNavigationDrawerItemTitles[8]);

        drawerItem[9] = new ObjectDrawerItem(R.mipmap.settingsmenuicon, mNavigationDrawerItemTitles[9]);


        adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        adapter.notifyDataSetChanged();
        mDrawerList.invalidateViews();
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        addHeaderToDrawer();
        linfooter = findViewById(R.id.footer);
        footertxt = linfooter.findViewById(R.id.tv);
        ivfooter = linfooter.findViewById(R.id.ivfooter);

        if (!SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(getApplicationContext(), Constants.UserStatusId).equalsIgnoreCase("1")) {
            linfooter.setVisibility(View.GONE);
            footertxt.setText(getResources().getString(R.string.unsubghannelypremuim));
            ivfooter.setVisibility(View.GONE);


        } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {

            linfooter.setVisibility(View.GONE);
            footertxt.setText(getResources().getString(R.string.unsubghannelypremuim));
            ivfooter.setVisibility(View.GONE);
        } else {
            linfooter.setVisibility(View.VISIBLE);

            footertxt.setText(getResources().getString(R.string.getghannelypremuim));
        }
        linfooter.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


            if (language.equalsIgnoreCase("ar")) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    // drawer is open
                    mDrawerLayout.closeDrawers();
                } else {

                    mDrawerLayout.openDrawer(GravityCompat.END);
                    mDrawerLayout.setEnabled(false);

                }
            }
            if (language.equalsIgnoreCase("en")) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // drawer is open
                    mDrawerLayout.closeDrawers();
                } else {

                    mDrawerLayout.openDrawer(GravityCompat.START);
                    mDrawerLayout.setEnabled(false);

                }
            }
            if (footertxt.getText().toString().equalsIgnoreCase(getResources().getString(R.string.getghannelypremuim))) {

                GetSubscribeOption(context);
            }


//            if (footertxt.getText().toString().equalsIgnoreCase(getResources().getString(R.string.unsubghannelypremuim))) {
//
//                ShowConfirmationUnsubscribe(context);
//            }


        });
        setUpNavigationView();
        Search = findViewById(R.id.add_btn);

        Search.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (Constants.isNetworkOnline(DrawerActivity.this)) {
                mDrawerList.performItemClick(mDrawerList.getAdapter().getView(1, null, null), 1, 0);

            } else {
                ShowOfflineconnection_Dialog(getApplicationContext());
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    public void ShowConfirmationUnsubscribe(Context context) {
        if (getActivity() != null) {
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            //noinspection ConstantConditions
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.setContentView(R.layout.dowloaddelete_confirmation_popup);
            TextView tvtext = dialog.findViewById(R.id.tvtext);
            tvtext.setText(context.getResources().getString(R.string.uusubconfirm));
            Button cancel = dialog.findViewById(R.id.cancel);
            cancel.setBackgroundColor(Utils.getColorWrapper(context, R.color.ghaneely_orange));

            // if button is clicked, close the custom dialog
            cancel.setOnClickListener(v -> dialog.dismiss());

            Button confirm = dialog.findViewById(R.id.change);
            confirm.setBackgroundColor(Utils.getColorWrapper(context, R.color.thinblack));
            // if button is clicked, close the custom dialog
            confirm.setOnClickListener(v -> {
                dialog.dismiss();
                SendSubsribedata(SharedPrefHelper.getSharedString(getApplicationContext(), Constants.ServiceID), "false", getApplicationContext());


                // reload login page to set it by new languageactive

            });

            dialog.show();
        } else {

            ShowConfirmationUnsubscribe(getActivity());
        }
    }

    private void setUpNavigationView() {

        upNavigation = findViewById(R.id.navigation_up);
        upNavigation.setOnClickListener(v -> {
            String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


            if (language.equalsIgnoreCase("ar")) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    // drawer is open
                    mDrawerLayout.closeDrawers();
                } else {

                    mDrawerLayout.openDrawer(GravityCompat.END);
                    mDrawerLayout.setEnabled(false);

                }
            }
            if (language.equalsIgnoreCase("en")) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // drawer is open
                    mDrawerLayout.closeDrawers();
                } else {

                    mDrawerLayout.openDrawer(GravityCompat.START);
                    mDrawerLayout.setEnabled(false);

                }
            }


        });
    }

    // //////////Search action ////////////////////
    protected boolean isEmpty() {
        return TextUtils.isEmpty(search_text.getText().toString());
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH
                || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // performSearch();
            if (!event.isShiftPressed()) {
                if (v.getId() == R.id.search_et) {
                    if (isEmpty()) {

                        Toast.makeText(DrawerActivity.this, getResources().getString(R.string.no_search_result),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("search btn pressed", "HERE");
                        // search(search_text.getText().toString());
                        Intent Search = new Intent(DrawerActivity.this, SearchTabActivity.class);
                        // Search.putExtra("keyword", search_text.getText()
                        // .toString());
                        // search_text.setText("");
                        startActivity(Search);

                    }
                }

            }
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub

    }

    private void addHeaderToDrawer() {

        if (SharedPrefHelper.getSharedString(context, Constants.UserImage).equalsIgnoreCase("")
                && SharedPrefHelper.getSharedString(context, Constants.isFaceReg)
                .equalsIgnoreCase("false")) {
            imageProfile.setImageResource(R.mipmap.profiledefault);

        } else {
            if (!SharedPrefHelper.getSharedString(context, Constants.UserImage).equalsIgnoreCase("")
                    && SharedPrefHelper.getSharedString(context, Constants.isFaceReg)
                    .equalsIgnoreCase("false")) {
                String imgpath = ServerConfig.Image_profile
                        + SharedPrefHelper.getSharedString(context, Constants.UserImage);
                String final_imgpath = imgpath.replaceAll(" ", "%20");
                Picasso.with(context).load(final_imgpath).error(R.mipmap.defualt_img)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.defualt_img).into(imageProfile);
                adapter.notifyDataSetChanged();
                mDrawerList.invalidateViews();

            } else {
                String isfbRegisterd = SharedPrefHelper.getSharedString(context, Constants.isFaceReg);
                if (isfbRegisterd.equalsIgnoreCase(
                        "true")) {
                    SharedPreferences prefsfacebook = context.getSharedPreferences("GhaniliPreffacebook",
                            Context.MODE_PRIVATE);
                    Picasso.with(context)
                            .load("https://graph.facebook.com/" + prefsfacebook.getString(
                                    Constants.facebookId, "") + "/picture?type=large") //extract as User instance method
                            .error(R.mipmap.defualt_img)
                            .placeholder(R.mipmap.defualt_img).into(imageProfile);

                }
            }
        }


    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void ShowPic_Popup() {

        final Dialog dialog = new Dialog(DrawerActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        dialog.setContentView(R.layout.imagepopup);
        iv_preview_image = dialog.findViewById(R.id.iv_preview_image);
        iv_preview_image.setImageDrawable(imageProfile.getDrawable());

        ImageView btnClose = dialog.findViewById(R.id.btnIvClose);

        btnClose.setOnClickListener(arg0 -> dialog.dismiss());
        dialog.show();
    }

    private PopupWindow popupWindowLangfunction() {

        // initialize a pop up window type
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;
        assert layoutInflater != null;
        View layout = layoutInflater.inflate(R.layout.ghannelymenu_listview, nullparent);
        ListView listView = layout.findViewById(R.id.lvmenu);

        final PopupWindow popupWindowlangf = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Creating the PopupWindow

        popupWindowlangf.setTouchable(true);
        popupWindowlangf.setFocusable(true);
        popupWindowlangf.setOutsideTouchable(false);
        popupWindowlangf.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        popupWindowlangf.setWidth((int) (width / 1.5));
        popupWindowlangf.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        // set the list view as pop up window content
        // Clear the default translucent background

        // the drop down list is a list view
        // set our adapter and pass our pop up window contents
        madaptor = new CustompopListMenuAdapter(DrawerActivity.this, R.layout.custom_ghannely_menu, popUpContents);

        listView.setAdapter(madaptor);
        // set the item click listener

        listView.setOnItemClickListener((arg0, v, pos, arg3) -> {
            // TODO Auto-generated method stub


            switch (pos) {

                case 0:
                    if (popupWindowlang != null && popupWindowlang.isShowing()) {
                        popupWindowlang.dismiss();
                    }
                    // get the text and set it as the button text
                    if (selected_popup_menu == 1 || selected_popup_menu == 2) {
                        selected_popup_menu = 0;
                        menu_btn.setText(getResources().getString(R.string.arabicmenu));

                        madaptor.notifyDataSetInvalidated();
                        LoadHomescreen(1);

                    } else {
                        // LoadHomescreen(2);

                        SharedPrefHelper.setSharedInt(context, Constants.GenreId, 1);

                    }
                    break;
//international
                case 1:
                    if (popupWindowlang != null && popupWindowlang.isShowing()) {
                        popupWindowlang.dismiss();
                    }

                    if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                        if (selected_popup_menu == 0 || selected_popup_menu == 2) {
                            selected_popup_menu = 1;
                            menu_btn.setText(getResources().getString(R.string.intment));

                            madaptor.notifyDataSetInvalidated();
                            LoadHomescreen(3);
                        }
                    } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {
                        showGracePopup(getApplicationContext());
                    } else {
                        Show_GhannelyExtra_Dialog(context);
                        SharedPrefHelper.setSharedInt(context, Constants.GenreId, 1);
                        madaptor.notifyDataSetInvalidated();
                    }

                    // get the text and set it as the button text


                    break;
//arabic&inter
                case 2:
                    if (popupWindowlang != null && popupWindowlang.isShowing()) {
                        popupWindowlang.dismiss();
                    }
                    // get the text and set it as the button text

                    if (selected_popup_menu == 0 || selected_popup_menu == 1) {
                        selected_popup_menu = 2;
                        menu_btn.setText(getResources().getString(R.string.arandint));

                        madaptor.notifyDataSetInvalidated();
                        LoadHomescreen(2);

                    }
                    break;
            }
        });


        return popupWindowlangf;
    }

    private void LoadHomescreen(int GenreId) {
        SharedPrefHelper.setSharedInt(context, Constants.GenreId, GenreId);
        Intent mIntent = new Intent(DrawerActivity.this, HomeActivity.class);
        mIntent.putExtra("loadinternational", true);
        startActivity(mIntent);
    }

    private void StopAllService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

    }

    @Override
    public void responseRbtReady(Addrbtresponse addrbtresponses, int code, int functiontype) {

    }

    @Override
    public void responseUserReady(User mUser, int code, int functiontype) {
        if (functiontype == 1) {


            if (mUser != null) {
                hide_progressbar();
                Log.v("result", mUser + "");

                String ServiceDailyPrice = mUser.getServiceDailyPrice();

                String ServiceMonthlyPrice = mUser.getServiceMonthlyPrice();
                mUser.setServiceDailyPrice(ServiceDailyPrice);
                mUser.setServiceMonthlyPrice(ServiceMonthlyPrice);

                SharedPrefHelper.setSharedString(context, Constants.ServiceDailyPrice, ServiceDailyPrice);

                SharedPrefHelper.setSharedString(context, Constants.ServiceMonthlyPrice, ServiceMonthlyPrice);

                String UserSubscTypeID = mUser.getUserSubscTypeID();
                if (UserSubscTypeID.equalsIgnoreCase("0")) {
                    Toast.makeText(context, R.string.tryagain, Toast.LENGTH_LONG).show();
                } else {
                    showAlertsubscripe_DailyANdMonthly(UserSubscTypeID, Applicationmanager.getContext());
                }


            } else {
                ApiUtils.handelErrorCode(context, code);
                System.out.println("onFailure");
                hide_progressbar();


            }
        }
        if (functiontype == 2) {
            if (mUser != null) {

                hide_progressbar();
                Log.v("Successobject", mUser + "");
                Log.e("response", mUser + "here");

                userID = mUser.getUserID();
                accesstoken = mUser.getUserToken();
                UserStatusId = mUser.getIsSubsc();
                ServiceID = mUser.getServiceID();
                DownloadKey = mUser.getDownloadKey();
                UserSubscResultDesc = mUser.getUserSubscResultDesc();
                //     String SubscEndDate = mUser.getSubscEndDate();
                String ServiceDailyPrice = mUser.getServiceDailyPrice();

                String ServiceMonthlyPrice = mUser.getServiceMonthlyPrice();
                // ISACTIVE=false;
                mUser.setUserID(userID);
                mUser.setUserToken(accesstoken);
                mUser.setIsSubsc(UserStatusId);
                mUser.setServiceID(ServiceID);
                mUser.setDownloadKey(DownloadKey);
                //     mUser.setSubscEndDate(SubscEndDate);
                mUser.setServiceDailyPrice(ServiceDailyPrice);
                mUser.setServiceMonthlyPrice(ServiceMonthlyPrice);
                SharedPrefHelper.setSharedString(context, Constants.ServiceDailyPrice, ServiceDailyPrice);

                SharedPrefHelper.setSharedString(context, Constants.ServiceMonthlyPrice, ServiceMonthlyPrice);


                SharedPrefHelper.setSharedInt(context, Constants.GenreId, Integer.parseInt("1"));

                SharedPrefHelper.setSharedString(context, Constants.UserStatusId, mUser.getIsSubsc());
                SharedPrefHelper.setSharedString(context, Constants.ServiceID, mUser.getServiceID());
                SharedPrefHelper.getSharedString(context, Constants.UserStatusId);

                //issub .. true ... user success to subscribe
                if (issubscribe.equalsIgnoreCase("true")) {
                    if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                        footertxt.setText(context.getResources().getString(R.string.unsubghannelypremuim));
                        ivfooter.setVisibility(View.GONE);
                        linfooter.setVisibility(View.GONE);
                        if (ServiceID.equalsIgnoreCase("11"))
                        //monthly
                        {
                            setEnd_SubscribeDate(30, context);
                        } else if (ServiceID.equalsIgnoreCase("12"))
                        //daily
                        {
                            setEnd_SubscribeDate(1, context);
                        }

                        Show_Success_Reject_sub_Dialog(true, true, UserSubscResultDesc, context);


                    } else {

                        Show_Success_Reject_sub_Dialog(false, true, UserSubscResultDesc, context);
                    }
                }

                //false  user success to unsubscribe
                if (issubscribe.equalsIgnoreCase("false")) {

                    if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("0")) {
                        footertxt.setText(context.getResources().getString(R.string.getghannelypremuim));
                        ivfooter.setVisibility(View.VISIBLE);

                        Show_Success_Reject_sub_Dialog(true, false, UserSubscResultDesc, context);

                    } else {
                        Show_Success_Reject_sub_Dialog(false, false, UserSubscResultDesc, context);

                        // Show_ToastError_unSub();

                    }
                }


            } else {
                ApiUtils.handelErrorCode(context, code);
                System.out.println("onFailure");

            }
        }

    }


    /*
        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            language = LanguageHelper.getCurrentLanguage(getBaseContext());

            Context context = LocaleHelper.setLocale(getBaseContext(), language);
            Applicationmanager.setContext(context);

            OcreateFunction();
        }
    */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String data = mNavigationDrawerItemTitles[position];

            selectItem(position, data);
            selectedListItem = position;
            Log.i("name", data + "clicked");
            mDrawerLayout.closeDrawers();

        }

        private void selectItem(int position, String data) {

            Intent intent;
            switch (position) {

                case 0:
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;

                case 1:
                    //
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, SearchTabActivity.class);
                        intent.putExtra("titlemenu", data);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;
                case 2:
                    //
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, FavoritesActivity.class);
                        intent.putExtra("titlemenu", data);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;
                case 3:
                    //
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, PlayListActivity.class);
                        intent.putExtra("titlemenu", data);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;
                case 4:
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, InboxActivity.class);
                        intent.putExtra("titlemenu", data);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;

                case 5:
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, Friendlist_Activity.class);
                        intent.putExtra("titlemenu", data);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;

                case 6:
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, FavoritesRadio.class);
                        intent.putExtra("titlemenu", data);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;

                case 7:
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {
                        if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                            StopAllService();
                            intent = new Intent(DrawerActivity.this, KaraokeTabActivity.class);
                            intent.putExtra("titlemenu", data);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                        } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {
                            showGracePopup(getApplicationContext());
                        } else {
                            Show_GhannelyExtra_Dialog(getApplicationContext());

                        }


                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;

                case 8:
                    if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                        intent = new Intent(DrawerActivity.this, DownloadActivity.class);
                        intent.putExtra("titlemenu", data);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {
                        showGracePopup(getApplicationContext());
                    } else {
                        Show_GhannelyExtra_Dialog(getApplicationContext());

                    }




                    break;

                case 9:
                    if (Constants.isNetworkOnline(DrawerActivity.this)) {

                        intent = new Intent(DrawerActivity.this, SeetingActivity.class);
                        intent.putExtra("titlemenu", data);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_x, R.anim.slide_out_x);
                    } else {
                        ShowOfflineconnection_Dialog(getApplicationContext());
                    }
                    break;


                default:
            }

        }


    }

}
