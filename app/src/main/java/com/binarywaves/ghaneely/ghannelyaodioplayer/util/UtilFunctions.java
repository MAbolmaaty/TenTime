package com.binarywaves.ghaneely.ghannelyaodioplayer.util;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.binarywaves.ghaneely.R;

@SuppressWarnings({"unused", "JavaDoc", "ConstantConditions"})
public class UtilFunctions {
    // --Commented out by Inspection (11-Dec-17 10:55 AM):static String LOG_CLASS = "UtilFunctions";

    /**
     * Check if service is running or not
     *
     * @param serviceName
     * @param context
     * @return
     */
    public static boolean isServiceRunning(String serviceName, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    /*
    public static ArrayList<TrackLst> listOfSongs(Context context){
		//Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	//	Cursor c = context.getContentResolver().query(uri, null, MediaStore.Audio.Media.IS_MUSIC + " != 0", null, null);
		ArrayList<TrackLst> listOfSongs = new ArrayList<TrackLst>();
	//	c.moveToFirst();
	//	while(c.moveToNext()){
	//		TrackLst songData = new TrackLst();
			/*
			String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
			String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
			long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
			String data = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
			long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			String composer = c.getString(c.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
			
			songData.setTitle(title);
			songData.setAlbum(album);
			songData.setArtist(artist);
			songData.setDuration(duration);
			songData.setPath(data);
			songData.setAlbumId(albumId);
			songData.setComposer(composer);
			listOfSongs.add(songData);
			*/
    //	}
    //	c.close();
    //	Log.d("SIZE", "SIZE: " + listOfSongs.size());
    //return listOfSongs;
//	}

    /*
    public static Bitmap getAlbumart(Context context,long album_id){
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
	    try{
	        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
	        ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
	        if (pfd != null){
	            FileDescriptor fd = pfd.getFileDescriptor();
	            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
	            pfd = null;
	            fd = null;
	        }
	    } catch(Error ee){}
	    catch (Exception e) {}
	    return bm;
	}
	*/

    /**
     * @param context
     * @return
     */
    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher, options);
        } catch (Error ee) {
            ee.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * Convert milliseconds into time hh:mm:ss
     *
     * @param milliseconds
     * @return time in String
     */
    public static String getDuration(long milliseconds) {
        long sec = (milliseconds / 1000) % 60;
        long min = (milliseconds / (60 * 1000)) % 60;
        long hour = milliseconds / (60 * 60 * 1000);

        String s = (sec < 10) ? "0" + sec : "" + sec;
        String m = (min < 10) ? "0" + min : "" + min;
        String h = "" + hour;

        String time;
        if (hour > 0) {
            time = h + ":" + m + ":" + s;
        } else {
            time = m + ":" + s;
        }
        return time;
    }

    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        return sdkVersion >= Build.VERSION_CODES.N_MR1 ;
    }

    public static boolean currentVersionSupportLockScreenControls() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        return sdkVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

}
