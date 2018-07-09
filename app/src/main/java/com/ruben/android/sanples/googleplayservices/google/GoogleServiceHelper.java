package com.ruben.android.sanples.googleplayservices.google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.ruben.android.sanples.googleplayservices.MainActivity;

public class GoogleServiceHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleServiceListener mGoogleServiceListener;
    private MainActivity mActivity;
    private GoogleApiClient mGoogleApiClient;
    public static final int REQUEST_CODE_RESOLUTION = -100;
    public static final int REQUEST_CODE_AVAILABILITY = -101;
    public GoogleServiceHelper(MainActivity mainActivity, GoogleServiceListener googleServiceListener) {
        mainActivity = mainActivity;
        mGoogleServiceListener = googleServiceListener;
        mGoogleApiClient = new  GoogleApiClient
                .Builder(mainActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,
                        Plus.PlusOptions.builder().setServerClientId("225520931418-1m666p8q24h8qgnmv39bo4e3c5r6li49.apps.googleusercontent.com")
                .build())
                .build()    ;
    }

    public void connect(){
        if(isGooglePlayServicesAvailable())
            mGoogleApiClient.connect();
        else
            mGoogleApiClient.disconnect();
    }

    public void disconnect(){
        if(isGooglePlayServicesAvailable())
            mGoogleApiClient.disconnect();
        else
            mGoogleServiceListener.onDisconnected();
    }
    public boolean isGooglePlayServicesAvailable(){
        int availability = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);

        switch (availability){
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
                GooglePlayServicesUtil.getErrorDialog(availability,mActivity,REQUEST_CODE_AVAILABILITY).show();
                return false;
            default:return false;
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        mGoogleServiceListener.onConnected();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleServiceListener.onDisconnected();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution() ){
            try {
                connectionResult.startResolutionForResult(mActivity,REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                connect();
            }
        }else{
            mGoogleServiceListener.onDisconnected();
        }

    }
    public void handleActitvityResult(int requestCode, int resultCode, Intent data ){
        if(resultCode == Activity.RESULT_OK){
            connect();
        }else{
            mGoogleServiceListener.onDisconnected();
        }
    }

    public interface GoogleServiceListener{

        void onConnected();
        void onDisconnected();

    }


}
