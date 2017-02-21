package com.example.pranavsrivatsav.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private String LOG_TAG=MainActivity.class.getSimpleName();
    private String sortChoice;
    private SharedPreferences sharedPreferences;
    private int mPosition=RecyclerView.NO_POSITION;
    private LoaderManager loaderManager=getSupportLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.animator.slide_from_left,R.animator.slide_to_right);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null && savedInstanceState.containsKey(getString(R.string.InstanceState_GridPosition)))
            mPosition=savedInstanceState.getInt(getString(R.string.InstanceState_GridPosition));
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_movies);
        mLoadingIndicator=(ProgressBar)findViewById(R.id.progressbar_main);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            Log.i(LOG_TAG,"lANDSCAPE");
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            if(getResources().getBoolean(R.bool.isbigtablet))
                layoutManager.setSpanCount(2);
            else
                layoutManager.setSpanCount(1);
            mRecyclerView.setLayoutManager(layoutManager);
        }else{
            Log.i(LOG_TAG,"PORTRAIT");
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            if(getResources().getBoolean(R.bool.isphone))
                layoutManager.setSpanCount(2);
            else
                layoutManager.setSpanCount(3);
            mRecyclerView.setLayoutManager(layoutManager);
        }
        Log.i(LOG_TAG,""+mPosition);
        mAdapter=new MovieAdapter(this);
        mErrorMessage=(TextView)findViewById(R.id.error_message);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        Log.i(LOG_TAG,"Finished on create");
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        sortChoice=sharedPreferences.getString(getString(R.string.sort_pref_key),getString(R.string.sort_popular));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        loadMoviegrid();
    }

    private void loadMoviegrid(){
        Log.i(LOG_TAG,"Inside loadMoviegrid");
        showMovieGrid();
        LoadMovies(sortChoice);
        Log.i(LOG_TAG,sortChoice);
    }

    private void showMovieGrid(){
        Log.i(LOG_TAG,"Inside ShowMoviegrid");
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }


    public void LoadMovies(String sortChoice){
        Log.i(LOG_TAG, sortChoice);
        Log.i(LOG_TAG, "lOAD MOVIES POSITION:"+mPosition);
        if(sortChoice.equals(getString(R.string.sort_favourites))){
            if(loaderManager.getLoader(MovieCursorLoader.ID_FAVOURITE_LOADER)==null)
            mErrorMessage.setVisibility(View.INVISIBLE);
                loaderManager.initLoader(MovieCursorLoader.ID_FAVOURITE_LOADER,
                        null,
                        new MovieCursorLoader(this,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,mPosition));

        }
        else{
                if(loaderManager.getLoader(MovieAsyncLoader.ID_MOVIE_LOADER)==null)
                loaderManager.initLoader(MovieAsyncLoader.ID_MOVIE_LOADER,
                    null,
                    new MovieAsyncLoader(this,sortChoice,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,mPosition));
        }

    }

    public void ReloadMovies(String sortChoice){
        if(sortChoice.equals(getString(R.string.sort_favourites))){
            mErrorMessage.setVisibility(View.INVISIBLE);
//            loaderManager.destroyLoader(MovieAsyncLoader.ID_MOVIE_LOADER);
            Log.i(LOG_TAG,"INSIDE FAV PART");
            if(loaderManager.getLoader(MovieCursorLoader.ID_FAVOURITE_LOADER)==null)
                loaderManager.initLoader(MovieCursorLoader.ID_FAVOURITE_LOADER,
                        null,
                        new MovieCursorLoader(this,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,0));
            else
                loaderManager.restartLoader(MovieCursorLoader.ID_FAVOURITE_LOADER,
                        null,
                        new MovieCursorLoader(this,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,0));

        }
        else{
            Log.i(LOG_TAG,"INSIDE ASYNC PART");
//            loaderManager.destroyLoader(MovieCursorLoader.ID_FAVOURITE_LOADER);
            if(loaderManager.getLoader(MovieAsyncLoader.ID_MOVIE_LOADER)==null)
                loaderManager.initLoader(MovieAsyncLoader.ID_MOVIE_LOADER,
                        null,
                        new MovieAsyncLoader(this,sortChoice,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,0));

            else
                loaderManager.restartLoader(MovieAsyncLoader.ID_MOVIE_LOADER,
                        null,
                        new MovieAsyncLoader(this,sortChoice,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,0));
        }
    }

    public void RefreshMovies(){
        if(sortChoice.equals(getString(R.string.sort_favourites))){
            mErrorMessage.setVisibility(View.INVISIBLE);
            loaderManager.restartLoader(MovieCursorLoader.ID_FAVOURITE_LOADER,
                    null,
                    new MovieCursorLoader(this,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,mPosition));
        }
        else
            loaderManager.restartLoader(MovieAsyncLoader.ID_MOVIE_LOADER,
                    null,
                    new MovieAsyncLoader(this,sortChoice,mAdapter,mLoadingIndicator,mErrorMessage,mRecyclerView,mPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_grid,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh_grid){
            mAdapter.setMovie_poster_list(null);
            RefreshMovies();
            return true;
        }

        if(item.getItemId()==R.id.sort){
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnClick(Movie movie) {
        Intent intent=new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra("MOVIEID",movie.getMovieId());
        intent.putExtra("TITLE",movie.getTitle());
        intent.putExtra("YEAR",movie.getYear());
        intent.putExtra("POSTER",movie.getPoster());
        intent.putExtra("RATING",Double.toString(movie.getRating()));
        intent.putExtra("RELEASE",movie.getReleaseDate());
        intent.putExtra("SYNOPSIS",movie.getSynopsis());
        startActivity(intent);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt(getString(R.string.InstanceState_GridPosition));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPosition = ((GridLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        Log.i(LOG_TAG,"Restored mPosition:"+mPosition);
        outState.putInt(getString(R.string.InstanceState_GridPosition),mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals("sort_by"))
            sortChoice=sharedPreferences.getString(getString(R.string.sort_pref_key),getString(R.string.sort_popular));
        Log.i(LOG_TAG,"SORT CHOICE: "+sortChoice);
        Log.i(LOG_TAG, "PREFERENCE CHANGE lOAD MOVIES POSITION:"+mPosition);
        ReloadMovies(sortChoice);

    }


    public static class MovieAsyncLoader implements LoaderManager.LoaderCallbacks<Movie[]> {

        private static final String LOG_TAG=MovieAsyncLoader.class.getSimpleName();
        private Context mContext;
        private MovieAdapter mAdapter;
        public static final int ID_MOVIE_LOADER=45;
        private static String mSortChoice;
        private Movie[] mMoviearray;
        private ProgressBar mLoadingIndicator;
        private TextView mErrorMessage;
        private RecyclerView mRecyclerView;
        private int mPosition;

        public MovieAsyncLoader(Context mContext, String mSortChoice, MovieAdapter mAdapter, ProgressBar mLoadingIndicator, TextView mErrorMessage, RecyclerView mRecyclerView, int mPosition) {
            this.mContext = mContext;
            this.mAdapter = mAdapter;
            this.mLoadingIndicator = mLoadingIndicator;
            this.mErrorMessage = mErrorMessage;
            this.mRecyclerView = mRecyclerView;
            this.mSortChoice=mSortChoice;
            this.mPosition=mPosition;
        }

        private void showMovieGrid(){
            Log.i(LOG_TAG,"Inside ShowMoviegrid");
            mErrorMessage.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage(){
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessage.setVisibility(View.VISIBLE);
        }

        @Override
        public Loader<Movie[]> onCreateLoader(int id, Bundle args) {
            return new android.support.v4.content.AsyncTaskLoader<Movie[]>(mContext) {

                @Override
                protected void onStartLoading() {
                    Log.i(LOG_TAG,"Entered onPreExecute");

                    if(mMoviearray!=null){
                        deliverResult(mMoviearray);
                        Log.i("AsyncLoader","mMoviearray in cache");
                    }

                    else{
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                }

                @Override
                public void deliverResult(Movie[] data) {
                    mMoviearray=data;
                    super.deliverResult(data);
                }

                @Override
                public Movie[] loadInBackground() {
                    Log.i(LOG_TAG,"Entered doInBackground");
                    String jsonString=null;
                    URL url=null;
                    try {
                        url=NetworkUtils.buildTmdbUrl(mSortChoice);
                        jsonString=NetworkUtils.getResponseFromHttpUrl(url);

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e(LOG_TAG,"Error Fetching data");
                        return null;
                    }

                    return JsonUtils.getJsonMovieInfo(jsonString);
                }


            };
        }

        @Override
        public void onLoadFinished(Loader<Movie[]> loader, Movie[] data) {
            Log.i(LOG_TAG,"Entered PostExecute");
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(data==null)
                showErrorMessage();
            else{

                Log.i(LOG_TAG,"COUNT:"+ Integer.toString(data.length));
                mAdapter.setMovie_poster_list(data);
                Log.i(LOG_TAG,"MPOSITION:"+mPosition);
                if(mPosition!=-1)
                    mRecyclerView.scrollToPosition(mPosition);
                showMovieGrid();

            }
        }

        @Override
        public void onLoaderReset(Loader<Movie[]> loader) {
            mAdapter.setMovie_poster_list(null);
            mMoviearray=null;
        }
    }


}
