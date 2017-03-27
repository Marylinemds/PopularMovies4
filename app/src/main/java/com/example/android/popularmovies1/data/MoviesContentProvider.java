package com.example.android.popularmovies1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.TABLE_NAME;

/**
 * Created by Maryline on 3/26/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    public MoviesDbHelper mMoviesDbHelper;

    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher UriMatcher = new UriMatcher(android.content.UriMatcher.NO_MATCH);

        UriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVORITE, FAVORITES);
        UriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVORITE + "/#", FAVORITE_WITH_ID);

        return UriMatcher;

    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new MoviesDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();


        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:

                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MovieslistEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
