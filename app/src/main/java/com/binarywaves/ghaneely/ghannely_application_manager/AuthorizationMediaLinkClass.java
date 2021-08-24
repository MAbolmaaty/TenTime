package com.binarywaves.ghaneely.ghannely_application_manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.Ghannely_Encrypt_Decrypt_Tracks.Base64;
import com.binarywaves.ghaneely.R;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Amany on 06-Sep-17.
 */

public class AuthorizationMediaLinkClass {

    private Context context;
    private String ip;

    @SuppressWarnings("unused")
    @SuppressLint("SimpleDateFormat")
    public String setTheDate() throws UnknownHostException, UnsupportedEncodingException {

        context = Applicationmanager.getCurrentActivity();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("M/d/y h:m:s a", Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        String today = simpleDateFormat.format(calendar.getTime());
        //   System.out.println("Time zone: " + timeZone.getID());
        //   System.out.println("default time zone: " + TimeZone.getDefault().getID());
        //   System.out.println();

        System.out.println("UTC:     " + simpleDateFormat.format(calendar.getTime()));
        //  System.out.println("Default: " + calendar.getTime());

        String initial_url = "http://yourdomain.com:8081/live";
        String video_url = "/Stream1";
        String ip = NetwordDetect();
        System.out.println("ip:     " + ip);

        String key = "Gh@n!!LY";
        System.out.println("key:     " + key);

        String validminutes = "20";
        System.out.println("validminutes:     " + validminutes);


        String to_hash = ip + key + today + validminutes;
        System.out.println("to_hash:     " + to_hash);

        byte[] ascii_to_hash = to_hash.getBytes("UTF-8");
        System.out.println("ascii_to_hash:     " + ascii_to_hash + " ");

        String base64hash = Base64.encodeBytes(DigestUtils.md5(ascii_to_hash));
        System.out.println("base64hash:     " + base64hash + " ");

        String urlsignature = "server_time=" + today + "&hash_value=" + base64hash + "&validminutes=" + validminutes;

        System.out.println("urlsignature:     " + urlsignature + " ");

        String base64urlsignature = Base64.encodeBytes(urlsignature.getBytes("UTF-8"));
        System.out.println("base64urlsignature:     " + base64urlsignature + " ");

        //  String signedurlwithvalidinterval = initial_url + "?wmsAuthSign=" + base64urlsignature + video_url;
        String signedurlwithvalidinterval = "http://streaming.sia-tec.com:8081/vod/Yara-Khallouni_Maou.mp4/playlist.m3u8" + "?wmsAuthSign=" + base64urlsignature;
        Log.i("wmsAuthSign", signedurlwithvalidinterval);

        return signedurlwithvalidinterval;
    }


    //Check the internet connection.
    private String NetwordDetect() throws UnknownHostException {

        boolean WIFI = false;

        boolean MOBILE = false;


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                WIFI = true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                MOBILE = true;

            }
        } else {
            // not connected to the internet
            Toast.makeText(context, context.getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }

        if (WIFI)

        {
            ip = GetDeviceipWiFiData();


        }

        if (MOBILE) {

            ip = GetDeviceipMobileData();

        }

        return ip;
    }


    private String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    private String GetDeviceipWiFiData() throws UnknownHostException {

        @SuppressLint("WifiManagerPotentialLeak")
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        assert wifiMgr != null;
        WifiInfo wifiinfo = wifiMgr.getConnectionInfo();
        byte[] myIPAddress = BigInteger.valueOf(wifiinfo.getIpAddress()).toByteArray();
// you must reverse the byte array before conversion. Use Apache's commons library
        ArrayUtils.reverse(myIPAddress);
        InetAddress myInetIP = InetAddress.getByAddress(myIPAddress);
        return myInetIP.getHostAddress();

    }


}

