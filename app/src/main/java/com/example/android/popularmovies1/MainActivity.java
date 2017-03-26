package com.example.android.popularmovies1;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmovies1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler {


    MovieAdapter movieAdapter;
    RecyclerView mMoviesList;
    public int mNumberItems;

    private SQLiteDatabase mDb;

    List<Movie> movies = new ArrayList<>();

    ToggleButton toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle = (ToggleButton)findViewById(R.id.favourite_button);


        mMoviesList = (RecyclerView) findViewById(R.id.rv_images);
        GridLayoutManager layoutManager;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this, 2);
        }
        else{
            layoutManager = new GridLayoutManager(this, 4);
        }

        mMoviesList.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this);

        mMoviesList.setAdapter(movieAdapter);

        makeTheQuery();

    }

    @Override
    public void onClick(int clickedItemIndex) {

        Movie movie = movies.get(clickedItemIndex);
        Context context = MainActivity.this;

        Class destinationActivity = ChildActivity.class;

        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra("MyClass", movie);
        startActivity(startChildActivityIntent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies, menu);

        return true;
    }

        public boolean onOptionsItemSelected(MenuItem item) {
                int itemThatWasClickedId = item.getItemId();
                if (itemThatWasClickedId == R.id.most_popular) {

                    Movie movie;
                    Movie movie2;
                    for (int i = 0; i < mNumberItems; i++) {
                        for (int j = i + 1; j < mNumberItems - 1; j++) {

                            movie = movies.get(i);
                            movie2 = movies.get(j);
                            double movie_d = Double.parseDouble((movie2.getPopularity()));
                            double movie2_d = Double.parseDouble((movie.getPopularity()));

                            if (Double.compare(movie2_d, movie_d)<0) {
                                movies.remove(j);
                                movies.add(i, movie2);
                            }
                        }
                    }

                    movieAdapter.notifyDataSetChanged();
                    Context context = MainActivity.this;
                    String textToShow = "Sorted by most popular";
                    Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();

                    }else if(itemThatWasClickedId == R.id.highest_rated) {

                    Movie movie;
                    Movie movie2;
                    for (int i = 0; i < mNumberItems; i++) {
                        for (int j = i + 1; j < mNumberItems - 1; j++) {

                            movie = movies.get(i);
                            movie2 = movies.get(j);

                            if ((movie2.getUserRating().compareTo(movie.getUserRating()) > 0)) {
                                movies.remove(j);
                                movies.add(i, movie2);
                            }
                        }

                    }

                    movieAdapter.notifyDataSetChanged();
                    Context context = MainActivity.this;
                    String textToShow = "Sorted by rate";
                    Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                    return true;

                }
             return super.onOptionsItemSelected(item);
           }





    private void makeTheQuery(){
        URL SearchUrl = NetworkUtils.buildUrl();
        new TheMovieAsyncTask().execute(SearchUrl);

    }


    public class TheMovieAsyncTask extends AsyncTask <URL, Void, String>{


        @Override
        protected String doInBackground(URL... params) {

            URL url = params[0];
            String movieData = null;
            try {
                movieData = NetworkUtils.getResponseFromHttpUrl(url);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return movieData;

        }
        @Override
        protected void onPostExecute(String jsonData) {


            System.out.println("JSON " + jsonData);
            if (jsonData !=null){
                try {
                    //System.out.println("http://image.tmdb.org/t/p/" + picSize + moviePath);
                    JSONObject obj = new JSONObject(jsonData);
                    JSONArray results = obj.getJSONArray("results");
                    Movie movie;

                    //iterate through JSON object and set fields to strings
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject resultsData = results.getJSONObject(i);

                        String originalTitle = resultsData.getString("original_title");
                        String synopsis = resultsData.getString("overview");
                        String userRating = resultsData.getString("vote_average");
                        String releaseDate =resultsData.getString("release_date");
                        String popularity = resultsData.getString("popularity");
                        String moviePath = resultsData.getString("poster_path").replace("\\Tasks", "");
                        String picSize = "w185";

                        movie = new Movie();
                        movie.setMoviePath(moviePath);
                        movie.setOriginalTitle(originalTitle);
                        movie.setPicSize(picSize);
                        movie.setReleaseDate(releaseDate);
                        movie.setSynopsis(synopsis);
                        movie.setUserRating(userRating);
                        movie.setPopularity(popularity);

                        movies.add(movie);

                        mNumberItems = results.length();

                    }

                    movieAdapter.setMovies(movies);
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


    }

}
