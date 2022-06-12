package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.docoding.clickcare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if (firebaseUser != null) {
                   Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                   startActivity(intent);
                   finish();
               } else {
                   Intent intent = new Intent(SplashScreen.this, OnboardingActivity.class);
                   startActivity(intent);
                   finish();
               }
            }
        }, 5000);
    }
}