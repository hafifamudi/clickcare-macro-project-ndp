package com.docoding.clickcare.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.docoding.clickcare.helper.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentService extends IntentService {
    private ResultReceiver resultReceiver;
    private String errorMsg;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            errorMsg = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            System.out.println("get data from service receiver " + resultReceiver.toString());
            System.out.println("get data from service location " + location.getLatitude());
            System.out.println("get data from service location " + location.getLongitude());

            if (location == null) return;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = null;

            try {
                addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            }catch (Exception e) {
                errorMsg = e.getMessage();
            }

            if (addressList == null || addressList.isEmpty()) {
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMsg);
            } else {
                System.out.println("success get address");

                String address = addressList.get(0).getAddressLine(0);

                deliverResultToReceiver(
                        Constants.SUCCESS_RESULT,
                        address
                );
            }
        } else {
            errorMsg = "There is no intent";
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMsg);
        }
    }

    private void deliverResultToReceiver(int resultCode, String addressMessage) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode, bundle);
        System.out.println("deliver " + bundle.getString(Constants.RESULT_DATA_KEY));
        System.out.println("receiver " + resultReceiver);
    }
}
