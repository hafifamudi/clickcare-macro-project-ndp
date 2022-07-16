package com.docoding.clickcare.activities.pasien;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docoding.clickcare.R;
import com.docoding.clickcare.activities.dokter.ListChatPasienActivityTwo;
import com.docoding.clickcare.databinding.FragmentSettingUserBinding;
import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.model.UserModel;
import com.docoding.clickcare.state.GlobalUserState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SettingUser extends Fragment {
    private FragmentSettingUserBinding binding;
    private Dialog dialog;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;

    private final int PICK_IMAGE_REQUEST = 22;
    private String imageURIacessToken;
    private static int PICK_IMAGE = 123;
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

        firebaseStorage = firebaseStorage.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();

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

        binding.changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button doneChange;
                TextView usernameProfile;
                EditText usernameInput;

                if (Prefs.getString(Constants.KEY_EMAIL) != null) {

                    dialog.setContentView(R.layout.change_profile_user);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
//                  set value to variable
                    doneChange = dialog.findViewById(R.id.done_change);
                    usernameProfile = dialog.findViewById(R.id.username);
                    userPhotoProfile = dialog.findViewById(R.id.doctor_photo);
                    usernameInput = dialog.findViewById(R.id.username_input);

                    usernameProfile.setText(Prefs.getString(Constants.KEY_NAME));
                    Glide.with(getActivity())
                            .load(Prefs.getString(Constants.KEY_IMAGE))
                            .into(userPhotoProfile);
//                  update image
                    userPhotoProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(
                                    Intent.createChooser(
                                            intent,
                                            "Select Image from here..."),
                                    PICK_IMAGE_REQUEST);
                        }
                    });

//              update user profile
                    doneChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newUsername = usernameInput.getText().toString().trim();
                            boolean isEmptyField = false;

                            if (TextUtils.isEmpty(newUsername)) {
                                isEmptyField = true;
                                usernameInput.setError("Mohon untuk Field username di isi");

                            } else if (imagePath != null && !isEmptyField) {
//                              update name in local storage
                                Prefs.putString(Constants.KEY_NAME, newUsername);
                                updateimagetostorage();
                                dialog.dismiss();
                            } else if (imagePath == null && !isEmptyField) {
                                Prefs.putString(Constants.KEY_NAME, newUsername);
                                updatenameoncloudfirestore();
                                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                    return;
                }
                Toast.makeText(getActivity(), "Mohon Login Terlebih Dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText oldPassword, newPassword;
                Button doneChangePassword;

                if (Prefs.getString(Constants.KEY_EMAIL) != null) {

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

                            documentReference = firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(Prefs.getString(Constants.KEY_USER_ID));

                            if (Prefs.getString(Constants.KEY_PASSWORD).equals(oldPasswordValue)) {
                                documentReference.update(Constants.KEY_PASSWORD, newPasswordValue);
//                              update password in local storage
                                Prefs.putString(Constants.KEY_PASSWORD, newPasswordValue);
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Password berhasil di ubah", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity(), "Password lama anda salah", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                    return;
                }
                Toast.makeText(getActivity(), "Mohon Login Terlebih Dahulu", Toast.LENGTH_SHORT).show();

            }
        });

        binding.logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String collectionPath =
                        Prefs.getString(Constants.KEY_LOGIN_INFO).equals("doctor") ? Constants.KEY_COLLECTION_DOCTORS : Constants.KEY_COLLECTION_USERS;
                DocumentReference documentReference =
                        firebaseFirestore.collection(collectionPath).document(
                                Prefs.getString(Constants.KEY_USER_ID)
                        );
                HashMap<String, Object> updates = new HashMap<>();
                documentReference.get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().getString(Constants.KEY_FCM_TOKEN) != null) {
                                    updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                                    documentReference.update(updates)
                                            .addOnSuccessListener(unused -> {
                                                Prefs.clear();
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Mohon Login Terlebih Dahulu", Toast.LENGTH_SHORT).show());
                                } else {
                                    Prefs.clear();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                }
                            }
                        });

            }
        });

        binding.changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Prefs.getString(Constants.KEY_EMAIL) != null) {
                    dialog.setContentView(R.layout.change_language_user);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    return;
                }
                Toast.makeText(getActivity(), "Mohon Login Terlebih Dahulu", Toast.LENGTH_SHORT).show();
            }
        });

        return viewBinding;
    }

    private void updatenameoncloudfirestore() {

        DocumentReference documentReference = firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(Prefs.getString(Constants.KEY_USER_ID));
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", Prefs.getString(Constants.KEY_NAME));
        userdata.put("image", imageURIacessToken != null ? imageURIacessToken : Prefs.getString(Constants.KEY_IMAGE));
        userdata.put("password", Prefs.getString(Constants.KEY_PASSWORD));
        userdata.put("email", Prefs.getString(Constants.KEY_EMAIL));
        userdata.put("uid", Prefs.getString(Constants.KEY_USER_ID));
        userdata.put("status", "Online");


        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Profile Update Succusfully", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void updateimagetostorage() {


        StorageReference imageref = storageReference.child("Images").child(Prefs.getString(Constants.KEY_USER_ID)).child("Profile Pic");

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
                        Prefs.putString(Constants.KEY_IMAGE, imageURIacessToken);
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

    private void signInWithEmailAndPassword(String email, String password, BottomSheetDialog bottomSheet, View view) {
        ProgressBar loginProses = view.findViewById(R.id.login_proses);

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS)
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
                Prefs.putString(Constants.KEY_LOGIN_INFO, "pasien");

                GlobalUserState.userAuthStatus = "login_true";
                loginProses.setVisibility(view.VISIBLE);
                bottomSheet.dismiss();
                GlobalUserState.userAuthStatus = "login_true";
                replaceFragment(new HomeNoLoginUser());

            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(getActivity(), "Password atau Email Salah",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == getActivity().RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            imagePath = data.getData();
        }
    }
}