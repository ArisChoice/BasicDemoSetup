package com.app.barber.ui.postauth.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.barber.R;
import com.app.barber.core.BarberApplication;
import com.app.barber.core.BaseFragment;
import com.app.barber.models.request.RequestMyAppointmentModel;
import com.app.barber.models.request.UpdateBookingRequestModel;
import com.app.barber.models.response.MyBookingsResponseMOdel;
import com.app.barber.net.NetworkConstatnts;
import com.app.barber.ui.postauth.activities.barber.BarberDetailActivity;
import com.app.barber.ui.postauth.activities.chat.ChatWorkActivity;
import com.app.barber.ui.postauth.activities.home.home_adapter.AppointmentsListAdapter;
import com.app.barber.ui.postauth.activities.barber.AppointmentDetailActivity;
import com.app.barber.ui.postauth.activities.home.homemvp.HomeAuthMVPView;
import com.app.barber.ui.postauth.activities.home.homemvp.HomeAuthPresenter;
import com.app.barber.util.FunctionalDialog;
import com.app.barber.util.GlobalValues;
import com.app.barber.util.iface.OnBottomDialogItemListener;
import com.app.barber.util.iface.OnItemClickListener;
import com.app.barber.views.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.barber.core.BaseActivity.activitySwitcher;

/**
 * Created by harish on 25/10/18.
 */

public class AppointsmentsFragment extends BaseFragment implements HomeAuthMVPView {
    //    @BindView(R.id.appointments_tabs)
//    TabLayout appointmentsTabs;

    @BindView(R.id.no_list_data_text)
    CustomTextView noListDataText;
    @BindView(R.id.first_header_txt)
    CustomTextView firstHeaderTxt;
    @BindView(R.id.second_header_txt)
    CustomTextView secondHeaderTxt;
    @BindView(R.id.skip_header_txt)
    CustomTextView skipHeaderTxt;
    @BindView(R.id.recyclar_view_lst)
    RecyclerView recyclarViewLst;
    private AppointmentsListAdapter appointmnetAdapter;
    private HomeAuthPresenter presenter;


    @Override
    protected int getFragmentLayout() {
        return R.layout.layout_appointments_screen;
    }

    @Override
    public void UpdateData(int position, Bundle bundle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((BarberApplication) getActivity().getApplication()).getMyComponent(getActivity()).inject(this);
        presenter = new HomeAuthPresenter(getActivity());
        presenter.attachView(this);
        View rootView = inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, rootView);
        firstHeaderTxt.setText(R.string.title_appointments);
        secondHeaderTxt.setText("0 " + getActivity().getString(R.string.str_appointments_));
//        initTabs();
        initRecyclar();
        getMyAppointments();
        return rootView;
    }

    private void getMyAppointments() {
        RequestMyAppointmentModel mRequest = new RequestMyAppointmentModel();
        mRequest.setPageNo(1);
        mRequest.setRecordsPerPage(20);
        presenter.getMyBookings(NetworkConstatnts.RequestCode.MY_ALL_BOOKINGS, mRequest, false);
    }

    private void initRecyclar() {
        appointmnetAdapter = new AppointmentsListAdapter(getActivity(), new ArrayList<>(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, int type, Object t) {
                        switch (type) {
                            case GlobalValues.ClickOperations.APAPTER_BOTTOM_DIALOG_CLICK:
                                new FunctionalDialog().openDialogAppointment(getActivity(), (MyBookingsResponseMOdel.ListBean) t, new OnBottomDialogItemListener() {
                                    @Override
                                    public void onItemClick(View view, int position, int type, Object t) {
                                        MyBookingsResponseMOdel.ListBean positionData = (MyBookingsResponseMOdel.ListBean) t;
                                        switch (type) {
                                            case GlobalValues.RequestCodes.MESSAGE:
                                                if (positionData.getQBdialogId() != null) {
                                                    Intent intent = new Intent(getActivity(), ChatWorkActivity.class);
                                                    intent.putExtra(GlobalValues.KEYS.EXTRA_DIALOG_ID, positionData.getQBdialogId());
                                                    intent.putExtra(GlobalValues.KEYS.OTHER_IMAGE, positionData.getProfileImage());
                                                    startActivity(intent);
                                                }
                                                break;
                                            case GlobalValues.RequestCodes.CALL:
                                                if (positionData.getPhone() != null)
                                                    callInit(positionData.getPhone());
                                                break;
                                            case GlobalValues.RequestCodes.CANCEL:
                                                UpdateBookingRequestModel uModel = new UpdateBookingRequestModel(positionData.getId(), true, positionData.getBookingType());
                                                presenter.updateBookingRequest(NetworkConstatnts.RequestCode.API_UPDATE_BOOKING_STATUS, uModel, true);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        positionData.setCanceled(true);
                                                        appointmnetAdapter.notifyDataSetChanged();
                                                    }
                                                }, GlobalValues.TIME_DURATIONS.SMALL);
                                                break;
                                        }
                                    }
                                });
                                break;
                        }
                    }
                });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclarViewLst.setLayoutManager(layoutManager);
        recyclarViewLst.setAdapter(appointmnetAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSuccessResponse(int serviceMode, Object o) {
        switch (serviceMode) {
            case NetworkConstatnts.RequestCode.MY_ALL_BOOKINGS:
                MyBookingsResponseMOdel responseData = (MyBookingsResponseMOdel) o;
                if (responseData.getList() != null && responseData.getList().size() > 0) {
                    appointmnetAdapter.updateAll(responseData.getList());
                    appointmnetAdapter.notifiBookigStatusHeaders();
                    secondHeaderTxt.setText(appointmnetAdapter.getItemCount() + " " + getActivity().getString(R.string.str_appointments_));
                    noListDataText.setVisibility(View.GONE);
                    recyclarViewLst.setVisibility(View.VISIBLE);
                } else {
                    noListDataText.setText(R.string.str_no_bookings);
                    noListDataText.setVisibility(View.VISIBLE);
                    recyclarViewLst.setVisibility(View.GONE);
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
}
