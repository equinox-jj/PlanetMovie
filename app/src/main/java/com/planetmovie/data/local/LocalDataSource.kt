package com.planetmovie.data.local

import com.planetmovie.data.local.database.MovieDao
import com.planetmovie.data.local.database.TvDao
import com.planetmovie.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource
@Inject
constructor(
    private val movieDao: MovieDao,
    private val tvDao: TvDao
) {
    // MOVIE LOCAL DATABASE
    fun readMovieNowPlaying(): Flow<List<MovieNowPlayingEntity>> {
        return movieDao.readMovieNowPlaying()
    }

    fun readMoviePopular(): Flow<List<MoviePopularEntity>> {
        return movieDao.readMoviePopular()
    }

    fun readMovieUpcoming(): Flow<List<MovieUpcomingEntity>> {
        return movieDao.readMovieUpcoming()
    }

    suspend fun insertMovieNowPlaying(moviesNowPlayingEntity: MovieNowPlayingEntity) {
        movieDao.insertMovieNowPlaying(moviesNowPlayingEntity)
    }

    suspend fun insertMoviePopular(moviesPopularEntity: MoviePopularEntity) {
        movieDao.insertMoviePopular(moviesPopularEntity)
    }

    suspend fun insertMovieUpcoming(moviesUpcomingEntity: MovieUpcomingEntity) {
        movieDao.insertMovieUpcoming(moviesUpcomingEntity)
    }

    // FAVORITE MOVIE
//    fun readFavoriteMovie(): Flow<List<MovieFavoriteEntity>> {
//        return movieDao.readFavoriteMovie()
//    }

//    suspend fun insertFavoriteMovie(movieFavoriteEntity: MovieFavoriteEntity) {
//        movieDao.insertFavoriteMovie(movieFavoriteEntity)
//    }

//    suspend fun deleteFavoriteMovie(movieFavoriteEntity: MovieFavoriteEntity) {
//        movieDao.deleteFavoriteMovie(movieFavoriteEntity)
//    }

//    suspend fun deleteAllFavoriteMovie() {
//        movieDao.deleteAllFavoriteMovie()
//    }

    // TV LOCAL DATABASE
    fun readTvAiringToday(): Flow<List<TvAiringTodayEntity>> {
        return tvDao.readTvAiringToday()
    }

    fun readTvPopular(): Flow<List<TvPopularEntity>> {
        return tvDao.readTvPopular()
    }

    fun readTvTopRated(): Flow<List<TvTopRatedEntity>> {
        return tvDao.readTvTopRated()
    }

    suspend fun insertTvAiringToday(tvAiringTodayEntity: TvAiringTodayEntity) {
        tvDao.insertTvAiringToday(tvAiringTodayEntity)
    }

    suspend fun insertTvPopular(tvPopularEntity: TvPopularEntity) {
        tvDao.insertTvPopular(tvPopularEntity)
    }

    suspend fun insertTvTopRated(tvTopRatedEntity: TvTopRatedEntity) {
        tvDao.insertTvTopRated(tvTopRatedEntity)
    }

    // FAVORITE TV
//    fun readFavoriteTv(): Flow<List<TvFavoriteEntity>> {
//        return tvDao.readFavoriteTv()
//    }
//
//    suspend fun insertFavoriteTv(tvFavoriteEntity: TvFavoriteEntity) {
//        tvDao.insertFavoriteTv(tvFavoriteEntity)
//    }
//
//    suspend fun deleteFavoriteTv(tvFavoriteEntity: TvFavoriteEntity) {
//        tvDao.deleteFavoriteTv(tvFavoriteEntity)
//    }
//
//    suspend fun deleteAllFavoriteTv() {
//        tvDao.deleteAllFavoriteTv()
//    }
}