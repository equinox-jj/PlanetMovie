package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.util.Constant.MOVIE_UPCOMING_TABLE

@Entity(tableName = MOVIE_UPCOMING_TABLE)
class MovieUpcomingEntity(
    var movieUpcomingData: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}