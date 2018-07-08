package com.ruben.android.sanples.googleplayservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ruben.android.sanples.googleplayservices.api.Etsy;
import com.ruben.android.sanples.googleplayservices.model.ActiveListings;

public class MainActivity extends AppCompatActivity {
private RecyclerView mRecyclerView;
private View mProgressBar;
private TextView mErrorView;
    ListingAdapter mListingAdapter;
    public static final String STATE_ACTIVE_LISTININGS = "StateActiveListinings";
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActiveListings activeListings = mListingAdapter.getActiveListings();
        if(activeListings!=null){
            outState.putParcelable(STATE_ACTIVE_LISTININGS, activeListings);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycleView);
        mProgressBar = findViewById(R.id.progressBar);
        mErrorView = findViewById(R.id.error_view);
        mListingAdapter = new ListingAdapter(this);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mListingAdapter);
        if(savedInstanceState == null) {
            showLoanding();
            Etsy.getActiveListings(mListingAdapter);
        }else{
            if(savedInstanceState.containsKey(STATE_ACTIVE_LISTININGS)){
                mListingAdapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTININGS), null);
                showList();
            }else{
                showLoanding();
                Etsy.getActiveListings(mListingAdapter);
            }
        }

    }

    public void showLoanding(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }
    public void showList(){
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
    }

    public void showError(){
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
