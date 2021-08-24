package com.binarywaves.ghaneely.ghannelyactivites.FriendsTabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.binarywaves.ghaneely.ghaneelysingleton.AppSingleTon;
import com.binarywaves.ghaneely.ghannely_application_manager.ActivityMainRunnuing;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyadaptors.GaussianBlur;
import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.FriendsDetailResponse;
import com.binarywaves.ghaneely.ghannelyresponse.TrackLst;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amany on 08-May-17.
 */

public class FriendsTabActivity extends ActivityMainRunnuing {
    public static RelativeLayout audioRelative;

    private static RelativeLayout progBar;
    public static Activity activity;
    public static String favo;
    // String albumId;
    public static ArrayList<TrackObject> mAlbumList;
    public static int selectedfragment;
    private static ViewPager viewPager;
    private static ImageView back;
    private static Context context;
    private static ImageView topimage;
    private static ProgressWheel pb1;
    private static String language;
    private TextView title_txt;
    private LinearLayout imagecontainer;
    private ArrayList<TrackObject> recentplayed;
    private ArrayList<TrackObject> likes;
    private Bundle bundle;
    private ViewPagerAdapter adapter;
    private String profileid;
    private String profilename;
    private String Friendname;
    private ArrayList<ArtistRadio> followeredSingers;
    private ArrayList<TrackLst> albumtrack = new ArrayList<>();
    private TabLayout tabLayout;
    private SparseArray<String> mFragmentTags;
    private FragmentManager mFragmentManager;
    Bitmap friendbitmap = null;

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

        setContentView(R.layout.activity_friendlistdetail);
        context = getApplicationContext();
        language = LanguageHelper.getCurrentLanguage(context);
        Applicationmanager.setContext(FriendsTabActivity.this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("profileid")) {
                profileid = getIntent().getExtras().getString("profileid");
            }


        }

        if (extras != null) {
            if (extras.containsKey("profilename")) {
                profilename = getIntent().getExtras().getString("profilename");

            }
        }

        Init_Ui();

    }

    private void Init_Ui() {
        title_txt = findViewById(R.id.title_txt);
        back = findViewById(R.id.navigation_up);
        back.setOnClickListener(v -> finish());
        topimage = findViewById(R.id.new_song_slider);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//        topimage = findViewById(R.id.new_song_slider);
//        topimage.setProfileId(profileid);
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

        tabLayout.setupWithViewPager(viewPager);

        progBar = findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        imagecontainer = findViewById(R.id.imagecontainer);
        progBar.setClickable(false);
        getlist(profileid);

    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        ViewGroup nullparent = null;

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabOne.setText(R.string.recenltlyplayed);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabTwo.setText(R.string.likes);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabThree.setText(R.string.follwartist);
        tabLayout.getTabAt(2).setCustomView(tabThree);


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new FriendTab_RecentlyplayedActivity(), getResources().getString(R.string.recenltlyplayed));
        adapter.addFragment(new FriendTab_LikesActivity(), getResources().getString(R.string.likes));
        adapter.addFragment(new FriendTab_followArtActivity(), getResources().getString(R.string.follwartist));

        viewPager.setAdapter(adapter);
    }

    private void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
        selectedfragment = pageIndex;
        if(friendbitmap==null){
        Thread thread = new Thread(() -> {
            try {
                //Your code goes here
                getFacebookProfilePicture(profileid);

            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }
        });
        thread.start();
    }}

    private void getlist(String facebookid) {
        if (Constants.isNetworkOnline(this)) {

            startwhellprogress();

            String search = ServerConfig.SERVER_URl + ServerConfig.GetFriendData + "?userId="
                    + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken)
                    + "&friendFacebookId=" + facebookid;

            String search_url = search.replaceAll(" ", "%20");
            Log.i("amanttest", search_url);
            Api_Interface service = ServiceGenerator.createService();
            service.GetFriendData(search_url).enqueue(new Callback<FriendsDetailResponse>() {

                @Override
                public void onResponse(@NonNull Call<FriendsDetailResponse> call, @NonNull Response<FriendsDetailResponse> mresult) {
                    if (mresult.isSuccessful()) {
                        FriendsDetailResponse response = mresult.body();
                        if (response != null) {
                            Friendname = response.getFriendFacebookName();
                            recentplayed = response.getRecentplayed();
                            likes = response.getLikes();
                            followeredSingers = response.getFolloweredSingers();
                        }

                        Thread thread = new Thread(() -> {
                            try {
                                //Your code goes here
                                getFacebookProfilePicture(profileid);

                            } catch (Exception e) {
                                Log.e("TAG", e.getMessage());
                            }
                        });
                        thread.start();


                        if (Friendname != null && !Friendname.equalsIgnoreCase("")) {
                            title_txt.setText(Friendname);
                        } else {
                            title_txt.setText(profilename + " ");

                        }

                        if (recentplayed.size() > 0 || likes.size() > 0 || followeredSingers.size() > 0) {
                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);
                            setupTabIcons();


                            selectPage(0);
                        } else {
                            Toast.makeText(FriendsTabActivity.this, getResources().getString(R.string.norecentdata),
                                    Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        progBar.setVisibility(View.GONE);

                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");
                        progBar.setVisibility(View.GONE);

                    }

                }

                @Override
                public void onFailure(@NonNull Call<FriendsDetailResponse> call, @NonNull Throwable t) {
                    progBar.setVisibility(View.GONE);

                }
            });


        } else {

            // no_timeline_data.setVisibility(View.VISIBLE);
            progBar.setVisibility(View.GONE);
            Toast.makeText(FriendsTabActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();


        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            Log.i("image", imageURL + "");
            friendbitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            topimage.setImageBitmap(friendbitmap);
            GaussianBlur gaussian = new GaussianBlur(context);
            gaussian.setMaxImageSize(60);
            gaussian.setRadius(25); // max

            Bitmap output = gaussian.render(friendbitmap, true);
            final BitmapDrawable ob = new BitmapDrawable(context.getResources(), output);

            imagecontainer.setBackground(ob);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return friendbitmap;
    }

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
                   // bundle = new Bundle();
                   // bundle.putParcelableArrayList("recentplayed", recentplayed);

                    Log.e("bundle", recentplayed + "");
                    AppSingleTon.getInstance().setRecentplayed(recentplayed);

                    fragment = new FriendTab_RecentlyplayedActivity();
                    //fragment.setArguments();
                    break;
                case 1:
                 //   bundle = new Bundle();
                 //   bundle.putParcelableArrayList("likes", likes);
                    Log.e("likes", likes + "");
                    AppSingleTon.getInstance().setLikes(likes);
                    fragment = new FriendTab_LikesActivity();
                 //   fragment.setArguments(bundle);
                    break;

                case 2:

                   // bundle = new Bundle();
                   // bundle.putParcelableArrayList("followeredSingers", followeredSingers);
                    Log.e("followeredSingers", followeredSingers + "");
                    AppSingleTon.getInstance().setFolloweredSingers(followeredSingers);

                    fragment = new FriendTab_followArtActivity();

                    //fragment.setArguments(bundle);
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
