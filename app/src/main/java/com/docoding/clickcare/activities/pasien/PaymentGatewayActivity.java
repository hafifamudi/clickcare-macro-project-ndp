package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.model.User;
import com.pixplicity.easyprefs.library.Prefs;

public class PaymentGatewayActivity extends AppCompatActivity {
    private User receiverUser;
    Button bookinDoctor;
    TextView doctorName;
    TextView doctorSpesialis;
    ImageView doctorPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymen_gateway);

        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);

        doctorName = findViewById(R.id.doctor_name);
        doctorName.setText(receiverUser.name);

        doctorSpesialis = findViewById(R.id.doctor_spesialis);
        doctorSpesialis.setText("Spesialis " + receiverUser.spesialis);

        doctorPhoto = findViewById(R.id.doctor_photo);
        Glide.with(getApplicationContext())
                .load(receiverUser.image)
                .into(doctorPhoto);

        bookinDoctor = findViewById(R.id.booking_doctor);
        bookinDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatActivity = new Intent(PaymentGatewayActivity.this, ChatDoctorActivity.class);
                chatActivity.putExtra(Constants.KEY_USER, receiverUser);
                startActivity(chatActivity);
            }
        });
    }
}