package com.example.pranavsrivatsav.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pranavsrivatsav.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static android.R.attr.id;
import static java.lang.Boolean.FALSE;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private String LOG_TAG = DetailActivity.class.getSimpleName();
    private static boolean ShowReviews = FALSE;

    private ScrollView detailScroll;
    //private ScrollView reviewScroll;

    private String mMovieId;
    private String mMovieTitle;
    private String mMovieYear;
    private String mMovieRating;
    private String mMovieRelease;
    private String mMovieSynopsis;
    private String mMoviePoster;

    private TextView mTitleText;
    private ImageView mPoster;
    private TextView mRatingText;
    private TextView mReleaseText;
    private TextView mSynopsis;
    //private TextView mReviews;
    private Button mButton;
    private Button mFavourite;
    private Toast mToast;
    private ContentValues cv;
    private RatingBar mRatingBar;
    private RecyclerView mTrailerRecycler;
    private RecyclerView mReviewRecycler;
    private TrailerAdapter mAdapter;
    private ReviewAdapter mReviewAdapter;
    private TextView mTrailerErrorMessage;
    private TextView mReviewErrorMessage;
//    private Button mlikeButton;
//
//    int reviewlistLength;


    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.animator.slide_from_left,R.animator.slide_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.slide_from_left,R.animator.slide_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.animator.slide_from_right,R.animator.slide_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailScroll = (ScrollView) findViewById(R.id.detail_scroll);
        //reviewScroll = (ScrollView) findViewById(R.id.review_scroll);
        mTrailerRecycler = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        mReviewRecycler = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mTrailerRecycler.setLayoutManager(layoutManager);
        mAdapter=new TrailerAdapter(this);
        mTrailerRecycler.setHasFixedSize(true);
        mTrailerRecycler.setAdapter(mAdapter);
        mReviewRecycler.setLayoutManager(layoutManager1);
        mReviewAdapter=new ReviewAdapter();
        mReviewRecycler.setHasFixedSize(true);
        mReviewRecycler.setAdapter(mReviewAdapter);

//        mlikeButton = (Button) findViewById(R.id.love_button) ;
        mToast=new Toast(this);
        //mButton = (Button) findViewById(R.id.Movie_ToggleReview);
        mTitleText = (TextView) findViewById(R.id.Movie_title);
        mPoster = (ImageView) findViewById(R.id.Movie_poster);
        mRatingText = (TextView) findViewById(R.id.Movie_Rating);
        mReleaseText = (TextView) findViewById(R.id.Movie_date);
        mSynopsis = (TextView) findViewById(R.id.Movie_synopsis);
        //mReviews = (TextView) findViewById(R.id.Movie_reviews);
        mFavourite = (Button) findViewById(R.id.Movie_favourite);
        mRatingBar = (RatingBar) findViewById(R.id.Movie_RatingBar);

        mTrailerErrorMessage = (TextView) findViewById(R.id.detail_trailer_error);
        mReviewErrorMessage = (TextView) findViewById(R.id.detail_review_error);

        cv=new ContentValues();

        Intent intent = getIntent();
        if (intent.hasExtra("MOVIEID")){
            mMovieId=intent.getStringExtra("MOVIEID");

            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,mMovieId);
        }

        if (intent.hasExtra("TITLE")){
            mMovieTitle=intent.getStringExtra("TITLE");
            mMovieYear=intent.getStringExtra("YEAR");
            mTitleText.setText(mMovieTitle+" ("+mMovieYear+")");
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,mMovieTitle);
            cv.put(MovieContract.MovieEntry.COLUMN_YEAR,mMovieYear);
        }

        if (intent.hasExtra("RATING")){
            mMovieRating=intent.getStringExtra("RATING");
            mRatingText.setText(mMovieRating);
            mRatingBar.setRating(Float.parseFloat(mMovieRating));
            cv.put(MovieContract.MovieEntry.COLUMN_RATING,Double.parseDouble(mMovieRating));
        }

        if (intent.hasExtra("RELEASE")){
            mMovieRelease=intent.getStringExtra("RELEASE");
            mReleaseText.setText("Released on "+mMovieRelease);
            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,mMovieRelease);
        }

        Log.i(LOG_TAG, mReleaseText.getText().toString());

        if (intent.hasExtra("SYNOPSIS")){
            mMovieSynopsis=intent.getStringExtra("SYNOPSIS");
            mSynopsis.setText(mMovieSynopsis);
            cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS,mMovieSynopsis);
        }

        if (intent.hasExtra("POSTER")){
            mMoviePoster=intent.getStringExtra("POSTER");
            Picasso.with(this).load(mMoviePoster).into(mPoster);
            cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,mMoviePoster);
        }



