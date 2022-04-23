package com.planetmovie.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.databinding.FragmentMovieBinding
import com.planetmovie.ui.SharedViewModel
import com.planetmovie.ui.adapter.ItemMovieListAdapter
import com.planetmovie.util.NetworkListener
import com.planetmovie.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment(R.layout.fragment_movie) {

    // View Binding
    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val mMovieViewModel: MovieViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    // Adapter
    private lateinit var mNowPlayingAdapter: ItemMovieListAdapter
    private lateinit var mPopularAdapter: ItemMovieListAdapter
    private lateinit var mUpcomingAdapter: ItemMovieListAdapter

    // TvNetwork Listener
    private lateinit var networkListener: NetworkListener

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieBinding.bind(view)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mMovieViewModel = mMovieViewModel

        initRecyclerView()
        readBackOnline()
    }

    private fun initRecyclerView() {
        binding.rvNowPlaying.apply {
            mNowPlayingAdapter = ItemMovieListAdapter()
            adapter = mNowPlayingAdapter
            setHasFixedSize(true)
        }

        binding.rvPopularMovie.apply {
            mPopularAdapter = ItemMovieListAdapter()
            adapter = mPopularAdapter
            setHasFixedSize(true)
        }

        binding.rvUpcomingMovie.apply {
            mUpcomingAdapter = ItemMovieListAdapter()
            adapter = mUpcomingAdapter
            setHasFixedSize(true)
        }
    }

    private fun readBackOnline() {
        mSharedViewModel.readBackOnline.observe(viewLifecycleOwner) {
            mSharedViewModel.backOnline = it
        }
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                    mSharedViewModel.networkStatus = status
                    mSharedViewModel.showNetworkStatus()
                    readDatabase()
                }
        }
    }

    private fun readDatabase() {
        lifecycleScope.launchWhenStarted {
            mMovieViewModel.readMoviesNowPlaying.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mNowPlayingAdapter.movieDiffUtil(database[0].movieNowPlayingData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }
            mMovieViewModel.readMoviesPopular.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mPopularAdapter.movieDiffUtil(database[0].moviePopularData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }
            mMovieViewModel.readMoviesUpcoming.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mUpcomingAdapter.movieDiffUtil(database[0].movieUpcomingData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }
        }
    }

    private fun readApiResponse() {
        mMovieViewModel.getMoviesNowPlaying()
        mMovieViewModel.movieNowPlaying.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mNowPlayingAdapter.movieDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    hideText()
                    loadDataFromCache()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }

        mMovieViewModel.getMoviesPopular()
        mMovieViewModel.moviePopular.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mPopularAdapter.movieDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    hideText()
                    loadDataFromCache()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }

        mMovieViewModel.getMoviesUpcoming()
        mMovieViewModel.movieUpcoming.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mUpcomingAdapter.movieDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    hideText()
                    loadDataFromCache()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mMovieViewModel.readMoviesNowPlaying.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mNowPlayingAdapter.movieDiffUtil(database[0].movieNowPlayingData)
                }
            }
            mMovieViewModel.readMoviesPopular.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mPopularAdapter.movieDiffUtil(database[0].moviePopularData)
                }
            }
            mMovieViewModel.readMoviesUpcoming.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mUpcomingAdapter.movieDiffUtil(database[0].movieUpcomingData)
                }
            }
        }
    }

    private fun showShimmer(boolean: Boolean) {
        binding.apply {
            if (boolean) {
                isShimmerLoading = true
                shimmerRvMovie.startShimmer()
                shimmerRvMovie.visibility = View.VISIBLE
                tvMovieOne.visibility = View.GONE
                tvMovieTwo.visibility = View.GONE
                tvMovieThree.visibility = View.GONE
                rvPopularMovie.visibility = View.GONE
                rvUpcomingMovie.visibility = View.GONE
                rvNowPlaying.visibility = View.GONE
            } else {
                isShimmerLoading = false
                shimmerRvMovie.stopShimmer()
                shimmerRvMovie.visibility = View.GONE
                tvMovieOne.visibility = View.VISIBLE
                tvMovieTwo.visibility = View.VISIBLE
                tvMovieThree.visibility = View.VISIBLE
                rvPopularMovie.visibility = View.VISIBLE
                rvUpcomingMovie.visibility = View.VISIBLE
                rvNowPlaying.visibility = View.VISIBLE
            }
        }
    }

    private fun hideText() {
        binding.apply {
            tvMovieOne.visibility = View.GONE
            tvMovieTwo.visibility = View.GONE
            tvMovieThree.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}