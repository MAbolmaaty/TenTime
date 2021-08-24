package com.binarywaves.ghaneely.ghannelyactivites.SearchTabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binarywaves.ghaneely.R;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.ApiUtils;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.Api_Interface;
import com.binarywaves.ghaneely.ghaneely_retrofit_api.retrofit_service.ServiceGenerator;
import com.binarywaves.ghaneely.ghannely_application_manager.BaseActivity;
import com.binarywaves.ghaneely.ghannely_application_manager.ServerConfig;
import com.binarywaves.ghaneely.ghannelyadaptors.AbumGallareyAdaptor;
import com.binarywaves.ghaneely.ghannelyadaptors.SearchfilterListview;
import com.binarywaves.ghaneely.ghannelymodels.ArtistRadio;
import com.binarywaves.ghaneely.ghannelymodels.Constants;
import com.binarywaves.ghaneely.ghannelymodels.LanguageHelper;
import com.binarywaves.ghaneely.ghannelymodels.SharedPrefHelper;
import com.binarywaves.ghaneely.ghannelymodels.SlideAlbumObject;
import com.binarywaves.ghaneely.ghannelymodels.TrackObject;
import com.binarywaves.ghaneely.ghannelyresponse.AlbumLst;
import com.binarywaves.ghaneely.ghannelyresponse.GetRecentUserSearchesListResponse;
import com.binarywaves.ghaneely.ghannelyresponse.GetRecentUserSearchesResponse;
import com.binarywaves.ghaneely.ghannelyresponse.PlaylistnotificationResponse;
import com.binarywaves.ghaneely.ghannelyresponse.SearchResponse;
import com.binarywaves.ghaneely.ghannelyresponse.SmartSearchResponse;
import com.binarywaves.ghaneely.ghannelyresponse.TrackLst;
import com.binarywaves.ghaneely.ghannelyresponse.VideoObjectResponse;
import com.binarywaves.ghaneely.ghannelystyles.ProgressWheel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Amany on 11-May-17.
 */

