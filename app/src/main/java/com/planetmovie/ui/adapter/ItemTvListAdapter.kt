package com.planetmovie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.planetmovie.data.remote.model.MovieResponse
import com.planetmovie.data.remote.model.MovieResult
import com.planetmovie.databinding.ItemListTvBinding
import com.planetmovie.util.NetworkDiffUtil

class ItemTvListAdapter : RecyclerView.Adapter<ItemTvListAdapter.MyViewHolder>() {

    private var tvResult = emptyList<MovieResult>()

    class MyViewHolder(private val binding: ItemListTvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tvResult: MovieResult) {
            binding.tvResult = tvResult
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTv = tvResult[position]
        holder.bind(currentTv)
    }

    override fun getItemCount(): Int = tvResult.size

    fun tvDiffUtil(newData: MovieResponse) {
        val tvDiffUtil = NetworkDiffUtil(tvResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(tvDiffUtil)
        tvResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }

}