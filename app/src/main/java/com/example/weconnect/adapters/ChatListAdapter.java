package com.example.weconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.example.weconnect.R;

import java.util.List;

/**
 * ChatListAdapter is aused to display the list of users. It helps to organize the users in recyclerView.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<User> chats;
    private Context context;

    /**
     * Constructor which takes users List as parameter for binding in adapter.
     * @param context is a object of Context.
     * @param chats is a list of users used in this adapter.
     */
    public ChatListAdapter(List<User> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    /**
     * Method to update the user list inorder to modify the list during search
     * @param users is new list of users used in this adapter.
     */
    public void setUsersList(List<User> users){
        chats = users;
    }

    /**
     * View holder class for displaying each chat in conversation list.
     */
    class ChatViewHolder extends RecyclerView.ViewHolder {
        CometChatAvatar chatIcon;
        TextView chatNameTextView;
        RelativeLayout containerLayout;

        /**
         * @brief Constructor for View Holder.
         * @param itemView : Represents the view.
         */
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatIcon = itemView.findViewById(R.id.av_user);
            chatNameTextView = itemView.findViewById(R.id.chatNameTextView);
            containerLayout = itemView.findViewById(R.id.containerLayout);
        }

        public void bind(User chat) {
            String avatarUrl = chat.getAvatar();
            if (avatarUrl == null || avatarUrl.isEmpty()) {
                chatIcon.setInitials(chat.getName());
            } else {
                chatIcon.setAvatar(chat.getAvatar());
            }
            chatNameTextView.setText(chat.getName());
            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CometChatMessageListActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.UID, chat.getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, chat.getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS, chat.getStatus());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, chat.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.LINK, chat.getLink());
                    intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                    context.startActivity(intent);
                }
            });
        }
    }
}
