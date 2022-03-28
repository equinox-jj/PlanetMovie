package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemSearchMovieBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemSearchMovieAdapter : RecyclerView.Adapter<ItemSearchMovieAdapter.MyViewHolder>() {

    private var searchMovieResult = emptyList<MovieResult>()

    class MyViewHolder(var binding: ItemSearchMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchMovieBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSearchMovie = searchMovieResult[position]
        holder.binding.searchMovieResult = currentSearchMovie
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = searchMovieResult.size

    fun searchDiffUtil(newData: MovieResponse) {
        val searchMovieDiffUtil = NetworkDiffUtil(searchMovieResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(searchMovieDiffUtil)
        searchMovieResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }

}