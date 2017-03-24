package com.itboys.lockscreen;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.itboys.safeswipe.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements
        ConnectionCallbacks, OnConnectionFailedListener {

    protected static final String TAG = "LocationFragment";
    protected GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;
    private double lastLat = 0;
    private double lastLong = 0;
    private String lance = "09354345539";
    private String tats = "09062041624";
    private String tanthan = "09153384007";
    private String sirMike = "09062581376";
    private String msg = "SafeSwipe Activated ";

    public LocationFragment() {
        // Required empty public constructor
    }


    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mLastLocation != null) {
//            mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
//                    mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
//                    mLastLocation.getLongitude()));
//            Toast.makeText(getActivity(), String.format("%f, %f", mLastLocation.getLatitude(),
//                    mLastLocation.getLongitude()), Toast.LENGTH_LONG).show();
            lastLat = mLastLocation.getLatitude();
            lastLong = mLastLocation.getLongitude();

            SmsManager sm = SmsManager.getDefault();
            msg += "Location Detected: " + lastLat + ", " + lastLong;
            sm.sendTextMessage(tats, null, msg, null, null);

        } else {
//            Toast.makeText(getActivity(), R.string.no_location_detected, Toast.LENGTH_LONG).show();
            SmsManager sm = SmsManager.getDefault();
            msg += "No Location Detected.";
            sm.sendTextMessage(tats, null, msg, null, null);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

}
