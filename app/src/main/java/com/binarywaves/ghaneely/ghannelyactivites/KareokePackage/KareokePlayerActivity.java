package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.Base64;
import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghaneelycashing.FileCache;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KareokeView.NonFocusableNestedScrollView;
import com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KareokeView.SwipeRefreshLayout;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.ChangepassResponse;
import com.binarywaves.ghaneely.ghannelyservice.TrackLisinterService;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.binarywaves.ghaneely.ghannelyutils.Utils;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.RxAmplitude;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.joanfuentes.hintcase.HintCase;
import com.joanfuentes.hintcaseassets.contentholderanimators.FadeInContentHolderAnimator;
import com.joanfuentes.hintcaseassets.hintcontentholders.SimpleHintContentHolder;
import com.joanfuentes.hintcaseassets.shapeanimators.RevealRectangularShapeAnimator;
import com.joanfuentes.hintcaseassets.shapeanimators.UnrevealRectangularShapeAnimator;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Amany on 22-Aug-17.
 */

public class KareokePlayerActivity extends RxAppCompatActivity implements Player.EventListener, AudioRecorder.OnErrorListener {
    private final static String TAG = "KareokePlayerActivity";
    private static final int MIN_AUDIO_LENGTH_SECONDS = 1;
    static final int BUFFER_SIZE = 2048;
    private static Handler seekHandler = new Handler();
    private static TextView textBufferDuration;
    private static TextView textDuration;
    private static int mBufferPosition;
    private static SeekBar seek_bar;
    private static RelativeLayout progBar;
    private static ExoPlayer mp;
    private static ProgressWheel pb1;
    private static Context context;
    private static Context cx;
    private static int percent;
    private static HintCase hint;
    String result;
    private LinearLayout lyrics_content;
    TextView tvlyrics;
   // private LrcView rowTextView;
  //  private ArrayList<LrcView> mTextViewList = new ArrayList<>();
    private RelativeLayout frame;
    private Bitmap bmp;
    private ImageView ivsinger;
    private ImageView close_player;
    ImageView ivsave;
    private Button ivrecord;
    private Button ivrecord_pause;
    private DataSource.Factory dataSourceFactory;
    private int mPalyTimerDuration = 100;
    private Timer mTimer;
    private TimerTask mTask;
    private NonFocusableNestedScrollView mScrollView;
    private SwipeRefreshLayout mRefreshLayout;
    private Thread mLrcThread;
    private String singername, trackname, trackid, trackpath, trackLyricFile;
    private Drawable background;
    private boolean mpaused;
    private boolean firsttype = true;
    private boolean blockSeekbar = false;
    private Dialog dialog;
    private boolean startRecording = true;
    private AudioRecorder mAudioRecorder;
    private File mAudioFile;
    private Disposable mRecordDisposable;
    private RxPermissions mPermissions;
    private Queue<File> mAudioFiles = new LinkedList<>();
    private Dialog dialogrecord;

    private boolean isReady;
    private Runnable lrcUpdater = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {

            if (isReady && mp != null) {

                runOnUiThread(() -> {
                    mpaused = true;
                    progBar.setVisibility(View.GONE);
                    LyricsUpdation();
                    updateSeekProgress();
                    textDuration.setText(" " + UtilFunctions.getDuration(mp.getDuration()));
                    // Displaying time completed playing
                    textBufferDuration.setText(" " + UtilFunctions.getDuration(mp.getCurrentPosition()));

                });

            }

        }
    };

    @SuppressWarnings("deprecation")
    private static void setBg(RelativeLayout layout, BitmapDrawable TileMe) {
        layout.setBackground(TileMe);
    }

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    private void showHint(final View view) {


        SimpleHintContentHolder blockInfo = new SimpleHintContentHolder.Builder(view.getContext())
                .setContentText(cx.getResources().getString(R.string.karaokehint)).setContentStyle(R.style.content_darkkar)
                .setGravity(Gravity.CENTER)
                .setMarginByResourcesId(R.dimen.activity_vertical_margin,
                        R.dimen.activity_horizontal_margin,
                        R.dimen.activity_vertical_margin,
                        R.dimen.activity_horizontal_margin)
                .build();
        new HintCase(view.getRootView())
                .setTarget(findViewById(R.id.ivrecord), HintCase.TARGET_IS_CLICKABLE)
                .setShapeAnimators(new RevealRectangularShapeAnimator(),
                        new UnrevealRectangularShapeAnimator())
                .setHintBlock(blockInfo, new FadeInContentHolderAnimator())
                .show();
    }

