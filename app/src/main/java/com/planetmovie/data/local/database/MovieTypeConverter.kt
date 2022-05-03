package com.planetmovie.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.planetmovie.data.remote.model.MovieResponse

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

}