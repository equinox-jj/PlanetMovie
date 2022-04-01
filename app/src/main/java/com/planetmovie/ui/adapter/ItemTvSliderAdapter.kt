package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemListTvSliderBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemTvSliderAdapter : CardSliderAdapter<ItemTvSliderAdapter.TvSlideViewHolder>() {

    private var tvResult = emptyList<MovieResult>()

    class TvSlideViewHolder(private val binding: ItemListTvSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tvResult: MovieResult) {
            binding.tvResult = tvResult
            binding.executePendingBindings()
        }
    }

    override fun bindVH(holder: TvSlideViewHolder, position: Int) {
        val currentTvSlider = tvResult[position]
        holder.bind(currentTvSlider)
    }

    override fun getItemCount(): Int = tvResult.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvSlideViewHolder {
        val binding = ItemListTvSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvSlideViewHolder(binding)
    }

    fun tvDiffUtil(newData: MovieResponse) {
        val tvDiffUtil = NetworkDiffUtil(tvResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(tvDiffUtil)
        tvResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}