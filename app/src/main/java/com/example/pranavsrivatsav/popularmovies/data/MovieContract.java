package com.example.pranavsrivatsav.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Pranav Srivatsav on 2/14/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY="com.example.pranavsrivatsav.popularmovies";

    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_MOVIES="movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME="movie";

        public static final String COLUMN_MOVIE_ID="movie_id";

        public static final String COLUMN_MOVIE_TITLE="movie_title";

        public static final String COLUMN_RATING="rating";

        public static final String COLUMN_SYNOPSIS="synopsis";

        public static final String COLUMN_RELEASE_DATE="release_date";

        public static final String COLUMN_YEAR="year";

        public static final String COLUMN_POSTER_PATH="poster_path";

        public static Uri buildMovieUriWithId(int id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }
}
