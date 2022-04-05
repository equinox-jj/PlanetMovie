package com.planetmovie.util

import com.planetmovie.data.local.entity.MovieFavoriteEntity
import com.planetmovie.data.local.entity.TvFavoriteEntity
import com.planetmovie.data.remote.model.MovieDetailResponse

fun MovieDetailResponse.mapMovieDetailToMovieFavoriteEntity(): MovieFavoriteEntity {
    return MovieFavoriteEntity(
        id,
        overview,
        backdropPath,
        posterPath,
        title,
        voteAverage,
        voteCount,
        popularity,
        releaseDate
    )
}

fun MovieDetailResponse.mapTvDetailToTvFavoriteEntity(): TvFavoriteEntity {
    return TvFavoriteEntity(
        id,
        overview,
        backdropPath,
        posterPath,
        tvName,
        voteAverage,
        voteCount,
        popularity,
        tvFirstAirDate
    )
}