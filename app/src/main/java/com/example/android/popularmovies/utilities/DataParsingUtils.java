package com.example.android.popularmovies.utilities;

import android.util.Log;
import com.example.android.popularmovies.data.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DataParsingUtils {

    private static final String TAG = DataParsingUtils.class.getSimpleName();

    /**
     * Converts a JSON String of Movie data into a List of
     * Movie objects.
     *
     * @param jsonString a raw JSON String of Movie data
     * @return a List of Movie objects parsed from the jsonString
     */
    public static List<Movie> getMovieDataListFromString(String jsonString) {

        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray jsonArray = obj.getJSONArray("results");

            ArrayList<Movie> movieList = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject movieJson = jsonArray.getJSONObject(i);
                    movieList.add(parseMovieFromJson(movieJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return movieList;
        } catch (Throwable t) {
            Log.e(TAG, "Could not parse malformed JSON: \"" + jsonString + "\"");
            return null;
        }
    }

    /**
     * Parses a Movie object from a single JSON object.
     *
     * @param json the JSONObject to be parsed
     * @return the resulting Movie object
     */
    private static Movie parseMovieFromJson(JSONObject json) {

        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_DATE = "release_date";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_POPULARITY = "popularity";

        try {
            String originalTitle = json.optString(MOVIE_ORIGINAL_TITLE, "N/A");
            String title = json.optString(MOVIE_TITLE, "N/A");
            String poster = json.optString(MOVIE_POSTER);
            String overview = json.optString(MOVIE_OVERVIEW, "N/A");
            String date = json.optString(MOVIE_DATE, "N/A");

            double rating = json.optDouble(MOVIE_RATING, 0);
            double popularity = json.optDouble(MOVIE_POPULARITY, 0);

            return new Movie(originalTitle, title, poster, overview, date, rating, popularity);
        } catch (Throwable t) {
            Log.e(TAG, "Parse error with \"" + json + "\"");
            return null;
        }
    }
}
