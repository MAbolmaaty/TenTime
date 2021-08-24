package com.Ghannely_Encrypt_Decrypt_Tracks;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannelyactivites.AddToActivity;
import com.binarywaves.ghaneely.ghannelyactivites.AddtoVideoActivity;
import com.binarywaves.ghaneely.ghannelyactivites.DownloadActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

/**
 * Created by Amany on 24-Sep-17.
 */

public class DownloadAndEncryptFileTask extends AsyncTask<Void, String, Void> {

    private String mUrl;
    private File mFile;
    private Cipher mCipher;
    private int activity;
    private RxPermissions mPermissions;
    private Boolean mper_granted = false;
    HttpURLConnection connection;

    public DownloadAndEncryptFileTask(String url, File file, Cipher cipher, int i) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("You need to supply a url to a clear MP4 file to downloadplus and encrypt, or modify the code to use a local encrypted mp4");
        }
        mPermissions = new RxPermissions(Applicationmanager.getCurrentActivity());
        if (Build.VERSION.SDK_INT >= 23) {
            check_Permission();
        } else {
            mper_granted = true;

          //  write_File();
        }
        mUrl = url;
        mFile = file;
        mCipher = cipher;
        activity = i;
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024 * 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

    private void downloadAndEncrypt() throws Exception {

        URL url = new URL(mUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        // this will be useful so that you can show a tipical 0-100% progress bar
        write_File();



    }

    private void write_File() throws Exception {
        if (mper_granted) {
            int lenghtOfFile = connection.getContentLength();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("server error: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }
            InputStream inputStream = connection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(mFile);


            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, mCipher);


            byte[] block = new byte[1024 * 1024];

            int bytesRead;
            long total = 0;
            while ((bytesRead = inputStream.read(block)) != -1) {
                total += bytesRead;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                Log.d(getClass().getCanonicalName(), "reading from http...");

                cipherOutputStream.write(block, 0, bytesRead);


            }
            inputStream.close();
            cipherOutputStream.close();
            connection.disconnect();

        } else {
            check_Permission();
        }

    }

    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        if (activity == 0) {
            //AddToActivity.mCircleView.setText(""+Integer.parseInt(progress[0]));
            AddToActivity.mCircleView.setValue(Integer.parseInt(progress[0]));

        }
        if (activity == 1) {
            //AddToActivity.mCircleView.setText(""+Integer.parseInt(progress[0]));
            PlayerAcreenActivity.mCircleView.setValue(Integer.parseInt(progress[0]));

        }
        if (activity == 2) {
            DownloadActivity.startwhellprogress();
            DownloadActivity.mCircleView.setValue(Integer.parseInt(progress[0]));

        }

        if (activity == 3) {
            //AddToActivity.mCircleView.setText(""+Integer.parseInt(progress[0]));
            AddtoVideoActivity.mCircleView.setValue(Integer.parseInt(progress[0]));

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (activity == 0) {

            AddToActivity.lincricle.setVisibility(View.VISIBLE);
            AddToActivity.mCircleView.spin();

        }
        if (activity == 1) {
            PlayerAcreenActivity.lincricle.setVisibility(View.VISIBLE);
            PlayerAcreenActivity.mCircleView.spin();

        }
        if (activity == 2) {
            DownloadActivity.lincricle.setVisibility(View.VISIBLE);
            DownloadActivity.mCircleView.spin();

        }
        if (activity == 3) {
            AddtoVideoActivity.lincricle.setVisibility(View.VISIBLE);
            AddtoVideoActivity.mCircleView.spin();

        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            downloadAndEncrypt();
        } catch (Exception e) {
            Log.d("downloadAndEncrypterror", e.getMessage()+"errordownload");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(getClass().getCanonicalName(), "done");
        if (activity == 0) {
            AddToActivity.ADD_DOWNLOAD_2_DATABASE();
            AddToActivity.lincricle.setVisibility(View.GONE);
        }

        if (activity == 1) {
            PlayerAcreenActivity.ADD_DOWNLOAD_2_DATABASE();
            PlayerAcreenActivity.lincricle.setVisibility(View.GONE);
        }
        if (activity == 2) {
            DownloadActivity.lincricle.setVisibility(View.GONE);
            DownloadActivity.mCircleView.stopSpinning();
            DownloadActivity.progBar.setVisibility(View.GONE);

        }

        if (activity == 3) {
            AddtoVideoActivity.ADD_DOWNLOAD_2_DATABASE();

            AddtoVideoActivity.lincricle.setVisibility(View.GONE);
        }
    }

    private void check_Permission() {
        boolean isPermissionsGranted
                = mPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && mPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!isPermissionsGranted) {
            mPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        // not record first time to request permission
                        if (granted) {
                            //write_File();
                            mper_granted = true;
                        } else {

                            mper_granted = false;
                        }
                    }, Throwable::printStackTrace);
        } else {
            try {
                //write_File();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mper_granted = true;
        }
    }
}
