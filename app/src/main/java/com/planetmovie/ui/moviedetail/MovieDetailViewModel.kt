package com.planetmovie.ui.moviedetail

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.planetmovie.data.Resource
import com.planetmovie.data.local.entity.MovieFavoriteEntity
import com.planetmovie.data.remote.model.MovieDetailResponse
import com.planetmovie.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /* ROOM DATABASE */
    val getFavoriteMovie: LiveData<List<MovieFavoriteEntity>> = repository.localData.getFavoriteMovie().asLiveData()

    fun insertFavoriteMovie(movieDetailResponse: MovieDetailResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertFavoriteMovie(movieDetailResponse)
        }

    fun deleteFavoriteMovie(movieId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.deleteFavoriteMovie(movieId)
        }

    /* RETROFIT*/
    var movieDetail: MutableLiveData<Resource<MovieDetailResponse>> = MutableLiveData()

    fun getMovieDetail(queries: Int) = viewModelScope.launch {
        movieDetail.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getMovieDetail(queries)
                movieDetail.value = handleMoviesResponse(response)
            } catch (e: Exception) {
                movieDetail.value = Resource.Error("Movies Not Found.")
            }
        } else {
            movieDetail.value = Resource.Error("No Internet Connection.")
        }
    }

    private fun handleMoviesResponse(response: Response<MovieDetailResponse>): Resource<MovieDetailResponse> {
        return when {
            response.message().toString().contains("Timeout") -> Resource.Error("Timeout")
            response.code() == 402 -> Resource.Error("Api Key Limited.")
            response.isSuccessful -> {
                val movieDetail = response.body()
                Resource.Success(movieDetail!!)
            }
            else -> Resource.Error(response.message())
        }
    }

    /** TO CHECK THE CONNECTIVITY*/
    private fun hasConnectivity(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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