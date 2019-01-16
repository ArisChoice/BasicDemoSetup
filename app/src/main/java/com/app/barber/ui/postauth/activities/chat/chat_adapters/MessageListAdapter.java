package com.app.barber.ui.postauth.activities.chat.chat_adapters;

import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.barber.R;
import com.app.barber.models.TimeSlotsModel;
import com.app.barber.models.response.ChatUsersResponseModel;
import com.app.barber.util.GlobalValues;
import com.app.barber.util.ImageUtility;
import com.app.barber.util.iface.OnItemClickListener;
import com.app.barber.views.CustomTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.OptionViewHolder> {

    FragmentActivity activity;
    ArrayList<ChatUsersResponseModel.ResponseBean> cList;
    OnItemClickListener listener;

    public MessageListAdapter(FragmentActivity activity, ArrayList<ChatUsersResponseModel.ResponseBean> cList, OnItemClickListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.cList = cList;
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_message_adapter, parent, false);
        return new OptionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OptionViewHolder holder, int position) {
        ChatUsersResponseModel.ResponseBean positionDta = cList.get(position);
        holder.userImage.setImageURI(ImageUtility.getValidUrl(positionDta.getImage()));
        holder.userName.setText(positionDta.getName());
        if (positionDta.getLastMessage() != null)
            holder.userLastMessage.setText(positionDta.getLastMessage());
        else holder.userLastMessage.setText("No message");

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public void updateAll(List<ChatUsersResponseModel.ResponseBean> posts) {
        this.cList.clear();
        this.cList.addAll(posts);
        notifyDataSetChanged();
    }

    public void addItem(TimeSlotsModel posts) {
//        this.slotsList.add(0, posts);
//        notifyDataSetChanged();
    }


    public class OptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_lay)
        LinearLayout viewLay;
        @BindView(R.id.user_name)
        CustomTextView userName;
        @BindView(R.id.user_last_message)
        CustomTextView userLastMessage;
        @BindView(R.id.user_image)
        SimpleDraweeView userImage;

        public OptionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.view_lay})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_lay:
                    listener.onItemClick(v, getAdapterPosition(), GlobalValues.ClickOperations.DETAILS, cList.get(getAdapterPosition()));
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