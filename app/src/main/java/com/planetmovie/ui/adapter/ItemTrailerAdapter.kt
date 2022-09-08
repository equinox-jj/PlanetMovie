package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieVideos
import com.planetmovie.data.remote.model.MovieVideosResult
import com.planetmovie.databinding.ItemTrailerBinding
import com.planetmovie.util.NetworkDiffUtil
import okhttp3.internal.immutableListOf

class ItemTrailerAdapter : RecyclerView.Adapter<ItemTrailerAdapter.MyViewHolder>() {

    private var movieTrailer = immutableListOf<MovieVideosResult>()

    class MyViewHolder(private val binding: ItemTrailerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieTrailer: MovieVideosResult) {
            binding.movieTrailer = movieTrailer
            binding.executePendingBindings()
            binding.tvTrailer.isSelected = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTrailerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val trailerDetail = movieTrailer[position]
        holder.bind(trailerDetail)
    }

    override fun getItemCount(): Int = movieTrailer.size

    fun trailerDiffUtil(newData: MovieVideos) {
        val trailerDIffUtil = NetworkDiffUtil(movieTrailer, newData.movieVideoResults)
        val diffUtilResult = DiffUtil.calculateDiff(trailerDIffUtil)
        movieTrailer = newData.movieVideoResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}