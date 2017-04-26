package com.example.android.popularmovies1;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.android.popularmovies1.data.MoviesContract;
import com.example.android.popularmovies1.data.MoviesDbHelper;
import com.example.android.popularmovies1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.COLUMN_MOVIE_ID;
import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.COLUMN_TITLE;
import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.TABLE_NAME;


public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler{

    private static final String TAG = MainActivity.class.getSimpleName();



    MovieAdapter movieAdapter;
    RecyclerView mMoviesList;
    public int mNumberItems;

    private SQLiteDatabase mDb;

    List<Movie> movies = new ArrayList<>();
    List<Movie> favorites;




    public boolean isFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MoviesDbHelper dbHelper = new MoviesDbHelper(this);


        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllFavorites();

        mMoviesList = (RecyclerView) findViewById(R.id.rv_images);
        GridLayoutManager layoutManager;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 4);
        }

        mMoviesList.setLayoutManager(layoutManager);


        movieAdapter = new MovieAdapter(this, cursor);

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
        isFavorite = movieAdapter.getFavorite();

        if (itemThatWasClickedId == R.id.most_popular) {

            movieAdapter.setFavorite(false);

            Movie movie;
            Movie movie2;
            for (int i = 0; i < mNumberItems; i++) {
                for (int j = i + 1; j < mNumberItems - 1; j++) {

                    movie = movies.get(i);
                    movie2 = movies.get(j);
                    double movie_d = Double.parseDouble((movie2.getPopularity()));
                    double movie2_d = Double.parseDouble((movie.getPopularity()));

                    if (Double.compare(movie2_d, movie_d) < 0) {
                        movies.remove(j);
                        movies.add(i, movie2);
                    }
                }
            }

            movieAdapter.notifyDataSetChanged();
            Context context = MainActivity.this;
            String textToShow = "Sorted by most popular";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();

        } else if (itemThatWasClickedId == R.id.highest_rated) {

            movieAdapter.setFavorite(false);

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

        } else if (itemThatWasClickedId == R.id.favorites) {

            //makeTheQuery();
            movieAdapter.setFavorite(true);

            Context context = MainActivity.this;

            movieAdapter.setMovies(movies);
            movieAdapter.notifyDataSetChanged();

            String textToShow = "Here is the favorite list";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }


    private void makeTheQuery() {
        URL SearchUrl = NetworkUtils.buildUrl();
        new TheMovieAsyncTask().execute(SearchUrl);

    }


    public class TheMovieAsyncTask extends AsyncTask<URL, Void, String> {


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
            if (jsonData != null) {
                try {

                    //System.out.println("http://image.tmdb.org/t/p/" + picSize + moviePath);
                    JSONObject obj = new JSONObject(jsonData);
                    JSONArray results = obj.getJSONArray("results");


                    if (!isFavorite) {
                        //iterate through JSON object and set fields to strings
                        for (int i = 0; i < results.length(); i++) {

                            Movie movie;

                            JSONObject resultsData = results.getJSONObject(i);

                            String originalTitle = resultsData.getString("original_title");
                            String synopsis = resultsData.getString("overview");
                            String userRating = resultsData.getString("vote_average");
                            String releaseDate = resultsData.getString("release_date");
                            String popularity = resultsData.getString("popularity");
                            String id = resultsData.getString("id");
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
                            movie.setId(id);

                            movies.add(movie);

                            mNumberItems = results.length();

                            movieAdapter.setMovies(movies);
                            movieAdapter.notifyDataSetChanged();
                        }

                    } else {
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject resultsData = results.getJSONObject(i);

                            String originalTitle = resultsData.getString("original_title");
                            String synopsis = resultsData.getString("overview");
                            String userRating = resultsData.getString("vote_average");
                            String releaseDate = resultsData.getString("release_date");
                            String popularity = resultsData.getString("popularity");
                            String id = resultsData.getString("id");
                            String moviePath = resultsData.getString("poster_path").replace("\\Tasks", "");
                            String picSize = "w185";



                                if (ExistsInDb(id)) {
                                    Movie movie;

                                    movie = new Movie();
                                    movie.setMoviePath(moviePath);
                                    movie.setOriginalTitle(originalTitle);
                                    movie.setPicSize(picSize);
                                    movie.setReleaseDate(releaseDate);
                                    movie.setSynopsis(synopsis);
                                    movie.setUserRating(userRating);
                                    movie.setPopularity(popularity);
                                    movie.setId(id);

                                    movies.add(movie);

                                    mNumberItems = results.length();

                                }
                                movieAdapter.setMovies(movies);
                                movieAdapter.notifyDataSetChanged();
                            }
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }}



    /*public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            public Cursor loadInBackground() {
                // Will implement to load data.

                // COMPLETED (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MoviesContract.CONTENT_URI,
                            null,
                            null,
                            null,
                            MoviesContract.MovieslistEntry.COLUMN_MOVIE_ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };

    }}

    */


    private Cursor getAllFavorites(){
        return mDb.query(
                MoviesContract.MovieslistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE
        );
    }

    public boolean ExistsInDb(String searchItem){

        String[] columns = {COLUMN_MOVIE_ID};
        String selection = COLUMN_MOVIE_ID+ " =?";
        String[] selectionArgs = {searchItem};
        String limit = "1";

        Cursor cursor = mDb.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean existsInDb = (cursor.getCount() > 0);
        cursor.close();
        return existsInDb;

    }

}