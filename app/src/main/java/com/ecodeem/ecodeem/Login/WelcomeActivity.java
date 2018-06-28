package com.ecodeem.ecodeem.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ecodeem.ecodeem.R;

public class WelcomeActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private TextView mLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mRegisterButton = (Button) findViewById(R.id.createAccountBtn);
        mLoginTextView = (TextView) findViewById(R.id.loginTextView);

        //Send User to the Register Page...
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegister();
            }
        });

        //Send User to the Log in Page, directly...
        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();
            }
        });

    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void sendToRegister() {

        Intent registerIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}
