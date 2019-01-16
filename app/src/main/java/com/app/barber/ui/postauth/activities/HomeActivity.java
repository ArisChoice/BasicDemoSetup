package com.app.barber.ui.postauth.activities;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.barber.R;
import com.app.barber.core.BarberApplication;
import com.app.barber.core.BaseActivity;
import com.app.barber.models.response.CheckQbIdResponseModel;
import com.app.barber.models.response.LoginResponseModel;
import com.app.barber.net.NetworkConstatnts;
import com.app.barber.ui.adapters.pageradapter.HomePagerAdapter;
import com.app.barber.ui.postauth.activities.barber.booking.ConfirmBookingActivity;
import com.app.barber.ui.postauth.activities.barber.ratesettings.RateBarberActivity;
import com.app.barber.ui.postauth.activities.home.homemvp.HomeAuthMVPView;
import com.app.barber.ui.postauth.activities.home.homemvp.HomeAuthPresenter;
import com.app.barber.ui.preauth.authrise.AuthanticationActivity;
import com.app.barber.util.CommonUtils;
import com.app.barber.util.CurrentLocationSinglton;
import com.app.barber.util.GlobalValues;
import com.app.barber.util.PreferenceManager;
import com.app.barber.views.CustomTextView;
import com.app.barber.views.CustomViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomeAuthMVPView {
    @Inject
    PreferenceManager mPref;

    @BindView(R.id.home_pager)
    CustomViewPager homePager;
    @BindView(R.id.navigation)
    com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx navigation;
    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    CustomTextView txtTitleToolbar;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.drawer_layout)
    LinearLayout drawerLayout;
    @BindView(R.id.toolbar_common)
    Toolbar toolbarCommon;
    private HomePagerAdapter homePagerAdapter;
    private HomeAuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BarberApplication) getApplication()).getMyComponent(this).inject(this);
        presenter = new HomeAuthPresenter(this);
        presenter.attachView(this);
        initPager();
        initBottomToolbar();
        getSpecialisations();
//       updateMenuFonts();
//        activitySwitcher(HomeActivity.this, RateBarberActivity.class, null);
    }

    private void getSpecialisations() {
        presenter.getSpecialisationList(NetworkConstatnts.RequestCode.API_GET_SPECIALISATION, "", false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    private void initBottomToolbar() {
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
//        navigation.setTextSize(0f);
        navigation.setIconSize(20, 20);
        navigation.setTextVisibility(false);
        CurrentLocationSinglton.getInstance().getCurrentLocation(this, new CurrentLocationSinglton.CurrentLocationCallback() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.e("Test", " lati " + location.getLatitude() + " long " + location.getLongitude());
//                    params.put("lat", location.getLatitude() + "");
//                    params.put("lng", location.getLongitude() + "");
//                    hitApiNowAll();
                }
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUserAuth();
    }

    private void refreshUserAuth() {
        navigation.setVisibility(View.VISIBLE);
        if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn)) {
            checkQb();
            checkRecentStatus();
        }
    }

    private void checkQb() {
        if (getUserData() != null && getUserData().getQBId() == null || getUserData().getQBId().equals(""))
            checkQuickBloxid();
    }

    private void checkQuickBloxid() {
        presenter.checkQbId(NetworkConstatnts.RequestCode.API_CHECK_QB_ID, null, false);
    }

    private void checkRecentStatus() {
        presenter.checkRecentStatus(NetworkConstatnts.RequestCode.API_CHECK_RECENT_STATUS, null, false);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            //reference to the item of the menu

            switch (item.getItemId()) {
                case R.id.navigation_explore:
                    homePager.setCurrentItem(0);
                    backToolbar.setVisibility(View.INVISIBLE);
                    txtTitleToolbar.setText(R.string.title_explor);
                    toolbarCommon.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_saved:
                    if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn)) {
                        toolbarCommon.setVisibility(View.GONE);
                        homePager.setCurrentItem(1);
                        backToolbar.setVisibility(View.INVISIBLE);
                        txtTitleToolbar.setText(R.string.title_saved);
                    } else {
                        activitySwitcher(HomeActivity.this, AuthanticationActivity.class, null);
                    }
//                        new CommonUtils().displayMessage(HomeActivity.this, getString(R.string.login_required));
                    return true;
                case R.id.navigation_appointments:
                    if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn)) {
                        toolbarCommon.setVisibility(View.GONE);
                        homePager.setCurrentItem(2);
                        backToolbar.setVisibility(View.INVISIBLE);
                        txtTitleToolbar.setText(R.string.title_appointments);
                    } else {
                        activitySwitcher(HomeActivity.this, AuthanticationActivity.class, null);
                    }
                    return true;
                case R.id.navigation_Message:
                    if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn)) {
                        toolbarCommon.setVisibility(View.GONE);
                        homePager.setCurrentItem(3);
                        txtTitleToolbar.setText(R.string.title_inbox);
                    } else {
                        activitySwitcher(HomeActivity.this, AuthanticationActivity.class, null);
                    }
                    return true;
                case R.id.navigation_more:
                    if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn)) {
                        homePager.setCurrentItem(4);
                        toolbarCommon.setVisibility(View.GONE);
                    } else {
                        activitySwitcher(HomeActivity.this, AuthanticationActivity.class, null);
                    }
                    return true;
            }
            return false;
        }
    };

    /**
     * initialize pager
     */
    private void initPager() {
        toolbarCommon.setVisibility(View.GONE);
        homePagerAdapter = new HomePagerAdapter(getApplicationContext(), getSupportFragmentManager());
        homePager.setAdapter(homePagerAdapter);
        homePager.setPagingEnabled(true);
        homePager.setOffscreenPageLimit(0);
        homePager.swipeable = false;
        homePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //double tap to close app
    boolean doubleBackToExitPressedOnce;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(HomeActivity.this, R.string.back_alert_message, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 3500);
    }


    @OnClick({R.id.back_toolbar, R.id.img_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                break;
            case R.id.img_edit:
                break;
        }
    }

    @Override
    public void onSuccessResponse(int serviceMode, Object o) {
        switch (serviceMode) {
            case NetworkConstatnts.RequestCode.API_CHECK_QB_ID:
                CheckQbIdResponseModel rData = (CheckQbIdResponseModel) o;
                LoginResponseModel.UserBean userData = getUserData();
                userData.setQBId(String.valueOf(rData.getId()));
                presenter.saveUserData(userData);
                presenter.initQbUser(userData);
                break;
        }
    }

    @Override
    public void onfaliurResponse(int serviceMode, Object o) {

    }

    @Override
    public void showProgres() {

    }

    @Override
    public void hidePreogress() {

    }

    @Override
    public void onSuccess(Object o, int type) {

    }

    @Override
    public void onError(String localizedMessage) {

    }

    @Override
    public void onException(Exception e) {

    }
}
