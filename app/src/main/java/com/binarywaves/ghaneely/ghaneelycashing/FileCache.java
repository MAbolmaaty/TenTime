package com.binarywaves.ghaneely.ghaneelycashing;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "com");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public static boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static float megabytesAvailable(File file) {
        StatFs stat = new StatFs(file.getPath());
        long bytesAvailable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = getAvailableBytes(stat);
        } else {
            //noinspection deprecation
            bytesAvailable = stat.getBlockSize() * stat.getAvailableBlocks();
        }

        return bytesAvailable / (1024.f * 1024.f);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static long getAvailableBytes(StatFs stat) {
        return stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
    }

    public static boolean delete(File path) {

        boolean result = true;
        if (path.exists()) {
            if (path.isDirectory()) {
                for (File child : path.listFiles()) {
                    result &= delete(child);
                }
                result &= path.delete(); // Delete empty directory.
            } else if (path.isFile()) {
                result &= path.delete();
            }
            return result;
        } else {
            return false;
        }
    }

    public File getFile(String url) {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename = String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        return new File(cacheDir, filename);

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }


}