package com.ruben.android.sanples.googleplayservices.api;

import com.ruben.android.sanples.googleplayservices.model.ActiveListings;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;


public class Etsy {

    private static final String API_KEY = "dpp5epbs6cthx94xxhjit3b7";

    private static RequestInterceptor getInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addEncodedQueryParam("api_key", API_KEY);
            }
        };
    }

    private static  Api getApi(){
        return new RestAdapter.Builder().

                setEndpoint("https://openapi.etsy.com/v2").
                setRequestInterceptor(getInterceptor()).
                build().
                create(Api.class);

    }

    public static void getActiveListings(Callback<ActiveListings> callback) {

        getApi().activeListenings("Images,Shop",callback);

    }

}
