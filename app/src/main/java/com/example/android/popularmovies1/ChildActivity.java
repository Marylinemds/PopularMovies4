package com.example.android.popularmovies1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmovies1.data.MoviesContentProvider;
import com.example.android.popularmovies1.data.MoviesContract;
import com.example.android.popularmovies1.data.MoviesDbHelper;
import com.squareup.picasso.Picasso;

import static android.R.attr.id;
import static android.R.id.input;
import static android.R.id.title;
import static com.example.android.popularmovies1.R.id.release_date;
import static com.example.android.popularmovies1.R.id.sypnosis;
import static com.example.android.popularmovies1.R.id.vote_average;
import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.COLUMN_TITLE;
import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.TABLE_NAME;


/**
 * Created by Maryline on 2/22/2017.
 */

public class ChildActivity extends AppCompatActivity {

    ImageView movieDisplay;
    TextView originalTitle_tv;
    TextView originalTitle_tv_2;
    TextView releaseDate_tv;
    TextView voteAverage_tv;
    TextView sypnosis_tv;
    Movie movie;
    RatingBar voteAverage_rb;

    String voteAverage;

    private SQLiteDatabase mDb;

    MoviesDbHelper mMoviesDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        movieDisplay = (ImageView) findViewById(R.id.display_movie);
        originalTitle_tv = (TextView) findViewById(R.id.movie_title);
        originalTitle_tv_2 = (TextView) findViewById(R.id.movie_title_2);
        releaseDate_tv= (TextView) findViewById(R.id.release_date);
        voteAverage_tv = (TextView) findViewById(R.id.vote_average_2);
        voteAverage_rb = (RatingBar) findViewById(R.id.vote_average);
        sypnosis_tv = (TextView) findViewById(sypnosis);



        Intent startChildActivityIntent = getIntent();

        if (startChildActivityIntent != null) {
            if (startChildActivityIntent.hasExtra("MyClass")) {
               movie = startChildActivityIntent.getParcelableExtra("MyClass");
                String moviePath = movie.getMoviePath();

                originalTitle_tv.setText(movie.getOriginalTitle());
                originalTitle_tv_2.setText(movie.getOriginalTitle());
                releaseDate_tv.setText(movie.getReleaseDate().substring(0,4));
                voteAverage = movie.getUserRating();
                voteAverage_rb.setRating((Float.valueOf(voteAverage))/2);
                voteAverage_tv.setText("(" + movie.getUserRating() + "/10)");
                sypnosis_tv.setText(movie.getSynopsis());


                Picasso.with(this).load("http://image.tmdb.org/t/p/" + "w185" + moviePath).into(movieDisplay);
            }
        }

        mMoviesDbHelper = new MoviesDbHelper(this);
        mDb = mMoviesDbHelper.getWritableDatabase();

    }

    public boolean ExistsInDb(String searchItem){

        String[] columns = {COLUMN_TITLE};
        String selection = COLUMN_TITLE + " =?";
        String[] selectionArgs = {searchItem};
        String limit = "1";

        Cursor cursor = mDb.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean existsInDb = (cursor.getCount() > 0);
        cursor.close();
        return existsInDb;

    }

    public void OnClickAddFavorite(View view){

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_USER_RATING, movie.getUserRating());
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        Uri uri = getContentResolver().insert(MoviesContract.MovieslistEntry.CONTENT_URI, contentValues);


        ToggleButton  ToggleButton = (ToggleButton) findViewById(R.id.favourite_button);

        if (!ToggleButton.isActivated()){
            Toast.makeText(this, "added to favorites", Toast.LENGTH_SHORT).show();
            ToggleButton.setActivated(true);
            ToggleButton.setChecked(true);

            mDb.insert(TABLE_NAME, null, contentValues);


        }else{
            Toast.makeText(this, "removed from favorites", Toast.LENGTH_SHORT).show();
            ToggleButton.setActivated(false);
            ToggleButton.setChecked(false);

            mDb.delete(TABLE_NAME, MoviesContract.MovieslistEntry._ID + "=" + id ,null);

        }


    }


}
