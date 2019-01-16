package com.app.barber.ui.postauth.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.app.barber.R;
import com.app.barber.core.BarberApplication;
import com.app.barber.core.BaseFragment;
import com.app.barber.models.request.RequestBarberModel;
import com.app.barber.models.request.RequestFavUnfavModel;
import com.app.barber.models.request.UpdateAddressRequestModel;
import com.app.barber.models.response.BarberListResponseModel;
import com.app.barber.net.NetworkConstatnts;
import com.app.barber.ui.postauth.activities.barber.AddressSelectionActivity;
import com.app.barber.ui.postauth.activities.barber.BarberDetailActivity;
import com.app.barber.ui.postauth.activities.barber.barber_adapter.BarberListAdapter;
import com.app.barber.ui.postauth.activities.barber.barber_adapter.HorizontalListAdapter;
import com.app.barber.ui.postauth.activities.barber.barbermvp.BarberMVPView;
import com.app.barber.ui.postauth.activities.barber.barbermvp.BarberPresenter;
import com.app.barber.ui.postauth.activities.home.SearchActivity;
import com.app.barber.util.CommonUtils;
import com.app.barber.util.FunctionalDialog;
import com.app.barber.util.GlobalValues;
import com.app.barber.util.iface.OnBottomDialogItemListener;
import com.app.barber.views.CustomTextView;
import com.app.barber.views.WorkaroundMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.barber.core.BaseActivity.activitySwitcher;

/**
 * Created by harish on 25/10/18.
 */

public class ExploreFragment extends BaseFragment implements BarberMVPView, OnMapReadyCallback {
    private static int BARBER_TYPE = 0;//All=0,Barber=1,CallOutBarber=2;

