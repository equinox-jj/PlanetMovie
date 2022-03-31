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

class ItemTvSliderAdapter : CardSliderAdapter<ItemTvSliderAdapter.MyViewHolder>() {

    private var tvResult = emptyList<MovieResult>()

    class MyViewHolder(var binding: ItemListTvSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListTvSliderBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun bindVH(holder: MyViewHolder, position: Int) {
        val currentTvSlider = tvResult[position]
        holder.binding.tvResult = currentTvSlider
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = tvResult.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    fun tvDiffUtil(newData: MovieResponse) {
        val tvDiffUtil = NetworkDiffUtil(tvResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(tvDiffUtil)
        tvResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}