/////


    private void LyricsUpdation() {

        if (mp != null) {
         //   rowTextView.changeCurrent(mp.getCurrentPosition());
        }

        seekHandler.postDelayed(lrcUpdater, 100);

    }

    private void updateSeekProgress() {
        if (seek_bar != null) {
            if (mp != null && mp.getDuration() > 0) {

                int videoProgress = (int) (mp.getCurrentPosition() * 100 / mp.getDuration());

                seek_bar.setProgress(videoProgress);

            }
        }
    }

    private void setBufferPosition(int progress) {
        mBufferPosition = progress;
    }

    private void CreateDynamic_TextView(String result) {
       tvlyrics.setText(result);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_kareokeplayer);

        Applicationmanager.setContext(KareokePlayerActivity.this);
        mPermissions = new RxPermissions(this);
        Utils.changeStatusBarColor(this,this);

        mAudioRecorder = AudioRecorder.getInstance();
        mAudioRecorder.setOnErrorListener(this);
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);


        frame = findViewById(R.id.frame);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("picture")) {
                byte[] b = extras.getByteArray("picture");
                if (b != null) {
                    bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                    SetMainBackgroundScreen(bmp);

                }

            }
        }

        if (extras != null) {
            if (extras.containsKey("trackname")) {
                singername = extras.getString("singername");
                trackname = extras.getString("trackname");
                trackid = extras.getString("trackid");
                trackpath = extras.getString("trackpath");
                trackLyricFile = extras.getString("trackLyricFile");

            }
        }
        GetStringFromUrl(ServerConfig.Lyrics_Url + trackLyricFile);

    }

    private void StopService() {
        // TODO Auto-generated method stub

        TrackLisinterService tracklisiten = new TrackLisinterService(getApplicationContext(), getApplication());
        tracklisiten.StopService();

    }

    @SuppressLint("CutPasteId")
    private void InitUi(String result) {
        ivsinger = findViewById(R.id.Image_player);
        ivsinger.setImageBitmap(bmp);
        TextView tvsingername = findViewById(R.id.tvsingername);
        TextView tvtrackname = findViewById(R.id.tvalbumname);
        tvsingername.setText(singername);
        tvtrackname.setText(trackname);
        StopService();

        close_player = findViewById(R.id.close_player);
        close_player.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            destroycomponent();

        });
        seek_bar = findViewById(R.id.seek_bar);
        textBufferDuration = findViewById(R.id.textBufferDuration);
        textDuration = findViewById(R.id.textDuration);
        ivrecord = findViewById(R.id.ivrecord);
        ivrecord_pause = findViewById(R.id.ivrecord_pause);
        context = getApplicationContext();
        cx = KareokePlayerActivity.this;
        hint = new HintCase(findViewById(R.id.ivrecord).getRootView());

        if (!hint.getView().isShown()) {
            View actionSearchView = findViewById(R.id.ivrecord);
            showHint(actionSearchView);
        }
        ivrecord.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            startwhellprogress();
            ivrecord.setVisibility(View.GONE);
            ivrecord_pause.setVisibility(View.VISIBLE);
            if (firsttype) {
                beginLrcPlay();
                start();
                firsttype = false;

            }
            if (mp != null && !mp.getPlayWhenReady()) {
                mp.setPlayWhenReady(true);

            }


            if (lrcUpdater != null) {
               // LyricsUpdation();

            }
        });


        ivrecord_pause.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            ivrecord_pause.setVisibility(View.GONE);
            ivrecord.setVisibility(View.VISIBLE);

            if (mp != null && mp.getPlayWhenReady()) {
                mp.setPlayWhenReady(false);

            }
            if (lrcUpdater != null) {
                stopLrcPlay();
            }

            showSaveAlert();
        });

        lyrics_content = findViewById(R.id.lyrics_content);
        tvlyrics=findViewById(R.id.tvlyrics);

        if (mp == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create a default LoadControl
            // LoadControl loadControl = new DefaultLoadControl();
            mp = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
            mp.removeListener(this);
        }

        CreateDynamic_TextView(result);


    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            setBufferPosition((int) (percent * mp.getDuration() / 100));


        }
        if (playbackState == Player.STATE_ENDED) {


            if (mp != null) {
               // ivrecord_pause.performClick();
                stopLrcPlay();


            }
        }

        if (playbackState == Player.STATE_READY) {
            if (mp.getPlayWhenReady()) {
                AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float percent = 0.7f;
                int seventyVolume = (int) (maxVolume*percent);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, 0);
                mp.setPlayWhenReady(true);
                isReady = true;

                LyricsUpdation();
            }
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }


    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp = null;
        }


        if (mRecordDisposable != null && !mRecordDisposable.isDisposed()) {
            mRecordDisposable.dispose();
        }


    }

    private void beginLrcPlay() {
        try {

            mp.removeListener(this);


            mp.addListener(this);


            String str;
            str = ServerConfig.KARAOKE_AUDIO_PATH + trackpath.replaceAll(" ", "%20");
            try {

                dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayer"));

                MediaSource audio = new ExtractorMediaSource(Uri.parse(str), dataSourceFactory, new DefaultExtractorsFactory(), null, null);

                mp.prepare(audio);
                AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 50, 0);

                mp.setPlayWhenReady(true);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    private void stopLrcPlay() {
        if (lrcUpdater != null) {
            seekHandler.removeCallbacks(lrcUpdater);
        }
    }

    private void SetMainBackgroundScreen(Bitmap bmp) {

        GaussianBlur gaussian = new GaussianBlur(getApplicationContext());
        gaussian.setMaxImageSize(60);
        gaussian.setRadius(25); // max

        Bitmap output = gaussian.render(bmp, true);
        final BitmapDrawable ob = new BitmapDrawable(getApplicationContext().getResources(), output);
        setBg(frame, ob);


    }

    @SuppressWarnings("ConstantConditions")
    private void showSaveAlert() {
        dialog = new Dialog(KareokePlayerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.savekaraoke_popup);

        Button cancel = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> {dialog.dismiss();
        });

        Button confirm = dialog.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            dialog=null;
            showkaraokerecordpopup();


        });

        dialog.show();
    }

    private void start() {
        boolean isPermissionsGranted
                = mPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && mPermissions.isGranted(Manifest.permission.RECORD_AUDIO);
        if (!isPermissionsGranted) {
            mPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        // not record first time to request permission
                        if (granted) {
                            Toast.makeText(getApplicationContext(), "Permission granted",
                                    Toast.LENGTH_SHORT).show();
                            recordAfterPermissionGranted();

                        } else {
                            Toast.makeText(getApplicationContext(), "Permission not granted",
                                    Toast.LENGTH_SHORT).show();
                            destroycomponent();
                        }
                    }, Throwable::printStackTrace);
        } else {
            recordAfterPermissionGranted();
        }
    }

    private void recordAfterPermissionGranted() {

        mRecordDisposable = Observable
                .fromCallable(() -> {
                    mAudioFile = new File(
                            Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + File.separator + System.nanoTime() + ".file.mp3");
                    Log.d(TAG, "to prepare record");


                        return mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
                            MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.HE_AAC,
                                16000 , 48000 , mAudioFile);
                }
                )

                .doOnComplete(() -> {
                    Log.d(TAG, "audio_record_ready play finished");
                    mAudioRecorder.startRecord();
                })
                .doOnNext(b -> Log.d(TAG, "startRecord success"))
                .flatMap(o -> RxAmplitude.from(mAudioRecorder))
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(level -> {
                    int progress = mAudioRecorder.progress();
                    Log.d(TAG, "amplitude: " + level + ", progress: " + progress);

                }, Throwable::printStackTrace);
    }

    private void release2Send(String Karaokename) {
        startwhellprogress();

        if (mRecordDisposable != null && !mRecordDisposable.isDisposed()) {
            mRecordDisposable.dispose();
            mRecordDisposable = null;
        }
        Observable
                .fromCallable(() -> {
                    int seconds = mAudioRecorder.stopRecord();
                    Log.d(TAG, "stopRecord: " + seconds);
                    if (seconds >= MIN_AUDIO_LENGTH_SECONDS) {
                        mAudioFiles.offer(mAudioFile);
                        return true;
                    }
                    progBar.setVisibility(View.GONE);
                    destroycomponent();

                    return false;
                })
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(added -> {
                    if (added) {
                        Log.i("audio file ", mAudioFile.getName() + " added");

                        ManageRecordfile(mAudioFile, Karaokename);
                    }
                }, Throwable::printStackTrace);
    }

    private void ManageRecordfile(File file, String karaokename) throws IOException {

        String EncodedString = Base64.encodeBytes(read(file));
        SetKaraokeTrack(EncodedString, karaokename);


    }

    private byte[] read(File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException(
                        "EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return buffer;
    }

    private void audioPlayer(File file) {
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(getApplicationContext(), Uri.fromFile(file));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (!mAudioFiles.isEmpty()) {
            File audioFile = mAudioFiles.poll();
            audioPlayer(audioFile);
        }
    }

    @WorkerThread
    @Override
    public void onError(int error) {
        runOnUiThread(
                () -> Toast.makeText(KareokePlayerActivity.this, "Error code: " + error, Toast.LENGTH_SHORT)
                        .show());
    }

    @SuppressWarnings("ConstantConditions")
    private void showkaraokerecordpopup() {
        dialogrecord = new Dialog(KareokePlayerActivity.this);
        dialogrecord.setCanceledOnTouchOutside(false);
        dialogrecord.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrecord.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogrecord.setContentView(R.layout.createkaraoke_record_popup);
        EditText recordname = dialogrecord.findViewById(R.id.editText1);
        Button cancel = dialogrecord.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(v -> dialogrecord.dismiss());

        Button confirm = dialogrecord.findViewById(R.id.change);
        // if button is clicked, close the custom dialog
        confirm.setOnClickListener(v -> {

            if (!recordname.getText().toString().equalsIgnoreCase("")) {
                dialogrecord.dismiss();
                release2Send(recordname.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), R.string.enterplaylist_name,
                        Toast.LENGTH_LONG).show();
            }


        });

        dialogrecord.show();
    }

    private void SetKaraokeTrack(String encodedString, String recordname) {
        // Set Cancelable as False


        if (Constants.isNetworkOnline(this)) {
            Api_Interface service = ServiceGenerator.createService();
            service.SetKaraokeTrack(recordname, encodedString, trackid,
                    SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID),
                    SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)).enqueue(new Callback<ChangepassResponse>() {

                @Override
                public void onResponse(@NonNull Call<ChangepassResponse> call, @NonNull Response<ChangepassResponse> mresult) {
                    if (mresult.isSuccessful()) {
                        ChangepassResponse response = mresult.body();
                        if (response != null) {
                            Log.i("response", response.getResultCode() + "bb");

                            if (response.getResultCode().equalsIgnoreCase("true")) {
                                dialogrecord.dismiss();
                                Toast.makeText(KareokePlayerActivity.this, getResources().getString(R.string.saveRecordsuccess),
                                        Toast.LENGTH_LONG).show();
                                destroycomponent();
                                if (mAudioFile.exists()) {
                                    Boolean delete = FileCache.delete(mAudioFile);
                                    if (delete) {
                                        Log.d(TAG, "audio_record_deleted");

                                    }
                                }

                            } else {

                                dialogrecord.dismiss();
                                Toast.makeText(KareokePlayerActivity.this, getResources().getString(R.string.saveRecordError),
                                        Toast.LENGTH_LONG).show();
                                destroycomponent();
                                if (mAudioFile.exists()) {
                                    Boolean delete = FileCache.delete(mAudioFile);
                                    if (delete) {
                                        Log.d(TAG, "audio_record_deleted");

                                    }
                                }
                            }
                        }
                    } else {
                        ApiUtils.handelErrorCode(getApplicationContext(), mresult.code());
                        System.out.println("onFailure");
                        dialogrecord.dismiss();

                    }
                }


                @Override
                public void onFailure(@NonNull Call<ChangepassResponse> call, @NonNull Throwable t) {
                    dialogrecord.dismiss();

                }
            });
        } else {

            Log.d("no internet found", "FOUND");
            Toast.makeText(KareokePlayerActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();


        }
    }

    private void GetStringFromUrl(String url) {
        startwhellprogress();
        Api_Interface service = ServiceGenerator.createService();
        service.Getlyricsfile(url).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> mresult) {
                if (mresult.isSuccessful()) {

                    ResponseBody responseBody = mresult.body();
                    InputStream is = null;
                    if (responseBody != null) {
                        is = responseBody.byteStream();
                    }
                    // ...
                    StringBuilder total = new StringBuilder();

                    BufferedReader r;
                    try {
                        r = new BufferedReader(
                                new InputStreamReader(
                                        is,
                                        "UTF-8"
                                )
                        );

                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line).append("\n");
                        }
                        String result = String.valueOf(total);
                        Log.i("Get URL", "Downloaded string: " + result);
                      //  LrcView.setSourceLrc(result);

                        // close progresses dialog
                        progBar.setVisibility(View.GONE);
                        InitUi(result);


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    ApiUtils.handelErrorCode(getApplicationContext(), mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroycomponent();
        finish();
    }

    private void destroycomponent() {
        if (mp != null) {
            mp.stop();
            mp = null;
        }


        if (mRecordDisposable != null && !mRecordDisposable.isDisposed()) {
            mRecordDisposable.dispose();
        }
        if (lrcUpdater != null) {
            seekHandler.removeCallbacks(lrcUpdater);
            lrcUpdater = null;
        }
        DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(7, null, null), 7, 0);
        finish();
    }
}






