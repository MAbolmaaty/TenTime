package com.binarywaves.ghaneely.ghannelyactivites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.DataBaseHandler;
import com.Ghannely_Encrypt_Decrypt_Tracks.DataBaseVideoHandler;
import com.Ghannely_Encrypt_Decrypt_Tracks.DownloadAndEncryptFileTask;
import com.Ghannely_Encrypt_Decrypt_Tracks.Encryption_DecryptionAudio;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghaneelycashing.Utils;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.BaseActivity;
import com.binarywaves.ghaneely.ghannely_application_manager.NetworkChangeReceiver;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyadaptors.DownloadListAdapter;
import com.binarywaves.ghaneely.ghannelyadaptors.DownloadListVideoAdapter;
import com.binarywaves.ghaneely.ghannelyaodioplayer.controls.Controls;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OFFLINEService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OfflineVideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackDownloadObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.GetUserDownloadedLst;
import com.binarywaves.ghaneely.ghannelyresponse.VideoDownloadObject;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.binarywaves.ghaneely.ghannelyutils.DbBitmapUtility;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 18-Sep-17.
 */

public class DownloadActivity extends BaseActivity {
    public static RelativeLayout audioRelative;
    public static RelativeLayout progBar;
    public static File mEncryptedFile;
    public static DynamicListView downloadListView;
    private static DynamicListView videoList;
    private static DownloadListAdapter listAdapter;
    private static DownloadListVideoAdapter listvideoAdapter;
    private static Encryption_DecryptionAudio enc;
    public static CircleProgressView mCircleView;
    public static LinearLayout lincricle;
    private static Context context;
    private static ImageView play;
    private static ImageView pause;
    private static ProgressWheel pb1;
    private static TextView albumname;
    private static ImageView player_image;
    private static ImageView Like;
    private static ImageView dislike;
    private static Activity activity;
    private static TextView songname;
    private static Context mContext;
    private static String ENCRYPTED_FILE_NAME;
    public static boolean IsDownloadFinished=false;
    private View activityView;
    private FrameLayout frameLayout;
    private ArrayList<TrackDownloadObject> downloadlist;
    private ArrayList<VideoDownloadObject> videolist;
    private ArrayList<TrackObject> trackObjectslst;
    private ArrayList<VideoObjectResponse> videoObjectslst;
    public static int selectedtrack;

    public static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    public static void changeUI() {
        updateUI();
        changeButton();
    }

