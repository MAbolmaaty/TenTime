package com.binarywaves.ghaneely.ghannelyactivites.ArtistTabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyactivites.ArtistradioActivity;
import com.binarywaves.ghaneely.ghannelyactivites.DrawerActivity;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.Addrbtresponse;
import com.binarywaves.ghaneely.ghannelyresponse.AlbumLst;
import com.binarywaves.ghaneely.ghannelyresponse.Similarartisr;
import com.binarywaves.ghaneely.ghannelyresponse.SingerfrmSingeridResponse;
import com.binarywaves.ghaneely.ghannelyresponse.TrackLst;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 19-Apr-17.
 */

public class ArtistTabsActivity extends ActivityMainRunnuing {
    public static Activity activity;
    public static RelativeLayout audioRelative;
    private static RelativeLayout progBar;
    public static TrackObject data;
    public static int selectedfragment;
    private static int selectessong = -1;
    private static ImageView topimage;
    private static Context context;
    private static ProgressWheel pb1;
    private static String language;
    private static ImageView back;
    private ArrayList<SlideAlbumObject> mPlaylistObjects;
    private ArrayList<ArtistRadio> singerObjects;
    private int singerid;
    private TextView title;
    private TextView followcount;
    TextView tvartistname;
    Button radio;
    private Button follow;
    private Button unfollow;
    private String artisttitle;
    private int followcounter;
    String like;
    private RelativeLayout btradio;
    private Bundle bundle;
    private TrackObject mPlayist;
    private LinearLayout imagecontainer;
    private ViewPagerAdapter adapter;
    private ArrayList<AlbumLst> albumLst;
    private ArrayList<Similarartisr> similarartist;
    private ArrayList<TrackLst> trackLst;
    private ArrayList<TrackLst> singlesongs;
    private ArrayList<TrackObject> mtop20;
    private ArrayList<TrackObject> msinglelist;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SparseArray<String> mFragmentTags;
    private FragmentManager mFragmentManager;

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.artist_tab_activity);
        context = getApplicationContext();
        language = LanguageHelper.getCurrentLanguage(context);
        Applicationmanager.setContext(ArtistTabsActivity.this);

        Init_Ui();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("playlistId"))
                singerid = extras.getInt("playlistId");
            GetArtistData(singerid);
        }

    }

    private void Init_Ui() {
        selectessong = -1;

        topimage = findViewById(R.id.new_song_slider);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedfragment = position;
                Log.i("selectedpager", selectedfragment + "");

                Fragment fragment = adapter.getFragment(position);
                if (fragment != null) {
                    fragment.onResume();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imagecontainer = findViewById(R.id.imagecontainer);
        followcount = findViewById(R.id.tvfollowcount);
        tabLayout = findViewById(R.id.tab_layout);
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


        if (language.equalsIgnoreCase("ar")) {
            tabLayout.setRotationY(180);
            viewPager.setRotationY(180);
        }

        tabLayout.setupWithViewPager(viewPager);
        title = findViewById(R.id.title_txt);
        back = findViewById(R.id.navigation_up);
        back.setOnClickListener(v -> {if( DrawerActivity.mDrawerList!=null)
                DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(0, null, null), 0, 0);
        });
        btradio = findViewById(R.id.btradio);
        btradio.setOnClickListener(v -> {

            Intent playTrack1 = new Intent(context, ArtistradioActivity.class);
            playTrack1.putExtra("album", Integer.toString(singerid));
            playTrack1.putExtra("title", artisttitle);
            startActivity(playTrack1);
            finish();
        });


        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);

        follow = findViewById(R.id.btfollow);
        unfollow = findViewById(R.id.btunfollow);

        follow.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            follow_unfollow(singerid, "true");

        });

        unfollow.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            follow_unfollow(singerid, "false");

        });


    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        ViewGroup nullparent = null;

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabOne.setText(R.string.Top_songs);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabTwo.setText(R.string.album);
        tabLayout.getTabAt(1).setCustomView(tabTwo);


        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabThree.setText(R.string.similarart);
        tabLayout.getTabAt(2).setCustomView(tabThree);


        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabFour.setText(R.string.singke);
        tabLayout.getTabAt(3).setCustomView(tabFour);


    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new ArtistTab_TopActivity(), getResources().getString(R.string.Top_songs));
        adapter.addFragment(new ArtistTab_AlbumsActivity(), getResources().getString(R.string.album));
        adapter.addFragment(new ArtistTab_SimilarActivity(), getResources().getString(R.string.similarart));
        adapter.addFragment(new ArtistTab_SingleActivity(), getResources().getString(R.string.singke));
        viewPager.setAdapter(adapter);
    }

    private void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
        selectedfragment = pageIndex;
    }

    private void GetArtistData(int singerid) {
        if (Constants.isNetworkOnline(this)) {
            startwhellprogress();

            String search = ServerConfig.SERVER_URl + ServerConfig.GetAllSingerData + "?singerID=" + singerid
                    + "&userId=" + SharedPrefHelper.getSharedString(context, Constants.USERID) + "&UserToken="
                    + SharedPrefHelper.getSharedString(context, Constants.accesstoken);

            String search_url = search.replaceAll(" ", "%20");
            Api_Interface service = ServiceGenerator.createService();
            Log.d("amanttest", search_url);
            service.GetAllSingerData(search_url).enqueue(new Callback<SingerfrmSingeridResponse>() {

                @Override
                public void onResponse(@NonNull Call<SingerfrmSingeridResponse> call, @NonNull Response<SingerfrmSingeridResponse> mresult) {
                    if (mresult.isSuccessful()) {
                        SingerfrmSingeridResponse response = mresult.body();
                        albumLst = response.getAlbumLst();
                        similarartist = response.getSimilarartist();
                        followcounter = Integer.parseInt(response.getFollowersCount());
                        mPlaylistObjects = new ArrayList<>();
                        singerObjects = new ArrayList<>();


                        if (language.equalsIgnoreCase("ar")) {
                            followcount.setText(response.getFollowersCount() + " " + getResources().getString(R.string.follower));
                            artisttitle = response.getSingerArName();
                            title.setText(artisttitle);
                            //   tvartistname.setText(artisttitle);

                        }
                        if (language.equalsIgnoreCase("en")) {
                            followcount.setText(response.getFollowersCount() + " " + getResources().getString(R.string.follower));
                            artisttitle = response.getSingerEnName();
                            title.setText(artisttitle);
                            //  tvartistname.setText(artisttitle);


                        }

                        String imgpath = ServerConfig.Image_pathSinger + response.getSingerImgPath();
                        String final_imgpath1 = imgpath.replaceAll(" ", "%20");

                        Picasso.with(getBaseContext()).load(final_imgpath1).error(R.mipmap.defualt_img)
                                .placeholder(R.mipmap.defualt_img).into(topimage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                BitmapDrawable drawable = (BitmapDrawable) topimage.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();
                                GaussianBlur gaussian = new GaussianBlur(context);
                                gaussian.setMaxImageSize(60);
                                gaussian.setRadius(25); // max
                                Bitmap output = gaussian.render(bitmap, true);
                                final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);
                                imagecontainer.setVisibility(View.VISIBLE);
                                imagecontainer.setBackground(ob);
                            }

                            @Override
                            public void onError() {
                                BitmapDrawable drawable = (BitmapDrawable) topimage.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();
                                GaussianBlur gaussian = new GaussianBlur(context);
                                gaussian.setMaxImageSize(60);
                                gaussian.setRadius(25); // max

                                Bitmap output = gaussian.render(bitmap, true);
                                final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);
                                imagecontainer.setVisibility(View.VISIBLE);
                                imagecontainer.setBackground(ob);
                            }
                        });


                        String IsFollowed = response.getIsFollowed();
                        if (IsFollowed.equalsIgnoreCase("true")) {
                            unfollow.setVisibility(View.VISIBLE);

                            follow.setVisibility(View.GONE);
                        } else {
                            unfollow.setVisibility(View.GONE);

                            follow.setVisibility(View.VISIBLE);
                        }
                        if (albumLst != null) {
                            if (albumLst.size() > 0) {

                                for (int j = 0; j < albumLst.size(); j++) {
                                    SlideAlbumObject mPlayist = new SlideAlbumObject();

                                    mPlayist.setAlbumId(albumLst.get(j).getAlbumId());
                                    mPlayist.setAlbumEnName(albumLst.get(j).getAlbumEnName());
                                    mPlayist.setAlbumArName(albumLst.get(j).getAlbumArName());
                                    mPlayist.setSingerEnName(albumLst.get(j).getSingerArName());
                                    mPlayist.setSingerArName(albumLst.get(j).getSingerEnName());
                                    mPlayist.setAlbumImgPath(albumLst.get(j).getAlbumImgPath());

                                    mPlaylistObjects.add(mPlayist);
                                }

                            }
                        }
                        if (similarartist != null) {
                            if (similarartist.size() > 0) {

                                for (int j = 0; j < similarartist.size(); j++) {
                                    ArtistRadio mPlayist = new ArtistRadio();

                                    mPlayist.setSingerId(similarartist.get(j).getSingerId());
                                    mPlayist.setSingerEnName(similarartist.get(j).getSingerEnName());
                                    mPlayist.setSingerArName(similarartist.get(j).getSingerArName());

                                    mPlayist.setSingerImgPath(similarartist.get(j).getSingerImgPath());

                                    singerObjects.add(mPlayist);
                                }
                            }
                        }
                        msinglelist = new ArrayList<>();

                        singlesongs = response.getTrackLstsingle();
                        if (singlesongs != null) {
                            if (singlesongs.size() > 0) {
                                for (int j = 0; j < singlesongs.size(); j++) {
                                    mPlayist = new TrackObject();

                                    mPlayist.setAlbumId(singlesongs.get(j).getAlbumId());
                                    mPlayist.setAlbumEnName(singlesongs.get(j).getAlbumEnName());
                                    mPlayist.setAlbumArName(singlesongs.get(j).getAlbumArName());
                                    mPlayist.setSingerEnName(singlesongs.get(j).getSingerEnName());
                                    mPlayist.setSingerArName(singlesongs.get(j).getSingerArName());
                                    mPlayist.setTrackImage(singlesongs.get(j).getTrackImage());
                                    mPlayist.setTrackEnName(singlesongs.get(j).getTrackEnName());
                                    mPlayist.setTrackArName(singlesongs.get(j).getTrackArName());

                                    mPlayist.setTrackId(singlesongs.get(j).getTrackId());
                                    mPlayist.setTrackPath(singlesongs.get(j).getTrackPath());
                                    mPlayist.setIsFavourite(singlesongs.get(j).getIsFavourite());
                                    mPlayist.setIsRBT(singlesongs.get(j).getIsRBT());
                                    mPlayist.setSingerId(singlesongs.get(j).getSingerId());
                                    mPlayist.setLikesCount(singlesongs.get(j).getLikesCount());
                                    mPlayist.setListenCount(singlesongs.get(j).getListenCount());
                                    mPlayist.setTrackLength(singlesongs.get(j).getTrackLength());
                                    mPlayist.setHasLyrics(singlesongs.get(j).getHasLyrics());
                                    mPlayist.setLyricFile(singlesongs.get(j).getLyricFile());

                                    mPlayist.setVideoID(singlesongs.get(j).getVideoID());
                                    mPlayist.setIsPremium(singlesongs.get(j).getIsPremium());
                                    mPlayist.setIsDownloaded(singlesongs.get(j).getIsDownloaded());

                                    msinglelist.add(mPlayist);
                                }
                                }
                            }
                            trackLst = response.getTrackLst();
                            mtop20 = new ArrayList<>();

                            if (trackLst != null) {
                                if (trackLst.size() > 0) {
                                    for (int j = 0; j < trackLst.size(); j++) {
                                        mPlayist = new TrackObject();

                                        mPlayist.setAlbumId(trackLst.get(j).getAlbumId());
                                        mPlayist.setAlbumEnName(trackLst.get(j).getAlbumEnName());
                                        mPlayist.setAlbumArName(trackLst.get(j).getAlbumArName());
                                        mPlayist.setSingerEnName(trackLst.get(j).getSingerEnName());
                                        mPlayist.setSingerArName(trackLst.get(j).getSingerArName());
                                        mPlayist.setTrackImage(trackLst.get(j).getTrackImage());
                                        mPlayist.setTrackEnName(trackLst.get(j).getTrackEnName());
                                        mPlayist.setTrackArName(trackLst.get(j).getTrackArName());

                                        mPlayist.setTrackId(trackLst.get(j).getTrackId());
                                        mPlayist.setTrackPath(trackLst.get(j).getTrackPath());
                                        mPlayist.setIsFavourite(trackLst.get(j).getIsFavourite());
                                        mPlayist.setIsRBT(trackLst.get(j).getIsRBT());
                                        mPlayist.setSingerId(trackLst.get(j).getSingerId());
                                        mPlayist.setLikesCount(trackLst.get(j).getLikesCount());
                                        mPlayist.setListenCount(trackLst.get(j).getListenCount());
                                        mPlayist.setTrackLength(trackLst.get(j).getTrackLength());
                                        mPlayist.setHasLyrics(trackLst.get(j).getHasLyrics());
                                        mPlayist.setLyricFile(trackLst.get(j).getLyricFile());

                                        mPlayist.setIsPremium(trackLst.get(j).getIsPremium());

                                        mPlayist.setVideoID(trackLst.get(j).getVideoID());
                                        mPlayist.setIsDownloaded(trackLst.get(j).getIsDownloaded());

                                        mtop20.add(mPlayist);

                                    }
                                }} else {

                                    finish();
                                }
                                progBar.setVisibility(View.GONE);
                                setupViewPager(viewPager);
                                tabLayout.setupWithViewPager(viewPager);
                                setupTabIcons();
                                selectPage(0);



                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");
                        progBar.setVisibility(View.GONE);

                    }

                }

                @Override
                public void onFailure(@NonNull Call<SingerfrmSingeridResponse> call, @NonNull Throwable t) {
                    progBar.setVisibility(View.GONE);

                }
            });
        } else {


            Toast.makeText(ArtistTabsActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();

        }
    }

    //Follow & unfollow
    private void follow_unfollow(int singerid2, final String isFollow) {
        // TODO Auto-generated method stub
        startwhellprogress();

        // Constants.SERVER_URl + Constants.REGISTER_PATH
        SharedPreferences prefs = getSharedPreferences("GhaniliPref", Context.MODE_PRIVATE);


        String fav = ServerConfig.SERVER_URl + ServerConfig.SetUserFollow + "?followId=" + singerid2 + "&userId="
                + prefs.getString(Constants.USERID, "") + "&UserToken=" + prefs.getString(Constants.accesstoken, "")
                + "&followTypeId=" + "1" + "&isFollow=" + isFollow;
        String fav_url = fav.replaceAll(" ", "%20");


        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.SetUserFollow(fav_url).enqueue(new Callback<Addrbtresponse>() {

            @Override
            public void onResponse(@NonNull Call<Addrbtresponse> call, @NonNull Response<Addrbtresponse> mresult) {
                if (mresult.isSuccessful()) {
                    Addrbtresponse response = mresult.body();
                    if (isFollow.equalsIgnoreCase("true")) {
                        if (response.getResultCode().equalsIgnoreCase("True")) {
                            // Toast.makeText(getApplicationContext(),
                            // "Added",
                            // Toast.LENGTH_LONG).show();
                            follow.setVisibility(View.GONE);

                            unfollow.setVisibility(View.VISIBLE);

                        }
                        progBar.setVisibility(View.GONE);
                        followcounter = followcounter + 1;


                        if (language.equalsIgnoreCase("ar")) {
                            followcount.setText(followcounter + " " + getResources().getString(R.string.follower));

                        }
                        if (language.equalsIgnoreCase("en")) {
                            followcount.setText(followcounter + " " + getResources().getString(R.string.follower));
                        }
                    }
                    if (isFollow.equalsIgnoreCase("false")) {
                        if (response.getResultCode().equalsIgnoreCase("True")) {
                            // Toast.makeText(getApplicationContext(),
                            // "Added",
                            // Toast.LENGTH_LONG).show();
                            unfollow.setVisibility(View.GONE);

                            follow.setVisibility(View.VISIBLE);

                        }
                        progBar.setVisibility(View.GONE);
                        followcounter = followcounter - 1;

                        if (language.equalsIgnoreCase("ar")) {
                            followcount.setText(followcounter + " " + getResources().getString(R.string.follower));

                        }
                        if (language.equalsIgnoreCase("en")) {
                            followcount.setText(followcounter + " " + getResources().getString(R.string.follower));
                        }
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);


                }
            }


            @Override
            public void onFailure(@NonNull Call<Addrbtresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(DrawerActivity.mDrawerList!=null){

        DrawerActivity.mDrawerList.performItemClick(DrawerActivity.mDrawerList.getAdapter().getView(0, null, null), 0, 0);
    }}

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);

            mFragmentManager = manager;
            mFragmentTags = new SparseArray<>();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;  // This will get invoke as soon as you call notifyDataSetChanged on viewPagerAdapter.
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {

                case 0:
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("top20", mtop20);
                    Log.e("mtop20", mtop20 + "");
                    fragment = new ArtistTab_TopActivity();
                    fragment.setArguments(bundle);
                    break;

                case 1:
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("otherartistalbum", mPlaylistObjects);
                    Log.e("otherartistalbum", mPlaylistObjects + "");
                    fragment = new ArtistTab_AlbumsActivity();
                    fragment.setArguments(bundle);
                    break;

                case 2:

                    bundle = new Bundle();
                    bundle.putParcelableArrayList("similatartist", singerObjects);
                    Log.e("similatartist", singerObjects + "");
                    fragment = new ArtistTab_SimilarActivity();
                    fragment.setArguments(bundle);
                    break;

                case 3:

                    bundle = new Bundle();
                    bundle.putParcelableArrayList("msinglelist", msinglelist);
                    Log.e("msinglelist", msinglelist + "");
                    fragment = new ArtistTab_SingleActivity();
                    fragment.setArguments(bundle);
                    break;


            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }
            return object;
        }

        public Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = mFragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }
    }
}