//
//        new FetchReviewList().execute(mMovieId);
//        new FetchTrailers().execute(mMovieId);
        LoadData(mMovieId);
        new CheckFavourite().execute();

    }

    public void LoadData(String mMovieId){
        if(getSupportLoaderManager().getLoader(TrailerAsyncLoader.ID_TRAILER_LOADER)==null){
            Log.i(LOG_TAG,"Initiating trailer loader ");
            getSupportLoaderManager().initLoader(TrailerAsyncLoader.ID_TRAILER_LOADER
                    ,null
                    ,new TrailerAsyncLoader(this,mAdapter,mTrailerRecycler,mTrailerErrorMessage,mMovieId));
        }
        else{
            getSupportLoaderManager().restartLoader(TrailerAsyncLoader.ID_TRAILER_LOADER
                    ,null
                    ,new TrailerAsyncLoader(this,mAdapter,mTrailerRecycler,mTrailerErrorMessage,mMovieId));
        }

        if(getSupportLoaderManager().getLoader(ReviewAsyncLoader.ID_REVIEW_LOADER)==null){
            Log.i(LOG_TAG,"Initiating review loader");
            getSupportLoaderManager().initLoader(ReviewAsyncLoader.ID_REVIEW_LOADER
                    ,null
                    ,new ReviewAsyncLoader(this,mReviewAdapter,mReviewRecycler,mReviewErrorMessage,mMovieId));
        }

        else{
            getSupportLoaderManager().restartLoader(ReviewAsyncLoader.ID_REVIEW_LOADER
                    ,null
                    ,new ReviewAsyncLoader(this,mReviewAdapter,mReviewRecycler,mReviewErrorMessage,mMovieId));
        }

    }


    public class FetchReviewList extends AsyncTask<String,Void,Review[]>{

        @Override
        protected Review[] doInBackground(String... strings) {
            Log.i(LOG_TAG,"Entered doInBackground");
            String jsonString=null;
            URL url=null;
            try {
                url=NetworkUtils.buildReviewUrl(strings[0]);
                jsonString=NetworkUtils.getResponseFromHttpUrl(url);

            }catch (Exception e){
                e.printStackTrace();
                Log.e(LOG_TAG,"Error Fetching data");
                return null;
            }

            return JsonUtils.getJsonReviewInfo(jsonString);

        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if(reviews==null || reviews.length==0){
                Log.i(LOG_TAG,"REVIEWS ARE NULL");
                mReviewRecycler.setVisibility(View.GONE);
                mReviewErrorMessage.setVisibility(View.VISIBLE);
            }
            else {
                mReviewAdapter.setReviewList(reviews);
                mReviewErrorMessage.setVisibility(View.GONE);
                mReviewRecycler.setVisibility(View.VISIBLE);
            }


        }
    }

    public class FetchTrailers extends AsyncTask<String,Void,Trailer[]>{

        @Override
        protected Trailer[] doInBackground(String... strings) {
            Log.i(LOG_TAG,"Entered doInBackground");
            String jsonString=null;
            URL url=null;
            try {
                url=NetworkUtils.buildTrailerUrl(strings[0]);
                jsonString=NetworkUtils.getResponseFromHttpUrl(url);

            }catch (Exception e){
                e.printStackTrace();
                Log.e(LOG_TAG,"Error Fetching data");
                return null;
            }

            return JsonUtils.getJsonTrailerInfo(jsonString);

        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            if(trailers == null || trailers.length==0){
                Log.i(LOG_TAG,"TRAILERS ARE NULL");
                mTrailerRecycler.setVisibility(View.GONE);
                mTrailerErrorMessage.setVisibility(View.VISIBLE);
            }

            else{
                mAdapter.setTrailerList(trailers);
                mTrailerRecycler.setVisibility(View.VISIBLE);
                mTrailerErrorMessage.setVisibility(View.GONE);
            }
        }
    }

    public class CheckFavourite extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            int count;
            Uri uri=MovieContract.MovieEntry.CONTENT_URI.buildUpon()
                    .appendPath(mMovieId)
                    .build();
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            if(cursor==null)
                count=0;
            else
                count=cursor.getCount();
            if(count==0)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
                mFavourite.setText("-Favourite");
            else
                mFavourite.setText("+Favourite");
        }
    }



