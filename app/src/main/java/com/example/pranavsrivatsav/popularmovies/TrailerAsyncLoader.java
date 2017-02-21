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

public class TrailerAsyncLoader implements LoaderManager.LoaderCallbacks<Trailer[]> {

    private final static String LOG_TAG="TrailerAsyncLoader";
    private Context mContext;
    private TrailerAdapter mAdapter;
    private RecyclerView mRecycler;
    private TextView mErrorMessage;
    private Trailer[] mTrailerArray;
    private String mMovieId;
    public static final int ID_TRAILER_LOADER=49;

    public TrailerAsyncLoader(Context mContext, TrailerAdapter mAdapter, RecyclerView mRecycler, TextView mErrorMessage, String mMovieId) {
        Log.i(LOG_TAG,"constructing trailerasyncloader");
        this.mContext = mContext;
        this.mAdapter = mAdapter;
        this.mRecycler = mRecycler;
        this.mErrorMessage = mErrorMessage;
        this.mMovieId = mMovieId;
    }

    @Override
    public Loader<Trailer[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Trailer[]>(mContext) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Trailer[] loadInBackground() {
                Log.i(LOG_TAG,"Entered doInBackground");
                String jsonString=null;
                URL url=null;
                try {
                    url=NetworkUtils.buildTrailerUrl(mMovieId);
                    jsonString=NetworkUtils.getResponseFromHttpUrl(url);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(LOG_TAG,"Error Fetching data");
                    return null;
                }

                return JsonUtils.getJsonTrailerInfo(jsonString);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Trailer[]> loader, Trailer[] data) {
        if(data == null || data.length==0){
            Log.i(LOG_TAG,"TRAILERS ARE NULL");
            mRecycler.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.VISIBLE);
        }

        else{
            Log.i(LOG_TAG,"COUNT:"+ Integer.toString(data.length));
            mAdapter.setTrailerList(data);
            mRecycler.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Trailer[]> loader) {

    }
}
