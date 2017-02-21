package com.example.pranavsrivatsav.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Pranav Srivatsav on 2/14/2017.
 */

public class MovieProvider extends ContentProvider{

    public static final int CODE_MOVIES=100;
    public static final int CODE_MOVIES_WITH_ID=101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,MovieContract.PATH_MOVIES,CODE_MOVIES);
        matcher.addURI(authority,MovieContract.PATH_MOVIES+"/#",CODE_MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){

            case CODE_MOVIES:{

                cursor=mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            }

            case CODE_MOVIES_WITH_ID:{

                String movieId=uri.getLastPathSegment();
                String[] selectionArguments=new String[]{movieId};

                cursor=mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);


                break;

            }

            default:
                throw new UnsupportedOperationException("Unknown uri:" +uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;

        if(null==selection)
            selection="1";

        switch (sUriMatcher.match(uri)){

            case CODE_MOVIES:{
                numRowsDeleted= mOpenHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            }

            case CODE_MOVIES_WITH_ID:{

                String movieId=uri.getLastPathSegment();
                String[] selectionArguments=new String[]{movieId};

                numRowsDeleted=mOpenHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?",
                        selectionArguments);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);


        }

        if(numRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numRowsDeleted;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int MovieId;
        switch (sUriMatcher.match(uri)){

            case CODE_MOVIES:{
                db.beginTransaction();
                int rowsInserted=0;
                try{
                    MovieId=contentValues.getAsInteger(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                    long _id=db.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);
                    if(_id!=-1){
                        rowsInserted+=1;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }

                return MovieContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(MovieId))
                        .build();

            }

            default:{
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
            }

        }

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new RuntimeException("Not implemented");
    }
}
