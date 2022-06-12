package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.docoding.clickcare.R;

public class PaymentGatewayActivity extends AppCompatActivity {
    Button bookinDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymen_gateway);

        bookinDoctor = findViewById(R.id.booking_doctor);
        bookinDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatActivity = new Intent(PaymentGatewayActivity.this, ChatDoctorActivity.class);
                startActivity(chatActivity);
            }
        });
    }
}