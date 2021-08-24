package com.binarywaves.ghaneely.ghannelyaodioplayer.util;

import android.os.Handler;

import com.binarywaves.ghaneely.ghannelymodels.TrackDownloadObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.KarokeSavedObject;
import com.binarywaves.ghaneely.ghannelyresponse.VideoDownloadObject;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class PlayerConstants {
    //List of Songs
    public static ArrayList<TrackObject> SONGS_LIST = new ArrayList<>();
    public static List<KarokeSavedObject> Karaoke_LIST = new ArrayList<>();
    public static ArrayList<TrackDownloadObject> OfflineSONGS_LIST = new ArrayList<>();
    public static ArrayList<VideoDownloadObject> OfflineVideo_LIST = new ArrayList<>();

    //song number which is playing right now from SONGS_LIST
    public static int SONG_NUMBER = 0;
    //song is playing or paused
    public static boolean SONG_PAUSED = true;
    //song changed (next, previous)
    public static boolean SONG_CHANGED = false;
    //handler for song changed(next, previous) defined in service(SongService)
    public static Handler SONG_CHANGE_HANDLER;
    //handler for song play/pause defined in service(SongService)
    public static Handler PLAY_PAUSE_HANDLER;
    //handler for showing song progress defined in Activities(MainActivity, AudioPlayerActivity)
    public static Handler PROGRESSBAR_HANDLER;
    public static boolean SONGnext = false;
    public static boolean SONGpause = false;

    public static ArrayList<VideoObjectResponse> SONGS_LISTVideo = new ArrayList<>();

}
