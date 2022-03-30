package com.planetmovie.ui.searchtv

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.databinding.FragmentSearchTvBinding
import com.planetmovie.ui.SharedViewModel
import com.planetmovie.ui.adapter.ItemSearchTvAdapter
import com.planetmovie.ui.tv.TvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTvFragment : Fragment(R.layout.fragment_search_tv) {

    // View Binding
    private var _binding: FragmentSearchTvBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val mSearchTvViewModel: TvViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    // Adapter
    private val mSearchMovieAdapter: ItemSearchTvAdapter by lazy { ItemSearchTvAdapter() }

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchTvBinding.bind(view)

        mSharedViewModel.readBackOnline.observe(viewLifecycleOwner) {
            mSharedViewModel.backOnline = it
        }

        setupRecycler()
        searchTv()
    }

    private fun setupRecycler() {
        binding.rvSearchTv.apply {
            adapter = mSearchMovieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun searchTv() {
        binding.svSearchTv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchApiData(query)
                    binding.svSearchTv.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = true

        })
    }

    private fun searchApiData(query: String) {
        val searchQuery = "%$query%"
        mSearchTvViewModel.getSearchTv(mSharedViewModel.searchQueries(searchQuery))
        mSearchTvViewModel.searchTv.observe(viewLifecycleOwner) { response ->
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
                rvSearchTv.visibility = View.INVISIBLE
            } else {
                isShimmerLoading = false
                shimmerRvSearch.stopShimmer()
                shimmerRvSearch.visibility = View.GONE
                rvSearchTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}