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
        UriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_FAVORITE + "/*", FAVORITE_WITH_ID);

        return UriMatcher;

    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new MoviesDbHelper(context);

        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();


        int match = sUriMatcher.match(uri);
        Cursor retCursor;


        switch (match) {

            case FAVORITES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;


        case FAVORITE_WITH_ID:
        // Get the id from the URI
        String id = uri.getPathSegments().get(1);

        // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
        String mSelection = "_id=?";
        String[] mSelectionArgs = new String[]{id};

        // Construct a query as you would normally, passing in the selection/args
        retCursor =  db.query(TABLE_NAME,
                projection,
                mSelection,
                mSelectionArgs,
                null,
                null,
                sortOrder);
        break;

        // Default exception
        default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Set a notification URI on the Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

    // Return the desired Cursor
        return retCursor;
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
                    returnUri = ContentUris.withAppendedId(MoviesContract.CONTENT_URI, id);
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
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int favoriteDeleted; // starts as 0

        // COMPLETED (2) Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVORITE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                favoriteDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // COMPLETED (3) Notify the resolver of a change and return the number of items deleted
        if (favoriteDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return favoriteDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
