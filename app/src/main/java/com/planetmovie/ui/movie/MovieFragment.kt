package com.planetmovie.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.planetmovie.data.Resource
import com.planetmovie.databinding.FragmentMovieBinding
import com.planetmovie.ui.SharedViewModel
import com.planetmovie.ui.adapter.ItemMovieListAdapter
import com.planetmovie.util.NetworkListener
import com.planetmovie.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = viewLifecycleOwner
            binding.mMovieViewModel = mMovieViewModel
        }

        setupRecycler()
        readBackOnline()
    }

    private fun setupRecycler() {
        binding.rvNowPlayingMovie.apply {
            mNowPlayingAdapter = ItemMovieListAdapter()
            binding.rvNowPlayingMovie.adapter = mNowPlayingAdapter
            binding.rvNowPlayingMovie.setHasFixedSize(true)
            binding.rvNowPlayingMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvPopularMovie.apply {
            mPopularAdapter = ItemMovieListAdapter()
            binding.rvPopularMovie.adapter = mPopularAdapter
            binding.rvPopularMovie.setHasFixedSize(true)
            binding.rvPopularMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvUpcomingMovie.apply {
            mUpcomingAdapter = ItemMovieListAdapter()
            binding.rvUpcomingMovie.adapter = mUpcomingAdapter
            binding.rvUpcomingMovie.setHasFixedSize(true)
            binding.rvUpcomingMovie.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        if (boolean) {
            isShimmerLoading = true
            binding.shimmerRvMovie.startShimmer()
            binding.shimmerRvMovie.visibility = View.VISIBLE
            binding.tvMovieOne.visibility = View.GONE
            binding.tvMovieTwo.visibility = View.GONE
            binding.tvMovieThree.visibility = View.GONE
            binding.rvPopularMovie.visibility = View.GONE
            binding.rvUpcomingMovie.visibility = View.GONE
            binding.rvNowPlayingMovie.visibility = View.GONE
        } else {
            isShimmerLoading = false
            binding.shimmerRvMovie.stopShimmer()
            binding.shimmerRvMovie.visibility = View.GONE
            binding.tvMovieOne.visibility = View.VISIBLE
            binding.tvMovieTwo.visibility = View.VISIBLE
            binding.tvMovieThree.visibility = View.VISIBLE
            binding.rvPopularMovie.visibility = View.VISIBLE
            binding.rvUpcomingMovie.visibility = View.VISIBLE
            binding.rvNowPlayingMovie.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}