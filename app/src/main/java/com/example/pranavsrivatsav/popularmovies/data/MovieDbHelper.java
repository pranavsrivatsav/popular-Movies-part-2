package com.example.pranavsrivatsav.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pranav Srivatsav on 2/14/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " REAL NOT NULL," +
                        MovieContract.MovieEntry.COLUMN_RATING + " DOUBLE NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_YEAR + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_SYNOPSIS + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER_PATH + " REAL NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
