package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.DataBaseHandler;
import com.Ghannely_Encrypt_Decrypt_Tracks.DownloadAndEncryptFileTask;
import com.Ghannely_Encrypt_Decrypt_Tracks.DownloadAudioFile;
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
import com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs.AlbumTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs.ArtistTabsActivity;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs.KaraokeTabActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelyadaptors.MyPlaylistAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.addListAdaptor;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OFFLINEService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackDownloadObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.Playlistlst;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.Direction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION_CODES.M;

public class AddToActivity extends ActivityMainRunnuing {
    public static RelativeLayout progBar;
    private static String isfavourite;
    public static TrackObject trask;
    private static String ENCRYPTED_FILE_NAME = "encrypted.m4a";
    public static CircleProgressView mCircleView;
    public static LinearLayout lincricle;
    private static String favo;
    static String trackid;
    public static Context context;
    private static RelativeLayout relbackground;
    static Bitmap bitmap;
    private static ProgressWheel pb1;
    private static ImageView Image_player;
    private static ImageView iv;
    private static addListAdaptor madaptor;
    public Activity activity;
    private BottomSheetLayout bottomSheetLayout;
    private ListView mList;
    private String[] mAraay;
    private String[] mAraayunlike;
    private ImageView close;
    private String FromPlayer;
    private TextView songname;
    private TextView albumname;
    private ArrayList<SlideAlbumObject> mPlaylistObjects;
    private Dialog dialog;
    private Dialog cretaeplaylistdialog;
    private ListView lvplaylist;
    private Button playchange;
    private Button playcancel;
    private ArrayList<Playlistlst> mPlaylistList;
    private MyPlaylistAdaptor mAdaptor;
    private EditText playlistname;
    private Dialog playlistdialog;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private String language;
    private String like;
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

    public static void ADD_DOWNLOAD_2_DATABASE() {
        DataBaseHandler mDbHandler;
        mDbHandler = DataBaseHandler.getInstance(context);
        Log.d("Insert: ", "Inserting ..");
        BitmapDrawable drawable = (BitmapDrawable) Image_player.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        DbBitmapUtility.getBytes(bitmap);
        TrackDownloadObject trackobject = new TrackDownloadObject();
        trackobject.setTrackImage(DbBitmapUtility.getBytes(bitmap));
        mDbHandler.addItem(new TrackDownloadObject(trask.getTrackEnName(), trask.getTrackId(), trask.getTrackArName(), trask.getSingerEnName(), trask.getSingerArName(), trask.getTrackLength(), trackobject.getTrackImage()));

        // Reading all shops
        Log.d("Reading: ", "Reading all shops..");
//        List<TrackDownloadObject> shops = mDbHandler.getAllDownloads();
//
//        for (TrackDownloadObject shop : shops) {
//            String log = "Id: " + shop.getTrackArName() + " ,Name: " + shop.getTrackEnName() + " ,Address: " + shop.getTrackLength();
//            // Writing shops  to log
//            Log.d("Shop: : ", log);
//        }
        AddUserDownloadTrack(trask.getTrackId(), 1, "1");
        Toast.makeText(context, context.getResources().getString(R.string.downloadsuccess), Toast.LENGTH_LONG).show();


    }

