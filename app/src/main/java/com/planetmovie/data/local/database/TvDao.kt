package com.planetmovie.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.planetmovie.data.local.entity.TvAiringTodayEntity
import com.planetmovie.data.local.entity.TvPopularEntity
import com.planetmovie.data.local.entity.TvTopRatedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TvDao {

    // TV ENTITY
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvAiringToday(tvAiringTodayEntity: TvAiringTodayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvPopular(tvPopularEntity: TvPopularEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvTopRated(tvTopRatedEntity: TvTopRatedEntity)

    @Query("SELECT * FROM tv_airing_today_table")
    fun readTvAiringToday(): Flow<List<TvAiringTodayEntity>>

    @Query("SELECT * FROM tv_popular_table")
    fun readTvPopular(): Flow<List<TvPopularEntity>>

    @Query("SELECT * FROM tv_top_rated_table")
    fun readTvTopRated(): Flow<List<TvTopRatedEntity>>

    // TV FAVORITE ENTITY
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertFavoriteTv(tvFavoriteEntity: TvFavoriteEntity)

//    @Query("SELECT * FROM tv_favorite_table ORDER BY id ASC")
//    fun readFavoriteTv(): Flow<List<TvFavoriteEntity>>

//    @Delete
//    suspend fun deleteFavoriteTv(tvFavoriteEntity: TvFavoriteEntity)

//    @Query("DELETE FROM tv_favorite_table")
//    suspend fun deleteAllFavoriteTv()
}