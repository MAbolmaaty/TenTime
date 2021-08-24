package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.DataBaseVideoHandler;
import com.Ghannely_Encrypt_Decrypt_Tracks.DownloadAndEncryptFileTask;
import com.Ghannely_Encrypt_Decrypt_Tracks.Encryption_DecryptionAudio;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghaneelycashing.Utils;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.KaraokeTabActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelyadaptors.addListAdaptor;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OFFLINEService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.VideoDownloadObject;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelyservice.Addfavourite_Response;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.binarywaves.ghaneely.ghannelyutils.DbBitmapUtility;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 06-Jul-17.
 */

public class AddtoVideoActivity extends ActivityMainRunnuing {
    public static Activity activity;
    private static RelativeLayout progBar;
    private static String isfavourite;
    public static VideoObjectResponse trask;
    private static String ENCRYPTED_FILE_NAME = "encrypted.m4a";
    public static CircleProgressView mCircleView;
    public static LinearLayout lincricle;
    private static String favo;
    static String trackid;
    private static Context context;
    private static RelativeLayout relbackground;
    static Bitmap bitmap;
    private static ProgressWheel pb1;
    private static ImageView Image_player;
    private static ImageView iv;
    private BottomSheetLayout bottomSheetLayout;
    private ListView mList;
    private addListAdaptor madaptor;
    private String[] mAraay;
    private String[] mAraayunlike;
    private ImageView close;
    private TextView songname;
    private TextView albumname;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private String language;
    private String like;
    private Dialog dialog;
    private Encryption_DecryptionAudio enc;


    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private static void setBg(RelativeLayout layout, BitmapDrawable TileMe) {
        layout.setBackground(TileMe);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    public static void ADD_DOWNLOAD_2_DATABASE() {
        DataBaseVideoHandler mDbHandler;
        mDbHandler = DataBaseVideoHandler.getInstance(context);
        Log.d("Insert: ", "Inserting ..");
        BitmapDrawable drawable = (BitmapDrawable) Image_player.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        DbBitmapUtility.getBytes(bitmap);
        VideoDownloadObject trackobject = new VideoDownloadObject();
        trackobject.setVideoPoster(DbBitmapUtility.getBytes(bitmap));
        mDbHandler.addItem(new VideoDownloadObject(trask.getVideoEnName(),trask.getVideoID(),trask.getVideoArName(), trask.getSingerEnName(), trask.getSingerArName(), trackobject.getVideoPoster()));

        // Reading all shops
        Log.d("Reading: ", "Reading all shops..");
//        List<VideoDownloadObject> shops = mDbHandler.getAllDownloads();
//
//        for (VideoDownloadObject shop : shops) {
//            String log = "Id: " + shop.getVideoArName() + " ,Name: " + shop.getVideoEnName() + " ,Address: " + shop.getVideoID();
//            // Writing shops  to log
//            Log.d("Shop: : ", log);
//        }
        AddUserDownloadTrack(trask.getVideoID(), 1, "2");
        Toast.makeText(context, context.getResources().getString(R.string.downloadvideosuccess), Toast.LENGTH_LONG).show();


    }

    private static void DeletefromSdcard_sql(String contentDisposition, File mEncryptedFile) {
        boolean isServiceRunning1 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                context);
        if (isServiceRunning1) {
            if (PlayerConstants.OfflineVideo_LIST != null && PlayerConstants.OfflineVideo_LIST.size() > 0) {
                if (PlayerConstants.OfflineVideo_LIST.get(PlayerConstants.SONG_NUMBER).getVideoID().equalsIgnoreCase(contentDisposition)) {


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
                mDbHandler = DataBaseVideoHandler.getInstance(context);
                mDbHandler.removeSingleItem(contentDisposition);


            }

        }


    }

    private static void AddUserDownloadTrack(String trackid, int isDownloaded, String downloadtype) {
        startwhellprogress();
        String fav = ServerConfig.SERVER_URl + ServerConfig.AddUserDownloadTrack + "?trackId=" + trackid + "&userId="
                + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&isDownloaded="
                + isDownloaded + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken) + "&downloadType="
                + downloadtype;
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddUserDownloadTrack(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {

                    Addrbtresponse response = mresult.body();
                    progBar.setVisibility(View.GONE);
                    if (response != null && response.getResultCode().equalsIgnoreCase("true")) {
                        if (isDownloaded == 1) {
                            iv.setBackgroundResource(R.mipmap.downloadon);
                            if (trask.getVideoID().equalsIgnoreCase(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID())) {
                                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setIsDownloaded("true");
                            }
                            trask.setIsDownloaded("true");


                        } else {
                            iv.setBackgroundResource(R.mipmap.downloadplus);
                            if (trask.getVideoID().equalsIgnoreCase(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID())) {
                                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setIsDownloaded("false");
                            }
                            trask.setIsDownloaded("false");


                        }
                    }
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_add_to);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.setFinishOnTouchOutside(false);

        Applicationmanager.setContext(AddtoVideoActivity.this);

        context = getApplicationContext();

        language = LanguageHelper.getCurrentLanguage(context);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        InitilaizeUI();

        mList.setOnItemClickListener((parent, view, position, id) -> {
            TextView tv = view.findViewById(R.id.add_player_text);
            iv = view.findViewById(R.id.add_player_icon);
            // TODO Auto-generated method stub
            switch (position) {
                case 0:


                    if (language.equalsIgnoreCase("ar")) {
                        if (tv.getText().toString().equalsIgnoreCase("اعجبنى")) {
                            tv.setText(getResources().getString(R.string.unlike));
                            iv.setBackgroundResource(R.mipmap.unlike);
                            Add_Removefavourite(trask.getVideoID(), getApplicationContext(), "1");
                            favo = "true";

                        } else if (tv.getText().toString().equalsIgnoreCase("غير معجب")) {
                            tv.setText(getResources().getString(R.string.like));
                            iv.setBackgroundResource(R.mipmap.like);

                            Add_Removefavourite(trask.getVideoID(), getApplicationContext(), "0");
                            favo = "false";

                        }

                    }
                    if (language.equalsIgnoreCase("en")) {
                        if (tv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.like))) {
                            tv.setText(getResources().getString(R.string.unlike));
                            iv.setBackgroundResource(R.mipmap.unlike);
                            Add_Removefavourite(trask.getVideoID(), getApplicationContext(), "1");
                            favo = "true";

                        } else {
                            if (tv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.unlike))) {
                                tv.setText(getResources().getString(R.string.like));
                                iv.setBackgroundResource(R.mipmap.like);

                                Add_Removefavourite(trask.getVideoID(), getApplicationContext(), "0");
                                favo = "false";

                            }
                        }

                    }

                    break;
                case 1:
                    if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                        if (trask.getIsDownloaded().equalsIgnoreCase("true")) {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.alreadydownloaded),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // startwhellprogress();
                            lincricle.setVisibility(View.VISIBLE);
                            mCircleView.spin();
                            String str = ServerConfig.Video_Url + trask.getVideoPath().replaceAll(" ", "%20");
                            SetupCiper2Download(trask.getVideoID(), str);
                            Log.e("audiourl", str);

                        }
                    } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {
                        DrawerActivity.showGracePopup(getApplicationContext());
                    } else {
                        Show_GhannelyExtra_Dialog();

                    }





                    break;
                case 2:
                    sharetext();

                    break;


                case 3:
                    Intent playTrack1 = new Intent(AddtoVideoActivity.this, ArtistradioActivity.class);
                    playTrack1.putExtra("album", trask.getSingerId());


                    if (language.equalsIgnoreCase("ar")) {
                        playTrack1.putExtra("title", trask.getSingerArName());
                    }
                    if (language.equalsIgnoreCase("en")) {
                        playTrack1.putExtra("title", trask.getSingerEnName());
                    }

                    startActivity(playTrack1);
                    finish();
                    break;

                case 4:
                    Intent playTrack = new Intent(AddtoVideoActivity.this, ArtistTabsActivity.class);
                    playTrack.putExtra("playlistId", Integer.valueOf(trask.getSingerId()));
                    startActivity(playTrack);
                    finish();

                    break;
                default:
                    break;
            }

        });
    }

    private void Show_GhannelyExtra_Dialog() {

        dialog = new Dialog(AddtoVideoActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        dialog.setContentView(R.layout.subscribe2premium);

        Button btsubscribe = dialog.findViewById(R.id.btremoveads);

        btsubscribe.setOnClickListener(v -> {
            dialog.dismiss();
            DrawerActivity.GetSubscribeOption(getApplicationContext());

        });


        Button cancel = dialog.findViewById(R.id.btclose);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        // if button is clicked, close the custom dialog

        dialog.show();
    }

    private void InitilaizeUI() {
        relbackground = findViewById(R.id.relbackground);
        if (!SharedPrefHelper.getSharedString(context, Constants.DownloadKey).equalsIgnoreCase("")) {


            enc = new Encryption_DecryptionAudio(SharedPrefHelper.getSharedString(context, Constants.DownloadKey));
        }
        mList = findViewById(R.id.player_list1);
        bottomSheetLayout = findViewById(R.id.bottomsheet);

        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        context = getApplicationContext();
        songname = findViewById(R.id.song_title);

        albumname = findViewById(R.id.album_title);

        progBar.setClickable(false);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("trask")) {

                trask = extras.getParcelable("trask");
                if (language.equalsIgnoreCase("ar")) {
                    songname.setText(trask.getVideoArName());
                    albumname.setText(trask.getSingerArName());

                }
                if (language.equalsIgnoreCase("en")) {
                    songname.setText(trask.getVideoEnName());
                    albumname.setText(trask.getSingerEnName());

                }

                isfavourite = trask.getIsFavourite();

                if (isfavourite.equalsIgnoreCase("true")) {
                    mAraayunlike = getResources().getStringArray(R.array.video_listunlike);
                    madaptor = new addListAdaptor(AddtoVideoActivity.this, R.layout.add_palyer_list_item, mAraayunlike);

                }

                if (isfavourite.equalsIgnoreCase("false")) {
                    mAraay = getResources().getStringArray(R.array.video_list);
                    madaptor = new addListAdaptor(AddtoVideoActivity.this, R.layout.add_palyer_list_item, mAraay);

                }
                mList.setAdapter(madaptor);
                setListViewHeightBasedOnChildren1(mList);

            }
        }

        //  FromPlayer = getIntent().getExtras().getString("FromPlayer");
        Image_player = findViewById(R.id.Image_player);

        Picasso.with(AddtoVideoActivity.this).load((ServerConfig.Video_Imagepath + trask.getVideoPoster()))
                .error(R.mipmap.defualt_img).placeholder(R.mipmap.defualt_img).into(Image_player);
        BitmapDrawable drawable = (BitmapDrawable) Image_player.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        GaussianBlur gaussian = new GaussianBlur(getApplicationContext());
        gaussian.setMaxImageSize(60);
        gaussian.setRadius(25); // max

        Bitmap output = gaussian.render(bitmap, true);
        final BitmapDrawable ob = new BitmapDrawable(getResources(), output);
        setBg(relbackground, ob);
        close = findViewById(R.id.close_player);
        close.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            finish();
        });
        lincricle = findViewById(R.id.cirrcleprogress);
        mCircleView = lincricle.findViewById(R.id.circleView);
        mCircleView.setShowTextWhileSpinning(false);
        mCircleView.setDirection(Direction.CW);

    }

    private void showCustomAlert(int fav) {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullparent = null;
        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, nullparent);
        ImageView image = toastRoot.findViewById(R.id.ivtoast);
        if (fav == 1) {
            image.setImageResource(R.mipmap.heart);

        }
        if (fav == 0) {
            image.setImageResource(R.mipmap.brokenheart1);

        }

        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }

    private void Add_Removefavourite(String trackid2, final Context context2, final String favourite) {
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddFavouriteVideoClip + "?videoClipId=" + trackid2 + "&userId="
                + SharedPrefHelper.getSharedString(context2, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context2, Constants.accesstoken)
                + "&isFavourite=" + favourite;
        String fav_url = fav.replaceAll(" ", "%20");

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddFavouriteTrack(fav_url).enqueue(new Callback<Addfavourite_Response>() {

            @Override
            public void onResponse(@NonNull Call<Addfavourite_Response> call, @NonNull Response<Addfavourite_Response> mresult) {


                if (mresult.isSuccessful()) {

                    //convert json string to object
                    HandleFavUi(mresult.body(), favourite);

                } else {
                    ApiUtils.handelErrorCode(context2, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Addfavourite_Response> call, @NonNull Throwable t) {

            }
        });
    }

    private void HandleFavUi(Addfavourite_Response response, final String favourite) {
        if (response != null) {
            like = response.getResultDesc();
            progBar.setVisibility(View.GONE);
            if (favourite.equalsIgnoreCase("1")) {
                showCustomAlert(1);

                boolean isServiceRunning3 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                        getApplicationContext());
                if (isServiceRunning3) {

                    if (language.equalsIgnoreCase("ar")) {
                        PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                        VideoPlayerActivity.likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());


                    }
                    if (language.equalsIgnoreCase("en")) {
                        PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);

                        VideoPlayerActivity.likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());

                    }
                    PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setIsFavourite("true");
                    trask.setIsFavourite("true");
                    favo = "true";

                }
            } else {
                if (favourite.equalsIgnoreCase("0")) {
                    showCustomAlert(0);


                    boolean isServiceRunning3 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                            getApplicationContext());
                    if (isServiceRunning3) {
                        if (language.equalsIgnoreCase("ar")) {
                            PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                            VideoPlayerActivity.likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());


                        }
                        if (language.equalsIgnoreCase("en")) {
                            PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                            VideoPlayerActivity.likescount.setText(PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getLikesCount());
                        }
                        PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).setIsFavourite("false");
                        trask.setIsFavourite("false");

                        favo = "false";

                    }
                }
            }
        }


    }

    private void setListViewHeightBasedOnChildren1(ListView listView) {
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
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on
            // external storage.
            // This way, you don't need to request external read/write
            // permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void sharetext() {


        if (language.equalsIgnoreCase("ar")) {
            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                if (PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID()
                        .equalsIgnoreCase(trask.getVideoID())) {

                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoArName();
                        String artistname = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER)
                                .getSingerArName();

                        ShareIntent(trackname, artistname,
                                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoPoster(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID(), 1);
                    }
                } else {
                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = trask.getVideoArName();
                        String artistname = trask.getSingerArName();

                        ShareIntent(trackname, artistname, trask.getVideoPoster(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getVideoID(), 0);
                    }
                }
            }


            if (!isServiceRunning3
                    ) {
                Uri bmpUri = getLocalBitmapUri(Image_player);
                if (bmpUri != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    intent.setType("image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_TEXT, "http://ghaneely.binarywaves.com/GhaneelyMobile/downloadplus.aspx");

                    ShareIntent(trask.getVideoArName(), trask.getSingerArName(), trask.getVideoPoster(),
                            SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getVideoID(), 0);
                }
            }
        }
        if (language.equalsIgnoreCase("en")) {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                if (PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID()
                        .equalsIgnoreCase(trask.getVideoID())) {

                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoEnName();
                        String artistname = PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER)
                                .getSingerEnName();

                        ShareIntent(trackname, artistname,
                                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoPoster(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                                PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID(), 1);
                    }
                } else {
                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = trask.getVideoEnName();
                        String artistname = trask.getSingerEnName();

                        ShareIntent(trackname, artistname, trask.getVideoPoster(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getVideoID(), 0);
                    }
                }
            }


            if (!isServiceRunning3
                    ) {
                Uri bmpUri = getLocalBitmapUri(Image_player);
                if (bmpUri != null) {

                    ShareIntent(trask.getVideoEnName(), trask.getSingerEnName(), trask.getVideoPoster(),
                            SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getVideoID(), 0);
                }
            }

        }
    }

    private void setupFacebookShareIntent(String url) {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle(null).setContentDescription(null)
                .setContentUrl(Uri.parse(url)).build();

        shareDialog.show(linkContent);
    }

    private void ShareIntent(final String title, final String desc, final String image, final String userid,
                             final String trackid, final int i) {


        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String sharerUrl = "https://bw.ghaneely.com/share/d.aspx?" + "t=" + title + "&i=" + image + "&s="
                + desc + "&l=1" + "&tk=" + trackid + "&u=" + userid + "&ls=" + i + "&v=1";
        String fav_url = sharerUrl.replaceAll(" ", "%20");
        //   shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        // shareIntent.setType("image/*");

        if (i == 1) {
            if (language.equalsIgnoreCase("ar")) {
                String total = " أنا بشاهد " + " " + title + "  " + "ل" + " " + desc + " "
                        + "علي تطبيق غنيلي , اضغط هنا للتحميل " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);
            } else {
                String total = " I am Watching " + " " + title + "  " + "by" + " " + desc + " "
                        + "on ghaneely app , download now from " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);

            }
        }

        if (i == 0) {
            if (language.equalsIgnoreCase("ar")) {
                String total = "شاهد" + " " + title + "  " + "ل" + " " + desc + " "
                        + "علي تطبيق غنيلي , اضغط هنا للتحميل " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);
            } else {
                String total = "Watch " + " " + title + "  " + "by" + " " + desc + " "
                        + "on ghaneely app , download now from " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);

            }
        }
        shareIntent.setType("text/plain");
        IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(AddtoVideoActivity.this, shareIntent, "Share with...", activityInfo -> {
            bottomSheetLayout.dismissSheet();
            if (activityInfo.componentName.getPackageName().startsWith("com.facebook.katana")) {
                // share facebook
                // http://ghaneely.binarywaves.com/GhaneelyMobile/download.aspx?title=test&albumImg=819.jpg&desc=desc
                String language = SharedPrefHelper.getSharedString(getApplicationContext(), "langid");
                String sharerUrl1;
                if (language.equalsIgnoreCase("ar")) {
                    sharerUrl1 = "https://bw.ghaneely.com/share/d.aspx?" + "t=" + title + "&i=" + image + "&s="
                            + desc + "&l=2" + "&tk=" + trackid + "&u=" + userid + "&ls=" + i + "&v=1";
                } else {
                    sharerUrl1 = "https://bw.ghaneely.com/share/d.aspx?" + "t=" + title + "&i=" + image + "&s="
                            + desc + "&l=1" + "&tk=" + trackid + "&u=" + userid + "&ls=" + i + "&v=1";

                }

                String fav_url1 = sharerUrl1.replaceAll(" ", "%20");

                setupFacebookShareIntent(fav_url1);
            } else {
                startActivity(activityInfo.getConcreteIntent(shareIntent));
            }
        });
        // Filter out built in sharing options such as bluetooth and beam.
        intentPickerSheet.setFilter(info -> !info.componentName.getPackageName().startsWith("com.android"));
        // Sort activities in reverse order for no good reason
        intentPickerSheet.setSortMethod((lhs, rhs) -> rhs.label.compareTo(lhs.label));

        // Add custom mixin example
        //	Drawable customDrawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null);
        //	IntentPickerSheetView.ActivityInfo customInfo = new IntentPickerSheetView.ActivityInfo(customDrawable, "Custom mix-in", PlayerAcreenActivity.this, MainActivity.class);
        //	intentPickerSheet.setMixins(Collections.singletonList(customInfo));

        bottomSheetLayout.showWithSheetView(intentPickerSheet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

            Toast.makeText(AddtoVideoActivity.this, "No Space", Toast.LENGTH_LONG).show();
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

    private boolean hasFile() {
        return Encryption_DecryptionAudio.mEncryptedFile != null
                && Encryption_DecryptionAudio.mEncryptedFile.exists()
                && Encryption_DecryptionAudio.mEncryptedFile.length() > 0;
    }

    private void encryptVideo_Audio(String url) {

        if (hasFile()) {
            Log.d(getClass().getCanonicalName(), "encrypted file found, no need to recreate");
            DeletefromSdcard_sql(trask.getVideoID(), Encryption_DecryptionAudio.mEncryptedFile);
        }

        try {
            if (!SharedPrefHelper.getSharedString(context, Constants.DownloadKey).equalsIgnoreCase("")) {
                enc = new Encryption_DecryptionAudio(SharedPrefHelper.getSharedString(context, Constants.DownloadKey));

                Encryption_DecryptionAudio.mCipher.init(Cipher.ENCRYPT_MODE, Encryption_DecryptionAudio.mSecretKeySpec, Encryption_DecryptionAudio.mIvParameterSpec);
                // TODO:
                // you need to encrypt a video somehow with the same key and iv...  you can do that yourself and update
                // the ciphers, key and iv used in this demo, or to see it from top to bottom,
                // supply a url to a remote unencrypted file - this method will downloadplus and encrypt it
                // this first argument needs to be that url, not null or empty...
                new DownloadAndEncryptFileTask(url, Encryption_DecryptionAudio.mEncryptedFile, Encryption_DecryptionAudio.mCipher, 3).execute();
            } else {
                lincricle.setVisibility(View.GONE);
                Toast.makeText(AddtoVideoActivity.this, getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            super.finish();
        }
    }

}