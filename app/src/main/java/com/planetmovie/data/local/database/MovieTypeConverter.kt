package com.planetmovie.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult

class MovieTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun movieDataToString(movieData: MovieResponse): String {
        return gson.toJson(movieData)
    }

    @TypeConverter
    fun stringToMovieData(data: String): MovieResponse {
        val listType = object : TypeToken<MovieResponse>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun movieResultToString(movieResult: MovieResult): String {
        return gson.toJson(movieResult)
    }

    @TypeConverter
    fun stringToMovieResult(data: String): MovieResult {
        val listType = object : TypeToken<MovieResult>() {}.type
        return gson.fromJson(data, listType)
    }

}