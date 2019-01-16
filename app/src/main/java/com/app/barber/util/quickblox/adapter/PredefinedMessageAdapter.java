package com.app.barber.util.quickblox.adapter;/*
package com.app.barber.util.quickblox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.app.barber.R;

import butterknife.BindView;
import butterknife.ButterKnife;

*/
/**
 * Created by varinder on 26/12/18.
 *//*


public class PredefinedMessageAdapter extends RecyclerView.Adapter<PredefinedMessageAdapter.PredefinedMessageViewHolder>{

    private Context context;
    private ItemOnClickListener clickListener;
    private String[] list;

    public PredefinedMessageAdapter(Context context, ItemOnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        list = context.getResources().getStringArray(R.array.pre_message_array);
    }

    @NonNull
    @Override
    public PredefinedMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pre_defined_message_list_item, parent, false);
        return new PredefinedMessageViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PredefinedMessageViewHolder holder, int position) {
        holder.txtPredefinedMessage.setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class PredefinedMessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtPredefinedMessage)
        TextView txtPredefinedMessage;

        public PredefinedMessageViewHolder(View itemView, final ItemOnClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
*/
