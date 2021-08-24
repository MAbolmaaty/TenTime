package com.binarywaves.ghaneely.ghannelyactivites;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 13-Jun-17.
 */

public class LyricsActivity extends ActivityMainRunnuing {
    private static ProgressWheel pb1;
    private RelativeLayout progBar;
    private TextView tvlyrics;
    private TextView tvsongname;
    private TextView tvartistname;
    private RelativeLayout rvcontainer;
    private String lyricsfile;
    private Bitmap bitmap;
    private String songname;
    private String artistname;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private static void setBg(RelativeLayout layout, BitmapDrawable TileMe) {
        layout.setBackground(TileMe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lyrics_screen);

        // String language = LanguageHelper.getCurrentLanguage(getApplicationContext());
        Applicationmanager.setContext(LyricsActivity.this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("lyricsfile")) {
                // extract the extra-data in the Notification
                lyricsfile = extras.getString("lyricsfile");
                songname = extras.getString("songname");
                artistname = extras.getString("artistname");

                byte[] byteArray = extras.getByteArray("picture");

                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray != null ? byteArray.length : 0);
            }

        }
        Init_UI();
        if (lyricsfile != null && !lyricsfile.equalsIgnoreCase("")) {
            GetStringFromUrl(ServerConfig.Lyrics_Url + lyricsfile);

        }
    }

    private void Init_UI() {
        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        tvlyrics = findViewById(R.id.tvlyrics);
        tvlyrics.setMovementMethod(new ScrollingMovementMethod());
        tvsongname = findViewById(R.id.tvsongname);
        tvsongname.setText(songname);
        tvartistname = findViewById(R.id.tvartistname);
        tvartistname.setText(artistname);
        ImageView close = findViewById(R.id.close_player);
        close.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            finish();
        });
        rvcontainer = findViewById(R.id.rvcontainer);
        GaussianBlur gaussian = new GaussianBlur(getApplicationContext());
        gaussian.setMaxImageSize(60);
        gaussian.setRadius(25); // max

        Bitmap output = gaussian.render(bitmap, true);
        final BitmapDrawable ob = new BitmapDrawable(getResources(), output);
        setBg(rvcontainer, ob);

    }

    private void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    private void GetStringFromUrl(String url) {
        startwhellprogress();
        Api_Interface service = ServiceGenerator.createService();
        service.Getlyricsfile(url).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> mresult) {
                if (mresult.isSuccessful()) {

                    ResponseBody responseBody = mresult.body();
                    InputStream is = responseBody != null ? responseBody.byteStream() : null;
                    // ...
                    StringBuilder total = new StringBuilder();

                    BufferedReader r = null;
                    try {
                        if (is != null) {
                            r = new BufferedReader(
                                    new InputStreamReader(
                                            is,
                                            "UTF-8"
                                    )
                            );
                        }

                        String line;
                        if (r != null) {
                            while ((line = r.readLine()) != null) {
                                total.append(line).append("\n");
                            }
                        }
                        String result = String.valueOf(total);
                        Log.i("Get URL", "Downloaded string: " + result);
                        if (result == null) {
                            tvlyrics.setText(R.string.lyricserror);
                        } else {
                            tvlyrics.setText(result);
                        }

                        // close progresses dialog
                        progBar.setVisibility(View.GONE);


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

}