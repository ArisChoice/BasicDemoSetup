package com.app.barber.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.barber.R;
import com.app.barber.core.BarberApplication;
import com.app.barber.models.HomeFiltersModel;
import com.app.barber.models.ModelDay;
import com.app.barber.models.request.RequestBarberModel;
import com.app.barber.models.response.MyBookingsResponseMOdel;
import com.app.barber.models.response.SpecialisationResponseModel;
import com.app.barber.ui.adapters.filtersadapter.HomeFilterAdapter;
import com.app.barber.ui.postauth.activities.barber.barber_adapter.SpecialisationAdapter;
import com.app.barber.ui.weekcontroler.WeekViewAdapter;
import com.app.barber.util.iface.OnBottomDialogItemListener;
import com.app.barber.util.iface.OnItemClickListener;
import com.app.barber.views.CustomTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;

/**
 * Created by harish on 18/12/18.
 */

public class FunctionalDialog {

    private static final String TAG = FunctionalDialog.class.getName();

    /**
     * Dialog appointment received.
     */


    public void openDialogAppointment(Activity activity, Object obj, final OnBottomDialogItemListener listener) {
        boolean isBookingCompleted;
        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_appointmnet_request_dialog, null);
        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        final TextView customerName = child.findViewById(R.id._about_customer_name);
        final TextView customerNumber = child.findViewById(R.id._about_customer_number);
        final TextView timeSlot = child.findViewById(R.id.booking_time_slot);
        final TextView bookingDate = child.findViewById(R.id.booking_date);
        final TextView bookingDay = child.findViewById(R.id.booking_day);
        final TextView serviceDuration = child.findViewById(R.id.booking_duration);
        final TextView bookingServices = child.findViewById(R.id.booking_services);
        final TextView bookingPrice = child.findViewById(R.id.booking_price);
        final SimpleDraweeView customerImage = child.findViewById(R.id._about_customer_image);
        final TextView address = child.findViewById(R.id.booking_address);
        final TextView reorder = child.findViewById(R.id.complete_btn);
        final TextView cancelBtn = child.findViewById(R.id.cancel_btn);
        final TextView statusText = child.findViewById(R.id.status_Text);
        final TextView appointmentStatus = child.findViewById(R.id.appointment_status);
        final ImageView messageBtn = child.findViewById(R.id.message_btn);
        final ImageView callBtn = child.findViewById(R.id.call_btn);
        MyBookingsResponseMOdel.ListBean currentData = (MyBookingsResponseMOdel.ListBean) obj;
        if (currentData != null) {
            customerName.setText(currentData.getName());
            customerNumber.setText(currentData.getPhone());
            timeSlot.setText(currentData.getTimingSlot());
//            bookingDate.setText(CustomDate.formatThis(GlobalValues.DATE_FORMAT.STANDARD, currentData.getDateString()));
            customerImage.setImageURI(CommonUtils.getValidUrl(currentData.getProfileImage()));
            bookingServices.setText(currentData.getServiceId());
            bookingPrice.setText("$" + currentData.getAmount());
            bookingDate.setText(CustomDate.getCurrentFormat(activity, currentData.getDateString(), "MM/dd/yyyy", "EEEE,MMMM YY"));
            bookingDay.setText(CustomDate.getCurrentFormat(activity, currentData.getDateString(), "MM/dd/yyyy", "EEEE"));
            try {
                if (currentData.getAddress() != null) {
                    address.setText(currentData.getAddress().getAddressLine1());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                serviceDuration.setText(currentData.getDuration() + " min");

            } catch (Exception e) {
                serviceDuration.setText("0 min");
            }
            Log.e("appointmentStatus ", " " + currentData.getAppointmentStatus().split("\\s+")[0]);
            appointmentStatus.setVisibility(View.VISIBLE);
            appointmentStatus.setText(currentData.getAppointmentStatus().split("\\s+")[0]);
            if (currentData.isCompleted()) {
                cancelBtn.setVisibility(View.GONE);
                statusText.setText(R.string.str_completed);
                statusText.setVisibility(View.VISIBLE);
                statusText.setBackgroundResource(R.drawable.rectangle_green_drawable);
                statusText.setTextColor(activity.getResources().getColor(R.color.color_white));
            }
            if (appointmentStatus.getText().toString().equals("Past")) {
                cancelBtn.setVisibility(View.GONE);

            }
        }
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (!currentData.isCanceled())
                    listener.onItemClick(child, 0, GlobalValues.RequestCodes.CANCEL, currentData);//false
                else if (currentData.isCanceled())
                    new CommonUtils().ShowToast("Booking already cancelled");
            }
        });
        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonUtils().ShowToast("Working on this feature");
                mBottomSheetDialog.dismiss();