//    public void toggleReviews(View view){
//        if(ShowReviews){
//            ShowReviews=false;
//            mButton.setText(getString(R.string.show_reviews));
//            mReviewErrorMessage.setVisibility(View.GONE);
//            mReviewRecycler.setVisibility(View.GONE);
//        }
//
//        else {
//            ShowReviews=true;
//            mButton.setText(R.string.reviews_hide);
//            Log.i(LOG_TAG,"reviewlistsize="+mReviewAdapter.getReviewlistSize());
//            if(mReviewAdapter.getReviewlistSize()==0)
//            {
//                mReviewErrorMessage.setVisibility(View.VISIBLE);
//            }else{
//
//                mReviewErrorMessage.setVisibility(View.VISIBLE);
//            }
//        }
//
//    }

    public void toggleFavourites(View view){
        if(mFavourite.getText().equals("-Favourite")){
            int delrows=0;
            Uri uri=MovieContract.MovieEntry.CONTENT_URI.buildUpon()
                    .appendPath(mMovieId)
                    .build();
            delrows=getContentResolver().delete(uri,null,null);
            Log.i(LOG_TAG,Integer.toString(delrows));
            if(delrows>0){
                mFavourite.setText("+Favourite");
                /*if(mToast!=null)
                    mToast.cancel();
                mToast.makeText(this,"Removed from Favourites",Toast.LENGTH_LONG);
                mToast.show();*/
                if(mToast!=null){
                    Log.i(LOG_TAG,"MTOAST NOT NULL");
                    mToast.cancel();
                }
                mToast.makeText(this,"Removed from Favourites",Toast.LENGTH_SHORT).show();
            }else{
                /*if(mToast!=null)
                    mToast.cancel();
                mToast.makeText(this,"Unable to Remove from Favourites",Toast.LENGTH_LONG);
                mToast.show();*/
                if(mToast!=null){
                    Log.i(LOG_TAG,"MTOAST NOT NULL");
                    mToast.cancel();
                }
                mToast.makeText(this,"Unable to Remove from Favourites",Toast.LENGTH_SHORT).show();
            }

        }else{
//            mlikeButton.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
            Uri retUri;
            retUri=getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,cv);
            Log.i(LOG_TAG,retUri.toString());
            if(retUri==null){
                /*if(mToast!=null)
                    mToast.cancel();
                mToast.makeText(this,"Unable to Add to Favourites",Toast.LENGTH_LONG);
                mToast.show();*/
                if(mToast!=null){
                    Log.i(LOG_TAG,"MTOAST NOT NULL");
                    mToast.cancel();
                }
                mToast.makeText(this,"Unable to Add to Favourites",Toast.LENGTH_SHORT).show();
            }
            else{
                mFavourite.setText("-Favourite");
                /*if(mToast!=null)
                    mToast.cancel();
                mToast.makeText(this,"Added To Favourites",Toast.LENGTH_LONG);
                mToast.show();*/
                if(mToast!=null){
                    Log.i(LOG_TAG,"MTOAST NOT NULL");
                    mToast.cancel();
                }

                mToast.makeText(this,"Added To Favourites",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void OnClick(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getSource().toString()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);

        }
    };
}
