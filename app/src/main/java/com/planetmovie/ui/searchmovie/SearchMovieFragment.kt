package com.planetmovie.ui.searchmovie

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.databinding.FragmentSearchMovieBinding
import com.planetmovie.ui.SharedViewModel
import com.planetmovie.ui.adapter.ItemSearchMovieAdapter
import com.planetmovie.ui.movie.MovieViewModel
import com.planetmovie.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieFragment : Fragment(R.layout.fragment_search_movie) {

    // View Binding
    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val mSearchViewModel: MovieViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    // Adapter
    private val mSearchMovieAdapter: ItemSearchMovieAdapter by lazy { ItemSearchMovieAdapter() }

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchMovieBinding.bind(view)

        initRecyclerView()
        searchMovie()

    }

    private fun initRecyclerView() {
        binding.rvSearchMovie.apply {
            adapter = mSearchMovieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun searchMovie() {
//        binding.svSearchMovie.requestFocus()
//        binding.svSearchMovie.showKeyboard()
        binding.svSearchMovie.isSubmitButtonEnabled = true
        binding.svSearchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchMovieViewModel(query)
                    binding.svSearchMovie.clearFocus()
                    binding.svSearchMovie.hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = false

        })
    }

    private fun searchMovieViewModel(query: String) {
        val searchQuery = "%$query%"
        mSearchViewModel.getSearchMovie(mSharedViewModel.searchQueries(searchQuery))
        mSearchViewModel.searchMovie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    val movieData = response.data
                    movieData?.let { mSearchMovieAdapter.searchDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }
    }

    private fun showShimmer(boolean: Boolean) {
        binding.apply {
            if (boolean) {
                isShimmerLoading = true
                shimmerRvSearch.startShimmer()
                shimmerRvSearch.visibility = View.VISIBLE
                rvSearchMovie.visibility = View.INVISIBLE
            } else {
                isShimmerLoading = false
                shimmerRvSearch.stopShimmer()
                shimmerRvSearch.visibility = View.GONE
                rvSearchMovie.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}