    @BindView(R.id.top_bar)
    LinearLayout topBar;
    @BindView(R.id.search_field)
    LinearLayout searchField;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.recyclar_view_lst)
    RecyclerView recyclarViewLst;
    @BindView(R.id.no_list_data_text)
    CustomTextView noListDataText;
    @BindView(R.id.barber_type_btn)
    CustomTextView barberTypeBtn;
    @BindView(R.id.day_btn)
    CustomTextView dayBtn;
    @BindView(R.id.filter_btn)
    CustomTextView filterBtn;
    @BindView(R.id.location_filter_home)
    CustomTextView locationFilterHome;
    @BindView(R.id.top_view_holder)
    RelativeLayout topViewHolder;
    @BindView(R.id.map_holder_lay)
    FrameLayout mapHolderLay;
    @BindView(R.id.List_holder_view)
    LinearLayout ListHolderView;
    @BindView(R.id.show_map_btn)
    CustomTextView showMapBtn;
    @BindView(R.id.scroll_vw)
    ScrollView scrollVw;
    @BindView(R.id.horizontal_list)
    RecyclerView horizontalList;

    private BarberListAdapter listAdapter;
    private HorizontalListAdapter hListAdapter;
    private BarberPresenter presenter;

    public static FilterNotifier filterNotifier;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private RequestBarberModel modelRequest;
    int PAGE_LIMIT = 20;
    private static int PAGE_NO = 1;
    private int previousTotal = 0;
    private boolean loading = false;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.getUiSettings().setCompassEnabled(false);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e(" onMarkerClick ", " " + getId() + "  " + marker.getSnippet());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.scrollto(hListAdapter, horizontalList, marker.getSnippet(), mMap);
                    }
                }, GlobalValues.TIME_DURATIONS.SMALL);
                return true;
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermis())
                    getLastKnownlocation();
                else requestPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private boolean checkPermis() {
        int locationPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        return locationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                GlobalValues.RequestCodes.PERMISSIONS_REQUEST_);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GlobalValues.RequestCodes.PERMISSIONS_REQUEST_: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownlocation();
                } else
                    Toast.makeText(getActivity(), "Location permission required", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void getLastKnownlocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            presenter.updateUserMarker(location, mMap);
                            mPref.setUserLocation(new Gson().toJson(location));
                            mPref.setPrefrencesString(GlobalValues.KEYS.LATITUDE, "" + location.getLatitude());//save
                            mPref.setPrefrencesString(GlobalValues.KEYS.LONGITUDE, "" + location.getLongitude());//save
                            modelRequest.setLat("" + location.getLatitude());
                            modelRequest.setLong("" + location.getLongitude());
                        }
                    }
                });

    }

    /**
     * Screen Notifier Interface.
     */
    public interface FilterNotifier<R> {

        void callBackNotify(Object addressModel);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.layout_explor_screen;
    }

    @Override
    public void UpdateData(int position, Bundle bundle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentLayout(), container, false);
        ((BarberApplication) getActivity().getApplication()).getMyComponent(getActivity()).inject(this);
        presenter = new BarberPresenter(getActivity());
        presenter.attachView(this);
        ButterKnife.bind(this, rootView);
        modelRequest = new RequestBarberModel();
        if (mPref.getPrefrencesString(GlobalValues.KEYS.LATITUDE) != null && !mPref.getPrefrencesString(GlobalValues.KEYS.LATITUDE).equals("")) {
            modelRequest.setLat(mPref.getPrefrencesString(GlobalValues.KEYS.LATITUDE));
            modelRequest.setLong(mPref.getPrefrencesString(GlobalValues.KEYS.LONGITUDE));
        }
        initRecyclar();
        getBarbers();
        filterNotifier = oj -> {
            if (oj != null) {
                UpdateAddressRequestModel selectedData = (UpdateAddressRequestModel) oj;
                locationFilterHome.setText(selectedData.getCity());
                modelRequest.setLong(String.valueOf(selectedData.getLong()));
                modelRequest.setLat(String.valueOf(selectedData.getLat()));
            }
            getBarbers();
        };
        searchField.bringToFront();
        initMap();

        return rootView;
    }

    private void initMap() {
        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setListener(() -> scrollVw.requestDisallowInterceptTouchEvent(true));
        initHorizontalView();
    }

    private void initHorizontalView() {
        hListAdapter = new HorizontalListAdapter(getActivity(), new ArrayList<BarberListResponseModel.ListBean>(), ExploreFragment.class.getName(),
                (view, position, type, t) -> {
                    switch (type) {
                        case GlobalValues.ClickOperations.SHOW_DETAIL:
                            BarberListResponseModel.ListBean positionData = ((BarberListResponseModel.ListBean) t);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(GlobalValues.KEYS.BARBER_DETAIL, positionData);
                            activitySwitcher(getActivity(), BarberDetailActivity.class, bundle);
                            break;
                        case GlobalValues.ClickOperations.FAV_UNFAV:
                            if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn)) {
                                BarberListResponseModel.ListBean barberData = ((BarberListResponseModel.ListBean) t);
                                RequestFavUnfavModel setModel = new RequestFavUnfavModel();
                                if (barberData.isFavourite())
                                    setModel.setAction(false);
                                else setModel.setAction(true);

                                setModel.setBarberId(barberData.getBarberId());
                                presenter.favUnfav(NetworkConstatnts.RequestCode.API_FAV_UNFAV, setModel, false);
                            }
                            break;

                    }

                });
        LinearLayoutManager layoutManagr = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalList.setLayoutManager(layoutManagr);
        horizontalList.setAdapter(hListAdapter);
        horizontalList.bringToFront();
        recyclarViewLst.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManagr.getChildCount();
                int totalItemCount = layoutManagr.getItemCount();
                int firstVisibleItemPosition = layoutManagr.findFirstVisibleItemPosition();
                final int lastItem = firstVisibleItemPosition + visibleItemCount;
                Log.e(" onScrolled  ", "    " + lastItem + "  " + firstVisibleItemPosition + "  " + visibleItemCount);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(" onScrollStateChanged  ", "    " + layoutManagr.findLastCompletelyVisibleItemPosition());
            }
        });

    }

    private void getBarbers() {
        modelRequest.setPageNo(PAGE_NO);
        modelRequest.setRecordsPerPage(String.valueOf(PAGE_LIMIT));
        if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn))
            presenter.getBarberList(NetworkConstatnts.RequestCode.API_GET_BARBERS_AUTH, modelRequest, true);
        else
            presenter.getBarberList(NetworkConstatnts.RequestCode.API_GET_BARBERS, modelRequest, true);
    }

    private void initRecyclar() {
        listAdapter = new BarberListAdapter(getActivity(), new ArrayList<BarberListResponseModel.ListBean>(), ExploreFragment.class.getName(),
                (view, position, type, t) -> {
                    switch (type) {
                        case GlobalValues.ClickOperations.SHOW_DETAIL:
                            BarberListResponseModel.ListBean positionData = ((BarberListResponseModel.ListBean) t);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(GlobalValues.KEYS.BARBER_DETAIL, positionData);
                            activitySwitcher(getActivity(), BarberDetailActivity.class, bundle);
                            break;
                        case GlobalValues.ClickOperations.FAV_UNFAV:
                            if (mPref.getPrefrencesBoolean(GlobalValues.KEYS.isLogedIn)) {
                                BarberListResponseModel.ListBean barberData = ((BarberListResponseModel.ListBean) t);
                                RequestFavUnfavModel setModel = new RequestFavUnfavModel();
                                if (barberData.isFavourite())
                                    setModel.setAction(false);
                                else setModel.setAction(true);

                                setModel.setBarberId(barberData.getBarberId());
                                presenter.favUnfav(NetworkConstatnts.RequestCode.API_FAV_UNFAV, setModel, false);
                            }
                            break;

                    }

                });
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclarViewLst.setLayoutManager(layoutManager);
        recyclarViewLst.setAdapter(listAdapter);
        recyclarViewLst.setNestedScrollingEnabled(false);


        recyclarViewLst.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.i("Yaeye!", "end called");
//                    PAGE_NO++;
                    getBarbers();

                    // Do something

                    loading = true;
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onSuccessResponse(int serviceMode, Object o) {
        switch (serviceMode) {
            case NetworkConstatnts.RequestCode.API_GET_BARBERS_AUTH:
            case NetworkConstatnts.RequestCode.API_GET_BARBERS:
                BarberListResponseModel responseData = (BarberListResponseModel) o;
                if (responseData != null && responseData.getList() != null && responseData.getList().size() > 0) {
                    if (PAGE_NO == 1) {
                        listAdapter.updateAll(responseData.getList());
                        hListAdapter.updateAll(responseData.getList());
                    } else {
                        listAdapter.addAll(responseData.getList());
                        hListAdapter.addAll(responseData.getList());
                    }
                    presenter.updateMarkers(listAdapter.getList(), mMap);
                }
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

    @OnClick({R.id.barber_type_btn, R.id.day_btn, R.id.filter_btn, R.id.search_field, R.id.location_filter_home, R.id.relevance_filter_btn, R.id.show_map_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.barber_type_btn:
                new CommonUtils().openDialogBarberType(getActivity(), modelRequest, new OnBottomDialogItemListener() {

                    @Override
                    public void onItemClick(View view, int position, int type, Object t) {
                        modelRequest = (RequestBarberModel) t;
                        Log.e(" onItemClick ", " " + new Gson().toJson(modelRequest));
                        getBarbers();
                    }
                });
                break;
            case R.id.location_filter_home:
                activitySwitcher(getActivity(), AddressSelectionActivity.class, null);
                break;
            case R.id.day_btn:
                new FunctionalDialog().openDialogFilterDay(getActivity(), modelRequest, new OnBottomDialogItemListener() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object t) {
                        modelRequest = (RequestBarberModel) t;
                        Log.e(" onItemClick ", " " + new Gson().toJson(modelRequest));
                        getBarbers();
                    }
                });
                break;
            case R.id.show_map_btn:
                notifyMapView();
                break;
            case R.id.filter_btn:
                new FunctionalDialog().openDialogFilter(getActivity(), modelRequest, new OnBottomDialogItemListener() {

                    @Override
                    public void onItemClick(View view, int position, int type, Object t) {
                        modelRequest = (RequestBarberModel) t;
                        Log.e(" onItemClick ", " " + new Gson().toJson(modelRequest));
                        getBarbers();
                    }
                });
                break;
            case R.id.search_field:
                activitySwitcher(getActivity(), SearchActivity.class, null);
                break;
            case R.id.relevance_filter_btn:
                new FunctionalDialog().openDialogRelevnceFilter(getActivity(), modelRequest, new OnBottomDialogItemListener() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object t) {
                        modelRequest = (RequestBarberModel) t;
                        Log.e(" onItemClick ", " " + new Gson().toJson(modelRequest));
                        getBarbers();
                    }
                });
                break;
        }
    }

    private void notifyMapView() {
        if (mapHolderLay.getVisibility() == View.VISIBLE) {
            mapHolderLay.setVisibility(View.GONE);
            ListHolderView.setVisibility(View.VISIBLE);
            showMapBtn.setText(R.string.str_show_map);
            topViewHolder.bringToFront();
            searchField.bringToFront();
            showMapBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.map_white, 0, 0, 0);
        } else {
            mapHolderLay.setVisibility(View.VISIBLE);
            ListHolderView.setVisibility(View.GONE);
            showMapBtn.setText(R.string.str_show_list);
            showMapBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.paper, 0, 0, 0);
            searchField.bringToFront();
        }
        scrollVw.fullScroll(ScrollView.FOCUS_UP);
        scrollVw.smoothScrollTo(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e(" onActivityResult ", " ");


    }
}
