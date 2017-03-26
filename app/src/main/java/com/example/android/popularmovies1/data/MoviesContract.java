package com.example.android.popularmovies1.data;

import android.provider.BaseColumns;

/**
 * Created by Maryline on 3/26/2017.
 */

public class MoviesContract {

    public static final class MovieslistEntry implements BaseColumns {
        public static final String TABLE_NAME = "movieslist";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_SYNOPSIS= "synopsis";
        public static final String COLUMN_USER_RATING = "userRating";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_POPULARITY = "popularity";

    }
}
