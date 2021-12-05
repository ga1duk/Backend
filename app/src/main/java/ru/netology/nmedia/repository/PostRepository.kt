package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: GetAllCallback)
    fun likeByIdAsync(id: Long, callback: LikeCallback)
    fun dislikeByIdAsync(id: Long, callback: LikeCallback)
    fun saveAsync(post: Post, callback: SaveCallback)
    fun removeByIdAsync(id: Long, callback: DeleteCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface LikeCallback {
        fun onSuccess(post : Post)
        fun onError(e: Exception)
    }

    interface SaveCallback {
        fun onSuccess()
        fun onError(e: Exception)
    }

    interface DeleteCallback {
        fun onSuccess()
        fun onError(e: Exception)
    }
}
