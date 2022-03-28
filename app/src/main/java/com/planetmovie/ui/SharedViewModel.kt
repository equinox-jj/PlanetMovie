package com.planetmovie.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.planetmovie.data.preferences.DataStoreRepository
import com.planetmovie.util.Constant.Companion.API_KEY
import com.planetmovie.util.Constant.Companion.QUERY_API
import com.planetmovie.util.Constant.Companion.QUERY_PAGE
import com.planetmovie.util.Constant.Companion.QUERY_SEARCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    var networkStatus = false
    var backOnline = false
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
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
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "Back Online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}