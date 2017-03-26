package com.example.android.popularmovies1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies1.data.MoviesContract.*;

/**
 * Created by Maryline on 3/26/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieslist.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIESLIST_TABLE = "CREATE TABLE " + MovieslistEntry.TABLE_NAME + " (" +
                MovieslistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieslistEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieslistEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                MovieslistEntry.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                MovieslistEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieslistEntry.COLUMN_POPULARITY + " TEXT NOT NULL, " +
                "); ";

        // Execute the query by calling execSQL on sqLiteDatabase and pass the string query SQL_CREATE_MOVIESLIST_TABLE
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIESLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieslistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
