package com.planetmovie.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.planetmovie.data.local.entity.MovieFavoriteEntity
import com.planetmovie.data.local.entity.MovieNowPlayingEntity
import com.planetmovie.data.local.entity.MoviePopularEntity
import com.planetmovie.data.local.entity.MovieUpcomingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // MOVIE ENTITY
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieNowPlaying(movieNowPlayingEntity: MovieNowPlayingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePopular(moviePopularEntity: MoviePopularEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieUpcoming(movieUpcomingEntity: MovieUpcomingEntity)

    @Query("SELECT * FROM movie_now_playing_table")
    fun readMovieNowPlaying(): Flow<List<MovieNowPlayingEntity>>

    @Query("SELECT * FROM movie_popular_table")
    fun readMoviePopular(): Flow<List<MoviePopularEntity>>

    @Query("SELECT * FROM movie_upcoming_table")
    fun readMovieUpcoming(): Flow<List<MovieUpcomingEntity>>

    // MOVIE FAVORITE ENTITY
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movieFavoriteEntity: MovieFavoriteEntity)

    @Query("SELECT * FROM movie_favorite_table ORDER BY id ASC")
    fun getFavoriteMovie(): Flow<List<MovieFavoriteEntity>>

    @Query("DELETE FROM movie_favorite_table WHERE id = :movieId")
    suspend fun deleteFavoriteMovie(movieId: Int)

    @Query("DELETE FROM movie_favorite_table")
    suspend fun deleteAllFavoriteMovie()
}