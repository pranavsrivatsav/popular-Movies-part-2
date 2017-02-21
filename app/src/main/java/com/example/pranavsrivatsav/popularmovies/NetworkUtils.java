package com.example.pranavsrivatsav.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pranav Srivatsav on 2/12/2017.
 */

public final class NetworkUtils {
    private static final String LOG_TAG=NetworkUtils.class.getSimpleName();
    private static final String SCHEME="https";
    private static final String AUTHORITY="api.themoviedb.org";
    private static final String THUMBNAIL_AUTHORITY="img.youtube.com";
    private static final String APPENDPATHTHUMBNAIL="vi";
    private static final String APPENDIMAGEPATHTHUMBNAIL="0.jpg";
    private static final String APPENDPATHREVIEW="reviews";
    private static final String APPENDTRAILERREVIEW="trailers";
    private static final String APIKEY="<<api_key>>";

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String jsonString;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line + "\n");
            if (buffer.length() == 0)
                return null;
            jsonString = buffer.toString();
            Log.i(LOG_TAG,"Json String:"+jsonString);
            inputStream.close();
            return jsonString;
        }finally {
            urlConnection.disconnect();
        }

    }




    public static URL buildTmdbUrl (String Pivot){

        try{
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(SCHEME);
            builder.authority(AUTHORITY);
            builder.appendPath("3");
            builder.appendPath("movie");
            builder.appendPath(Pivot);
            builder.appendQueryParameter("api_key", APIKEY);
            String urlpath = builder.build().toString();
            Log.i(LOG_TAG,"URL PATH:"+urlpath);
            URL url = new URL(urlpath);
            return url;
        }catch (Exception e){
            Log.e(LOG_TAG,"Error building TMDB List URL");
            return null;
        }

    }

    public static URL buildReviewUrl (String Movieid){

        try{
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(SCHEME);
            builder.authority(AUTHORITY);
            builder.appendPath("3");
            builder.appendPath("movie");
            builder.appendPath(Movieid);
            builder.appendPath(APPENDPATHREVIEW);
            builder.appendQueryParameter("api_key", APIKEY);
            String urlpath = builder.build().toString();
            Log.i(LOG_TAG,"URL PATH:"+urlpath);
            URL url = new URL(urlpath);
            return url;

        }catch (Exception e){
            Log.e(LOG_TAG,"Error building TMDB Review URL");
            return null;
        }
    }

    public static URL buildTrailerUrl (String Movieid){

        try{
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(SCHEME);
            builder.authority(AUTHORITY);
            builder.appendPath("3");
            builder.appendPath("movie");
            builder.appendPath(Movieid);
            builder.appendPath(APPENDTRAILERREVIEW);
            builder.appendQueryParameter("api_key", APIKEY);
            String urlpath=builder.build().toString();
            Log.i(LOG_TAG,"URL PATH:"+urlpath);
            URL url = new URL(urlpath);
            return url;
        }catch (Exception e){
            Log.e(LOG_TAG,"Error handling TMDB Trailer URL");
            return null;
        }
    }

    public static URL buildThumbnailUrl (String VideoId){
        try{
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(SCHEME);
            builder.authority(THUMBNAIL_AUTHORITY);
            builder.appendPath(APPENDPATHTHUMBNAIL);
            builder.appendPath(VideoId);
            builder.appendPath(APPENDIMAGEPATHTHUMBNAIL);
            String urlpath=builder.build().toString();
            Log.i(LOG_TAG,"URL PATH:"+urlpath);
            URL url = new URL(urlpath);
            return url;
        }catch (Exception e){
            Log.e(LOG_TAG,"Error loading thumbnail URL");
            return null;
        }
    }





}
