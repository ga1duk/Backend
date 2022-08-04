package ru.netology.nmedia.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onAttachmentClick(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener, ::getItem)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach {
                if (it is Payload) {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
    private val getPost: (position: Int) -> Post
) : RecyclerView.ViewHolder(binding.root) {

    private val BASE_URL = BuildConfig.BASE_URL

    fun bind(payload: Payload) {
        payload.likedByMe?.also { likedByMe ->
            binding.like.isChecked = likedByMe
            if (likedByMe) {
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
                ObjectAnimator.ofPropertyValuesHolder(binding.like, scaleX, scaleY).apply {
                    repeatCount = 1
                }
            } else {
                ObjectAnimator.ofFloat(binding.like, View.ROTATION, 0F, 360F)
            }.start()
        }
        payload.content?.also {
            binding.content.text = it
        }
        payload.likes?.also {
            binding.like.text = it.toString()
        }
    }

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.text = "${post.likes}"

            val urlAvatars = "${BASE_URL}/avatars/${post.authorAvatar}"
            Glide.with(binding.avatar)
                .load(urlAvatars)
                .circleCrop()
                .timeout(10_000)
                .into(binding.avatar)

            if (post.attachment?.url != null) {
                val urlImages = "${BASE_URL}/media/${post.attachment.url}"
                Glide.with(binding.attachment)
                    .load(urlImages)
                    .timeout(10_000)
                    .into(binding.attachment)
            }

            binding.attachment.setOnClickListener {
                onInteractionListener.onAttachmentClick(post)
            }

            menu.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(getPost(bindingAdapterPosition))
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Post, newItem: Post): Any =
        Payload(
            likedByMe = newItem.likedByMe.takeIf { it != oldItem.likedByMe },
            content = newItem.content.takeIf { it != oldItem.content },
            likes = newItem.likes.takeIf { it != oldItem.likes }
        )
}

data class Payload(
    val likedByMe: Boolean? = null,
    val content: String? = null,
    val likes: Int? = null
)
