package com.docoding.clickcare.activities.pasien;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.docoding.clickcare.helper.Constants;
import com.docoding.clickcare.services.FetchAddressIntentService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.docoding.clickcare.R;
import com.docoding.clickcare.adapter.NewsAdapter;
import com.docoding.clickcare.databinding.FragmentHomeUserLoginBinding;
import com.docoding.clickcare.dummydata.NewsDummy;
import com.docoding.clickcare.model.NewsModel;
import com.docoding.clickcare.state.GlobalUserState;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

public class HomeUserLogin extends Fragment {
    private FragmentHomeUserLoginBinding binding;
    private ArrayList<NewsModel> listNews = new ArrayList<>();
    private ResultReceiver resultReceiver;
    private FirebaseFirestore firebaseFirestore;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

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
        firebaseFirestore = FirebaseFirestore.getInstance();

        listNews.addAll(NewsDummy.ListData());
        showRecycleListNews();

        // service setup
        resultReceiver = new AddressResultReceiver(new Handler());

        binding.konsultasiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

//                      validate user auth for email and password
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

        binding.loginBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getActivity(), R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(getActivity())
                        .inflate(
                                R.layout.login_bottom_sheet,
                                (LinearLayout) viewBinding.findViewById(R.id.login_bottom_sheet_two)
                        );

                TextView title = bottomSheetView.findViewById(R.id.login_bottom_title);
                title.setText("Silahkan melakukan Login");

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

        binding.registerBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getActivity(), R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(getActivity())
                        .inflate(
                                R.layout.register_bottom_sheet,
                                (ScrollView) viewBinding.findViewById(R.id.register_bottom_sheet_two)
                        );

                bottomSheetView.findViewById(R.id.registrasi).setOnClickListener(new View.OnClickListener() {
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

        binding.googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getCurrentLocation();
                }
            }
        });
        return viewBinding;
    }


    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Ijin tidak di berikan",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            Location currentLocation = locationResult.getLastLocation();
                            LatLng locationCoordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            Location location = new Location("providerNA");
                            location.setLatitude(locationCoordinates.latitude);
                            location.setLongitude(locationCoordinates.longitude);
                            fetchAddressFromLocation(location);
                        }
                    }
                }, Looper.getMainLooper());
    }


    private void showRecycleListNews() {
        binding.newsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        NewsAdapter listNewsAdapter = new NewsAdapter(listNews);
        binding.newsList.setAdapter(listNewsAdapter);
        System.out.println("execute recycle....");
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

    private void fetchAddressFromLocation(Location location) {
        System.out.println("send data to service...");
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        getActivity().startService(intent);
    }

    private void displayTrack(String source, String destination) {
        System.out.println("display track source " + source);
        try {
            Uri uri = Uri.parse("http://www.google.co.in/maps/dir/" + source + "/" + destination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(getActivity(), "Ijin tidak di berikan",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                // redirect to maps
                String destination = "Jl. Jeruk Raya No.15, RW.1, Jagakarsa, Kec. Jagakarsa, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12620";
                displayTrack(resultData.getString(Constants.RESULT_DATA_KEY), destination);
            } else {
                Toast.makeText(getActivity(), resultData.getString(Constants.RESULT_DATA_KEY),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}