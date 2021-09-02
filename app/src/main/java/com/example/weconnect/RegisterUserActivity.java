package com.example.weconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.example.weconnect.Constants.AppConfig;

/**
 * @brief RegisterUserActivity loads the screen for user registration / sign up
 */
public class RegisterUserActivity extends AppCompatActivity {

    private EditText mobile;
    private EditText name;
    private Button createUserBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        progressBar = findViewById(R.id.createUser_pb);
        mobile = findViewById(R.id.etMobile);
        name = findViewById(R.id.etName);
        createUserBtn = findViewById(R.id.create_user_btn);
        createUserBtn.setOnClickListener(v ->
                signUpTapped()
        );
    }

    /**
     * @brief Method called while sign p button is tapped
     */
    private void signUpTapped(){
        User user = new User();
        user.setUid(mobile.getText().toString());
        user.setName(name.getText().toString());
        registerUser(user);
    }

    /**
     * @brief Method to register the user details
     */
    private void registerUser(User user) {
        progressBar.setVisibility(View.VISIBLE);
        CometChat.createUser(user, AppConfig.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                progressBar.setVisibility(View.GONE);
                login(user);
            }
            @Override
            public void onError(CometChatException e) {
                progressBar.setVisibility(View.GONE);
                createUserBtn.setClickable(true);
                Toast.makeText(RegisterUserActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * @brief Method to trigger user login
     */
    private void login(User user) {
        progressBar.setVisibility(View.VISIBLE);
        CometChat.login(user.getUid(), AppConfig.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                progressBar.setVisibility(View.GONE);
                openChatListActivity();
            }
            @Override
            public void onError(CometChatException e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterUserActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * @brief Method to redirect the user to screen listing all the conversations
     */
    private void openChatListActivity() {
        ChatListActivity.start(this);
    }
}
