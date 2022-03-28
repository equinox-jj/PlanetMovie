package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.data.remote.model.MovieDetailResponse
import com.planetmovie.util.Constant.Companion.TV_FAVORITE_TABLE

@Entity(tableName = TV_FAVORITE_TABLE)
class TvFavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var tvFavorite: MovieDetailResponse
)
