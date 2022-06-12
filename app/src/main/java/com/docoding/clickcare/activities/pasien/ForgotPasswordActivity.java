package com.docoding.clickcare.activities.pasien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.docoding.clickcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button sendEmailVerification;
    EditText userEmail;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        firebaseAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.email_verification);

        sendEmailVerification = findViewById(R.id.send_email_verification);
        sendEmailVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText(getApplicationContext(), "Email Verifikasi telah di kirim",
                                           Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText(getApplicationContext(), "Email Verifikasi Gagal di kirim",
                                           Toast.LENGTH_SHORT).show();
                               }
                            }
                        });
            }
        });
    }
}