package com.ruben.android.sanples.googleplayservices.api;

import com.ruben.android.sanples.googleplayservices.model.ActiveListings;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Api {

    @GET("/listings/active")
    void activeListenings(@Query("includes") String includes,
                          Callback<ActiveListings> callback);
}
