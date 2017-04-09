package com.example.android.popularmovies1.utilities;

/**
 * Created by Maryline on 2/19/2017.
 */



    import android.net.Uri;

    import com.example.android.popularmovies1.ChildActivity;
    import com.example.android.popularmovies1.Movie;

    import java.io.IOException;
    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.MalformedURLException;
    import java.net.URL;
    import java.util.Scanner;

    import static android.R.attr.id;

/**
     * These utilities will be used to communicate with the network.
     */
    public class NetworkUtils {



        final static String MOVIE_BASE_URL =
                "http://api.themoviedb.org/3/movie/";

        final static String APIKey = "3288276d5ac9532fc0574989eacd03e3";


        /*
         * The sort field. One of stars, forks, or updated.
         * Default: results are sorted by best match if no field is specified.
         */

        /**
         * Builds the URL used to query Github.
         *
         *  The keyword that will be queried for.
         * @return The URL to use to query the weather server.
         */
        public static URL buildUrl() {
            // COMPLETED (1) Fill in this method to build the proper Github query URL
            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath("popular")
                    .appendQueryParameter("api_key", APIKey)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            System.out.println(url);
            return url;
        }

        public static URL buildUrlVideo(String id) {
            // COMPLETED (1) Fill in this method to build the proper Github query URL
            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(id)
                    .appendPath("videos")
                    .appendQueryParameter("api_key", APIKey)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            System.out.println(url);
            return url;
        }

    public static URL buildUrlReviews(String id) {
        // COMPLETED (1) Fill in this method to build the proper Github query URL
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath("reviews")
                .appendQueryParameter("api_key", APIKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(url);
        return url;
    }

        /**
         * This method returns the entire result from the HTTP response.
         *
         * @param url The URL to fetch the HTTP response from.
         * @return The contents of the HTTP response.
         * @throws IOException Related to network and stream reading
         */
        public static String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
    }

