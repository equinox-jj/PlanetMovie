package com.planetmovie.ui.searchmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.planetmovie.data.Resource
import com.planetmovie.databinding.FragmentSearchMovieBinding
import com.planetmovie.ui.SharedViewModel
import com.planetmovie.ui.adapter.ItemSearchMovieAdapter
import com.planetmovie.ui.movie.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSharedViewModel.readBackOnline.observe(viewLifecycleOwner) {
            mSharedViewModel.backOnline = it
        }

        setupRecycler()
        searchMovie()

    }

    private fun setupRecycler() {
        binding.rvSearchMovie.adapter = mSearchMovieAdapter
        binding.rvSearchMovie.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchMovie.setHasFixedSize(true)
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
        if (boolean) {
            isShimmerLoading = true
            binding.shimmerRvSearch.startShimmer()
            binding.shimmerRvSearch.visibility = View.VISIBLE
            binding.rvSearchMovie.visibility = View.INVISIBLE
        } else {
            isShimmerLoading = false
            binding.shimmerRvSearch.stopShimmer()
            binding.shimmerRvSearch.visibility = View.GONE
            binding.rvSearchMovie.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}