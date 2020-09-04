package com.example.postie11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT= 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//
                //  if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {
                //    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                //   startActivity(intent);
                 //   finish();
                    //return;
                }

                Intent homeIntent = new Intent(SplashActivity.this, RegisterActivity .class);
                startActivity(homeIntent);
                finish();

            }
        },SPLASH_TIME_OUT);

    }
}
