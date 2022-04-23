package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemSearchTvBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemSearchTvAdapter : RecyclerView.Adapter<ItemSearchTvAdapter.MyViewHolder>() {

    private var searchTvResult = emptyList<MovieResult>()

    class MyViewHolder(private val binding: ItemSearchTvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchTvResult: MovieResult) {
            binding.searchTvResult = searchTvResult
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSearchTv = searchTvResult[position]
        holder.bind(currentSearchTv)
    }

    override fun getItemCount(): Int = searchTvResult.size

    fun searchDiffUtil(newData: MovieResponse) {
        val searchTvDiffUtil = NetworkDiffUtil(searchTvResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(searchTvDiffUtil)
        searchTvResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}