package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: GetAllCallback)
    fun likeById(id: Long, callback: LikeCallback)
    fun dislikeById(id: Long): Post
    fun save(post: Post)
    fun removeById(id: Long)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface LikeCallback {
        fun onSuccess(post : Post)
        fun onError(e: Exception)
    }
}
