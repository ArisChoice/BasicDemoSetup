package com.app.barber.ui.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.barber.R;
import com.app.barber.models.ChatMessageModel;
import com.app.barber.models.response.ServiceListResponseModel;
import com.app.barber.util.iface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Sunny on 3/4/2017.
 */

public class BarberReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> /*implements
        StickyHeaderAdapter<ConversationAdapter.HeaderViewHolder> */ {

    private static final String TAG = BarberReviewAdapter.class.getSimpleName();

    Activity chatWindowsActivity;
    private LayoutInflater mInflater;
    private boolean isDeleteSelected;
    ArrayList<ChatMessageModel> chatList;
    OnItemClickListener onItemClickListener;

    public BarberReviewAdapter(Activity chatWindowsActivity, ArrayList<ServiceListResponseModel.ResponseBean> chatList, OnItemClickListener onItemClickListener) {
        this.chatWindowsActivity = chatWindowsActivity;
//        this.chatList = chatList;
        this.onItemClickListener = onItemClickListener;
        mInflater = LayoutInflater.from(chatWindowsActivity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_barber_review_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int post) {
        int pos = holder.getAdapterPosition();


    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public void updateList(List<ChatMessageModel> newData) {
        chatList.clear();
        chatList.addAll(newData);
        notifyDataSetChanged();
    }

    public void add(ChatMessageModel item) {
        chatList.add(item);
        notifyDataSetChanged();
    }

    public void addList(ChatMessageModel items, int posit) {
        chatList.add(posit, items);
        notifyDataSetChanged();
    }

    public List<ChatMessageModel> getList() {
        return chatList;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

}
