package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieVideos
import com.planetmovie.data.remote.model.MovieVideosResult
import com.planetmovie.databinding.ItemTrailerBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemTrailerAdapter : RecyclerView.Adapter<ItemTrailerAdapter.MyViewHolder>() {

    private var movieTrailer = emptyList<MovieVideosResult>()

    class MyViewHolder(var binding: ItemTrailerBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTrailerBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val trailerDetail = movieTrailer[position]
        holder.binding.movieTrailer = trailerDetail
        holder.binding.executePendingBindings()
        holder.binding.tvTrailer.isSelected = true
    }

    override fun getItemCount(): Int = movieTrailer.size

    fun trailerDiffUtil(newData: MovieVideos) {
        val trailerDIffUtil = NetworkDiffUtil(movieTrailer, newData.movieVideoResults)
        val diffUtilResult = DiffUtil.calculateDiff(trailerDIffUtil)
        movieTrailer = newData.movieVideoResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}