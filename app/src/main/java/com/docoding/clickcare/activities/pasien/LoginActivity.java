package com.docoding.clickcare.activities.pasien;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.docoding.clickcare.R;
import com.docoding.clickcare.activities.dokter.ListChatPasienActivityTwo;
import com.docoding.clickcare.databinding.ActivityLoginBinding;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.helper.PreferenceManager;
import com.docoding.clickcare.state.GlobalUserState;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    //    set varialbe for google sign
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private BeginSignInRequest signInRequest;
    private GoogleSignInClient googleSignInClient;
    private String redirectActivity;

    //    set firebase variable
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//       initialize preference
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();


        // set up google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        firebaseAuth = FirebaseAuth.getInstance();
//        set default state
        GlobalUserState.userSuccessOrder = "ACCEPT";
        GlobalUserState.userSuccessOrder = "START";

        binding.skipToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
//              save state
                GlobalUserState.userAuthStatus = "login_false";
                startActivity(homeActivity);
            }
        });
        binding.loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // register user
                // take user value when register

                String emailLogin = binding.emailLogin.getText().toString().trim();
                String passwordLogin = binding.passwordLogin.getText().toString().trim();

                // validate user input
                boolean isEmptyField = false;

                if (TextUtils.isEmpty(emailLogin)) {
                    isEmptyField = true;
                    binding.emailLogin.setError("Mohon Field Email untuk di isi");
                }

                if (TextUtils.isEmpty(passwordLogin)) {
                    isEmptyField = true;
                    binding.passwordLogin.setError("Mohon Field Password untuk di isi");
                }

//              validate user auth for email and password
                if (!isEmptyField) {
                    binding.loginProses.setVisibility(view.VISIBLE);
                    signInWithEmailAndPassword(emailLogin, passwordLogin, view);
                }


            }
        });

        binding.registrasiUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.loginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        binding.forgotPasswordUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPassword);
            }
        });
    }


    private void signInWithEmailAndPassword(String email, String password, View view) {
        String documentPath = "";
        List<String> doctorList = Arrays.asList("drsrilestari@gmail.com", "drsultonhendra@gmail.com");

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        if (doctorList.contains(email)) {
            documentPath = Constants.KEY_COLLECTION_DOCTORS;
            this.redirectActivity = "ChatDoctorActivity.class";
        } else {
            documentPath = Constants.KEY_COLLECTION_USERS;
            this.redirectActivity = "HomeActivity.class";
        }

        database.collection(documentPath)
                .whereEqualTo(Constants.KEY_EMAIL, email)
                .whereEqualTo(Constants.KEY_PASSWORD, password)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {

                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                Prefs.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                Prefs.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                Prefs.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                Prefs.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                Prefs.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                Prefs.putString(Constants.KEY_PASSWORD, documentSnapshot.getString(Constants.KEY_PASSWORD));

                if (this.redirectActivity.equalsIgnoreCase("ChatDoctorActivity.class")) {
//                    set user login info
                    Prefs.putString(Constants.KEY_LOGIN_INFO, "doctor");
                    Prefs.putString(Constants.KEY_NAME_DOCTOR, documentSnapshot.getString(Constants.KEY_NAME_DOCTOR));
                    Prefs.putString(Constants.KEY_DOCTOR_SPESIALIST, documentSnapshot.getString(Constants.KEY_DOCTOR_SPESIALIST));
                    Prefs.putString(Constants.KEY_AVAILABILITY, documentSnapshot.getString(Constants.KEY_AVAILABILITY));
                    Intent intent = new Intent(getApplicationContext(), ListChatPasienActivityTwo.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Prefs.putString(Constants.KEY_LOGIN_INFO, "pasien");
                    GlobalUserState.userAuthStatus = "login_true";
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            } else {
                binding.loginProses.setVisibility(view.INVISIBLE);
                // If sign in fails, display a message to the user.
                binding.passwordLogin.setError("Password atau Email salah");
                Toast.makeText(LoginActivity.this, "Password atau Email Salah",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_ONE_TAP);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Password atau Email Salah",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}