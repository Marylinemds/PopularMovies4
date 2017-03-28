package com.example.android.popularmovies1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmovies1.data.MoviesContract;
import com.squareup.picasso.Picasso;

import static android.R.id.input;
import static android.R.id.title;
import static com.example.android.popularmovies1.R.id.release_date;
import static com.example.android.popularmovies1.R.id.sypnosis;
import static com.example.android.popularmovies1.R.id.vote_average;


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



    }

    public void OnClickAddFavorite(View view){

        ContentValues contentValues = new ContentValues();

        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_USER_RATING, movie.getUserRating());
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        Uri uri = getContentResolver().insert(MoviesContract.MovieslistEntry.CONTENT_URI, contentValues);


        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }


        finish();
    }
}