//                listener.onItemClick(child, 0, 1, currentData);//true
            }
        });
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(child, 0, GlobalValues.RequestCodes.MESSAGE, currentData);//true
            }
        });
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(child, 0, GlobalValues.RequestCodes.CALL, currentData);//true
            }
        });
    }

    /**
     * Dialog appointment received.
     */
    public void openDialogRelevnceFilter(Activity activity, Object obj, final OnBottomDialogItemListener listener) {

        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_relevnce_filter_dialog, null);
        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        RecyclerView recyclerViewList = child.findViewById(R.id.recyclar_view_lst);
        RequestBarberModel rModl = (RequestBarberModel) obj;

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(layoutManager);
        HomeFilterAdapter filterAdapter = new HomeFilterAdapter(activity, getAvailableFilters(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int positio, int type, Object o) {

                rModl.getFilter().setSoryBy(((HomeFiltersModel) o).getFilterKey());
                listener.onItemClick(child, 0, 1, rModl);//true
                mBottomSheetDialog.dismiss();
            }
        });

        recyclerViewList.setAdapter(filterAdapter);
        if (rModl.getFilter() == null) {
            RequestBarberModel rModel = new RequestBarberModel();
            RequestBarberModel.Filter fModel = rModel.new Filter();
            fModel.setDate(new CustomDate().getCurrentMonth(activity, GlobalValues.DateFormats.DEFAULT_FORMAT_DATE));//default
            fModel.setDayAvailability(activity.getString(R.string.str_any_time));//default
            rModl.setFilter(fModel);
        } else {
            if (rModl.getFilter().getSoryBy() != null) {
                filterAdapter.setSelected(rModl.getFilter().getSoryBy());
            }
        }
    }


    private ArrayList<HomeFiltersModel> getAvailableFilters() {
        ArrayList<HomeFiltersModel> filterList = new ArrayList<>();
        String[] name = BarberApplication.getInstance().getResources().getStringArray(R.array.filter_names);//filter names
        String[] namekey = BarberApplication.getInstance().getResources().getStringArray(R.array.filter_names_key);//filter keys

        for (int i = 0; i < name.length; i++) {
            HomeFiltersModel fModel = new HomeFiltersModel();
            fModel.setFilterName(name[i]);
            fModel.setSelected(false);
            fModel.setFilterKey(namekey[i]);
            filterList.add(fModel);
        }
        return filterList;
    }

    /**
     * Dialog booking type.
     */
    int typeSelected;

    public void openDialogBookingType(Activity activity, Object obj, final OnBottomDialogItemListener listener) {

        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_booking_type_dialog, null);
        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        CustomTextView barberType = child.findViewById(R.id.type_barber);
        CustomTextView barberCallout = child.findViewById(R.id.type_callout);
        CustomTextView continueBtn = child.findViewById(R.id.continue_btn);


        barberType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barberType.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.barber_icon_active, 0, 0);
                barberCallout.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.callout_barber_icon_normal, 0, 0);
                barberType.setTextColor(activity.getResources().getColor(R.color.color_app_blue));
                barberCallout.setTextColor(activity.getResources().getColor(R.color.color_grey));

                typeSelected = GlobalValues.BARBER_TYPES.BARBER;
                continueBtn.setVisibility(View.VISIBLE);

            }
        });
        barberCallout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barberType.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.barber_icon_normal, 0, 0);
                barberCallout.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.callout_barber_icon_active, 0, 0);
                barberType.setTextColor(activity.getResources().getColor(R.color.color_grey));
                barberCallout.setTextColor(activity.getResources().getColor(R.color.color_app_blue));

                typeSelected = GlobalValues.BARBER_TYPES.CALLOUT_BARBER;
                continueBtn.setVisibility(View.VISIBLE);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                listener.onItemClick(child, 0, typeSelected, null);
            }
        });
    }

    /**
     * Dialog appointment received.
     */
    String paymentType = null;

    public void openDialogFilter(Activity activity, Object obj, final OnBottomDialogItemListener listener) {

        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_filter_dialog, null);
        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        CheckBox superBarberChkbx = child.findViewById(R.id.chkboc_filter_super_only_barber);
        CheckBox isTraneechkbox = child.findViewById(R.id.chkboc_filter_trainee_only_barber);
        CustomTextView applyBtn = child.findViewById(R.id.apply_btn);
        CustomTextView resetBtn = child.findViewById(R.id.reset_filter_btn);
        RadioButton cardRbtn = child.findViewById(R.id.radio_btn_Card);
        RadioButton cashRbtn = child.findViewById(R.id.radio_btn_cash);
        // Seek bar for which we will set text color in code
        RangeSeekBar rangeSeekBarTextColorWithCode = (RangeSeekBar) child.findViewById(R.id.priceRange);
        rangeSeekBarTextColorWithCode.setTextAboveThumbsColorResource(R.color.color_app_blue);

        RangeSeekBar priceRange = child.findViewById(R.id.priceRange);
        RecyclerView stylerecyclar = child.findViewById(R.id.style_recyclar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        stylerecyclar.setLayoutManager(layoutManager);
        String styleData = new PreferenceManager().getSpecialisation();
        SpecialisationResponseModel styleModel = new Gson().fromJson(styleData, SpecialisationResponseModel.class);
        SpecialisationAdapter specAdapter = null;
        if (styleModel != null && styleModel.getResponse() != null && styleModel.getResponse().size() > 0) {
            specAdapter = new SpecialisationAdapter(activity, styleModel.getResponse(),
                    new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int type, Object o) {
//                    Log.e("Onitem click ", " " + specAdapter.getselected());

                        }
                    });
            stylerecyclar.setAdapter(specAdapter);
        }
        RequestBarberModel rModl = (RequestBarberModel) obj;
        if (rModl.getFilter() == null) {
            RequestBarberModel rModel = new RequestBarberModel();
            RequestBarberModel.Filter fModel = rModel.new Filter();
            fModel.setDate(new CustomDate().getCurrentMonth(activity, GlobalValues.DateFormats.DEFAULT_FORMAT_DATE));//default
            fModel.setDayAvailability(activity.getString(R.string.str_any_time));//default
            rModl.setFilter(fModel);
        } else {
            isTraneechkbox.setChecked(rModl.getFilter().isTrainee());
            if (rModl.getFilter().getStyleType() != null && !rModl.getFilter().getStyleType().equals("") && specAdapter != null)
                specAdapter.setSelected(rModl.getFilter().getStyleType());
            if (rModl.getFilter().getPaymentType() != null && !rModl.getFilter().getPaymentType().equals("")) {
                if (rModl.getFilter().getPaymentType().contains("1")) {
                    cardRbtn.setChecked(true);
                } else if (rModl.getFilter().getPaymentType().contains("2")) {
                    cashRbtn.setChecked(true);
                } else if (rModl.getFilter().getPaymentType().contains("3")) {
                    cardRbtn.setChecked(true);
                    cashRbtn.setChecked(true);
                }
            }
            if (rModl.getFilter().getMinPrice() != null) {
                priceRange.setSelectedMinValue(Integer.parseInt(rModl.getFilter().getMinPrice()));
            }
            if (rModl.getFilter().getMaxPrice() != null) {
                priceRange.setSelectedMaxValue(Integer.parseInt(rModl.getFilter().getMaxPrice()));
            }
        }
        SpecialisationAdapter finalSpecAdapter = specAdapter;
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                rModl.setFilter(null);
                listener.onItemClick(child, 0, 0, rModl);

            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                rModl.getFilter().setMinPrice("" + priceRange.getSelectedMinValue());
                rModl.getFilter().setMaxPrice("" + priceRange.getSelectedMaxValue());
                rModl.getFilter().setTrainee(isTraneechkbox.isChecked());
                if (finalSpecAdapter != null && finalSpecAdapter.getItemCount() > 0)
                    rModl.getFilter().setStyleType(finalSpecAdapter.getselected());

                if (cardRbtn.isChecked()) {
                    paymentType = "1";
                    rModl.getFilter().setPaymentType(paymentType);
                } else if (cashRbtn.isChecked()) {
                    paymentType = "2";
                    rModl.getFilter().setPaymentType(paymentType);
                } else if (cardRbtn.isChecked() && cashRbtn.isChecked()) {
                    paymentType = "3";
                    rModl.getFilter().setPaymentType(paymentType);
                }


                listener.onItemClick(child, 0, 0, rModl);
            }
        });
    }

    /**
     * Dialog appointment received.
     */
    public void openDialogFilterDay(Activity activity, Object obj, final OnBottomDialogItemListener listener) {

        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_day_filter_dialog, null);
        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        CustomTextView cnFrmBtn = child.findViewById(R.id.btn_cnfrm);
        CustomTextView anyTime = child.findViewById(R.id.btn_anytime);
        CustomTextView morningTime = child.findViewById(R.id.btn_morning);
        CustomTextView afterNoon = child.findViewById(R.id.btn_afternoon);
        RecyclerView weekRecyclar = child.findViewById(R.id.weekViewRecyclar);
        RequestBarberModel rModl = (RequestBarberModel) obj;

        WeekViewAdapter weekAdapter = new WeekViewAdapter(activity, new ArrayList<ModelDay>(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type, Object o) {
                Log.e(" onItemClick ", "  -- " + position);
                ModelDay positionData = (ModelDay) o;
                rModl.getFilter().setDate(positionData.getDay());
            }
        });
        LinearLayoutManager lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        weekRecyclar.setLayoutManager(lManager);
        weekRecyclar.setAdapter(weekAdapter);
        weekAdapter.setCurrentWeek(14);//next 14 days visible
        if (rModl.getFilter() == null) {
            RequestBarberModel rModel = new RequestBarberModel();
            RequestBarberModel.Filter fModel = rModel.new Filter();
            fModel.setDate(new CustomDate().getCurrentMonth(activity, GlobalValues.DateFormats.DEFAULT_FORMAT_DATE));//default
            fModel.setDayAvailability(activity.getString(R.string.str_any_time));//default
            rModl.setFilter(fModel);
        } else {
            if (rModl.getFilter().getDate() != null)
                weekAdapter.setselected(rModl.getFilter().getDate());

            if (rModl.getFilter().getDayAvailability() != null)
                if (rModl.getFilter().getDayAvailability().equals(activity.getString(R.string.str_any_time))) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            anyTime.performClick();
                        }
                    }, GlobalValues.TIME_DURATIONS.SMALL);

                } else if (rModl.getFilter().getDayAvailability().equals(activity.getString(R.string.str_morning))) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            morningTime.performClick();
                        }
                    }, GlobalValues.TIME_DURATIONS.SMALL);

                } else if (rModl.getFilter().getDayAvailability().equals(activity.getString(R.string.str_afternoon))) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            afterNoon.performClick();
                        }
                    }, GlobalValues.TIME_DURATIONS.SMALL);

                } else {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            anyTime.performClick();
                        }
                    }, GlobalValues.TIME_DURATIONS.SMALL);

                }
        }
        anyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyTime.setBackgroundResource(R.drawable.rectangle_blue_drawable);
                morningTime.setBackgroundResource(R.drawable.rectangle_grey_border);
                afterNoon.setBackgroundResource(R.drawable.rectangle_grey_border);
                anyTime.setTextColor(activity.getResources().getColor(R.color.color_white));
                morningTime.setTextColor(activity.getResources().getColor(R.color.color_dark_grey));
                afterNoon.setTextColor(activity.getResources().getColor(R.color.color_dark_grey));
                rModl.getFilter().setDayAvailability(activity.getString(R.string.str_any_time));
            }
        });
        morningTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyTime.setBackgroundResource(R.drawable.rectangle_grey_border);
                morningTime.setBackgroundResource(R.drawable.rectangle_blue_drawable);
                afterNoon.setBackgroundResource(R.drawable.rectangle_grey_border);
                anyTime.setTextColor(activity.getResources().getColor(R.color.color_dark_grey));
                morningTime.setTextColor(activity.getResources().getColor(R.color.color_white));
                afterNoon.setTextColor(activity.getResources().getColor(R.color.color_dark_grey));
                rModl.getFilter().setDayAvailability(activity.getString(R.string.str_morning));
            }
        });
        afterNoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anyTime.setBackgroundResource(R.drawable.rectangle_grey_border);
                morningTime.setBackgroundResource(R.drawable.rectangle_grey_border);
                afterNoon.setBackgroundResource(R.drawable.rectangle_blue_drawable);
                anyTime.setTextColor(activity.getResources().getColor(R.color.color_dark_grey));
                morningTime.setTextColor(activity.getResources().getColor(R.color.color_dark_grey));
                afterNoon.setTextColor(activity.getResources().getColor(R.color.color_white));
                rModl.getFilter().setDayAvailability(activity.getString(R.string.str_evening));
            }
        });


        cnFrmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.dismiss();
                rModl.getFilter().setDate(weekAdapter.getSelectedDate());
                listener.onItemClick(child, 0, 0, rModl);
            }
        });
    }
}
