package com.planetmovie.data.remote.model


import com.google.gson.annotations.SerializedName

data class MovieCredits(
    @SerializedName("cast")
    val movieCast: List<MovieCast>
)