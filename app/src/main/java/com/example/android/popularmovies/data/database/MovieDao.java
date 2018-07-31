package com.example.android.popularmovies.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.graphics.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void entryInsert(MovieEntry movie);

    @Query("SELECT * FROM movie ORDER BY dbId ASC")
    LiveData<List<MovieEntry>> getMovieEntry();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieEntry> getMovieByLiveId(int id);

    @Query("SELECT * FROM movie WHERE id = :id")
    MovieEntry getMovieById(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movie);

    @Query("SELECT * FROM movie WHERE favored")
    List<MovieEntry> getFavoriteMovie();
}
