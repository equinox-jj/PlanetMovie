package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.util.Constant.Companion.MOVIE_FAVORITE_TABLE

@Entity(tableName = MOVIE_FAVORITE_TABLE)
class MovieFavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val adult: Boolean,
    val backdropPath: String,
    val homepage: String,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
    val tvFirstAirDate: String,
    val tvName: String,
)