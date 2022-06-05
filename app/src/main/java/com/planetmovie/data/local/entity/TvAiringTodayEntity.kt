package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.util.Constant.TV_AIRING_TODAY_TABLE

@Entity(tableName = TV_AIRING_TODAY_TABLE)
class TvAiringTodayEntity(
    var tvAiringTodayData: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}