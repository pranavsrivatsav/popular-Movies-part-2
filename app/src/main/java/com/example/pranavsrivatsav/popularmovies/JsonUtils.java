package com.example.pranavsrivatsav.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Pranav Srivatsav on 2/13/2017.
 */

public final class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();
    private static final String POSTERPATHSTATIC="http://image.tmdb.org/t/p/w342/";

    public static Movie[] getJsonMovieInfo(String jsonString){
        try{

            JSONObject jsonParent=new JSONObject(jsonString);
            JSONArray jsonResults=jsonParent.getJSONArray("results");
            Movie[] container=new Movie[jsonResults.length()];
            String mTitle,mYear,mReleaseDate,mSynopsis,mPoster,poster_path,mId;
            Double mRating;
            for(int i=0;i<jsonResults.length();i++){
                mId= Integer.toString(jsonResults.getJSONObject(i).getInt("id"));
                mTitle=jsonResults.getJSONObject(i).getString("title");
                mReleaseDate=jsonResults.getJSONObject(i).getString("release_date");
                mYear=mReleaseDate.substring(0,4);
                mSynopsis=jsonResults.getJSONObject(i).getString("overview");
                mRating=jsonResults.getJSONObject(i).getDouble("vote_average");
                poster_path=jsonResults.getJSONObject(i).getString("poster_path").substring(1);
                mPoster=POSTERPATHSTATIC+poster_path;
                container[i]=new Movie(mId,mTitle,mRating,mSynopsis,mReleaseDate,mYear,mPoster);
                Log.i(LOG_TAG,container[i].toString());
            }

            return container;

        }catch(Exception e){
            e.printStackTrace();
            Log.v(LOG_TAG,"Error parsing json Movie Info");
            return null;
        }

    }

    public static Review[] getJsonReviewInfo(String jsonString){
        try{
            JSONObject jsonParent=new JSONObject(jsonString);
            JSONArray jsonResults=jsonParent.getJSONArray("results");
            Review[] container=new Review[jsonResults.length()];
            String mAuthor,mContent;
            for(int i=0;i<jsonResults.length();i++){
                mAuthor=jsonResults.getJSONObject(i).getString("author");
                mContent=jsonResults.getJSONObject(i).getString("content");
                container[i]=new Review(mAuthor,mContent);
                Log.i(LOG_TAG,container[i].toString());
            }

            return container;

        }catch (Exception e){
            e.printStackTrace();
            Log.e(LOG_TAG,"Error parsing Json Review Info");
            return null;
        }
    }

    public static Trailer[] getJsonTrailerInfo(String jsonString){
        try{
            JSONObject jsonParent=new JSONObject(jsonString);
            JSONArray jsonResults=jsonParent.getJSONArray("youtube");
            Trailer[] container=new Trailer[jsonResults.length()];
            String mTitle,mSource;
            for(int i=0;i<jsonResults.length();i++){
                mTitle=jsonResults.getJSONObject(i).getString("name");
                mSource=jsonResults.getJSONObject(i).getString("source");
                container[i]=new Trailer(mTitle,mSource);
                Log.i(LOG_TAG,container[i].toString());
            }
            return container;
        }catch(Exception e){
            e.printStackTrace();
            Log.e(LOG_TAG,"Error parsing Json Trailer Info");
            return null;
        }
    }
}
