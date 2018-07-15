package com.ruben.android.sanples.googleplayservices;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;
import com.ruben.android.sanples.googleplayservices.api.Etsy;
import com.ruben.android.sanples.googleplayservices.google.GoogleServiceHelper;
import com.ruben.android.sanples.googleplayservices.model.ActiveListings;
import com.ruben.android.sanples.googleplayservices.model.Listing;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingHolder> implements Callback<ActiveListings>, GoogleServiceHelper.GoogleServiceListener{

    public static final int REQUEST_CODE_PLUS_ONE = 10;
    public static final int  REQUEST_CODE_SHARE = 11;

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

        final Listing listing = mActiveListings.results[position];

        holder.titleView.setText(listing.title);
        holder.priceView.setText(listing.price);
        holder.shopNameView.setText(listing.Shop.shop_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openListing = new Intent(Intent.ACTION_VIEW);
                openListing.setData(Uri.parse(listing.url));
                mActivity.startActivity(openListing);
            }
        });

        if(isGooglePlayServicesAvailable){
            holder.plusOneButton.setVisibility(View.VISIBLE);
            holder.plusOneButton.initialize(listing.url, position);
            holder.plusOneButton.setAnnotation(PlusOneButton.ANNOTATION_NONE);
            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new PlusShare.Builder(mActivity)
                            .setType("text/plain")
                            .setText("Checkout this item on Etsy " + listing.title)
                            .setContentUrl(Uri.parse(listing.url))
                            .getIntent();

                    mActivity.startActivityForResult(intent, ListingAdapter.REQUEST_CODE_SHARE);
                }
            });
        }else{
            holder.plusOneButton.setVisibility(View.GONE);

            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_TEXT,"CheckOut this item on Etsy")
                            .setType("text/plain");


                    mActivity.startActivityForResult(Intent.createChooser(intent,"Share"), ListingAdapter.REQUEST_CODE_SHARE);
                }
            });        }




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
        public PlusOneButton plusOneButton;
        public ImageButton shareButton;

        public ListingHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.listing_image);
            titleView = itemView.findViewById(R.id.listing_title);
            shopNameView = itemView.findViewById(R.id.listing_shop_name);
            priceView = itemView.findViewById(R.id.listing_price);
            plusOneButton = itemView.findViewById(R.id.listing_plus_one_btn);
            shareButton = itemView.findViewById(R.id.listing_share_btn);
        }



    }
}