public class SearchTabActivity extends BaseActivity {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    public static RelativeLayout audioRelative;
    private static RelativeLayout progBar;
    public static Activity activity;
    public static String favo;
    // String albumId;
    public static ArrayList<TrackObject> mAlbumList;
    public static int selectedfragment;
    private static EditText search_text;
    private static Context context;
    private static ProgressWheel pb1;
    private static String language;
    SlideAlbumObject MyslideAlbume;
    ArrayList<SlideAlbumObject> mPlaylistObjects;
    ArrayList<SlideAlbumObject> mPlaylistObjects2;
    LinearLayout imagecontainer;
    private Bundle bundle;
    private FrameLayout frameLayout;
    private View activityView;
    private ListView list;
    private String UserSearchID;
    private ArrayList<GetRecentUserSearchesListResponse> RecentUserSearchesList;
    private ArrayList<String> arraylist = new ArrayList<>();
    private Context mcontext;
    private SearchfilterListview adapterfilter;
    private ViewPagerAdapter adapter;
    private AbumGallareyAdaptor myTrackadAdaptor;
    private ArrayList<AlbumLst> similaralbumLst;
    private ArrayList<AlbumLst> otheralbumLst;
    private ArrayList<TrackLst> tracklist;
    private ArrayList<TrackLst> albumtrack = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView mXMark;
    private ArrayList<AlbumLst> albumLst;
    private ArrayList<ArtistRadio> singerLst;
    private ArrayList<TrackLst> trackLst;
    private ArrayList<PlaylistnotificationResponse> playlistlst;
    private ArrayList<VideoObjectResponse> videolst;

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
        frameLayout = findViewById(R.id.content_frame);
        // inflate the custom activity layout
      final   ViewGroup nullparent = null;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            activityView = layoutInflater.inflate(R.layout.activity_searchtab, nullparent, false);
        }
        context = getApplicationContext();
        mcontext = this;

        language = LanguageHelper.getCurrentLanguage(context);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("titlemenu")) {
                header_image_view.setVisibility(View.GONE);
                title_txt.setVisibility(View.VISIBLE);
                title_txt.setText(extras.getString("titlemenu"));
            }
        }
        Init_Ui();
        GetRecentSearch();

    }

    private void Init_Ui() {
        arraylist = new ArrayList<>();

        viewPager = activityView.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedfragment = position;
                Log.i("selectedpager", selectedfragment + "");

                // Fragment fragment = adapter.getFragment(position);
                Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());

                fragment.onResume();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = activityView.findViewById(R.id.tab_layout);
        String language = LanguageHelper.getCurrentLanguage(getApplicationContext());


        if (language.equalsIgnoreCase("ar")) {
            tabLayout.setRotationY(180);
            viewPager.setRotationY(180);
        }

        tabLayout.setupWithViewPager(viewPager);

        progBar = activityView.findViewById(R.id.progress);
        pb1 = progBar.findViewById(R.id.progress_bar_2);
        progBar.setClickable(false);
        search_text = activityView.findViewById(R.id.search_et);
        search_text.setOnKeyListener(this);

        mXMark = activityView.findViewById(R.id.search_x);

        mXMark.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            search_text.setText(null);

        });
        list = activityView.findViewById(R.id.list);
        list.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            TextView text = view.findViewById(R.id.rank);
            search_text.setText(text.getText().toString());
            // Log.d("text", text.getText().toString());
            list.setVisibility(View.GONE);
            String search_url = null;
            try {
                search_url = URLEncoder.encode(search_text.getText().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SearchCategoriesN(search_url);
        });
        search_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = search_text.getText().toString();

                if (text.length() == 3 && arraylist.size() == 0) {

                    list.setVisibility(View.VISIBLE);
                    Searchselected(text);
                }
                else if (text.length() == 3 && arraylist.size() > 0) {
                    adapterfilter.getFilter().filter(text);

                }
               else if (arraylist.size() > 0) {

                    if (text.length() > 3) {
                        adapterfilter.getFilter().filter(text);
                    }
                }

                if (text.isEmpty() || text.length() < 3) {
                    arraylist = new ArrayList<>();
                    list.setAdapter(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }
        });


        frameLayout.addView(activityView);

    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        ViewGroup nullparent = null;

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabOne.setText(R.string.allsearch);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabTwo.setText(R.string.tracksearch);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabThree.setText(R.string.playlistsearch);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabfour.setText(R.string.artistsearch);
        tabLayout.getTabAt(3).setCustomView(tabfour);

        TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabfive.setText(R.string.albumsearch);
        tabLayout.getTabAt(4).setCustomView(tabfive);

        TextView tabsix = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabview, nullparent);
        tabsix.setText(R.string.video_search);
        tabLayout.getTabAt(5).setCustomView(tabsix);


    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new SearchTab_AllActivity(), getResources().getString(R.string.allsearch));
        adapter.addFragment(new SearchTab_TracksActivity(), getResources().getString(R.string.tracksearch));
        adapter.addFragment(new SearchTab_PlaylistActivity(), getResources().getString(R.string.playlistsearch));
        adapter.addFragment(new SearchTab_ArtistActivity(), getResources().getString(R.string.artistsearch));
        adapter.addFragment(new SearchTab_AlbumActivity(), getResources().getString(R.string.albumsearch));
        adapter.addFragment(new SearchTab_AlbumActivity(), getResources().getString(R.string.video_search));
        viewPager.setAdapter(adapter);

    }

    private void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
        selectedfragment = pageIndex;

    }

    private void GetRecentSearch() {
        startwhellprogress();
        String fav = ServerConfig.SERVER_URl + ServerConfig.GetRecentUserSearches + "?userId="
                + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
        String fav_url = fav.replaceAll(" ", "%20");
        Log.d("amanttest", fav_url);

        Api_Interface service = ServiceGenerator.createService();
        service.GetRecentUserSearches(fav_url).enqueue(new Callback<GetRecentUserSearchesResponse>() {

            @Override
            public void onResponse(@NonNull Call<GetRecentUserSearchesResponse> call, @NonNull Response<GetRecentUserSearchesResponse> mresult) {
                if (mresult.isSuccessful()) {
                    GetRecentUserSearchesResponse response = mresult.body();
                    if (response != null && response.size() > 0) {
                        RecentUserSearchesList = response;

                    }

                    progBar.setVisibility(View.GONE);
                    tabLayout.setupWithViewPager(viewPager);
                    setupViewPager(viewPager);
                    setupTabIcons();
                    selectPage(0);

                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");
                    progBar.setVisibility(View.GONE);

                }
            }


            @Override
            public void onFailure(@NonNull Call<GetRecentUserSearchesResponse> call, @NonNull Throwable t) {
                progBar.setVisibility(View.GONE);

            }
        });
    }

    private void SearchCategoriesN(String keyword) {
        if (Constants.isNetworkOnline(this)) {
            list.setVisibility(View.GONE);
            startwhellprogress();

            final String search = ServerConfig.SERVER_URl + ServerConfig.SearchCategoriesN + "?keyword=" + keyword + "&catId=0"
                    + "&userId=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID) + "&UserToken="
                    + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken);
            String search_url = search.replaceAll(" ", "%20");

            Log.d("TEST", search_url);
            Api_Interface service = ServiceGenerator.createService();
            Log.d("amanttest", search_url);
            service.SearchCategoriesN(search_url).enqueue(new Callback<SearchResponse>() {

                @Override
                public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> mresult) {
                    if (mresult.isSuccessful()) {
                        SearchResponse response = mresult.body();

                        if (response != null) {
                            Toast.makeText(getApplicationContext(), R.string.thnxmessage, Toast.LENGTH_LONG).show();
                            UserSearchID = response.getUsersearchID();
                            albumLst = new ArrayList<>();
                            singerLst = new ArrayList<>();
                            trackLst = new ArrayList<>();
                            playlistlst = new ArrayList<>();
                            videolst=new ArrayList<>();
                            if (response.getSingerLst() != null && response.getSingerLst().size() > 0)
                                singerLst = response.getSingerLst();

                            if (response.getAlbumLst() != null && response.getAlbumLst().size() > 0)
                                albumLst = response.getAlbumLst();

                            if (response.getPlaylistLst() != null && response.getPlaylistLst().size() > 0)
                                playlistlst = response.getPlaylistLst();

                            if (response.getTrackLst() != null && response.getTrackLst().size() > 0)
                                trackLst = response.getTrackLst();
                            if (response.getVideoLsts() != null && response.getVideoLsts().size() > 0)
                                videolst = response.getVideoLsts();


                        }
                        progBar.setVisibility(View.GONE);
                        if(adapter!=null){
                        adapter.notifyDataSetChanged();}
                        viewPager.destroyDrawingCache();
                        tabLayout.setupWithViewPager(viewPager);
                        setupViewPager(viewPager);
                        setupTabIcons();
                    } else {
                        ApiUtils.handelErrorCode(context, mresult.code());
                        System.out.println("onFailure");
                        progBar.setVisibility(View.GONE);

                    }
                }


                @Override
                public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                    progBar.setVisibility(View.GONE);

                }
            });

        } else {


            Toast.makeText(SearchTabActivity.this, getResources().getString(R.string.gnrl_internet_error),
                    Toast.LENGTH_SHORT).show();

        }
    }

    private void Searchselected(final String trackid2) {


        String fav = ServerConfig.SERVER_URl + ServerConfig.GetSuggestedText + "?keyword=" + trackid2 + "&UserToken=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.accesstoken) + "&userId=" + SharedPrefHelper.getSharedString(getApplicationContext(), Constants.USERID);
        String fav_url = fav.replaceAll(" ", "%20");
        Api_Interface service = ServiceGenerator.createService();
        Log.d("amanttest", fav_url);
        service.GetSuggestedText(fav_url).enqueue(new Callback<SmartSearchResponse>() {

            @Override
            public void onResponse(@NonNull Call<SmartSearchResponse> call, @NonNull Response<SmartSearchResponse> mresult) {
                if (mresult.isSuccessful()) {

                    SmartSearchResponse response = mresult.body();
                    arraylist = response;
                    Log.e("tet", response + "");

                    if (arraylist.size() == 0) {
                        Toast.makeText(SearchTabActivity.this, getResources().getString(R.string.no_search_result),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (arraylist.size() > 0) {
                        adapterfilter = new SearchfilterListview(SearchTabActivity.this, R.layout.listview_item, arraylist);
                        // list.setVisibility(View.VISIBLE);

                        // Binds the Adapter to the ListView
                        list.setAdapter(adapterfilter);
                        //  setListViewHeightBasedOnChildren(list);
                    }
                } else {
                    ApiUtils.handelErrorCode(context, mresult.code());
                    System.out.println("onFailure");

                }
            }


            @Override
            public void onFailure(@NonNull Call<SmartSearchResponse> call, @NonNull Throwable t) {

            }
        });


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH
                || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            // performSearch();
            if (!event.isShiftPressed()) {
                if (v.getId() == R.id.search_et) {
                    if (isEmpty()) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_search_result),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("search btn pressed", "HERE");
                        if (search_text.getText().length() >= 2) {
                            String search_url = null;
                            try {
                                search_url = URLEncoder.encode(search_text.getText().toString(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            SearchCategoriesN(search_url);
                            // search_text.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.maxcharacter),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }

            }
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(search_text.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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
                    bundle.putParcelableArrayList("RecentUserSearchesList", RecentUserSearchesList);
                    bundle.putParcelableArrayList("albumLst", albumLst);
                    Log.e("albumLst", albumLst + "");

                    bundle.putParcelableArrayList("singerLst", singerLst);
                    Log.e("singerLst", singerLst + "");

                    bundle.putParcelableArrayList("trackLst", trackLst);

                    bundle.putParcelableArrayList("playlistlst", playlistlst);
                    bundle.putString("UsersearchID", UserSearchID);
                    bundle.putParcelableArrayList("videolst", videolst);

                    Log.e("bundle", RecentUserSearchesList + "" + albumLst + "" + trackLst + playlistlst+videolst);
                    fragment = new SearchTab_AllActivity();
                    fragment.setArguments(bundle);
                    break;

                case 1:
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("trackLst", trackLst);
                    bundle.putString("UsersearchID", UserSearchID);

                    Log.e("trackLst", trackLst + "");

                    fragment = new SearchTab_TracksActivity();
                    fragment.setArguments(bundle);
                    break;


                case 2:

                    bundle = new Bundle();
                    bundle.putParcelableArrayList("playlistlst", playlistlst);
                    bundle.putString("UsersearchID", UserSearchID);
                    Log.e("playlistlst", playlistlst + "");
                    fragment = new SearchTab_PlaylistActivity();
                    fragment.setArguments(bundle);
                    break;


                case 3:

                    bundle = new Bundle();
                    bundle.putParcelableArrayList("singerLst", singerLst);
                    bundle.putString("UsersearchID", UserSearchID);

                    Log.e("bundlerelatedalbumLst", singerLst + "");
                    fragment = new SearchTab_ArtistActivity();
                    fragment.setArguments(bundle);
                    break;


                case 4:

                    bundle = new Bundle();
                    bundle.putParcelableArrayList("albumLst", albumLst);
                    bundle.putString("UsersearchID", UserSearchID);
                    Log.e("bundlerelatedalbumLst", albumLst + "");
                    fragment = new SearchTab_AlbumActivity();
                    fragment.setArguments(bundle);
                    break;

                case 5:

                    bundle = new Bundle();
                    bundle.putParcelableArrayList("videoLst", videolst);
                    bundle.putString("UsersearchID", UserSearchID);
                    Log.e("bundlerelatedalbumLst", videolst + "");
                    fragment = new SearchTab_VideoActivity();
                    fragment.setArguments(bundle);
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

         void addFragment(Fragment fragment, String title) {
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
@SuppressWarnings("unused")
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
