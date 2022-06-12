package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.docoding.clickcare.R;

public class ChatDoctorActivity extends AppCompatActivity {
    Button endChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_doctor);

        endChat = findViewById(R.id.end_chat);
        endChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeActivity = new Intent(ChatDoctorActivity.this, HomeActivity.class);
                homeActivity.putExtra(HomeActivity.END_CHAT, "endchat");
                startActivity(homeActivity);
            }
        });
    }
}