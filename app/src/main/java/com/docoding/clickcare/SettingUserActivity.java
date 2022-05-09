package com.docoding.clickcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.databinding.ActivityDetailDoctorBinding;
import com.docoding.clickcare.databinding.ActivitySettingUserBinding;
import com.docoding.clickcare.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingUserActivity extends AppCompatActivity {
    private ActivitySettingUserBinding binding;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private String imageURIacessToken;
    private static int PICK_IMAGE = 123;
    private String newUsernameUser;
    private Uri imagePath;
    ImageView userPhotoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
        firebaseStorage = firebaseStorage.getInstance();

        binding.changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              declare variable for handle UI dialog
                Button doneChange;
                TextView usernameProfile;
                EditText usernameInput;


                AlertDialog.Builder alertadd = new AlertDialog.Builder(SettingUserActivity.this);
                LayoutInflater factory = LayoutInflater.from(SettingUserActivity.this);
                View viewDialog = factory.inflate(R.layout.change_profile_user, null);
                alertadd.setView(viewDialog);

//              set value to variable
                doneChange = viewDialog.findViewById(R.id.done_change);
                usernameProfile = viewDialog.findViewById(R.id.username);
                userPhotoProfile = viewDialog.findViewById(R.id.doctor_photo);
                usernameInput = viewDialog.findViewById(R.id.username_input);

//              get user info
                DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userProfile = snapshot.getValue(UserModel.class);
                        usernameProfile.setText(userProfile.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Failed To Fetch", Toast.LENGTH_SHORT).show();
                    }
                });

//               get user photo
                storageReference = firebaseStorage.getReference();
                storageReference.child("Images").child(
                        firebaseAuth.getCurrentUser().getUid())
                        .child("Profile Pic").getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageURIacessToken = uri.toString();
                                Glide.with(SettingUserActivity.this)
                                        .load(uri)
                                        .into(userPhotoProfile);
                            }
                        });

//              update image
                userPhotoProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_IMAGE);
                    }
                });

//              update user profile
                doneChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newUsername = usernameInput.getText().toString().trim();
                        newUsernameUser = newUsername;

                        boolean isEmptyField = false;

                        if (TextUtils.isEmpty(newUsername)) {
                            isEmptyField = true;
                            usernameInput.setError("Mohon untuk Field username di isi");

                        } else if (imagePath != null && !isEmptyField) {
                            UserModel userNewProfile = new UserModel();
                            userNewProfile.setUserUID(firebaseAuth.getCurrentUser().getUid());
                            userNewProfile.setUsername(newUsername);

                            databaseReference.setValue(userNewProfile);
                            updateimagetostorage();
                        } else if (imagePath == null && !isEmptyField) {
                            UserModel userNewProfile = new UserModel();
                            userNewProfile.setUserUID(firebaseAuth.getCurrentUser().getUid());
                            userNewProfile.setUsername(newUsername);
                            databaseReference.setValue(userNewProfile);
                            updatenameoncloudfirestore();
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertadd.show();
            }
        });


        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText oldPassword, newPassword;
                Button doneChangePassword;

                AlertDialog.Builder alertadd = new AlertDialog.Builder(SettingUserActivity.this);
                LayoutInflater factory = LayoutInflater.from(SettingUserActivity.this);
                View viewDialog = factory.inflate(R.layout.change_user_password, null);
                alertadd.setView(viewDialog);

                oldPassword = viewDialog.findViewById(R.id.old_password);
                newPassword = viewDialog.findViewById(R.id.new_password);
                doneChangePassword = viewDialog.findViewById(R.id.change_password_done);

                doneChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String oldPasswordValue = oldPassword.getText().toString();
                        String newPasswordValue = newPassword.getText().toString();

//                      before changing the password, authenticate the user

                        AuthCredential authCredential = EmailAuthProvider.getCredential(
                                firebaseAuth.getCurrentUser().getEmail(), oldPasswordValue);

                        firebaseAuth.getCurrentUser().reauthenticate(authCredential)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        firebaseAuth.getCurrentUser().updatePassword(newPasswordValue)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(), "Password telah di Update",
                                                                Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Password Gagal di Update" + e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                        System.out.println(e.getMessage());
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Password Gagal di Update" + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                System.out.println(e.getMessage());
                            }
                        });
                    }
                });
                alertadd.show();
            }
        });

        binding.logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent loginActivity = new Intent(SettingUserActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        binding.changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(SettingUserActivity.this);
                LayoutInflater factory = LayoutInflater.from(SettingUserActivity.this);
                View viewDialog = factory.inflate(R.layout.change_language_user, null);
                alertadd.setView(viewDialog);
                alertadd.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imagePath = data.getData();
            userPhotoProfile.setImageURI(imagePath);
        }
    }

    private void updatenameoncloudfirestore() {

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", newUsernameUser);
        userdata.put("image", imageURIacessToken);
        userdata.put("uid", firebaseAuth.getCurrentUser().getUid());
        userdata.put("status", "Online");


        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Profile Update Succusfully", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void updateimagetostorage() {


        StorageReference imageref = storageReference.child("Images").child(firebaseAuth.getCurrentUser().getUid()).child("Profile Pic");

        //Image compresesion

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        imageURIacessToken = uri.toString();
                        Toast.makeText(getApplicationContext(), "URI get sucess", Toast.LENGTH_SHORT).show();
                        updatenameoncloudfirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "URI get Failed", Toast.LENGTH_SHORT).show();
                    }


                });
                Toast.makeText(getApplicationContext(), "Image is Updated", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }


}