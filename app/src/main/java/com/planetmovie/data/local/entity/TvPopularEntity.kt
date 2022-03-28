package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.util.Constant.Companion.TV_POPULAR_TABLE

@Entity(tableName = TV_POPULAR_TABLE)
class TvPopularEntity(
    var tvPopularData: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}