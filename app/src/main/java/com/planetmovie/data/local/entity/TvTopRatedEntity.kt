package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.util.Constant.Companion.TV_TOP_RATED_TABLE

@Entity(tableName = TV_TOP_RATED_TABLE)
class TvTopRatedEntity(
    var tvTopRatedData: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}