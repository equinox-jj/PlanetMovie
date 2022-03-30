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

        mSharedViewModel.readBackOnline.observe(viewLifecycleOwner) {
            mSharedViewModel.backOnline = it
        }

        setupRecycler()
        searchMovie()

    }

    private fun setupRecycler() {
        binding.rvSearchMovie.apply {
            adapter = mSearchMovieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun searchMovie() {
        binding.svSearchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchApiData(query)
                    binding.svSearchMovie.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = true

        })
    }

    private fun searchApiData(query: String) {
        val searchQuery = "%$query%"
        mSearchViewModel.getSearchMovie(mSharedViewModel.searchQueries(searchQuery))
        mSearchViewModel.searchMovie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showSearchShimmer(false)
                    val movieData = response.data
                    movieData?.let { mSearchMovieAdapter.searchDiffUtil(it) }
                }
                is Resource.Error -> {
                    showSearchShimmer(false)
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showSearchShimmer(true)
                }
            }
        }
    }

    private fun showSearchShimmer(boolean: Boolean) {
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