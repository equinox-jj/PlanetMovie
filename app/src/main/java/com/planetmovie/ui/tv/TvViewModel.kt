package com.planetmovie.ui.tv

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.planetmovie.data.Repository
import com.planetmovie.data.Resource
import com.planetmovie.data.local.entity.TvAiringTodayEntity
import com.planetmovie.data.local.entity.TvPopularEntity
import com.planetmovie.data.local.entity.TvTopRatedEntity
import com.planetmovie.data.remote.model.MovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TvViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE
     * VARIABLE*/
    val readTvAiringToday: LiveData<List<TvAiringTodayEntity>> = repository.localData.readTvAiringToday().asLiveData()
    val readTvPopular: LiveData<List<TvPopularEntity>> = repository.localData.readTvPopular().asLiveData()
    val readTvTopRated: LiveData<List<TvTopRatedEntity>> = repository.localData.readTvTopRated().asLiveData()

    private fun insertTvAiringToday(tvAiringTodayEntity: TvAiringTodayEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertTvAiringToday(tvAiringTodayEntity)
        }

    private fun insertTvPopular(tvPopularEntity: TvPopularEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertTvPopular(tvPopularEntity)
        }

    private fun insertTvTopRated(tvTopRatedEntity: TvTopRatedEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localData.insertTvTopRated(tvTopRatedEntity)
        }

    /** RETROFIT
     * VARIABLE*/
    var tvAiringToday: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var tvPopular: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var tvTopRated: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var searchTv: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()

    fun getTvsAiringToday() = viewModelScope.launch {
        tvAiringToday.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getTvAiringToday()
                tvAiringToday.value = handleTvResponse(response)

                /** ROOM DATABASE */
                val tvAiringTodayData = tvAiringToday.value!!.data
                if (tvAiringTodayData != null) {
                    offlineCacheTvAiringToday(tvAiringTodayData)
                }
            } catch (e: Exception) {
                tvAiringToday.value = Resource.Error("Tv Not Found.")
            }
        } else {
            tvAiringToday.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getTvsPopular() = viewModelScope.launch {
        tvPopular.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getTvPopular()
                tvPopular.value = handleTvResponse(response)

                /** ROOM DATABASE */
                val tvPopularData = tvPopular.value!!.data
                if (tvPopularData != null) {
                    offlineCacheTvPopular(tvPopularData)
                }
            } catch (e: Exception) {
                tvPopular.value = Resource.Error("Tv Not Found.")
            }
        } else {
            tvPopular.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getTvsTopRated() = viewModelScope.launch {
        tvTopRated.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.remoteData.getTvTopRated()
                tvTopRated.value = handleTvResponse(response)

                /** ROOM DATABASE */
                val tvTopRatedData = tvTopRated.value!!.data
                if (tvTopRatedData != null) {
                    offlineCacheTvTopRated(tvTopRatedData)
                }
            } catch (e: Exception) {
                tvTopRated.value = Resource.Error("Tv Not Found.")
            }
        } else {
            tvTopRated.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getSearchTv(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchTv.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val responseSearchTv = repository.remoteData.getSearchTv(searchQuery)
                searchTv.value = handleTvResponse(responseSearchTv)
            } catch (e: Exception) {
                searchTv.value = Resource.Error("Tv Not Found.")
            }
        } else {
            searchTv.value = Resource.Error("No Internet Connection.")
        }
    }

    /** ROOM DATABASE */
    private fun offlineCacheTvAiringToday(tvAiringTodayData: MovieResponse) {
        val tvAiringTodayEntity = TvAiringTodayEntity(tvAiringTodayData)
        insertTvAiringToday(tvAiringTodayEntity)
    }

    private fun offlineCacheTvPopular(tvPopularData: MovieResponse) {
        val tvPopularEntity = TvPopularEntity(tvPopularData)
        insertTvPopular(tvPopularEntity)
    }

    private fun offlineCacheTvTopRated(tvTopRatedData: MovieResponse) {
        val tvTopRatedEntity = TvTopRatedEntity(tvTopRatedData)
        insertTvTopRated(tvTopRatedEntity)
    }

    /** TO HANDLE THE RESPONSE MOVIE DATA*/
    private fun handleTvResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        return when {
            response.message().toString().contains("Timeout") -> Resource.Error("Timeout")
            response.code() == 402 -> Resource.Error("Api Key Limited.")
            response.body()!!.movieResults.isNullOrEmpty() -> Resource.Error("Tv Not Found.")
            response.isSuccessful -> {
                val tvData = response.body()
                Resource.Success(tvData!!)
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