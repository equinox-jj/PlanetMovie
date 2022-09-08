package com.planetmovie.ui.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.planetmovie.R
import com.planetmovie.data.local.entity.MovieFavoriteEntity
import com.planetmovie.databinding.ItemFavoriteMovieBinding
import com.planetmovie.ui.favorite_movie.FavoriteMovieFragmentDirections
import com.planetmovie.ui.favorite_movie.FavoriteViewModel
import com.planetmovie.util.NetworkDiffUtil
import okhttp3.internal.immutableListOf

class ItemFavoriteMovieAdapter(
    private val requireActivity: FragmentActivity,
    private val favMovieViewModel: FavoriteViewModel
) : RecyclerView.Adapter<ItemFavoriteMovieAdapter.ItemFavMovieViewHolder>(), ActionMode.Callback {

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var multiSelection = false

    private var selectedMovies = arrayListOf<MovieFavoriteEntity>()
    private var viewHolder = arrayListOf<ItemFavMovieViewHolder>()
    private var movieFavoriteEntity = immutableListOf<MovieFavoriteEntity>()

    class ItemFavMovieViewHolder(val binding: ItemFavoriteMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieFavoriteEntity: MovieFavoriteEntity) {
            binding.favoriteMovieEntity = movieFavoriteEntity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFavMovieViewHolder {
        val binding = ItemFavoriteMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFavMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFavMovieViewHolder, position: Int) {
        viewHolder.add(holder)
        rootView = holder.itemView.rootView

        val currentFavMovie = movieFavoriteEntity[position]
        holder.bind(currentFavMovie)

        saveItemOnScroll(currentFavMovie, holder)

        /** SINGLE CLICK LISTENER */
        holder.binding.constraintFavoriteMovie.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentFavMovie)
            } else {
                val action = FavoriteMovieFragmentDirections.actionFavoriteMovieFragmentToMovieDetailFragment(currentFavMovie.id)
                holder.itemView.findNavController().navigate(action)
            }
        }

        /** LONG CLICK LISTENER */
        holder.binding.constraintFavoriteMovie.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentFavMovie)
                true
            } else {
                applySelection(holder, currentFavMovie)
                true
            }

        }
    }

    override fun getItemCount(): Int = movieFavoriteEntity.size

    fun newData(newMovieFavorite: List<MovieFavoriteEntity>) {
        val movieFavoriteDiffUtil = NetworkDiffUtil(movieFavoriteEntity, newMovieFavorite)
        val diffUtilResult = DiffUtil.calculateDiff(movieFavoriteDiffUtil)
        movieFavoriteEntity = newMovieFavorite
        diffUtilResult.dispatchUpdatesTo(this)
    }

    /** Action Mode*/
    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.menu_favorite_contextual, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.color_status_bar)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menuItem: MenuItem?): Boolean {
        if (menuItem?.itemId == R.id.favorite_movie_delete_menu) {
            selectedMovies.forEach {
                favMovieViewModel.deleteFavoriteMovie(it.id)
            }
            showSnackBar("${selectedMovies.size} Movie/s Removed.")
            multiSelection = false
            selectedMovies.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        viewHolder.forEach { holder ->
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
        multiSelection = false
        selectedMovies.clear()
        applyStatusBarColor(R.color.color_status_bar)
    }

    /** Contextual Action Mode */
    private fun saveItemOnScroll(currentMovies: MovieFavoriteEntity, holder: ItemFavMovieViewHolder) {
        if (selectedMovies.contains(currentMovies)) {
            changeMovieStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
        } else {
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
    }

    private fun applySelection(holder: ItemFavMovieViewHolder, currentMovies: MovieFavoriteEntity) {
        if (selectedMovies.contains(currentMovies)) {
            selectedMovies.remove(currentMovies)
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
            applyActionModeTitle()
        } else {
            selectedMovies.add(currentMovies)
            changeMovieStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
            applyActionModeTitle()
        }
    }

    private fun changeMovieStyle(holder: ItemFavMovieViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.constraintFavoriteMovie.setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))
        holder.binding.cvFavoriteMovie.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedMovies.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> {
                mActionMode.title = "${selectedMovies.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedMovies.size} items selected"
            }
        }
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay.") {}
            .show()
    }


    fun destroyContextualAction() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}