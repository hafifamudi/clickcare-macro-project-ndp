package com.docoding.clickcare.activities.pasien;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.Preference;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.docoding.clickcare.R;
import com.docoding.clickcare.databinding.FragmentBookAntrianPasienBinding;
import com.docoding.clickcare.databinding.FragmentHomeUserLoginBinding;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.helper.PreferenceManager;
import com.docoding.clickcare.model.Pasien;
import com.docoding.clickcare.state.GlobalUserState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pixplicity.easyprefs.library.Prefs;

import kotlin.jvm.internal.PackageReference;


public class BookAntrianPasien extends Fragment {
    private FragmentBookAntrianPasienBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public BookAntrianPasien() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        binding = FragmentBookAntrianPasienBinding.inflate(inflater, container, false);
        View viewBinding = binding.getRoot();

        if (!Prefs.getString(Constants.KEY_WAKTU_PASIEN).isEmpty()) {
            binding.thereIsAntrian.setVisibility(View.VISIBLE);
            binding.nothingAntrian.setVisibility(View.GONE);

            binding.antrianName.setText(Prefs.getString(Constants.KEY_NAMA_PASIEN));
            binding.antrianDate.setText(Prefs.getString(Constants.KEY_WAKTU_PASIEN));
            binding.antrianKeluhan.setText(Prefs.getString(Constants.KEY_KELUHAN_PASIEN));
            binding.antrianDokter.setText(Prefs.getString(Constants.KEY_DOKTER_PASIEN));
            binding.antrianDokter.setText(Prefs.getString(Constants.KEY_ANTRIAN_PASIEN));
        }

        if (Prefs.getString(Constants.KEY_WAKTU_PASIEN).isEmpty()) {
            binding.thereIsAntrian.setVisibility(View.GONE);
            binding.nothingAntrian.setVisibility(View.VISIBLE);
        }

        if (Prefs.getString(Constants.KEY_EMAIL) == null) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottomSheetDialogTheme
            );

            View bottomSheetView = LayoutInflater.from(getActivity())
                    .inflate(
                            R.layout.login_bottom_sheet,
                            (LinearLayout) viewBinding.findViewById(R.id.login_bottom_sheet_two)
                    );

            bottomSheetView.findViewById(R.id.login_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // register user
                    // take user value when register
                    EditText emailLogin = bottomSheetView.findViewById(R.id.email_login);
                    EditText passwordLogin = bottomSheetView.findViewById(R.id.password_login);
                    ProgressBar loginProses = bottomSheetView.findViewById(R.id.login_proses);
                    // validate user input
                    boolean isEmptyField = false;

                    if (TextUtils.isEmpty(emailLogin.getText().toString().trim())) {
                        isEmptyField = true;
                        emailLogin.setError("Mohon Field Email untuk di isi");
                    }

                    if (TextUtils.isEmpty(passwordLogin.getText().toString().trim())) {
                        isEmptyField = true;
                        passwordLogin.setError("Mohon Field Password untuk di isi");
                    }

//              validate user auth for email and password
                    if (!isEmptyField) {
                        loginProses.setVisibility(view.VISIBLE);
                        signInWithEmailAndPassword(emailLogin.getText().toString().trim(),
                                passwordLogin.getText().toString().trim(), bottomSheetDialog, bottomSheetView);
                    }


                }
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }

        binding.bookingAntrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent registrasiActivity = new Intent(getActivity(), RegisterPasienActivity.class);
                    startActivity(registrasiActivity);
                    return;
                }

                Toast.makeText(getActivity(), "Mohon Login Terlebih Dahulu", Toast.LENGTH_SHORT);
            }
        });

        return viewBinding;
    }

    private void signInWithEmailAndPassword(String email, String password, BottomSheetDialog bottomSheet, View view) {
        ProgressBar loginProses = view.findViewById(R.id.login_proses);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            loginProses.setVisibility(view.INVISIBLE);

                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            System.out.println("=====user=====" + user.getEmail());

                            loginProses.setVisibility(view.VISIBLE);
                            bottomSheet.dismiss();
                            GlobalUserState.userAuthStatus = "login_true";
                            replaceFragment(new HomeNoLoginUser());
                        } else {
//                            loginProses.setVisibility(view.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Password atau Email Salah",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

}