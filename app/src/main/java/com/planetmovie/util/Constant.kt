package com.planetmovie.util

class Constant {
    companion object {
        const val API_KEY = "[ PUT YOUR API KEY HERE ]"

        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMG_URL_BACKDROP = "https://image.tmdb.org/t/p/w780/"
        const val BASE_IMG_URL_POSTER = "https://image.tmdb.org/t/p/w342/"
        const val BASE_IMG_URL_CAST = "https://image.tmdb.org/t/p/w185/"
        const val BASE_TRAILER_URL = "https://www.youtube.com/watch?v="
        const val BASE_TRAILER_URL_APP = "vnd.youtube:"
        const val BASE_TRAILER_THUMBNAIL = "https://img.youtube.com/vi/"
        const val BASE_TRAILER_THUMBNAIL_END = "/hqdefault.jpg"

        // QUERY VALUE
        const val QUERY_PAGE = "page"
        const val QUERY_API = "api_key"
        const val QUERY_SEARCH = "query"
        const val QUERY_MOVIE_ID = "movie_id"
        const val QUERY_TV_ID = "tv_id"
        const val QUERY_APPEND_RESPONSE = "append_to_response"
        const val QUERY_APPEND = "videos,credits"

        // ROOM DATABASE
        const val DATABASE_NAME = "movie_database"
        const val MOVIE_NOW_PLAYING_TABLE = "movie_now_playing_table"
        const val MOVIE_POPULAR_TABLE = "movie_popular_table"
        const val MOVIE_UPCOMING_TABLE = "movie_upcoming_table"
        const val MOVIE_FAVORITE_TABLE = "movie_favorite_table"
        const val TV_AIRING_TODAY_TABLE = "tv_airing_today_table"
        const val TV_POPULAR_TABLE = "tv_popular_table"
        const val TV_TOP_RATED_TABLE = "tv_top_rated_table"
        const val TV_FAVORITE_TABLE = "tv_favorite_table"

        // DATA STORE
        const val PREFERENCE_NAME = "movie preference"
        const val PREFERENCE_BACK_ONLINE = "backOnline"
    }
}