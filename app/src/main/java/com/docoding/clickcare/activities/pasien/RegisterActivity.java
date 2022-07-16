package com.docoding.clickcare.activities.pasien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.docoding.clickcare.R;
import com.docoding.clickcare.databinding.RegisterActivityBinding;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.helper.PreferenceManager;
import com.docoding.clickcare.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private RegisterActivityBinding binding;
    private PreferenceManager preferenceManager;
    //    Firebase Variable
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private String imageUriAccessToken;
    private boolean isEmailExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferenceManager = new PreferenceManager(getApplicationContext());


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // register user
                // take user value when register

                String usernameRegister = binding.usernameRegister.getText().toString().trim();
                String emailRegister = binding.emailRegister.getText().toString().trim();
                String passwordRegister = binding.passwordRegister.getText().toString().trim();
                String confirmPasswordRegister = binding.confirmPasswordRegister.getText().toString().trim();

                // validate user input
                boolean isEmptyField = false;

                if (TextUtils.isEmpty(usernameRegister)) {
                    isEmptyField = true;
                    binding.usernameRegister.setError("Mohon Field Name untuk di isi");
                }

                if (TextUtils.isEmpty(emailRegister)) {
                    isEmptyField = true;
                    binding.emailRegister.setError("Mohon Field Email untuk di isi");
                }

                if (TextUtils.isEmpty(passwordRegister)) {
                    isEmptyField = true;
                    binding.passwordRegister.setError("Mohon Field Password untuk di isi");
                }

                if (TextUtils.isEmpty(confirmPasswordRegister)) {
                    isEmptyField = true;
                    binding.confirmPasswordRegister.setError("Mohon Field Confirm Password untuk di isi");
                }

                if (!passwordRegister.equals(confirmPasswordRegister)) {
                    isEmptyField = true;
                    binding.confirmPasswordRegister.setError("Field Password dan Confirm Password tidak sama");
                }

////              validate email is already exist or not
//                isEmailExistOrNot();
                if (isEmailExist) {
                    isEmptyField = true;
                    binding.emailRegister.setError("Email sudah terdaftar, silahkan gunakan Email lain");
                }

                isEmailExistOrNot();
                if (!isEmptyField) {
                    binding.registerProses.setVisibility(View.VISIBLE);
                    sendNewUserData(view);
                }
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isEmailExistOrNot() {
        // validate user email is exist or not

        firebaseFirestore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("email").equals(
                                        binding.emailRegister.getText().toString().trim()
                                )) {
                                    isEmailExist = true;
                                } else {
                                    isEmailExist = false;
                                }
                            }
                        }
                    }
                });

        return isEmailExist;
    }

    //
    private void sendNewUserData(View view) {
        // First register the user for get the FirebaseAuth UID
        firebaseAuth.createUserWithEmailAndPassword(
                binding.emailRegister.getText().toString().trim()
                , binding.passwordRegister.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.registerProses.setVisibility(view.INVISIBLE);

                            Toast.makeText(getApplicationContext(), "Berhasil melakukan registrasi",
                                    Toast.LENGTH_SHORT).show();
                            // get the user UID
                            currentUser = firebaseAuth.getCurrentUser();
                            sendDataToRealTimeDatabase(currentUser.getUid());
                        } else {
                            binding.registerProses.setVisibility(view.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Gagal melakukan registrasi",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal melakukan registrasi",
                                Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }
                });
    }

    private void sendDataToRealTimeDatabase(String userUID) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(userUID);

        UserModel userProfile = new UserModel(
                currentUser.getUid(),
                binding.usernameRegister.getText().toString().trim(),
                binding.emailRegister.getText().toString().trim(),
                binding.passwordRegister.getText().toString().trim(),
                binding.confirmPasswordRegister.getText().toString().trim());

        databaseReference.setValue(userProfile);
        Toast.makeText(getApplicationContext(), "User Profile Added Sucessfully", Toast.LENGTH_SHORT).show();
        sendDefaultImagetoStorage();

    }

    private void sendDefaultImagetoStorage() {
        StorageReference imageref = storageReference.child("Images").child(currentUser.getUid()).child("Profile Pic");

        //Image compresesion

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.user_no_login_profile);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        ///putting image to storage

        UploadTask uploadTask = imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUriAccessToken = uri.toString();
                        Toast.makeText(getApplicationContext(), "URI get sucess", Toast.LENGTH_SHORT).show();
                        sendDataTocloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Gagal Upload image==============");
                        Toast.makeText(getApplicationContext(), "URI get Failed", Toast.LENGTH_SHORT).show();
                    }


                });
                Toast.makeText(getApplicationContext(), "Image is uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image Not UPdloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendDataTocloudFirestore() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String, Object> userdata = new HashMap<>();
        userdata.put("uid", currentUser.getUid());
        userdata.put("name", binding.usernameRegister.getText().toString().trim());
        userdata.put("email", binding.emailRegister.getText().toString().trim());
        userdata.put("password", binding.passwordRegister.getText().toString().trim());
        userdata.put("image", imageUriAccessToken);
        userdata.put("status", "Online");
        database.collection(Constants.KEY_COLLECTION_USERS).add(userdata).addOnSuccessListener(documentReference -> {
            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
            preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
            preferenceManager.putString(Constants.KEY_NAME, binding.usernameRegister.getText().toString());
            preferenceManager.putString(Constants.KEY_IMAGE, imageUriAccessToken);
            preferenceManager.putString("uid", documentReference.getId());
            preferenceManager.putString("name", binding.usernameRegister.getText().toString());
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error ================");
                System.out.println(e.getMessage());
                Toast.makeText(getApplicationContext(), "Failed to send data to firestore" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
