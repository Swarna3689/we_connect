package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.example.weconnect.adapters.ChatListAdapter;

import java.util.List;

/**
* @brief ChatListActivity loads list of all one-to-one and group conversations of user.
*/
public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = "ChatListActivity";

    private EditText etSearch;    // Use to perform search operation on list of users.

    ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        etSearch = findViewById(com.cometchat.pro.uikit.R.id.search_bar);

        getChatList();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()==0) {
                    // If no text is entered in search box, display whole chats
                    getChatList();
                }
                else {
                    // Search users based on text in etSearch field.
                    searchUser(editable.toString());
                }
            }
        });
    }

    /**
     * This method is used to fetch the list from CometChat
     */
    private void getChatList() {
        UsersRequest usersRequest = new UsersRequest.UsersRequestBuilder().build();

        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List <User> list) {
                refreshUI(list);
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "Groups list fetching failed with exception: " + e.getMessage());
            }
        });
    }

    /**
     * This method is used to populate the UI with chats in the chat list
     */
    private void refreshUI(List<User> list) {
        RecyclerView chatListRecyclerView = findViewById(R.id.chatList_rv);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatListAdapter = new ChatListAdapter(list, this);
        chatListRecyclerView.setAdapter(chatListAdapter);
    }

    /**
     * This method is used to start the activity where all the chats are listed.
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, ChatListActivity.class);
        context.startActivity(starter);
    }

    /**
     * This method is used to search users present in your App_ID.
     * @param s is a string used to get users matches with this string.
     */
    private void searchUser(String s) {
        UsersRequest usersRequest = new UsersRequest.UsersRequestBuilder().setSearchKeyword(s).setLimit(100).build();
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                chatListAdapter.setUsersList(users); // set the users to adapter
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

}
