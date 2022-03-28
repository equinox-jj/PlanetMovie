package com.planetmovie.ui.tv

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
import com.planetmovie.databinding.FragmentTvBinding
import com.planetmovie.ui.SharedViewModel
import com.planetmovie.ui.adapter.ItemTvListAdapter
import com.planetmovie.util.NetworkListener
import com.planetmovie.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvFragment : Fragment() {

    // View Binding
    private var _binding: FragmentTvBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val mTvViewModel: TvViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    // Adapter
    private lateinit var mAiringTodayAdapter: ItemTvListAdapter
    private lateinit var mPopularAdapter: ItemTvListAdapter
    private lateinit var mTopRatedAdapter: ItemTvListAdapter

    // TvNetwork Listener
    private lateinit var networkListener: NetworkListener

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            binding.lifecycleOwner = viewLifecycleOwner
            binding.mTvViewModel = mTvViewModel
        }

        setupRecycler()
        readBackOnline()
    }

    private fun setupRecycler() {
        binding.rvAiringTodayTv.apply {
            mAiringTodayAdapter = ItemTvListAdapter()
            binding.rvAiringTodayTv.adapter = mAiringTodayAdapter
            binding.rvAiringTodayTv.setHasFixedSize(true)
            binding.rvAiringTodayTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvPopularTv.apply {
            mPopularAdapter = ItemTvListAdapter()
            binding.rvPopularTv.adapter = mPopularAdapter
            binding.rvPopularTv.setHasFixedSize(true)
            binding.rvPopularTv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvTopRatedTv.apply {
            mTopRatedAdapter = ItemTvListAdapter()
            binding.rvTopRatedTv.adapter = mTopRatedAdapter
            binding.rvTopRatedTv.setHasFixedSize(true)
            binding.rvTopRatedTv.layoutManager =
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
                    readDatabase()
                }
        }
    }

    private fun readDatabase() {
        lifecycleScope.launchWhenStarted {
            mTvViewModel.readTvAiringToday.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAiringTodayAdapter.tvDiffUtil(database[0].tvAiringTodayData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }
            mTvViewModel.readTvPopular.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mPopularAdapter.tvDiffUtil(database[0].tvPopularData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }
            mTvViewModel.readTvTopRated.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mTopRatedAdapter.tvDiffUtil(database[0].tvTopRatedData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }
        }
    }

    private fun readApiResponse() {
        mTvViewModel.getTvAiringToday()
        mTvViewModel.tvAiringToday.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mAiringTodayAdapter.tvDiffUtil(it) }
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

        mTvViewModel.getTvPopular()
        mTvViewModel.tvPopular.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mPopularAdapter.tvDiffUtil(it) }
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

        mTvViewModel.getTvTopRated()
        mTvViewModel.tvTopRated.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mTopRatedAdapter.tvDiffUtil(it) }
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
            mTvViewModel.readTvAiringToday.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAiringTodayAdapter.tvDiffUtil(database[0].tvAiringTodayData)
                }
            }
            mTvViewModel.readTvPopular.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mPopularAdapter.tvDiffUtil(database[0].tvPopularData)
                }
            }
            mTvViewModel.readTvTopRated.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mTopRatedAdapter.tvDiffUtil(database[0].tvTopRatedData)
                }
            }
        }
    }

    private fun showShimmer(boolean: Boolean) {
        if (boolean) {
            isShimmerLoading = true
            binding.shimmerRvTv.startShimmer()
            binding.shimmerRvTv.visibility = View.VISIBLE
            binding.tvTvOne.visibility = View.GONE
            binding.tvTvTwo.visibility = View.GONE
            binding.tvTvThree.visibility = View.GONE
            binding.rvAiringTodayTv.visibility = View.GONE
            binding.rvPopularTv.visibility = View.GONE
            binding.rvTopRatedTv.visibility = View.GONE
        } else {
            isShimmerLoading = false
            binding.shimmerRvTv.stopShimmer()
            binding.shimmerRvTv.visibility = View.GONE
            binding.tvTvOne.visibility = View.VISIBLE
            binding.tvTvTwo.visibility = View.VISIBLE
            binding.tvTvThree.visibility = View.VISIBLE
            binding.rvAiringTodayTv.visibility = View.VISIBLE
            binding.rvPopularTv.visibility = View.VISIBLE
            binding.rvTopRatedTv.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}