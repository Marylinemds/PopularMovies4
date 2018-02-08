package com.example.android.popularmovies1;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.popularmovies1.data.MoviesContract;
import com.example.android.popularmovies1.data.MoviesDbHelper;
import com.example.android.popularmovies1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.popularmovies1.R.id.sypnosis;
import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.COLUMN_TITLE;
import static com.example.android.popularmovies1.data.MoviesContract.MovieslistEntry.TABLE_NAME;


/**
 * Created by Maryline on 2/22/2017.
 */

public class ChildActivity extends AppCompatActivity implements VideoAdapter.ListItemClickHandler{

    WebView mReviewsList;
    RecyclerView mVideosList;

    VideoAdapter videoAdapter;


    ImageView movieDisplay;
    TextView originalTitle_tv;
    TextView originalTitle_tv_2;
    TextView releaseDate_tv;
    TextView voteAverage_tv;
    TextView sypnosis_tv;
    Movie movie;
    RatingBar voteAverage_rb;

    ToggleButton ToggleButton;

    String voteAverage;

    private SQLiteDatabase mDb;
    List<Video> videos = new ArrayList<>();
    List<Review> reviews = new ArrayList<>();

    MoviesDbHelper mMoviesDbHelper;

    public String id;


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
                id = movie.getId();
                System.out.println("blabla " + id );


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

        //TestUtil.insertFakeData(mDb);


        ToggleButton = (ToggleButton) findViewById(R.id.favourite_button);

        if (ExistsInDb(movie.getOriginalTitle())){
            ToggleButton.setActivated(true);
            ToggleButton.setChecked(true);
        }

        mReviewsList = (WebView) findViewById(R.id.wv_reviews);
        mVideosList = (RecyclerView) findViewById(R.id.rv_videos);

        GridLayoutManager layoutManager;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 4);
        }

        mVideosList.setLayoutManager(layoutManager);


        videoAdapter = new VideoAdapter(this);

        Cursor cursor = getAllFavorites();



       mVideosList.setAdapter(videoAdapter);

        makeTheQueryVideos();
        makeTheQueryReviews();


    }

    public void makeTheQueryVideos(){

        URL SearchUrl = NetworkUtils.buildUrlVideo(id);
        String searchUrl = SearchUrl.toString();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String JSONData) {
                        if (JSONData != null) {

                            try {

                                JSONObject objJSON = new JSONObject(JSONData);
                                JSONArray results = objJSON.getJSONArray("results");
                                Video video;

                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject resultsData = results.getJSONObject(i);

                                    String key = resultsData.getString("key");
                                    String videoName = resultsData.getString("name");

                                    video = new Video();
                                    video.setKey(key);
                                    video.setVideoName(videoName);
                                    videos.add(video);

                                }

                                videoAdapter.setVideos(videos);
                                videoAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public void makeTheQueryReviews(){
        URL SearchUrl = NetworkUtils.buildUrlReviews(movie.getId());
        String searchUrl = SearchUrl.toString();
        System.out.println("blabla " + SearchUrl);
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String JSONData) {
                        if (JSONData != null) {

                            try {

                                JSONObject objJSON = new JSONObject(JSONData);
                                JSONArray results = objJSON.getJSONArray("results");
                                Review review;
                                String sumOfReviews = "";

                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject resultsData = results.getJSONObject(i);

                                    String content = resultsData.getString("content");
                                    String author = resultsData.getString("author");
                                    sumOfReviews = sumOfReviews + "<p><b>" + author + ": </b>"
                                            + content + " <p><hr />";


                                    review = new Review();
                                    review.setAuthor(author);
                                    review.setContent(content);

                                    reviews.add(review);


                                    mReviewsList.loadData("<html><body>" + sumOfReviews + "</body></html>", "text/html", "UTF-8");
                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mReviewsList.loadData("error", "text/html; UTF-8", null);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
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
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MoviesContract.MovieslistEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        ContentResolver contentResolver = getContentResolver();

        String id = getContentResolver().insert(MoviesContract.CONTENT_URI, contentValues).getPathSegments().get(1);;
        Uri uri = MoviesContract.CONTENT_URI.buildUpon().appendPath(id).build();
        Uri uri2 = MoviesContract.CONTENT_URI;
        System.out.println(uri);




        if (!ToggleButton.isActivated()){

            contentResolver.insert(uri2, contentValues);

            ToggleButton.setActivated(true);
            ToggleButton.setChecked(true);
            Toast.makeText(this, "added to favorites", Toast.LENGTH_SHORT).show();

        }else{

            String[] movieId = new String[]{String.valueOf( movie.getId())};
            contentResolver.delete(uri, MoviesContract.MovieslistEntry.COLUMN_MOVIE_ID + "=?",movieId);
            ToggleButton.setActivated(false);
            ToggleButton.setChecked(false);

            Toast.makeText(this, "removed from favorites", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onClick(String mVideoId) {
        // open video Youtube

        Matcher matcher = Pattern.compile("http://www.youtube.com/embed/").matcher(mVideoId);
        matcher.find();
        Intent VideoIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + mVideoId));
        startActivity(VideoIntent);
    }

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
}
