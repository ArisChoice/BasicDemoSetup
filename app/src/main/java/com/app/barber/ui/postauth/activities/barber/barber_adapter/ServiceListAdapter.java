package com.app.barber.ui.postauth.activities.barber.barber_adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.app.barber.R;
import com.app.barber.models.BookingData;
import com.app.barber.models.TimeSlotsModel;
import com.app.barber.models.response.ServiceListResponseModel;
import com.app.barber.util.GlobalValues;
import com.app.barber.util.iface.OnItemClickListener;
import com.app.barber.views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ServiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity serviceListActivity;
    List<ServiceListResponseModel.ResponseBean> serviceList;
    OnItemClickListener onItemClickListener;


    public ServiceListAdapter(Activity serviceListActivity, List<ServiceListResponseModel.ResponseBean> serviceList, OnItemClickListener onItemClickListener) {
        this.serviceListActivity = serviceListActivity;
        this.serviceList = serviceList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_service_adapter, parent, false);
        return new ServiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceListResponseModel.ResponseBean positionData = serviceList.get(position);
        if (holder instanceof ServiceViewHolder) {
            ((ServiceViewHolder) holder).serviceName.setText(positionData.getServiceName().toString());
            ((ServiceViewHolder) holder).serviceCost.setText("€" + positionData.getPrice());
            ((ServiceViewHolder) holder).serviceTime.setText(positionData.getDuration() + "m");
            if (positionData.isSelected()) {
                ((ServiceViewHolder) holder).bookBtn.setText(R.string.remove);
                ((ServiceViewHolder) holder).bookBtn.setTextColor(serviceListActivity.getResources().getColor(R.color.color_black));
            } else {
                ((ServiceViewHolder) holder).bookBtn.setText(R.string.str_add);
                ((ServiceViewHolder) holder).bookBtn.setTextColor(serviceListActivity.getResources().getColor(R.color.color_app_blue));
            }
        }

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void updateAll(List<ServiceListResponseModel.ResponseBean> serviceList) {
        this.serviceList.clear();
        this.serviceList.addAll(serviceList);
        notifyDataSetChanged();
    }

    public void addItem(ServiceListResponseModel.ResponseBean posts) {
        this.serviceList.add(0, posts);
        notifyDataSetChanged();
    }

    public void updateLikeData(int selectedPosition, String status, String postId) {

    }

    public void remove(int positio) {
        serviceList.remove(positio);
        notifyDataSetChanged();
    }

    /**
     * check service added or not
     */
    public boolean serviceSelected() {
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * get booking data.
     */
    public BookingData getBookingData() {
        BookingData bookingData = null;
        int bookedItems = 0;
        int totalAmount = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).isSelected()) {
                bookingData = new BookingData();
                bookedItems++;
                totalAmount = (totalAmount + serviceList.get(i).getPrice());
                builder.append(serviceList.get(i).getId() + ",");
                bookingData.setTotalServices(bookedItems);
                bookingData.setBookedServicesId(builder.toString().substring(0, builder.toString().length() - 1).toString());
                bookingData.setTotalAmount(totalAmount);
            }
        }
        return bookingData;
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_service_detail)
        ImageView imgServiceDetail;

        @BindView(R.id.service_name)
        CustomTextView serviceName;
        @BindView(R.id.service_time)
        CustomTextView serviceTime;
        @BindView(R.id.service_cost)
        CustomTextView serviceCost;
        @BindView(R.id.book_btn)
        CustomTextView bookBtn;


        public ServiceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick({R.id.img_service_detail, R.id.book_btn})
        public void onLCick(View v) {
            switch (v.getId()) {
                case R.id.img_service_detail:
                    onItemClickListener.onItemClick(v, getAdapterPosition(), GlobalValues.ClickOperations.SERVICE_DETAIL, null);
                    break;
                case R.id.book_btn:
                    updateSelection(getAdapterPosition());
                    onItemClickListener.onItemClick(v, getAdapterPosition(), GlobalValues.ClickOperations.SERVICE_BOOK, null);
                    break;
            }
        }

        public void setData(TimeSlotsModel slotData) {
            // User Detail
        }

        private void toggleRefreshing(boolean b) {
        }


    }

    private void updateSelection(int adapterPosition) {
        if (serviceList.get(adapterPosition).isSelected()) {
            serviceList.get(adapterPosition).setSelected(false);
        } else serviceList.get(adapterPosition).setSelected(true);
        notifyDataSetChanged();
    }
}