package com.planetmovie.ui.movie

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.databinding.FragmentMovieBinding
import com.planetmovie.ui.SharedViewModel
import com.planetmovie.ui.adapter.ItemMovieListAdapter
import com.planetmovie.ui.adapter.ItemMovieSliderAdapter
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
    private lateinit var mNowPlayingAdapter: ItemMovieSliderAdapter
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

        setupRecycler()
        readBackOnline()
    }

    private fun setupRecycler() {
        binding.sliderMovie.apply {
            mNowPlayingAdapter = ItemMovieSliderAdapter()
            adapter = mNowPlayingAdapter
        }

        binding.rvPopularMovie.apply {
            mPopularAdapter = ItemMovieListAdapter()
            adapter = mPopularAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvUpcomingMovie.apply {
            mUpcomingAdapter = ItemMovieListAdapter()
            adapter = mUpcomingAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun readBackOnline() {
        mSharedViewModel.readBackOnline.observe(viewLifecycleOwner) {
            mSharedViewModel.backOnline = it
        }
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
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
        mMovieViewModel.getMovieNowPlaying()
        mMovieViewModel.movieNowPlaying.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mNowPlayingAdapter.movieDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    loadDataFromCache()
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

        mMovieViewModel.getMoviePopular()
        mMovieViewModel.moviePopular.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mPopularAdapter.movieDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    loadDataFromCache()
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

        mMovieViewModel.getMovieUpcoming()
        mMovieViewModel.movieUpcoming.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mUpcomingAdapter.movieDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    loadDataFromCache()
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
                binding.sliderMovie.visibility = View.GONE
                binding.sliderIndicatorMovie.visibility = View.GONE
            } else {
                isShimmerLoading = false
                shimmerRvMovie.stopShimmer()
                shimmerRvMovie.visibility = View.GONE
                tvMovieOne.visibility = View.VISIBLE
                tvMovieTwo.visibility = View.VISIBLE
                tvMovieThree.visibility = View.VISIBLE
                rvPopularMovie.visibility = View.VISIBLE
                rvUpcomingMovie.visibility = View.VISIBLE
                binding.sliderMovie.visibility = View.VISIBLE
                binding.sliderIndicatorMovie.visibility = View.VISIBLE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}