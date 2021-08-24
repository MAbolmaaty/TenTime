package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KaraokeTabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.Applicationmanager;
import com.binarywaves.ghaneely.ghannely_application_manager.BaseActivity;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyaodioplayer.service.KaraokeSongService;
import com.binarywaves.ghaneely.ghannelyaodioplayer.util.UtilFunctions;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.KarokeSavedObject;
import com.binarywaves.ghaneely.ghannelyresponse.KraokeSavedResponse;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Amany on 24-Aug-17.
 */

public class KaraokeTabActivity extends BaseActivity {
    public static RelativeLayout audioRelative;

    private static RelativeLayout progBar;
    public static Activity activity;
    public static String favo;
    // String albumId;
    private static ViewPager viewPager;
    static ImageView back;
    private static Context context;
    private static ProgressWheel pb1;
    private static String language;
    private ArrayList<TrackObject> KarokeTrack;
    private ArrayList<KarokeSavedObject> Karokesaved;
    private Bundle bundle;
    private KaraokeTabActivity.ViewPagerAdapter adapter;
    private FrameLayout frameLayout;
    private View activityView;
    private TabLayout tabLayout;
    private SparseArray<String> mFragmentTags;
    private FragmentManager mFragmentManager;
    public static int selectedfragment;

    private static void startwhellprogress() {

        progBar.setVisibility(View.VISIBLE);
        pb1.spin();

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout
        ViewGroup nullparent = null;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activityView = layoutInflater.inflate(R.layout.karaoketabmain, nullparent, false);

        context = getApplicationContext();
        language = LanguageHelper.getCurrentLanguage(context);
        Applicationmanager.setContext(KaraokeTabActivity.this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }
        }

        Init_Ui();

    }

    private void Init_Ui() {

        viewPager = activityView.findViewById(R.id.viewpager);
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

        tabLayout = activityView.findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


        if (language.equalsIgnoreCase("ar")) {
            tabLayout.setRotationY(180);
            viewPager.setRotationY(180);
        }
        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        frameLayout.addView(activityView);
        GetkaraokeScreen();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        ViewGroup nullparent = null;

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabOne.setTextSize(15);

        tabOne.setText(R.string.songs);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabTwo.setTextSize(15);
        tabTwo.setText(R.string.saved_Songs);
        tabLayout.getTabAt(1).setCustomView(tabTwo);


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new SongTabsFragment(), getResources().getString(R.string.songs));
        adapter.addFragment(new SavedTabsFragment(), getResources().getString(R.string.saved_Songs));

        viewPager.setAdapter(adapter);
    }

    private void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
        selectedfragment = pageIndex;
    }

    private void GetkaraokeScreen() {
        startwhellprogress();

        // Constants.SERVER_URl + Constants.REGISTER_PATH

        String fav = ServerConfig.SERVER_URl + ServerConfig.GetkaraokeScreen + "?userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);
        Api_Interface service = ServiceGenerator.createService();
        service.GetkaraokeScreen(fav_url).enqueue(new Callback<KraokeSavedResponse>() {
            @Override
            public void onResponse(@NonNull Call<KraokeSavedResponse> call, @NonNull Response<KraokeSavedResponse> mresult) {
                if (mresult.isSuccessful()) {
                    KraokeSavedResponse response = mresult.body();
                    if (response.getKaraokeTracks().size() > 0) {
                        KarokeTrack = response.getKaraokeTracks();

                    }

                    if (response.getSavedkaraokeTracks().size() > 0) {
                        Karokesaved = response.getSavedkaraokeTracks();

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
            public void onFailure(@NonNull Call<KraokeSavedResponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });


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
            boolean isServiceRunning2 = UtilFunctions.isServiceRunning(KaraokeSongService.class.getName(),
                    context);
            if (isServiceRunning2) {

                Intent i = new Intent(context, KaraokeSongService.class);

                context.stopService(i);
            }
            switch (position) {
                case 0:
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("songplayed", KarokeTrack);
                    Log.e("songplayed", KarokeTrack + "");
                    fragment = new SongTabsFragment();
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("savedsong", Karokesaved);
                    Log.e("savedsong", Karokesaved + "");
                    fragment = new SavedTabsFragment();
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
