package com.app.barber.ui.postauth.activities.barber.barber_adapter;

import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.barber.R;
import com.app.barber.models.TimeSlotsModel;
import com.app.barber.util.iface.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BarberPortfolioAdapter extends RecyclerView.Adapter<BarberPortfolioAdapter.OptionViewHolder> {

    FragmentActivity activity;
    String[] namelist;
    OnItemClickListener listener;
    TypedArray icons;

    public BarberPortfolioAdapter(FragmentActivity activity, String[] moreOptionModels, OnItemClickListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_portfolio_adapter, parent, false);
        return new OptionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void updateAll(List<TimeSlotsModel> posts) {
//        this.slotsList.clear();
//        this.slotsList.addAll(posts);
//        notifyDataSetChanged();
    }

    public void addItem(TimeSlotsModel posts) {
//        this.slotsList.add(0, posts);
//        notifyDataSetChanged();
    }


    public class OptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.portfolio_image)
        SimpleDraweeView portfolioImage;


        public OptionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick({R.id.portfolio_image})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.portfolio_image:
//                    listener.onItemClick(v, getAdapterPosition(), GlobalValues.ClickOperations.SHOW_DETAIL, null);
                    break;

            }
        }

        public void setData(TimeSlotsModel slotData) {
            // User Detail


        }

        private void toggleRefreshing(boolean b) {

        }


    }
}