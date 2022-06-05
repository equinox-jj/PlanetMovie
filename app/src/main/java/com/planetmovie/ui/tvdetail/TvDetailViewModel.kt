package com.planetmovie.ui.tvdetail

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.planetmovie.data.Resource
import com.planetmovie.data.local.entity.TvFavoriteEntity
import com.planetmovie.data.remote.model.MovieDetailResponse
import com.planetmovie.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /* ROOM DATABASE */
    val getFavoriteTv: LiveData<List<TvFavoriteEntity>> = repository.localData.getFavoriteTv().asLiveData()

    fun insertFavoriteTv(movieDetailResponse: MovieDetailResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertFavoriteTv(movieDetailResponse)
        }

    fun deleteFavoriteTv(tvId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.deleteFavoriteTv(tvId)
        }

    /* RETROFIT */
    var tvDetail: MutableLiveData<Resource<MovieDetailResponse>> = MutableLiveData()

    fun getTvDetail(queries: Int) = viewModelScope.launch {
        tvDetail.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getTvDetail(queries)
                tvDetail.value = handleMoviesResponse(response)
            } catch (e: Exception) {
                tvDetail.value = Resource.Error("Tv Not Found.")
            }
        } else {
            tvDetail.value = Resource.Error("No Internet Connection.")
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