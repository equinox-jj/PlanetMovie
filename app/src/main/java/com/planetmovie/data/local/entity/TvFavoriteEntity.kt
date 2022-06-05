package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.util.Constant.TV_FAVORITE_TABLE

@Entity(tableName = TV_FAVORITE_TABLE)
class TvFavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    val overview: String?,
    val backdropPath: String?,
    val posterPath: String?,
    val tvName: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val popularity: Double?,
    val tvFirstAirDate: String?,
)