    private static void updateUI() {
        try {

            TrackDownloadObject data = PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER);

            String language = LanguageHelper.getCurrentLanguage(context);


            if (language.equalsIgnoreCase("ar")) {
                songname.setText(data.getTrackArName());
                albumname.setText(data.getSingerArName());


            }
            if (language.equalsIgnoreCase("en")) {
                songname.setText(data.getTrackEnName());
                albumname.setText(data.getSingerEnName());

            }

            player_image.setImageBitmap(DbBitmapUtility.getImage(data.getTrackImage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeButton() {
        if (PlayerConstants.SONG_PAUSED) {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    private void DeletefromSdcard_sqlvideo(String contentDisposition) {
        startwhellprogress();
        ENCRYPTED_FILE_NAME = contentDisposition;
        Pattern regex = Pattern.compile("(?<=ENCRYPTED_FILE_NAME=\").*?(?=\")");
        Matcher regexMatcher = regex.matcher(contentDisposition);
        if (regexMatcher.find()) {
            ENCRYPTED_FILE_NAME = regexMatcher.group();
        }

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Not sure if the / is on the path or not
        File mEncryptedFile = new File(baseDir + File.separator + ENCRYPTED_FILE_NAME);
        boolean isServiceRunning1 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                context);
        if (isServiceRunning1) {
            if (PlayerConstants.OfflineSONGS_LIST != null && PlayerConstants.OfflineSONGS_LIST.size() > 0) {
                if (PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(contentDisposition)) {

                    Intent i = new Intent(context, OFFLINEService.class);
                    context.stopService(i);

                }
            }
        }
        Log.i("file", mEncryptedFile + "");

        Uri uri = Uri.fromFile(mEncryptedFile);
        File fdelete = new File(uri.getPath());
        if (fdelete.exists()) {
            Boolean delete = FileCache.delete(fdelete);
            if (delete) {
                System.out.println("file Deleted :" + uri.getPath());
                DataBaseVideoHandler mDbHandler;
                mDbHandler = DataBaseVideoHandler.getInstance(mContext);
                mDbHandler.removeSingleItem(contentDisposition);


            } else {
                System.out.println("file not Deleted :" + uri.getPath());
                Toast.makeText(mContext, mContext.getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();
            }
        } else {
            System.out.println("file Deleted :" + uri.getPath());
            DataBaseVideoHandler mDbHandler;
            mDbHandler = DataBaseVideoHandler.getInstance(mContext);
            mDbHandler.removeSingleItem(contentDisposition);

        }

        DeleteUserDownloadTrack(contentDisposition, 0, "2");
    }

    private void DeletefromSdcard_sql(String contentDisposition) {
        startwhellprogress();
        ENCRYPTED_FILE_NAME = contentDisposition;
        Pattern regex = Pattern.compile("(?<=ENCRYPTED_FILE_NAME=\").*?(?=\")");
        Matcher regexMatcher = regex.matcher(contentDisposition);
        if (regexMatcher.find()) {
            ENCRYPTED_FILE_NAME = regexMatcher.group();
        }

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Not sure if the / is on the path or not
        File mEncryptedFile = new File(baseDir + File.separator + ENCRYPTED_FILE_NAME);
        boolean isServiceRunning1 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                context);
        if (isServiceRunning1) {
            if (PlayerConstants.OfflineSONGS_LIST != null && PlayerConstants.OfflineSONGS_LIST.size() > 0) {
                if (PlayerConstants.OfflineSONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(contentDisposition)) {

                    Intent i = new Intent(context, OFFLINEService.class);
                    context.stopService(i);

                }
            }
        }
        Log.i("file", mEncryptedFile + "");

        Uri uri = Uri.fromFile(mEncryptedFile);
        File fdelete = new File(uri.getPath());
        if (fdelete.exists()) {
            Boolean delete = FileCache.delete(fdelete);
            if (delete) {
                System.out.println("file Deleted :" + uri.getPath());
                DataBaseHandler mDbHandler;
                mDbHandler = DataBaseHandler.getInstance(mContext);
                mDbHandler.removeSingleItem(contentDisposition);


            } else {
                System.out.println("file not Deleted :" + uri.getPath());
                Toast.makeText(mContext, mContext.getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();
            }
        } else {
            System.out.println("file Deleted :" + uri.getPath());
            DataBaseHandler mDbHandler;
            mDbHandler = DataBaseHandler.getInstance(mContext);
            mDbHandler.removeSingleItem(contentDisposition);


        }

        DeleteUserDownloadTrack(contentDisposition, 0, "1");
    }

    @SuppressWarnings({"unused", "ConstantConditions"})
    public void showAlertconfirm(String trackid) {
        if (Constants.isNetworkOnline(context)) {
            Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.setContentView(R.layout.dowloaddelete_confirmation_popup);

            Button cancel = dialog.findViewById(R.id.cancel);
            TextView tvtext = dialog.findViewById(R.id.tvtext);

            // if button is clicked, close the custom dialog
            cancel.setOnClickListener(v -> dialog.dismiss());
            tvtext.setText(mContext.getResources().getString(R.string.deletedownload));
            Button confirm = dialog.findViewById(R.id.change);
            // if button is clicked, close the custom dialog
            confirm.setOnClickListener(v -> {
                DeletefromSdcard_sql(trackid);
                dialog.dismiss();

                // reload login page to set it by new languageactive

            });

            dialog.show();
        } else {
            ShowOfflineconnection_Dialog(context);
        }
    }

    public static void SetupCiper2Decrypt_files(String contentDisposition) {
        if (!SharedPrefHelper.getSharedString(context, Constants.DownloadKey).equalsIgnoreCase("")) {
            enc = new Encryption_DecryptionAudio(SharedPrefHelper.getSharedString(context, Constants.DownloadKey));

            ENCRYPTED_FILE_NAME = contentDisposition;
            Pattern regex = Pattern.compile("(?<=ENCRYPTED_FILE_NAME=\").*?(?=\")");
            Matcher regexMatcher = regex.matcher(contentDisposition);
            if (regexMatcher.find()) {
                ENCRYPTED_FILE_NAME = regexMatcher.group();
            }
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Not sure if the / is on the path or not
            mEncryptedFile = new File(baseDir + File.separator + ENCRYPTED_FILE_NAME);

            Log.i("file", mEncryptedFile + "");


            try {
                Encryption_DecryptionAudio.mCipher.init(Cipher.DECRYPT_MODE, Encryption_DecryptionAudio.mSecretKeySpec, Encryption_DecryptionAudio.mIvParameterSpec);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            progBar.setVisibility(View.GONE);
            Toast.makeText(context, context.getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();

        }
    }

    private  void DeleteUserDownloadTrack(String trackid, int isDownloaded, String downloadtype) {


        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.SetUserDownloadTrack + "?trackId=" + trackid + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&isDownloaded="
                + isDownloaded + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken) + "&downloadType="
                + downloadtype;
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.DeleteUserDownloadTrack(fav_url).enqueue(new Callback<GetUserDownloadedLst>() {

            @Override
            public void onResponse(@NonNull Call<GetUserDownloadedLst> call, @NonNull Response<GetUserDownloadedLst> mresult) {
                if (mresult.isSuccessful()) {
                    Log.i("response", mresult.body() + "");
                    //noinspection ConstantConditions
                    if (mresult.isSuccessful()) {
                        //convert json string to object
                        GetUserDownloadedLst response = mresult.body();
                        trackObjectslst = new ArrayList<>();
                        Log.i("response", response + "");
                        trackObjectslst = response != null ? response.getTracksLst() : null;
                        videoObjectslst = response != null ? response.getVideosLst() : null;
                        if (trackObjectslst != null && trackObjectslst.size() > 0) {

                            try {
                                AddtracklistTODataBase(trackObjectslst);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                        if (videoObjectslst != null && videoObjectslst.size() > 0) {

                            try {
                                AddvideolistTODataBase(videoObjectslst);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        progBar.setVisibility(View.GONE);

                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");

                    }
                }
            }


                @Override
            public void onFailure(@NonNull Call<GetUserDownloadedLst> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


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

    @SuppressWarnings("StatementWithEmptyBody")
    private void checkFile_Found2delete(String contentDisposition, String str) {
        startwhellprogress();
        ENCRYPTED_FILE_NAME = contentDisposition;
        Pattern regex = Pattern.compile("(?<=ENCRYPTED_FILE_NAME=\").*?(?=\")");
        Matcher regexMatcher = regex.matcher(contentDisposition);
        if (regexMatcher.find()) {
            ENCRYPTED_FILE_NAME = regexMatcher.group();
        }

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Not sure if the / is on the path or not
        mEncryptedFile = new File(baseDir + File.separator + ENCRYPTED_FILE_NAME);

        Log.i("file", mEncryptedFile + "");

        Uri uri = Uri.fromFile(mEncryptedFile);
        File fdelete = new File(uri.getPath());
        if (fdelete.exists()&&SharedPrefHelper.getSharedBoolean(context, Constants.FirstTimeTODownload)) {
        Boolean delete = FileCache.delete(fdelete);
        if (delete) {
            System.out.println("file Deleted :" + uri.getPath());
  //DownloadFiles AGian
            progBar.setVisibility(View.GONE);
            DownloadActivity.lincricle.setVisibility(View.VISIBLE);
            DownloadActivity.mCircleView.spin();
            SetupCiper2Download(contentDisposition, str);


        } else {
            System.out.println("file not Deleted :" + uri.getPath());
            Toast.makeText(mContext, mContext.getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();
        }
        } else if (!fdelete.exists()){
            lincricle.setVisibility(View.VISIBLE);
            mCircleView.spin();
            System.out.println("file alreadt not found:" + uri.getPath());
            //Downloadfiles
            SetupCiper2Download(contentDisposition, str);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        OnCreateFunction();

    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private void appearanceAnimate() {
        listAdapter = new DownloadListAdapter(this, R.layout.customdownloadlist_item, downloadlist);

        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(listAdapter, this,
                (listView, reverseSortedPositions) -> {
                    if (Constants.isNetworkOnline(context)) {

                    for (int position : reverseSortedPositions) {
                        if(PlayerConstants.OfflineSONGS_LIST!=null&&PlayerConstants.OfflineSONGS_LIST.size()>0){
                        if (PlayerConstants.OfflineSONGS_LIST.get(position) == PlayerConstants.OfflineSONGS_LIST
                                .get(PlayerConstants.SONG_NUMBER)) {
                            boolean isServiceRunning1 = UtilFunctions
                                    .isServiceRunning(OFFLINEService.class.getName(), getApplicationContext());
                            if (isServiceRunning1) {
                                Intent i = new Intent(getApplicationContext(), OFFLINEService.class);
                                stopService(i);
                                audioRelative.setVisibility(View.GONE);


                            }}}
                        DeletefromSdcard_sql(downloadlist.get(position).getTrackId());
                        listAdapter.remove(position);

                        break;

                    }

                } else {
                        ShowOfflineconnection_Dialog(context);
                    } });
        progBar.setVisibility(View.GONE);


        swipeUndoAdapter.setAbsListView(downloadListView);
        downloadListView.setAdapter(swipeUndoAdapter);
        downloadListView.enableSimpleSwipeUndo();
        justifyListViewHeightBasedOnChildren(downloadListView);

    }

    private void appearanceAnimatevideo() {
        listvideoAdapter = new DownloadListVideoAdapter(this, R.layout.customdownloadlist_item, videolist);

        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(listvideoAdapter, this,
                (listView, reverseSortedPositions) -> {
                    if (Constants.isNetworkOnline(context)) {

                        for (int position : reverseSortedPositions) {
                            if(PlayerConstants.OfflineVideo_LIST!=null&&PlayerConstants.OfflineVideo_LIST.size()>0){
                            if (PlayerConstants.OfflineVideo_LIST.get(position) == PlayerConstants.OfflineVideo_LIST
                                    .get(PlayerConstants.SONG_NUMBER)) {
                                boolean isServiceRunning1 = UtilFunctions
                                        .isServiceRunning(OfflineVideoService.class.getName(), getApplicationContext());
                                if (isServiceRunning1) {
                                    Intent i = new Intent(getApplicationContext(), OfflineVideoService.class);
                                    stopService(i);
                                    audioRelative.setVisibility(View.GONE);


                                }}}
                            DeletefromSdcard_sqlvideo(videolist.get(position).getVideoID());
                            listvideoAdapter.remove(position);

                            break;

                        }
                    } else {
                            ShowOfflineconnection_Dialog(context);
                        }
                });
        progBar.setVisibility(View.GONE);
        swipeUndoAdapter.setAbsListView(videoList);
        videoList.setAdapter(swipeUndoAdapter);
        videoList.enableSimpleSwipeUndo();
        justifyListViewHeightBasedOnChildren(videoList);

    }

    private void OnCreateFunction() {
        frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullparent = null;
        assert layoutInflater != null;
        activityView = layoutInflater.inflate(R.layout.activity_download, nullparent, false);

        context = getApplicationContext();
        mContext = DownloadActivity.this;
        activity = this;
        Applicationmanager.setContext(DownloadActivity.this);
        selectedListItem = 8;

        Init_UI();
        audioRelative.setVisibility(View.GONE);

    }

    private void Init_UI() {
        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        pb1.setVisibility(View.GONE);

        progBar.setClickable(false);
        audioRelative = activityView.findViewById(R.id.audioplayer);

        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);
        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);
        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);
        downloadListView = activityView.findViewById(R.id.favTrackList);
        downloadListView.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            SetupCiper2Decrypt_files(downloadlist.get(position).getTrackId());
            PlaySongFromList(position, 1);
            selectedtrack=position;

        });
        videoList = activityView.findViewById(R.id.videoList);
        videoList.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub
            SetupCiper2Decrypt_files(videolist.get(position).getVideoID());
            PlaySongFromList(position, 2);

        });
        if (Constants.isNetworkOnline(DownloadActivity.this)) {
            GetUserDownloadedLst();
        } else {
            if (SharedPrefHelper.getSharedBoolean(context, Constants.FirstTimeTODownload)) {
                boolean isServiceRunning2 = UtilFunctions.isServiceRunning(NetworkChangeReceiver.class.getName(),
                        context);
                if (isServiceRunning2) {
                    Intent i = new Intent(context, NetworkChangeReceiver.class);
                    context.stopService(i);


                }
                showSyncPopup(mContext);
            } else {
                boolean isServiceRunning2 = UtilFunctions.isServiceRunning(NetworkChangeReceiver.class.getName(),
                        context);
                if (isServiceRunning2) {
                    Intent i = new Intent(context, NetworkChangeReceiver.class);
                    context.stopService(i);


                }
                get_DOWNLOAD_FROM_DATABASE();
            }
        }
        setListeners();
        try {
            boolean isServiceRunning = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning) {
                audioRelative.setVisibility(View.VISIBLE);
                changeUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lincricle = activityView.findViewById(R.id.cirrcleprogress);
        mCircleView = lincricle.findViewById(R.id.circleView);
        mCircleView.setShowTextWhileSpinning(false);
        mCircleView.setTextColorAuto(false);
        mCircleView.setDirection(Direction.CW);
        frameLayout.addView(activityView);


    }

    public static void showSyncPopup(Context context) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        dialog.setContentView(R.layout.activity_download_syncaccount);

        Button retry = dialog.findViewById(R.id.change);

        retry.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(context, DownloadActivity.class);
            context.startActivity(intent);
            getActivity().finish();


        });


        Button cancel = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v ->{ dialog.dismiss();
        System.exit(0);
    });

        // if button is clicked, close the custom dialog

        dialog.show();
    }

    private void setListeners() {
        dislike.setVisibility(View.GONE);
        Like.setVisibility(View.GONE);

        pause.setOnClickListener(v -> {

            PlayerConstants.SONGnext = false;

            Controls.pauseControl(getApplicationContext());
        });

        play.setOnClickListener(v -> {
            PlayerConstants.SONGnext = false;
            Controls.playControl(getApplicationContext());
        });


    }

    private void get_DOWNLOAD_FROM_DATABASE() {
        startwhellprogress();
        DataBaseHandler mDbHandler;
        mDbHandler = DataBaseHandler.getInstance(context);
        // Reading all shops
        Log.d("Reading: ", "Reading all shops..");
        downloadlist = mDbHandler.getAllDownloads();

        for (TrackDownloadObject shop : downloadlist) {
            String log = "Id: " + shop.getTrackArName() + " ,Name: " + shop.getTrackEnName() + " ,Address: " + shop.getTrackLength();
            // Writing shops  to log
            Log.d("Shop: : ", log);
        }
        if (downloadlist.size() > 0) {
            appearanceAnimate();

        }

        DataBaseVideoHandler mDbHandlervideo;
        mDbHandlervideo = DataBaseVideoHandler.getInstance(context);
        // Reading all shops
        Log.d("Reading: ", "Reading all shops..");
        videolist = mDbHandlervideo.getAllDownloads();

        for (VideoDownloadObject shop : videolist) {
            String log = "Id: " + shop.getVideoEnName() + " ,Name: " + shop.getVideoArName() + " ,Address: " + shop.getVideoID();
            // Writing shops  to log
            Log.d("Shop: : ", log);
        }
        if (videolist.size() > 0) {
            appearanceAnimatevideo();

        }
        SharedPrefHelper.setSharedBoolean(context, Constants.FirstTimeTODownload, false);

    }

    private void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(context, getApplication());
        tracklisiten.StopService();

    }

    @Override
    public void onBackPressed() {
       if(pb1!=null){
           pb1.setVisibility(View.GONE);
       }
    }


    private void PlaySongFromList(int position, int type) {
        if (type == 1) {
            Log.e("list clicked ", "here");
            startwhellprogress();
            audioRelative.setEnabled(false);

            StopService();
            PlayerConstants.OfflineSONGS_LIST = downloadlist;

            PlayerConstants.SONG_PAUSED = false;
            PlayerConstants.SONG_NUMBER = position;

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                    getApplicationContext());
            if (!isServiceRunning3) {

                Intent i = new Intent(getApplicationContext(), OFFLINEService.class);
                startService(i);

                // progBar.setVisibility(View.GONE);

            } else {
                PlayerConstants.SONG_CHANGE_HANDLER
                        .sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
            }

            changeUI();


            Log.d("TAG", "TAG Tapped INOUT(OUT)");
        }
        if (type == 2) {
            Log.e("list clicked ", "here");
            startwhellprogress();
            audioRelative.setEnabled(false);

            StopService();
            PlayerConstants.OfflineVideo_LIST = videolist;
            Intent playTrack1 = new Intent(DownloadActivity.this, Activity_fullvideodownload.class);
            playTrack1.putExtra("position", position);
            startActivity(playTrack1);
        }
    }

    private void GetUserDownloadedLst() {
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetUserDownloadedLst + "?userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);


        Api_Interface service = ServiceGenerator.createService();
        service.GetUserDownloadedLst(fav_url).enqueue(new Callback<GetUserDownloadedLst>() {

            @Override
            public void onResponse(@NonNull Call<GetUserDownloadedLst> call, @NonNull Response<GetUserDownloadedLst> mresult) {
                if (mresult.isSuccessful()) {
                    //convert json string to object
                    GetUserDownloadedLst response = mresult.body();
                    trackObjectslst = new ArrayList<>();
                    Log.i("response", response + "");
                    trackObjectslst = response != null ? response.getTracksLst() : null;
                    videoObjectslst = response != null ? response.getVideosLst() : null;
                    if (trackObjectslst != null && trackObjectslst.size() > 0) {

                        try {
                            AddtracklistTODataBase(trackObjectslst);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    if (videoObjectslst != null && videoObjectslst.size() > 0) {

                        try {
                            AddvideolistTODataBase(videoObjectslst);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    progBar.setVisibility(View.GONE);

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<GetUserDownloadedLst> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });

    }

    private void AddvideolistTODataBase(ArrayList<VideoObjectResponse> videoObjectslst) throws IOException {
        DataBaseVideoHandler mDbHandler;
        mDbHandler = DataBaseVideoHandler.getInstance(context);
        mDbHandler.delete();
        for (int i = 0; i < videoObjectslst.size(); i++) {
            startwhellprogress();

            Log.d("Insert: ", "Inserting ..");
            BitmapDrawable drawable = Constants.drawableFromUrl(getApplicationContext(), ServerConfig.Video_Imagepath + videoObjectslst.get(i).getVideoPoster());
            Bitmap bitmap = drawable.getBitmap();
            DbBitmapUtility.getBytes(bitmap);
            VideoDownloadObject trackobject = new VideoDownloadObject();
            trackobject.setVideoPoster(DbBitmapUtility.getBytes(bitmap));
            mDbHandler.addItem(new VideoDownloadObject(videoObjectslst.get(i).getVideoEnName(),videoObjectslst.get(i).getVideoID(), videoObjectslst.get(i).getVideoArName(), videoObjectslst.get(i).getSingerEnName(), videoObjectslst.get(i).getSingerArName(), trackobject.getVideoPoster()));
            String str = ServerConfig.Video_Url + videoObjectslst.get(i).getVideoPath().replaceAll(" ", "%20");
            checkFile_Found2delete(videoObjectslst.get(i).getVideoID(), str);
            Log.e("audiourl", str);

        }

        get_DOWNLOAD_FROM_DATABASE();
    }

    private void AddtracklistTODataBase(ArrayList<TrackObject> trackObjectslst) throws IOException {

        DataBaseHandler mDbHandler;
        mDbHandler = DataBaseHandler.getInstance(context);
        mDbHandler.delete();
        for (int i = 0; i < trackObjectslst.size(); i++) {
            startwhellprogress();

            Log.d("Insert: ", "Inserting ..");
            BitmapDrawable drawable = Constants.drawableFromUrl(getApplicationContext(), ServerConfig.Image_path + trackObjectslst.get(i).getTrackImage());
            Bitmap bitmap = drawable.getBitmap();
            DbBitmapUtility.getBytes(bitmap);
            TrackDownloadObject trackobject = new TrackDownloadObject();
            trackobject.setTrackImage(DbBitmapUtility.getBytes(bitmap));
            mDbHandler.addItem(new TrackDownloadObject( trackObjectslst.get(i).getTrackEnName(),trackObjectslst.get(i).getTrackId(), trackObjectslst.get(i).getTrackArName(), trackObjectslst.get(i).getSingerEnName(), trackObjectslst.get(i).getSingerArName(), trackObjectslst.get(i).getTrackLength(), trackobject.getTrackImage()));
            String str = ServerConfig.AudioUrl + trackObjectslst.get(i).getTrackPath().replaceAll(" ", "%20");
            checkFile_Found2delete(trackObjectslst.get(i).getTrackId(), str);
            Log.e("audiourl", str);

        }

        get_DOWNLOAD_FROM_DATABASE();
    }

    private void SetupCiper2Download(String contentDisposition, String audiolink) {

        ENCRYPTED_FILE_NAME = contentDisposition;
        Pattern regex = Pattern.compile("(?<=contentDisposition=\").*?(?=\")");
        Matcher regexMatcher = regex.matcher(contentDisposition);
        if (regexMatcher.find()) {
            ENCRYPTED_FILE_NAME = regexMatcher.group();
        }

        if (Checkexternalstorage() >= 0) {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Not sure if the / is on the path or not
            Encryption_DecryptionAudio.mEncryptedFile = new File(baseDir + File.separator + ENCRYPTED_FILE_NAME);
            Log.i("file", Encryption_DecryptionAudio.mEncryptedFile + "");
            encryptVideo_Audio(audiolink);
        } else {

            Log.i(getClass().getCanonicalName(), "encrypted file found, no need to recreate");
            progBar.setVisibility(View.GONE);

            Toast.makeText(DownloadActivity.this, "No Space", Toast.LENGTH_LONG).show();
        }
//        try {
//            enc.mCipher.init(Cipher.DECRYPT_MODE, enc.mSecretKeySpec, enc.mIvParameterSpec);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private Double Checkexternalstorage() {

        Double finalspace = Utils.getAvailableExternalMemorySize();

        Log.i("value", finalspace + "vvv" + finalspace);


        return finalspace;
    }

    @SuppressWarnings("unused")
    private boolean hasFile() {
        return Encryption_DecryptionAudio.mEncryptedFile != null
                && Encryption_DecryptionAudio.mEncryptedFile.exists()
                && Encryption_DecryptionAudio.mEncryptedFile.length() > 0;
    }

    private void encryptVideo_Audio(String url) {
        // startwhellprogress();
        lincricle.setVisibility(View.VISIBLE);
        mCircleView.spin();
        try {
            if (!SharedPrefHelper.getSharedString(context, Constants.DownloadKey).equalsIgnoreCase("")) {
                enc = new Encryption_DecryptionAudio(SharedPrefHelper.getSharedString(context, Constants.DownloadKey));

                Encryption_DecryptionAudio.mCipher.init(Cipher.ENCRYPT_MODE, Encryption_DecryptionAudio.mSecretKeySpec, Encryption_DecryptionAudio.mIvParameterSpec);
                // TODO:
                // you need to encrypt a video somehow with the same key and iv...  you can do that yourself and update
                // the ciphers, key and iv used in this demo, or to see it from top to bottom,
                // supply a url to a remote unencrypted file - this method will downloadplus and encrypt it
                // this first argument needs to be that url, not null or empty...
                new DownloadAndEncryptFileTask(url, Encryption_DecryptionAudio.mEncryptedFile, Encryption_DecryptionAudio.mCipher, 2).execute();
            } else {
                lincricle.setVisibility(View.GONE);
                Toast.makeText(DownloadActivity.this, getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
