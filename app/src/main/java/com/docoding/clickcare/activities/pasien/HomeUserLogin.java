package com.docoding.clickcare.activities.pasien;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBindings;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.adapter.NewsAdapter;
import com.docoding.clickcare.databinding.ActivityHomeBinding;
import com.docoding.clickcare.databinding.FragmentHomeUserLoginBinding;
import com.docoding.clickcare.dummydata.NewsDummy;
import com.docoding.clickcare.model.NewsModel;
import com.docoding.clickcare.model.UserModel;
import com.docoding.clickcare.state.GlobalUserState;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeUserLogin extends Fragment {
    private FragmentHomeUserLoginBinding binding;
    private ArrayList<NewsModel> listNews = new ArrayList<>();
    private BeginSignInRequest signInRequest;
    private GoogleSignInClient googleSignInClient;

    //    set firebase variable
    private FirebaseAuth firebaseAuth;

    public HomeUserLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeUserLoginBinding.inflate(inflater, container, false);
        View viewBinding = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        listNews.addAll(NewsDummy.ListData());
        showRecycleListNews();

        binding.konsultasiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
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
        });


        return viewBinding;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showRecycleListNews() {
        binding.newsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        NewsAdapter listNewsAdapter = new NewsAdapter(listNews);
        binding.newsList.setAdapter(listNewsAdapter);
        System.out.println("execute recycle....");
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