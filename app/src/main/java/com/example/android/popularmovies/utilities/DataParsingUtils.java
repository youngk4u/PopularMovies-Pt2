package com.example.android.popularmovies.utilities;

import android.graphics.Movie;
import android.util.Log;

import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Trailer;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.MovieResponse;

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
    public static MovieResponse getMovieDataListFromString(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray jsonArray = obj.getJSONArray("results");

            ArrayList<MovieEntry> movieEntryList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject movieJson = jsonArray.getJSONObject(i);
                    MovieEntry entry = parseMovieFromJson(movieJson);
                    movieEntryList.add(entry);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return new MovieResponse(movieEntryList);
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
    private static MovieEntry parseMovieFromJson(JSONObject json) {

        final String MOVIE_ID = "id";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_DATE = "release_date";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_POPULARITY = "popularity";

        try {
            int id = json.optInt(MOVIE_ID, 0);
            String originalTitle = json.optString(MOVIE_ORIGINAL_TITLE, "N/A");
            String title = json.optString(MOVIE_TITLE, "N/A");
            String poster = json.optString(MOVIE_POSTER);
            String overview = json.optString(MOVIE_OVERVIEW, "N/A");
            String date = json.optString(MOVIE_DATE, "N/A");

            double userRating = json.optDouble(MOVIE_RATING, 0);
            double popularity = json.optDouble(MOVIE_POPULARITY, 0);

            return new MovieEntry(id, originalTitle, title, poster, overview, date, userRating, popularity, 0);
        } catch (Throwable t) {
            Log.e(TAG, "Parse error with \"" + json + "\"");
            return null;
        }
    }

    public static List<Trailer> getTrailerListFromString(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray jsonArray = obj.getJSONArray("results");

            ArrayList<Trailer> trailerList = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject trailerJson = jsonArray.getJSONObject(i);
                    trailerList.add(parseTrailerFromJson(trailerJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return trailerList;
        } catch (Throwable t) {
            Log.e(TAG, "Could not parse malformed JSON: \"" + jsonString + "\"");
            return null;
        }
    }

    private static Trailer parseTrailerFromJson(JSONObject json) {

        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";

        try {
            String key = json.optString(TRAILER_KEY, "N/A");
            String name = json.optString(TRAILER_NAME, "N/A");

            return new Trailer(key, name);
        } catch (Throwable t) {
            Log.e(TAG, "Parse error with \"" + json + "\"");
            return null;
        }
    }
    public static List<Review> getReviewListFromString(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray jsonArray = obj.getJSONArray("results");

            ArrayList<Review> reviewList = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject reviewJson = jsonArray.getJSONObject(i);
                    reviewList.add(parseReviewFromJson(reviewJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return reviewList;
        } catch (Throwable t) {
            Log.e(TAG, "Could not parse malformed JSON: \"" + jsonString + "\"");
            return null;
        }
    }

    private static Review parseReviewFromJson(JSONObject json) {

        final String REVIEW_AUTHOR = "author";
        final String REVIEW_COMMENT = "content";

        try {
            String author = json.optString(REVIEW_AUTHOR, "N/A");
            String comment = json.optString(REVIEW_COMMENT, "N/A");

            return new Review(author, comment);
        } catch (Throwable t) {
            Log.e(TAG, "Parse error with \"" + json + "\"");
            return null;
        }
    }
}
