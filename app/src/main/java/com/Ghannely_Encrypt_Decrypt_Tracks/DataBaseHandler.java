package com.Ghannely_Encrypt_Decrypt_Tracks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.binarywaves.ghaneely.ghannelymodels.TrackDownloadObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amany on 19-Sep-17.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "DownloadListDataBase";

    // Table Name
    private static final String TABLE_DOWNLOAD_LIST = "DownloadList";

    // Table Columns names
    private static final String KEY_TRACKENNAME = "TrackEnName";
    private static final String KEY_TRACKID = "TrackId";
    private static final String TRACKARNAME = "TrackArName";
    private static final String KEY_SINGERENNAME = "SingerEnName";
    private static final String KEY_SINGERARNAME = "SingerArName";
    private static final String KEY_TRACKLENGTH = "TrackLength";
    private static final String KEY_TRACKIMAGE = "TrackImage";

    private static DataBaseHandler mInstance = null;

    private DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DataBaseHandler getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DataBaseHandler(context);

        return mInstance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_DOWNLOAD_LIST + "("
                + KEY_TRACKENNAME + " TEXT,"
                + KEY_TRACKID + " TEXT,"
                + TRACKARNAME + " TEXT,"
                + KEY_SINGERENNAME + " TEXT,"
                + KEY_SINGERARNAME + " TEXT,"
                + KEY_TRACKLENGTH + " TEXT,"
                + KEY_TRACKIMAGE + " BLOB "

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


    public void addItem(TrackDownloadObject item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRACKENNAME, item.getTrackEnName());
        values.put(KEY_TRACKID, item.getTrackId());
        values.put(TRACKARNAME, item.getTrackArName());
        values.put(KEY_SINGERENNAME, item.getSingerEnName());
        values.put(KEY_SINGERARNAME, item.getSingerArName());

        values.put(KEY_TRACKLENGTH, item.getTrackLength());

        values.put(KEY_TRACKIMAGE, item.getTrackImage());

        // Inserting Row

        db.insert(TABLE_DOWNLOAD_LIST, null, values);
        db.close(); // Closing database connection

    }

    TrackDownloadObject getItem(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOWNLOAD_LIST, new String[]{
                        KEY_TRACKENNAME, KEY_TRACKID, TRACKARNAME, KEY_SINGERENNAME, KEY_SINGERARNAME, KEY_TRACKLENGTH, KEY_TRACKIMAGE}, KEY_TRACKID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new TrackDownloadObject(
                cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getBlob(6));

    }

    // Getting All Shops
    public ArrayList<TrackDownloadObject> getAllDownloads() {
        ArrayList<TrackDownloadObject> downloadList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DOWNLOAD_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    TrackDownloadObject shop = new TrackDownloadObject();
                    shop.setTrackEnName(cursor.getString(0));
                    shop.setTrackId(cursor.getString(1));
                    shop.setTrackArName(cursor.getString(2));
                    shop.setSingerEnName(cursor.getString(3));
                    shop.setSingerArName(cursor.getString(4));
                    shop.setTrackLength(cursor.getString(5));
                    shop.setTrackImage(cursor.getBlob(6));
                    // Adding contact to list
                    downloadList.add(shop);
                } while (cursor.moveToNext());
            }
        }  finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        // return contact list
        return downloadList;
    }

    public void loadAllItems(List<TrackDownloadObject> items) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DOWNLOAD_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        items.clear();
        // looping through all rows and adding to list
        try{
        if (cursor.moveToFirst()) {
            do {
                TrackDownloadObject item = new TrackDownloadObject(
                        cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getBlob(6));
                items.add(item);
            } while (cursor.moveToNext());
        }}finally {
            cursor.close();
        }
    }

    public int updateItem(TrackDownloadObject item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TRACKENNAME, item.getTrackEnName());
        values.put(KEY_TRACKID, item.getTrackId());
        values.put(TRACKARNAME, item.getTrackArName());
        values.put(KEY_SINGERENNAME, item.getSingerEnName());
        values.put(KEY_SINGERARNAME, item.getSingerArName());

        values.put(KEY_TRACKLENGTH, item.getTrackLength());

        values.put(KEY_TRACKIMAGE, item.getTrackImage());

        // updating row
        return db.update(TABLE_DOWNLOAD_LIST, values, KEY_TRACKID + " = ?",
                new String[]{String.valueOf(item.getTrackId())});
    }

    public boolean deleteItem(String TRACKID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DOWNLOAD_LIST, KEY_TRACKID + "=" + TRACKID, null) > 0;

    }

    public void removeSingleItem(String TRACKID) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + TABLE_DOWNLOAD_LIST + " WHERE " + KEY_TRACKID + "= '" + TRACKID + "'");

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
