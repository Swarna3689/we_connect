package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.example.weconnect.Constants.AppConfig;

/**
 * @brief MainActivity loads the screen for user login
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initCometChat();
        initView();
    }

    /**
     * @brief Method to initialize the Login Page
     */
    private void initView() {
        EditText userIdEditText = findViewById(R.id.etMobile);
        Button submitButton = findViewById(R.id.login_user_btn);
        Button registerUserButton = findViewById(R.id.create_user_btn);
        ProgressBar progressBar = findViewById(R.id.createUser_pb);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CometChat.getLoggedInUser() == null) {
                    progressBar.setVisibility(View.VISIBLE);
                    CometChat.login(userIdEditText.getText().toString(), AppConfig.AUTH_KEY, new CometChat.CallbackListener<User>() {

                        @Override
                        public void onSuccess(User user) {
                            progressBar.setVisibility(View.GONE);
                            openChatListActivity();
                        }

                        @Override
                        public void onError(CometChatException e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    openChatListActivity();
                }
            }
        });

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterUserActivity.class));
            }
        });
    }

    /**
     * @brief Method to redirect the user to screen listing all the conversations
     */
    private void openChatListActivity() {
        ChatListActivity.start(this);
    }

    /**
     * @brief Method to initialize the Settings of CometChat
     */
    private void initCometChat() {

        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(AppConfig.REGION).build();

        CometChat.init(this, AppConfig.APP_ID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {

            }

            @Override
            public void onError(CometChatException e) {
            }
        });


    }

}
