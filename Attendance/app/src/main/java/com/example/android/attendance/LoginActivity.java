package com.example.android.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView needHelpLink;

    private int attempts = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        needHelpLink = findViewById(R.id.need_help_link);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        needHelpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent needHelpIntent = new Intent(Intent.ACTION_SEND);
                needHelpIntent.setType("text/html");
                needHelpIntent.putExtra(Intent.EXTRA_SUBJECT,"Need Help");
                needHelpIntent.putExtra(Intent.EXTRA_TEXT,"Describe the problem");
                startActivity(Intent.createChooser(needHelpIntent,"Send Email..."));
            }
        });
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.equals(" ") && password.equals(" ")) {
            Toast.makeText(this, R.string.toast_login_successful,Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent();
            mainIntent.setClass(this,MainActivity.class);
            startActivity(mainIntent);
        } else {
            if (attempts > 0) {
                attempts--;
            } else {
                loginButton.setClickable(false);
            }
            Toast.makeText(this,"Attempts Left: "+attempts,Toast.LENGTH_SHORT).show();
        }
    }
}
