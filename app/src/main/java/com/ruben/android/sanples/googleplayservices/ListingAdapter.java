package com.ruben.android.sanples.googleplayservices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruben.android.sanples.googleplayservices.api.Etsy;
import com.ruben.android.sanples.googleplayservices.google.GoogleServiceHelper;
import com.ruben.android.sanples.googleplayservices.model.ActiveListings;
import com.ruben.android.sanples.googleplayservices.model.Listing;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingHolder> implements Callback<ActiveListings>, GoogleServiceHelper.GoogleServiceListener{

    private LayoutInflater mInflater;
    private ActiveListings mActiveListings;
    private MainActivity mActivity;

    private boolean isGooglePlayServicesAvailable=false;

    public ListingAdapter(MainActivity mainActivity) {
        this.mActivity = mainActivity;
        mInflater = LayoutInflater.from(mainActivity);
    }

    @Override
    public ListingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ListingHolder(mInflater.inflate(R.layout.layout_listing,parent,false));
    }

    @Override
    public void onBindViewHolder(ListingHolder holder, int position) {

        Listing listing = mActiveListings.results[position];

        holder.titleView.setText(listing.title);
        holder.priceView.setText(listing.price);
        holder.shopNameView.setText(listing.Shop.shop_name);

        if(isGooglePlayServicesAvailable){

        }else{

        }


        Picasso.with(holder.imageView.getContext())
                .load(listing.Images[0].url_570xN)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (mActiveListings ==null)
            return 0;

        if (mActiveListings.results == null)
            return 0;

            return mActiveListings.results.length  ;
    }

    @Override
    public void success(ActiveListings activeListings, Response response) {
        this.mActiveListings = activeListings;
        notifyDataSetChanged();
        this.mActivity.showList();
    }

    @Override
    public void failure(RetrofitError error) {
        this.mActivity.showError();
    }

    public ActiveListings getActiveListings(){
        return mActiveListings;
    }

    @Override
    public void onConnected() {
        if(getItemCount()==0){
            Etsy.getActiveListings(this);
        }
        isGooglePlayServicesAvailable = true;
        notifyDataSetChanged();
    }

    @Override
    public void onDisconnected() {
        if(getItemCount()==0){
            Etsy.getActiveListings(this);
        }
        isGooglePlayServicesAvailable = false;
        notifyDataSetChanged();
    }

    class ListingHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView titleView;
        TextView shopNameView;
        TextView priceView;

        public ListingHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.listing_image);
            titleView = itemView.findViewById(R.id.listing_title);
            shopNameView = itemView.findViewById(R.id.listing_shop_name);
            priceView = itemView.findViewById(R.id.listing_price);
        }



    }
}
