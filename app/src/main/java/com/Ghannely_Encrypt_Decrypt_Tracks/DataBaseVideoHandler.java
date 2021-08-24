package com.Ghannely_Encrypt_Decrypt_Tracks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.binarywaves.ghaneely.ghannelyresponse.VideoDownloadObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amany on 01-Nov-17.
 */

public class DataBaseVideoHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "DownloadListVideoDataBase";

    // Table Name
    private static final String TABLE_DOWNLOAD_LIST = "DownloadListVideo";

    // Table Columns names
    private static final String KEY_VideoArName = "VideoArName";
    private static final String KEY_Videoid = "Videoid ";
    private static final String VideoEnName = "VideoEnName";
    private static final String KEY_SINGERENNAME = "SingerEnName";
    private static final String KEY_SINGERARNAME = "SingerArName";
    private static final String KEY_VideoPoster = "VideoPoster";

    private static DataBaseVideoHandler mInstance = null;

    private DataBaseVideoHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DataBaseVideoHandler getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DataBaseVideoHandler(context);

        return mInstance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_DOWNLOAD_LIST + "("
                + VideoEnName + " TEXT,"
                + KEY_Videoid + " TEXT,"
                + KEY_VideoArName + " TEXT,"
                + KEY_SINGERENNAME + " TEXT,"
                + KEY_SINGERARNAME + " TEXT,"
                + KEY_VideoPoster + " BLOB "
                + ")");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOAD_LIST);

        // Create tables again
        onCreate(db);
    }


    public void addItem(VideoDownloadObject item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VideoEnName, item.getVideoEnName());
        values.put(KEY_Videoid, item.getVideoID());
        values.put(KEY_VideoArName, item.getVideoArName());
        values.put(KEY_SINGERENNAME, item.getSingerEnName());
        values.put(KEY_SINGERARNAME, item.getSingerArName());
        values.put(KEY_VideoPoster, item.getVideoPoster());


        // Inserting Row

        db.insert(TABLE_DOWNLOAD_LIST, null, values);
        db.close(); // Closing database connection

    }

    VideoDownloadObject getItem(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DOWNLOAD_LIST, new String[]{
                        VideoEnName, KEY_Videoid, KEY_VideoArName, KEY_SINGERENNAME, KEY_SINGERARNAME, KEY_VideoPoster}, KEY_Videoid + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new VideoDownloadObject(
                cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5));
    }

    // Getting All Shops
    public ArrayList<VideoDownloadObject> getAllDownloads() {
        ArrayList<VideoDownloadObject> downloadList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DOWNLOAD_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
try{
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VideoDownloadObject shop = new VideoDownloadObject();
                shop.setVideoEnName(cursor.getString(0));
                shop.setVideoID(cursor.getString(1));
                shop.setVideoArName(cursor.getString(2));
                shop.setSingerEnName(cursor.getString(3));
                shop.setSingerArName(cursor.getString(4));
                shop.setVideoPoster(cursor.getBlob(5));
                // Adding contact to list
                downloadList.add(shop);
            } while (cursor.moveToNext());
        }}
finally {
    cursor.close();
}

        // return contact list
        return downloadList;
    }

    public void loadAllItems(List<VideoDownloadObject> items) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DOWNLOAD_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        items.clear();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VideoDownloadObject item = new VideoDownloadObject(
                        cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5));
                items.add(item);
            } while (cursor.moveToNext());
        }
    }

    public int updateItem(VideoDownloadObject item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VideoEnName, item.getVideoEnName());
        values.put(KEY_Videoid, item.getVideoID());
        values.put(KEY_VideoArName, item.getVideoArName());
        values.put(KEY_SINGERENNAME, item.getSingerEnName());
        values.put(KEY_SINGERARNAME, item.getSingerArName());


        values.put(KEY_VideoPoster, item.getVideoPoster());

        // updating row
        return db.update(TABLE_DOWNLOAD_LIST, values, KEY_Videoid + " = ?",
                new String[]{String.valueOf(item.getVideoID())});
    }

    public boolean deleteItem(String TRACKID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DOWNLOAD_LIST, KEY_Videoid + "=" + TRACKID, null) > 0;

    }

    public void removeSingleItem(String TRACKID) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + TABLE_DOWNLOAD_LIST + " WHERE " + KEY_Videoid + "= '" + TRACKID + "'");

        //Close the database
        database.close();
    }

    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DOWNLOAD_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void delete() {
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(TABLE_DOWNLOAD_LIST, null, null);
    }
}
