package com.example.pranavsrivatsav.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pranavsrivatsav.popularmovies.data.MovieContract;

/**
 * Created by Pranav Srivatsav on 2/16/2017.
 */

public class MovieCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG="MovieCursorLoader";
    private Context mContext;
    private MovieAdapter mAdapter;
    private ProgressBar mProgress;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private int mPosition;
    public static final int ID_FAVOURITE_LOADER=44;

    public MovieCursorLoader(Context context, MovieAdapter adapter, ProgressBar progress, TextView errorMessage,RecyclerView mRecyclerView, int mPosition) {
        Log.i(LOG_TAG,"INSIDE CONST");
        mContext=context;
        mAdapter=adapter;
        mProgress=progress;
        mErrorMessage=errorMessage;
        this.mRecyclerView=mRecyclerView;
        this.mPosition=mPosition;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case ID_FAVOURITE_LOADER:{
                return new CursorLoader(mContext,
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG,"INSIDE CURSOR ADAPTER ONLOAD FINISHED");
        if(data==null || data.getCount()==0){
            Log.i(LOG_TAG,"mAdapter set to null");
            mAdapter.setMovie_poster_list(null);
            mProgress.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.VISIBLE);
        }
        else{
            data.moveToFirst();
            Log.i(LOG_TAG,data.getString(3));
            mAdapter.setMovie_poster_list(data,data.getCount());
            mProgress.setVisibility(View.GONE);
            if(mPosition== RecyclerView.NO_POSITION)
                mPosition=0;
            mRecyclerView.scrollToPosition(mPosition);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setMovie_poster_list(null);
    }

}
