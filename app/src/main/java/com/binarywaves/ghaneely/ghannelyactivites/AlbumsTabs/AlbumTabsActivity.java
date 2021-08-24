package com.binarywaves.ghaneely.ghannelyactivites.AlbumsTabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.binarywaves.ghaneely.ghannelyadaptors.AbumGallareyAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.AlbumLst;
import com.binarywaves.ghaneely.ghannelyresponse.AlbumScreenresponse;
import com.binarywaves.ghaneely.ghannelyresponse.TrackLst;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 10-Apr-17.
 */

public class AlbumTabsActivity extends ActivityMainRunnuing {
    private static RelativeLayout audioRelative;

    private static RelativeLayout progBar;
    public static Activity activity;
    public static String favo;
    // String albumId;
    private static ArrayList<TrackObject> mAlbumList;
    public static int selectedfragment;
    private static ViewPager viewPager;
    private static ImageView play;
    private static ImageView pause;
    private static ImageView Like;
    private static ImageView dislike;
    private static ImageView back;
    private static Context context;
    private static ImageView topimage;
    private static ProgressWheel pb1;
    private static String language;
    private static TextView songname;
    private static TextView albumname;
    private static ImageView player_image;
    private SlideAlbumObject MyslideAlbume;
    private TextView title_txt;
    private ArrayList<SlideAlbumObject> mPlaylistObjects;
    private ArrayList<SlideAlbumObject> mPlaylistObjects2;
    RelativeLayout Editor_relative;
    ArrayList<TrackObject> mPlaylistObjects1;
    private TrackObject mPlayist;
    private LinearLayout imagecontainer;
    private Bundle bundle;
    private ViewPagerAdapter adapter;
    private AbumGallareyAdaptor myTrackadAdaptor;
    private ArrayList<AlbumLst> similaralbumLst;
    private ArrayList<AlbumLst> otheralbumLst;
    private ArrayList<TrackLst> tracklist;
    private ArrayList<TrackLst> albumtrack = new ArrayList<>();
    private TabLayout tabLayout;
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

        setContentView(R.layout.albumtabactivity);
        context = getApplicationContext();
        Applicationmanager.setContext(AlbumTabsActivity.this);

        language = LanguageHelper.getCurrentLanguage(context);

