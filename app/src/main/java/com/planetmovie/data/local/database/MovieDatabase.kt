package com.planetmovie.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.planetmovie.data.local.entity.*

@Database(
    entities = [
        MovieNowPlayingEntity::class,
        MoviePopularEntity::class,
        MovieUpcomingEntity::class,
        MovieFavoriteEntity::class,
        TvAiringTodayEntity::class,
        TvPopularEntity::class,
        TvTopRatedEntity::class,
        TvFavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MovieTypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao

}