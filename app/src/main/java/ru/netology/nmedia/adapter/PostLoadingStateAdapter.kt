package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemLoadingBinding


class PostLoadingStateAdapter(
    private val onInteractionListener: OnInteractionListener
) : LoadStateAdapter<PostLoadingViewHolder>() {

    interface OnInteractionListener {
        fun onRetry() {}
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PostLoadingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PostLoadingViewHolder(
            ItemLoadingBinding.inflate(
                layoutInflater,
                parent,
                false
            ), onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostLoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class PostLoadingViewHolder(
    private val binding: ItemLoadingBinding,
    private val onInteractionListener: PostLoadingStateAdapter.OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.apply {
            progress.isVisible = loadState is LoadState.Loading
            btnRetry.isVisible = loadState is LoadState.Error

            btnRetry.setOnClickListener {
                onInteractionListener.onRetry()
            }
        }
    }
}