    private static void DeletefromSdcard_sql(String contentDisposition, File mEncryptedFile) {
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
                mDbHandler = DataBaseHandler.getInstance(context);
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
                        if (PlayerConstants.SONGS_LIST != null && PlayerConstants.SONGS_LIST.size() > 0) {
                            if (isDownloaded == 1) {
                                iv.setBackgroundResource(R.mipmap.downloadon);

                                if (trask.getTrackId().equalsIgnoreCase(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId())) {
                                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsDownloaded("true");
                                }
                                trask.setIsDownloaded("true");

                            } else {
                                iv.setBackgroundResource(R.mipmap.downloadplus);
                                if (trask.getTrackId().equalsIgnoreCase(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId())) {
                                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsDownloaded("false");
                                }
                                trask.setIsDownloaded("false");


                            }
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
        FacebookSdk.sdkInitialize(getApplicationContext());
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_to);
        context = getApplicationContext();
        Applicationmanager.setContext(AddToActivity.this);

        language = LanguageHelper.getCurrentLanguage(context);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("trask")) {

                trask = extras.getParcelable("trask");

                mPlaylistObjects = new ArrayList<>();
                SlideAlbumObject mPlayist = new SlideAlbumObject();

                mPlayist.setAlbumId(trask.getAlbumId());
                mPlayist.setSingerId(trask.getSingerId());

                mPlayist.setAlbumEnName(trask.getAlbumEnName());
                mPlayist.setAlbumArName(trask.getAlbumArName());
                mPlayist.setSingerArName(trask.getSingerArName());
                mPlayist.setSingerEnName(trask.getSingerEnName());
                mPlayist.setAlbumImgPath(trask.getTrackImage());
                mPlaylistObjects.add(mPlayist);
            }
        }
        InitilaizeUI();


        if (language.equalsIgnoreCase("ar")) {
            songname.setText(trask.getTrackArName());
            albumname.setText(trask.getAlbumArName());

        }
        if (language.equalsIgnoreCase("en")) {
            songname.setText(trask.getTrackEnName());
            albumname.setText(trask.getAlbumEnName());

        }

        isfavourite = trask.getIsFavourite();

        if (isfavourite.equalsIgnoreCase("true")) {
            mAraayunlike = getResources().getStringArray(R.array.player_listunlike);
            madaptor = new addListAdaptor(AddToActivity.this, R.layout.add_palyer_list_item, mAraayunlike);

        }

        if (isfavourite.equalsIgnoreCase("false")) {
            mAraay = getResources().getStringArray(R.array.player_list);
            madaptor = new addListAdaptor(AddToActivity.this, R.layout.add_palyer_list_item, mAraay);

        }
        mList.setAdapter(madaptor);
        setListViewHeightBasedOnChildren1(mList);

        mList.setOnItemClickListener((parent, view, position, id) -> {
            TextView tv = view.findViewById(R.id.add_player_text);
            iv = view.findViewById(R.id.add_player_icon);
            // TODO Auto-generated method stub
            switch (position) {
                case 0:


                    if (language.equalsIgnoreCase("ar")) {
                        if (tv.getText().toString().equalsIgnoreCase("اعجبنى")) {
                            tv.setText(getResources().getString(R.string.unlike).trim());
                            iv.setBackgroundResource(R.mipmap.unlike);
                            Add_Removefavourite(trask.getTrackId(), getApplicationContext(), "1");
                            favo = "true";

                        } else if (tv.getText().toString().equalsIgnoreCase("غير معجب")) {
                            tv.setText(getResources().getString(R.string.like).trim());
                            iv.setBackgroundResource(R.mipmap.like);

                            Add_Removefavourite(trask.getTrackId(), getApplicationContext(), "0");
                            favo = "false";

                        }

                    }
                    if (language.equalsIgnoreCase("en")) {
                        if (tv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.like))) {
                            tv.setText(getResources().getString(R.string.unlike));
                            iv.setBackgroundResource(R.mipmap.unlike);
                            Add_Removefavourite(trask.getTrackId(), getApplicationContext(), "1");
                            favo = "true";

                        } else {
                            if (tv.getText().toString().equalsIgnoreCase(getResources().getString(R.string.unlike))) {
                                tv.setText(getResources().getString(R.string.like));
                                iv.setBackgroundResource(R.mipmap.like);

                                Add_Removefavourite(trask.getTrackId(), getApplicationContext(), "0");
                                favo = "false";

                            }
                        }

                    }

                    break;
                case 1:
                    getPlayList();

                    break;
                case 2:
                    if (trask.getIsRBT().equalsIgnoreCase("true")) {
                        showAlert();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.calltonenotavailable),
                                Toast.LENGTH_SHORT).show();

                    }

                    break;
                case 3:

                    if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                        BitmapDrawable drawable = (BitmapDrawable) Image_player.getDrawable();
                        Bitmap bmp = drawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        if (trask.getHasLyrics().equalsIgnoreCase("true")) {
                            Intent playTrackk = new Intent(AddToActivity.this, LyricsActivity.class);
                            playTrackk.putExtra("lyricsfile", trask.getLyricFile());
                            if (language.equalsIgnoreCase("ar")) {
                                playTrackk.putExtra("songname", trask.getTrackArName());

                                playTrackk.putExtra("artistname", trask.getSingerArName());
                            }
                            if (language.equalsIgnoreCase("en")) {
                                playTrackk.putExtra("songname", trask.getTrackEnName());

                                playTrackk.putExtra("artistname", trask.getSingerEnName());
                            }


                            playTrackk.putExtra("picture", byteArray);

                            startActivity(playTrackk);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.nothavelyrics),
                                    Toast.LENGTH_SHORT).show();


                        }
                    } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {
                        DrawerActivity.showGracePopup(getApplicationContext());
                    } else {
                        Show_GhannelyExtra_Dialog();

                    }


                    break;
                case 4:
                    if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("1")) {
                        if (trask.getIsDownloaded().equalsIgnoreCase("true")) {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.alreadydownloaded),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            startwhellprogress();
                            lincricle.setVisibility(View.VISIBLE);
                            mCircleView.spin();
                            String str = ServerConfig.AudioUrl + trask.getTrackPath().replaceAll(" ", "%20");
                            SetupCiper2Download(trask.getTrackId(), str);
                            Log.e("audiourl", str);

                            //    downloadtset(trask.getTrackId(), str,trask.getTrackEnName());

                        }
                    } else if (!SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("") && SharedPrefHelper.getSharedString(context, Constants.UserStatusId).equalsIgnoreCase("3")) {
                        DrawerActivity.showGracePopup(getApplicationContext());
                    } else {
                        Show_GhannelyExtra_Dialog();

                    }



                    break;
                case 5:
                    sharetext();

                    break;


                case 6:
                    Intent playTrack1 = new Intent(AddToActivity.this, ArtistradioActivity.class);
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
                case 8:
                    if (position >= mPlaylistObjects.size()) {
                        position = position % mPlaylistObjects.size();
                    }
                    Intent albumIntent = new Intent(AddToActivity.this, AlbumTabsActivity.class);
                    albumIntent.putExtra("album", mPlaylistObjects.get(position));
                    startActivity(albumIntent);
                    finish();

                    break;
                case 7:
                    Intent playTrack = new Intent(AddToActivity.this, ArtistTabsActivity.class);
                    playTrack.putExtra("playlistId", Integer.valueOf(trask.getSingerId()));
                    startActivity(playTrack);
                    finish();

                    break;
                default:
                    break;
            }

        });
    }

    private void downloadtset(String trackId, String str, String title) {
        new DownloadAudioFile(getApplicationContext()).onDownloadStart(str, trackId, title, "", 0);

    }

    private void Show_GhannelyExtra_Dialog() {

        dialog = new Dialog(AddToActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

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
            if (extras.containsKey("FromPlayer")) {
                FromPlayer = getIntent().getExtras().getString("FromPlayer");

            }
        }
        Image_player = findViewById(R.id.Image_player);
        // Image_player.setBackgroundResource(Constants.ALBUM_IMAGE_PATH
        // +trask.getTrackImage());
        Log.e("HENANHENEJEUHEUH(HH", ServerConfig.ALBUM_IMAGE_PATH + trask.getTrackImage() + "herer");
        Picasso.with(AddToActivity.this).load((ServerConfig.ALBUM_IMAGE_PATH + trask.getTrackImage()))
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
        mCircleView.setTextColorAuto(false);
        mCircleView.setDirection(Direction.CW);
    }

    private void getPlayList() {
        startwhellprogress();
        Log.e("USER ID", SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "here");
        startwhellprogress();
        Log.d("Login Found", "heree");

        Api_Interface service = ServiceGenerator.createService();
        service.GetUserPlaylists(SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)).enqueue(new Callback<ArrayList<Playlistlst>>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<ArrayList<Playlistlst>> call, @NonNull Response<ArrayList<Playlistlst>> mPlaylistArray) {
                if (mPlaylistArray.isSuccessful()) {
                    mPlaylistList = new ArrayList<>();
                    if (mPlaylistList != null && mPlaylistArray.body().size() > 0) {
                        for (int i = 0; i < mPlaylistArray.body().size(); i++) {

                            Playlistlst mplaylist = new Playlistlst();
                            mplaylist.setPlaylistId(mPlaylistArray.body().get(i).getPlaylistId());
                            mplaylist.setPlaylistName(mPlaylistArray.body().get(i).getPlaylistName());
                            mplaylist.setPlaylistImgPath(mPlaylistArray.body().get(i).getPlaylistImgPath());
                            mPlaylistList.add(mplaylist);

                        }
                    }
                    playlistshowAlert();

                    progBar.setVisibility(View.GONE);

                } else {
                    ApiUtils.handelErrorCode(context, mPlaylistArray.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<ArrayList<Playlistlst>> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }


        });
    }

    private void showCustomAlert(int fav) {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, nullParent);
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

    private void showCustomAlertPlaylist_added() {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup nullParent = null;
        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, nullParent);
        ImageView image = toastRoot.findViewById(R.id.ivtoast);
        image.setImageResource(R.mipmap.donetick);
        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }


    private void Add_Removefavourite(String trackid2, final Context context2, final String favourite) {
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddFavouriteTrack + "?trackId=" + trackid2 + "&userId="
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

                boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                        getApplicationContext());
                if (isServiceRunning3 && PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(trask.getTrackId())) {

                    if (language.equalsIgnoreCase("ar")) {
                        PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                        if (PlayerAcreenActivity.SlideAdaptor != null) {
                            PlayerAcreenActivity.SlideAdaptor.notifyDataSetChanged();
                        }                            // gallery.invalidate();

                    }
                    if (language.equalsIgnoreCase("en")) {
                        PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);

                        if (PlayerAcreenActivity.SlideAdaptor != null) {
                            PlayerAcreenActivity.SlideAdaptor.notifyDataSetChanged();
                        }                            // gallery.invalidate();
                    }
                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsFavourite("true");
                    trask.setIsFavourite("true");

                    favo = "true";

                } else {
                    trask.setIsFavourite("true");

                    favo = "true";
                }
            } else {
                if (favourite.equalsIgnoreCase("0")) {
                    showCustomAlert(0);


                    boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                            getApplicationContext());
                    if (isServiceRunning3 && PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId().equalsIgnoreCase(trask.getTrackId())) {
                        if (language.equalsIgnoreCase("ar")) {
                            PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                            if (PlayerAcreenActivity.SlideAdaptor != null) {
                                PlayerAcreenActivity.SlideAdaptor.notifyDataSetChanged();
                            }
                            // gallery.invalidate();

                        }
                        if (language.equalsIgnoreCase("en")) {
                            PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setLikesCount(like);
                            if (PlayerAcreenActivity.SlideAdaptor != null) {
                                PlayerAcreenActivity.SlideAdaptor.notifyDataSetChanged();
                            }
                        }
                        PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).setIsFavourite("false");
                        trask.setIsFavourite("false");

                        favo = "false";

                    } else {
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
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you
                // won't get measured height at all. The listitem needs to be
                // drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @SuppressWarnings("ConstantConditions")
    private void showAlert() {
        dialog = new Dialog(AddToActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.customcalltune_popup);

        Button cancel = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialog.dismiss());

        Button confirm = dialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            Addtorbt(trask.getTrackId(), getApplicationContext());
            dialog.dismiss();

        });

        ImageView image = dialog.findViewById(R.id.ivimage);
        String imgpath = ServerConfig.Image_path
                + trask.getTrackImage();
        String final_imgpath = imgpath.replaceAll(" ", "%20");
        Picasso.with(context).load((final_imgpath)).error(R.mipmap.defualt_img)
                .placeholder(R.mipmap.defualt_img).into(image);

        dialog.show();
    }

    @SuppressWarnings("ConstantConditions")
    private void playlistshowAlert() {
        playlistdialog = new Dialog(AddToActivity.this);
        playlistdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        playlistdialog.setCanceledOnTouchOutside(false);
        playlistdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        playlistdialog.setContentView(R.layout.addplaylist);
        lvplaylist = playlistdialog.findViewById(R.id.player_list1);
        if (mPlaylistList != null) {
            if (mPlaylistList.size() > 0) {
                mAdaptor = new MyPlaylistAdaptor(AddToActivity.this, R.layout.myplaylist_item_list, mPlaylistList, null);
                lvplaylist.setAdapter(mAdaptor);
            }
        }
        lvplaylist.setOnItemClickListener((parent, view, position, id) -> {
            // mPlaylistList.get(position).getPlaylistId();
            Addtoplaylist(mPlaylistList.get(position).getPlaylistId());

        });
        playcancel = playlistdialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        playcancel.setOnClickListener(v -> playlistdialog.dismiss());

        playchange = playlistdialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        playchange.setOnClickListener(v -> cretaeplaylistshowAlert());
        if (!isFinishing()) {
            playlistdialog.show();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void cretaeplaylistshowAlert() {
        cretaeplaylistdialog = new Dialog(AddToActivity.this);
        cretaeplaylistdialog.setCanceledOnTouchOutside(false);
        cretaeplaylistdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cretaeplaylistdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        cretaeplaylistdialog.setContentView(R.layout.create_playlist);
        playlistname = cretaeplaylistdialog.findViewById(R.id.editText1);
        Button cancel = cretaeplaylistdialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> cretaeplaylistdialog.dismiss());

        Button confirm = cretaeplaylistdialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            if (!playlistname.getText().toString().equalsIgnoreCase("")) {
                Creareplaylist(playlistname.getText().toString());
                cretaeplaylistdialog.dismiss();
                playlistdialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), R.string.enterplaylist_name,
                        Toast.LENGTH_LONG).show();
            }


        });

        cretaeplaylistdialog.show();
    }

    private void Addtoplaylist(String usrPlaylstId) {
        // TODO Auto-generated method stub
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.AddUserPlaylistTrack + "?usrPlaylstId=" + usrPlaylstId + "&userId=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken) + "&trackId=" + trask.getTrackId();
        String fav_url = fav.replaceAll(" ", "%20");

        Log.d("amanttest", fav_url);


        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddUserPlaylistTrack(fav_url).enqueue(new Callback<Addrbtresponse>() {

                                                          @Override
                                                          public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                                                              if (mresult.isSuccessful()) {
                                                                  Addrbtresponse response = mresult.body();

                                                                  if (response != null) {
                                                                      if (response.getResultCode().equalsIgnoreCase("True")) {

                                                                          showCustomAlertPlaylist_added();
                                                                      }


                                                                      progBar.setVisibility(View.GONE);

                                                                      playlistdialog.dismiss();
                                                                  }
                                                              } else {
                                                                  ApiUtils.handelErrorCode(context, mresult.code());
                                                                  System.out.println("onFailure");
                                                                  progBar.setVisibility(View.GONE);

                                                                  playlistdialog.dismiss();
                                                              }
                                                          }

                                                          @Override
                                                          public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                                                              progBar.setVisibility(View.GONE);

                                                          }
                                                      }
        );

    }

    private void Creareplaylist(String playlistname) {
        Log.d("Login Found", "heree");
        startwhellprogress();

        ArrayList<String> strArray = new ArrayList<>(Collections.singletonList(trask.getTrackId()));


        String fav_url = ServerConfig.SERVER_URl + ServerConfig.AddUserPlaylistWithTracks + "?userId=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken) + "&usrPlaylstName=" + playlistname
                + "&trackIDs=" + strArray;
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.AddUserPlaylistWithTracks(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    progBar.setVisibility(View.GONE);
                    showCustomAlertPlaylist_added();

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

    private void Addtorbt(String string, Context context) {
        startwhellprogress();

        String fav = ServerConfig.SERVER_URl + ServerConfig.ADDRBTREQUEST + "?trackId=" + string + "&userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.ADDRBTREQUEST(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {


                if (mresult.isSuccessful()) {
                    Addrbtresponse response = mresult.body();
                    Log.i("response", response + "");
                    progBar.setVisibility(View.GONE);

                    //convert json string to object

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


            if (Build.VERSION.SDK_INT >= M) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "share_image_" + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                bmpUri = Uri.fromFile(file);

            } else {
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "share_image_" + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                bmpUri = Uri.fromFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void sharetext() {


        if (language.equalsIgnoreCase("ar")) {
            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId()
                        .equalsIgnoreCase(trask.getTrackId())) {

                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackArName();
                        String artistname = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)
                                .getSingerArName();

                        ShareIntent(trackname, artistname,
                                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), 1);
                    }
                } else {
                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = trask.getTrackArName();
                        String artistname = trask.getSingerArName();

                        ShareIntent(trackname, artistname, trask.getTrackImage(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getTrackId(), 0);
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

                    ShareIntent(trask.getTrackArName(), trask.getSingerArName(), trask.getTrackImage(),
                            SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getTrackId(), 0);
                }
            }
        }
        if (language.equalsIgnoreCase("en")) {

            boolean isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                    getApplicationContext());
            if (isServiceRunning3) {
                if (PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId()
                        .equalsIgnoreCase(trask.getTrackId())) {

                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackEnName();
                        String artistname = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER)
                                .getSingerEnName();

                        ShareIntent(trackname, artistname,
                                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackImage(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                                PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), 1);
                    }
                } else {
                    Uri bmpUri = getLocalBitmapUri(Image_player);
                    if (bmpUri != null) {
                        String trackname = trask.getTrackEnName();
                        String artistname = trask.getSingerEnName();

                        ShareIntent(trackname, artistname, trask.getTrackImage(),
                                SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getTrackId(), 0);
                    }
                }
            }


            if (!isServiceRunning3
                    ) {
                Uri bmpUri = getLocalBitmapUri(Image_player);
                if (bmpUri != null) {

                    ShareIntent(trask.getTrackEnName(), trask.getSingerEnName(), trask.getTrackImage(),
                            SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID), trask.getTrackId(), 0);
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
                + desc + "&l=1" + "&tk=" + trackid + "&u=" + userid + "&ls=" + i + "&v=0";
        String fav_url = sharerUrl.replaceAll(" ", "%20");
        //shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        //shareIntent.setType("image/*");

        if (i == 1) {
            if (language.equalsIgnoreCase("ar")) {
                String total = " أنا بستمع إلى" + " " + title + "  " + "ل" + " " + desc + " "
                        + "علي تطبيق غنيلي , اضغط هنا للتحميل " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);
            } else {
                String total = " I am listening to " + " " + title + "  " + "by" + " " + desc + " "
                        + "on ghaneely app , download now from " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);

            }
        }

        if (i == 0) {
            if (language.equalsIgnoreCase("ar")) {
                String total = "استمع الي" + " " + title + "  " + "ل" + " " + desc + " "
                        + "علي تطبيق غنيلي , اضغط هنا للتحميل " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);
            } else {
                String total = "Listen to " + " " + title + "  " + "by" + " " + desc + " "
                        + "on ghaneely app , download now from " + " ";
                shareIntent.putExtra(Intent.EXTRA_TEXT, total + fav_url);

            }
        }
        shareIntent.setType("text/plain");
        IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(AddToActivity.this, shareIntent, "Share with...", activityInfo -> {
            bottomSheetLayout.dismissSheet();
            if (activityInfo.componentName.getPackageName().startsWith("com.facebook.katana")) {
                // share facebook
                // http://ghaneely.binarywaves.com/GhaneelyMobile/download.aspx?title=test&albumImg=819.jpg&desc=desc
                String language = SharedPrefHelper.getSharedString(getApplicationContext(), "langid");
                String sharerUrl1;
                if (language.equalsIgnoreCase("ar")) {
                    sharerUrl1 = "https://bw.ghaneely.com/share/d.aspx?" + "t=" + title + "&i=" + image + "&s="
                            + desc + "&l=2" + "&tk=" + trackid + "&u=" + userid + "&ls=" + i + "&v=0";
                } else {
                    sharerUrl1 = "https://bw.ghaneely.com/share/d.aspx?" + "t=" + title + "&i=" + image + "&s="
                            + desc + "&l=1" + "&tk=" + trackid + "&u=" + userid + "&ls=" + i + "&v=0";

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
        //	intentPickerSheet.setMixins(Collections.singletonList(customInfo))

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

            Toast.makeText(AddToActivity.this, "No Space", Toast.LENGTH_LONG).show();
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
            DeletefromSdcard_sql(trask.getTrackId(), Encryption_DecryptionAudio.mEncryptedFile);
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
                new DownloadAndEncryptFileTask(url, Encryption_DecryptionAudio.mEncryptedFile, Encryption_DecryptionAudio.mCipher, 0).execute();
            } else {
                lincricle.setVisibility(View.GONE);
                Toast.makeText(AddToActivity.this, getResources().getString(R.string.generalerror), Toast.LENGTH_LONG).show();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}