        Init_Ui();


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("album")) {
                // extract the extra-data in the Notification
                MyslideAlbume = extras.getParcelable("album");
                if (MyslideAlbume != null) {
                    Log.e("albumid", MyslideAlbume.getAlbumId() + "found");
                    GetAlbumScren(MyslideAlbume.getAlbumId());

                }
            }
        }

        if (extras != null) {
            if (extras.containsKey("id")) {
                String id = extras.getString("id");
                GetAlbumScren(id);
            }
        }


    }

    private void Init_Ui() {

        topimage = findViewById(R.id.new_song_slider);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

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

        tabLayout = findViewById(R.id.tab_layout);
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


        if (language.equalsIgnoreCase("ar")) {
            tabLayout.setRotationY(180);
            viewPager.setRotationY(180);
        }


            //RTL



      progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        imagecontainer = findViewById(R.id.imagecontainer);
        startwhellprogress();

        title_txt = findViewById(R.id.title_txt);
        title_txt.setSelected(true);

        back = findViewById(R.id.navigation_up);
        back.setOnClickListener(v -> finish());

        //Player Ui initialization
        audioRelative = findViewById(R.id.audioplayer);
        audioRelative.setEnabled(false);

        play = audioRelative.findViewById(R.id.play_img);
        pause = audioRelative.findViewById(R.id.btnPause);

        Like = audioRelative.findViewById(R.id.like_img);
        dislike = audioRelative.findViewById(R.id.btnlikesdis);

        songname = audioRelative.findViewById(R.id.song_name);
        albumname = audioRelative.findViewById(R.id.album_name);
        player_image = audioRelative.findViewById(R.id.player_image);
        //End of player initilaization


    }


    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        ViewGroup nullparent = null;

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabOne.setText(R.string.tracks);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabTwo.setText(R.string.otheralbum);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabThree.setText(R.string.similaralbum);
        tabLayout.getTabAt(2).setCustomView(tabThree);


    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new AlbumTab_Tracks_Activity(), getResources().getString(R.string.tracks));
        adapter.addFragment(new AlbumTab_Otheralbum_Activity(), getResources().getString(R.string.otheralbum));
        adapter.addFragment(new AlbumTab_Relatedalbum_Activity(), getResources().getString(R.string.similaralbum));

        viewPager.setAdapter(adapter);
    }

    private void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);

        viewPager.setCurrentItem(pageIndex);
        selectedfragment = pageIndex;

    }

    private void GetAlbumScren(String myString) {
        startwhellprogress();
        String fav = ServerConfig.SERVER_URl + ServerConfig.Getalbumscreen + "?albumID=" + myString + "&userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");

        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);

        service.Getalbumscreen(fav_url).enqueue(new Callback<AlbumScreenresponse>() {
            @Override
            public void onResponse(@NonNull Call<AlbumScreenresponse> call, @NonNull Response<AlbumScreenresponse> mresult) {
                if (mresult.isSuccessful()) {

                    AlbumScreenresponse response = mresult.body();

                    setResponseUi(response);
                    //convert json string to object

                } else {
                    progBar.setVisibility(View.GONE);
                    ApiUtils.handelErrorCode(getApplicationContext(), mresult.code());
                    System.out.println("onFailure");

                }
            }

            @Override
            public void onFailure(@NonNull Call<AlbumScreenresponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);
                System.out.println("onFailure");
                System.out.println(t.getMessage());


            }

        });
    }

    @SuppressLint("SetTextI18n")
    private void setResponseUi(AlbumScreenresponse response) {
        similaralbumLst = response.getsimilarLst();
        otheralbumLst = response.getOtheralbumLst();
        tracklist = response.getTrackLst();
        mAlbumList = new ArrayList<>();
        mPlaylistObjects = new ArrayList<>();
        mPlaylistObjects2 = new ArrayList<>();
        if (Integer.parseInt(response.getAlbumId()) != 0) {
            if (tracklist.size() > 0) {

                for (int j = 0; j < tracklist.size(); j++) {
                    mPlayist = new TrackObject();

                    mPlayist.setAlbumId(tracklist.get(j).getAlbumId());
                    mPlayist.setAlbumEnName(tracklist.get(j).getAlbumEnName());
                    mPlayist.setAlbumArName(tracklist.get(j).getAlbumArName());
                    mPlayist.setSingerEnName(tracklist.get(j).getSingerEnName());
                    mPlayist.setSingerArName(tracklist.get(j).getSingerArName());
                    mPlayist.setTrackImage(tracklist.get(j).getTrackImage());
                    mPlayist.setTrackEnName(tracklist.get(j).getTrackEnName());
                    mPlayist.setTrackArName(tracklist.get(j).getTrackArName());

                    mPlayist.setTrackId(tracklist.get(j).getTrackId());
                    mPlayist.setTrackPath(tracklist.get(j).getTrackPath());
                    mPlayist.setIsFavourite(tracklist.get(j).getIsFavourite());
                    mPlayist.setIsRBT(tracklist.get(j).getIsRBT());
                    mPlayist.setSingerId(tracklist.get(j).getSingerId());
                    mPlayist.setLikesCount(tracklist.get(j).getLikesCount());
                    mPlayist.setListenCount(tracklist.get(j).getListenCount());
                    mPlayist.setTrackLength(tracklist.get(j).getTrackLength());
                    mPlayist.setLyricFile(tracklist.get(j).getLyricFile());
                    mPlayist.setHasLyrics(tracklist.get(j).getHasLyrics());
                    mPlayist.setVideoID(tracklist.get(j).getVideoID());
                    mPlayist.setIsPremium(tracklist.get(j).getIsPremium());
                    mPlayist.setIsDownloaded(tracklist.get(j).getIsDownloaded());

                    mAlbumList.add(mPlayist);

                }
            } else {

                finish();
            }
            if (otheralbumLst.size() > 0) {

                for (int j = 0; j < otheralbumLst.size(); j++) {
                    SlideAlbumObject mPlayist = new SlideAlbumObject();

                    mPlayist.setAlbumId(otheralbumLst.get(j).getAlbumId());
                    mPlayist.setAlbumEnName(otheralbumLst.get(j).getAlbumEnName());
                    mPlayist.setAlbumArName(otheralbumLst.get(j).getAlbumArName());
                    mPlayist.setSingerEnName(otheralbumLst.get(j).getSingerArName());
                    mPlayist.setSingerArName(otheralbumLst.get(j).getSingerEnName());
                    mPlayist.setAlbumImgPath(otheralbumLst.get(j).getAlbumImgPath());

                    mPlaylistObjects.add(mPlayist);
                }

            }

            if (similaralbumLst.size() > 0) {

                for (int j = 0; j < similaralbumLst.size(); j++) {
                    SlideAlbumObject mPlayist = new SlideAlbumObject();

                    mPlayist.setAlbumId(similaralbumLst.get(j).getAlbumId());
                    mPlayist.setAlbumEnName(similaralbumLst.get(j).getAlbumEnName());
                    mPlayist.setAlbumArName(similaralbumLst.get(j).getAlbumArName());
                    mPlayist.setSingerEnName(similaralbumLst.get(j).getSingerArName());
                    mPlayist.setSingerArName(similaralbumLst.get(j).getSingerEnName());
                    mPlayist.setAlbumImgPath(similaralbumLst.get(j).getAlbumImgPath());

                    mPlaylistObjects2.add(mPlayist);
                }
            }
            // TODO Auto-generated method stub
            if (language.equalsIgnoreCase("ar")) {
                title_txt.setText(
                        response.getAlbumArName() + " ' " + response.getSingerArName() + " ' ");

            }
            if (language.equalsIgnoreCase("en")) {
                title_txt.setText(response.getAlbumEnName() + " ' "
                        + response.getSingerEnName() + " ' ");

            }
            progBar.setVisibility(View.GONE);

            if (mAlbumList != null) {
                if (mAlbumList.size() > 0) {
                    String imgpath1 = ServerConfig.Image_path + response.getAlbumImgPath();
                    String final_imgpath1 = imgpath1.replaceAll(" ", "%20");

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
                }
            }
        } else {
            Toast.makeText(AlbumTabsActivity.this, getResources().getString(R.string.no_search_result),
                    Toast.LENGTH_LONG).show();
            finish();
        }
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        selectPage(0);
    }

    // TODO Auto-generated method stub

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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
                    bundle.putParcelableArrayList("mtracklist", mAlbumList);
                    Log.e("bundle", mAlbumList + "");
                    fragment = new AlbumTab_Tracks_Activity();
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("otheralbumLst", mPlaylistObjects);
                    Log.e("bundlemPlaylistObjects", mPlaylistObjects + "");
                    fragment = new AlbumTab_Otheralbum_Activity();
                    fragment.setArguments(bundle);
                    break;

                case 2:

                    bundle = new Bundle();
                    bundle.putParcelableArrayList("relatedalbumLst", mPlaylistObjects2);
                    Log.e("bundlerelatedalbumLst", mPlaylistObjects2 + "");

                    fragment = new AlbumTab_Relatedalbum_Activity();

                    fragment.setArguments(bundle);
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
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