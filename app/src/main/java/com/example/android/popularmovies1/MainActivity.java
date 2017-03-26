package com.example.android.popularmovies1;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

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




public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler {

    String movieTitle;
    String synopsis;
    String userRating;
    String releaseDate;
    String popularity;

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

        RecyclerView favoritesRecyclerView;

        favoritesRecyclerView = (RecyclerView) findViewById(R.id.favorites_list);



        MoviesDbHelper dbHelper = new MoviesDbHelper(this);

        if (mDb != null) {

            mDb = dbHelper.getWritableDatabase();
        }

        //Cursor cursor = getAllFavorites();
          Cursor cursor = new Cursor() {
              @Override
              public int getCount() {
                  return 0;
              }

              @Override
              public int getPosition() {
                  return 0;
              }

              @Override
              public boolean move(int i) {
                  return false;
              }

              @Override
              public boolean moveToPosition(int i) {
                  return false;
              }

              @Override
              public boolean moveToFirst() {
                  return false;
              }

              @Override
              public boolean moveToLast() {
                  return false;
              }

              @Override
              public boolean moveToNext() {
                  return false;
              }

              @Override
              public boolean moveToPrevious() {
                  return false;
              }

              @Override
              public boolean isFirst() {
                  return false;
              }

              @Override
              public boolean isLast() {
                  return false;
              }

              @Override
              public boolean isBeforeFirst() {
                  return false;
              }

              @Override
              public boolean isAfterLast() {
                  return false;
              }

              @Override
              public int getColumnIndex(String s) {
                  return 0;
              }

              @Override
              public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
                  return 0;
              }

              @Override
              public String getColumnName(int i) {
                  return null;
              }

              @Override
              public String[] getColumnNames() {
                  return new String[0];
              }

              @Override
              public int getColumnCount() {
                  return 0;
              }

              @Override
              public byte[] getBlob(int i) {
                  return new byte[0];
              }

              @Override
              public String getString(int i) {
                  return null;
              }

              @Override
              public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {

              }

              @Override
              public short getShort(int i) {
                  return 0;
              }

              @Override
              public int getInt(int i) {
                  return 0;
              }

              @Override
              public long getLong(int i) {
                  return 0;
              }

              @Override
              public float getFloat(int i) {
                  return 0;
              }

              @Override
              public double getDouble(int i) {
                  return 0;
              }

              @Override
              public int getType(int i) {
                  return 0;
              }

              @Override
              public boolean isNull(int i) {
                  return false;
              }

              @Override
              public void deactivate() {

              }

              @Override
              public boolean requery() {
                  return false;
              }

              @Override
              public void close() {

              }

              @Override
              public boolean isClosed() {
                  return false;
              }

              @Override
              public void registerContentObserver(ContentObserver contentObserver) {

              }

              @Override
              public void unregisterContentObserver(ContentObserver contentObserver) {

              }

              @Override
              public void registerDataSetObserver(DataSetObserver dataSetObserver) {

              }

              @Override
              public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

              }

              @Override
              public void setNotificationUri(ContentResolver contentResolver, Uri uri) {

              }

              @Override
              public Uri getNotificationUri() {
                  return null;
              }

              @Override
              public boolean getWantsAllOnMoveCalls() {
                  return false;
              }

              @Override
              public void setExtras(Bundle bundle) {

              }

              @Override
              public Bundle getExtras() {
                  return null;
              }

              @Override
              public Bundle respond(Bundle bundle) {
                  return null;
              }
          };

        // Link the adapter to the RecyclerView
//        favoritesRecyclerView.setAdapter(movieAdapter);

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

//        favoritesRecyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this, cursor);

        mMoviesList.setAdapter(movieAdapter);

        makeTheQuery();

    }

    public void addToFavoriteList(){
        addNewFavorite();
        //MovieAdapter.swapCursor(getAllFavorites());
    }

    //private Cursor getAllFavorites() {
      //  return mDb.query(
      //          MoviesContract.MovieslistEntry.TABLE_NAME,
      //          null,
      //          null,
      //         null,
      //          null,
      //          null,
      //          MoviesContract.MovieslistEntry.COLUMN_POPULARITY
      //  );
    //};

    private long addNewFavorite() {
        //Inside, create a ContentValues instance to pass the values onto the insert query
        ContentValues cv = new ContentValues();
        // COMPLETED (6) call put to insert the name value with the key COLUMN_GUEST_NAME
        cv.put(MoviesContract.MovieslistEntry.COLUMN_TITLE, movieTitle);
        // COMPLETED (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE
        cv.put(MoviesContract.MovieslistEntry.COLUMN_SYNOPSIS, synopsis);
        cv.put(MoviesContract.MovieslistEntry.COLUMN_USER_RATING, userRating);
        cv.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE, releaseDate);
        cv.put(MoviesContract.MovieslistEntry.COLUMN_POPULARITY, popularity);
        // COMPLETED (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
        return mDb.insert(MoviesContract.MovieslistEntry.TABLE_NAME, null, cv);
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

                }else if (itemThatWasClickedId == R.id.favorites) {
                    Context context = MainActivity.this;
                    String textToShow = "Here is the favorite list";
                    Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();

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
