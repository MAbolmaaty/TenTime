package com.Ghannely_Encrypt_Decrypt_Tracks;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.widget.Toast;

import com.binarywaves.ghaneely.ghaneelycashing.Utils;
import com.binarywaves.ghaneely.ghannelyactivites.AddToActivity;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amany on 19-Mar-17.
 */

public class DownloadAudioFile implements DownloadListener {
    private static Context mContext;
    private static String ENCRYPTED_FILE_NAME;
    private long activity;
    private DownloadManager mDownloadManager;
    private long mDownloadedFileID;
    private DownloadManager.Request mRequest;

    public DownloadAudioFile(Context context) {
        mContext = context;
        mDownloadManager = (DownloadManager) mContext
                .getSystemService(Context.DOWNLOAD_SERVICE);

    }

    public static Double Checkexternalstorage() {
        Double finalspace = Utils.getAvailableExternalMemorySize();
        Log.i("value", finalspace + "vvv" + finalspace);
        return finalspace;
    }

    public static void deleteaudiofile() {
        File directory;
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            //  Environment.getExternalStorageDirectory() + "/"+Environment.DIRECTORY_DOWNLOADS+ "/
            directory = new File(mContext.getExternalFilesDir(null)
                    + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + ENCRYPTED_FILE_NAME);
            if (directory.exists()) {
                mContext.deleteFile(directory.getName());
            }
                /*
                 * this checks to see if there are any previous test photo files
                 * if there are any photos, they are deleted for the sake of
                 * memory
                 */

            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card

            directory = new File(
                    mContext.getExternalFilesDir(null)
                            + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + ENCRYPTED_FILE_NAME);
            if (directory.exists()) {
                mContext.deleteFile(directory.getName());
            }
            // if no directory exists, create new directory to store test
            // results
            if (!directory.exists()) {
                directory.mkdir();
            }
        }// end of SD card checking
    }

    private static byte[] readFile(File file, Context mContext) {
        byte[] contents = null;


        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(
                    new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    @Override
    public void onDownloadStart(final String url, String contentDisposition, String
            title, final String mimetype, long contentLength) {
        ENCRYPTED_FILE_NAME = contentDisposition;


        // Function is called once downloadplus completes.
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                queryStatus();

            }
        };
        // Registers function to listen to the completion of the downloadplus.
        mContext.registerReceiver(onComplete, new
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mRequest = new DownloadManager.Request(Uri.parse(url));
        mRequest.allowScanningByMediaScanner();
        mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Limits the downloadplus to only over WiFi. Optional.
        //    mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // Makes downloadplus visible in notifications while downloading, but disappears after downloadplus completes. Optional.
        //  mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        mRequest.setMimeType(mimetype);

        // If necessary for a security check. I needed it, but I don't think it's mandatory.
        String cookie = CookieManager.getInstance().getCookie(url);
        mRequest.addRequestHeader("Cookie", cookie);

        // Grabs the file name from the Content-Disposition
        ENCRYPTED_FILE_NAME = contentDisposition;
        Pattern regex = Pattern.compile("(?<=filename=\").*?(?=\")");
        Matcher regexMatcher = regex.matcher(contentDisposition);
        if (regexMatcher.find()) {
            ENCRYPTED_FILE_NAME = regexMatcher.group();
        }
        activity = contentLength;
        // Sets the file path to save to, including the file name. Make sure to have the WRITE_EXTERNAL_STORAGE permission!!
        mRequest.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, ENCRYPTED_FILE_NAME);
        // Sets the title of the notification and how it appears to the user in the saved directory.
        mRequest.setTitle(title);
        mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        // Adds the request to the DownloadManager queue to be executed at the next available opportunity.
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        mDownloadedFileID = mDownloadManager.enqueue(mRequest);
    }

    private String statusMessage(Cursor c) {
        String msg = "???";

        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                if (Checkexternalstorage() >= 0) {
                    // Not sure if the / is on the path or not
                    File directory = new File(
                            mContext.getExternalFilesDir(null)
                                    + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + ENCRYPTED_FILE_NAME);
                    Encryption_DecryptionAudio.saveFile(mContext, readFile(directory, mContext), ENCRYPTED_FILE_NAME);
                }
                if (activity == 0) {
                    AddToActivity.ADD_DOWNLOAD_2_DATABASE();
                    AddToActivity.progBar.setVisibility(View.GONE);

                }

                if (activity == 1) {
                    PlayerAcreenActivity.ADD_DOWNLOAD_2_DATABASE();
                    PlayerAcreenActivity.progBar.setVisibility(View.GONE);
                }
                break;

            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return (msg);
    }

    private void queryStatus() {
        Cursor c = mDownloadManager.query(new DownloadManager.Query().setFilterById(mDownloadedFileID));

        if (c == null) {
            Toast.makeText(mContext, "Download not found!", Toast.LENGTH_LONG).show();
        } else {
            c.moveToFirst();

            Log.d(getClass().getName(), "COLUMN_ID: " +
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));
            Log.d(getClass().getName(), "COLUMN_BYTES_DOWNLOADED_SO_FAR: " +
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
            Log.d(getClass().getName(), "COLUMN_LAST_MODIFIED_TIMESTAMP: " +
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
            Log.d(getClass().getName(), "COLUMN_LOCAL_URI: " +
                    c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
            Log.d(getClass().getName(), "COLUMN_STATUS: " +
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
            Log.d(getClass().getName(), "COLUMN_REASON: " +
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));

            Toast.makeText(mContext, statusMessage(c), Toast.LENGTH_LONG).show();
        }
    }
}