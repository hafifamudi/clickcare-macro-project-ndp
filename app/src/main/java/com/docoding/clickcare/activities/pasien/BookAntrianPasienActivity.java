package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.docoding.clickcare.R;

public class BookAntrianPasienActivity extends AppCompatActivity {
    Button bookingAntrian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_antrian_pasien);

        bookingAntrian = findViewById(R.id.booking_antrian);
        bookingAntrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrasiActivity = new Intent(BookAntrianPasienActivity.this, RegisterPasienActivity.class);
                startActivity(registrasiActivity);
            }
        });
    }
}