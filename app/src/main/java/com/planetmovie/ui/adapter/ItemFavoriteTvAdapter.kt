package com.planetmovie.ui.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.planetmovie.R
import com.planetmovie.data.local.entity.TvFavoriteEntity
import com.planetmovie.databinding.ItemFavoriteTvBinding
import com.planetmovie.ui.favorite_movie.FavoriteViewModel
import com.planetmovie.ui.favorite_tv.FavoriteTvFragmentDirections
import com.planetmovie.util.NetworkDiffUtil

class ItemFavoriteTvAdapter(
    private val requireActivity: FragmentActivity,
    private val favTvViewModel: FavoriteViewModel
) : RecyclerView.Adapter<ItemFavoriteTvAdapter.ItemFavTvViewHolder>(), ActionMode.Callback {

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var multiSelection = false

    private var selectedTv = arrayListOf<TvFavoriteEntity>()
    private var viewHolder = arrayListOf<ItemFavTvViewHolder>()
    private var tvFavoriteEntity = emptyList<TvFavoriteEntity>()

    class ItemFavTvViewHolder(val binding: ItemFavoriteTvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tvFavoriteEntity: TvFavoriteEntity) {
            binding.favoriteTvEntity = tvFavoriteEntity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFavTvViewHolder {
        val binding = ItemFavoriteTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFavTvViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFavTvViewHolder, position: Int) {
        viewHolder.add(holder)
        rootView = holder.itemView.rootView

        val currentFavTv = tvFavoriteEntity[position]
        holder.bind(currentFavTv)

        saveItemOnScroll(currentFavTv, holder)

        /** SINGLE CLICK LISTENER */
        holder.binding.constraintFavoriteTv.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentFavTv)
            } else {
                val action = FavoriteTvFragmentDirections.actionFavoriteTvFragmentToTvDetailFragment(currentFavTv.id)
                holder.itemView.findNavController().navigate(action)
            }
        }

        /** LONG CLICK LISTENER */
        holder.binding.constraintFavoriteTv.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentFavTv)
                true
            } else {
                applySelection(holder, currentFavTv)
                true
            }

        }
    }

    override fun getItemCount(): Int = tvFavoriteEntity.size

    fun newData(newTvFavorite: List<TvFavoriteEntity>) {
        val tvFavoriteDiffUtil = NetworkDiffUtil(tvFavoriteEntity, newTvFavorite)
        val diffUtilResult = DiffUtil.calculateDiff(tvFavoriteDiffUtil)
        tvFavoriteEntity = newTvFavorite
        diffUtilResult.dispatchUpdatesTo(this)
    }

    /** Action Mode*/
    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.menu_favorite_contextual, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.gray_black)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menuItem: MenuItem?): Boolean {
        if (menuItem?.itemId == R.id.favorite_movie_delete_menu) {
            selectedTv.forEach {
                favTvViewModel.deleteFavoriteTv(it.id)
            }
            showSnackBar("${selectedTv.size} Tv/s Removed.")
            multiSelection = false
            selectedTv.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        viewHolder.forEach { holder ->
            cardViewFavTvStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
        multiSelection = false
        selectedTv.clear()
        applyStatusBarColor(R.color.color_appbar)
    }

    /** Contextual Action Mode */
    private fun saveItemOnScroll(currentTv: TvFavoriteEntity, holder: ItemFavTvViewHolder) {
        if (selectedTv.contains(currentTv)) {
            cardViewFavTvStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
        } else {
            cardViewFavTvStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
    }

    private fun applySelection(holder: ItemFavTvViewHolder, currentTv: TvFavoriteEntity) {
        if (selectedTv.contains(currentTv)) {
            selectedTv.remove(currentTv)
            cardViewFavTvStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
            applyActionModeTitle()
        } else {
            selectedTv.add(currentTv)
            cardViewFavTvStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
            applyActionModeTitle()
        }
    }

    private fun cardViewFavTvStyle(holder: ItemFavTvViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.constraintFavoriteTv.setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))
        holder.binding.cvFavoriteTv.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedTv.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> {
                mActionMode.title = "${selectedTv.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedTv.size} items selected"
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