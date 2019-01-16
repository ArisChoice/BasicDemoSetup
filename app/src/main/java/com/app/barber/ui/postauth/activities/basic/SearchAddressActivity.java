package com.app.barber.ui.postauth.activities.basic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.barber.R;
import com.app.barber.core.BaseActivity;
import com.app.barber.ui.adapters.PlaceAutocompleteAdapter;
import com.app.barber.util.GlobalValues;
import com.app.barber.views.CustomEditText;
import com.app.barber.views.CustomTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harish on 31/10/18.
 */

public class SearchAddressActivity extends BaseActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    CustomTextView txtTitleToolbar;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.recyclar_view_lst)
    RecyclerView recyclarViewLst;
    @BindView(R.id.no_list_data_text)
    CustomTextView noListDataText;
    @BindView(R.id.edtxt_search)
    CustomEditText edtxtSearch;
    @BindView(R.id.enter_manually_btn)
    CustomTextView enterManuallyBtn;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    //    List<SavedAddress> mSavedAddressList;
//    PlaceSavedAdapter mSavedAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.layout_search_address;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backToolbar.setImageResource(R.drawable.close_1);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        initView();
    }

    private void initView() {
        recyclarViewLst.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(SearchAddressActivity.this);
        recyclarViewLst.setLayoutManager(llm);
        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, null);
        recyclarViewLst.setAdapter(mAdapter);
        edtxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
//                    mClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        recyclarViewLst.setAdapter(mAdapter);
                    }
                } else {
//                    mClear.setVisibility(View.GONE);
                  /*  if (mSavedAdapter != null && mSavedAddressList.size() > 0) {
                        recyclarViewLst.setAdapter(mSavedAdapter);
                    }*/
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mAdapter.clearList();
                }
            }


        });
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick({R.id.back_toolbar, R.id.txt_title_toolbar, R.id.enter_manually_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.txt_title_toolbar:
                break;
            case R.id.enter_manually_btn:
                activitySwitcher(SearchAddressActivity.this, AddressActivity.class, null);
                break;
        }
    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            Log.e(" onResult ", " " + places.get(0).getAddress());
                            Log.e(" onResult ", " " + places.get(0).getName());
                            Log.e(" onResult ", " " + places.get(0).getLocale());
                            Log.e(" onResult ", " " + places.get(0).getAttributions());
                            //Do the things here on Click.....
                         /*   Intent data = new Intent();
                            data.putExtra(GlobalValues.KEYS.PLACE_DETAIL, places.get(0).getAddress());
                            setResult(RESULT_OK, data);
                            finish();*/
                            Bundle bundle = new Bundle();
                            bundle.putString(GlobalValues.KEYS.PLACE_DETAIL, places.get(0).getAddress().toString());
                            activitySwitcher(SearchAddressActivity.this, AddressActivity.class, bundle);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }
}

