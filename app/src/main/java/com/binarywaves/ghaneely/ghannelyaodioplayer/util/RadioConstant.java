package com.binarywaves.ghaneely.ghannelyaodioplayer.util;

import android.os.Handler;

import com.binarywaves.ghaneely.ghannelymodels.Radio;

import java.util.ArrayList;

public class RadioConstant {
    //song changed (next, previous)
    public static final boolean SONG_CHANGED = false;
    //List of Songs
    public static ArrayList<Radio> SONGS_LIST = new ArrayList<>();
    //song number which is playing right now from SONGS_LIST
    public static int SONG_NUMBER = 0;
    //song is playing or paused
    public static boolean SONG_PAUSED = true;
    //handler for song changed(next, previous) defined in service(SongService)
    public static Handler SONG_CHANGE_HANDLER;
    //handler for song play/pause defined in service(SongService)
    public static Handler PLAY_PAUSE_HANDLER;
    //handler for showing song progress defined in Activities(MainActivity, AudioPlayerActivity)
    public static Handler PROGRESSBAR_HANDLER;
    public static boolean SONGnext = false;
    public static boolean SONGpause = false;


}
