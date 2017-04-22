package com.example.android.popularmovies1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Maryline on 3/26/2017.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.example.android.popularmovies1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE = "favorites";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();



    public static final class MovieslistEntry implements BaseColumns {


        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_SYNOPSIS= "synopsis";
        public static final String COLUMN_USER_RATING = "userRating";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";


    }
}

















