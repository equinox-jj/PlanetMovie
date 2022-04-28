package com.planetmovie.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.planetmovie.data.preferences.BackOnlinePreferences
import com.planetmovie.util.Constant.Companion.API_KEY
import com.planetmovie.util.Constant.Companion.QUERY_API
import com.planetmovie.util.Constant.Companion.QUERY_PAGE
import com.planetmovie.util.Constant.Companion.QUERY_SEARCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val backOnlinePreferences: BackOnlinePreferences,
    application: Application
) : AndroidViewModel(application) {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
        }
    }

    var networkStatus = false
    var backOnline = false
    val readBackOnline = backOnlinePreferences.readBackOnline.asLiveData()

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            backOnlinePreferences.saveBackOnline(backOnline)
        }

    fun searchQueries(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_API] = API_KEY
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_PAGE] = "1"
        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "Offline.", Toast.LENGTH_SHORT).show()
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "Back Online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}