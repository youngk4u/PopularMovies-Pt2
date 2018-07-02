package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    /* Tag for logs */
    private static final String TAG = NetworkUtils.class.getSimpleName();
    /* Base URL */
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    /* Popular list */
    private static final String POPULAR_SEARCH = "popular";
    /* Top rated list */
    private static final String TOP_RATED_SEARCH = "top_rated";

    /*************************************************
     API key : Please plug in your own API key below.
     *************************************************/
    private static final String API_KEY = "fa2106b6ca5510fc495fd33f8533d081";
    private static final String KEY_PARAM = "api_key";

    /* Base URL for themoviedb.org API */
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    /* Path for poster size */
    private static final String POSTER_SIZE_PATH = "w185";

    private static boolean sInitialized = false;

    /**
     * Builds the URL to start a sorting by popularity.
     *
     * @return the URL to be used.
     */
    public static URL buildPopularListJsonUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(POPULAR_SEARCH)
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();
        return buildUrl(builtUri);
    }

    /**
     * Builds the URL to start a sorting by ratings.
     *
     * @return the URL to be used.
     */
    public static URL buildTopRatedListJsonUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TOP_RATED_SEARCH)
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .build();
        return buildUrl(builtUri);
    }

    /**
     * Builds the string path for fetching poster data.
     *
     * @param posterId the poster id string.
     * @return the String for the poster.
     */
    public static String buildPosterPath(String posterId) {
        return IMAGE_BASE_URL + POSTER_SIZE_PATH + posterId;
    }


    /**
     * This method converts to URL from Uri.
     *
     * @param uri the Uri from built path.
     * @return the URL to be used.
     */
    private static URL buildUrl(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }

    /**
     * This method returns the result from the HTTP response.
     *
     * @param url the URL to fetch the HTTP response from.
     * @return the contents of the HTTP response, null if no response.
     * @throws IOException Related to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        if (!sInitialized) {
            CookieHandler.setDefault(new CookieManager());
            sInitialized = true;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
