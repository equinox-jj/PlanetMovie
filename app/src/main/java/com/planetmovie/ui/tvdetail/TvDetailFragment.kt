package com.planetmovie.ui.tvdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.planetmovie.R
import com.planetmovie.data.Resource
import com.planetmovie.data.remote.model.MovieDetailResponse
import com.planetmovie.databinding.FragmentTvDetailBinding
import com.planetmovie.ui.adapter.ItemCastAdapter
import com.planetmovie.ui.adapter.ItemTrailerAdapter
import com.planetmovie.util.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvDetailFragment : Fragment() {

    // View Binding
    private var _binding: FragmentTvDetailBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val mTvDetailViewModel: TvDetailViewModel by viewModels()

    // Adapter
    private val mTvDetailCastAdapter: ItemCastAdapter by lazy { ItemCastAdapter() }
    private val mTvDetailTrailerAdapter: ItemTrailerAdapter by lazy { ItemTrailerAdapter() }

    // Shimmer Loading
    private var isShimmerLoading: Boolean = false

    // Navigation Args
    private val args by navArgs<TvDetailFragmentArgs>()

    // Favorite Menu
    private lateinit var favMenuItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeViewModel()
    }

    private fun setupRecycler() {
        binding.rvDtlCast.apply {
            binding.rvDtlCast.adapter = mTvDetailCastAdapter
            binding.rvDtlCast.setHasFixedSize(true)
            binding.rvDtlCast.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.rvDtlTrailer.apply {
            binding.rvDtlTrailer.adapter = mTvDetailTrailerAdapter
            binding.rvDtlTrailer.setHasFixedSize(true)
            binding.rvDtlTrailer.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeViewModel() {
        val tvId: Int = args.tvId
        mTvDetailViewModel.getTvDetail(tvId)
        mTvDetailViewModel.tvDetail.observe(viewLifecycleOwner) { detailTv ->
            when (detailTv) {
                is Resource.Success -> {
                    showShimmer(false)
                    detailTv.data?.let { detailData(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }
    }

    private fun detailData(data: MovieDetailResponse) {
        data.let {
            val rating = "${it.voteAverage} / 10"
//            binding.ctlTvDetail.title = it.tvName
            binding.tvDtlDate.text = it.tvFirstAirDate
            binding.rbDtlOne.rating = it.voteAverage.div(2).toFloat()
            if (it.overview.isNotEmpty()) {
                binding.tvDtlOverview.text = it.overview
            } else binding.tvDtlOverview.text = getString(R.string.overview_not_available)
            binding.tvDtlRate.text = rating
            binding.ivDtlTvBackdrop.load(Constant.BASE_IMG_URL_BACKDROP + it.backdropPath) {
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
            binding.ivDtlPoster.load(Constant.BASE_IMG_URL_POSTER + it.posterPath) {
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
            if (it.movieCredits.movieCast.isNotEmpty()) {
                mTvDetailCastAdapter.castDiffUtil(it.movieCredits)
            } else binding.tvCast.visibility = View.GONE
            if (it.movieVideos.movieVideoResults.isNotEmpty()) {
                mTvDetailTrailerAdapter.trailerDiffUtil(it.movieVideos)
            } else binding.tvTrailer.visibility = View.GONE

        }
    }

    private fun showShimmer(boolean: Boolean) {
        if (boolean) {
            isShimmerLoading = true
            binding.shimmerDetailTv.startShimmer()
            binding.shimmerDetailTv.visibility = View.VISIBLE
            binding.ivDtlTvBackdrop.visibility = View.GONE
            binding.shadowBackdrop.visibility = View.GONE
            binding.ivDtlPoster.visibility = View.GONE
            binding.tvRating.visibility = View.GONE
            binding.tvDtlRate.visibility = View.GONE
            binding.rbDtlOne.visibility = View.GONE
            binding.tvReleaseDate.visibility = View.GONE
            binding.tvDtlDate.visibility = View.GONE
            binding.tvOverview.visibility = View.GONE
            binding.tvDtlOverview.visibility = View.GONE
            binding.tvCast.visibility = View.GONE
            binding.rvDtlCast.visibility = View.GONE
            binding.tvTrailer.visibility = View.GONE
            binding.rvDtlTrailer.visibility = View.GONE
        } else {
            isShimmerLoading = false
            binding.shimmerDetailTv.stopShimmer()
            binding.shimmerDetailTv.visibility = View.GONE
            binding.ivDtlTvBackdrop.visibility = View.VISIBLE
            binding.shadowBackdrop.visibility = View.VISIBLE
            binding.ivDtlPoster.visibility = View.VISIBLE
            binding.tvRating.visibility = View.VISIBLE
            binding.tvDtlRate.visibility = View.VISIBLE
            binding.rbDtlOne.visibility = View.VISIBLE
            binding.tvReleaseDate.visibility = View.VISIBLE
            binding.tvDtlDate.visibility = View.VISIBLE
            binding.tvOverview.visibility = View.VISIBLE
            binding.tvDtlOverview.visibility = View.VISIBLE
            binding.tvCast.visibility = View.VISIBLE
            binding.rvDtlCast.visibility = View.VISIBLE
            binding.tvTrailer.visibility = View.VISIBLE
            binding.rvDtlTrailer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}