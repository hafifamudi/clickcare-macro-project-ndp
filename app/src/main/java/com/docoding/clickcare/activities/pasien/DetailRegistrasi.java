package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.docoding.clickcare.R;

public class DetailRegistrasi extends AppCompatActivity {
    Button registrasiAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_registrasi);

        registrasiAcc = findViewById(R.id.registrasi_acc);
        registrasiAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeActivity = new Intent(DetailRegistrasi.this, HomeActivity.class);
                homeActivity.putExtra(HomeActivity.SUCCESS_ORDER, "accepted");
                startActivity(homeActivity);
            }
        });
    }
}