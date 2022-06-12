package com.docoding.clickcare.activities.pasien;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.databinding.FragmentHomeUserLoginBinding;
import com.docoding.clickcare.databinding.FragmentSettingUserBinding;
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


public class SettingUser extends Fragment {
    private FragmentSettingUserBinding binding;
    private Dialog dialog;
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


    public SettingUser() {
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
        binding = FragmentSettingUserBinding.inflate(inflater, container, false);
        View viewBinding = binding.getRoot();

        dialog = new Dialog(getActivity());

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
        firebaseStorage = firebaseStorage.getInstance();

        binding.changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clickedd");
//              declare variable for handle UI dialog
                Button doneChange;
                TextView usernameProfile;
                EditText usernameInput;


//                AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
//                LayoutInflater factory = LayoutInflater.from(getActivity());
//                View viewDialog = factory.inflate(R.layout.change_profile_user, null);
//                alertadd.setView(viewDialog);

                dialog.setContentView(R.layout.change_profile_user);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
//              set value to variable
                doneChange = dialog.findViewById(R.id.done_change);
                usernameProfile = dialog.findViewById(R.id.username);
                userPhotoProfile = dialog.findViewById(R.id.doctor_photo);
                usernameInput = dialog.findViewById(R.id.username_input);

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
                        Toast.makeText(getActivity(), "Failed To Fetch", Toast.LENGTH_SHORT).show();
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
                                Glide.with(getActivity())
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
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });


        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText oldPassword, newPassword;
                Button doneChangePassword;
//
//                AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
//                LayoutInflater factory = LayoutInflater.from(getActivity());
//                View viewDialog = factory.inflate(R.layout.change_user_password, null);
//                alertadd.setView(viewDialog);

                dialog.setContentView(R.layout.change_user_password);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));

                oldPassword = dialog.findViewById(R.id.old_password);
                newPassword = dialog.findViewById(R.id.new_password);
                doneChangePassword = dialog.findViewById(R.id.change_password_done);

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
                                                        Toast.makeText(getActivity(), "Password telah di Update",
                                                                Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Password Gagal di Update" + e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                        System.out.println(e.getMessage());
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Password Gagal di Update" + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                System.out.println(e.getMessage());
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        binding.logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        binding.changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
//                LayoutInflater factory = LayoutInflater.from(getActivity());
//                View viewDialog = factory.inflate(R.layout.change_language_user, null);
//                alertadd.setView(viewDialog);
//                alertadd.show();
                  dialog.setContentView(R.layout.change_language_user);
                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  dialog.show();
            }
        });

        return viewBinding;
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
                Toast.makeText(getActivity(), "Profile Update Succusfully", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void updateimagetostorage() {


        StorageReference imageref = storageReference.child("Images").child(firebaseAuth.getCurrentUser().getUid()).child("Profile Pic");

        //Image compresesion

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
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
                        Toast.makeText(getActivity(), "URI get sucess", Toast.LENGTH_SHORT).show();
                        updatenameoncloudfirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "URI get Failed", Toast.LENGTH_SHORT).show();
                    }


                });
                Toast.makeText(getActivity(), "Image is Updated", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Image Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

}