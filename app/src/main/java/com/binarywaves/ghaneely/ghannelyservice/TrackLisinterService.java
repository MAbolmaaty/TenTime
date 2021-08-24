package com.binarywaves.ghaneely.ghannelyservice;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.PlayerAcreenActivity;
import com.binarywaves.ghaneely.ghannelyactivites.VideoPlayerActivity;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.KaraokeSongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OFFLINEService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.OfflineVideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.SongServiceradio;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.VideoService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.PlayerConstants;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;

import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackLisinterService {
    public static Handler seekHandler = new Handler();
    public static Activity activity;
    public static boolean start = true;
    private static boolean isServiceRunning3;
    private static long UPDATE_INTERVAL = 1000; // default
    private static Timer timer = new Timer();
    private Context context;
    private Application application;
    WakeLock wakeLock;
    String songnumber = "0";

    public TrackLisinterService(Context context, Application application) {
        super();
        this.context = context;
        this.application = application;
    }

/*
    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("Google", "Service Started");

		timer.scheduleAtFixedRate(

				new TimerTask() {

					public void run() {
						isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
								context);
						if (isServiceRunning3 == true && SongService.mp != null && start == true) {
							if (SongService.mp.getCurrentPosition() >= 10000) {
								SetTrackListened(
										PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId());
								start = false;

							}
						}

						boolean isServiceRunning1 = UtilFunctions.isServiceRunning(SongServiceAlbum.class.getName(),
								context);
						if (isServiceRunning1 == true && SongServiceAlbum.mp != null && start == true) {
							if (SongServiceAlbum.mp.getCurrentPosition() >= 10000) {

								SetTrackListened(Albumconstant.SONGS_LIST.get(Albumconstant.SONG_NUMBER).getTrackId());
								start = false;
							}
						}

						boolean isServiceRunning2 = UtilFunctions.isServiceRunning(Songserviceplaylist.class.getName(),
								context);
						if (isServiceRunning2 == true && Songserviceplaylist.mp != null && start == true) {
							if (Songserviceplaylist.mp.getCurrentPosition() >= 10000) {

								SetTrackListened(
										Playlistconstant.SONGS_LIST1.get(Playlistconstant.SONG_NUMBER).getTrackId());
								start = false;
							}
						}

						boolean isServiceRunning5 = UtilFunctions.isServiceRunning(SongServiceMoods.class.getName(),
								context);
						if (isServiceRunning5 == true && SongServiceMoods.mp != null && start == true) {

							if (SongServiceMoods.mp.getCurrentPosition() >= 10000) {

								SetTrackListened(MoodsConstant.SONGS_LIST.get(MoodsConstant.SONG_NUMBER).getTrackId());
								start = false;
							}
						}
					}
				}, 1000, UPDATE_INTERVAL);
		return START_STICKY;

	}
	*/

    private static void Remove_handlers() {
        // TODO Auto-generated method stub
        if (PlayerAcreenActivity.seekHandler != null) {
            PlayerAcreenActivity.seekHandler.removeCallbacks(PlayerAcreenActivity.run);
        }
        if (VideoPlayerActivity.seekHandler != null) {
            VideoPlayerActivity.seekHandler.removeCallbacks(VideoPlayerActivity.run);
        }


    }

    public void StopService() {
        // TODO Auto-generated method stub
        if(context==null){
            context= Applicationmanager.getContext();
        }
        boolean isServiceRunning = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                context);
        if (isServiceRunning) {

            Intent i = new Intent(context, KaraokeSongService.class);
            context.stopService(i);
            Remove_handlers();

        }
        boolean isServiceRunning1 = UtilFunctions.isServiceRunning(OFFLINEService.class.getName(),
                context);
        if (isServiceRunning1) {

            Intent i = new Intent(context, OFFLINEService.class);
            if(OFFLINEService.mNotificationUtils!=null) {
                OFFLINEService.mNotificationUtils.getManager().cancel(OFFLINEService.NOTIFICATION_ID);
            }
            context.stopService(i);

            Remove_handlers();

        }
        boolean isServiceRunning5 = UtilFunctions.isServiceRunning(OfflineVideoService.class.getName(),
                context);
        if (isServiceRunning5) {

            Intent i = new Intent(context, OfflineVideoService.class);
            context.stopService(i);
            Remove_handlers();

        }
        boolean isServiceRunning2 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                context);
        if (isServiceRunning2) {

            Intent i = new Intent(context, SongService.class);
            if (SongService.isReady && SongService.mp != null) {
                Log.d("durationtest", SongService.mp.getCurrentPosition() / 1000 + "");
                settracklisten(SongService.mp.getCurrentPosition() / 1000);
            }
            if(SongService.mNotificationUtils!=null) {
                SongService.mNotificationUtils.getManager().cancel(SongService.NOTIFICATION_ID);
            }
            context.stopService(i);

            Remove_handlers();

        }

        boolean isServiceRunning3 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                context);
        if (isServiceRunning3) {

            Intent i = new Intent(context, VideoService.class);
            if (VideoService.isReady && VideoService.mp != null) {
                Log.d("durationtest", VideoService.mp.getCurrentPosition() / 1000 + "");
                setVideolisten(VideoService.mp.getCurrentPosition() / 1000);
            }
            context.stopService(i);

            Remove_handlers();

        }
        boolean isServiceRunning4 = UtilFunctions.isServiceRunning(SongServiceradio.class.getName(),
                context);
        if (isServiceRunning4) {
            Intent i = new Intent(context, SongServiceradio.class);
            if(SongServiceradio.mNotificationUtils!=null) {
                SongServiceradio.mNotificationUtils.getManager().cancel(SongServiceradio.NOTIFICATION_ID);
            }
            context.stopService(i);

            Remove_handlers();

        }


    }

    public void settracklisten(long l) {
        isServiceRunning3 = UtilFunctions.isServiceRunning(SongService.class.getName(),
                context);
        if (isServiceRunning3 && SongService.mp != null && start) {
            SetTrackListened(
                    PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTrackId(), l);
            start = false;


        }


    }


    private void setVideolisten(long l) {
        isServiceRunning3 = UtilFunctions.isServiceRunning(VideoService.class.getName(),
                context);
        if (isServiceRunning3 && VideoService.mp != null && start) {
            SetVideoClipViewed(
                    PlayerConstants.SONGS_LISTVideo.get(PlayerConstants.SONG_NUMBER).getVideoID(), l);
            start = false;


        }
    }

    private void SetTrackListened(String trackId, Long position) {
        // TODO Auto-generated method stub

        if (Constants.isNetworkOnline(context)) {

            String fav = ServerConfig.SERVER_URl + ServerConfig.SetTrackListened + "?trackId=" + trackId + "&userId="
                    + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken) + "&duration=" + position;
            String fav_url = fav.replaceAll(" ", "%20");
            Api_Interface service = ServiceGenerator.createService();
            Log.d("amanttest", fav_url);
            service.SetTrackListened(fav_url).enqueue(new Callback<Addrbtresponse>() {

                @Override
                public void onResponse(Call<Addrbtresponse> call, Response<Addrbtresponse> mresult) {
                    if (mresult.isSuccessful()) {
                        Addrbtresponse response = mresult.body();

                        Log.d("TrackLisinter", response + "");

                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");

                    }
                }


                @Override
                public void onFailure(Call<Addrbtresponse> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }
    }


    private void SetVideoClipViewed(String trackId, Long position) {
        // TODO Auto-generated method stub

        if (Constants.isNetworkOnline(context)) {

            String fav = ServerConfig.SERVER_URl + ServerConfig.SetVideoClipViewed + "?videoClipId=" + trackId + "&userId="
                    + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(context, Constants.accesstoken) + "&duration=" + position;
            String fav_url = fav.replaceAll(" ", "%20");

            Log.d("amanttest", fav_url);
            Api_Interface service = ServiceGenerator.createService();
            service.SetVideoClipViewed(fav_url).enqueue(new Callback<Addrbtresponse>() {

                @Override
                public void onResponse(Call<Addrbtresponse> call, Response<Addrbtresponse> mresult) {
                    if (mresult.isSuccessful()) {
                        Addrbtresponse response = mresult.body();
                        Log.e("TrackLisinter", response + "");


                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");

                    }
                }


                @Override
                public void onFailure(Call<Addrbtresponse> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_LONG).show();
        }
    }


}