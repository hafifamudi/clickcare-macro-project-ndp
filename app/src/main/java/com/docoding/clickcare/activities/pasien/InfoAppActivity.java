package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.docoding.clickcare.databinding.ActivityHomeBinding;
import com.docoding.clickcare.databinding.ActivityInfoAppBinding;

public class InfoAppActivity extends AppCompatActivity {
    private ActivityInfoAppBinding binding;
    // toggle variable
    private int collapse = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoAppBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        binding.expandable.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collapse == 0) {
                    collapse = 1;
                    binding.expandable.expand();
                } else {
                    collapse = 0;
                    binding.expandable.collapse();
                }
            }
        });

        binding.expandable2.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collapse == 0) {
                    collapse = 1;
                    binding.expandable2.expand();
                } else {
                    collapse = 0;
                    binding.expandable2.collapse();
                }
            }
        });

        binding.expandable3.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collapse == 0) {
                    collapse = 1;
                    binding.expandable3.expand();
                } else {
                    collapse = 0;
                    binding.expandable3.collapse();
                }
            }
        });

        binding.expandable4.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collapse == 0) {
                    collapse = 1;
                    binding.expandable4.expand();
                } else {
                    collapse = 0;
                    binding.expandable4.collapse();
                }
            }
        });
    }
}