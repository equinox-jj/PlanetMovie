package com.planetmovie.data.remote.model


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    val movieResults: List<MovieResult>,
)