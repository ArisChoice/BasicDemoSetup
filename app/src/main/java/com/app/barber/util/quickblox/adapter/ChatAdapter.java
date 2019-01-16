package com.app.barber.util.quickblox.adapter;/*
package com.app.barber.util.quickblox.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.barber.R;
import com.app.barber.net.NetworkConstatnts;
import com.app.barber.util.quickblox.utils.ResourceUtils;
import com.app.barber.util.quickblox.utils.TimeUtils;
import com.app.barber.util.quickblox.utils.chat.ChatHelper;
import com.app.barber.util.quickblox.utils.qb.PaginationHistoryListener;
import com.app.barber.util.quickblox.utils.qb.QbUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.helper.CollectionsUtil;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

*
 * Created by varinder on 12/10/18.



public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
 {

    private static final String TAG = ChatAdapter.class.getSimpleName();
    private final QBChatDialog chatDialog;
    private PaginationHistoryListener paginationListener;
    private int previousGetCount = 0;
    Context context;
    List<QBChatMessage> chatMessages;

    public ChatAdapter(Context context, QBChatDialog chatDialog, List<QBChatMessage> chatMessages) {
        this.chatDialog = chatDialog;
        this.context = context;
        this.chatMessages = chatMessages;
    }

    public void addList(List<QBChatMessage> items) {
        this.chatMessages.clear();
        this.chatMessages.addAll(items);
        this.notifyDataSetChanged();
    }

    public void addToList(List<QBChatMessage> items) {
        chatMessages.addAll(0, items);
        notifyItemRangeInserted(0, items.size());
    }

    public List<QBChatMessage> getList() {
        return this.chatMessages;
    }

    public void add(QBChatMessage item) {
        this.chatMessages.add(item);
        this.notifyItemInserted(chatMessages.size() - 1);
    }

    private String getSenderName(QBChatMessage chatMessage) {
        QBUser sender = QbUsersHolder.getInstance().getUserById(chatMessage.getSenderId());
        return sender.getFullName();
    }

    private void readMessage(QBChatMessage chatMessage) {
        try {
            chatDialog.readMessage(chatMessage);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private boolean isRead(QBChatMessage chatMessage) {
        Integer currentUserId = ChatHelper.getCurrentUser().getId();
        return !CollectionsUtil.isEmpty(chatMessage.getReadIds()) && chatMessage.getReadIds().contains(currentUserId);
    }

    public void setPaginationHistoryListener(PaginationHistoryListener paginationListener) {
        this.paginationListener = paginationListener;
    }

    private void downloadMore(int position) {
        if (position == 0) {
            if (getItemCount() != previousGetCount) {
                paginationListener.downloadMore();
                previousGetCount = getItemCount();
            }
        }
    }

    @Override
    public long getHeaderId(int position) {
        QBChatMessage chatMessage = chatMessages.get(position);
        return TimeUtils.getDateAsHeaderId(chatMessage.getDateSent() * 1000);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_chat_message_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        View view = holder.itemView;
        TextView dateTextView = view.findViewById(R.id.header_date_textview);

        QBChatMessage chatMessage = chatMessages.get(position);
        dateTextView.setText(TimeUtils.getDate(chatMessage.getDateSent() * 1000));

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) dateTextView.getLayoutParams();
        if (position == 0) {
            lp.topMargin = ResourceUtils.getDimen(R.dimen.chat_date_header_top_margin);
        } else {
            lp.topMargin = 0;
        }
        dateTextView.setLayoutParams(lp);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_right, parent, false);
            return new MessageRightViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_left, parent, false);
            return new MessageLeftViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageRightViewHolder) {
            MessageRightViewHolder messageRightViewHolder = (MessageRightViewHolder) holder;
            messageRightViewHolder.msg.setText(chatMessages.get(position).getBody());
//            Glide.with(context)
//                    .load(getImageUrl(getPref().getUserData().image))
//                    .apply(new RequestOptions().placeholder(R.drawable.passanger_blue))
//                    .into(messageRightViewHolder.imgChatRight);
        } else {
            MessageLeftViewHolder messageLeftViewHolder = (MessageLeftViewHolder) holder;
            messageLeftViewHolder.msg.setText(chatMessages.get(position).getBody());
//            Glide.with(context)
//                    .load(getImageUrl(getPref().getPrefrencesString("userImage")))
//                    .apply(new RequestOptions().placeholder(R.drawable.passanger_blue))
//                    .into(messageLeftViewHolder.imgChatLeft);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        QBChatMessage chatMessage = chatMessages.get(position);
        if (this.hasAttachments(chatMessage)) {
            QBAttachment attachment = this.getQBAttach(position);
            return !"photo".equalsIgnoreCase(attachment.getType()) &&
                    !"image".equalsIgnoreCase(attachment.getType()) ?
                    ("location".equalsIgnoreCase(attachment.getType()) ?
                            this.getLocationView(chatMessage) : ("audio".equalsIgnoreCase(attachment.getType()) ?
                            (this.isIncoming(chatMessage) ? 6 : 5) : ("video".equalsIgnoreCase(attachment.getType()) ?
                            (this.isIncoming(chatMessage) ? 8 : 7) : this.customViewType(position)))) : (this.isIncoming(chatMessage) ? 4 : 3);
        } else {
            return this.isIncoming(chatMessage) ? 2 : 1;
        }
    }

    protected boolean isIncoming(QBChatMessage chatMessage) {
        QBUser currentUser = QBChatService.getInstance().getUser();
        return chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(currentUser.getId());
    }

    protected boolean hasAttachments(QBChatMessage chatMessage) {
        Collection<QBAttachment> attachments = chatMessage.getAttachments();
        return attachments != null && !attachments.isEmpty();
    }

    protected QBAttachment getQBAttach(int position) {
        QBChatMessage chatMessage = chatMessages.get(position);
        return (QBAttachment) chatMessage.getAttachments().iterator().next();
    }

    protected int getLocationView(QBChatMessage chatMessage) {
        return this.isIncoming(chatMessage) ? 4 : 3;
    }

    protected int customViewType(int position) {
        return -1;
    }

    public class MessageLeftViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        CircleImageView imgChatLeft;

        public MessageLeftViewHolder(View itemView) {
            super(itemView);
//            msg = itemView.findViewById(R.id.txt_msgLeft);
//            imgChatLeft = itemView.findViewById(R.id.imgChatLeft);
        }

        public void setHolder(MessageLeftViewHolder holder, String msg) {
            holder.msg.setText(msg);
        }
    }

    public class MessageRightViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        CircleImageView imgChatRight;


        public MessageRightViewHolder(View itemView) {
            super(itemView);
//            msg=itemView.findViewById(R.id.txt_msgRight);
//            imgChatRight = itemView.findViewById(R.id.imgChatRight);
        }

        public void setHolder(MessageRightViewHolder holder, String msg) {
            holder.msg.setText(msg);
        }

    }

    public PreferenceManager getPref() {
        return PreferenceManager.getInstance(context);
    }

    public String getImageUrl(String url) {
        return NetworkConstatnts.URL.IMAGE_URL + url;
    }


}
*/
