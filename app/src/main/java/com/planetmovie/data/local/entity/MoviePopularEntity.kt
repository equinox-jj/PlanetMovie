package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.util.Constant.Companion.MOVIE_POPULAR_TABLE

@Entity(tableName = MOVIE_POPULAR_TABLE)
class MoviePopularEntity(
    var moviePopularData: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}