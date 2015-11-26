package kr.whatshoe.whatShoe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

public class NavigationDrawerFragment extends Fragment implements View.OnClickListener{
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mlayout;
    private View mFragmentContainerView;
    private TextView id_text;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private KakaoLink kakaoLink =null ;
    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            kakaoLink= KakaoLink.getKakaoLink(getActivity());
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, true);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mlayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        mlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Button pointBtn = (Button)mlayout.findViewById(R.id.pointBtn);
        pointBtn.setOnClickListener(this);
        Button noticeBtn = (Button)mlayout.findViewById(R.id.notice_btn);
        noticeBtn.setOnClickListener(this);
        Button eventBtn = (Button)mlayout.findViewById(R.id.event_btn);
        eventBtn.setOnClickListener(this);
        Button healthCare = (Button)mlayout.findViewById(R.id.healthBtn);
        healthCare.setOnClickListener(this);
        Button talkBtn = (Button)mlayout.findViewById(R.id.talkBtn);
        talkBtn.setOnClickListener(this);
        Button couponBtn = (Button)mlayout.findViewById(R.id.couponBtn);
        couponBtn.setOnClickListener(this);
        Button historyBtn = (Button)mlayout.findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener(this);
        Button callBtn = (Button)mlayout.findViewById(R.id.call_btn);
        callBtn.setOnClickListener(this);
        return mlayout;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.actionbar_btn);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();

                }
                SharedPreferences idPreferences = getActivity().getSharedPreferences("login_pref", 0);
                String id = idPreferences.getString("id","WhatShoe");
                id_text= (TextView)drawerView.findViewById(R.id.id_text);
                id_text.setText(id);
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle==null){
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DrawerActivity.class);
                switch (v.getId()){
                    case R.id.pointBtn:
                        intent.putExtra("Fragment",DrawerActivity.POINT_FRAGMENT_TYPE);
                        startActivity(intent);
                        break;

                    case R.id.notice_btn:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/whatshoeman"));
                        startActivity(intent);
                        break;
                    case R.id.event_btn:
                        intent.putExtra("Fragment",DrawerActivity.EVENT_FRAGMENT_TYPE);
                        startActivity(intent);
                        break;
                    case R.id.healthBtn:
                        intent.putExtra("Fragment",DrawerActivity.HEALTHCARE_FRAGMENT_TYPE);
                        startActivity(intent);
                        break;
                    case R.id.talkBtn:
                        if (kakaoLink != null) {
                            KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
                            kakaoTalkLinkMessageBuilder = kakaoLink
                                    .createKakaoTalkLinkMessageBuilder();
                            try {

                                final String linkContents = kakaoTalkLinkMessageBuilder
                                        .addText("Manage your fashion! 당신을 왓슈로 초대합니다!")
                                       // .addImage()
                                        .addAppButton(
                                                "왓슈로 이동",
                                                new AppActionBuilder()
                                                        .setUrl("https://play.google.com/store/apps/details?id=kr.whatshoe.whatShoe")
                                                        .build()).build();
                                kakaoLink.sendMessage(linkContents,
                                        getActivity());
                            } catch (KakaoParameterException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.historyBtn:
                        intent.putExtra("Fragment",DrawerActivity.HISTORY_FRAGMENT_TYPE);
                        startActivity(intent);
                        break;
                    case R.id.couponBtn:
                        intent.putExtra("Fragment",DrawerActivity.COUPON_FRAGMENT_TYPE);
                        startActivity(intent);
                        break;
                    case R.id.call_btn:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://goto.kakao.com/@whatshoe"));
                        startActivity(intent);
                        break;
                }

                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawer(mFragmentContainerView);
                }

    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
