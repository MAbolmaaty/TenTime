package com.binarywaves.ghaneely.ghaneelycashing;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static Double getAvailableInternalMemorySize() {
        Double bytesAvailable;

        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = getAvailableBytes(stat);
        } else {

            bytesAvailable = formatSize(stat.getBlockSize() * stat.getAvailableBlocks());
        }


        return bytesAvailable;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static Double getAvailableBytes(StatFs stat) {
        Double bytesAvailable;

        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        bytesAvailable = formatSize(availableBlocks * blockSize);
        return bytesAvailable;
    }

    public static Double getTotalInternalMemorySize() {
        Double bytesAvailable;

        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = getAvailableBytes(stat);
        } else {

            bytesAvailable = formatSize(stat.getBlockSize() * stat.getAvailableBlocks());
        }
        return bytesAvailable;
    }

    public static Double getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            Double bytesAvailable;

            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bytesAvailable = getAvailableBytes(stat);
            } else {

                bytesAvailable = formatSize(stat.getBlockSize() * stat.getAvailableBlocks());
            }
            return bytesAvailable;
        } else {
            return 0.0;
        }
    }

    public static Double getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            Double bytesAvailable;

            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bytesAvailable = getAvailableBytes(stat);
            } else {

                bytesAvailable = formatSize(stat.getBlockSize() * stat.getAvailableBlocks());
            }
            return bytesAvailable;
        } else {
            return 0.0;
        }
    }


    public static Double formatSize(long size) {
        Double hrSize = null;

        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();


        symbols.setDecimalSeparator('.');
        DecimalFormat dec = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.US));

        if (t > 1) {
            hrSize = Double.valueOf(dec.format(t));//.concat(" TB");
        } else if (g > 1) {
            hrSize = Double.valueOf(dec.format(g));//.concat(" GB");
        } else if (m > 1) {
            hrSize = Double.valueOf(dec.format(m));//.concat(" MB");
        } else if (k > 1) {
            hrSize = Double.valueOf(dec.format(k));//.concat(" KB");
        } else {
            hrSize = Double.valueOf(dec.format((double) size));//.concat(" Bytes");
        }

        return hrSize;
    }
}