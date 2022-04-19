package com.planetmovie.ui.movie

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.planetmovie.data.Repository
import com.planetmovie.data.Resource
import com.planetmovie.data.local.entity.MovieNowPlayingEntity
import com.planetmovie.data.local.entity.MoviePopularEntity
import com.planetmovie.data.local.entity.MovieUpcomingEntity
import com.planetmovie.data.remote.model.MovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE
     * VARIABLE*/
    val readMoviesNowPlaying: LiveData<List<MovieNowPlayingEntity>> = repository.localData.readMovieNowPlaying().asLiveData()
    val readMoviesPopular: LiveData<List<MoviePopularEntity>> = repository.localData.readMoviePopular().asLiveData()
    val readMoviesUpcoming: LiveData<List<MovieUpcomingEntity>> = repository.localData.readMovieUpcoming().asLiveData()

    private fun insertMoviesNowPlaying(moviesNowPlayingEntity: MovieNowPlayingEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertMovieNowPlaying(moviesNowPlayingEntity)
        }

    private fun insertMoviesPopular(moviesPopularEntity: MoviePopularEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertMoviePopular(moviesPopularEntity)
        }

    private fun insertMoviesUpcoming(moviesUpcomingEntity: MovieUpcomingEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertMovieUpcoming(moviesUpcomingEntity)
        }

    /** RETROFIT
     * VARIABLE*/
    var movieNowPlaying: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var moviePopular: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var movieUpcoming: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var searchMovie: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()

    fun getMoviesNowPlaying() = viewModelScope.launch {
        movieNowPlaying.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getMovieNowPlaying()
                movieNowPlaying.value = handleMoviesResponse(response)
                /** ROOM DATABASE */
                val movieNowPlaying = movieNowPlaying.value!!.data
                if (movieNowPlaying != null) {
                    offlineCacheNowPlaying(movieNowPlaying)
                }
            } catch (e: Exception) {
                movieNowPlaying.value = Resource.Error("Movies Now Playing Not Found.")
            }
        } else {
            movieNowPlaying.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getMoviesPopular() = viewModelScope.launch {
        moviePopular.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getMoviePopular()
                moviePopular.value = handleMoviesResponse(response)
                /** ROOM DATABASE */
                val moviePopularData = moviePopular.value!!.data
                if (moviePopularData != null) {
                    offlineCachePopular(moviePopularData)
                }
            } catch (e: Exception) {
                moviePopular.value = Resource.Error("Movies Popular Not Found.")
            }
        }
        else {
            moviePopular.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getMoviesUpcoming() = viewModelScope.launch {
        movieUpcoming.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getMovieUpcoming()
                movieUpcoming.value = handleMoviesResponse(response)
                /** ROOM DATABASE */
                val movieUpcomingData = movieUpcoming.value!!.data
                if (movieUpcomingData != null) {
                    offlineCacheUpcoming(movieUpcomingData)
                }
            } catch (e: Exception) {
                movieUpcoming.value = Resource.Error("Movies Upcoming Not Found.")
            }
        }
        else {
            movieUpcoming.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getSearchMovie(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchMovie.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val responseSearchMovie = repository.remoteData.getSearchMovie(searchQuery)
                searchMovie.value = handleMoviesResponse(responseSearchMovie)
            } catch (e: Exception) {
                searchMovie.value = Resource.Error("Movies Not Found.")
            }
        } else {
            searchMovie.value = Resource.Error("No Internet Connection.")
        }
    }

    /** ROOM DATABASE */
    private fun offlineCacheNowPlaying(movieNowPlayingData: MovieResponse) {
        val movieEntity = MovieNowPlayingEntity(movieNowPlayingData)
        insertMoviesNowPlaying(movieEntity)
    }

    private fun offlineCachePopular(moviePopularData: MovieResponse) {
        val moviePopularEntity = MoviePopularEntity(moviePopularData)
        insertMoviesPopular(moviePopularEntity)
    }

    private fun offlineCacheUpcoming(movieUpcomingData: MovieResponse) {
        val movieUpcomingEntity = MovieUpcomingEntity(movieUpcomingData)
        insertMoviesUpcoming(movieUpcomingEntity)
    }

    /** TO HANDLE THE RESPONSE MOVIE DATA*/
    private fun handleMoviesResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        return when {
            response.message().toString().contains("Timeout") -> Resource.Error("Timeout")
            response.code() == 402 -> Resource.Error("Api Key Limited.")
            response.body()!!.movieResults.isNullOrEmpty() -> Resource.Error("Movies Not Found.")
            response.isSuccessful -> {
                val moviesData = response.body()
                Resource.Success(moviesData!!)
            }
            else -> Resource.Error(response.message())
        }
    }

    /** TO CHECK THE CONNECTIVITY*/
    private fun hasConnectivity(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}
