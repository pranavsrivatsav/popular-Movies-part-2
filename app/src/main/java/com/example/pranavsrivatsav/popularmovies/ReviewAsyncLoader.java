package com.example.pranavsrivatsav.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.net.URL;

/**
 * Created by Pranav Srivatsav on 2/19/2017.
 */

public class ReviewAsyncLoader implements LoaderManager.LoaderCallbacks<Review[]> {

    private final static String LOG_TAG="ReviewAsyncLoader";
    private Context mContext;
    private ReviewAdapter mAdapter;
    private RecyclerView mRecycler;
    private TextView mErrorMessage;
    private Trailer[] mReviewArray;
    private String mMovieId;
    public static final int ID_REVIEW_LOADER=50;

    public ReviewAsyncLoader(Context mContext, ReviewAdapter mAdapter, RecyclerView mRecycler, TextView mErrorMessage, String mMovieId) {
        Log.i(LOG_TAG,"constructing reviewasyncloader");
        this.mContext = mContext;
        this.mAdapter = mAdapter;
        this.mRecycler = mRecycler;
        this.mErrorMessage = mErrorMessage;
        this.mMovieId = mMovieId;
    }

    @Override
    public Loader<Review[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Review[]>(mContext) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Review[] loadInBackground() {
                Log.i(LOG_TAG,"Entered doInBackground");
                String jsonString=null;
                URL url=null;
                try {
                    url=NetworkUtils.buildReviewUrl(mMovieId);
                    jsonString=NetworkUtils.getResponseFromHttpUrl(url);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(LOG_TAG,"Error Fetching data");
                    return null;
                }

                return JsonUtils.getJsonReviewInfo(jsonString);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Review[]> loader, Review[] data) {
        if(data==null || data.length==0){
            Log.i(LOG_TAG,"REVIEWS ARE NULL");
            mRecycler.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.VISIBLE);
        }
        else {
            Log.i(LOG_TAG,"COUNT:"+ Integer.toString(data.length));
            mAdapter.setReviewList(data);
            mErrorMessage.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Review[]> loader) {

    }
}
