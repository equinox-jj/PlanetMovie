package com.planetmovie.ui.favorite_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.planetmovie.data.local.entity.MovieFavoriteEntity
import com.planetmovie.data.local.entity.TvFavoriteEntity
import com.planetmovie.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {

    val getFavoriteMovie: LiveData<List<MovieFavoriteEntity>> = repository.localData.getFavoriteMovie().asLiveData()
    val getFavoriteTv: LiveData<List<TvFavoriteEntity>> = repository.localData.getFavoriteTv().asLiveData()

    fun deleteFavoriteMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.localData.deleteFavoriteMovie(movieId)
    }

    fun deleteFavoriteTv(tvId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.localData.deleteFavoriteTv(tvId)
    }
}